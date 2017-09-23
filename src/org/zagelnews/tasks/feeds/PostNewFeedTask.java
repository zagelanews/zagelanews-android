package org.zagelnews.tasks.feeds;

import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.NewFeedActivity;
import org.zagelnews.constants.Constants;
import org.zagelnews.delegates.FeedsFunctions;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * add new feed
 * @author oomran
 *
 */
public class PostNewFeedTask extends AsyncTask<String,String,JSONObject> {
	
	private NewFeedActivity activity;
	private Double latitud;
	private Double longitude;

	private ProgressDialog pDialog;
	
	public PostNewFeedTask(NewFeedActivity activity, Double latitud, Double longitude){
		this.activity = activity;
		this.latitud = latitud;
		this.longitude = longitude;
	}
	
	@Override
	protected void onPreExecute() {
		
		pDialog = new ProgressDialog(activity);
		pDialog.setTitle(activity.getResources().getString(R.string.contacting_server));
		pDialog.setMessage(activity.getResources().getString(R.string.registering));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
		
		
		super.onPreExecute();
	}

	@Override
	protected JSONObject doInBackground(String... args) {
		JSONObject json = null;
		String feedText = activity.getHolder().newFeed.getText().toString();
		if(feedText!=null&&feedText.trim().length()>0){
			if(latitud!=null&&longitude!=null
					){
				FeedsFunctions feedsFunction = new FeedsFunctions(activity);
				json = feedsFunction.addNewFeed(
						feedText, 
						latitud,
						longitude, 
						activity.getFeedImgsList());
			}else{
				try {
					json = new JSONObject("{\"MANDATORY_CHECK\":\"false\"}");
				} catch (JSONException e) {
					e = null;
				}
			}
		}else{
			try {
				json = new JSONObject("{\"MANDATORY_CHECK\":\"false\"}");
			} catch (JSONException e) {
				e = null;
			}
		}
		return json;
	}

	@Override
	protected void onPostExecute(JSONObject json) {
		try {
			if(json==null){
				Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
				pDialog.dismiss();
				return;
			}
			
			if(json.has("MANDATORY_CHECK")&&!json.getBoolean("MANDATORY_CHECK")){
				Toast.makeText(activity, activity.getResources().getString(R.string.MandatoryFields), Toast.LENGTH_LONG).show();
				pDialog.dismiss();
				return;
			}


			if (!json.has(NewFeedActivity.KEY_ERROR_CODE)||
					json.getString(NewFeedActivity.KEY_ERROR_CODE)==null||
					json.getString(NewFeedActivity.KEY_ERROR_CODE).trim().length()==0) {	
				if(latitud==null||longitude==null){
					Toast.makeText(activity, activity.getResources().getString(R.string.LocationErrorFeedNotSent), Toast.LENGTH_LONG).show();
				}else{
					activity.finish();
				}
			} 

			//error occurred in one of the service
			else {
				pDialog.dismiss();

				String errorCode = json.getString(NewFeedActivity.KEY_ERROR_CODE);
				String errorMessage = json.getString(NewFeedActivity.KEY_ERROR_MESSAGE);
				Toast.makeText(activity, errorCode+":"+errorMessage, Toast.LENGTH_LONG).show();
				if(Constants.TOKEN_NOT_MATCH.equals(errorCode)){
					activity.redirectToLogin();
				}
			}
		}catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
			pDialog.dismiss();

		}finally{
			activity.setPageEnabled(true);
			pDialog.dismiss();

		}
	}
}
