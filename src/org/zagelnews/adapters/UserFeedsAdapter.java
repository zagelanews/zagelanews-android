package org.zagelnews.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.zagelnews.activities.BaseActivity;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.FeedCommentsActivity;
import org.zagelnews.activities.utils.FullscreenIntent;
import org.zagelnews.constants.FeedsConstants;
import org.zagelnews.constants.ImagesConstants;
import org.zagelnews.constants.ServerConstants;
import org.zagelnews.db.DatabaseHandler;
import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.dtos.feeds.FeedDto;
import org.zagelnews.dtos.feeds.ImageDto;
import org.zagelnews.dtos.feeds.VoteInputDto;
import org.zagelnews.tasks.feeds.VoteForFeedTask;
import org.zagelnews.ui.images.ImageLoader;
import org.zagelnews.utils.ClientConfiguration;
import org.zagelnews.utils.GeneralUtils;
import org.zagelnews.utils.IntentHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserFeedsAdapter extends BaseAdapter{

	private boolean fromCache = true;

	private ImageLoader feedImageLoader;
	private ImageLoader userImageLoader;

	// Android Components
	Button update;

	static class ViewHolder {
		public TextView text;
		public TextView feedDate;
		public TextView voterCount;
		public TextView feedUserFullName;
		public Bitmap bitmap;
		public ImageView image;
		public Button likeMe;
		public TextView accuracyPercentage;
		public Button unLikeMe;
		public Button shareBtn;
		public Button commentBtn;
		public LinearLayout imagesLayout;
		public HorizontalScrollView scrollFooterProgress;
		public View lsitItem;
	}

	public UserFeedsAdapter(Activity context, ArrayList<ZagelnewsDto> names) {
		super(context, R.layout.activity_feeds_raw, names);
		this.context = context;
		this.names = names;
		int feedImageWidth = Double.valueOf(context.getResources().getDisplayMetrics().widthPixels).intValue();

		userImageLoader = new ImageLoader(
				context.getApplicationContext(), 
				ImageLoader.stub_id_defuser);
		
		
		feedImageLoader = new ImageLoader(
				context.getApplicationContext(), 
				feedImageWidth,
				(Double.valueOf(feedImageWidth*0.5).intValue()),
				ImageLoader.stub_id_noimage);
	}

	@Override
	public View getView(final int position, final View convertView, ViewGroup parent) {   
		View rowView = convertView;
		final ViewHolder viewHolder;
		// reuse views
		if (rowView == null) {
			//LayoutInflater inflater = context.getLayoutInflater();
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.activity_user_feeds_raw, null);
			// configure view holder
			viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.label);
			viewHolder.feedDate = (TextView) rowView.findViewById(R.id.feedDate);
			viewHolder.voterCount = (TextView) rowView.findViewById(R.id.voterCount);
			viewHolder.feedUserFullName = (TextView) rowView.findViewById(R.id.feedUserFullName);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.userImage);
			
			//configure unlike button
			
			
			
			LinearLayout.LayoutParams likeW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			likeW.weight = 0.75f;
			likeW.width = 0;
			
			
			LinearLayout.LayoutParams percW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			percW.weight = 2f;
			percW.width = 0;
			
			LinearLayout.LayoutParams unlikeW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			unlikeW.weight = 0.75f;
			unlikeW.width = 0;
			
			LinearLayout.LayoutParams shareW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			shareW.weight = 2.5f;
			shareW.width = 0;
			
			LinearLayout.LayoutParams commentW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			commentW.weight = 2.5f;
			commentW.width = 0;
			
			viewHolder.unLikeMe = (Button) rowView.findViewById(R.id.unLikeId);
			viewHolder.unLikeMe.setLayoutParams(unlikeW);
			viewHolder.unLikeMe.setOnClickListener( new View.OnClickListener() {
				public void onClick(View v) {
					VoteInputDto voteInputDto = new VoteInputDto();
					FeedDto uf = (FeedDto) viewHolder.likeMe.getTag();
					
					voteInputDto.setUf(uf);
					voteInputDto.setVoteType(FeedsConstants.VoteTypes.DISLIKE);
					voteInputDto.setAccuracyPercentage(viewHolder.accuracyPercentage);
					voteInputDto.setLikeMe(viewHolder.likeMe);
					voteInputDto.setUnLikeMe(viewHolder.unLikeMe);
					voteInputDto.setFeedSeq(uf.getFeedSeq());
					voteInputDto.setVoteCountTv(viewHolder.voterCount);
					
					//load all the vote reasons
					voteInputDto.setVoteReason(1);
					new VoteForFeedTask((Activity)context, voteInputDto).execute();
					uf = null;
				
				}

			});

			
			viewHolder.image.setOnClickListener( new View.OnClickListener() { 
				public void onClick(View v) {
					FeedDto uf = (FeedDto) viewHolder.likeMe.getTag();
					Intent i = new Intent(context, FullscreenIntent.class);
					i.putExtra("feedImages", uf.getAuthorFullImageUrl());
					context.startActivity(i);
					uf = null;
				} 
			});
			
			//configure comment button
			viewHolder.commentBtn = (Button) rowView.findViewById(R.id.commentId);
			viewHolder.commentBtn.setLayoutParams(commentW);
			viewHolder.commentBtn.setOnClickListener( new View.OnClickListener() { 
				public void onClick(View v) { 
					FeedDto uf = (FeedDto) viewHolder.likeMe.getTag();
					Intent i = new Intent(context, FeedCommentsActivity.class);
					//interest related info
					i.putExtra("authorId",uf.getAuthorId());
					i.putExtra("feedSeq",Integer.valueOf(uf.getFeedSeq()));
					context.startActivity(i);
					uf = null;
				} 
			});

			viewHolder.lsitItem = rowView;
			//configure share button
			viewHolder.shareBtn = (Button) rowView.findViewById(R.id.shareId);
			viewHolder.shareBtn.setLayoutParams(shareW);
			viewHolder.shareBtn.setOnClickListener( new View.OnClickListener() { 
				public void onClick(View v) {
					FeedDto uf = (FeedDto) viewHolder.likeMe.getTag();
					Intent sendIntent = new Intent();
					sendIntent.setAction(Intent.ACTION_SEND);
					sendIntent.putExtra(Intent.EXTRA_TEXT, getFormattedShareText(uf));
					sendIntent.setType("text/plain");

					context.startActivity(Intent.createChooser(sendIntent, "send to"/*context.getResources().getText(R.string.send_to)*/));
				}

				private String getFormattedShareText(FeedDto uf){
					StringBuffer shareText = new StringBuffer();
					shareText.append(ServerConstants.WEB_URL);
					shareText.append("news/LoadFeedInfo/").
					append(uf.getFeedSeq()).append("/").
					append(uf.getAuthorId()).
					append("/0?zone=Asia%2FBaghdad").append("&feedSeq=").append(uf.getFeedSeq());
					return shareText.toString();
				}
			});

			viewHolder.accuracyPercentage = (TextView) rowView.findViewById(R.id.accuracyPercentage);
			viewHolder.accuracyPercentage.setLayoutParams(percW);
			
			
			//configure like button
			viewHolder.likeMe = (Button) rowView.findViewById(R.id.likeId);
			viewHolder.likeMe.setLayoutParams(likeW);

			viewHolder.likeMe.setOnClickListener( new View.OnClickListener() { 
				public void onClick(View v) {
					VoteInputDto voteInputDto = new VoteInputDto();
					FeedDto uf = (FeedDto) viewHolder.likeMe.getTag();
					voteInputDto.setUf(uf);
					voteInputDto.setVoteType(FeedsConstants.VoteTypes.LIKE);
					voteInputDto.setAccuracyPercentage(viewHolder.accuracyPercentage);
					voteInputDto.setLikeMe(viewHolder.likeMe);
					voteInputDto.setUnLikeMe(viewHolder.unLikeMe);
					voteInputDto.setFeedSeq(uf.getFeedSeq());
					voteInputDto.setVoteCountTv(viewHolder.voterCount);
					voteInputDto.setVoteReason(1);
					uf = null;
					
					//load all the vote reasons
					//new LoadAllVoteReasonsTask(FeedDetailsActivity.this, voteInputDto).execute();
					new VoteForFeedTask((Activity)context, voteInputDto).execute();
				
				} 
			}); 


			//load the feed images
			viewHolder.scrollFooterProgress = (HorizontalScrollView)rowView.findViewById(R.id.feedImgsScrollLayout);
			viewHolder.imagesLayout = (LinearLayout) viewHolder.scrollFooterProgress.findViewById(R.id.feedImgsListLayout);

			rowView.setTag(viewHolder);
		}else{
			// fill data
			viewHolder = (ViewHolder) rowView.getTag();
		}
		final FeedDto s = (FeedDto)names.get(position);
		viewHolder.text.setText(getTruncatedText(s.getFeedText(), FeedsConstants.TEXT_LIMIT));
		viewHolder.feedUserFullName.setText(s.getAuthorFullName());
		if(FeedsConstants.FeedAuthorTypeConst.GROUP.equals(s.getFeedAuthorType())){
			viewHolder.feedUserFullName.setTextColor(Color.RED);
		}else{
			viewHolder.feedUserFullName.setTextColor(Color.BLACK);
		}
		viewHolder.feedDate.setText(s.getFeedDate());

		//get the author image
		if(s.getAuthorSampleImageUrl()!=null&&s.getAuthorSampleImageUrl().trim().length()>0
				&&s.getAuthorFullImageUrl()!=null && s.getAuthorFullImageUrl().trim().length()>0){

			//DisplayImage function from ImageLoader Class
			String imageUrl = s.getAuthorSampleImageUrl();
			if(imageUrl!=null&&imageUrl.trim().length()>ImagesConstants.MIN_IMAGE_URL_SIZE){
				userImageLoader.DisplayImage(imageUrl, viewHolder.image, fromCache);
			}else{
				userImageLoader.DisplayImage(ImagesConstants.NO_USER_IMAGE_URL, viewHolder.image, fromCache);
			}
		}else{
			userImageLoader.DisplayImage(ImagesConstants.NO_USER_IMAGE_URL, viewHolder.image, fromCache);
		}
		
		viewHolder.image.setLayoutParams(new LinearLayout.LayoutParams(
				ImagesConstants.SAMPLE_IMAGE_SIZE, 
				ImagesConstants.SAMPLE_IMAGE_SIZE));
		
		if(s.getFeedAccuracy()!=null){
			Double likeCount = Double.valueOf(s.getFeedAccuracy());
			viewHolder.accuracyPercentage.setText(likeCount.longValue()+" %");
		}else{
			viewHolder.accuracyPercentage.setText("0 %");
		}

		if(s.getVoteCount()!=null&&s.getVoteCount()>0){
			viewHolder.voterCount.setText(context.getResources().getString(R.string.noVoters)+s.getVoteCount());
		}else{
			viewHolder.voterCount.setText("");
		}



		//if this is the News of the user disable the voting buttons
		String userId = ClientConfiguration.getUser((BaseActivity)context).get(DatabaseHandler.KEY_ID);
		if(s.getAuthorId()!=null&&!GeneralUtils.isStringEmpty(userId)){
			if(s.isMyOwnFeed()){
				viewHolder.unLikeMe.setTextColor(context.getResources().getColor(R.color.grey));
				viewHolder.likeMe.setTextColor(context.getResources().getColor(R.color.grey));
				viewHolder.unLikeMe.setEnabled(false);
				viewHolder.likeMe.setEnabled(false);
			}else{
				viewHolder.unLikeMe.setEnabled(true);
				viewHolder.likeMe.setEnabled(true);

				//set the color to blue if the user has made an action on this News
				Integer requesterLikeAction = s.getRequesterVoteAction();
				if(requesterLikeAction!=null){
					if(requesterLikeAction.equals(1)){
						viewHolder.likeMe.setTextColor(Color.BLUE);
						viewHolder.unLikeMe.setTextColor(Color.BLACK);
					}else if(requesterLikeAction.equals(-1)){
						viewHolder.unLikeMe.setTextColor(Color.BLUE);
						viewHolder.likeMe.setTextColor(Color.BLACK);
					}else{
						viewHolder.unLikeMe.setTextColor(Color.BLACK);
						viewHolder.likeMe.setTextColor(Color.BLACK);
					}
				}else{
					viewHolder.unLikeMe.setTextColor(Color.BLACK);
					viewHolder.likeMe.setTextColor(Color.BLACK);
				}
			}
		}


		viewHolder.likeMe.setTag(s);
		if(s.getCommentCount()!=null && s.getCommentCount()>0){
			viewHolder.commentBtn.setText(context.getResources().getString(R.string.Comment)+" ("+s.getCommentCount()+")");
		}else{
			viewHolder.commentBtn.setText(context.getResources().getString(R.string.Comment));
		}


		//load feed images
		final String feedFullImagesList = s.getFirstFullImageUrl();
		final String feedSampleImagesList = s.getFirstSampleImageUrl();
		
		final List<ImageDto>feedImagesList = getFeedImagesList(feedFullImagesList, feedSampleImagesList);
		viewHolder.imagesLayout.removeAllViews();
		viewHolder.imagesLayout.removeAllViewsInLayout();

		if(feedFullImagesList!=null&&feedFullImagesList.trim().length()>0&&!feedFullImagesList.equalsIgnoreCase("null")&&
				feedSampleImagesList!=null&&feedSampleImagesList.trim().length()>0&&!feedSampleImagesList.equalsIgnoreCase("null")){

			viewHolder.scrollFooterProgress.setVisibility(View.VISIBLE);

			int feedImageWidth = 
					Double.valueOf(
							context.
							getResources().
							getDisplayMetrics().
							widthPixels).
							intValue();
			
			for(ImageDto imageDto : feedImagesList){
				ImageView img = new ImageView(context);
				
				img.setLayoutParams(new LinearLayout.LayoutParams(
						feedImageWidth/2, feedImageWidth/2));
				
				feedImageLoader.DisplayImage(imageDto.getSampleImageUrl(), img, fromCache);
				img.setPadding(
						ImagesConstants.feedImagesPadding, 
						0, 
						ImagesConstants.feedImagesPadding, 
						0);
				viewHolder.imagesLayout.addView(img);
				viewHolder.imagesLayout.setPadding(
						0, 
						0, 
						0, 
						0);
				img.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(context, FullscreenIntent.class);
						IntentHelper.addObjectForKey(feedImagesList, "imagesDtosList");
						context.startActivity(i);
					}
				});
				img.setContentDescription(""+imageDto.getImageKey());

			}
			
		}

		return rowView;
	}
	
	private List<ImageDto> getFeedImagesList(String feedFullImagesStringList,
			String feedSampleImagesStringList) {
		
		List<ImageDto> feedImagesDtosList = new ArrayList<ImageDto>();

		if(feedFullImagesStringList==null||feedFullImagesStringList.trim().length()==0||
				feedSampleImagesStringList==null||feedSampleImagesStringList.trim().length()==0){
			return feedImagesDtosList;
		}
		//Full Images
		List<String> feedFullImagesList = new ArrayList<String>(); 
		StringTokenizer stringTokenizer = new StringTokenizer(feedFullImagesStringList, ",");
		while(stringTokenizer.hasMoreTokens()){
			feedFullImagesList.add(stringTokenizer.nextToken().replace(".full", ""));
		}
		
		//Sample Images
		List<String> feedSampleImagesList = new ArrayList<String>(); 
		stringTokenizer = new StringTokenizer(feedSampleImagesStringList, ",");
		while(stringTokenizer.hasMoreTokens()){
			feedSampleImagesList.add(stringTokenizer.nextToken().replace(".sample", ""));
		}
		
		for(String fullImage : feedFullImagesList){
			ImageDto imageDto = new ImageDto();
			imageDto.setFullImageUrl(fullImage+".full");
			imageDto.setSampleImageUrl(feedSampleImagesList.get(feedSampleImagesList.indexOf(fullImage))+".sample");
			feedImagesDtosList.add(imageDto);
		}
		return feedImagesDtosList;
	}

	public void addAll(ArrayList<FeedDto> list) {
		names.addAll(list);
	}
}
