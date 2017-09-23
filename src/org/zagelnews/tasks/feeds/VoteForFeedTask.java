package org.zagelnews.tasks.feeds;

import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.FeedCommentsActivity;
import org.zagelnews.activities.feeds.FilteredFeedsListActivity;
import org.zagelnews.constants.Constants;
import org.zagelnews.delegates.FeedsFunctions;
import org.zagelnews.dtos.feeds.FeedDto;
import org.zagelnews.dtos.feeds.VoteInputDto;
import org.zagelnews.utils.AndroidUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * vote for the given feed
 * @author oomran
 *
 */
public class VoteForFeedTask extends AsyncTask<String,String,JSONObject> {
	
	private Activity activity;
	private VoteInputDto voteInputDto;
	
	//for internal use
	private Integer feedSeq;
	private Integer voteType;
	private Integer voteReason;
	private FeedDto uf;

	public VoteForFeedTask(Activity activity, VoteInputDto voteInputDto){
		this.activity = activity;
		this.voteInputDto = voteInputDto;
		this.feedSeq = voteInputDto.getFeedSeq();
		this.voteType = voteInputDto.getVoteType();
		this.voteReason = voteInputDto.getVoteReason();
		this.uf = voteInputDto.getUf();
	}

	@Override
	protected JSONObject doInBackground(String... args) {	
		JSONObject json = null;
		FeedsFunctions feedsFunction = new FeedsFunctions(activity);
		json = feedsFunction.voteForFeed(feedSeq, voteType, voteReason);
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
				
				TextView accuracyPercentage = voteInputDto.getAccuracyPercentage();
				Button likeMe = voteInputDto.getLikeMe();
				Button unLikeMe = voteInputDto.getUnLikeMe();
				TextView voteCountTv = voteInputDto.getVoteCountTv();
				
				Integer feedAccuracy = null;
				if(zagelnewsDtoObj.has("feedAccuracy")){
					feedAccuracy = zagelnewsDtoObj.getInt("feedAccuracy");
				}

				if(zagelnewsDtoObj.has("votesCount")){
					Integer voteCount = zagelnewsDtoObj.getInt("votesCount");
					if(voteCount!=null&&voteCount>0){
						voteCountTv.setText(activity.getResources().getString(R.string.noVoters)+voteCount);
						if(uf!=null){
							uf.setVoteCount(voteCount);
						}
					}else{
						voteCountTv.setText("");
					}
				}

				Integer requesterPrevLikeAction = null;
				if(zagelnewsDtoObj.has("currentUserVote")){
					String requesterVoteAction = zagelnewsDtoObj.getString("currentUserVote");
					if(requesterVoteAction!=null&& !"null".equals(requesterVoteAction)){
						requesterPrevLikeAction = zagelnewsDtoObj.getInt("currentUserVote");
						if(uf!=null){
							uf.setRequesterVoteAction(requesterPrevLikeAction);
						}
					}else{
						uf.setRequesterVoteAction(null);
					}
					
				}

				if(requesterPrevLikeAction==null){
					likeMe.setTextColor(Color.BLACK);
					unLikeMe.setTextColor(Color.BLACK);
				}else{
					if(requesterPrevLikeAction.equals(1)){
						likeMe.setTextColor(Color.BLUE);
						unLikeMe.setTextColor(Color.BLACK);
					}else if(requesterPrevLikeAction.equals(-1)){
						unLikeMe.setTextColor(Color.BLUE);
						likeMe.setTextColor(Color.BLACK);
					}else{
						likeMe.setTextColor(Color.BLACK);
						unLikeMe.setTextColor(Color.BLACK);
					}
				}

				if(feedAccuracy!=null){
					accuracyPercentage.setText(feedAccuracy.longValue()+" %");
					if(uf!=null){
						uf.setFeedAccuracy(feedAccuracy);
					}
				}

			} 

			//error occurred in one of the service
			else {
				String errorCode = zagelnewsDtoObj.getString(FilteredFeedsListActivity.KEY_ERROR_CODE);
				String errorMessage = zagelnewsDtoObj.getString(FilteredFeedsListActivity.KEY_ERROR_MESSAGE);
				Toast.makeText(activity, errorCode+":"+errorMessage, Toast.LENGTH_LONG).show();
				if(Constants.TOKEN_NOT_MATCH.equals(errorCode)){
					AndroidUtils.redirectToLogin(activity);
				}
			}


		}catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}
	}

	public VoteInputDto getVoteInputDto() {
		return voteInputDto;
	}
	
	public void setVoteInputDto(VoteInputDto voteInputDto) {
		this.voteInputDto = voteInputDto;
	}
}
