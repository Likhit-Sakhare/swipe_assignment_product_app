package com.likhit.swipeassignment.utils

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null,
) {
    class Success<T>(data: T?): Result<T>(data)
    class Error<T>(message: String?, data: T? = null): Result<T>(data, message)
    class Loading<T>: Result<T>()
}

fun <T> safeApiCall(
    call: suspend () -> T
): Flow<Result<T>>{
    return flow {
        emit(Result.Loading())
        try {
            val response = call()
            Log.d("safeApiCall", response.toString())
            if((response as Response<*>).isSuccessful){
                emit(Result.Success(data = response))
            }else{
                emit(Result.Error(message = response.message()))
            }
        }catch (e: HttpException){
            emit(Result.Error(message = "HttpException: ${e.message()}"))
        }catch (e: IOException){
            emit(Result.Error(message = "NetworkException: ${e.message?: "IOException"}"))
        }catch (e: Exception){
            emit(Result.Error(message = "Something went wrong ${e.message?: "Exception"}"))
        }
    }
}