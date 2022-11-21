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
        Log.d("dodo55 ","lat $lat" )
        Log.d("dodo55 ","log $log" )
        return flow {
            weatherService.getWeatherData(lat, log, WeatherService.API_KEY,"kr").apply {
                this.onSuccessSuspend {
                    Log.d("dodo55 ","getWeatherInfo onSuccessSuspend" )
                    data?.let { res ->
                        Log.d("dodo55 ","getWeatherInfo onSuccessSuspend22222" )
//                        val localDate : LocalDate = LocalDate.now()
//                        Log.d("dodo55 ","localDate : $localDate")
//                        for(day in res.list){
//                            Log.d("dodo55 ","day.dt_tx : ${day.dt_txt}")
//
//                           //오늘날짜
//                        }
                        Log.d("dodo55 ","getWeatherInfo onSuccessSuspend33333" )
                        emit(ApiStates.success(res))
                    }
                }
            }.onErrorSuspend {
                Log.d("dodo55 ","getWeatherInfo onErrorSuspend" )
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