package com.kdh.eatwd.presenter.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdh.eatwd.data.entity.AddressResponse
import com.kdh.eatwd.data.remote.ApiStates
import com.kdh.eatwd.data.usecase.AddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressSearchViewModel @Inject constructor(
    private val addressUseCase: AddressUseCase
) : ViewModel() {

    private val _addressInfo: MutableStateFlow<List<AddressResponse.Results.Juso>?> = MutableStateFlow(null)
    var addressInfo = _addressInfo.asStateFlow()

    fun searchAddress(juso: String) {

        viewModelScope.launch {
            addressUseCase(juso).collect { state ->
                when (state) {
                    is ApiStates.Success -> {
                        _addressInfo.value = state.data.results.juso
                        Log.d("dodo66 ", " searchAddress Api 성공이연")
                    }
                    is ApiStates.Error -> {
                        Log.d("dodo66 ", " searchAddress Api 오류")
                    }
                    else -> {}
                }
            }
        }


    }
}