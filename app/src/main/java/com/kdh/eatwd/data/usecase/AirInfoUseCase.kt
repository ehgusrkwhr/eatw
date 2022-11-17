package com.kdh.eatwd.data.usecase

import com.kdh.eatwd.data.repository.AirInfoRepository
import javax.inject.Inject

class AirInfoUseCase @Inject constructor(
    private val repository: AirInfoRepository
) {
    suspend operator fun invoke(lat: Double, log: Double) =
        repository.getAirInfo(lat, log)
}