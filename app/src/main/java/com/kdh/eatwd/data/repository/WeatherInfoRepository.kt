package com.kdh.eatwd.data.repository

import com.kdh.eatwd.data.datasource.AddressDataSource
import com.kdh.eatwd.data.datasource.AirInfoDataSource
import com.kdh.eatwd.data.datasource.WeatherDataSource
import javax.inject.Inject

class WeatherInfoRepository @Inject constructor(
    private val airInfoDataSource: AirInfoDataSource,
    private val weatherDataSource: WeatherDataSource,
    private val addressDataSource: AddressDataSource
) {

    suspend fun getAirInfo(lat: Double, log: Double) = airInfoDataSource.getAirInfo(lat,log)
    suspend fun getWeatherInfo(lat: Double, log: Double) = weatherDataSource.getWeatherInfo(lat,log)
    suspend fun getSearchAddressInfo(keyword : String) = addressDataSource.getSearchAddressInfo(keyword)

}