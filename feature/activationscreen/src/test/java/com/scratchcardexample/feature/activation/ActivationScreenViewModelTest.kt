package com.scratchcardexample.feature.activation

import com.scratchcardexample.core.domain.usecases.GetCodeFromStringUseCase
import com.scratchcardexample.core.domain.usecases.ReadCodeUseCase
import com.scratchcardexample.core.domain.usecases.SendCodeUseCase
import com.scratchcardexample.feature.activation.types.FakeScratchCodeRepository
import com.scratchcardexample.feature.activation.types.FakeSendCodeService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ActivationScreenViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun checkStartState() = runTest {
        val expected = 277028
        val repo = FakeScratchCodeRepository(expected.toString())
        val service = FakeSendCodeService()
        val sendCode = SendCodeUseCase(service)
        val readCode = ReadCodeUseCase(repo)
        val getCodeFromStringUseCase = GetCodeFromStringUseCase()
        val viewModel = ActivationScreenViewModel(
            sendCode,
            readCode,
            repo,
            getCodeFromStringUseCase
        )

        assertEquals(viewModel.state.value, ActivationState.Default)
    }
}