package com.kdh.eatwd.data.repository

import com.kdh.eatwd.data.entity.AirStatusResponse
import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.kdh.eatwd.data.remote.ApiStates
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherInfo(lat : Double,log :Double) : Flow<ApiStates<WeatherInfoResponse>>
}