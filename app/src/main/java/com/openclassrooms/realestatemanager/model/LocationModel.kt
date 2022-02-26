package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LocationModel(
    @PrimaryKey
    var homeUid: Long = 0,
    var url: String = "",
    var lat: Double? = null,
    var lng: Double? = null
) {
    constructor() : this(+1)

    companion object {
        var testLocation = LocationModel(
            url = "https://www.mapquestapi.com/staticmap/v5/map?key=bld6sIlLn6IbKlr00OXoiUSNRq4tIjG3&center=Boston,MA&size=600,400@2x",
            lat = 2.0,
            lng = 1.3
        )
    }
}