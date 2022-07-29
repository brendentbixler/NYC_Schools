package com.example.nyc_schools_test.model.remote

import com.example.nyc_schools_test.common.*
import retrofit2.Response
import retrofit2.http.GET

/**
 * Specify which end point to use for the desired network call
 */
interface Api {

    /**
     * Get initial schools list for recycler view
     */
    @GET(END_POINT_SCHOOLS)
    suspend fun getSchoolList(): Response<List<SchoolListResponse>>

    /**
     * Get sat scores for a selected school
     */
    @GET(END_POINT_SAT)
    suspend fun getSchoolSat(): Response<List<SchoolSatResponse>>
}