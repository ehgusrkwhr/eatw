package com.kdh.eatwd.data.repository

import android.util.Log
import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.kdh.eatwd.data.remote.*
import com.kdh.eatwd.data.util.StringUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherRepository {

    override suspend fun getWeatherInfo(
        lat: Double,
        log: Double
    ): Flow<ApiStates<WeatherInfoResponse>> {
        return flow {
            weatherService.getWeatherData(lat.toString(), log.toString(), WeatherService.API_KEY,"kr").apply {
                this.onSuccessSuspend {
                    data?.let { res ->
                        val localDate : LocalDate = LocalDate.now()
                        Log.d("dodo55 ","localDate : $localDate")
                        for(day in res.list){
                            Log.d("dodo55 ","day.dt_tx : ${day.dt_txt}")

                           //오늘날짜
                        }
                        emit(ApiStates.success(res))
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