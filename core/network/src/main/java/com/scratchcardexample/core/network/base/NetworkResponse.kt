package com.scratchcardexample.core.network.base

sealed interface NetworkResponse<out T_Body : Any, out T_Error : Any> {
    val isSuccess: Boolean

    /**
     * Success response with body
     */
    data class Success<T_Body : Any>(
        val body: T_Body,
        override val isSuccess: Boolean = true) : NetworkResponse<T_Body, Nothing>

    /**
     * Failure response with body
     */
    data class ApiError<T_Error : Any>(
        val body: T_Error,
        val code: Int,
        override val isSuccess: Boolean = false) : NetworkResponse<Nothing, T_Error>

    /**
     * Network error
     */
    data class NetworkError(
        val error: Throwable?,
        override val isSuccess: Boolean = false) : NetworkResponse<Nothing, Nothing>

    /**
     * For example, json parsing error
     */
    data class UnknownError(
        val error: Throwable?,
        override val isSuccess: Boolean = false) : NetworkResponse<Nothing, Nothing>
}