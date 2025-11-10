package com.scratchcardexample.feature.activation.types

import com.scratchcardexample.core.network.base.NetworkResponse
import com.scratchcardexample.core.network.responses.SendCodeResponse
import com.scratchcardexample.core.network.service.SendCodeService

class FakeSendCodeService: SendCodeService {
    override suspend fun send(code: String): NetworkResponse<SendCodeResponse, Int> {
        return NetworkResponse.Success(SendCodeResponse("123"))
    }
}