package com.scratchcardexample.core.network.base

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

internal class NetworkResponseCall<T_Body : Any, T_Error : Any>(
    private val delegate: Call<T_Body>,
    private val errorConverter: Converter<ResponseBody, T_Error>,
) : Call<NetworkResponse<T_Body, T_Error>> {

    override fun enqueue(callback: Callback<NetworkResponse<T_Body, T_Error>>) {
        return delegate.enqueue(object : Callback<T_Body> {
            override fun onResponse(call: Call<T_Body>, response: Response<T_Body>) {
                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Success(body))
                        )
                    } else {
                        // Response is successful but the body is null
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.UnknownError(null))
                        )
                    }
                } else {
                    val error = response.errorBody()

                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    if (errorBody != null) {
                        val code = response.code()
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.ApiError(errorBody, code))
                        )
                    } else {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.UnknownError(null))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<T_Body>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is HttpException -> NetworkResponse.NetworkError(throwable)
                    is IOException -> NetworkResponse.NetworkError(throwable)
                    else -> NetworkResponse.UnknownError(throwable)
                }
                callback.onResponse(
                    this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<T_Body, T_Error>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout {
        return Timeout().timeout(10, TimeUnit.MINUTES)
    }
}