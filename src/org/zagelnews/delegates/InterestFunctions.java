package org.zagelnews.delegates;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.zagelnews.constants.ServerConstants;
import org.zagelnews.utils.ServiceHandler;

import android.app.Activity;


/**
 * user interests related API
 * @author oomran
 *
 */
public class InterestFunctions {

	private Activity activity;
	
    //URL of the PHP API
    private static String loadInterestURL = ServerConstants.SERVER_URL+"/GetInterestsList";
    private static String addInterestURL = ServerConstants.SERVER_URL+"/AddInterest";
    private static String removeInterestURL = ServerConstants.SERVER_URL+"/RemoveInterest";
    
    
    // constructor
    public InterestFunctions(Activity activity){
    	this.activity = activity;
    }


    /**
     * @return the areas of interest for the current user
     */
    public JSONArray loadInterests(){
    	JSONArray json = ServiceHandler.makeServiceCallArr(activity, loadInterestURL);
        return json;
    }
    
    /**
     * add new interest area to the current user
     * @param latitude
     * @param longitude
     * @param radious
     * @param interestName
     * @return
     */
    public JSONObject addNewInterest(Double latitude, Double longitude, Double radious, String interestName){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("latitude", latitude==null?"":latitude+""));
        params.add(new BasicNameValuePair("longitude", longitude==null?"":longitude+""));
        params.add(new BasicNameValuePair("radious", radious==null?"":radious+""));
        params.add(new BasicNameValuePair("interestName", interestName));
        JSONObject json = ServiceHandler.makeServiceCall(activity, addInterestURL, params);
        return json;
    }
    

    /**
     * delete the given interest areas from the user
     * @param interestIds
     * @return
     */
    public JSONObject deleteInterests(String interestIds){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("interestIds", interestIds));
        JSONObject json = ServiceHandler.makeServiceCall(activity, removeInterestURL, params);
        return json;
    }
}

