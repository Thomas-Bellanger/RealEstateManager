package com.openclassrooms.realestatemanager.domain.firebaseManager;

import android.net.Uri;

import com.google.firebase.firestore.CollectionReference;
import com.openclassrooms.realestatemanager.domain.firebaseRepository.PhotoRepository;
import com.openclassrooms.realestatemanager.model.PhotoModel;

public class PhotoManager {
    private static volatile PhotoManager instance;
    private final PhotoRepository photoRepository;

    //manager instance
    private PhotoManager() {
        photoRepository = PhotoRepository.getInstance();
    }

    public static PhotoManager getInstance() {
        PhotoManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (PhotoManager.class) {
            if (instance == null) {
                instance = new PhotoManager();
            }
            return instance;
        }
    }

    public CollectionReference getPhotoCollection() {
        return photoRepository.getPhotoCollection();
    }

    public void changeImageForUrl(Uri imageUri, PhotoModel photoModel, String uid, String homeUid) {
        photoRepository.uploadImage(imageUri, homeUid, uid).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                photoRepository.createUrlForImage(uri.toString(), photoModel, uid);
            });
        });
    }

    public void deleteImage(long uid) {
        photoRepository.getPhotoData(uid).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                PhotoModel photoToDelete = documentSnapshot.toObject(PhotoModel.class);
                photoRepository.deleteImage(photoToDelete.getImage());
                photoRepository.deletePhotoFromFirestore(photoToDelete.getUid());
            }
        });
    }
}
