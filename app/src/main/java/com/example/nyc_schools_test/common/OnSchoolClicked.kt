package com.example.nyc_schools_test.common

import com.example.nyc_schools_test.model.remote.SchoolListResponse

/**
 * Interface to support a selected school
 */
interface OnSchoolClicked {
    fun schoolClicked(school: SchoolListResponse)
}