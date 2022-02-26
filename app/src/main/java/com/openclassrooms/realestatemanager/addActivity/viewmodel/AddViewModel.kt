package com.openclassrooms.realestatemanager.addActivity.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.domain.firebaseManager.PhotoManager
import com.openclassrooms.realestatemanager.domain.firebaseRepository.PhotoRepository
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.ViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel

class AddViewModel {

    var home = MutableLiveData<HomeModel>()
    var avatar: String = ""
    var isAppartment: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var viewModel = ViewModel.getInstance()
    val photoManager = PhotoManager.getInstance()

    companion object {
        @Volatile
        private var instance: AddViewModel? = null
        fun getInstance(): AddViewModel? {
            val result = instance
            instance = instance?.let { result } ?: AddViewModel()
            return instance
        }
    }

    fun compareString(type: String) {
        isAppartment.value = type == "Apartment"
    }

    fun createHomeFireBase(
        dataViewModel: DataViewModel,
        net: Boolean,
        avatar: String,
        type: String,
        city: String,
        price: Double,
        street: String,
        apartment: String?,
        postalCode: String,
        country: String,
        surface: Int,
        roomNumber: Int,
        bathRoomNumber: Int,
        bedRoomNumber: Int,
        description: String,
        agent: String,
        train:Boolean,
        shop:Boolean,
        school:Boolean,
        park:Boolean,
        context: Context
    ) {
        val creationTime = Utils.getTodayDate()
        val homeToCreate = HomeModel(
            avatar = avatar,
            type = type,
            city = city,
            price = price,
            street = street,
            appartment = apartment,
            postalCode = postalCode,
            country = country,
            surface = surface,
            roomNumber = roomNumber,
            bedRoomNumber = bedRoomNumber,
            bathRoomNumber = bathRoomNumber,
            photoNumber = viewModel?.listPhoto?.value?.size!!,
            description = description,
            creationTime = creationTime,
            isSold = false,
            sellTime = "",
            lastModifTime = creationTime,
            sellerName = agent,
            station = train,
            park = park,
            shops = shop,
            school = school
        )
        dataViewModel.createHome(homeToCreate, net, dataViewModel, context)
    }

    //create photo on phone and firebase
    fun createPhotoForHome(homeToCreate: HomeModel, dataViewModel: DataViewModel) {
        for (photo: PhotoModel in viewModel?.listPhoto?.value ?: mutableListOf()) {
            photo.homeUid = homeToCreate.uid
            dataViewModel.createPhoto(photo)
            val imageUri = Uri.parse(photo.image)
            photoManager.changeImageForUrl(
                imageUri,
                photo,
                photo.uid.toString(),
                photo.homeUid.toString()
            )
        }
    }
}