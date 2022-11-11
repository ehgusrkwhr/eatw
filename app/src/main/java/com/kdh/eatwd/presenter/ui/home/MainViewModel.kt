package com.kdh.eatwd.presenter.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdh.eatwd.data.entity.AirStatusResponse
import com.kdh.eatwd.data.remote.AirStatusService
import com.kdh.eatwd.data.remote.ApiStates
import com.kdh.eatwd.data.usecase.AirInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val airInfoUseCase: AirInfoUseCase,
    private val airStatusService: AirStatusService
) : ViewModel() {
    private var _airInfo : MutableStateFlow<AirStatusResponse.Data.Current.Pollution?> = MutableStateFlow(null)
    val airInfo = _airInfo.asStateFlow()

    fun getAirInfo(lat: String, log: String) {
        viewModelScope.launch {

            airInfoUseCase(lat, log).collect{ apiState ->
                when(apiState){
                    is ApiStates.Success -> {
                        _airInfo.value = apiState.data.data.current.pollution
                        Log.d("dodo55 ","모지 : ${apiState.data.data.current.pollution.aqius.toString()}")
                    }
                    is ApiStates.Error -> {

                    }
                    else ->{

                    }
                }
            }


        }
    }
}