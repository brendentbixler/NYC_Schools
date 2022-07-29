package com.example.nyc_schools_test.model.remote

import android.util.Log
import com.example.nyc_schools_test.common.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface Repository {
    fun NYCSchoolCatched(): Flow<ResponseState>
    fun NYCSatCatched(): Flow<ResponseState>
}

/**
 * Supplies data for the view model
 */
class RepositoryImpl @Inject constructor(
    private val service: Api
) : Repository {

    /**
     * Initial schools list
     */
    override fun NYCSchoolCatched() = flow {
        emit(ResponseState.LOADING)
        try {
            val respose = service.getSchoolList()
            if (respose.isSuccessful) {
                respose.body()?.let {
                    emit(ResponseState.SUCCESS(it))
                } ?: throw Exception("Error null")
            } else {
                throw Exception("Error failure")
            }
        } catch (e: Exception) {
            emit(ResponseState.ERROR(e))
        }
    }

    /**
     * Sat scores for selected school
     */
    override fun NYCSatCatched() = flow {
        emit(ResponseState.LOADING)
        try {
            val respose = service.getSchoolSat()
            if (respose.isSuccessful) {
                respose.body()?.let {
                    emit(ResponseState.SUCCESS(it))
                    Log.i("Repository", "NYCSatCatched: $it ")
                } ?: throw Exception("Error null")
            } else {
                throw Exception("Error failure")
            }
        } catch (e: Exception) {
            emit(ResponseState.ERROR(e))
        }
    }
}