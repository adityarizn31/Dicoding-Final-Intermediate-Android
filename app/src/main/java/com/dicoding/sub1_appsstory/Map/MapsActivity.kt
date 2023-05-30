package com.dicoding.sub1_appsstory.Map

import android.content.ContentValues.TAG
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dicoding.sub1_appsstory.Data.DetailStoryResp
import com.dicoding.sub1_appsstory.Data.GetStoryResp
import com.dicoding.sub1_appsstory.Data.Story

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.sub1_appsstory.R
import com.dicoding.sub1_appsstory.ViewModel.MainViewModel
import com.dicoding.sub1_appsstory.ViewModel.ViewModelFactory
import com.dicoding.sub1_appsstory.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.MapStyleOptions
import com.dicoding.sub1_appsstory.RemoteData.Result
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private var token: String? = null
    private val boundsBuilder = LatLngBounds.builder()

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()

//        val token = mainViewModel.getPreference(this).value.toString()
//        mainViewModel.getMapsStories("Bearer $token").observe(this  ) {
//            if (it != null) {
//                when(it) {
//                    is Result.Success -> {
//                        addStoryMapsMarker(it.data)
//                    }
//                    is Result.Loading -> {
//
//                    }
//                    is Result.Error -> {
//                        Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//        }

        val token = mainViewModel.getPreference(this).value.toString()
        mainViewModel.getMapsStories("Bearer $token").observe(this){
            if (it != null) {
                when(it) {
                    is Result.Success -> {
                        addStoryMapsMarker(it.data)
                    }
                    is Result.Loading -> {
                    }
                    is Result.Error -> {
                        Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun addStoryMapsMarker(listStory: GetStoryResp) {
        listStory.listStory.forEach { usermaps ->
            val latLng = LatLng(usermaps.lat, usermaps.lon)
            mMap.addMarker(MarkerOptions()
                .position(latLng)
                .title(usermaps.name)
            )
            boundsBuilder.include(latLng)
        }
        val boundss : LatLngBounds = boundsBuilder.build()

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                boundss,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                30
            )
        )
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style Parsing Is Failed")
            }
        } catch (excep : Resources.NotFoundException) {
            Log.e(TAG, "Can't Find Style Error : ", excep)
        }
    }

//    private fun getStoriesMap(token: String) {
//        mainViewModel.getCompletedStories("Bearer $token").observe(this) { response ->
//            when(response) {
//                is Result.Loading -> {}
//                is Result.Success -> {
//                    val stories = response.data.story
//                    Story(
//                        id = id
//                    )
//                }
//                is Result.Error -> {
//                    Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    private fun setMapStyle() {
//        try {
//            val successs = mMap.setMapStyle(
//                MapStyleOptions.loadRawResourceStyle(
//                    requireContext(),
//                    R.raw.map_style
//                )
//            )
//            if (!successs) {
//                Log.e("TagLa", "Failed Style")
//            }
//        } catch (exception: Resources.NotFoundException) {
//            Log.e("TagLa", "Can't Find Style: ", exception)
//        }
//    }
//
//    private fun getMyLocation() {
//        if (ContextCompat.checkSelfPermission(
//                this.applicationContext,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            mMap.isMyLocationEnabled = true
//        } else {
//            requestPermissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }
}