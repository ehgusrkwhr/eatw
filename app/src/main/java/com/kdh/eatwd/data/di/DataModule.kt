package com.kdh.eatwd.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kdh.eatwd.data.remote.AddressService
import com.kdh.eatwd.data.remote.AirStatusService
import com.kdh.eatwd.data.remote.WeatherService
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
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Type1

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Type2

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Type3



    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient{
        val okhttpClient = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okhttpClient.addInterceptor(loggingInterceptor)
        return okhttpClient.build()
    }

    @Singleton
    @Provides
    @Type1
    fun provideAirStatusRetrofitClient(): Retrofit {
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
    @Type2
    fun provideWeatherInfoRetrofitClient(): Retrofit {
        val okhttpClient = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okhttpClient.addInterceptor(loggingInterceptor)

        return Retrofit.Builder()
            .baseUrl(WeatherService.BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    @Type3
    fun provideAddressRetrofitClient(): Retrofit {
        val okhttpClient = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okhttpClient.addInterceptor(loggingInterceptor)

        return Retrofit.Builder()
            .baseUrl(AddressService.BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideAirApiService(@Type1 retrofit: Retrofit): AirStatusService {
        return retrofit.create(AirStatusService::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherApiService(@Type2 retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Singleton
    @Provides
    fun provideAddressService(@Type3 retrofit: Retrofit): AddressService {
        return retrofit.create(AddressService::class.java)
    }

}