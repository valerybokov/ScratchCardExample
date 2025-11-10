package com.scratchcardexample.core.data

import com.scratchcardexample.core.data.repository.ScratchCodeRepositoryImpl
import com.scratchcardexample.core.data.types.FakeSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ScratchCodeRepositoryImplTest {
    @Test
    fun testIsActivated() = runTest {
        val settings = FakeSettings()
        val scope = CoroutineScope(Dispatchers.IO)
        val repo = ScratchCodeRepositoryImpl(settings, scope)

        assertFalse(repo.isCardActivated().first())
        assertEquals(repo.readCode().first(), null)

        repo.setCardAsActivated()
        assertFalse(repo.isCardActivated().first())

        repo.saveCode("1234")
        repo.setCardAsActivated()
        val isActivated = repo.isCardActivated().first()
        assertEquals("isActivated " + isActivated, isActivated, true)
    }

    @Test
    fun testSaveValidCode() = runTest {
        val expected = "1234"
        val settings = FakeSettings()
        val scope = CoroutineScope(Dispatchers.IO)
        val repo = ScratchCodeRepositoryImpl(settings, scope)

        repo.saveCode(expected)

        assertEquals(repo.readCode().first(), expected)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEmptyCode() = runTest {
        val expected = ""
        val settings = FakeSettings()
        val scope = CoroutineScope(Dispatchers.IO)
        val repo = ScratchCodeRepositoryImpl(settings, scope)

        repo.saveCode(expected)
    }
}