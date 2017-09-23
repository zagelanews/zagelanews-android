package org.zagelnews.tasks.feeds;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.MapActivity;
import org.zagelnews.delegates.FeedsFunctions;
import org.zagelnews.dtos.feeds.FeedsSummaryDto;
import org.zagelnews.dtos.geo.PointDto;
import org.zagelnews.tasks.TaskIds;

import android.os.AsyncTask;
import android.widget.Toast;

/**
 * load the feeds for the given profile id
 **/

public class LoadFeedsSummaryTask extends AsyncTask<String,String,JSONArray>
{

	private MapActivity activity;
	private Double currentLatitude;
	private Double currentLongitude;
	private Integer days;
	

	public LoadFeedsSummaryTask(
			MapActivity activity, 
			Double currentLatitude, 
			Double currentLongitude, 
			Integer days) {
		this.activity = activity;
		this.currentLatitude = currentLatitude;
		this.currentLongitude = currentLongitude;
		this.days = days;
	}

	@Override
	protected void onPreExecute(){
		super.onPreExecute();
	}

	/**
	 * call the server api to retrive the feeds
	 **/
	@Override
	protected JSONArray doInBackground(String... args){
		JSONArray json = null;
		FeedsFunctions feedsFunction = new FeedsFunctions(activity);
		json = feedsFunction.loadFeedsSummary(currentLatitude, currentLongitude, days);
		return json;
	}

	@Override
	protected void onPostExecute(JSONArray zagelnewsDtoObjArr){

		try {
			if (zagelnewsDtoObjArr!=null) {
				ArrayList<FeedsSummaryDto> db = new ArrayList<FeedsSummaryDto>();

				//fill the FilteredFeedsListActivity list from the server
				for (int i=0;i<zagelnewsDtoObjArr.length();i++){ 
					JSONObject jsonObject = (JSONObject)zagelnewsDtoObjArr.get(i);
					FeedsSummaryDto feedsSummaryDto = new FeedsSummaryDto();
					feedsSummaryDto.setFeedSeq(jsonObject.getInt("feedSeq"));
					
					if(jsonObject.has("feedType")&&!jsonObject.isNull("feedType")){
						feedsSummaryDto.setFeedType(jsonObject.getInt("feedType"));
					}

					if(jsonObject.has("feedText")){
						feedsSummaryDto.setFeedText(jsonObject.getString("feedText"));
					}
					
					
					if(jsonObject.has("feedAuthorType")){
						feedsSummaryDto.setFeedAuthorType(jsonObject.getInt("feedAuthorType"));
					}
					
					//get feed locations
					if(jsonObject.has("feedLocation")&&
							!jsonObject.isNull("feedLocation")){
						JSONObject feedLocation = jsonObject.getJSONObject("feedLocation");

						if (feedLocation!=null) {
							//fill the feed locations list from the server
							PointDto pointDto = new PointDto();
							if(feedLocation.has("latitude")){
								pointDto.setLatitude(feedLocation.getDouble("latitude"));
							}

							if(feedLocation.has("longitude")){
								pointDto.setLongitude(feedLocation.getDouble("longitude"));
							}

							feedsSummaryDto.setFeedLocation(pointDto);
						}else{
							continue;
						}
					}else{
						continue;
					}



					db.add(feedsSummaryDto);
				} 
				
				activity.getTaskResult(db, TaskIds.LoadFeedsSummaryTask);

			}
		}catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}

	}
}

