package org.zagelnews.tasks.users;

import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.feeds.MapActivity;
import org.zagelnews.constants.Constants;
import org.zagelnews.delegates.InterestFunctions;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import org.zagelnews.activities.R;

/**
 * add new area of interest to the user
 * @author oomran
 *
 */
public class AddNewInterestTask extends AsyncTask<String, String, JSONObject> {
	
	private MapActivity activity;
	private LatLng center;
	private Double radious;
	private String interestName;
	
	public AddNewInterestTask(MapActivity activity, LatLng center, Double radious, String interestName){
		this.activity = activity;
		this.center = center;
		this.radious = radious;
		this.interestName = interestName;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected JSONObject doInBackground(String... args) {
		JSONObject json = null;
		if(interestName!=null&& interestName.trim().length()>0){
			InterestFunctions interestFunctions = new InterestFunctions(activity);
			json = interestFunctions.addNewInterest(center.latitude, center.longitude, radious, interestName);
		}
		return json;
	}

	@Override
	protected void onPostExecute(JSONObject zagelnewsDtoObj) {
		super.onPostExecute(zagelnewsDtoObj);
		try{
			if(zagelnewsDtoObj==null){
				activity.setInterestAddedSucc(true);
				return;
			}

			if (zagelnewsDtoObj.has(MapActivity.KEY_ERROR_CODE)&&
					zagelnewsDtoObj.getString(MapActivity.KEY_ERROR_CODE)!=null&&
					zagelnewsDtoObj.getString(MapActivity.KEY_ERROR_CODE).trim().length()>0) {
				activity.setInterestAddedSucc(false);
				String errorCode = zagelnewsDtoObj.getString(MapActivity.KEY_ERROR_CODE);
				String errorMessage = zagelnewsDtoObj.getString(MapActivity.KEY_ERROR_MESSAGE);
				Toast.makeText(activity, errorCode+":"+errorMessage, Toast.LENGTH_LONG).show();
				if(Constants.TOKEN_NOT_MATCH.equals(errorCode)){
					activity.redirectToLogin();
				}
			}else{
				activity.setInterestAddedSucc(true);
			}
		} catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}
	}
}

