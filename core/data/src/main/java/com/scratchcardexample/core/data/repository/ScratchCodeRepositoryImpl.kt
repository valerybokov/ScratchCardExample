package com.scratchcardexample.core.data.repository

import androidx.datastore.core.IOException
import com.scratchcardexample.core.domain.repository.ScratchCodeRepository
import com.scratchcardexample.core.domain.repository.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

    init {
        scope.launch(Dispatchers.IO) {
            try {
                val code = settings.readString(ID_SCRATCH_CODE, "").first()

                if (code.isNotEmpty())
                    _code.value = code

                _isActivated.value = settings.readIntSuspend(ID_CARD_ACTIVATED, 0) == 1
            }
            catch (x: IOException) {
                //todo analytics
            }
        }
    }

    override fun isCardActivated(): Flow<Boolean> = _isActivated.map {
        it && !_code.value.isNullOrEmpty()
    }

    override suspend fun setCardAsActivated() {
        if (_code.value != null) {
            _isActivated.value = true

            settings.write(ID_CARD_ACTIVATED, 1)
        }
    }

    override fun readCode(): Flow<String?> = _code.asStateFlow()

    override suspend fun saveCode(code: String) {
        //We save the code in RAM.
        // Even if we can't save it to a flash drive,
        // the user will still be able to use it.
        _code.value = code

        settings.write(ID_SCRATCH_CODE, code)
    }
}