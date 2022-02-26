package com.openclassrooms.realestatemanager.domain.firebaseRepository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.realestatemanager.model.HomeModel;
import com.openclassrooms.realestatemanager.model.PhotoModel;

public class HomeRepository {
    private static final String HOME_COLLECTION = "homes";
    private static volatile HomeRepository instance;


    private HomeRepository() {
    }

    //repository instance
    public static HomeRepository getInstance() {
        HomeRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (HomeRepository.class) {
            if (instance == null) {
                instance = new HomeRepository();
            }
            return instance;
        }
    }

    //get the Restaurant collection in firebase
    public CollectionReference getHomeCollection() {
        return FirebaseFirestore.getInstance().collection(HOME_COLLECTION);
    }

    //get the data for a restaurant in firebase
    public Task<DocumentSnapshot> getPhotoData(PhotoModel photo) {
        Long uid = photo.getUid();
        return this.getHomeCollection().document(uid.toString()).get();
    }

    //add a restaurant to collection in firebase
    public void createHomeFirebase(HomeModel home) {
        Long uid = home.getUid();
        this.getHomeCollection().document(uid.toString()).set(home);
    }

    //delete home from firestore
    public void deleteHomeFromFirestore(HomeModel home) {
        Long uid = home.getUid();
        this.getHomeCollection().document(uid.toString()).delete();
    }
}
