package com.example.nyc_schools_test.viewmodel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nyc_schools_test.common.ResponseState
import com.example.nyc_schools_test.model.remote.Repository
import com.example.nyc_schools_test.model.remote.SchoolListResponse
import com.example.nyc_schools_test.model.remote.SchoolSatResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception


@ExperimentalCoroutinesApi
class ViewModelTest{

    /**
     * Instant Task executor
     * All tasks shall be done instantly
     */
    @get:Rule var rule = InstantTaskExecutorRule()
    lateinit var subject:ViewModel
    lateinit var repo: Repository

    // Scope and dispatchers need to be created because the repo and
    // view model rely on them
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScopeCoroutine = TestScope(testDispatcher)

    /**
     * Create the mock (dummy values)
     */
    @Before
    fun setUpTest() {
        repo = mockk()
        subject = ViewModel(repo,testScopeCoroutine)
    }

    /**
     * TEST CASE - SUCCESS response state for Schools list api call
     *
     * Create success scenario using our view model
     * Observe and add mock, then compare with real value after api call
     * @return list of dummy values for every successful school caught
     */
    @Test
    fun `get schools list when fetching data from server for SUCCESS response state`(){
        every {
            repo.NYCSchoolCatched()
        } returns flowOf(ResponseState.SUCCESS(listOf(
            mockk<SchoolListResponse>())))

        var ResponseStateTestList = mutableListOf<ResponseState>()
        subject.schoolResponse.observeForever{
            ResponseStateTestList.add(it)
        }

        subject.getSchoolList()
        assertEquals(ResponseStateTestList.size, 1)
        val successTest = ResponseStateTestList.get(0) as ResponseState.SUCCESS<List<SchoolListResponse>>
        //assertEquals(successTest.response.size, 1)
        assertEquals(successTest.response.size, ResponseStateTestList.size)

    }

    /**
     * TEST CASE - SUCCESS response state for Schools sat api call
     *
     * Create success scenario using our view model
     * Observe and add mock, then compare with real value after api call
     * @return list of dummy values for every successful sat school caught
     */
    @Test
    fun `get sat scores when fetching data from server for SUCCESS response state`(){
        every {
            repo.NYCSatCatched()
        } returns flowOf(ResponseState.SUCCESS(listOf(
            mockk<SchoolSatResponse>())))

        var ResponseStateTestList = mutableListOf<ResponseState>()
        subject.schoolSatResponse.observeForever{
            ResponseStateTestList.add(it)
        }

        subject.getSatList()
        assertEquals(ResponseStateTestList.size, 1)
        val successTest = ResponseStateTestList.get(0) as ResponseState.SUCCESS<List<SchoolSatResponse>>
        //assertEquals(successTest.response.size, 1)
        assertEquals(successTest.response.size, ResponseStateTestList.size)

    }

    /**
     * TEST CASE - LOADING response state for Schools list api call
     *
     * Create loading scenario using our view model
     * Observe and add mock, then compare with real value after api call
     * @return dummy value for the loading class
     */
    @Test
    fun `get schools list when fetching data from server for LOADING response state`(){
        every {
            repo.NYCSchoolCatched()
        } returns flowOf(ResponseState.LOADING)

        var ResponseStateTestList = mutableListOf<ResponseState>()
        subject.schoolResponse.observeForever{
            ResponseStateTestList.add(it)
        }

        subject.getSchoolList()
        val loadingTest = ResponseStateTestList.get(0) as ResponseState.LOADING
        assertThat(loadingTest).isInstanceOf(ResponseState.LOADING::class.java)

    }

    /**
     * TEST CASE - LOADING response state for Schools sat api call
     *
     * Create loading scenario using our view model
     * Observe and add mock, then compare with real value after api call
     * @return dummy value for the loading class
     */
    @Test
    fun `get sat scores when fetching data from server for LOADING response state`(){
        every {
            repo.NYCSatCatched()
        } returns flowOf(ResponseState.LOADING)

        var ResponseStateTestList = mutableListOf<ResponseState>()
        subject.schoolSatResponse.observeForever{
            ResponseStateTestList.add(it)
        }

        subject.getSatList()
        val loadingTest = ResponseStateTestList.get(0) as ResponseState.LOADING
        assertThat(loadingTest).isInstanceOf(ResponseState.LOADING::class.java)

    }

    /**
     * TEST CASE - ERROR response state for Schools list api call
     *
     * Create error scenario using our view model
     * Observe and add mock, then compare with real value after api call
     * @return dummy value for the error class
     */
    @Test
    fun `get schools list when fetching data from server for ERROR response state`(){
        every {
            repo.NYCSchoolCatched()
        } returns flowOf(ResponseState.ERROR(Exception("Error fetching data from server")))

        var ResponseStateTestList = mutableListOf<ResponseState>()
        subject.schoolResponse.observeForever{
            ResponseStateTestList.add(it)
        }

        subject.getSchoolList()
        val errorTest = ResponseStateTestList.get(0) as ResponseState.ERROR
        assertThat(errorTest).isInstanceOf(ResponseState.ERROR::class.java)

    }

    /**
     * TEST CASE - ERROR response state for Schools sat api call
     *
     * Create error scenario using our view model
     * Observe and add mock, then compare with real value after api call
     * @return dummy value for the error class
     */
    @Test
    fun `get sat score when fetching data from server returns ERROR response state`(){
        every {
            repo.NYCSatCatched()
        } returns flowOf(ResponseState.ERROR(Exception("Error fetching data from server")))

        var ResponseStateTestList = mutableListOf<ResponseState>()
        subject.schoolSatResponse.observeForever{
            ResponseStateTestList.add(it)
        }

        subject.getSatList()
        val errorTest = ResponseStateTestList.get(0) as ResponseState.ERROR
        assertThat(errorTest).isInstanceOf(ResponseState.ERROR::class.java)

    }

    /**
     * Clear data and mocks once completed
     */
    @After
    fun shutdownTest() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

}