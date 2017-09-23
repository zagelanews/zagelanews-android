package org.zagelnews.activities.feeds;

import java.util.ArrayList;

import org.zagelnews.activities.BaseActivity;
import org.zagelnews.activities.R;
import org.zagelnews.adapters.UserFeedsAdapter;
import org.zagelnews.adapters.VoteReasonsAdapter;
import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.dtos.feeds.FeedDto;
import org.zagelnews.dtos.feeds.VoteInputDto;
import org.zagelnews.dtos.feeds.VoteReasonDto;
import org.zagelnews.tasks.TaskIds;
import org.zagelnews.tasks.feeds.LoadFilteredFeedsListTask;
import org.zagelnews.tasks.feeds.VoteForFeedTask;
import org.zagelnews.utils.GeneralUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Load filtered list of feeds based on the passed parameters
 * @author oomran
 *
 */
public class FilteredFeedsListActivity extends BaseActivity {

	/**
	 * Holder for the view components
	 * @author oomran
	 *
	 */
	public static class ViewHolder {
		public View footerView;
		public TextView fetchMoreFeeds;
		public ListView v;
		public Boolean flag_loading = true;
		public Boolean flag_initialy_loaded = false;
	} public ViewHolder holder;

	//feeds list adapter
	private UserFeedsAdapter adapter;
	
	//vote reason alert
	private AlertDialog voteReasonsDlg = null;

	//get the feed filtration Criteria
	private Double interestLatitude;
	private Double interestLongitude;
	private Double interestRadious;
	private String interestIds;
	
	private Double currentLatitude;
	private Double currentLongitude;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feeds);

		//clear the cached images, so if someone updated his image it will get updated
		//AndroidUtils.clearImageCache(this);


		//get the activity parameters
		Intent intent = getIntent();
		interestLatitude = intent.getDoubleExtra("circleLatitude", -1);
		interestLongitude = intent.getDoubleExtra("circleLongitude", -1);
		interestRadious = intent.getDoubleExtra("circleRadious", -1);
		
		
		currentLatitude = intent.getDoubleExtra("currentLatitude", -1);
		currentLongitude = intent.getDoubleExtra("currentLongitude", -1);

		if(intent.getExtras()!=null){
			String [] interestIdsArr = intent.getExtras().getStringArray("interestIds");
			interestIds = GeneralUtils.arrToCommaSeperated(interestIdsArr);
		}
		
		holder = new ViewHolder();
		holder.v = (ListView) findViewById(R.id.feedsList);
		holder.footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_user_feeds_footer, null, false);
		holder.v.addFooterView(holder.footerView);
		adapter = new UserFeedsAdapter(this, new ArrayList<ZagelnewsDto>());
		holder.v.setAdapter(adapter);
		
		holder.fetchMoreFeeds = (TextView) holder.footerView.findViewById(R.id.fetchMoreUserFeeds);
		
		
		//load the feeds initially
		new LoadFilteredFeedsListTask(this, null).execute();

		
		
		/***********************************Initialize the listeners***************************************************************/
		
		//fetch more feeds from the server
		holder.fetchMoreFeeds.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(holder.flag_loading == false||holder.flag_initialy_loaded)
				{
					Integer feedSeqFrom = null;
					if(adapter.getCount()>0){
						feedSeqFrom =((FeedDto) adapter.getItem(adapter.getCount()-1)).getFeedSeq();
					}
					new LoadFilteredFeedsListTask(FilteredFeedsListActivity.this, feedSeqFrom).execute();
				}
			}
		});


		//go to the feed details
		holder.v.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent i = new Intent(getApplicationContext(), FeedDetailsActivity.class);
				FeedDto userFeeds = ((FeedDto)holder.v.getAdapter().getItem(arg2));
				//interest related info
				i.putExtra("feedSeq",userFeeds.getFeedSeq());
				i.putExtra("feedAuthorType",userFeeds.getFeedAuthorType());
				startActivity(i);
			}
		});



		//to handle the loading of the feeds on scrolling
/*		holder.v.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == 0){
					holder.flag_loading = false;
				}   

			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//get more latest feeds when the scroll touch the top
				if(firstVisibleItem==0){
					if(holder.flag_loading == false)
					{
						holder.flag_loading = true;
						additems(null, firstVisibleItem);
					}
				}
			}

			private void additems(Integer feedSeqFrom, int position) {
				new LoadFilteredFeedsListTask(FilteredFeedsListActivity.this, feedSeqFrom).execute();
				holder.v.setSelection(position);
			}
		});*/
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	/*************************************************fields accesses*********************************/
	
	public ViewHolder getHolder() {
		return holder;
	}


	public void setHolder(ViewHolder holder) {
		this.holder = holder;
	}


	public UserFeedsAdapter getAdapter() {
		return adapter;
	}


	public void setAdapter(UserFeedsAdapter adapter) {
		this.adapter = adapter;
	}


	public Double getInterestLatitude() {
		return interestLatitude;
	}


	public void setInterestLatitude(Double interestLatitude) {
		this.interestLatitude = interestLatitude;
	}


	public Double getInterestLongitude() {
		return interestLongitude;
	}


	public void setInterestLongitude(Double interestLongitude) {
		this.interestLongitude = interestLongitude;
	}


	public Double getInterestRadious() {
		return interestRadious;
	}


	public void setInterestRadious(Double interestRadious) {
		this.interestRadious = interestRadious;
	}


	public String getInterestIds() {
		return interestIds;
	}


	public void setInterestIds(String interestIds) {
		this.interestIds = interestIds;
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
						new VoteForFeedTask(FilteredFeedsListActivity.this, voteInputDto).execute();
						voteReasonsDlg.dismiss();
					}
				});
				voteReasonsDlg = builder.create();
				voteReasonsDlg.show();
			}
		}
	}


	public Double getCurrentLatitude() {
		return currentLatitude;
	}


	public void setCurrentLatitude(Double currentLatitude) {
		this.currentLatitude = currentLatitude;
	}


	public Double getCurrentLongitude() {
		return currentLongitude;
	}


	public void setCurrentLongitude(Double currentLongitude) {
		this.currentLongitude = currentLongitude;
	}


}
