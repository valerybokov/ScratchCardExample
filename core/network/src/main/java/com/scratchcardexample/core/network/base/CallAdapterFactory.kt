package com.scratchcardexample.core.network.base

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class CallAdapterFactory: CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // suspend functions wrap the response type in `Call`
        if (Call::class.java == getRawType(returnType)) {
            // check first that the return type is `ParameterizedType`
            if (returnType is ParameterizedType) {
                // get the response type inside the `Call` type
                val responseType = getParameterUpperBound(0, returnType)
                // if the response type not ApiResponse then we can't handle this type, so we return null
                if (getRawType(responseType) == NetworkResponse::class.java) {
                    // the response type is ApiResponse and should be parameterized
                    check(responseType is ParameterizedType) {
                        "Response must be parameterized as NetworkResponse<Foo> or NetworkResponse<out Foo>"
                    }

                    val successBodyType = getParameterUpperBound(0, responseType)
                    val errorBodyType = getParameterUpperBound(1, responseType)

                    val errorBodyConverter =
                        retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)

                    return NetworkResponseAdapter<Any, Any>(successBodyType, errorBodyConverter)
                }
                //else if the response type is not ApiResponse then we can't handle this type, so we return null
            }
            //else                 "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
        }

        return null
    }
}

class NetworkResponseAdapter<T_Body: Any, T_Error: Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, T_Error>
): CallAdapter<T_Body, Call<NetworkResponse<T_Body, T_Error>>> {
    override fun responseType(): Type {
        return successType
    }

    override fun adapt(call: Call<T_Body>): Call<NetworkResponse<T_Body, T_Error>> {
        return NetworkResponseCall(call, errorBodyConverter)
    }
}