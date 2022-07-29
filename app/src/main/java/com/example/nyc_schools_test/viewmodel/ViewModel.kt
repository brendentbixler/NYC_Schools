package com.example.nyc_schools_test.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nyc_schools_test.common.ResponseState
import com.example.nyc_schools_test.model.remote.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Our one view model
 * Take the data, process it, and add it to the lists
 */
@HiltViewModel
class ViewModel @Inject constructor(
    private val repository: Repository,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _schoolResponse = MutableLiveData<ResponseState>()
    val schoolResponse: MutableLiveData<ResponseState>
        get() = _schoolResponse

    private val _schoolSatResponse = MutableLiveData<ResponseState>()
    val schoolSatResponse: MutableLiveData<ResponseState>
        get() = _schoolSatResponse

    /**
     * Fetch school list data
     */
    fun getSchoolList() {
        coroutineScope.launch {
            repository.NYCSchoolCatched().collect {
                _schoolResponse.postValue(it)
            }
        }
    }

    /**
     * Fetch SAT info data
     */
    fun getSatList() {
        coroutineScope.launch {
            repository.NYCSatCatched().collect {
                _schoolSatResponse.postValue(it)
            }
        }
    }
}