package com.kdh.eatwd.data.datasource

import com.kdh.eatwd.data.entity.AddressResponse
import com.kdh.eatwd.data.remote.ApiStates
import kotlinx.coroutines.flow.Flow


interface AddressDataSource {

    suspend fun getSearchAddressInfo(keyword : String) : Flow<ApiStates<AddressResponse>>

}