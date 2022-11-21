package com.kdh.eatwd.data.usecase

import com.kdh.eatwd.data.repository.WeatherRepository
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    //    suspend fun fetchWeatherInfo(lat: Double, lon: Double): Flow<ApiStates<WeatherInfoResponse>> = repository.getWeatherInfo(lat, lon)
    suspend fun fetchWeatherInfo(lat: Double, lon: Double) = repository.getWeatherInfo(lat, lon)
}