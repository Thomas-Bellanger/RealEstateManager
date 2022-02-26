package com.openclassrooms.realestatemanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.LocationModel

@Dao
interface LocationDao {
    //get
    @Query("SELECT * FROM LocationModel WHERE homeUid= :homeUid")
    fun getLocation(homeUid: Long): LiveData<LocationModel>


    //get
    @Query("SELECT * FROM LocationModel")
    fun getAllLocations(): LiveData<List<LocationModel>>

    //insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: LocationModel?): Long
}