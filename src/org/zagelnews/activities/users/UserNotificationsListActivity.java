package org.zagelnews.activities.users;

import org.json.JSONObject;
import org.zagelnews.activities.BaseActivity;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.FeedDetailsActivity;
import org.zagelnews.adapters.UserFeedsNotificationsAdapter;
import org.zagelnews.delegates.FeedsFunctions;
import org.zagelnews.dtos.feeds.UserNotificationDto;
import org.zagelnews.utils.ClientConfiguration;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
public class UserNotificationsListActivity extends BaseActivity {	

	static class ViewHolder {
		ListView v;
	}
	ViewHolder holder;


	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.notifications_activities);

		holder = new ViewHolder();
		holder.v = (ListView) findViewById(R.id.notificationsList);//getListView();

		UserFeedsNotificationsAdapter adapter = new UserFeedsNotificationsAdapter(UserNotificationsListActivity.this, ClientConfiguration.userFeedsNotifications);
		holder.v.setAdapter(adapter);

		//to handle the view of the interest details
		holder.v.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent i = new Intent(getApplicationContext(), FeedDetailsActivity.class);
				UserNotificationDto notificationsDto = ((UserNotificationDto)holder.v.getAdapter().getItem(position));
				i.putExtra("feedSeq",notificationsDto.getObjectSeq());
				startActivity(i);
			
			}
		});

		new ProcessResetNotification().execute();
	}

	/**
	 * Async Task to check whether internet connection is working
	 **/

	private class ProcessResetNotification extends AsyncTask<String,String,JSONObject>
	{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}

		/**
		 * Gets current device state and checks for working internet connection by trying Google.
		 **/
		@Override
		protected JSONObject doInBackground(String... args){
			FeedsFunctions feedsFunctions = new FeedsFunctions(UserNotificationsListActivity.this);
			JSONObject json = feedsFunctions.resetUserFeedsNotifications();
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json){/*

			try {
				if(json!=null){

					JSONObject json_container = json.has("androidDto")?json.getJSONObject("androidDto"):null;
					if (json_container==null||!json_container.has(FilteredFeedsListActivity.KEY_ERROR_CODE)||json_container.getString(FilteredFeedsListActivity.KEY_ERROR_CODE)==null||json_container.getString(FilteredFeedsListActivity.KEY_ERROR_CODE).trim().length()==0) {
						if( json_container.has("zagelnewsDto")){
							JSONObject zagelnewsDtoObj = json_container.getJSONObject("zagelnewsDto");
							if(zagelnewsDtoObj.has("status")){
								Long status = zagelnewsDtoObj.getLong("status");
								if(status!=null&&status.equals(1)){
									//checkNotification();								
								}
							}
						}
					} 

					//error occured in one of the service
					else {
						String errorCode = json_container.getString(FilteredFeedsListActivity.KEY_ERROR_CODE);
						String errorMessage = json_container.getString(FilteredFeedsListActivity.KEY_ERROR_MESSAGE);
						Toast.makeText(UserNotificationsListActivity.this, errorCode+":"+errorMessage, Toast.LENGTH_LONG).show();
						if(Constants.TOKEN_NOT_MATCH.equals(errorCode)){
							redirectToLogin();
						}
					}


				}else{
					Toast.makeText(UserNotificationsListActivity.this, UserNotificationsListActivity.this.getResources().getString(R.string.netError), Toast.LENGTH_LONG).show();
				}
			}catch (JSONException e) {
				Toast.makeText(UserNotificationsListActivity.this, UserNotificationsListActivity.this.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
			}

		*/}
	}
	
	@Override
	public void getTaskResult(Object result, Integer taskId) {
	}
}
