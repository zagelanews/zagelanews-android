package org.zagelnews.tasks.users;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.R;
import org.zagelnews.constants.FeedsConstants;
import org.zagelnews.delegates.FeedsFunctions;
import org.zagelnews.dtos.feeds.UserNotificationDto;
import org.zagelnews.utils.ClientConfiguration;
import org.zagelnews.utils.FeedsUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * process the user notifications
 *
 */
public class ProcessGetNotificationsTask extends AsyncTask<String, String, JSONArray> {
	
	private Menu menu;
	private Activity activity;
	
	public ProcessGetNotificationsTask(Menu menu, Activity activity) {
		this.menu = menu;
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected JSONArray doInBackground(String... args) {
		JSONArray json = null;
		FeedsFunctions feedsFunctions = new FeedsFunctions(activity);
		json = feedsFunctions.getUserFeedsNotifications();
		return json;
	}

	@Override
	protected void onPostExecute(JSONArray json) {
		super.onPostExecute(json);
		try{
			if(json==null){
				return;
			}
			JSONArray zagelnewsDtoObjArr = json;

			if (zagelnewsDtoObjArr!=null) {
				ArrayList<UserNotificationDto> userNotificationsList = new ArrayList<UserNotificationDto>();

				//fill the FilteredFeedsListActivity list from the server
				for (int i=0;i<zagelnewsDtoObjArr.length();i++){ 
					JSONObject jsonObject = (JSONObject)zagelnewsDtoObjArr.get(i);
					UserNotificationDto UserNotificationDto = new UserNotificationDto();
					
					UserNotificationDto.setNotificationType(jsonObject.getInt("notificationType"));
					
					if(FeedsConstants.NotificationTypesConst.FEEDS.equals(UserNotificationDto.getNotificationType())){
						UserNotificationDto.setObjectSeq(jsonObject.getInt("objectSeq"));
						UserNotificationDto.setObjectDescription(jsonObject.getString("objectDescription"));
						UserNotificationDto.setNotificationCount(jsonObject.getInt("notificationCount"));
						UserNotificationDto.setNotificationTypeDesc(jsonObject.getString("notificationTypeDesc"));
						UserNotificationDto.setNotificationDate(jsonObject.getString("notificationDate"));
						UserNotificationDto.setNotificationStatus(jsonObject.getInt("notificationStatus"));
						UserNotificationDto.setNotificationClass(jsonObject.getInt("notificationClass"));

						userNotificationsList.add(UserNotificationDto);
					}
				} 

				//store the notifications into the user session
				ClientConfiguration.userFeedsNotifications = userNotificationsList;

				//check for new notifications
				checkNotification();


			}


		} catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}
	}
	
	
	/**
	 * check if there is any new notifications
	 */
	protected void checkNotification() {
		//set the notification icon
		MenuItem notificationItem = menu.getItem(1);
		if(notificationItem!=null){
			if(ClientConfiguration.userFeedsNotifications!=null&&ClientConfiguration.userFeedsNotifications.size()>0){
				if(FeedsUtils.hasNewNotifications(ClientConfiguration.userFeedsNotifications)){
					notificationItem.setIcon(activity.getResources().getDrawable(R.drawable.notifications));
				}else{
					notificationItem.setIcon(activity.getResources().getDrawable(R.drawable.no_notifications));
				}
			}else{
				notificationItem.setIcon(activity.getResources().getDrawable(R.drawable.no_notifications));
			}
		}
	}
}
