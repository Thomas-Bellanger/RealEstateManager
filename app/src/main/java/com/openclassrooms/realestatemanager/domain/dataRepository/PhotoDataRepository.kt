package com.openclassrooms.realestatemanager.domain.dataRepository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.PhotoDao
import com.openclassrooms.realestatemanager.domain.firebaseManager.PhotoManager
import com.openclassrooms.realestatemanager.model.PhotoModel

class PhotoDataRepository(private val photoDao: PhotoDao) {
    private val photoManager: PhotoManager = PhotoManager.getInstance()

    //create
    fun createPhoto(photo: PhotoModel?) {
        photo?.uid = photoDao.insertPhoto(photo)
        val imageUri = Uri.parse(photo?.image)
        photoManager.changeImageForUrl(
            imageUri,
            photo,
            photo?.uid.toString(),
            photo?.homeUid.toString()
        )
        photoDao.insertPhoto(photo)
    }

    //delete
    fun deletePhoto(uid: Long) {
        photoManager.deleteImage(uid)
        photoDao.deletePhoto(uid)
    }

    //get
    fun getPhotos(homeUid: Long): LiveData<List<PhotoModel>> {
        return this.photoDao.getPhotos(homeUid)
    }

    fun getPhoto(uid: Long): LiveData<PhotoModel> {
        return this.photoDao.getPhoto(uid)
    }
}