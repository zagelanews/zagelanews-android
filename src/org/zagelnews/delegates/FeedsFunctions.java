package org.zagelnews.delegates;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.zagelnews.constants.ImagesConstants;
import org.zagelnews.constants.ServerConstants;
import org.zagelnews.dtos.NamedBinaryObject;
import org.zagelnews.dtos.feeds.SampleFullImageDto;
import org.zagelnews.utils.EncodingUtils;
import org.zagelnews.utils.ServiceHandler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;


/**
 * Feeds related APIs
 * @author oomran
 *
 */
public class FeedsFunctions {
	
	private Activity activity;

    //URL of the API
    private static String LoadFeedsURL = ServerConstants.SERVER_URL+"/LoadFeeds";
    private static String LoadFeedsSummaryURL = ServerConstants.SERVER_URL+"/LoadFeedsSummary";
    private static String LoadFeedInfoServletURL = ServerConstants.SERVER_URL+"/LoadFeedInfo";
    private static String LoadProfileFeedsURL = ServerConstants.SERVER_URL+"/LoadProfileFeeds";
    private static String AddUserFeedURL = ServerConstants.SERVER_URL+"/AddFeed";
    private static String LikeUserFeedURL = ServerConstants.SERVER_URL+"/VoteForFeed";
    private static String LoadFeedCommentURL = ServerConstants.SERVER_URL+"/LoadFeedComments";
    private static String AddUserFeedCommentURL = ServerConstants.SERVER_URL+"/AddFeedComment";
    private static String DelUserFeedURL = ServerConstants.SERVER_URL+"/DeleteFeed";
    private static String DelUserFeedImageURL = ServerConstants.SERVER_URL+"/DeleteFeedImage";
    private static String EditFeedURL = ServerConstants.SERVER_URL+"/EditFeed";
    private static String GetUserFeedsNotificationsURL = ServerConstants.SERVER_URL+"/GetFeedsNotification";
    private static String ResetUserFeedsNotificationsURL = ServerConstants.SERVER_URL+"/ResetUserFeedsNotifications";
    private static String LoadFeedTypesURL = ServerConstants.SERVER_URL+"/ListAllTypes";
    
    
    
    


    // constructor
    public FeedsFunctions(Activity activity){
    	this.activity = activity;
    }


    /**
     * Load feeds list based on the given criteria
     * @param interestIds
     * @param feedSeqFrom
     * @param circleLatitude
     * @param circleLongitude
     * @param circleRadius
     * @return
     */
    public JSONArray loadFeeds(String interestIds, Integer feedSeqFrom, Double circleLatitude, Double circleLongitude, Double circleRadius, Double currentLatitude, Double currentLongitude){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("feedSeqFrom", feedSeqFrom==null?"":feedSeqFrom+""));
        
        if(interestIds!=null&&interestIds.trim().length()>0){
        	params.add(new BasicNameValuePair("interestIds", ""+interestIds));
        }
        
        if(circleLatitude!=null&&!circleLatitude.equals(-1.0)){
        	params.add(new BasicNameValuePair("circleLatitude", ""+circleLatitude));
        }
        if(circleLongitude!=null&&!circleLongitude.equals(-1.0)){
        	params.add(new BasicNameValuePair("circleLongitude", ""+circleLongitude));
        }
        
        if(circleRadius!=null&&!circleRadius.equals(-1.0)){
        	params.add(new BasicNameValuePair("circleRadius", ""+circleRadius));
        }
        
        if(currentLatitude!=null&&!currentLatitude.equals(-1.0)){
        	params.add(new BasicNameValuePair("CurrentLatitude", ""+currentLatitude));
        }
        if(currentLongitude!=null&&!currentLongitude.equals(-1.0)){
        	params.add(new BasicNameValuePair("CurrentLongitude", ""+currentLongitude));
        }
        
        
        JSONArray json = ServiceHandler.makeServiceCallArr(activity, LoadFeedsURL, params);
        return json;
    }
    
    
    /**
     * Load feeds summary based on the type criteria
     * @param currentLatitude
     * @param currentLongitude
     * @param feedTypes
     * @param days
     * @return
     */
    public JSONArray loadFeedsSummary(
    		Double currentLatitude, 
    		Double currentLongitude, 
    		Integer days){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if(currentLatitude!=null&&!currentLatitude.equals(-1.0)){
        	params.add(new BasicNameValuePair("CurrentLatitude", ""+currentLatitude));
        }
        if(currentLongitude!=null&&!currentLongitude.equals(-1.0)){
        	params.add(new BasicNameValuePair("CurrentLongitude", ""+currentLongitude));
        }
        
        params.add(new BasicNameValuePair("days", days==null?"":days+""));

        
        
        JSONArray json = ServiceHandler.makeServiceCallArr(activity, LoadFeedsSummaryURL, params);
        return json;
    }

    /**
     * Load the feed details for the given feed
     * @param feedSeq
     * @param feedAuthorType
     * @return
     */
    public JSONObject loadFeedInfo(Integer feedSeq, Integer feedAuthorType){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("feedSeq", feedSeq==null?"":feedSeq+""));
        params.add(new BasicNameValuePair("feedAuthorType", feedAuthorType==null?"":feedAuthorType+""));
        JSONObject json = ServiceHandler.makeServiceCall(activity, LoadFeedInfoServletURL, params);
        return json;
    }
    

    /**
     * Add new feed based on the given parameters
     * @param feedType
     * @param refGroupId
     * @param feedText
     * @param latitude
     * @param longitude
     * @param feedImgsList
     * @return
     */
    public JSONObject addNewFeed(String feedText, Double latitude, Double longitude, List<SampleFullImageDto> feedImgsList){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("points", latitude+"_"+longitude));
        params.add(new BasicNameValuePair("feedText", feedText));
        
        
        //add feed images
        NamedBinaryObject [] feedImagesArr = new NamedBinaryObject[feedImgsList.size()*2];
        if(feedImgsList!=null&&feedImgsList.size()>0){
        	int j = 0;//for the fill list of images
        	for(int i=0; i<feedImgsList.size(); i++){
        		SampleFullImageDto imageDto = feedImgsList.get(i);
        		Bitmap bitmap = ((BitmapDrawable)imageDto.getSampleImg().getDrawable()).getBitmap();
        		feedImagesArr[j] = new NamedBinaryObject(EncodingUtils.BitMapToByteArray(bitmap), i+ImagesConstants.SAMPLE_IMAGE_TAGE);
        		j++;
        		feedImagesArr[j] = new NamedBinaryObject(EncodingUtils.BitMapToByteArray(imageDto.getFullSizeImage()), i+ImagesConstants.FULL_SIZE_IMAGE_TAGE);
        		j++;
        	}
        }
        
        //call the add feed api
        JSONObject json = ServiceHandler.makeServiceCall(activity, AddUserFeedURL,
        		params, feedImagesArr);
        return json;
    }
    
    
    /**
     * Load the comments of the given feed
     * @param feedSeq
     * @param commentSeqFrom
     * @return
     */
    public JSONArray loadFeedComments(Integer feedSeq, Integer commentSeqFrom){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("feedSeq", feedSeq==null?"":feedSeq+""));
        params.add(new BasicNameValuePair("commentSeqFrom", commentSeqFrom==null?"":commentSeqFrom+""));
        JSONArray json = ServiceHandler.makeServiceCallArr(activity, LoadFeedCommentURL, params);
        return json;
    }
    
    
    
    /**
     * add new comment to the given feed
     * @param feedSeq
     * @param comment
     * @return
     */
    public JSONObject addNewComment(Integer feedSeq, String comment){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if(feedSeq!=null&&!feedSeq.equals(-1)){
        	params.add(new BasicNameValuePair("feedSeq", feedSeq==null?"":feedSeq+""));
        }
        params.add(new BasicNameValuePair("comment", comment));
        JSONObject json = ServiceHandler.makeServiceCall(activity, AddUserFeedCommentURL, params);
        return json;
    }
    
    
    /**
     * Load the feeds of the given profile
     * @param authorId
     * @param authorType
     * @param feedSeqFrom
     * @return
     */
    public JSONArray loadProfileFeeds(Integer authorId, Integer authorType, Integer feedSeqFrom){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("authorId", authorId==null?"":authorId+""));
        params.add(new BasicNameValuePair("authorType", authorType==null?"":authorType+""));
        params.add(new BasicNameValuePair("feedSeqFrom", feedSeqFrom==null?"":feedSeqFrom+""));
        JSONArray json = ServiceHandler.makeServiceCallArr(activity, LoadProfileFeedsURL, params);
        return json;
    }
    
    
    /**
     * delete the given feed
     * @param feedSeq
     * @return
     */
    public JSONObject deleteFeed(Integer feedSeq){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("feedSeq", feedSeq==null?"":feedSeq+""));
        JSONObject json = ServiceHandler.makeServiceCall(activity, DelUserFeedURL, params);
        return json;
    }

    
    /**
     * delete the given image from the given feed
     * @param feedSeq
     * @param imageUrl
     * @return
     */
    public JSONObject deleteFeedImage(Integer feedSeq, String imageKey){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("feedSeq", feedSeq==null?"":feedSeq+""));
        params.add(new BasicNameValuePair("imageKey", imageKey));
        JSONObject json = ServiceHandler.makeServiceCall(activity, DelUserFeedImageURL, params);
        return json;
    }
    
    
    /**
     * edit the given feed, with given updated information
     * @param feedSeq
     * @param feedText
     * @param latitude
     * @param longitude
     * @return
     */
    public JSONObject editFeed(Integer feedSeq, String feedText, Double latitude, Double longitude){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("feedSeq", feedSeq==null?"":feedSeq+""));
        params.add(new BasicNameValuePair("feedText", feedText));
        params.add(new BasicNameValuePair("points", latitude+"_"+longitude));
        JSONObject json = ServiceHandler.makeServiceCall(activity, EditFeedURL, params);
        return json;
    }

	
	public JSONArray getUserFeedsNotifications() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONArray json = ServiceHandler.makeServiceCallArr(activity, GetUserFeedsNotificationsURL, params);
        return json;
    }
	
	public JSONObject resetUserFeedsNotifications() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = ServiceHandler.makeServiceCall(activity, ResetUserFeedsNotificationsURL, params);
        return json;
    }
	
	
	
    /**
     * Vote for the given feed
     * @param feedSeq
     * @param voteType
     * @param voteReason
     * @return
     */
    public JSONObject voteForFeed(Integer feedSeq, Integer voteType, Integer voteReason){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("feedSeq", feedSeq==null?"":feedSeq+""));
        params.add(new BasicNameValuePair("voteType", voteType==null?"":voteType+""));
        params.add(new BasicNameValuePair("voteReason", voteReason==null?"":voteReason+""));
        JSONObject json = ServiceHandler.makeServiceCall(activity, LikeUserFeedURL, params);
        return json;
    }

    /**
     * @return the list of the active feed types
     */
    public JSONArray loadFeedTypes() {
		JSONArray json = ServiceHandler.makeServiceCallArr(activity, LoadFeedTypesURL);
        return json;
    }    
    
}

