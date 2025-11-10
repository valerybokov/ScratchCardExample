package com.scratchcardexample.feature.startscreen.types

import com.scratchcardexample.core.domain.repository.ScratchCodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FakeScratchCodeRepository(
    private var code: String?): ScratchCodeRepository {
    private val _isActivated = MutableStateFlow(false)

    override fun isCardActivated(): Flow<Boolean> = _isActivated.map {
        it && !code.isNullOrEmpty()
    }

    override suspend fun setCardAsActivated() {
        _isActivated.value = true
    }

    override fun readCode(): Flow<String?> =
        flow { emit(code) }

    override suspend fun saveCode(code: String) {
        this.code = code
    }
}