package com.scratchcardexample.feature.startscreen

import com.scratchcardexample.feature.startscreen.types.FakeScratchCodeRepository
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class StartScreenViewModelTest {
    @Test
    fun notActivated_null() {
        val repo = FakeScratchCodeRepository(null)
        val vm = StartScreenViewModel(repo)

        assertEquals(vm.scratchcardStateId.value, R.string.card_not_scratched)
    }

    @Test
    fun notActivated_not_null() {
        val repo = FakeScratchCodeRepository("2324")
        val vm = StartScreenViewModel(repo)

        assertEquals(vm.scratchcardStateId.value, R.string.card_not_scratched)
    }
}