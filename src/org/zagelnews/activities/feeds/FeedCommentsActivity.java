package org.zagelnews.activities.feeds;

import java.util.ArrayList;

import org.zagelnews.activities.BaseBorderlessActivity;
import org.zagelnews.activities.R;
import org.zagelnews.adapters.FeedCommentsAdapter;
import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.dtos.feeds.FeedCommentDto;
import org.zagelnews.tasks.feeds.AddFeedCommentTask;
import org.zagelnews.tasks.feeds.LoadFeedCommentsListTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Load the comments list for the given feed
 * @author oomran
 *
 */
public class FeedCommentsActivity extends BaseBorderlessActivity {

	/**
	 * Holder for the view components
	 * @author oomran
	 *
	 */
	public static class ViewHolder {
		public EditText newComment;
		public View footerView;
		public ListView v;
		public TextView fetchMoreComments;
		public Boolean flag_loading = true;
		public Boolean flag_initialy_loaded = false;
	}
	public ViewHolder holder;


	//feeds list adapter
	private FeedCommentsAdapter adapter;

	//get the feed to load its comments
	private Integer feedSeq;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feed_comments);

		//get the feed Seq
		Intent intent = getIntent();
		feedSeq = intent.getIntExtra("feedSeq", -1);

		
		holder = new ViewHolder();
		holder.v = (ListView) findViewById(R.id.commentList);
		holder.footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_feed_comments_footer, null, false);
		holder.newComment= (EditText) holder.footerView.findViewById(R.id.newComment);
		holder.v.addFooterView(holder.footerView);
		holder.fetchMoreComments= (TextView) holder.footerView.findViewById(R.id.fetchMoreComments);
		adapter = new FeedCommentsAdapter(FeedCommentsActivity.this, new ArrayList<ZagelnewsDto>());
		holder.v.setAdapter(adapter);

		
		//load the feeds initially
		new LoadFeedCommentsListTask(this, null).execute();
		
		
		/***********************************Initialize the listeners***************************************************************/
		//add new comment
		holder.newComment.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {
					holder.newComment.setEnabled(false);
					// Perform action on key press
					new AddFeedCommentTask(FeedCommentsActivity.this).execute();
					return true;
				}
				return false;
			}
		});



		//fetch more comments from the server
		holder.fetchMoreComments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(holder.flag_loading == false||holder.flag_initialy_loaded)
				{
					Integer commentSeq = ((FeedCommentDto)adapter.getItem(adapter.getCount()-1)).getCommentSeq();
					new LoadFeedCommentsListTask(FeedCommentsActivity.this, commentSeq).execute();
					adapter.notifyDataSetChanged();
				}
			}
		});


		//to handle the loading of the feeds on scrolling
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

			private void additems(Integer commentSeq, int position) {
				new LoadFeedCommentsListTask(FeedCommentsActivity.this, commentSeq).execute();
				//adapter.notifyDataSetChanged();
				holder.v.setSelection(position);
			
			}
		});
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


	public FeedCommentsAdapter getAdapter() {
		return adapter;
	}


	public void setAdapter(FeedCommentsAdapter adapter) {
		this.adapter = adapter;
	}


	public Integer getFeedSeq() {
		return feedSeq;
	}


	public void setFeedSeq(Integer feedSeq) {
		this.feedSeq = feedSeq;
	}


}
