package com.setiawanalraz.finalprojectgigihme.api.services

import com.setiawanalraz.finalprojectgigihme.api.model.DataDisaster
import com.setiawanalraz.finalprojectgigihme.api.model.DisasterReportModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//interface ApiService {
//    @GET("reports/archive")
//    fun getDisaster(
//        @Query("start", encoded = true) startTime: String,
//        @Query("end", encoded = true) endTime: String
//    ): Call<DataDisaster>
//}

interface ApiService {
    @GET("reports?timeperiod=604800")
    fun getReports(): Call<List<DisasterReportModel>>
}