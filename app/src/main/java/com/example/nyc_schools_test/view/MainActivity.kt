package com.example.nyc_schools_test.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nyc_schools_test.common.OnSchoolClicked
import com.example.nyc_schools_test.common.SCHOOL_ITEM
import com.example.nyc_schools_test.common.ResponseState
import com.example.nyc_schools_test.databinding.ActivityMainBinding
import com.example.nyc_schools_test.model.remote.SchoolListResponse
import com.example.nyc_schools_test.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnSchoolClicked {

    // Main bind
    private lateinit var bindingMain: ActivityMainBinding

    // Set view model
    private val viewModel: ViewModel by lazy {
        ViewModelProvider(this).get(ViewModel::class.java)
    }

    // Set adapter
    private lateinit var adapterSchool: Adapter

    /**
     * OnCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
    }

    /**
     * OnResume
     */
    override fun onResume() {
        super.onResume()
        initializeRecyclerView()
        initObservables()
        viewModel.getSchoolList()
    }

    /**
     * Helper method to init Recycler view
     */
    private fun initializeRecyclerView() {
        adapterSchool = Adapter(this)
        bindingMain.listSchool.apply {
            layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            adapter = adapterSchool
        }
    }

    /**
     * Helper method to init response states
     */
    private fun initObservables() {
        viewModel.schoolResponse.observe(this) { action ->
            when (action) {
                is ResponseState.LOADING -> {
                    Toast.makeText(baseContext, "Loading ...", Toast.LENGTH_SHORT).show()
                }
                is ResponseState.SUCCESS<*> -> {
                    val newSchools = action.response as? List<SchoolListResponse>
                    newSchools?.let {
                        adapterSchool.updateData(it)
                        Log.i("MainActivity", "initIbservablesSchoolResponse $it ")
                    } ?: showError("Error at casting")
                }
                is ResponseState.ERROR -> {
                    showError(action.error.localizedMessage)
                }
            }
        }
    }

    /**
     * Error message
     */
    private fun showError(message: String) {
        AlertDialog.Builder(baseContext)
            .setTitle("Error occurred")
            .setMessage(message)
            .setNegativeButton("CLOSE") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    /**
     * selected school by click
     */
    override fun schoolClicked(school: SchoolListResponse) {
        Intent(baseContext, SchoolInfo::class.java).apply {
            putExtra(SCHOOL_ITEM, school)
            startActivity(this)
        }
    }

    /**
     * OnStop
     */
    override fun onStop() {
        super.onStop()
        viewModel.schoolResponse.removeObservers(this)
    }
}

