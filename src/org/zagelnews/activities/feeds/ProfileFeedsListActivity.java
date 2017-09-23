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
import org.zagelnews.tasks.feeds.GetUserInfoTask;
import org.zagelnews.tasks.feeds.LoadProfileFeedsListTask;
import org.zagelnews.tasks.feeds.VoteForFeedTask;

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
 * Load the feeds for the given profile
 * @author oomran
 *
 */
public class ProfileFeedsListActivity extends BaseActivity {

	/**
	 * Holder for the view components
	 * @author oomran
	 *
	 */
	public static class ViewHolder {
		public View footerView;
		//public ImageView coverImageV;
		public TextView fetchMoreFeeds;
		public ListView v;
		public Boolean flag_loading = true;
		public Boolean flag_initialy_loaded = false;
	}
	public ViewHolder holder;

	//feeds list adapter
	private UserFeedsAdapter adapter;

	//activity parameters
	Integer profileOwnerId;
	Integer feedAuthorType;
	String profileOwnerFullName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_feeds);


		//get the feed parameters
		Intent intent = getIntent();
		profileOwnerId = intent.getIntExtra("profileOwnerId", -1);
		feedAuthorType = intent.getIntExtra("feedAuthorType", -1);
		profileOwnerFullName = intent.getStringExtra("profileOwnerFullName");

		this.setTitle(profileOwnerFullName);

		holder = new ViewHolder();
		holder.v = (ListView) findViewById(R.id.feedsList);
		holder.footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
				inflate(R.layout.activity_user_feeds_footer, null, false);
		holder.v.addFooterView(holder.footerView);
		adapter = new UserFeedsAdapter(this, new ArrayList<ZagelnewsDto>());
		holder.v.setAdapter(adapter);
		holder.fetchMoreFeeds = (TextView) holder.footerView.findViewById(R.id.fetchMoreUserFeeds);
		//holder.coverImageV =  (ImageView) findViewById(R.id.coverImage);


		//load the user info
		new GetUserInfoTask(this, feedAuthorType, profileOwnerId).execute();


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
					new LoadProfileFeedsListTask(ProfileFeedsListActivity.this, feedSeqFrom).execute();
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
				if(userFeeds!=null){
					i.putExtra("feedSeq",userFeeds.getFeedSeq());
					i.putExtra("feedAuthorType",userFeeds.getFeedAuthorType());
					startActivity(i);
				}
			}
		});



		/*		//to handle the loading of the feeds on scrolling
		holder.v.setOnScrollListener(new OnScrollListener() {

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
				new LoadProfileFeedsListTask(ProfileFeedsListActivity.this, feedSeqFrom).execute();
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


	public Integer getProfileOwnerId() {
		return profileOwnerId;
	}


	public void setProfileOwnerId(Integer profileOwnerId) {
		this.profileOwnerId = profileOwnerId;
	}


	public Integer getFeedAuthorType() {
		return feedAuthorType;
	}


	public void setFeedAuthorType(Integer feedAuthorType) {
		this.feedAuthorType = feedAuthorType;
	}


	public String getProfileOwnerFullName() {
		return profileOwnerFullName;
	}


	public void setProfileOwnerFullName(String profileOwnerFullName) {
		this.profileOwnerFullName = profileOwnerFullName;
	}


	public UserFeedsAdapter getAdapter() {
		return adapter;
	}


	public void setAdapter(UserFeedsAdapter adapter) {
		this.adapter = adapter;
	}


	@Override
	public void getTaskResult(Object result, Integer taskId) {
		if(TaskIds.GetUserInfoTask.equals(taskId)){
			//load the feeds initially
			new LoadProfileFeedsListTask(this, null).execute();
		}else if(TaskIds.LoadAllVoteReasonsTask.equals(taskId)){
			if(result!=null){
				final VoteInputDto voteInputDto = (VoteInputDto)result;
				final VoteReasonsAdapter voteReasonsAdapter = voteInputDto.getVoteReasonsAdapter();
				AlertDialog voteReasonsDlg = null;
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(this.getResources().getString(R.string.voteReasonsStr));
				builder.setSingleChoiceItems(voteReasonsAdapter, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						VoteReasonDto voteReasonDto = voteReasonsAdapter.getItem(item);
						Integer reasonKey = voteReasonDto.getReasonKey();
						voteInputDto.setVoteReason(reasonKey);
						new VoteForFeedTask(ProfileFeedsListActivity.this, voteInputDto).execute();
					}
				});
				voteReasonsDlg = builder.create();
				voteReasonsDlg.show();
			}
		}
	}

}
