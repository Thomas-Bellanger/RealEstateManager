package com.openclassrooms.realestatemanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.HomeModel

@Dao
interface HomeDao {
    //get
    @Query("SELECT * FROM HomeModel")
    fun getHomes(): LiveData<List<HomeModel>>

    //insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHome(home: HomeModel?): Long

    //delete
    @Query("DELETE FROM HomeModel WHERE uid= :uid")
    fun deleteHome(uid: Long): Int

    //getList
    @Query("SELECT * FROM HomeModel WHERE uid= :homeUid")
    fun getHome(homeUid: Long): LiveData<HomeModel?>?

    //edit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun editHome(home: HomeModel?): Long

    //search
    @Query("SELECT * FROM HomeModel WHERE (isSold= CASE when :status = 1 THEN :isSold else isSold END) and (type = CASE when :type!='All' THEN :type else type END) and surface > :surfaceMin and surface < :surfaceMax and price<:priceMax and roomNumber >= :roomNumber and bedRoomNumber >= :bedRoomNumber and bathRoomNumber >= :bathRoomNumber and photoNumber>=:photoNumber and street like ('%'||:street||'%') and city like ('%'||:city||'%') and country like ('%'||:country||'%') and postalCode like ('%'||:postal||'%') and (school = CASE when :school=1 THEN :school else school END) and (park = CASE when :park=1 THEN :park else park END) and (shops = CASE when :shop=1 THEN :shop else shops END) and (station = CASE when :train=1 THEN :train else station END) ")
    fun searchHome(
        type: String,
        isSold: Boolean,
        status: Int,
        surfaceMin: Int,
        surfaceMax: Int,
        priceMax: Double,
        roomNumber: Int,
        bedRoomNumber: Int,
        bathRoomNumber: Int,
        photoNumber: Int,
        street: String,
        country: String,
        postal: String,
        city: String,
        train:Boolean,
        shop:Boolean,
        school:Boolean,
        park:Boolean
    ): LiveData<List<HomeModel>>
}