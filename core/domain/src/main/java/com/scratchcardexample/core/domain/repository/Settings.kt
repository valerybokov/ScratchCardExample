package com.scratchcardexample.core.domain.repository

import kotlinx.coroutines.flow.Flow
import androidx.datastore.core.IOException

interface Settings {
    @Throws(IOException::class)
    fun readString(key: String, defaultValue: String): Flow<String>

    @Throws(IOException::class)
    suspend fun readIntSuspend(key: String, defaultValue: Int): Int

    @Throws(IOException::class)
    fun readInt(key: String, defaultValue: Int): Flow<Int>

    @Throws(IOException::class)
    suspend fun write(key: String, value: String)

    @Throws(IOException::class)
    suspend fun write(key: String, value: Int)
}