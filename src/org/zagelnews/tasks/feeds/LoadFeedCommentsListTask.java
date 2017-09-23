package org.zagelnews.tasks.feeds;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.feeds.FeedCommentsActivity;
import org.zagelnews.delegates.FeedsFunctions;
import org.zagelnews.dtos.feeds.FeedCommentDto;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.zagelnews.activities.R;

/**
 * load the comments for the given feed
 **/

public class LoadFeedCommentsListTask extends AsyncTask<String,String,JSONArray>
{

	private FeedCommentsActivity activity;
	private Integer commentSeqFrom;

	//for internal use only 
	private LinearLayout linlaFooterProgress = null;

	public LoadFeedCommentsListTask(FeedCommentsActivity activity, Integer commentSeqFrom) {
		this.activity = activity;
		this.commentSeqFrom = commentSeqFrom;
		linlaFooterProgress = (LinearLayout) activity.findViewById(R.id.linlaFooterProgress);
	}

	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		linlaFooterProgress.setVisibility(View.VISIBLE);
	}

	/**
	 * call the server api to retrive the feeds
	 **/
	@Override
	protected JSONArray doInBackground(String... args){
		JSONArray json = null;
		FeedsFunctions feedsFunction = new FeedsFunctions(activity);
		json = feedsFunction.loadFeedComments(activity.getFeedSeq(), commentSeqFrom); 
		return json;
	}

	@Override
	protected void onPostExecute(JSONArray zagelnewsDtoObjArr){

		try {
			if (zagelnewsDtoObjArr!=null) {
				ArrayList<FeedCommentDto> db = new ArrayList<FeedCommentDto>();

				//fill the FeedCommentsActivity list from the server
				for (int i=0;i<zagelnewsDtoObjArr.length();i++){

					JSONObject jsonObject = (JSONObject)zagelnewsDtoObjArr.get(i);
					FeedCommentDto feedCommentDto = new FeedCommentDto();
					if(jsonObject.has("commentText")){
						feedCommentDto.setCommentText(jsonObject.getString("commentText"));
					}

					if(jsonObject.has("commentDate")){
						feedCommentDto.setCommentDate(jsonObject.getString("commentDate"));
					}

					if(jsonObject.has("commentSeq")){
						feedCommentDto.setCommentSeq(jsonObject.getInt("commentSeq"));
					}

					if(jsonObject.has("feedSeq")){
						feedCommentDto.setFeedSeq(jsonObject.getInt("feedSeq"));
					}

					if(jsonObject.has("userFullNameWhoComment")){
						feedCommentDto.setUserFullNameWhoComment(jsonObject.getString("userFullNameWhoComment"));
					}

					if(jsonObject.has("userIdWhoComment")){
						feedCommentDto.setUserIdWhoComment(jsonObject.getInt("userIdWhoComment"));
					}


					//get user image
					if(jsonObject.has("sampleImageUrlForUserWhoComment")&&jsonObject.has("fullImageUrlForUserWhoComment")){
						feedCommentDto.setSampleImageUrlForUserWhoComment(jsonObject.getString("sampleImageUrlForUserWhoComment"));
						feedCommentDto.setFullImageUrlForUserWhoComment(jsonObject.getString("fullImageUrlForUserWhoComment"));
					}

					db.add(feedCommentDto);
				} 

				if(commentSeqFrom==null||commentSeqFrom<=0){
					activity.getAdapter().clear();
				}
				if(db.size()>0){
					activity.getAdapter().addAll(db);
					activity.getAdapter().notifyDataSetChanged();
					activity.getHolder().fetchMoreComments.setVisibility(View.VISIBLE);
				}else{
					activity.getHolder().fetchMoreComments.setVisibility(View.GONE);
				}
				if((commentSeqFrom==null||commentSeqFrom<=0)&&!activity.getHolder().flag_initialy_loaded){
					activity.getHolder().v.setSelection(0);
					activity.getHolder().flag_initialy_loaded = true;
				}

			}
			linlaFooterProgress.setVisibility(View.GONE);
		}catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
			linlaFooterProgress.setVisibility(View.GONE);
		}

	}
}

