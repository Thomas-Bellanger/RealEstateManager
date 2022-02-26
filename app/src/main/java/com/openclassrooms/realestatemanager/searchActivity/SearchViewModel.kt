package com.openclassrooms.realestatemanager.searchActivity

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.viewModel.ViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel
import java.text.SimpleDateFormat
import java.util.*

class SearchViewModel {
    val viewModel = ViewModel.getInstance()
    var dateSearch: String = ""
    var listDateFiltered: MutableList<HomeModel> = mutableListOf()
    var contextForToast: Context? = null

    //instance
    companion object {
        @Volatile
        private var instance: SearchViewModel? = null
        fun getInstance(): SearchViewModel? {
            val result = instance
            instance = instance?.let { result } ?: SearchViewModel()
            return instance
        }
    }

    //query room for filter
    fun filter(
        isAvailable: Boolean,
        isSold: Boolean,
        type: String,
        surfaceMin: Int,
        surfaceMax: Int,
        priceMax: Double,
        roomNumber: Int,
        bedRoomNumber: Int,
        bathRoomNumber: Int,
        photoNumber: Int,
        street: String,
        city: String,
        country: String,
        postal: String,
        date: String,
        train:Boolean,
        shop:Boolean,
        school:Boolean,
        park:Boolean,
        dataViewModel: DataViewModel,
        lifecycleOwner: LifecycleOwner,
        context: Context
    ) {
        var status = 0
        var sold = false
        if(isAvailable && !isSold){
            status = 1
            sold = false
        }
        else if(!isAvailable && isSold){
            status = 1
            sold = true
        }
        dateSearch = date
        contextForToast = context
            dataViewModel.searchHome(type,
                sold,
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
                .observe(lifecycleOwner, this::filterList)
    }

    //filter by date
    private fun filterList(list: List<HomeModel>) {
        listDateFiltered.clear()
        if (dateSearch == "") {
            viewModel?.listHomesFiltered?.value = list as MutableList<HomeModel>
        } else {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd")
            for (home in list) {
                val dateToCompare: Date = dateFormat.parse(dateSearch)
                val dateHome: Date = dateFormat.parse(home.creationTime)
                if (dateToCompare.before(dateHome)) {
                    listDateFiltered.add(home)
                } else if (dateToCompare == dateHome) {
                    listDateFiltered.add(home)
                }
            }
            viewModel?.listHomesFiltered?.value = listDateFiltered
        }
        Toast.makeText(
            contextForToast,
            "Filter succeed! You have " + viewModel?.listHomesFiltered?.value!!.size + " results found!",
            Toast.LENGTH_SHORT
        ).show()
    }
}