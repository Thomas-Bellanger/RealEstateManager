package com.openclassrooms.realestatemanager.domain.firebaseManager;

import com.google.firebase.firestore.CollectionReference;
import com.openclassrooms.realestatemanager.domain.firebaseRepository.HomeRepository;
import com.openclassrooms.realestatemanager.model.HomeModel;

public class HomeManager {
    private static volatile HomeManager instance;
    private final HomeRepository homeRepository;

    //manager instance
    private HomeManager() {
        homeRepository = HomeRepository.getInstance();
    }

    public static HomeManager getInstance() {
        HomeManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (HomeManager.class) {
            if (instance == null) {
                instance = new HomeManager();
            }
            return instance;
        }
    }

    public void createHomeFirebase(HomeModel home) {
        homeRepository.createHomeFirebase(home);
    }

    public CollectionReference getHomeCollection() {
        return homeRepository.getHomeCollection();
    }

    public void deletteHomeFirebase(HomeModel home) {
        homeRepository.deleteHomeFromFirestore(home);
    }
}