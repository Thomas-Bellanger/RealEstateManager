package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HomeModel(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0,
    var avatar: String = "",
    var type: String = "",
    var city: String = "",
    var price: Double = 0.0,
    var street: String = "",
    var appartment: String? = null,
    var postalCode: String = "",
    var country: String = "",
    var surface: Int = 0,
    var roomNumber: Int = 0,
    var bathRoomNumber: Int = 0,
    var bedRoomNumber: Int = 0,
    var photoNumber: Int = 0,
    var description: String = "",
    var creationTime: String = "",
    var isSold: Boolean = false,
    var sellTime: String = "",
    var lastModifTime: String = "",
    var sellerName: String ="",
    var school:Boolean = false,
    var shops: Boolean = false,
    var station: Boolean = false,
    var park: Boolean = false
) {
    constructor() : this(+1)

    companion object {
        var testHome = HomeModel(
            avatar = "avatar",
            type = "Penthouse",
            city = "L.A",
            price = 1000.00,
            street = "3 rue de france",
            appartment = null,
            postalCode = "77950",
            country = "United States",
            surface = 80,
            roomNumber = 4,
            bedRoomNumber = 2,
            bathRoomNumber = 1,
            photoNumber = 1,
            description = "for test purpose",
            creationTime = "30/01/22",
            isSold = false,
            sellTime = "",
            lastModifTime = "30/01/22",
            sellerName ="",
            school = true,
            shops = false,
            station = false,
            park = false
        )
        var testHome2 = HomeModel(
            avatar = "avatar",
            type = "Duplex",
            city = "N.Y",
            price = 2000.00,
            street = "30 rue de NY",
            appartment = null,
            postalCode = "49055",
            country = "United States",
            surface = 90,
            roomNumber = 5,
            bedRoomNumber = 2,
            bathRoomNumber = 3,
            photoNumber = 1,
            description = "for test purpose",
            creationTime = "30/01/21",
            isSold = true,
            sellTime = "05/02/21",
            lastModifTime = "05/02/21",
            sellerName ="",
            school = true,
            shops = false,
            station = false,
            park = false
        )
    }
}