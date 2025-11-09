package com.scratchcardexample.core.data.repository.interfaces

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import androidx.datastore.core.IOException

interface Settings {
    fun raw(): Flow<Preferences>

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