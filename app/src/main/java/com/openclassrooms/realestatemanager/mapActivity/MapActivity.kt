package com.openclassrooms.realestatemanager.mapActivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.detailActivity.DetailActivity
import com.openclassrooms.realestatemanager.domain.firebaseManager.MapQuestManager
import com.openclassrooms.realestatemanager.domain.firebaseRepository.MapQuestRepository
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.model.LocationModel
import com.openclassrooms.realestatemanager.model.apiModel.ResponseApi
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.Injection
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.ViewModelFactory
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MapActivity : AppCompatActivity(), OnMapReadyCallback, MapQuestRepository.Callbacks {
    private var mapView: MapView? = null
    lateinit var map: MapboxMap
    private val PERMS = Manifest.permission.ACCESS_FINE_LOCATION
    private val RC_LOCATION = 100
    private lateinit var dataViewModel: DataViewModel
    var mapViewModel: MapViewModel = MapViewModel.getInstance()
    var mapQuestManager: MapQuestManager = MapQuestManager.getInstance()
    var viewModel = com.openclassrooms.realestatemanager.viewModel.ViewModel.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MAP_KEY)
        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.mapview)
        mapView!!.onCreate(savedInstanceState)
        configureViewModel()
        mapView?.getMapAsync(this)
    }

    //map
    @SuppressLint("MissingPermission")
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.map = mapboxMap
        map.uiSettings.isZoomControlsEnabled = true
        acceptPermission()
    }

    //permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(100)
    private fun acceptPermission() {
        if (!EasyPermissions.hasPermissions(baseContext, this.PERMS)) {
            EasyPermissions.requestPermissions(
                this,
                "Need the permission to start!",
                this.RC_LOCATION,
                this.PERMS
            )
            return
        }
        enableLocationComponent(Style.MAPBOX_STREETS)
    }

    //enable location
    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: String) {
// Get an instance of the component
        val locationComponent = map.locationComponent
        // Activate with options
        locationComponent.activateLocationComponent(
            Mapbox.getApplicationContext()
        )
// Enable to make component visible
        locationComponent.isLocationComponentEnabled = true
// Set the component's camera mode
        locationComponent.cameraMode = CameraMode.TRACKING
// Set the component's render mode
        locationComponent.renderMode = RenderMode.COMPASS
        dataViewModel.allLocations.observe(this, this::addMarkers)
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    private fun addMarkers(list: List<LocationModel>) {
        for (location in list) {
            if (location.lat == null || location.lng == null) {
                mapViewModel.locationModel = location
                dataViewModel.getHome(location.homeUid).observe(this, this::checkLocation)
            } else {
                val latLng = LatLng(location.lat!!, location.lng!!)
                mapViewModel.addMarker(map, latLng, dataViewModel)
            }
        }
        map.setOnMarkerClickListener {
            mapViewModel.index = mapViewModel.markerList.indexOf(it)
            dataViewModel.homes.observe(this, this::detailIntent)
            false
        }
    }

    private fun detailIntent(list: List<HomeModel>) {
        viewModel!!.home.value = list[mapViewModel.index]
        val editIntent = Intent(this, DetailActivity::class.java)
        startActivity(editIntent)
    }

    private fun configureViewModel() {
        val viewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.dataViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DataViewModel::class.java)
    }

    //get home to call Api
    private fun checkLocation(home: HomeModel) {
        val street = home.street
        val city = home.city
        val postal = home.postalCode
        val country = home.country
        mapQuestManager.getLatLng(this, "$street,$city,$postal,$country")
    }

    //Api Response
    override fun onResponse(response: ResponseApi?) {
        val latLng = response!!.results[0].locations[0].latLng
        mapViewModel.locationModel.lat = latLng.lat
        mapViewModel.locationModel.lng = latLng.lng
        val latLngMarker = LatLng(latLng.lat, latLng.lng)
        dataViewModel.editLocation(mapViewModel.locationModel)
        mapViewModel.addMarker(map, latLngMarker, dataViewModel)
    }

    //Api fail
    override fun onFailure() {
        Toast.makeText(
            this,
            "For some reason, one or multiple markers couldn't be show",
            Toast.LENGTH_SHORT
        ).show()
    }
}