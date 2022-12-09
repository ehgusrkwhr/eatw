package com.kdh.eatwd.data.usecase

import com.kdh.eatwd.data.repository.WeatherInfoRepository
import javax.inject.Inject

class AddressUseCase @Inject constructor(
    private val repository: WeatherInfoRepository
) {
    //    suspend operator fun invoke(keyword : String) = repository.getSearchAddressInfo(keyword = keyword)
    suspend operator fun invoke(keyword: String) = repository.searchAddressInfoPaging(keyword = keyword)
}