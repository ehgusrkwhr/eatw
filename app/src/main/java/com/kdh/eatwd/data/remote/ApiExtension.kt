package com.kdh.eatwd.data.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException


inline fun <T> apiCall(responseFun : () -> T) : ApiStates<T> =
    try{
        ApiStates.Success(responseFun.invoke())
    }catch (e : Exception){
        when(e){
            is SocketTimeoutException -> ApiStates.Error("타임아웃에러!!")
            else -> ApiStates.Error("에러!!")
        }
    }


//inline fun <T> apiCall(responseFun : () -> T) : ApiStates<T>{
//    return try{
//        ApiStates.Success(responseFun.invoke())
//    }catch (e : Exception){
//        when(e){
//            is SocketTimeoutException -> ApiStates.Error("타임아웃에러!!")
//            else -> ApiStates.Error("에러!!")
//        }
//    }
//}

//suspend fun <T : Any> apiCall(responseFun : suspend () -> T) : Flow<ApiStates<T>> {
//  return flow{
//      try{
//          emit(ApiStates.Success(responseFun.invoke()))
//      }catch (e : Exception){
//          when (e) {
//              is SocketTimeoutException -> emit(ApiStates.Error("타임아웃에러!!"))
//              else -> emit(ApiStates.Error("에러!!"))
//          }
//      }
//  }
//}