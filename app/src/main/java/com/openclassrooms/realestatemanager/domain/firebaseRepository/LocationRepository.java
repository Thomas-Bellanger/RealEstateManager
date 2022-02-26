package com.openclassrooms.realestatemanager.domain.firebaseRepository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.realestatemanager.model.LocationModel;

public class LocationRepository {
    private static final String LOCATION_COLLECTION = "locations";
    private static volatile LocationRepository instance;


    private LocationRepository() {
    }

    //repository instance
    public static LocationRepository getInstance() {
        LocationRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (LocationRepository.class) {
            if (instance == null) {
                instance = new LocationRepository();
            }
            return instance;
        }
    }

    //get the Location collection in firebase
    public CollectionReference getLocationCollection() {
        return FirebaseFirestore.getInstance().collection(LOCATION_COLLECTION);
    }

    //add a restaurant to collection in firebase
    public void createLocationFirebase(LocationModel location) {
        Long uid = location.getHomeUid();
        this.getLocationCollection().document(uid.toString()).set(location);
    }

}
