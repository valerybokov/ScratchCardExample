package com.scratchcardexample.core.data.types

import com.scratchcardexample.core.domain.repository.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSettings: Settings {
    var valueInt = 0
    var valueString: String = ""

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
        valueString = value
    }

    override suspend fun write(key: String, value: Int) {
        valueInt = value
    }
}