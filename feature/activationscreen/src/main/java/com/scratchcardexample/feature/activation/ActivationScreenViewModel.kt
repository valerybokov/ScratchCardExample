package com.scratchcardexample.feature.activation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scratchcardexample.core.domain.repository.ScratchCodeRepository
import com.scratchcardexample.core.domain.usecases.CodeFromStringResult
import com.scratchcardexample.core.domain.usecases.GetCodeFromStringUseCase
import com.scratchcardexample.core.domain.usecases.ReadCodeUseCase
import com.scratchcardexample.core.domain.usecases.SendCodeResult
import com.scratchcardexample.core.domain.usecases.SendCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivationScreenViewModel @Inject constructor(
    private val sendCode: SendCodeUseCase,
    private val readCode: ReadCodeUseCase,
    private val repo: ScratchCodeRepository,
    private val getCodeFromString: GetCodeFromStringUseCase,
    private val appScope: CoroutineScope,
): ViewModel() {
    private val _state = mutableStateOf<ActivationState>(ActivationState.Default)

    val state: State<ActivationState> = _state

    fun reset() {
        _state.value = ActivationState.Default
    }

    fun activate() {
        if (_state.value != ActivationState.Default)
            return

        _state.value = ActivationState.ReadingTheCode

        // The activation must not be cancelled when the user closes the screen,
        // so it runs in the app-wide scope. The screen only waits for the result:
        // leaving it cancels the waiting, not the activation itself.
        val activation = appScope.async { runActivation() }

        viewModelScope.launch {
            _state.value = activation.await()
        }
    }

    private suspend fun runActivation(): ActivationState {
        val code: String? = readCode()

        if (code.isNullOrEmpty())
            return ActivationState.Error(R.string.error_no_code)

        return when (val sendCodeResult = sendCode(code)) {
            is SendCodeResult.Success -> getCodeFromString(sendCodeResult.value)
            is SendCodeResult.ApiError -> ActivationState.Error(R.string.error_api)
            SendCodeResult.NetworkError -> ActivationState.Error(R.string.error_no_internet)
            is SendCodeResult.UnknownError -> ActivationState.Error(R.string.error_unknown)
        }
    }

    private suspend fun getCodeFromString(code: String?): ActivationState {
        val codeFromStringResult = getCodeFromString(code, 277028)
        return when (codeFromStringResult) {
            is CodeFromStringResult.Success -> {
                repo.setCardAsActivated()
                ActivationState.Success(codeFromStringResult.value)
            }
            is CodeFromStringResult.TooSmall -> ActivationState.Error(R.string.error_number_is_small)
            is CodeFromStringResult.InvalidFormat -> ActivationState.Error(R.string.error_number_has_invalid_format)
        }
    }
}

sealed interface ActivationState {
    object Default: ActivationState
    object ReadingTheCode: ActivationState
    class Success(val code: Int): ActivationState
    class Error(val value: Int): ActivationState
}