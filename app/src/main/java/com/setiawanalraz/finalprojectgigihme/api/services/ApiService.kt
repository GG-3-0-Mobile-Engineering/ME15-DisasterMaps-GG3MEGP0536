package com.setiawanalraz.finalprojectgigihme.api.services

import com.setiawanalraz.finalprojectgigihme.api.model.DisasterReportModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("reports")
    fun getDisasterAll(
        @Query("timeperiod") number: Number
    ): Call<DisasterReportModel>
}