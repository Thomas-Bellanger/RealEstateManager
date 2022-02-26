package com.openclassrooms.realestatemanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.PhotoModel

@Dao
interface PhotoDao {
    //get
    @Query("SELECT * FROM PhotoModel WHERE uid= :uid")
    fun getPhoto(uid: Long): LiveData<PhotoModel>

    //insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: PhotoModel?): Long

    //delete
    @Query("DELETE FROM PhotoModel WHERE uid= :uid")
    fun deletePhoto(uid: Long): Int

    //getList
    @Query("SELECT * FROM PhotoModel WHERE homeUid= :homeuid")
    fun getPhotos(homeuid: Long): LiveData<List<PhotoModel>>
}