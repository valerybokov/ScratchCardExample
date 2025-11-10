package com.scratchcardexample.core.domain

import com.scratchcardexample.core.domain.types.FakeScratchCodeRepository
import com.scratchcardexample.core.domain.usecases.ReadCodeUseCase
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlinx.coroutines.test.runTest

class ReadCodeUseCaseTest {
    @Test
    fun read_null() = runTest {
        val repo = FakeScratchCodeRepository(null)
        val result = ReadCodeUseCase(repo)()

        assertEquals(result, null)
    }

    @Test
    fun read_empty() = runTest {
        val expected = ""
        val repo = FakeScratchCodeRepository(expected)
        val result = ReadCodeUseCase(repo)()

        assertEquals(result, expected)
    }

    @Test
    fun read_not_empty() = runTest {
        val expected = "23234"
        val repo = FakeScratchCodeRepository(expected)
        val result = ReadCodeUseCase(repo)()

        assertEquals(result, expected)
    }
}