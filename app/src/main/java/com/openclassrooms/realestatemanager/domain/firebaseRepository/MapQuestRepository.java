package com.openclassrooms.realestatemanager.domain.firebaseRepository;

import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.model.apiModel.ResponseApi;
import com.openclassrooms.realestatemanager.utils.ApiAdressService;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapQuestRepository {
    private static volatile MapQuestRepository instance;

    //manager instance
    private MapQuestRepository() {
    }

    //repository instance
    public static MapQuestRepository getInstance() {
        MapQuestRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (MapQuestRepository.class) {
            if (instance == null) {
                instance = new MapQuestRepository();
            }
            return instance;
        }
    }

    public void getLatLng(Callbacks callbacks, String location) {
        //Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);
        // get retrofit instance
        ApiAdressService apiAdressService = ApiAdressService.retrofit.create(ApiAdressService.class);
        // create the call on the API
        Call<ResponseApi> liveDataCall = apiAdressService.getLatLng(location);
        // 2.4 - Start the call
        liveDataCall.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> liveDataCall, Response<ResponseApi> response) {
                // Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    //Callbacks used for Call
    public interface Callbacks {
        void onResponse(@Nullable ResponseApi response);

        void onFailure();
    }
}