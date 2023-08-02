package com.setiawanalraz.finalprojectgigihme.api.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.setiawanalraz.finalprojectgigihme.api.client.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisasterViewModel: ViewModel() {
    private val _disasterCoordinate = MutableLiveData<List<Geometry>?>()
    val disasterCoordinate: LiveData<List<Geometry>?> = _disasterCoordinate
    private val _disasterReport = MutableLiveData<List<DisasterReports>?>()
    val disasterReports: LiveData<List<DisasterReports>?> = _disasterReport

    fun getDisasterCoordinates(timePeriod: Number) {
        ApiClient.instance.getDisasterAll(timePeriod).enqueue(object :
            Callback<DisasterReportModel> {
            override fun onResponse(
                call: Call<DisasterReportModel>,
                response: Response<DisasterReportModel>
            ) {
                if (response.isSuccessful) {
                    val coordinates = response.body()?.result?.objects?.output?.geometries
                    _disasterCoordinate.value = coordinates
                    _disasterReport.value = coordinates?.map { it.disasterReports }
                }
            }

            override fun onFailure(call: Call<DisasterReportModel>, t: Throwable) {
                t.message?.let { Log.d("Failed to Load!", it) }
            }
        })
    }
}