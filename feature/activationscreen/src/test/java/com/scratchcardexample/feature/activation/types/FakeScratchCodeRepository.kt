package com.scratchcardexample.feature.activation.types

import com.scratchcardexample.core.domain.repository.ScratchCodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeScratchCodeRepository(
    private val code: String?): ScratchCodeRepository {

    var isActivated = false
        private set

    override fun isCardActivated(): Flow<Boolean> =
        flow { emit(isActivated) }

    override suspend fun setCardAsActivated() {
        isActivated = true
    }

    override fun readCode(): Flow<String?> =
        flow { emit(code) }

    override suspend fun saveCode(code: String) {
        TODO("Not yet implemented")
    }
}
