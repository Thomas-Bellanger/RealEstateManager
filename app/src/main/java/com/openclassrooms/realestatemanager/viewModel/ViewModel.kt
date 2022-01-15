package com.openclassrooms.realestatemanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.model.HomeModel

class ViewModel {

    var home = MutableLiveData<HomeModel>()

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

    var moneyType: MutableLiveData<MoneyType?> = MutableLiveData<MoneyType?>()

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
}