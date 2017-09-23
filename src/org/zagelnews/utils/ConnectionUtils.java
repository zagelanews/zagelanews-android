package org.zagelnews.utils;
/*package org.zagelnews.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtils {
	public static Boolean netCheck(ConnectivityManager cm) {
    	NetworkInfo netInfo = cm.getActiveNetworkInfo();
    	if (netInfo != null && netInfo.isConnected()) {
    		try {
    			URL url = new URL(ServerConstants.ServerURL);
    			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
    			urlc.setConnectTimeout(ServerConstants.timeoutConnection);
    			urlc.connect();
    			if (urlc.getResponseCode() == 200) {
    				return true;
    			}
    		} catch (MalformedURLException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return false;
	}
}

*/