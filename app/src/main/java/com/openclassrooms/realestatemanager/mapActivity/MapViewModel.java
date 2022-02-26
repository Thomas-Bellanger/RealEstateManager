package com.openclassrooms.realestatemanager.mapActivity;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.openclassrooms.realestatemanager.model.LocationModel;
import com.openclassrooms.realestatemanager.viewModel.ViewModel;
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel {

    private static volatile MapViewModel instance;
    List<Marker> markerList = new ArrayList<>();
    int index;
    ViewModel viewModel = ViewModel.Companion.getInstance();
    LocationModel locationModel;

    //instance of the service
    public static MapViewModel getInstance() {
        MapViewModel result = instance;
        if (instance != null) {
            return result;
        } else {
            instance = new MapViewModel();
        }
        return instance;
    }

    public void addMarker(MapboxMap map, com.mapbox.mapboxsdk.geometry.LatLng latLng, DataViewModel dataViewModel) {
        Marker marker = map.addMarker(new MarkerOptions().setPosition(latLng));
        markerList.add(marker);
    }
}
