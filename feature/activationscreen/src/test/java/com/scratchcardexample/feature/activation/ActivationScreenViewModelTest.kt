package com.scratchcardexample.feature.activation

import androidx.lifecycle.viewModelScope
import com.scratchcardexample.core.domain.usecases.GetCodeFromStringUseCase
import com.scratchcardexample.core.domain.usecases.ReadCodeUseCase
import com.scratchcardexample.core.domain.usecases.SendCodeUseCase
import com.scratchcardexample.feature.activation.types.FakeScratchCodeRepository
import com.scratchcardexample.feature.activation.types.FakeSendCodeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActivationScreenViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun TestScope.createViewModel(
        repo: FakeScratchCodeRepository,
        service: FakeSendCodeService,
    ): ActivationScreenViewModel {
        // Stands in for the app-wide scope; shares the test scheduler.
        val appScope = CoroutineScope(SupervisorJob() + StandardTestDispatcher(testScheduler))
        return ActivationScreenViewModel(
            SendCodeUseCase(service),
            ReadCodeUseCase(repo),
            repo,
            GetCodeFromStringUseCase(),
            appScope,
        )
    }

    @Test
    fun checkStartState() = runTest(dispatcher) {
        val viewModel = createViewModel(
            FakeScratchCodeRepository("277028"), FakeSendCodeService())

        assertEquals(ActivationState.Default, viewModel.state.value)
    }

    @Test
    fun activationSucceeds() = runTest(dispatcher) {
        val repo = FakeScratchCodeRepository("some-code")
        val viewModel = createViewModel(repo, FakeSendCodeService(android = "287028"))

        viewModel.activate()
        advanceUntilIdle()

        assertTrue(repo.isActivated)
        val state = viewModel.state.value
        assertTrue(state is ActivationState.Success)
        assertEquals(287028, (state as ActivationState.Success).code)
    }

    @Test
    fun activationContinuesAfterScreenIsClosed() = runTest(dispatcher) {
        val repo = FakeScratchCodeRepository("some-code")
        val viewModel = createViewModel(
            repo, FakeSendCodeService(android = "287028", latency = 1_000))

        viewModel.activate()
        advanceTimeBy(500) // request is in flight
        assertFalse(repo.isActivated)

        // Closing the screen clears the ViewModel, which cancels its scope.
        viewModel.viewModelScope.cancel()
        advanceUntilIdle()

        assertTrue(repo.isActivated)
    }
}
