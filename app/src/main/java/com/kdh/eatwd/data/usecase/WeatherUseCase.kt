package com.kdh.eatwd.data.usecase

import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.kdh.eatwd.data.remote.ApiStates
import com.kdh.eatwd.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend fun fetchWeatherInfo(lat: Double, lon: Double): Flow<ApiStates<WeatherInfoResponse>> {
        return repository.getWeatherInfo(lat, lon)
    }
}