package org.zagelnews.tasks.feeds;

import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.BaseActivity;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.FeedDetailsActivity;
import org.zagelnews.constants.Constants;
import org.zagelnews.delegates.UserFunctions;
import org.zagelnews.dtos.users.UserDto;
import org.zagelnews.tasks.TaskIds;

import android.os.AsyncTask;
import android.widget.Toast;

/**
 * load the user info
 * @author oomran
 *
 */
public class GetUserInfoTask extends AsyncTask<String,String,JSONObject> {
	
	private BaseActivity activity;
	private Integer profileType;
	private Integer inquiredProfileId;
	
	
	public GetUserInfoTask(BaseActivity activity, Integer profileType, Integer inquiredProfileId){
		this.activity = activity;
		this.profileType = profileType;
		this.inquiredProfileId = inquiredProfileId;
	}

	@Override
	protected JSONObject doInBackground(String... args) {	
		UserFunctions userFunctions = new UserFunctions(activity);
		JSONObject json = userFunctions.getProfileInfo(profileType, inquiredProfileId);
		return json;
	}

	protected void onPostExecute(JSONObject jsonObject) {
		try {
			if(jsonObject==null){
				Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
				return;
			}


			if (!jsonObject.has(FeedDetailsActivity.KEY_ERROR_CODE)||
					jsonObject.getString(FeedDetailsActivity.KEY_ERROR_CODE)==null||
							jsonObject.getString(FeedDetailsActivity.KEY_ERROR_CODE).trim().length()==0) {

				if (jsonObject!=null) {
					//fill the FilteredFeedsListActivity list from the server
					UserDto userDto = new UserDto();
					
					userDto.setProfileType(jsonObject.getInt("profileType"));
					userDto.setUserId(jsonObject.getInt("userId"));


					//get profile image
					if(jsonObject.has("profileImageRaw")){
						JSONObject profileImageRawObj = jsonObject.getJSONObject("profileImageRaw");

						//check if the user has profile image
						if (profileImageRawObj!=null) {

							if(profileImageRawObj.has("fullImageUrl")){
								userDto.setProfileFullImageUrl(profileImageRawObj.getString("fullImageUrl"));
							}

							if(profileImageRawObj.has("sampleImageUrl")){
								userDto.setProfileSampleImageUrl(profileImageRawObj.getString("sampleImageUrl"));
							}

						}
					}

					//get cover image
					if(jsonObject.has("coverImageRaw")){
						JSONObject coverImageRawObj = jsonObject.getJSONObject("coverImageRaw");

						//check if the user has profile image
						if (coverImageRawObj!=null) {

							if(coverImageRawObj.has("fullImageUrl")){
								userDto.setCoverFullImageUrl(coverImageRawObj.getString("fullImageUrl"));
							}

							if(coverImageRawObj.has("sampleImageUrl")){
								userDto.setCoverSampleImageUrl(coverImageRawObj.getString("sampleImageUrl"));
							}

						}
					}

					activity.getTaskResult(userDto, TaskIds.GetUserInfoTask);

				}

			} 

			//error occurred in one of the service
			else {
				String errorCode = jsonObject.getString(FeedDetailsActivity.KEY_ERROR_CODE);
				String errorMessage = jsonObject.getString(FeedDetailsActivity.KEY_ERROR_MESSAGE);
				Toast.makeText(activity, errorCode+":"+errorMessage, Toast.LENGTH_LONG).show();
				if(Constants.TOKEN_NOT_MATCH.equals(errorCode)){
					activity.redirectToLogin();
				}
			}


		}catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}

	}
}