package com.openclassrooms.realestatemanager.viewModel.dataViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.domain.dataRepository.HomeDataRepository
import com.openclassrooms.realestatemanager.domain.dataRepository.LocationDataRepository
import com.openclassrooms.realestatemanager.domain.dataRepository.PhotoDataRepository
import java.util.concurrent.Executor

class ViewModelFactory(
    private val homeDataRepository: HomeDataRepository,
    private val photoDataRepository: PhotoDataRepository,
    private val locationDataRepository: LocationDataRepository,
    private val executor: Executor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            return DataViewModel(
                homeDataRepository,
                photoDataRepository,
                locationDataRepository,
                executor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}