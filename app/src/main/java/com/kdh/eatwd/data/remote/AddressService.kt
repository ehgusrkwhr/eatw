package com.kdh.eatwd.data.remote

import com.kdh.eatwd.data.entity.AddressResponse
import com.kdh.eatwd.data.entity.AirStatusResponse
import com.wajahatkarim3.imagine.data.remote.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface AddressService {

    companion object {
        const val BASE_URL = "https://business.juso.go.kr/addrlink/"
        const val API_KEY = "devU01TX0FVVEgyMDIyMTIwMjEyMTkyNjExMzI4ODU="
    }

//    @GET("nearest_city")
//    fun getAirQualityData(@Query("lat") lat : String, @Query("lon") lon : String, @Query("key") key : String ) : ApiResponse<AirStatusResponse>

    @GET("addrLinkApi.do")
    suspend fun getAddressData(
        @Query("keyword") keyword: String,
        @Query("resultType") resultType: String,
        @Query("confmKey") key: String
    ): ApiResponse<AddressResponse>


}