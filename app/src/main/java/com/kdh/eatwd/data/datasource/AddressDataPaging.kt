package com.kdh.eatwd.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kdh.eatwd.data.entity.AddressResponse
import com.kdh.eatwd.data.remote.AddressService
import com.kdh.eatwd.presenter.util.Constants.PAGE_START


class AddressDataPaging(
    private val addressService: AddressService,
    private val keyword: String
) : PagingSource<Int, AddressResponse.Results.Juso>() {



    override fun getRefreshKey(state: PagingState<Int, AddressResponse.Results.Juso>): Int? {
        return state.anchorPosition?.let { currentPos ->
            state.closestPageToPosition(currentPos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(currentPos)?.nextKey?.minus(1)

        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AddressResponse.Results.Juso> {
        return try {

            val pageNum = params.key ?: PAGE_START
            Log.d("dodo66 ", " pageNum : ${pageNum}")
            Log.d("dodo66 ", " params.loadSize : ${params.loadSize}")

            val res = addressService.getAddressData(
                keyword,
                "json",
                AddressService.API_KEY,
                pageNum,
                params.loadSize
            )
            val prevKey = if (pageNum == PAGE_START) null else pageNum - 1
            val nextKey = pageNum + 1
            val data = res.body()?.results?.juso!!
            Log.d("dodo66 ", " data : ${data}")
            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }

    }
}