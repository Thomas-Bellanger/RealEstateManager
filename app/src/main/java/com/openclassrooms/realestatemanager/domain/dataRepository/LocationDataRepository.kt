package com.openclassrooms.realestatemanager.domain.dataRepository

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.LocationDao
import com.openclassrooms.realestatemanager.domain.firebaseManager.LocationManager
import com.openclassrooms.realestatemanager.model.LocationModel

class LocationDataRepository(private val locationDao: LocationDao) {
    private val locationManager: LocationManager = LocationManager.getInstance()

    //create
    fun createLocation(location: LocationModel?) {
        locationManager.createLocationFirebase(location)
        locationDao.insertLocation(location)
    }

    //get
    fun getLocation(homeUid: Long): LiveData<LocationModel> {
        return this.locationDao.getLocation(homeUid)
    }

    //get all
    fun getAllLocations(): LiveData<List<LocationModel>> {
        return this.locationDao.getAllLocations()
    }

    //edit
    fun editLocation(location: LocationModel?) {
        locationDao.insertLocation(location)
    }
}