package com.scratchcardexample.core.domain

import com.scratchcardexample.core.domain.usecases.CodeFromStringResult
import org.junit.Test
import org.junit.Assert.*
import com.scratchcardexample.core.domain.usecases.GetCodeFromStringUseCase
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GetCodeFromStringUseCaseTest {
    @Test
    fun invalid_format_1() {
        val result = GetCodeFromStringUseCase()(null, 123)

        assertEquals(result, CodeFromStringResult.InvalidFormat)
    }


    @Test
    fun invalid_format_2() {
        val result = GetCodeFromStringUseCase()("", 123)

        assertEquals(result, CodeFromStringResult.InvalidFormat)
    }

    @Test
    fun invalid_format_3() {
        val result = GetCodeFromStringUseCase()("fgdgd", 123)

        assertEquals(result, CodeFromStringResult.InvalidFormat)
    }

    @Test
    fun valid_format() {
        val result = GetCodeFromStringUseCase()("123", 3)

        assertTrue(result is CodeFromStringResult.Success)

        val code = (result as CodeFromStringResult.Success).value
        assertEquals(code, 123)
    }

    @Test
    fun valid_format_too_small() {
        val result = GetCodeFromStringUseCase()("123", 123)

        assertEquals(result, CodeFromStringResult.TooSmall)
    }
}