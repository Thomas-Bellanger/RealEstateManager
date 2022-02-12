package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.openclassrooms.realestatemanager.model.HomeModel;
import com.openclassrooms.realestatemanager.model.PhotoModel;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    List<HomeModel> listHomeTest = new ArrayList<>();
    List<PhotoModel> listPhotoTest = new ArrayList<>();
    public HomeModel homeToTest =new HomeModel( 0,
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
           2,
           1,
           "for test purpose",
            "30/01/22",
           false,
            "",
           "30/01/22");
    public PhotoModel photoToTest = new PhotoModel(0,0,"PhotoTest","No Image");

    @Test
    public void convertEuroToDollar(){
        int result = Utils.convertEuroToDollar(100);
        assert (result==113);
    }
    @Test
    public void getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assert (Utils.getTodayDate().equals(dateFormat.format(new Date())));
    }
    @Test
    public void checkUid(){
        assert (homeToTest.getUid()==0);
    }
    @Test
    public void checkAvatar(){
        assert (homeToTest.getAvatar().equals("avatar"));
    }
    @Test
    public void checkType(){
        assert (homeToTest.getType().equals("Penthouse"));
    }
    @Test
    public void checkCity(){
        assert (homeToTest.getCity().equals("L.A"));
    }
    @Test
    public void checkPrice(){
        assert (homeToTest.getPrice()==1000.00);
    }
    @Test
    public void checkStreet(){
        assert (homeToTest.getStreet().equals("3 rue de france"));
    }
    @Test
    public void checkApartment(){
        assert (homeToTest.getAppartment() == null);
    }
    @Test
    public void checkPostalCode(){
        assert (homeToTest.getPostalCode().equals("77950"));
    }
    @Test
    public void checkCountry(){
        assert (homeToTest.getCountry().equals("United States"));
    }
    @Test
    public void checkSurface(){
        assert (homeToTest.getSurface()==80);
    }
    @Test
    public void checkRoomNumber(){
        assert (homeToTest.getRoomNumber()==4);
    }
    @Test
    public void checkBedroomNumber(){
        assert (homeToTest.getBedRoomNumber()==1);
    }
    @Test
    public void checkBathRoomNumber(){
        assert (homeToTest.getBathRoomNumber()==2);
    }
    @Test
    public void checkDescription(){
        assert (homeToTest.getDescription().equals("for test purpose"));
    }
    @Test
    public void checkCreationTime(){
        assert (homeToTest.getCreationTime().equals("30/01/22"));
    }
    @Test
    public void checkIsSold(){
        assertFalse (homeToTest.isSolde());
    }
    @Test
    public void checkSellTime(){
        assert (homeToTest.getSellTime().equals(""));
    }
    @Test
    public void checkLastModifiedTime(){
        assert (homeToTest.getLastModifTime().equals("30/01/22"));
    }
    @Test
    public void checkPhotoHomeUid(){
        assert (photoToTest.getHomeUid()==0);
    }
    @Test
    public void checkPhotoUid(){
        assert (photoToTest.getUid()==0);
    }
    @Test
    public void checkPhotoTitle(){
        assert (photoToTest.getTitle().equals("PhotoTest"));
    }
    @Test
    public void checkPhotoImage(){
        assert (photoToTest.getImage().equals("No Image"));
    }
    @Test
    public void addHomeWithSuccess(){
        listHomeTest.add(homeToTest);
        assert (listHomeTest.contains(homeToTest));
    }
    @Test
    public void removeHomeWithSuccess(){
        listHomeTest.add(homeToTest);
        listHomeTest.remove(homeToTest);
        assert (!listHomeTest.contains(homeToTest));
    }
    @Test
    public void addPhotoWithSuccess(){
        listPhotoTest.add(photoToTest);
        assert (listPhotoTest.contains(photoToTest));
    }
    @Test
    public void removePhotoWithSuccess(){
        listPhotoTest.add(photoToTest);
        listPhotoTest.remove(photoToTest);
        assert (!listPhotoTest.contains(photoToTest));
    }
}