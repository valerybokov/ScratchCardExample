package com.scratchcardexample.core.domain.usecases

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetCodeFromStringUseCase @Inject constructor() {
    /** Attempts to convert the parameter to an integer.
     * If the value is greater than minValue,
     * returns that integer wrapped on CodeFromStringResult.Success type.
     * Otherwise, returns some instance of CodeFromStringResult.
     * */
    operator fun invoke(code: String?, minValue: Int): CodeFromStringResult {
        if (code.isNullOrEmpty())
            return CodeFromStringResult.InvalidFormat

        var codeAsInt: Int?
        try {
            codeAsInt = code.toInt()
        }
        catch (x: NumberFormatException) {
            return CodeFromStringResult.InvalidFormat
        }

        return if (codeAsInt > minValue)
            CodeFromStringResult.Success(codeAsInt)
        else
            CodeFromStringResult.TooSmall
    }
}

sealed interface CodeFromStringResult {
    object InvalidFormat: CodeFromStringResult
    object TooSmall: CodeFromStringResult
    class Success(val value: Int): CodeFromStringResult
}