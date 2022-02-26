package com.openclassrooms.realestatemanager.viewModel.dataViewModel

import android.content.Context
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.domain.dataRepository.HomeDataRepository
import com.openclassrooms.realestatemanager.domain.dataRepository.LocationDataRepository
import com.openclassrooms.realestatemanager.domain.dataRepository.PhotoDataRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {
    private var homeDataRepository: HomeDataRepository? = null
    private var photoDataRepository: PhotoDataRepository? = null
    private var locationDataRepository: LocationDataRepository? = null
    private fun provideHomeDataSource(context: Context?): HomeDataRepository? {
        if (homeDataRepository == null) {
            val database = RealEstateManagerDatabase.getInstance(context)
            homeDataRepository = HomeDataRepository(database.homeDao())
        }
        return homeDataRepository
    }

    private fun providePhotoDataSource(context: Context?): PhotoDataRepository? {
        if (photoDataRepository == null) {
            val database = RealEstateManagerDatabase.getInstance(context)
            photoDataRepository = PhotoDataRepository(database.photoDao())
        }
        return photoDataRepository
    }

    private fun provideLocationDataSource(context: Context?): LocationDataRepository? {
        if (locationDataRepository == null) {
            val database = RealEstateManagerDatabase.getInstance(context)
            locationDataRepository = LocationDataRepository(database.locationDao())
        }
        return locationDataRepository
    }

    private fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context?): ViewModelFactory {
        val dataSourceHome = provideHomeDataSource(context)
        val dataSourcePhoto = providePhotoDataSource(context)
        val dataSourceLocation = provideLocationDataSource(context)
        val executor = provideExecutor()
        return ViewModelFactory(dataSourceHome!!, dataSourcePhoto!!, dataSourceLocation!!, executor)
    }
}