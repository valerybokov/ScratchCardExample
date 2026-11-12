package com.scratchcardexample.feature.activation.types

import com.scratchcardexample.core.network.base.NetworkResponse
import com.scratchcardexample.core.network.responses.SendCodeResponse
import com.scratchcardexample.core.network.service.SendCodeService
import kotlinx.coroutines.delay

class FakeSendCodeService(
    private val android: String = "123",
    /** Simulated network latency in ms. */
    private val latency: Long = 0,
): SendCodeService {
    override suspend fun send(code: String): NetworkResponse<SendCodeResponse, Int> {
        delay(latency)
        return NetworkResponse.Success(SendCodeResponse(android))
    }
}
