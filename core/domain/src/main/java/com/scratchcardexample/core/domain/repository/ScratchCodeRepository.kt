package com.scratchcardexample.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface ScratchCodeRepository {
    fun isCardActivated(): Flow<Boolean>

    suspend fun setCardAsActivated()

    fun readCode(): Flow<String?>

    suspend fun saveCode(code: String)
}