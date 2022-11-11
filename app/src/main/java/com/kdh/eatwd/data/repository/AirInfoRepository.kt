package com.kdh.eatwd.data.repository

import com.kdh.eatwd.data.entity.AirStatusResponse
import com.kdh.eatwd.data.remote.ApiStates
import kotlinx.coroutines.flow.Flow

interface AirInfoRepository {

    suspend fun getAirInfo(lat : String,log :String) : Flow<ApiStates<AirStatusResponse>>
//suspend fun getAirInfo(lat : String,log :String) : ApiStates<AirStatusResponse>
}