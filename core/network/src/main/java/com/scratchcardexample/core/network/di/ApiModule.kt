package com.scratchcardexample.core.network.di

import com.scratchcardexample.core.network.base.CallAdapterFactory
import com.scratchcardexample.core.network.service.SendCodeService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

private const val BASE_URL = "https://api.o2.sk"

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10_000, TimeUnit.MINUTES)
            .readTimeout(10_000, TimeUnit.MINUTES)

        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CallAdapterFactory())
            .client(okHttpClient)
            .build()
    }
    @Singleton
    @Provides
    fun provideSendCodeService(retrofit: Retrofit): SendCodeService =
        retrofit.create(SendCodeService::class.java)
}