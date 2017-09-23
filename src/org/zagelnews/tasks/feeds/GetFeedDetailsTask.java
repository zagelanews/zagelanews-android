package org.zagelnews.tasks.feeds;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.FeedDetailsActivity;
import org.zagelnews.constants.Constants;
import org.zagelnews.delegates.FeedsFunctions;
import org.zagelnews.dtos.feeds.FeedDto;
import org.zagelnews.dtos.feeds.ImageDto;
import org.zagelnews.dtos.geo.PointDto;

import android.os.AsyncTask;
import android.widget.Toast;

/**
 * load the feed details from the server
 * @author oomran
 *
 */
public class GetFeedDetailsTask extends AsyncTask<String,String,JSONObject> {
	
	private FeedDetailsActivity activity;
	private Integer feedSeq;
	private Integer feedAuthorType;
	
	
	public GetFeedDetailsTask(FeedDetailsActivity activity, Integer feedSeq, Integer feedAuthorType){
		this.activity = activity;
		this.feedSeq = feedSeq;
		this.feedAuthorType = feedAuthorType;
	}

	@Override
	protected JSONObject doInBackground(String... args) {	
		FeedsFunctions feedsFunction = new FeedsFunctions(activity);
		JSONObject json = feedsFunction.loadFeedInfo(feedSeq, feedAuthorType);
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
					FeedDto feedDto = activity.getFeedDto();
					
					feedDto.setFeedDate(jsonObject.getString("feedDate"));
					feedDto.setAuthorFullName(jsonObject.getString("authorFullName"));
					feedDto.setFeedText(jsonObject.getString("feedText"));
					feedDto.setAuthorId(jsonObject.getInt("authorId"));
					feedDto.setFeedSeq(jsonObject.getInt("feedSeq"));
					feedDto.setMyOwnFeed(jsonObject.getBoolean("isMyOwnFeed"));

					if(jsonObject.has("feedAuthorType")){
						feedDto.setFeedAuthorType(jsonObject.getInt("feedAuthorType"));
					}

					//get feed images
					if(jsonObject.has("feedImages")){
						JSONArray feedImagesArr = jsonObject.getJSONArray("feedImages");

						//check if the feed has some images
						if (feedImagesArr!=null) {
							ArrayList<ImageDto> imagesList = new ArrayList<ImageDto>();

							//fill the feed Images list from the server
							for (int i=0;i<feedImagesArr.length();i++){ 
								JSONObject feedImageJson = (JSONObject)feedImagesArr.get(i);
								ImageDto imageDto = new ImageDto();
								if(feedImageJson.has("imageKey")){
									imageDto.setImageKey(feedImageJson.getInt("imageKey"));
								}

								if(feedImageJson.has("fullImageUrl")){
									imageDto.setFullImageUrl(feedImageJson.getString("fullImageUrl"));
								}

								if(feedImageJson.has("sampleImageUrl")){
									imageDto.setSampleImageUrl(feedImageJson.getString("sampleImageUrl"));
								}

								imagesList.add(imageDto);
							}

							feedDto.setFeedImages(imagesList);
						}
					}



					//get feed locations
					if(jsonObject.has("feedLocations")){
						JSONArray feedLocationsArr = jsonObject.getJSONArray("feedLocations");

						if (feedLocationsArr!=null) {
							ArrayList<PointDto> locationsList = new ArrayList<PointDto>();

							//fill the feed locations list from the server
							for (int i=0;i<feedLocationsArr.length();i++){ 
								JSONObject feedLocationsJson = (JSONObject)feedLocationsArr.get(i);
								PointDto pointDto = new PointDto();
								if(feedLocationsJson.has("latitude")){
									pointDto.setLatitude(feedLocationsJson.getDouble("latitude"));
								}

								if(feedLocationsJson.has("longitude")){
									pointDto.setLongitude(feedLocationsJson.getDouble("longitude"));
								}
								locationsList.add(pointDto);
							}

							feedDto.setFeedLocations(locationsList);
						}
					}


					//author image
					if(jsonObject.has("authorFullImageUrl")){
						feedDto.setAuthorFullImageUrl(jsonObject.getString("authorFullImageUrl"));
					}
					if(jsonObject.has("authorSampleImageUrl")){
						feedDto.setAuthorSampleImageUrl(jsonObject.getString("authorSampleImageUrl"));
					}


					//feed statistics
					if(jsonObject.has("feedAccuracy")){
						feedDto.setFeedAccuracy(jsonObject.getInt("feedAccuracy"));
					}
					if(jsonObject.has("commentCount")){
						feedDto.setCommentCount(jsonObject.getInt("commentCount"));
					}
					if(jsonObject.has("voteCount")){
						feedDto.setVoteCount(jsonObject.getInt("voteCount"));
					}									

					if(jsonObject.has("requesterVoteAction")){
						String requesterVoteAction = jsonObject.getString("requesterVoteAction");
						if(requesterVoteAction!=null&& !"null".equals(requesterVoteAction)){
							feedDto.setRequesterVoteAction(jsonObject.getInt("requesterVoteAction"));	
						}
						
					}
				}

				activity.fillView();
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