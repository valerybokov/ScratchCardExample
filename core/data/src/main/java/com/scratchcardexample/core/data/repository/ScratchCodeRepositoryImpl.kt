package com.scratchcardexample.core.data.repository

import androidx.datastore.core.IOException
import com.scratchcardexample.core.domain.repository.ScratchCodeRepository
import com.scratchcardexample.core.domain.repository.Settings
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val ID_SCRATCH_CODE = "ID_SCRATCH_CODE"
private const val ID_CARD_ACTIVATED = "ID_CARD_ACTIVATED"

@Singleton
class ScratchCodeRepositoryImpl @Inject constructor(
    private val settings: Settings,
    scope: CoroutineScope,
): ScratchCodeRepository {
    private val _code = MutableStateFlow(null as String?)
    private val _isActivated = MutableStateFlow(false)

    /** Completes once the stored values have been read (or failed to be read). */
    private val loaded = CompletableDeferred<Unit>()

    init {
        scope.launch(Dispatchers.IO) {
            try {
                val code = settings.readString(ID_SCRATCH_CODE, "").first()

                // Don't overwrite a code saved while we were loading.
                if (code.isNotEmpty())
                    _code.compareAndSet(null, code)

                if (settings.readIntSuspend(ID_CARD_ACTIVATED, 0) == 1)
                    _isActivated.value = true
            }
            catch (x: IOException) {
                //todo analytics
            }
            finally {
                loaded.complete(Unit)
            }
        }
    }

    // Readers wait for the stored values, so nobody sees the empty
    // defaults and mistakes them for "card not scratched".
    override fun isCardActivated(): Flow<Boolean> = flow {
        loaded.await()
        emitAll(combine(_isActivated, _code) { isActivated, code ->
            isActivated && !code.isNullOrEmpty()
        })
    }

    override suspend fun setCardAsActivated() {
        if (_code.value != null) {
            _isActivated.value = true

            // As with the code, the activation stays in RAM
            // even if it can't be persisted.
            try {
                settings.write(ID_CARD_ACTIVATED, 1)
            }
            catch (x: IOException) {
                //todo analytics
            }
        }
    }

    override fun readCode(): Flow<String?> = flow {
        loaded.await()
        emitAll(_code)
    }

    override suspend fun saveCode(code: String) {
        if (code.isEmpty())
            throw IllegalArgumentException("code is empty")

        //We save the code in RAM.
        // Even if we can't save it to a flash drive,
        // the user will still be able to use it.
        _code.value = code

        try {
            settings.write(ID_SCRATCH_CODE, code)
        }
        catch (x: IOException) {
            //todo analytics
        }
    }
}