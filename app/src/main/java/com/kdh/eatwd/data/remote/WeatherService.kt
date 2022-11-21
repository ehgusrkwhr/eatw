package com.kdh.eatwd.data.remote

import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.wajahatkarim3.imagine.data.remote.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY = "81fe5e546e1236cabea9635bb65ee8f2"
    }

//    @GET("nearest_city")
//    fun getAirQualityData(@Query("lat") lat : String, @Query("lon") lon : String, @Query("key") key : String ) : ApiResponse<AirStatusResponse>

//    @GET("forecast")
//    suspend fun getWeatherData(
//        @Query("lat") lat: String,
//        @Query("lon") lon: String,
//        @Query("appid") key: String,
//        @Query("lang") lang: String
//    ): ApiResponse<WeatherInfoResponse>

    @GET("forecast")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") key: String,
        @Query("lang") lang: String
    ): ApiResponse<WeatherInfoResponse>


}