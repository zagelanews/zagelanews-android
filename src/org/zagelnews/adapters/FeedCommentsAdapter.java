package org.zagelnews.adapters;

import java.util.ArrayList;

import org.zagelnews.activities.feeds.FeedCommentsActivity;
import org.zagelnews.constants.ImagesConstants;
import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.dtos.feeds.FeedCommentDto;
import org.zagelnews.ui.images.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.zagelnews.activities.R;

public class FeedCommentsAdapter extends BaseAdapter {
	private boolean fromCache = true;


	public ImageLoader imageLoader; 

	static class ViewHolder {
		public TextView text;
		public TextView commentDate;
		public TextView commentUserFullName;
		public Bitmap bitmap;
		public ImageView image;
	}

	public FeedCommentsAdapter(Activity context, ArrayList<ZagelnewsDto> feedComments) {
		super(context, R.layout.activity_feed_comments_raw, feedComments);
		this.context = (FeedCommentsActivity)context;
		this.names = feedComments;
		imageLoader = new ImageLoader(context.getApplicationContext(), ImageLoader.stub_id_defuser);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {   
		View rowView = convertView;
		final ViewHolder viewHolder;
		// reuse views
		if (rowView == null) {
			//LayoutInflater inflater = context.getLayoutInflater();
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.activity_feed_comments_raw, null);
			// configure view holder
			viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.comment);
			// Recognize all of the default link text patterns 
			Linkify.addLinks(viewHolder.text, Linkify.ALL);

			
			viewHolder.commentDate = (TextView) rowView.findViewById(R.id.commentDate);
			viewHolder.commentUserFullName = (TextView) rowView.findViewById(R.id.commentUserFullName);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.userImage);
			rowView.setTag(viewHolder);
		}else{
			// fill data
			viewHolder = (ViewHolder) rowView.getTag();
		}
		FeedCommentDto s = (FeedCommentDto)names.get(position);
		viewHolder.text.setText(s.getCommentText());
		viewHolder.commentUserFullName.setText(s.getUserFullNameWhoComment());
		viewHolder.commentDate.setText(s.getCommentDate());
		if(s.getFullImageUrlForUserWhoComment()!=null&&s.getSampleImageUrlForUserWhoComment()!=null){
			//DisplayImage function from ImageLoader Class
			viewHolder.image.getLayoutParams().height = ImagesConstants.SAMPLE_IMAGE_SIZE;
			viewHolder.image.getLayoutParams().width = ImagesConstants.SAMPLE_IMAGE_SIZE;
			String imageUrl = s.getSampleImageUrlForUserWhoComment();
			if(imageUrl!=null&&imageUrl.trim().length()>ImagesConstants.MIN_IMAGE_URL_SIZE){
				imageLoader.DisplayImage(imageUrl, viewHolder.image, fromCache);
			}else{
				viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.def_user));
			}
		}

		return rowView;
	}

	public void addAll(ArrayList<FeedCommentDto> list) {
		names.addAll(list);
	}

}
