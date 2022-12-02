package com.kdh.eatwd.data.datasource


import com.kdh.eatwd.data.entity.AirStatusResponse
import com.kdh.eatwd.data.remote.*
import com.kdh.eatwd.data.util.StringUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class AirInfoDataSourceImpl @Inject constructor(
    private val airStatusService: AirStatusService
) : AirInfoDataSource {

    override suspend fun getAirInfo(lat: Double, log: Double): Flow<ApiStates<AirStatusResponse>> {
        return flow {
            airStatusService.getAirQualityData(
                lat.toString(),
                log.toString(),
                AirStatusService.API_KEY
            ).apply {
                this.onSuccessSuspend {
                    data?.let {
                        emit(ApiStates.success(it))
                    }
                }
            }.onErrorSuspend {
                emit(ApiStates.error(message()))
            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(ApiStates.error(StringUtils.networkErrorMessage()))
                } else {
                    emit(ApiStates.error(StringUtils.etcError()))
                }
            }
        }
    }


}