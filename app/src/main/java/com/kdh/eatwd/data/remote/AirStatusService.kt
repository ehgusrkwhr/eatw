package com.kdh.eatwd.data.remote

import com.kdh.eatwd.data.entity.AirStatusResponse
import com.wajahatkarim3.imagine.data.remote.ApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface AirStatusService {

    companion object {
        const val BASE_URL = "https://api.airvisual.com/v2/"
        const val API_KEY = "28c2fc05-f7bf-4b5b-aa5a-495df4497f75"
    }

//    @GET("nearest_city")
//    fun getAirQualityData(@Query("lat") lat : String, @Query("lon") lon : String, @Query("key") key : String ) : ApiResponse<AirStatusResponse>

    @GET("nearest_city")
    suspend fun getAirQualityData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("key") key: String
    ): ApiResponse<AirStatusResponse>

    @GET("nearest_city")
    suspend fun getAirQualityDataTwo(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("key") key: String
    ): Call<AirStatusResponse>

    @GET("nearest_city")
    suspend fun getAirQualityDataThree(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("key") key: String
    ): Response<AirStatusResponse>


}