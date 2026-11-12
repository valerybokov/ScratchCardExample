package com.scratchcardexample.core.network.service

import com.scratchcardexample.core.network.responses.SendCodeResponse
import com.scratchcardexample.core.network.base.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SendCodeService {
    /** GET https://api.o2.sk/version?code={code} */
    @GET("version")
    suspend fun send(@Query("code") code: String): NetworkResponse<SendCodeResponse, Int>
}
