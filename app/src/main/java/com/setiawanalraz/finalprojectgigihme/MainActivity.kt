package com.setiawanalraz.finalprojectgigihme

import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.setiawanalraz.finalprojectgigihme.adapter.DisasterAdapter
import com.setiawanalraz.finalprojectgigihme.api.model.DisasterReports
import com.setiawanalraz.finalprojectgigihme.api.model.DisasterViewModel
import com.setiawanalraz.finalprojectgigihme.databinding.ActivityMainBinding
import com.setiawanalraz.finalprojectgigihme.ui.settings.SettingsActivity


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var adapter: DisasterAdapter
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private lateinit var bottomSheet: FrameLayout
    private lateinit var viewModel: DisasterViewModel

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

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DisasterViewModel::class.java]

        viewModel.disasterReports.observe(this) { report ->
            if (report != null) {
                adapter = DisasterAdapter(report)
                showBottomSheet(report)
            }
        }

        bottomSheet = findViewById(R.id.sheet)
        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val settings : ImageButton = findViewById(R.id.settings)
        settings.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        viewModel.getDisasterCoordinates(604800)
        viewModel.disasterCoordinate.observe(this) {
            it?.forEach { coordinates ->
                if (coordinates.type == "Point") {
                    val data = coordinates.coordinates
                    val latitude = data[1]
                    val longitude = data[0]
                    val markers = LatLng(latitude, longitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(markers)
                            .title(coordinates.disasterReports.disasterType)
                            .snippet("Telah terjadi bencana pada: ${coordinates.disasterReports.createdAt}")
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markers, 10f))
                }
            }
        }
    }

    private fun zoomOnMap(latLng: LatLng) {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, 12f)
        mGoogleMap?.animateCamera(newLatLngZoom)
    }

    private fun showBottomSheet(report: List<DisasterReports>) {
        val bottomSheetContent = layoutInflater.inflate(R.layout.bottom_sheet_content, null)
        val recyclerView = bottomSheetContent.findViewById<RecyclerView>(R.id.rv_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DisasterAdapter(report)

        bottomSheet.removeAllViews()
        bottomSheet.addView(bottomSheetContent)

        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 200
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

}