package com.setiawanalraz.finalprojectgigihme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.setiawanalraz.finalprojectgigihme.adapter.RVAdapter
import com.setiawanalraz.finalprojectgigihme.api.client.ApiClient
import com.setiawanalraz.finalprojectgigihme.api.model.DisasterReportModel
import com.setiawanalraz.finalprojectgigihme.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RVAdapter

    private var mGoogleMap: GoogleMap? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    private lateinit var settings: ImageButton
    private lateinit var sheet: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Places.initialize(applicationContext, getString(R.string.google_map_api_key))
        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Toast.makeText(this@MainActivity, p0.statusMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                latLng?.let { zoomOnMap(it) }
            }

        })

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        sheet = findViewById(R.id.sheet)
        BottomSheetBehavior.from(sheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        adapter = RVAdapter(this@MainActivity, arrayListOf())

        binding.rvMain.adapter = adapter
        binding.rvMain.setHasFixedSize(true)

        getDataFromApi()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
    }

    private fun zoomOnMap(latLng: LatLng) {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, 12f)
        mGoogleMap?.animateCamera(newLatLngZoom)
    }

//    private fun remoteGetUsers() {
//        ApiClient.apiService.getUsers().enqueue(object : Callback<ArrayList<DisasterReportModel>> {
//            override fun onResponse(
//                call: Call<ArrayList<DisasterReportModel>>,
//                response: Response<ArrayList<DisasterReportModel>>
//            ) {
//                if (response.isSuccessful) {
//                    val data = response.body()
//                    setDataToAdapter(data!!)
//                }
//            }
//
//            override fun onFailure(call: Call<ArrayList<DisasterReportModel>>, t: Throwable) {
//                Log.d("Error", "" + t.stackTraceToString())
//            }
//
//        })
//    }
//
//    private fun setDataToAdapter(data: ArrayList<DisasterReportModel>) {
//        adapter.setData(data)
//    }

    private fun getDataFromApi() {
        val call = ApiClient.apiService.getReports()

        call.enqueue(object : Callback<List<DisasterReportModel>> {
            override fun onResponse(
                call: Call<List<DisasterReportModel>>,
                response: Response<List<DisasterReportModel>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    setDataToAdapter(data as ArrayList<DisasterReportModel>)
                }
            }

            override fun onFailure(call: Call<List<DisasterReportModel>>, t: Throwable) {
                Log.d("Error", "" + t.stackTraceToString())
            }
        })
    }

    private fun setDataToAdapter(data: ArrayList<DisasterReportModel>) {
        adapter.setData(data)
    }
}