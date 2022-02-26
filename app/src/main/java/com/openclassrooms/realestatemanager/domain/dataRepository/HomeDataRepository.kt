package com.openclassrooms.realestatemanager.domain.dataRepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.addActivity.viewmodel.AddViewModel
import com.openclassrooms.realestatemanager.database.HomeDao
import com.openclassrooms.realestatemanager.domain.firebaseManager.HomeManager
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel

class HomeDataRepository(private val homeDao: HomeDao) {
    var homeManager: HomeManager = HomeManager.getInstance()
    var addViewModel = AddViewModel.getInstance()

    //create
    fun createHome(home: HomeModel?, net: Boolean, dataViewModel: DataViewModel, context: Context) {
        home?.uid = homeDao.insertHome(home)
        homeDao.insertHome(home)
        dataViewModel.createLocation(home!!, context)
        addViewModel?.createPhotoForHome(home, dataViewModel)
        if (net) {
            homeManager.createHomeFirebase(home)
        }
    }

    //get
    fun getHomes(): LiveData<List<HomeModel>> {
        return this.homeDao.getHomes()
    }

    fun getHome(id: Long): LiveData<HomeModel?>? {
        return this.homeDao.getHome(id)
    }

    fun editHome(home: HomeModel?) {
        homeDao.editHome(home)
    }

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

    ): LiveData<List<HomeModel>> {
        return this.homeDao.searchHome(
            type,
            isSold,
            status,
            surfaceMin,
            surfaceMax,
            priceMax,
            roomNumber,
            bedRoomNumber,
            bathRoomNumber,
            photoNumber,
            street,
            country,
            postal,
            city,
            train,
            shop,
            school,
            park
        )
    }
}