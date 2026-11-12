package com.scratchcardexample.core.network

import com.scratchcardexample.core.network.base.NetworkResponse
import com.scratchcardexample.core.network.di.ApiModule
import com.scratchcardexample.core.network.responses.SendCodeResponse
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test

class SendCodeServiceTest {
    @Test
    fun sendsCodeAsQueryParameter() = runTest {
        var request: Request? = null
        // Records the request and answers without touching the network.
        val fakeServer = Interceptor { chain ->
            request = chain.request()
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body("""{"android":"287028"}""".toResponseBody("application/json".toMediaType()))
                .build()
        }
        val client = OkHttpClient.Builder().addInterceptor(fakeServer).build()
        val service = ApiModule.provideSendCodeService(ApiModule.provideRetrofit(client))

        val response = service.send("abc-123")

        assertEquals("GET", request!!.method)
        assertEquals("https://api.o2.sk/version?code=abc-123", request!!.url.toString())
        assertEquals(NetworkResponse.Success(SendCodeResponse("287028")), response)
    }
}
