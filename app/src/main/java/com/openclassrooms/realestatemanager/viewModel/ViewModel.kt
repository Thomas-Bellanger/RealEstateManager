package com.openclassrooms.realestatemanager.viewModel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.manager.HomeManager
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.model.PhotoModel

class ViewModel {

    var home = MutableLiveData<HomeModel>()
    var homeManager = HomeManager.getInstance()
    var listPhoto: MutableLiveData<MutableList<PhotoModel>> =
        MutableLiveData<MutableList<PhotoModel>>()
    var listHomes: MutableLiveData<MutableList<HomeModel>> =
        MutableLiveData<MutableList<HomeModel>>()
    private val NOTIFICATION_ID = 7
    private val NOTIFICATION_TAG = "RealEstateManager"
    var isAppartment: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    companion object {
        @Volatile
        private var instance: ViewModel? = null
        fun getInstance(): ViewModel? {
            val result = instance
            instance = instance?.let { result } ?: ViewModel()
            return instance
        }
    }

    fun getRestaurantsFromFireBase() {
        homeManager.homeCollection.get().addOnSuccessListener { }
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
    }

    fun createHomeFireBase(
        avatar: String,
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
        description: String,
        listPhoto: MutableList<PhotoModel>
    ) {
        val homeToCreate = HomeModel(
            avatar,
            type,
            city,
            price,
            street,
            appartment,
            postalCode,
            country,
            surface,
            roomNumber,
            bathRoomNumber,
            bedRoomNumber,
            location,
            uid,
            description,
            listPhoto
        )
        homeManager.createHomeFirebase(homeToCreate)
    }

    fun sendVisualNotification(context: Context, notificationManagers: NotificationManager) {
        // Create a Channel (Android 8)
        val channelId = "0"

        // Build a Notification object
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.mapbox_logo_icon)
                .setContentTitle("Real Estate Manager!")
                .setContentText(
                    "The list of home has changed!"
                )
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName: CharSequence = "Messages"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, channelName, importance)
            notificationManagers.createNotificationChannel(mChannel)
        }

        // Show notification
        notificationManagers.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build())
    }

    fun compareString(type: String) {
        isAppartment.value = type == "Appartment"
    }
}