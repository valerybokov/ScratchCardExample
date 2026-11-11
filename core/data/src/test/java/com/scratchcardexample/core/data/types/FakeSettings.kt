package com.scratchcardexample.core.data.types

import com.scratchcardexample.core.domain.repository.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeSettings: Settings {
    var valueInt = 0
    var valueString: String = ""
    /** When true, every write fails as if the storage were unavailable. */
    var failWrites = false

    override fun readString(
        key: String,
        defaultValue: String,
    ): Flow<String> {
        return flow{ emit(valueString) }
    }

    override suspend fun readIntSuspend(key: String, defaultValue: Int): Int {
        return valueInt
    }

    override fun readInt(
        key: String,
        defaultValue: Int,
    ): Flow<Int> {
        return flow { emit(valueInt) }
    }

    override suspend fun write(key: String, value: String) {
        if (failWrites) throw IOException("write failed")
        valueString = value
    }

    override suspend fun write(key: String, value: Int) {
        if (failWrites) throw IOException("write failed")
        valueInt = value
    }
}