package com.scratchcardexample.core.data.repository

import com.scratchcardexample.core.data.repository.interfaces.ScratchCodeRepository
import com.scratchcardexample.core.data.repository.interfaces.Settings
import com.scratchcardexample.core.network.base.NetworkResponse
import com.scratchcardexample.core.network.responses.SendCodeResponse
import javax.inject.Inject
import javax.inject.Singleton
import com.scratchcardexample.core.network.service.SendCodeService
import kotlinx.coroutines.flow.Flow

private const val ID_SCRATCH_CODE = "ID_SCRATCH_CODE"

@Singleton
class ScratchCodeRepositoryImpl @Inject constructor(
    private val settings: Settings,
    private val service: SendCodeService,
    ): ScratchCodeRepository {
    override suspend fun sendCode(code: String): NetworkResponse<SendCodeResponse, Int> {
        return service.send(code)
    }

    override suspend fun readCode(): Flow<String> =
        settings.readString(ID_SCRATCH_CODE, "")

    override suspend fun saveCode(code: String) {
        settings.write(ID_SCRATCH_CODE, code)
    }
}