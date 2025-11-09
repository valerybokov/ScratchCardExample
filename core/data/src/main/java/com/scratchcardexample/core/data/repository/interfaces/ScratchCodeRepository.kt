package com.scratchcardexample.core.data.repository.interfaces

import com.scratchcardexample.core.network.base.NetworkResponse
import com.scratchcardexample.core.network.responses.SendCodeResponse
import kotlinx.coroutines.flow.Flow

interface ScratchCodeRepository {
    suspend fun sendCode(code: String): NetworkResponse<SendCodeResponse, Int>

    suspend fun readCode(): Flow<String>

    suspend fun saveCode(code: String)
}