package com.openclassrooms.realestatemanager;

import org.junit.Test;

import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
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
}