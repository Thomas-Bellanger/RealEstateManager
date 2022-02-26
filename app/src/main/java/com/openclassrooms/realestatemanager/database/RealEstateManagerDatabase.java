package com.openclassrooms.realestatemanager.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.model.HomeModel;
import com.openclassrooms.realestatemanager.model.LocationModel;
import com.openclassrooms.realestatemanager.model.PhotoModel;

import java.util.concurrent.Executors;

@Database(entities = {HomeModel.class, PhotoModel.class, LocationModel.class}, version = 1, exportSchema = false)

public abstract class RealEstateManagerDatabase extends RoomDatabase {

    private static volatile RealEstateManagerDatabase INSTANCE;

    public static RealEstateManagerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RealEstateManagerDatabase.class, "dataBase1.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadExecutor().execute(() -> {
                    INSTANCE.photoDao().insertPhoto(PhotoModel.Companion.getNO_PHOTO());
                });
            }
        };
    }

    public abstract HomeDao homeDao();

    public abstract PhotoDao photoDao();

    public abstract LocationDao locationDao();
}
