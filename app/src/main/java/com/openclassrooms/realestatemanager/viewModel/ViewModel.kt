package com.openclassrooms.realestatemanager.viewModel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.firebaseManager.HomeManager
import com.openclassrooms.realestatemanager.domain.firebaseManager.LocationManager
import com.openclassrooms.realestatemanager.domain.firebaseManager.PhotoManager
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.model.LocationModel
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel

class ViewModel {

    var home: MutableLiveData<HomeModel> =
        MutableLiveData<HomeModel>()
    var homeManager: HomeManager = HomeManager.getInstance()
    var photoManager: PhotoManager = PhotoManager.getInstance()
    var locationManager: LocationManager = LocationManager.getInstance()
    var listPhoto: MutableLiveData<MutableList<PhotoModel>> =
        MutableLiveData<MutableList<PhotoModel>>()
    private var listPhotoToAdd: MutableList<PhotoModel> = mutableListOf()
    private var listLocationToAdd: MutableList<LocationModel> = mutableListOf()
    var listHomes: MutableList<HomeModel> = mutableListOf()
    var listHomesFull: MutableList<HomeModel> = mutableListOf()
    var listHomesFiltered: MutableLiveData<MutableList<HomeModel>> =
        MutableLiveData<MutableList<HomeModel>>()
    var avatar: String = ""
    private val NOTIFICATION_ID = 7
    private val NOTIFICATION_TAG = "RealEstateManager"

    //instance
    companion object {
        @Volatile
        private var instance: ViewModel? = null
        fun getInstance(): ViewModel? {
            val result = instance
            instance = instance?.let { result } ?: ViewModel()
            return instance
        }
    }

    var moneyType: MutableLiveData<MoneyType> = MutableLiveData<MoneyType>()

    //money type class
    enum class MoneyType {
        DOLLAR, EURO
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

    //get home collection from firestore to refresh the database
    fun getHomesFromFireStore(
        context: Context,
        dataViewModel: DataViewModel,
        list: List<HomeModel>
    ) {
        listHomes = mutableListOf()
        if (Utils.isConnected(context)) {
            homeManager.homeCollection.get().addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    for (documents in it.documents) {
                        val home = documents.toObject(HomeModel::class.java)
                        if (home != null) {
                            compareToDb(home, dataViewModel, list, context)
                            listHomes.add(home)
                        }
                    }
                }
            }
            for (home in list) {
                compareTofB(home, dataViewModel, context)
            }
        } else {
            Toast.makeText(context, "No internet Available", Toast.LENGTH_SHORT).show()
        }
        listHomesFull = listHomes
    }

    //compare the home list from db to firebase
    private fun compareTofB(home: HomeModel, dataViewModel: DataViewModel, context: Context) {
        var isContainedInDb = false
        if (listHomes.isNotEmpty()) {
            for (homeFb in listHomes) {
                if (home.uid == homeFb.uid) {
                    isContainedInDb = true
                    compareHome(homeFb, home, dataViewModel, context)
                }
                if (!isContainedInDb) {
                    dataViewModel.createHome(homeFb, true, dataViewModel, context)
                }
            }
        } else {
            homeManager.createHomeFirebase(home)
        }
    }

    //compare the data from firebase to db
    private fun compareToDb(home: HomeModel, dataViewModel: DataViewModel, list: List<HomeModel>, context: Context) {
        var isContainedInFb = false
        if (list.isNotEmpty()) {
            for (homeDb in list) {
                if (home.uid == homeDb.uid) {
                    isContainedInFb = true
                    compareHome(home, homeDb, dataViewModel, context)
                }
                if (!isContainedInFb) {
                    homeManager.createHomeFirebase(homeDb)
                }
            }
        } else {
            dataViewModel.createHome(home, true, dataViewModel, context)
        }
    }

    //compare the home to check which was the last modified
    private fun compareHome(home: HomeModel, homeDb: HomeModel, dataViewModel: DataViewModel, context: Context) {
        if (home.lastModifTime > homeDb.lastModifTime) {
            dataViewModel.createHome(home, true, dataViewModel, context)
        } else {
            homeManager.createHomeFirebase(homeDb)
        }
    }

    //get photo collection from firestore to refresh the database
    fun getPhotosFromFireStore(
        context: Context,
        dataViewModel: DataViewModel,
        list: List<PhotoModel>
    ) {
        listPhotoToAdd = mutableListOf()
        if (Utils.isConnected(context)) {
            photoManager.photoCollection.get().addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    for (documents in it.documents) {
                        val photo = documents.toObject(PhotoModel::class.java)
                        if (photo != null) {
                            if (photo.homeUid == home.value?.uid) {
                                compareToDbPhoto(photo, dataViewModel, list)
                                listPhotoToAdd.add(photo)
                            }
                        }
                    }
                }
            }
            for (photo in list) {
                compareTofBPhoto(photo, dataViewModel)
            }
        } else {
            Toast.makeText(context, "No internet Available", Toast.LENGTH_SHORT).show()
        }
    }

    //compare the photo list from db to firestore
    private fun compareTofBPhoto(photoModel: PhotoModel, dataViewModel: DataViewModel) {
        var isContainedInFb = false
        if (listPhotoToAdd.isNotEmpty()) {
            for (photoFb in listPhotoToAdd) {
                if (photoModel.uid == photoFb.uid) {
                    isContainedInFb = true
                }
                if (!isContainedInFb) {
                    val imageUri = Uri.parse(photoModel.image)
                    photoManager.changeImageForUrl(
                        imageUri,
                        photoModel,
                        photoModel.uid.toString(),
                        photoModel.homeUid.toString()
                    )
                }
            }
        } else {
            val imageUri = Uri.parse(photoModel.image)
            photoManager.changeImageForUrl(
                imageUri,
                photoModel,
                photoModel.uid.toString(),
                photoModel.homeUid.toString()
            )
        }
    }

    //compare the data from firebase to db
    private fun compareToDbPhoto(
        photoModel: PhotoModel,
        dataViewModel: DataViewModel,
        list: List<PhotoModel>
    ) {
        var isContainedInDb = false
        if (list.isNotEmpty()) {
            for (photoDb in list) {
                if (photoModel.uid == photoDb.uid) {
                    isContainedInDb = true
                }
            }
            if (!isContainedInDb) {
                dataViewModel.createPhoto(photoModel)
            }
        } else {
            dataViewModel.createPhoto(photoModel)
        }
    }

    //get Locations from firebase and compare it to db
    fun getLocationsFromFireStore(
        context: Context,
        dataViewModel: DataViewModel,
        list: List<LocationModel>
    ) {
        listLocationToAdd = mutableListOf()
        if (Utils.isConnected(context)) {
            locationManager.locationCollection.get().addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    for (documents in it.documents) {
                        val location = documents.toObject(LocationModel::class.java)
                        if (location != null) {
                            compareToDbLocation(location, dataViewModel, list)
                            listLocationToAdd.add(location)
                        }
                    }
                }
            }
            for (location in list) {
                compareTofBLocation(location, dataViewModel)
            }
        } else {
            Toast.makeText(context, "No internet Available", Toast.LENGTH_SHORT).show()
        }
    }

    //compare the photo list from db to firestore
    private fun compareTofBLocation(location: LocationModel, dataViewModel: DataViewModel) {
        var isContainedInFb = false
        if (listLocationToAdd.isNotEmpty()) {
            for (locationFb in listLocationToAdd) {
                if (location.homeUid == locationFb.homeUid) {
                    isContainedInFb = true
                }
                if (!isContainedInFb) {
                    locationManager.createLocationFirebase(location)
                }
            }
        } else {
            locationManager.createLocationFirebase(location)
        }
    }

    //compare the data from firebase to db
    private fun compareToDbLocation(
        location: LocationModel,
        dataViewModel: DataViewModel,
        list: List<LocationModel>
    ) {
        if (list.isNotEmpty()) {
            for (locationDb in list) {
                var isContainedInDb = false
                if (location.homeUid == locationDb.homeUid) {
                    isContainedInDb = true
                }
                if (!isContainedInDb) {
                    dataViewModel.createLocationFromFireBase(location)
                }
            }
        } else {
            dataViewModel.createLocationFromFireBase(location)
        }
    }
}