package com.scratchcardexample.core.network

import com.scratchcardexample.core.network.base.NetworkResponse
import com.scratchcardexample.core.network.base.NetworkResponseCall
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response

class NetworkResponseCallTest {
    /** Delegate call that immediately answers with the given response. */
    private class FakeCall(private val response: Response<String>) : Call<String> {
        override fun enqueue(callback: Callback<String>) = callback.onResponse(this, response)
        override fun execute(): Response<String> = response
        override fun isExecuted() = true
        override fun cancel() {}
        override fun isCanceled() = false
        override fun clone(): Call<String> = FakeCall(response)
        override fun request(): Request = Request.Builder().url("https://example.com").build()
        override fun timeout(): Timeout = Timeout.NONE
    }

    /** Behaves like Moshi converting an Int from a body that isn't a number. */
    private val intConverter = Converter<ResponseBody, Int> { it.string().trim().toInt() }

    private fun enqueue(response: Response<String>): NetworkResponse<String, Int> {
        var result: NetworkResponse<String, Int>? = null
        NetworkResponseCall(FakeCall(response), intConverter).enqueue(
            object : Callback<NetworkResponse<String, Int>> {
                override fun onResponse(
                    call: Call<NetworkResponse<String, Int>>,
                    response: Response<NetworkResponse<String, Int>>
                ) {
                    result = response.body()
                }

                override fun onFailure(call: Call<NetworkResponse<String, Int>>, t: Throwable) {}
            }
        )
        return result!!
    }

    private fun errorResponse(code: Int, body: String): Response<String> =
        Response.error(code, body.toResponseBody("application/json".toMediaType()))

    @Test
    fun unparsableErrorBodyIsApiErrorWithCode() {
        val result = enqueue(errorResponse(404, """{"message":"not found"}"""))

        assertTrue(result is NetworkResponse.ApiError)
        result as NetworkResponse.ApiError
        assertEquals(404, result.code)
        assertNull(result.body)
    }

    @Test
    fun emptyErrorBodyIsApiErrorWithCode() {
        val result = enqueue(errorResponse(500, ""))

        assertTrue(result is NetworkResponse.ApiError)
        assertEquals(500, (result as NetworkResponse.ApiError).code)
    }

    @Test
    fun parsableErrorBodyIsKept() {
        val result = enqueue(errorResponse(400, "42"))

        result as NetworkResponse.ApiError
        assertEquals(400, result.code)
        assertEquals(42, result.body)
    }

    @Test
    fun successBodyIsSuccess() {
        val result = enqueue(Response.success("ok"))

        assertEquals(NetworkResponse.Success("ok"), result)
    }
}
