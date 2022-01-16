package com.openclassrooms.realestatemanager.mapActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R

class MapActivity : AppCompatActivity() {
    var mMapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MAP_KEY)
        setContentView(R.layout.activity_map)
        mMapView = findViewById(R.id.mapview)
        mMapView!!.onCreate(savedInstanceState)
    }
}