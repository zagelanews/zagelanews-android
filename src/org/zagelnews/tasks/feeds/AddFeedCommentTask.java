package org.zagelnews.tasks.feeds;

import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.feeds.FeedCommentsActivity;
import org.zagelnews.constants.Constants;
import org.zagelnews.delegates.FeedsFunctions;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.zagelnews.activities.R;

/**
 * add comment to the given feed
 **/

public class AddFeedCommentTask extends AsyncTask<String,String,JSONObject>
{

	private FeedCommentsActivity activity;

	//for internal use only 
	private LinearLayout linlaFooterProgress = null;

	public AddFeedCommentTask(FeedCommentsActivity activity) {
		this.activity = activity;
		linlaFooterProgress = (LinearLayout) activity.findViewById(R.id.linlaFooterProgress);
	}

	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		linlaFooterProgress.setVisibility(View.VISIBLE);
	}

	/**
	 * call the server api to add the comment to the feeds
	 **/
	@Override
	protected JSONObject doInBackground(String... args){
		JSONObject json = null;
		FeedsFunctions feedsFunction = new FeedsFunctions(activity);
		if(activity.getHolder().newComment!=null){
			String commentText = activity.getHolder().newComment.getText().toString();
			if(commentText!=null&&commentText.trim().length()>0){
				json = feedsFunction.addNewComment(activity.getFeedSeq(), commentText);
			}
		} 
		return json;
	}

	@Override
	protected void onPostExecute(JSONObject zagelnewsDtoObj){
		try {
			if(zagelnewsDtoObj==null){
				activity.getHolder().newComment.setEnabled(true);
				Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
				linlaFooterProgress.setVisibility(View.GONE);
				return;
			}


			if (!zagelnewsDtoObj.has(FeedCommentsActivity.KEY_ERROR_CODE)||
					zagelnewsDtoObj.getString(FeedCommentsActivity.KEY_ERROR_CODE)==null||
					zagelnewsDtoObj.getString(FeedCommentsActivity.KEY_ERROR_CODE).trim().length()==0) {
				new LoadFeedCommentsListTask(activity, null).execute();
				activity.getHolder().newComment.setText("");
				activity.getHolder().newComment.setEnabled(true);
			} 

			//error occured in one of the service
			else {
				linlaFooterProgress.setVisibility(View.GONE);
				activity.getHolder().newComment.setEnabled(true);
				String errorCode = zagelnewsDtoObj.getString(FeedCommentsActivity.KEY_ERROR_CODE);
				String errorMessage = zagelnewsDtoObj.getString(FeedCommentsActivity.KEY_ERROR_MESSAGE);
				Toast.makeText(activity, errorCode+":"+errorMessage, Toast.LENGTH_LONG).show();
				if(Constants.TOKEN_NOT_MATCH.equals(errorCode)){
					activity.redirectToLogin();
				}
			}
		}catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
			linlaFooterProgress.setVisibility(View.GONE);
		}

	}
}

