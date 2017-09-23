package org.zagelnews.tasks.feeds;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.ProfileFeedsListActivity;
import org.zagelnews.delegates.FeedsFunctions;
import org.zagelnews.dtos.feeds.FeedDto;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * load the feeds for the given profile id
 **/

public class LoadProfileFeedsListTask extends AsyncTask<String,String,JSONArray>
{

	private ProfileFeedsListActivity activity;
	private Integer feedSeqFrom;

	//for internal use only 
	private LinearLayout linlaFooterProgress = null;

	public LoadProfileFeedsListTask(ProfileFeedsListActivity activity, Integer feedSeqFrom) {
		this.activity = activity;
		this.feedSeqFrom = feedSeqFrom;
		linlaFooterProgress = (LinearLayout) activity.findViewById(R.id.linlaFooterProgress);
	}

	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		linlaFooterProgress.setVisibility(View.VISIBLE);
	}

	/**
	 * call the server api to retrieve the feeds
	 **/
	@Override
	protected JSONArray doInBackground(String... args){
		JSONArray json = null;
		FeedsFunctions feedsFunction = new FeedsFunctions(activity);
		json = feedsFunction.loadProfileFeeds(activity.getProfileOwnerId(), activity.getFeedAuthorType(), feedSeqFrom);
		return json;
	}

	@Override
	protected void onPostExecute(JSONArray zagelnewsDtoObjArr){

		try {
			if (zagelnewsDtoObjArr!=null) {
				ArrayList<FeedDto> db = new ArrayList<FeedDto>();

				//fill the FilteredFeedsListActivity list from the server
				for (int i=0;i<zagelnewsDtoObjArr.length();i++){ 
					JSONObject jsonObject = (JSONObject)zagelnewsDtoObjArr.get(i);
					FeedDto userFeeds = new FeedDto();
					userFeeds.setFeedDate(jsonObject.getString("feedDate"));
					userFeeds.setAuthorFullName(jsonObject.getString("authorFullName"));
					userFeeds.setFeedText(jsonObject.getString("feedText"));
					userFeeds.setAuthorId(jsonObject.getInt("authorId"));
					userFeeds.setFeedSeq(jsonObject.getInt("feedSeq"));

					if(jsonObject.has("feedAuthorType")){
						userFeeds.setFeedAuthorType(jsonObject.getInt("feedAuthorType"));
					}

					//feed first image

					if(jsonObject.has("firstFullImageUrl")){
						userFeeds.setFirstFullImageUrl(jsonObject.getString("firstFullImageUrl"));
					}

					if(jsonObject.has("firstSampleImageUrl")){
						userFeeds.setFirstSampleImageUrl(jsonObject.getString("firstSampleImageUrl"));
					}

					//author image
					if(jsonObject.has("authorFullImageUrl")){
						userFeeds.setAuthorFullImageUrl(jsonObject.getString("authorFullImageUrl"));
					}

					if(jsonObject.has("authorSampleImageUrl")){
						userFeeds.setAuthorSampleImageUrl(jsonObject.getString("authorSampleImageUrl"));
					}


					//fill the counts and statistics
					if(jsonObject.has("feedAccuracy")){
						userFeeds.setFeedAccuracy(jsonObject.getInt("feedAccuracy"));
					}
					if(jsonObject.has("commentCount")){
						userFeeds.setCommentCount(jsonObject.getInt("commentCount"));
					}
					if(jsonObject.has("voteCount")){
						userFeeds.setVoteCount(jsonObject.getInt("voteCount"));
					}
					
					if(jsonObject.has("isMyOwnFeed")){
						userFeeds.setMyOwnFeed(jsonObject.getBoolean("isMyOwnFeed"));
					}	

					if(jsonObject.has("requesterVoteAction")){
						String requesterVoteAction = jsonObject.getString("requesterVoteAction");
						if(requesterVoteAction!=null&& !"null".equals(requesterVoteAction)){
							userFeeds.setRequesterVoteAction(jsonObject.getInt("requesterVoteAction"));	
						}

					}

					db.add(userFeeds);
				} 

				if(feedSeqFrom==null||feedSeqFrom<=0){
					activity.getAdapter().clear();
				}
				if(db.size()>0){
					activity.getAdapter().addAll(db);
					activity.getAdapter().notifyDataSetChanged();
					activity.getHolder().fetchMoreFeeds.setVisibility(View.VISIBLE);
				}else{
					activity.getHolder().fetchMoreFeeds.setVisibility(View.GONE);
				}
				if((feedSeqFrom==null||feedSeqFrom<=0)&&!activity.getHolder().flag_initialy_loaded){
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

