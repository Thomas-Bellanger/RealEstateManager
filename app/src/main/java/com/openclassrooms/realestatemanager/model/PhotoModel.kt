package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PhotoModel(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0,
    var homeUid: Long = 0,
    var title: String? = null,
    var image: String? = null
) {
    constructor() : this(+1)

    companion object {
        var NO_PHOTO = PhotoModel(homeUid = 20, title = "No Photo Available", image = "")
        var PHOTO_TEST = PhotoModel(
            homeUid = 1,
            title = "No Photo Available",
            image = "content://media/external/images/media/31"
        )
    }
}
