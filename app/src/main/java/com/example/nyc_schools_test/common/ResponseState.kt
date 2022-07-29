package com.example.nyc_schools_test.common

sealed class ResponseState {
    /**
     * Loading Response Object
     */
    object LOADING : ResponseState()

    /**
     * Successful Response Object
     */
    class SUCCESS<T>(val response : T) : ResponseState()

    /**
     * Error Response Object
     */
    class ERROR(val error: Exception) : ResponseState()
}
