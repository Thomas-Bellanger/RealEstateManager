package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.model.LocationModel
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class UnitTest {
    var listHomeTest: MutableList<HomeModel> = ArrayList()
    var listPhotoTest: MutableList<PhotoModel> = ArrayList()
    var homeToTest = HomeModel(
        0,
        "avatar",
        "Duplex",
        "N.Y",
        2000.00,
        "30 rue de NY",
        null,
        "49055",
        "United States",
        90,
        5,
        2,
        3,
        1,
        "for test purpose",
        "30/01/21",
        true,
        "05/02/21",
        "05/02/21",
        "Tom",
        true,
        false,
        false,
        false
    )
    var photoToTest = PhotoModel(0, 0, "PhotoTest", "No Image")
    var testLocation = LocationModel(
        1,
        "https://www.mapquestapi.com/staticmap/v5/map?key=bld6sIlLn6IbKlr00OXoiUSNRq4tIjG3&center=Boston,MA&size=600,400@2x",
        2.0,
        1.3
    )

    @Test
    fun convertEuroToDollar() {
        val result = Utils.convertEuroToDollar(100)
        assert(result == 113)
    }

    @Test
    fun getDate(){
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        assert(Utils.getTodayDate() == dateFormat.format(Date()))
    }

    @Test
    fun checkUid() {
        assert(homeToTest.uid == 0L)
    }

    @Test
    fun checkAvatar() {
        assert(homeToTest.avatar == "avatar")
    }

    @Test
    fun checkType() {
        assert(homeToTest.type == "Duplex")
    }

    @Test
    fun checkCity() {
        assert(homeToTest.city == "N.Y")
    }

    @Test
    fun checkPrice() {
        assert(homeToTest.price == 2000.00)
    }

    @Test
    fun checkStreet() {
        assert(homeToTest.street == "30 rue de NY")
    }

    @Test
    fun checkApartment() {
        assert(homeToTest.appartment == null)
    }

    @Test
    fun checkPostalCode() {
        assert(homeToTest.postalCode == "49055")
    }

    @Test
    fun checkCountry() {
        assert(homeToTest.country == "United States")
    }

    @Test
    fun checkSurface() {
        assert(homeToTest.surface == 90)
    }

    @Test
    fun checkRoomNumber() {
        assert(homeToTest.roomNumber == 5)
    }

    @Test
    fun checkBedroomNumber() {
        assert(homeToTest.bedRoomNumber == 3)
    }

    @Test
    fun checkBathRoomNumber() {
        assert(homeToTest.bathRoomNumber == 2)
    }

    @Test
    fun checkDescription() {
        assert(homeToTest.description == "for test purpose")
    }

    @Test
    fun checkCreationTime() {
        assert(homeToTest.creationTime == "30/01/21")
    }

    @Test
    fun checkIsSold() {
        assert(homeToTest.isSold)
    }

    @Test
    fun checkSellTime() {
        assert(homeToTest.sellTime == "05/02/21")
    }

    @Test
    fun checkAgent() {
        assert(homeToTest.sellerName == "Tom")
    }

    @Test
    fun checkSchool() {
        assert(homeToTest.school)
    }

    @Test
    fun checkPark() {
        assert(!homeToTest.park)
    }

    @Test
    fun checkStation() {
        assert(!homeToTest.station)
    }

    @Test
    fun checkShop() {
        assert(!homeToTest.shops)
    }

    @Test
    fun checkLastModifiedTime() {
        assert(homeToTest.lastModifTime == "05/02/21")
    }

    @Test
    fun checkPhotoHomeUid() {
        assert(photoToTest.homeUid == 0L)
    }

    @Test
    fun checkPhotoUid() {
        assert(photoToTest.uid == 0L)
    }

    @Test
    fun checkPhotoTitle() {
        assert(photoToTest.title == "PhotoTest")
    }

    @Test
    fun addHomeWithSuccess() {
        listHomeTest.add(homeToTest)
        assert(listHomeTest.contains(homeToTest))
    }

    @Test
    fun removeHomeWithSuccess() {
        listHomeTest.add(homeToTest)
        listHomeTest.remove(homeToTest)
        assert(!listHomeTest.contains(homeToTest))
    }

    @Test
    fun addPhotoWithSuccess() {
        listPhotoTest.add(photoToTest)
        assert(listPhotoTest.contains(photoToTest))
    }

    @Test
    fun removePhotoWithSuccess() {
        listPhotoTest.add(photoToTest)
        listPhotoTest.remove(photoToTest)
        assert(!listPhotoTest.contains(photoToTest))
    }

    @Test
    fun testLocationUrl() {
        assert(testLocation.url == "https://www.mapquestapi.com/staticmap/v5/map?key=bld6sIlLn6IbKlr00OXoiUSNRq4tIjG3&center=Boston,MA&size=600,400@2x")
    }

    @Test
    fun testLocationLng() {
        assert(testLocation.lng == 1.3)
    }

    @Test
    fun testLocationLat() {
        assert(testLocation.lat == 2.0)
    }

    @Test
    fun testLocationUid() {
        assert(testLocation.homeUid == 1L)
    }
}