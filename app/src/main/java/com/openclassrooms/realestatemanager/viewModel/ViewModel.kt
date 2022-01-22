package com.openclassrooms.realestatemanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.domain.manager.HomeManager
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.model.PhotoModel
import java.util.ArrayList

class ViewModel {

    var home = MutableLiveData<HomeModel>()
    var homeManager = HomeManager.getInstance()
    var listPhoto: MutableLiveData<MutableList<PhotoModel>> = MutableLiveData<MutableList<PhotoModel>>()

    companion object {

        @Volatile
        private var instance: ViewModel? = null
        fun getInstance(): ViewModel? {
            val result = instance
            instance = if (instance != null) {
                return result
            } else {
                ViewModel()
            }
            return instance
        }
    }

    var moneyType: MutableLiveData<MoneyType> = MutableLiveData<MoneyType>()

    enum class MoneyType {
        DOLLAR, EURO
    }

    fun getMyHome(): MutableLiveData<HomeModel> {
        return home
    }

    fun setMyHome(newHome: HomeModel) {
        home.value = newHome
        Log.e("viewmodel", home.value?.type.toString())
    }

    fun createHomeFireBase(avatar: String,
                           type: String,
                           city: String,
                           price: Double,
                           street: String,
                           appartment: String?,
                           postalCode: String,
                           country: String,
                           surface: Int,
                           roomNumber: Int,
                           bathRoomNumber: Int,
                           bedRoomNumber: Int,
                           location: String,
                           uid: String,
                           description:String){
       var homeToCreate = HomeModel(avatar, type, city, price, street, appartment, postalCode, country, surface, roomNumber, bathRoomNumber, bedRoomNumber, location, uid, description)
        homeManager.createHomeFirebase(homeToCreate)
    }
}