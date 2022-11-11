package com.kdh.eatwd.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kdh.eatwd.data.remote.AirStatusService
import com.wajahatkarim3.imagine.data.remote.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideRetrofitClient(): Retrofit {
        val okhttpClient = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okhttpClient.addInterceptor(loggingInterceptor)

        return Retrofit.Builder()
            .baseUrl(AirStatusService.BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideAirApiService(retrofit: Retrofit): AirStatusService {
        return retrofit.create(AirStatusService::class.java)
    }

}