package com.kdh.eatwd.presenter.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdh.eatwd.data.entity.AirStatusResponse
import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.kdh.eatwd.data.remote.ApiStates
import com.kdh.eatwd.data.usecase.AirInfoUseCase
import com.kdh.eatwd.data.usecase.WeatherUseCase
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

    private var _weatherInfo: MutableStateFlow<WeatherInfoResponse?> = MutableStateFlow(null)
    val weatherInfo = _weatherInfo.asStateFlow()

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
        viewModelScope.launch {
            weatherUseCase.fetchWeatherInfo(lat, log).collect { apiState ->
                when (apiState) {
                    is ApiStates.Success -> {
//                        _weatherInfo.value = apiState.data.list
                        Log.d(TAG, "apiState.data.list : ${apiState.data.list}");
                    }
                    is ApiStates.Error -> {
                        Log.d(TAG, "fetchWeatherInfo 에러입니다")
                    }
                }
            }
        }
    }


    companion object {
        private const val TAG = "dodo55"
    }
}