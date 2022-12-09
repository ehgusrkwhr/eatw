package com.kdh.eatwd.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kdh.eatwd.data.datasource.AddressDataPaging
import com.kdh.eatwd.data.datasource.AddressDataSource
import com.kdh.eatwd.data.datasource.AirInfoDataSource
import com.kdh.eatwd.data.datasource.WeatherDataSource
import com.kdh.eatwd.data.entity.AddressResponse
import com.kdh.eatwd.data.remote.AddressService
import com.kdh.eatwd.data.remote.ApiStates
import com.kdh.eatwd.presenter.util.Constants.PAGING_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherInfoRepository @Inject constructor(
    private val airInfoDataSource: AirInfoDataSource,
    private val weatherDataSource: WeatherDataSource,
    private val addressService: AddressService,
//    private val addressDataSource: AddressDataSource
) {

    suspend fun getAirInfo(lat: Double, log: Double) = airInfoDataSource.getAirInfo(lat,log)
    suspend fun getWeatherInfo(lat: Double, log: Double) = weatherDataSource.getWeatherInfo(lat,log)
//    suspend fun getSearchAddressInfo(keyword : String) = addressDataSource.getSearchAddressInfo(keyword)

    fun searchAddressInfoPaging(keyword: String) : Flow<PagingData<AddressResponse.Results.Juso>> {
        val source = {AddressDataPaging(addressService = addressService ,keyword = keyword)}
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
            ) ,
            pagingSourceFactory = source
        ).flow
    }

//    private fun AddressDataPaging(keyword: String): AddressDataPaging {
//
//    }

}