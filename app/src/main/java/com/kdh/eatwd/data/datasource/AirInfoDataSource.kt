package com.kdh.eatwd.data.datasource

import com.kdh.eatwd.data.entity.AirStatusResponse
import com.kdh.eatwd.data.remote.ApiStates
import kotlinx.coroutines.flow.Flow

interface AirInfoDataSource {

    suspend fun getAirInfo(lat : Double,log :Double) : Flow<ApiStates<AirStatusResponse>>

}