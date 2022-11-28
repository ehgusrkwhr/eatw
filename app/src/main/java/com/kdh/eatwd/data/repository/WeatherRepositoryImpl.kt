package com.kdh.eatwd.data.repository

import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.kdh.eatwd.data.remote.*
import com.kdh.eatwd.data.util.StringUtils
import com.kdh.eatwd.presenter.util.getAverage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherRepository {

    override suspend fun getWeatherInfo(
        lat: Double,
        log: Double
    ): Flow<ApiStates<WeatherInfoResponse>> {
        return flow {
            weatherService.getWeatherData(lat, log, WeatherService.API_KEY, "kr").apply {
                this.onSuccessSuspend {
                    data?.let { res ->
                        var tempDay = ""
                        var tempMinSum = 0.0
                        var tempMaxSum = 0.0
                        var dayCount = 0
                        var pairDayCount = 0
                        val tempPairList = mutableListOf<Pair<Double, Double>>()
                        val list = mutableListOf<WeatherInfoResponse.DayInfo>()
                        res.list.forEachIndexed { index, value ->
                            val resDate = value.dt_txt.split(" ")
                            if (index == 0) tempDay = resDate[0]
                            if ((tempDay.isEmpty() || tempDay != resDate[0]) && index != 0) {
                                tempDay = resDate[0]
                                tempPairList.add(
                                    pairDayCount++,
                                    Pair(
                                        getAverage(tempMinSum, dayCount),
                                        getAverage(tempMaxSum, dayCount)
                                    )
                                )
                                val daylist = WeatherInfoResponse.DayInfo(
                                    day = value.dt_txt,
                                    icon = value.weather[0].icon,
                                    tempMin = tempMaxSum,
                                    tempMax = tempMinSum,
                                )
                                list.add(daylist)
                                tempMinSum = 0.0
                                tempMaxSum = 0.0
                                dayCount = 0
                            } else {
                                tempMinSum += value.main.temp_min
                                tempMaxSum += value.main.temp_max
                                dayCount++
                            }
                        }
                        res.dayList = list

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