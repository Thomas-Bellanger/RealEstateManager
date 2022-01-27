package com.openclassrooms.realestatemanager.model

class HomeModel(
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
    var uid: String,
    var description: String,
    var listPhoto: MutableList<PhotoModel>
) {
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
            "testhomeUid",
            "for test purpose",
            mutableListOf()
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
            "testHome2Uid",
            "for test purpose",
            mutableListOf()
        )
    }
}