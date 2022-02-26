package com.openclassrooms.realestatemanager.editActivity.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.firebaseManager.HomeManager
import com.openclassrooms.realestatemanager.domain.firebaseManager.PhotoManager
import com.openclassrooms.realestatemanager.model.LocationModel
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.ViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel

class EditViewModel {


    var isAppartment: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var photoToRemove: MutableList<PhotoModel> = mutableListOf()
    var photoToAdd: MutableList<PhotoModel> = mutableListOf()
    var viewModel = ViewModel.getInstance()
    private val photoManager = PhotoManager.getInstance()

    companion object {
        @Volatile
        private var instance: EditViewModel? = null
        fun getInstance(): EditViewModel? {
            val result = instance
            instance = instance?.let { result } ?: EditViewModel()
            return instance
        }
    }

    //check if the property is an apartment
    fun compareString(type: String) {
        isAppartment.value = type == "Apartment"
    }

    //editHome in db and fb
    fun editHome(
        dataViewModel: DataViewModel, context: Context,
        callBackCity: String,
        callBackCountry: String,
        callBackPostal: String,
        callBackStreet: String,
        callBackSurface: Int,
        callBackRoomNumber: Int,
        callBackPrice: Double,
        callBackBathRoom: Int,
        callBackDescription: String,
        callBackBedRoom: Int,
        callBackApartment: String?,
        callBackAgent: String,
        callBackTrain:Boolean,
        callBackShop:Boolean,
        callBackSchool:Boolean,
        callBackPark:Boolean
    ) {
        if (viewModel?.home?.value != null) {
            val home = viewModel?.home?.value
            if (callBackCity != home?.city || callBackCountry != home.country || home.postalCode != callBackPostal || home.street != callBackStreet) {
                home?.city = callBackCity
                home?.country = callBackCountry
                home?.postalCode = callBackPostal
                home?.street = callBackStreet
                dataViewModel.createLocation(home!!, context)
            }
            home.surface = callBackSurface
            home.roomNumber = callBackRoomNumber
            home.price = callBackPrice
            home.bathRoomNumber = callBackBathRoom
            home.description = callBackDescription
            home.bedRoomNumber = callBackBedRoom
            home.bedRoomNumber = callBackBedRoom
            home.appartment = callBackApartment
            home.lastModifTime = Utils.getTodayDate()
            home.photoNumber = viewModel?.listPhoto?.value?.size!!
            home.school = callBackSchool
            home.station = callBackTrain
            home.park = callBackPark
            home.shops = callBackShop
            home.sellerName = callBackAgent
            for (photo in photoToRemove) {
                dataViewModel.deletePhoto(photo.uid)
            }
            for (photo in photoToAdd) {
                photo.homeUid = home.uid
                dataViewModel.createPhoto(photo)
            }
            dataViewModel.editHome(home)
            if (Utils.isConnected(context)) {
                HomeManager.getInstance().createHomeFirebase(home)
            }
        }
    }
}