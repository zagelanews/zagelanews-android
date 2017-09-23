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
import android.view.View;
import android.widget.Toast;

/**
 * edit the given feed, with given updated information
 * @author oomran
 *
 */
public class EditFeedTask extends AsyncTask<String,String,JSONObject> {
	
	private FeedDetailsActivity activity;
	private Integer feedSeq;
	private String feedText;
	private Double feedLatitude;
	private Double feedLongitude;

	public EditFeedTask(FeedDetailsActivity activity, Integer feedSeq, String feedText, Double feedLatitude, Double feedLongitude){
		this.activity = activity;
		this.feedSeq = feedSeq;
		this.feedText = feedText;
		this.feedLatitude = feedLatitude;
		this.feedLongitude = feedLongitude;
	}


	@Override
	protected JSONObject doInBackground(String... args) {	
		JSONObject json = null;
		FeedsFunctions feedsFunction = new FeedsFunctions(activity);
		json = feedsFunction.editFeed(feedSeq, feedText, feedLatitude, feedLongitude);
		return json;
	}

	protected void onPostExecute(JSONObject zagelnewsDtoObj) {
		try {
/*			if(zagelnewsDtoObj==null){
				Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
				return;
			}*/

			if (zagelnewsDtoObj==null||!zagelnewsDtoObj.has(FeedCommentsActivity.KEY_ERROR_CODE)||
					zagelnewsDtoObj.getString(FeedCommentsActivity.KEY_ERROR_CODE)==null||
					zagelnewsDtoObj.getString(FeedCommentsActivity.KEY_ERROR_CODE).trim().length()==0) {

				//check the successful of editing the feed
				//if(zagelnewsDtoObj.has("status")){
					//Integer status = zagelnewsDtoObj.getInt("status");
					//if(status!=null&&status.equals(1)){
						//hide the edit text box
						activity.getViewHolder().feedTextE.setVisibility(View.GONE);
						//show the view text box
						activity.getViewHolder().feedTextV.setText(activity.getViewHolder().feedTextE.getText());
						activity.getViewHolder().feedTextV.setVisibility(View.VISIBLE);
						activity.setEditMode(false);
					//}
				//}
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
