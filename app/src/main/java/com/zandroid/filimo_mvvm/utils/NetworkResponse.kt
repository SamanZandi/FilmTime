package com.zandroid.filimo_mvvm.utils

import retrofit2.Response

open class NetworkResponse<T>(private val response: Response<T>) {

    fun generalNetworkResponse(): NetworkRequest<T> {
        return when {
            response.code() == 500 -> NetworkRequest.Error("Try again")
            response.code() == 200->NetworkRequest.Success(response.body()!!)
            response.isSuccessful -> NetworkRequest.Success(response.body()!!)
            else -> NetworkRequest.Error(response.message())
        }
    }
}