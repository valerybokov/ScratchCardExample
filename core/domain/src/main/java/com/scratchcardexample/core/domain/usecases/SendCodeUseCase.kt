package com.scratchcardexample.core.domain.usecases

import com.scratchcardexample.core.network.base.NetworkResponse
import com.scratchcardexample.core.network.responses.SendCodeResponse
import com.scratchcardexample.core.network.service.SendCodeService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SendCodeUseCase @Inject constructor(
    private val service: SendCodeService,
) {
    suspend operator fun invoke(code: String): SendCodeResult {
        if (code.isNullOrEmpty())
            throw IllegalArgumentException("code is null or empty")

        val response = service.send(code)
        //todo analytics
        return when(response) {
            is NetworkResponse.Success<*> -> {
                val body = (response as NetworkResponse.Success<SendCodeResponse>).body

                return SendCodeResult.Success(body.android)
            }
            is NetworkResponse.NetworkError -> SendCodeResult.NetworkError
            is NetworkResponse.ApiError<*> -> SendCodeResult.ApiError(response.code)
            is NetworkResponse.UnknownError -> SendCodeResult.UnknownError
        }
    }
}

sealed interface SendCodeResult {
    class Success(val value: String?): SendCodeResult

    object UnknownError: SendCodeResult
    class ApiError(val code: Int) : SendCodeResult
    object NetworkError: SendCodeResult
}