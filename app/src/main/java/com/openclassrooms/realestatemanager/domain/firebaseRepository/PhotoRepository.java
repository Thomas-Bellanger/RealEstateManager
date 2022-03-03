package com.openclassrooms.realestatemanager.domain.firebaseRepository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.openclassrooms.realestatemanager.model.PhotoModel;

import java.util.UUID;

public class PhotoRepository {
    private static final String PHOTO_COLLECTION = "photos";
    private static volatile PhotoRepository instance;


    private PhotoRepository() {
    }

    //repository instance
    public static PhotoRepository getInstance() {
        PhotoRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (PhotoRepository.class) {
            if (instance == null) {
                instance = new PhotoRepository();
            }
            return instance;
        }
    }

    //get the Restaurant collection in firebase
    public CollectionReference getPhotoCollection() {
        return FirebaseFirestore.getInstance().collection(PHOTO_COLLECTION);
    }

    //get the data for a restaurant in firebase
    public Task<DocumentSnapshot> getPhotoData(long uid) {
        return this.getPhotoCollection().document(Long.toString(uid)).get();
    }

    //delete home from firestore
    public void deletePhotoFromFirestore(Long uid) {
        this.getPhotoCollection().document(uid.toString()).delete();
    }

    public UploadTask uploadImage(Uri imageUri, String homeUid, String id) {
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(homeUid + "/" + id);
        return mImageRef.putFile(imageUri);
    }

    public void deleteImage(String imageUri) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference photoRef = firebaseStorage.getReferenceFromUrl(imageUri);
        photoRef.delete().addOnSuccessListener(unused -> {
            //deleted
        });
    }

    public void createUrlForImage(String urlImage, PhotoModel photoModel, String uid) {
        // Creating url image for photomodel
        photoModel.setImage(urlImage);
        this.getPhotoCollection().document(uid).set(photoModel);
    }
}
