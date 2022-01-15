package com.openclassrooms.realestatemanager.mapActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.openclassrooms.realestatemanager.R

class MapActivity : AppCompatActivity() {
    var mMapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        Mapbox.getInstance(applicationContext, getString(R.string.access_token))
        mMapView = findViewById(R.id.mapview)
        mMapView!!.onCreate(savedInstanceState)
    }
}