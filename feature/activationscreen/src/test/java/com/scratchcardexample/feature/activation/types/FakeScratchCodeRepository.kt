package com.scratchcardexample.feature.activation.types

import com.scratchcardexample.core.domain.repository.ScratchCodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeScratchCodeRepository(
    private val code: String?): ScratchCodeRepository {

    override fun isCardActivated(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setCardAsActivated() {
        TODO("Not yet implemented")
    }

    override fun readCode(): Flow<String?> =
        flow { emit(code) }

    override suspend fun saveCode(code: String) {
        TODO("Not yet implemented")
    }
}