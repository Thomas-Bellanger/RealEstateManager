package com.openclassrooms.realestatemanager.domain.firebaseManager;

import com.openclassrooms.realestatemanager.domain.firebaseRepository.MapQuestRepository;

public class MapQuestManager {
    private static volatile MapQuestManager instance;
    private final MapQuestRepository mapQuestRepository;

    //manager instance
    private MapQuestManager() {
        mapQuestRepository = MapQuestRepository.getInstance();
    }

    public static MapQuestManager getInstance() {
        MapQuestManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (MapQuestManager.class) {
            if (instance == null) {
                instance = new MapQuestManager();
            }
            return instance;
        }
    }

    public void getLatLng(MapQuestRepository.Callbacks callbacks, String location) {
        mapQuestRepository.getLatLng(callbacks, location);
    }

}
