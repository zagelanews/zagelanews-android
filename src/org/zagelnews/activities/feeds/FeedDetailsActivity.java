package org.zagelnews.activities.feeds;

import java.util.Date;
import java.util.List;

import org.zagelnews.activities.BaseFragmentActivity;
import org.zagelnews.activities.R;
import org.zagelnews.activities.users.LoginActivity;
import org.zagelnews.activities.utils.FullscreenIntent;
import org.zagelnews.adapters.VoteReasonsAdapter;
import org.zagelnews.constants.FeedsConstants;
import org.zagelnews.constants.ImagesConstants;
import org.zagelnews.constants.MapConstants;
import org.zagelnews.constants.ServerConstants;
import org.zagelnews.db.DatabaseHandler;
import org.zagelnews.dtos.feeds.FeedDto;
import org.zagelnews.dtos.feeds.ImageDto;
import org.zagelnews.dtos.feeds.VoteInputDto;
import org.zagelnews.dtos.feeds.VoteReasonDto;
import org.zagelnews.dtos.geo.PointDto;
import org.zagelnews.tasks.TaskIds;
import org.zagelnews.tasks.feeds.DeleteFeedImageTask;
import org.zagelnews.tasks.feeds.DeleteFeedTask;
import org.zagelnews.tasks.feeds.EditFeedTask;
import org.zagelnews.tasks.feeds.GetFeedDetailsTask;
import org.zagelnews.tasks.feeds.VoteForFeedTask;
import org.zagelnews.ui.images.ImageLoader;
import org.zagelnews.utils.AndroidUtils;
import org.zagelnews.utils.ClientConfiguration;
import org.zagelnews.utils.DateUtils;
import org.zagelnews.utils.GeneralUtils;
import org.zagelnews.utils.IntentHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * Feed details and operations
 * @author oomran
 *
 */
public class FeedDetailsActivity extends BaseFragmentActivity {

	/**
	 * Holder for the view components
	 * @author oomran
	 *
	 */
	public static class ViewHolder {
		public TextView feedTextV;
		public EditText feedTextE;
		public TextView feedDateV;
		public TextView authorFullNameV;
		public TextView voterCount;
		public Button likeMe;
		public TextView accuracyPercentage;
		public Button unLikeMe;
		public Button shareBtn;
		public Button commentBtn;
		public ImageView userImage;
		public LinearLayout card;
	}public ViewHolder viewHolder;


	//input parameters
	private Integer feedSeq;
	private Integer feedAuthorType;

	//for internal use only
	private boolean fromCache = true;
	private ImageView selectedImg;
	private FeedDto feedDto = new FeedDto();

	//vote reason alert
	private AlertDialog voteReasonsDlg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_details);

		try { 
			viewHolder = new ViewHolder();

			//google map configuration 
			if (googleMap == null) {
				initMap();
				if(googleMap!=null){
					googleMap.getUiSettings().setScrollGesturesEnabled(false);
					googleMap.getUiSettings().setZoomGesturesEnabled(false);
					googleMap.getUiSettings().setZoomControlsEnabled(false);
				}

			}

			//disable the soft keyboard
			AndroidUtils.disableSpftKeyboard(this);


			//load the input parameters
			Intent myIntent = getIntent();

			feedSeq= myIntent.getIntExtra("feedSeq",-1);

			if(feedSeq.equals(-1)){

				Uri uri = myIntent.getData();
				if(uri!=null){
					String feedSeqStr = uri.getQueryParameter("feedSeq");
					if(feedSeqStr!=null&&feedSeqStr.trim().length()>0){
						feedSeq = Integer.valueOf(feedSeqStr);
					}
				}
				
				if(feedSeq.equals(-1)){
					Intent i = new Intent(this, LoginActivity.class);
					startActivity(i);
				}
			}
			feedAuthorType= 0;//myIntent.getIntExtra("feedAuthorType",-1);

			//get the details of the given feed
			new GetFeedDetailsTask(this, feedSeq, feedAuthorType).execute();

		} catch (Exception e) {
			Toast.makeText(FeedDetailsActivity.this, getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * fill the view with the details of the given feed
	 */
	public void fillView() {

		viewHolder.card = (LinearLayout) findViewById(R.id.card);

		//load the feed images into the view




		int feedImageWidth = 
				Double.valueOf(
						this.
						getResources().
						getDisplayMetrics().
						widthPixels).
						intValue();
		ImageLoader imageLoader = new ImageLoader(
				this, 
				feedImageWidth,
				(feedImageWidth/2),
				ImageLoader.stub_id_noimage);


		ImageLoader userImageLoader = new ImageLoader(
				this, 
				ImageLoader.stub_id_defuser);


		final List<ImageDto> feedImagesList = feedDto.getFeedImages();
		if(feedImagesList!=null&&feedImagesList.size()>0){
			HorizontalScrollView scrollFooterProgress = (HorizontalScrollView)findViewById(R.id.feedImgsScrollLayout);
			LinearLayout imagesLayout = (LinearLayout) 
					scrollFooterProgress.findViewById(R.id.feedImgsListLayout);

			for(ImageDto imageDto : feedImagesList){
				ImageView img = new ImageView(this);
				img.setPadding(
						ImagesConstants.feedImagesPadding, 
						0, 
						ImagesConstants.feedImagesPadding, 
						0);
				img.setLayoutParams(new LinearLayout.LayoutParams(
						feedImageWidth/2, feedImageWidth/2));

				imageLoader.DisplayImage(imageDto.getSampleImageUrl(), img, fromCache);
				img.setAdjustViewBounds(true);
				imagesLayout.addView(img);
				img.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(FeedDetailsActivity.this, FullscreenIntent.class);
						IntentHelper.addObjectForKey(feedImagesList, "imagesDtosList");
						FeedDetailsActivity.this.startActivity(i);
					}
				});
				img.setContentDescription(""+imageDto.getImageKey());
				img.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View arg0) {
						selectedImg = ((ImageView)arg0);
						return false;
					}
				});
				registerForContextMenu(img);

			}
		}


		//load the author image
		String userImageUrl=feedDto.getAuthorSampleImageUrl();
		if(userImageUrl!=null&&userImageUrl.trim().length()>0&&userImageUrl.trim().length()>ImagesConstants.MIN_IMAGE_URL_SIZE){
			viewHolder.userImage = (ImageView) findViewById(R.id.userImage);
			viewHolder.userImage.getLayoutParams().height = ImagesConstants.SAMPLE_IMAGE_SIZE;
			viewHolder.userImage.getLayoutParams().width = ImagesConstants.SAMPLE_IMAGE_SIZE;

			userImageLoader.DisplayImage(userImageUrl, viewHolder.userImage, fromCache);
		}


		//load the location of the feed
		List<PointDto> feedLocations = feedDto.getFeedLocations();
		if(feedLocations!=null&&feedLocations.size()>0){
			for(PointDto location: feedLocations){
				if(location.getLatitude()!=null&&location.getLongitude()!=null){
					markerPoint = new LatLng(location.getLatitude(), location.getLongitude());
					googleMap.addMarker(new MarkerOptions()
					.position(markerPoint)
					.icon(BitmapDescriptorFactory.defaultMarker(
							BitmapDescriptorFactory.HUE_AZURE)));
					googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPoint, MapConstants.GOOGLE_MAPS_ZOOM));
				}
			}
		}

		//load the feed text
		//make the feed text view to recognize links
		if(viewHolder.feedTextV==null){
			viewHolder.feedTextV = (TextView) findViewById(R.id.feedTextV);
			viewHolder.feedTextV.setMovementMethod(new ScrollingMovementMethod());
			viewHolder.feedTextV.setText(feedDto.getFeedText()); 
			Linkify.addLinks(viewHolder.feedTextV, Linkify.ALL);
		}

		//load the feed date
		if(viewHolder.feedDateV==null){
			viewHolder.feedDateV = (TextView) findViewById(R.id.feedDateV);
		}
		viewHolder.feedDateV.setText(feedDto.getFeedDate()); 


		//load the feed author full name 
		if(viewHolder.authorFullNameV==null){
			viewHolder.authorFullNameV = (TextView) findViewById(R.id.feedUserFullNameV);
		}
		viewHolder.authorFullNameV.setText(feedDto.getAuthorFullName()); 

		//load the feed author type
		if(FeedsConstants.FeedAuthorTypeConst.GROUP.equals(feedDto.getFeedAuthorType())){
			viewHolder.authorFullNameV.setTextColor(Color.RED);
		}else{
			viewHolder.authorFullNameV.setTextColor(Color.BLACK);
		}


		//enable delete feed if the its for the logged in user
		Button deleteFeedButton = (Button) findViewById(R.id.delFeedId);
		String userId = ClientConfiguration.getUser(this).get(DatabaseHandler.KEY_ID);
		boolean isUserTheGroupAdmin = this.getIntent().getBooleanExtra("isUserTheGroupAdmin", false);
		if(isUserTheGroupAdmin||feedDto.isMyOwnFeed()){
			deleteFeedButton.setVisibility(View.VISIBLE);
			deleteFeedButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					new DeleteFeedTask(FeedDetailsActivity.this, feedSeq).execute();
				}
			});
		}else{
			deleteFeedButton.setVisibility(View.GONE);
		}

		//enable edit feed if the its for the logged in user, and the delete is still available	
		Button editFeedButton = (Button) findViewById(R.id.editFeedId);
		if(feedDto.isMyOwnFeed()&&editIsNotExpired(feedDto.getFeedDate())){
			editFeedButton.setVisibility(View.VISIBLE);
			editFeedButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewHolder.feedTextE = (EditText) findViewById(R.id.feedTextE);
					if(viewHolder.feedTextE!=null){

						//enable the edit box
						if(viewHolder.feedTextE.getVisibility()==View.GONE){
							viewHolder.feedTextE.setVisibility(View.VISIBLE);
							viewHolder.feedTextE.setText(viewHolder.feedTextV.getText());
							viewHolder.feedTextV.setVisibility(View.GONE);
							editMode = true;
						}else{//perform the edit operation
							String feedTextStr = viewHolder.feedTextE.getText().toString();

							if (GeneralUtils.isStringEmpty(feedTextStr))
							{
								Toast.makeText(FeedDetailsActivity.this, getResources().getString(R.string.feedTextEmpty), Toast.LENGTH_LONG).show();
							}else if (markerPoint==null){
								Toast.makeText(FeedDetailsActivity.this, getResources().getString(R.string.locationEmpty), Toast.LENGTH_LONG).show();
							}
							else{
								new EditFeedTask(
										FeedDetailsActivity.this, 
										feedSeq, 
										viewHolder.feedTextE.getText().toString(), 
										markerPoint.latitude, 
										markerPoint.longitude).execute();
							}
						}
					}
				}
			});

		}else{//the current user is not the author of the feed
			editFeedButton.setVisibility(View.GONE);
		}

		//load the vote count
		viewHolder.voterCount = (TextView) findViewById(R.id.voterCount);
		if(feedDto.getVoteCount()!=null&&feedDto.getVoteCount()>0){
			viewHolder.voterCount.setText(getResources().getString(R.string.noVoters)+feedDto.getVoteCount());
		}

		//configure comment button
		viewHolder.commentBtn = (Button) findViewById(R.id.commentId);
		Integer commentCount = feedDto.getCommentCount();
		if(commentCount!=null && commentCount>0){
			viewHolder.commentBtn.setText(getResources().getString(R.string.Comment)+" ("+commentCount+")");
		}
		viewHolder.commentBtn.setOnClickListener( new View.OnClickListener() { 
			public void onClick(View v) { 
				Intent i = new Intent(FeedDetailsActivity.this, FeedCommentsActivity.class);
				//interest related info
				i.putExtra("feedSeq",Integer.valueOf(feedSeq));
				FeedDetailsActivity.this.startActivity(i);
			} 
		});

		//configure share button
		viewHolder.shareBtn = (Button) findViewById(R.id.shareId);
		viewHolder.shareBtn.setOnClickListener( new View.OnClickListener() { 
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, getFormattedShareText(feedDto));
				sendIntent.setType("text/plain");

				FeedDetailsActivity.this.startActivity(Intent.createChooser(sendIntent, "send to"/*context.getResources().getText(R.string.send_to)*/));
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

		viewHolder.accuracyPercentage = (TextView) findViewById(R.id.accuracyPercentage);
		Integer accuracyPercentage = feedDto.getFeedAccuracy();
		if(accuracyPercentage!=null){
			Double likeCount = Double.valueOf(accuracyPercentage);
			viewHolder.accuracyPercentage.setText(likeCount.longValue()+" %");
		}else{
			viewHolder.accuracyPercentage.setText("0 %");
		}


		//configure vote buttons
		viewHolder.likeMe = (Button) findViewById(R.id.likeId);
		viewHolder.likeMe.setOnClickListener( new View.OnClickListener() { 
			public void onClick(View v) {
				VoteInputDto voteInputDto = new VoteInputDto();
				voteInputDto.setUf(feedDto);
				voteInputDto.setVoteType(FeedsConstants.VoteTypes.LIKE);
				voteInputDto.setAccuracyPercentage(viewHolder.accuracyPercentage);
				voteInputDto.setLikeMe(viewHolder.likeMe);
				voteInputDto.setUnLikeMe(viewHolder.unLikeMe);
				voteInputDto.setFeedSeq(feedSeq);
				voteInputDto.setVoteCountTv(viewHolder.voterCount);
				voteInputDto.setVoteReason(1);

				//load all the vote reasons
				//new LoadAllVoteReasonsTask(FeedDetailsActivity.this, voteInputDto).execute();
				new VoteForFeedTask(FeedDetailsActivity.this, voteInputDto).execute();

			} 
		}); 

		viewHolder.unLikeMe = (Button) findViewById(R.id.unLikeId);
		viewHolder.unLikeMe.setOnClickListener( new View.OnClickListener() { 
			public void onClick(View v) {
				VoteInputDto voteInputDto = new VoteInputDto();

				voteInputDto.setUf(feedDto);
				voteInputDto.setVoteType(FeedsConstants.VoteTypes.DISLIKE);
				voteInputDto.setAccuracyPercentage(viewHolder.accuracyPercentage);
				voteInputDto.setLikeMe(viewHolder.likeMe);
				voteInputDto.setUnLikeMe(viewHolder.unLikeMe);
				voteInputDto.setFeedSeq(feedSeq);
				voteInputDto.setVoteCountTv(viewHolder.voterCount);

				//load all the vote reasons
				voteInputDto.setVoteReason(1);
				new VoteForFeedTask(FeedDetailsActivity.this, voteInputDto).execute();
			} 
		});

		//manage justify enabled
		//if this is the News of the user disable the voting buttons
		userId = ClientConfiguration.getUser(this).get(DatabaseHandler.KEY_ID);
		if(feedDto.getAuthorId()!=null&&!GeneralUtils.isStringEmpty(userId)){
			if(feedDto.isMyOwnFeed()){// the user is the owner of the feed
				viewHolder.unLikeMe.setTextColor(getResources().getColor(R.color.grey));
				viewHolder.likeMe.setTextColor(getResources().getColor(R.color.grey));
				viewHolder.unLikeMe.setEnabled(false);
				viewHolder.likeMe.setEnabled(false);
			}else{
				viewHolder.unLikeMe.setEnabled(true);
				viewHolder.likeMe.setEnabled(true);

				//set the color to blue if the user has made an action on this News
				Integer requesterLikeAction = feedDto.getRequesterVoteAction();
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



		/*******************set the feed details listeners************************************/

		viewHolder.feedDateV.setOnClickListener( new View.OnClickListener() { 
			public void onClick(View v) { 
				Intent i = new Intent(FeedDetailsActivity.this, ProfileFeedsListActivity.class);
				i.putExtra("profileOwnerId", feedDto.getAuthorId());
				i.putExtra("feedAuthorType", feedDto.getFeedAuthorType());
				i.putExtra("profileOwnerFullName", feedDto.getAuthorFullName());
				FeedDetailsActivity.this.startActivity(i);
			} 
		});

		viewHolder.authorFullNameV.setOnClickListener( new View.OnClickListener() { 
			public void onClick(View v) { 
				Intent i = new Intent(FeedDetailsActivity.this, ProfileFeedsListActivity.class);
				i.putExtra("profileOwnerId", feedDto.getAuthorId());
				i.putExtra("feedAuthorType", feedDto.getFeedAuthorType());
				i.putExtra("profileOwnerFullName", feedDto.getAuthorFullName());
				FeedDetailsActivity.this.startActivity(i);
			} 
		});

		if(viewHolder.userImage!=null){
			viewHolder.userImage.setOnClickListener( new View.OnClickListener() { 
				public void onClick(View v) {
					Intent i = new Intent(FeedDetailsActivity.this, FullscreenIntent.class);
					i.putExtra("feedImages", feedDto.getAuthorFullImageUrl());
					FeedDetailsActivity.this.startActivity(i);
				} 
			});
		}
	}
	/*
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}	
		return super.onOptionsItemSelected(item);
	}*/



	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		//show the context menu of the image, if the in the edit mode
		if(editMode){
			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.image_menu, menu);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected (MenuItem item){
		//delete the selected image
		if (item.getItemId() == R.id.menuitem2_delete)
		{
			if( selectedImg!=null){
				new DeleteFeedImageTask(this, feedSeq,  selectedImg.getContentDescription()+"").execute();
				selectedImg.setVisibility(View.GONE);
			}
			selectedImg = null;
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * check if the feed date is not more than 24 hours of the current date
	 * @param feedDate
	 * @return
	 */
	private boolean editIsNotExpired(String feedDate) {
		boolean editIsNotExpired = false;
		Date feedDateD = DateUtils.parseFullDate(feedDate);
		if(feedDateD!=null){
			Integer hours = DateUtils.getDiffHours(feedDateD, new Date());
			if(hours<=FeedsConstants.FEED_EDIT_EXPIRY){
				editIsNotExpired = true;
			}
		}
		return editIsNotExpired;
	}

	/*************************************************fields accesses*********************************/

	public ViewHolder getViewHolder() {
		return viewHolder;
	}

	public void setViewHolder(ViewHolder viewHolder) {
		this.viewHolder = viewHolder;
	}

	public boolean isFromCache() {
		return fromCache;
	}

	public void setFromCache(boolean fromCache) {
		this.fromCache = fromCache;
	}

	public ImageView getSelectedImg() {
		return selectedImg;
	}

	public void setSelectedImg(ImageView selectedImg) {
		this.selectedImg = selectedImg;
	}

	public FeedDto getFeedDto() {
		return feedDto;
	}

	public void setFeedDto(FeedDto feedDto) {
		this.feedDto = feedDto;
	}

	@Override
	public void getTaskResult(Object result, Integer taskId) {
		if(TaskIds.LoadAllVoteReasonsTask.equals(taskId)){
			if(result!=null){
				final VoteInputDto voteInputDto = (VoteInputDto)result;
				final VoteReasonsAdapter voteReasonsAdapter = voteInputDto.getVoteReasonsAdapter();
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(this.getResources().getString(R.string.voteReasonsStr));
				builder.setSingleChoiceItems(voteReasonsAdapter, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						VoteReasonDto voteReasonDto = voteReasonsAdapter.getItem(item);
						Integer reasonKey = voteReasonDto.getReasonKey();
						voteInputDto.setVoteReason(reasonKey);
						new VoteForFeedTask(FeedDetailsActivity.this, voteInputDto).execute();
						voteReasonsDlg.dismiss();
					}
				});
				voteReasonsDlg = builder.create();
				voteReasonsDlg.show();
			}
		}
	}

}
