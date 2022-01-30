package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
class HomeModel(
    @PrimaryKey
    var uid: Long,
    var avatar: String,
    var type: String,
    var city: String,
    var price: Double,
    var street: String,
    var appartment: String?,
    var postalCode: String,
    var country: String,
    var surface: Int,
    var roomNumber: Int,
    var bathRoomNumber: Int,
    var bedRoomNumber: Int,
    var location: String,
    var description: String
) {
    companion object {
        var testHome = HomeModel(
            0L,
            "avatar",
            "Penthouse",
            "L.A",
            1000.00,
            "3 rue de france",
            null,
            "77950",
            "United States",
            80,
            4,
            1,
            2,
            "location",
            "for test purpose"
        )
        var testHome2 = HomeModel(
            1L,
            "avatar",
            "House",
            "N.Y",
            5000.00,
            "18 rue de L.A",
            null,
            "90516",
            "United States",
            60,
            2,
            1,
            2,
            "location",
            "for test purpose"
        )
    }
}