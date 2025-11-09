package com.scratchcardexample.core.network.service

import com.scratchcardexample.core.network.responses.SendCodeResponse
import com.scratchcardexample.core.network.base.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SendCodeService {
    @GET("version")
    suspend fun send(@Path("code") code: String): NetworkResponse<SendCodeResponse, Int>
}