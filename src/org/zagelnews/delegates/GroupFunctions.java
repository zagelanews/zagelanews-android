package org.zagelnews.delegates;

import org.json.JSONArray;
import org.zagelnews.constants.ServerConstants;
import org.zagelnews.utils.ServiceHandler;

import android.app.Activity;


public class GroupFunctions {
	
	private Activity activity;

    private static String LoadMyActiveGroupsURL = ServerConstants.SERVER_URL+"/LoadMyActiveGroups";
    
    // constructor
    public GroupFunctions(Activity activity){
    	this.activity = activity;
    }


	/**
	 * @param userId
	 * @param userToken
	 * @param zone
	 * @return simple list of the groups I am member of
	 */
	public JSONArray loadMyActiveGroups() {
		JSONArray json = ServiceHandler.makeServiceCallArr(activity, LoadMyActiveGroupsURL);
        return json;
    }
	
}

