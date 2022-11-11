package com.kdh.eatwd.data.remote

sealed class ApiStates<out T> {
    class Success<T>(val data: T) : ApiStates<T>()
    class Error<T>(val message: String) : ApiStates<T>()
//    object Loading : ApiStates<Nothing>()
    companion object {
        fun <T> success(data: T) = Success<T>(data)
        fun <T> error(message: String) = Error<T>(message)
    }
}