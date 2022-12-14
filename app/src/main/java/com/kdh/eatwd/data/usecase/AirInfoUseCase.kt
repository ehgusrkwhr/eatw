package com.kdh.eatwd.data.usecase

import com.kdh.eatwd.data.repository.AirInfoRepository
import com.kdh.eatwd.data.repository.WeatherInfoRepository
import javax.inject.Inject

class AirInfoUseCase @Inject constructor(
//    private val repository: AirInfoRepository
    private val repository: WeatherInfoRepository
) {
    suspend operator fun invoke(lat: Double, log: Double) = repository.getAirInfo(lat, log)
}