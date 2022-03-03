package com.openclassrooms.realestatemanager.viewModel.dataViewModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.domain.dataRepository.HomeDataRepository;
import com.openclassrooms.realestatemanager.domain.dataRepository.LocationDataRepository;
import com.openclassrooms.realestatemanager.domain.dataRepository.PhotoDataRepository;
import com.openclassrooms.realestatemanager.domain.firebaseManager.MapQuestManager;
import com.openclassrooms.realestatemanager.domain.firebaseRepository.MapQuestRepository;
import com.openclassrooms.realestatemanager.model.HomeModel;
import com.openclassrooms.realestatemanager.model.LocationModel;
import com.openclassrooms.realestatemanager.model.PhotoModel;
import com.openclassrooms.realestatemanager.model.apiModel.ResponseApi;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.Executor;


public class DataViewModel extends ViewModel implements MapQuestRepository.Callbacks {
    private final HomeDataRepository homeDataRepository;
    private final PhotoDataRepository photoDataRepository;
    private final LocationDataRepository locationDataRepository;
    private final Executor executor;
    private final MapQuestManager mapQuestManager = MapQuestManager.getInstance();
    private com.openclassrooms.realestatemanager.viewModel.ViewModel viewModel;
    private LocationModel locationModel;

    public DataViewModel(HomeDataRepository homeDataRepository, PhotoDataRepository photoDataRepository, LocationDataRepository locationDataRepository, Executor executor) {
        this.homeDataRepository = homeDataRepository;
        this.photoDataRepository = photoDataRepository;
        this.locationDataRepository = locationDataRepository;
        this.executor = executor;
    }

    public LiveData<List<HomeModel>> getHomes() {
        return homeDataRepository.getHomes();
    }

    public LiveData<HomeModel> getHome(Long uid) {
        return homeDataRepository.getHome(uid);
    }

    public void createHome(HomeModel home, Boolean net, DataViewModel dataViewModel, Context context) {
        executor.execute(() -> {
            homeDataRepository.createHome(home, net, dataViewModel, context);
        });
    }

    public void editHome(HomeModel home) {
        executor.execute(() -> {
            homeDataRepository.editHome(home);
        });
    }

    public LiveData<List<HomeModel>> searchHome(String type,
                                                Boolean isSold,
                                                int status,
                                                int surfaceMin,
                                                int surfaceMax,
                                                Double priceMax,
                                                int roomNumber,
                                                int bedRoomNumber,
                                                int bathRoomNumber,
                                                int photoNumber,
                                                String street,
                                                String country,
                                                String postal,
                                                String city,
                                                Boolean train,
                                                Boolean shop,
                                                Boolean school,
                                                Boolean park) {
        return homeDataRepository.searchHome(type, isSold, status, surfaceMin, surfaceMax, priceMax, roomNumber, bedRoomNumber, bathRoomNumber, photoNumber, street, country, postal, city,train, shop, school, park );
    }

    public LiveData<List<PhotoModel>> getPhotos(Long homeUid) {
        return photoDataRepository.getPhotos(homeUid);
    }

    public void createPhoto(PhotoModel photo) {
        executor.execute(() -> {
            photoDataRepository.createPhoto(photo);
        });
    }

    public void createLocation(HomeModel homeModel, Context context) {
        LocationModel location = new LocationModel(
                homeModel.getUid(),
                "https://www.mapquestapi.com/staticmap/v5/map?key=" + BuildConfig.API_KEY + "&locations=" + homeModel.getStreet() + "," + homeModel.getCity() + "," + homeModel.getCountry() + "&defaultMarker=marker-sm-22407F-3B5998&size=250,250@2x",
                null,
                null);
        locationModel = location;
        Glide.with(context)
                .asBitmap()
                .load(locationModel.getUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream baos=new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.PNG,100, baos);
                        byte [] arr=baos.toByteArray();
                        String result=Base64.encodeToString(arr, Base64.DEFAULT);
                        locationModel.setUrl(result);
                        apiCall(homeModel);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
    private void apiCall(HomeModel homeModel){
        String street = homeModel.getStreet();
        String city = homeModel.getCity();
        String postal = homeModel.getPostalCode();
        String country = homeModel.getCountry();
        mapQuestManager.getLatLng(this, street + "," + city + "," + postal + "," + country);
    }

    public void createLocationFromFireBase(LocationModel location) {
        executor.execute(() -> {
            locationDataRepository.createLocation(location);
        });
    }

    public void editLocation(LocationModel location) {
        locationDataRepository.editLocation(location);
    }

    public LiveData<LocationModel> getLocation(Long uid) {
        return locationDataRepository.getLocation(uid);
    }

    public LiveData<List<LocationModel>> getAllLocations() {
        return locationDataRepository.getAllLocations();
    }

    public void deletePhoto(Long photoUid) {
        executor.execute(() -> {
            photoDataRepository.deletePhoto(photoUid);
        });
    }

    @Override
    public void onResponse(@Nullable ResponseApi response) {
        Double lat = response.getResults().get(0).getLocations().get(0).getLatLng().getLat();
        Double lng = response.getResults().get(0).getLocations().get(0).getLatLng().getLng();
        locationModel.setLat(lat);
        locationModel.setLng(lng);
        executor.execute(() -> {
            locationDataRepository.createLocation(locationModel);
        });
    }

    @Override
    public void onFailure() {
        executor.execute(() -> {
            locationDataRepository.createLocation(locationModel);
        });
    }
}