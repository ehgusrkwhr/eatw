package com.kdh.eatwd.presenter.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdh.eatwd.data.entity.AirStatusResponse
import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.kdh.eatwd.data.remote.ApiStates
import com.kdh.eatwd.data.usecase.AirInfoUseCase
import com.kdh.eatwd.data.usecase.WeatherUseCase
import com.kdh.eatwd.presenter.util.Constants.AFTER_TOMORROW
import com.kdh.eatwd.presenter.util.Constants.TOMORROW
import com.kdh.eatwd.presenter.util.getDayOfWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val airInfoUseCase: AirInfoUseCase,
    private val weatherUseCase: WeatherUseCase
) : ViewModel() {
    private var _airInfo: MutableStateFlow<AirStatusResponse.Data.Current.Pollution?> =
        MutableStateFlow(null)
    val airInfo = _airInfo.asStateFlow()

    private var _weatherInfo: MutableStateFlow<List<WeatherInfoResponse.WeatherDetail?>?> =
        MutableStateFlow(null)
    val weatherInfo = _weatherInfo.asStateFlow()

    private var _weatherShortInfo: MutableStateFlow<List<WeatherInfoResponse.WeatherDetail?>?> =
        MutableStateFlow(null)
    val weatherShortInfo = _weatherShortInfo.asStateFlow()

    private val _loadingFlag: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val loadingFlag = _loadingFlag.asStateFlow()

    var scrollPositionTitle: MutableStateFlow<String?> = MutableStateFlow(null)

    private var dayCount = 0
    private var tempDay = ""
    fun getAirInfo(lat: Double, log: Double) {
        viewModelScope.launch {
            airInfoUseCase(lat, log).collect { apiState ->
                when (apiState) {
                    is ApiStates.Success -> {
                        _airInfo.value = apiState.data.data.current.pollution
                    }
                    is ApiStates.Error -> {
                        Log.d(TAG, "getAirInfo 에러입니다");
                    }
                }
            }
        }
    }

    fun fetchWeatherInfo(lat: Double, log: Double) {
        _loadingFlag.value = true
        viewModelScope.launch {
            weatherUseCase.fetchWeatherInfo(lat, log).collect { apiState ->
                when (apiState) {
                    is ApiStates.Success -> {
                        _weatherInfo.value = apiState.data.list
                        _weatherShortInfo.value = weatherItemDivideInfo(apiState.data.list)

                        _loadingFlag.value = false
                        Log.d(TAG, "apiState.data.list : ${apiState.data.list}");
                    }
                    is ApiStates.Error -> {
                        Log.d(TAG, "fetchWeatherInfo 에러입니다")
                    }
                }
            }
        }
    }

    private fun weatherItemDivideInfo(list: List<WeatherInfoResponse.WeatherDetail>): List<WeatherInfoResponse.WeatherDetail> {
        return list.filterIndexed { index, it ->
            val checkDay = getDayOfWeek(it.dt_txt.split(" ")[0])
            if (index == 0) {
                tempDay = checkDay
            }
            if (tempDay == checkDay && dayCount <= 2) {
                true
            } else if (dayCount <= 2) {
                when (dayCount) {
                    0 -> it.day_flag = TOMORROW
                    1 -> it.day_flag = AFTER_TOMORROW
                }
                tempDay = checkDay
                dayCount++
                true
            } else {
                false
            }

        }
    }


    companion object {
        private const val TAG = "dodo55"
    }
}