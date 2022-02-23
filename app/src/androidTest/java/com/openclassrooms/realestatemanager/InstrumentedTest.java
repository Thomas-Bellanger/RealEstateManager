package com.openclassrooms.realestatemanager;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest extends ConnectivityManager.NetworkCallback {

    @Test
    public void useAppContext() throws Exception {
               assert (getApplicationContext()!=null);
    }

    @Test
    public void getActiveNetworkInfo_shouldReturnCorrectly() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        assert(Utils.isConnected(getApplicationContext())==(connected));
    }

    @Test
    public void testNetWork() {
        ConnectivityManager connectivityManager = getApplicationContext().getSystemService(ConnectivityManager.class);
        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Log.e("TAG", "The default network is now: " + network);
                assert (Utils.isConnected(getApplicationContext()));
            }

            @Override
            public void onLost(Network network) {
                Log.e("TAG", "The application no longer has a default network. The last default network was " + network);
                assert (!Utils.isConnected(getApplicationContext()));
            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                Log.e("TAG", "The default network changed capabilities: " + networkCapabilities);
                assert (Utils.isConnected(getApplicationContext()));
            }

            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                Log.e("TAG", "The default network changed link properties: " + linkProperties);
                assert (Utils.isConnected(getApplicationContext()));
            }
        });
    }
}
