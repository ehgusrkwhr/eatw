package com.kdh.eatwd.data.datasource

import com.kdh.eatwd.data.entity.AddressResponse
import com.kdh.eatwd.data.remote.*
import com.kdh.eatwd.data.util.StringUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class AddressDataSourceImpl @Inject constructor(
    private val addressService: AddressService
) : AddressDataSource {

    override suspend fun getSearchAddressInfo(keyword: String): Flow<ApiStates<AddressResponse>> {

        return flow {
            addressService.getAddressData(
                keyword,
                "json",
                AddressService.API_KEY
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