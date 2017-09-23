package org.zagelnews.tasks.users;

import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.users.InterestsListActivity;
import org.zagelnews.constants.Constants;
import org.zagelnews.delegates.InterestFunctions;

import android.os.AsyncTask;
import android.widget.Toast;

import org.zagelnews.activities.R;

public class DeleteUserInterestTask extends AsyncTask<String,String,JSONObject>
{
	private InterestsListActivity activity;
	private String interestId;
	
	
	public DeleteUserInterestTask(InterestsListActivity activity,
			String interestId) {
		super();
		this.activity = activity;
		this.interestId = interestId;
	}

	@Override
	protected void onPreExecute(){
		super.onPreExecute();
	}

	/**
	 * Gets current device state and checks for working internet connection by trying Google.
	 **/
	@Override
	protected JSONObject doInBackground(String... args){
		if(args.length>0){
			interestId = args[0];
		}

		InterestFunctions userFunction = new InterestFunctions(activity);
		JSONObject json = null;
		json = userFunction.deleteInterests(interestId);
		return json;

	}

	@Override
	protected void onPostExecute(JSONObject json){
		try {
			if (json==null||!json.has(InterestsListActivity.KEY_ERROR_CODE)||
					json.getString(InterestsListActivity.KEY_ERROR_CODE)==null||
					json.getString(InterestsListActivity.KEY_ERROR_CODE).trim().length()==0) {
				new LoadUserInterestsTask(activity).execute();
			}
			else {
				String errorCode = json.getString(InterestsListActivity.KEY_ERROR_CODE);
				String errorMessage = json.getString(InterestsListActivity.KEY_ERROR_MESSAGE);
				Toast.makeText(activity, errorCode+":"+errorMessage, Toast.LENGTH_SHORT).show();
				if(Constants.TOKEN_NOT_MATCH.equals(errorCode)){
					activity.redirectToLogin();
				}
			}

		} catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}

	}
}

