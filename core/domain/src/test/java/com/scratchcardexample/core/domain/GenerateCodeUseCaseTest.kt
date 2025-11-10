package com.scratchcardexample.core.domain

import com.scratchcardexample.core.domain.usecases.GenerateCodeUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
class GenerateCodeUseCaseTest {
    @Test
    fun generate_code() = runTest {
        val result = GenerateCodeUseCase()()

        assertFalse(result.isNullOrEmpty())
    }
}