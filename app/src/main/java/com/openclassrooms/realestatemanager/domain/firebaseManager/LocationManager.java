package com.openclassrooms.realestatemanager.domain.firebaseManager;

import com.google.firebase.firestore.CollectionReference;
import com.openclassrooms.realestatemanager.domain.firebaseRepository.LocationRepository;
import com.openclassrooms.realestatemanager.model.LocationModel;

public class LocationManager {
    private static volatile LocationManager instance;
    private final LocationRepository locationRepository;

    //manager instance
    private LocationManager() {
        locationRepository = LocationRepository.getInstance();
    }

    public static LocationManager getInstance() {
        LocationManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (LocationManager.class) {
            if (instance == null) {
                instance = new LocationManager();
            }
            return instance;
        }
    }

    //create
    public void createLocationFirebase(LocationModel location) {
        locationRepository.createLocationFirebase(location);
    }

    //get
    public CollectionReference getLocationCollection() {
        return locationRepository.getLocationCollection();
    }
}
