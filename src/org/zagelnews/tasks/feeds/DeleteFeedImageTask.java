package org.zagelnews.tasks.feeds;

import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.FeedCommentsActivity;
import org.zagelnews.activities.feeds.FeedDetailsActivity;
import org.zagelnews.activities.feeds.FilteredFeedsListActivity;
import org.zagelnews.constants.Constants;
import org.zagelnews.delegates.FeedsFunctions;

import android.os.AsyncTask;
import android.widget.Toast;

/**
 * delete the given image of the given feed
 * @author oomran
 *
 */
public class DeleteFeedImageTask extends AsyncTask<String,String,JSONObject> {
	
	private FeedDetailsActivity activity;
	private Integer feedSeq;
	private String imageKey;
	
	public DeleteFeedImageTask(FeedDetailsActivity activity, Integer feedSeq, String imageKey){
		this.activity = activity;
		this.feedSeq = feedSeq;
		this.imageKey = imageKey;
	}

	@Override
	protected JSONObject doInBackground(String... args) {	
		JSONObject json = null;
		FeedsFunctions feedsFunction = new FeedsFunctions(activity);
		json = feedsFunction.deleteFeedImage(feedSeq, imageKey);
		return json;
	}

	protected void onPostExecute(JSONObject zagelnewsDtoObj) {
		try {
			if(zagelnewsDtoObj==null){
				Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
				return;
			}

			if (!zagelnewsDtoObj.has(FeedCommentsActivity.KEY_ERROR_CODE)||
					zagelnewsDtoObj.getString(FeedCommentsActivity.KEY_ERROR_CODE)==null||
					zagelnewsDtoObj.getString(FeedCommentsActivity.KEY_ERROR_CODE).trim().length()==0) {

				//check for the successful deletion
				if(zagelnewsDtoObj.has("status")){
					Integer status = zagelnewsDtoObj.getInt("status");
					if(status!=null&&status.equals(1)){
						Toast.makeText(activity, activity.getResources().getString(R.string.sucessDel), Toast.LENGTH_LONG).show();
						activity.finish();
					}
				}
			} 

			//error occurred in one of the service
			else {
				String errorCode = zagelnewsDtoObj.getString(FilteredFeedsListActivity.KEY_ERROR_CODE);
				String errorMessage = zagelnewsDtoObj.getString(FilteredFeedsListActivity.KEY_ERROR_MESSAGE);
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