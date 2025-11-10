package com.scratchcardexample

import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainActivityViewModelTest {
    @Test
    fun check_Header_value() {
        val viewModel = MainActivityViewModel()
        viewModel.updateHeader(ROUTE_SCRATCH)
        assertEquals(
            viewModel.header.value,
            com.scratchcardexample.feature.scratchscreen.R.string.scratch)

        viewModel.updateHeader(ROUTE_START)
        assertEquals(
            viewModel.header.value,
            R.string.app_name)

        viewModel.updateHeader(ROUTE_ACTIVATION)
        assertEquals(
            viewModel.header.value,
            com.scratchcardexample.feature.activation.R.string.title_activation)
    }

    @Test
    fun check_showBackButton() {
        val viewModel = MainActivityViewModel()
        viewModel.updateHeader(ROUTE_SCRATCH)
        assertTrue(viewModel.showBackButton)

        viewModel.updateHeader(ROUTE_START)
        assertFalse(viewModel.showBackButton)

        viewModel.updateHeader(ROUTE_ACTIVATION)
        assertTrue(viewModel.showBackButton)
    }
}