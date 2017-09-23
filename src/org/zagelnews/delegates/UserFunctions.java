package org.zagelnews.delegates;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.zagelnews.constants.ServerConstants;
import org.zagelnews.db.DatabaseHandler;
import org.zagelnews.dtos.NamedBinaryObject;
import org.zagelnews.utils.JSONParser;
import org.zagelnews.utils.ServiceHandler;

import android.app.Activity;
import android.content.Context;


public class UserFunctions {

	private Activity activity;

	//URL of the PHP API
	private static String loginURL = ServerConstants.SERVER_URL+"/AuthenticateUser";
	private static String logoutURL = ServerConstants.SERVER_URL+"/Logout";
	private static String getProfileInfoURL = ServerConstants.SERVER_URL+"/GetProfileInfo";
	private static String registerURL = ServerConstants.SERVER_URL+"/AddUser";
	private static String updateURL = ServerConstants.SERVER_URL+"/UpdateUser";
	private static String forgetPasswordURL= ServerConstants.SERVER_URL+"/ForgetPassword";
    private static String changePasswordURL = ServerConstants.SERVER_URL+"/ChangePassword";




	// constructor
	public UserFunctions(Activity activity){
		this.activity = activity;
	}

	/**
	 * Function to LoginActivity
	 **/

	public JSONObject loginUser(String email, String password){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = ServiceHandler.makeServiceCall(activity, loginURL, params);
		return json;
	}


	/**
	 * Function to logout user
	 * Resets the temporary data stored in SQLite Database
	 * */
	public boolean resetTables(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}


	public JSONObject logout(String sessionId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sessionId", sessionId));
		JSONObject json = ServiceHandler.makeServiceCall(activity, logoutURL,params);
		return json;
	}

	public JSONObject getProfileInfo(Integer profileType, Integer inquiredProfileId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("profileType", profileType==null?"":profileType+""));
		params.add(new BasicNameValuePair("inquiredProfileId", inquiredProfileId==null?"":inquiredProfileId+""));
		JSONObject json = ServiceHandler.makeServiceCall(activity, getProfileInfoURL,params);
		return json;
	}

	/**
	 * Function to  udpate user
	 **/
	public JSONObject updateUser(
			String userId, 
			String fname, 
			String email, 
			byte [] sampleProfileImage,
			byte [] fullProfileImage,
			String zone){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("fullName", fname));
		params.add(new BasicNameValuePair("userEmail", email));

		
        
        //add feed images
        NamedBinaryObject [] feedImagesArr = new NamedBinaryObject[2];
        		feedImagesArr[0] = 
        				new NamedBinaryObject(
        						sampleProfileImage, "PROFILE.sample");
        		feedImagesArr[1] = 
        				new NamedBinaryObject(
        						fullProfileImage, "PROFILE.full");
		
		JSONObject json = ServiceHandler.makeServiceCall(activity,
				updateURL,
				params, 
				feedImagesArr);
		return json;
	}


	/**
	 * Function to  Register
	 **/
	public JSONObject registerUser(
			String fname, 
			String email, 
			String password, 
			byte [] sampleProfileImage,
			byte [] fullProfileImage,
			
			String zone){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("fullName", fname));
		params.add(new BasicNameValuePair("userEmail", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("zone", zone));
		//params.add(new BasicNameValuePair("image", image));
		JSONObject json = JSONParser.getJSONFromUrlMultiPartReq(
				registerURL,
				params, 
				sampleProfileImage,
				fullProfileImage);
		return json;
	}

	
    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean clearLocalUserData(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
    
    
	public JSONObject forgetPassword(String email) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("zone",         TimeZone.getDefault().getID()
));
        JSONObject json = JSONParser.getJSONFromUrl(forgetPasswordURL,params);
        return json;
    }
	
	
	public JSONObject changePassword(String oldPassword, String newPassword) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("oldPassword", oldPassword));
        params.add(new BasicNameValuePair("newPassword", newPassword));
        JSONObject json = ServiceHandler.
        		makeServiceCall(activity, changePasswordURL, params);

        return json;
    }
}

