package org.zagelnews.tasks.users;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.R;
import org.zagelnews.activities.users.InterestsListActivity;
import org.zagelnews.adapters.InterestsAdapter;
import org.zagelnews.db.DatabaseHandler;
import org.zagelnews.delegates.InterestFunctions;
import org.zagelnews.dtos.users.UserInterestDto;
import org.zagelnews.utils.ClientConfiguration;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

/**
 * Load the interest areas of the current user
 * @author oomran
 *
 */
public class LoadUserInterestsTask extends AsyncTask<String,String,JSONArray>
{
	private InterestsListActivity activity;
	
	
	public LoadUserInterestsTask(InterestsListActivity activity) {
		super();
		this.activity = activity;
	}

	@Override
	protected void onPreExecute(){
		super.onPreExecute();
	}

	/**
	 * Gets current device state and checks for working internet connection by trying Google.
	 **/
	@Override
	protected JSONArray doInBackground(String... args){
		InterestFunctions userFunction = new InterestFunctions(activity);
		JSONArray json = null;
		json = userFunction.loadInterests();
		return json;

	}

	@Override
	protected void onPostExecute(JSONArray zagelnewsDtoObjArr){
		try {
			if(zagelnewsDtoObjArr==null){
				return;
			}

			if (zagelnewsDtoObjArr!=null) {
				ArrayList<UserInterestDto> db = new ArrayList<UserInterestDto>();

				//fill the FilteredFeedsListActivity list from the server
				for (int i=0;i<zagelnewsDtoObjArr.length();i++){ 
					JSONObject jsonObject = (JSONObject)zagelnewsDtoObjArr.get(i);
					UserInterestDto userInterest = new UserInterestDto();
					userInterest.setCreateTimestamp(jsonObject.getString("createTimestamp"));
					userInterest.setInterestKey(jsonObject.getInt("interestKey"));
					userInterest.setInterestName(jsonObject.getString("interestName"));
					userInterest.setLatitude(jsonObject.getDouble("latitude"));
					userInterest.setLongitude(jsonObject.getDouble("longitude"));

					Double radius = jsonObject.getDouble("radius");
					userInterest.setRadius(radius);
					String userId = ClientConfiguration.getUser(activity).get(DatabaseHandler.KEY_ID);
					userInterest.setUserId(Integer.valueOf(userId));

					db.add(userInterest);
				} 
				InterestsAdapter adapter = new InterestsAdapter(activity, db);
				activity.holder.v.setAdapter(adapter);

				if(db.size()==0){
					activity.holder.delInterest.setVisibility(View.GONE);
				}

				adapter.notifyDataSetChanged();
			}


		} catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}

	}
}