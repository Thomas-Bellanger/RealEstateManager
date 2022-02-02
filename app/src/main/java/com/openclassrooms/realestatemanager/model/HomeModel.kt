package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class HomeModel{
    @PrimaryKey
    var uid: Long=0
    lateinit var avatar: String
    lateinit var type: String
    lateinit var city: String
    var price: Double=0.0
    lateinit var street: String
    var appartment: String? = null
    lateinit var postalCode: String
    lateinit var country: String
    var surface: Int = 0
    var roomNumber: Int = 0
    var bathRoomNumber: Int = 0
    var bedRoomNumber: Int = 0
    lateinit var location: String
    lateinit var description: String
    lateinit var creationTime: String
    var isSolde: Boolean=false
    lateinit var sellTime: String

    constructor(avatar: String,
                type: String,
                city: String,
                price: Double,
                street: String,
                appartment: String?,
                postalCode: String,
                country: String,
                surface: Int,
                roomNumber: Int,
                bathRoomNumber: Int,
                bedRoomNumber: Int,
                location: String,
                description: String,
                creationTime: String,
                isSolde: Boolean,
                sellTime: String)
    constructor(){}
    companion object {
        var testHome = HomeModel(
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
            "for test purpose",
            "30/01/22",
            false,
            ""
        )
        var testHome2 = HomeModel(
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
            "for test purpose",
            "30/01/22",
            true,
            "30/01/22"
        )
    }
}