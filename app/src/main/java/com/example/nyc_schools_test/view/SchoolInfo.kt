package com.example.nyc_schools_test.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nyc_schools_test.common.SCHOOL_ITEM
import com.example.nyc_schools_test.common.ResponseState
import com.example.nyc_schools_test.databinding.SchoolInfoLayoutBinding
import com.example.nyc_schools_test.model.remote.SchoolListResponse
import com.example.nyc_schools_test.model.remote.SchoolSatResponse
import com.example.nyc_schools_test.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main class/activity to display correct SAT info for a selected school from Main
 *
 * Potential Feature for the future: MAKE THIS A FRAGMENT
 */
@AndroidEntryPoint
class SchoolInfo : AppCompatActivity() {
    // School Info Bind
    private lateinit var bindingSchoolInfoLayoutBinding: SchoolInfoLayoutBinding

    // Set view model
    private val viewModel: ViewModel by lazy {
        ViewModelProvider(this).get(ViewModel::class.java)
    }

    /**
     * Inflate and bind
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSchoolInfoLayoutBinding = SchoolInfoLayoutBinding.inflate(layoutInflater)
        setContentView(bindingSchoolInfoLayoutBinding.root)

        intent.apply {
            val school = getParcelableExtra<SchoolListResponse>(SCHOOL_ITEM)

            bindingSchoolInfoLayoutBinding.tvSchoolName.text = school?.school_name
            bindingSchoolInfoLayoutBinding.tvAddress.text = school?.location
            bindingSchoolInfoLayoutBinding.tvEmail.text = school?.school_email
            bindingSchoolInfoLayoutBinding.tvWebsite.text = school?.website
            bindingSchoolInfoLayoutBinding.tvOverview.text = school?.overview_paragraph

            initObservables(school?.dbn)
        }

        viewModel.getSatList()

        bindingSchoolInfoLayoutBinding.backButton.setOnClickListener{
            val intentBack = Intent(this, MainActivity::class.java)
            startActivity(intentBack)
        }
    }

    /**
     * Helper method to init response states
     */
    private fun initObservables(schDbn: String?) {
        viewModel.schoolSatResponse.observe(this) { action ->
            when (action) {
                is ResponseState.LOADING -> {
                    Toast.makeText(baseContext, "loading SAT schools...", Toast.LENGTH_SHORT).show()
                }
                is ResponseState.SUCCESS<*> -> {
                    val newSchools = action.response as? List<SchoolSatResponse>
                    newSchools?.let {
                        Log.i("MainActivity2", "initObservablesSAT: $it ")
                        schDbn?.let { schoolDbn ->
                            populateSatDetails(it, schoolDbn)
                        } ?: showError("Error at school dbn null")
                    } ?: showError("Error at casting SAT")
                }
                is ResponseState.ERROR -> {
                    showError(action.error.localizedMessage)
                }
            }
        }
    }

    /**
     * Bind and display correct sat details for a selected school
     */
    private fun populateSatDetails(satDetails: List<SchoolSatResponse>, schDbn: String) {
        satDetails.firstOrNull { it.dbn == schDbn }?.let {
            if (it.mathAvg.isEmpty()) {
                bindingSchoolInfoLayoutBinding.scoreInfo.visibility = View.INVISIBLE
            } else {
                bindingSchoolInfoLayoutBinding.scoreInfo.visibility = View.VISIBLE
            }
            bindingSchoolInfoLayoutBinding.tvMathScores.text = it.mathAvg
            bindingSchoolInfoLayoutBinding.tvReadingScores.text = it.readingAvg
            bindingSchoolInfoLayoutBinding.tvWritingScores.text = it.writingAvg
        }
    }

    /**
     * Provide Error to user
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
     * OnStop
     */
    override fun onStop() {
        super.onStop()
        viewModel.schoolSatResponse.removeObservers(this)
    }
}