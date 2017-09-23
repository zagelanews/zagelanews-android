package org.zagelnews.adapters;

import java.util.ArrayList;

import org.zagelnews.activities.R;
import org.zagelnews.constants.FeedsConstants;
import org.zagelnews.constants.FeedsConstants.NotificationTypesConst;
import org.zagelnews.dtos.feeds.UserNotificationDto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserFeedsNotificationsAdapter extends ArrayAdapter<UserNotificationDto> {
	private final Activity context;
	private final ArrayList<UserNotificationDto> names;

	static class ViewHolder {
		public TextView activity;
		public TextView newsText;	
		public TextView newsDate;
		public ImageView activityIcon;	
		LinearLayout card;
	}

	public UserFeedsNotificationsAdapter(Activity context, ArrayList<UserNotificationDto> names) {
		super(context, R.layout.notifications_activities, names);
		this.context = context;
		this.names = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		// reuse views
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.notifications_activities_raws, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.activity = (TextView) rowView.findViewById(R.id.activity);
			viewHolder.newsText = (TextView) rowView.findViewById(R.id.news_text);
			viewHolder.newsDate = (TextView) rowView.findViewById(R.id.news_date);
			viewHolder.activityIcon = (ImageView) rowView.findViewById(R.id.activityIcon);
			//set listener
			viewHolder.card = (LinearLayout) rowView.findViewById(R.id.card);

			rowView.setBackgroundResource(R.drawable.listview_selector_odd);

			rowView.setTag(viewHolder);
		}

		// fill data
		ViewHolder holder = (ViewHolder) rowView.getTag();
		UserNotificationDto s = names.get(position);
		if(s.getObjectDescription()!=null&&s.getObjectDescription().length()>100){
			holder.newsText.setText(s.getObjectDescription().substring(0, 100)+"...");
		}else{
			holder.newsText.setText(s.getObjectDescription());
		}
		holder.newsDate.setText(s.getNotificationDate());
		StringBuffer activityText = new StringBuffer();
		activityText.append(context.getResources().getString(R.string.YouHaveReceived));
		activityText.append(" ").append(s.getNotificationCount()).append(" ");
		if(FeedsConstants.NotificationClassesConst.FEED_COMMENTS.equals(s.getNotificationClass())){
			activityText.append(context.getResources().getString(R.string.Comments));
			holder.activityIcon.setImageDrawable
			(context.getResources().getDrawable(R.drawable.comment));
		}else if(FeedsConstants.NotificationClassesConst.FEED_POSITIVE_VOTE.equals(s.getNotificationClass())){
			activityText.append(context.getResources().getString(R.string.positive_vote));
			holder.activityIcon.setImageDrawable
			(context.getResources().getDrawable(R.drawable.ok));
		}else if(FeedsConstants.NotificationClassesConst.FEED_NEGATIVE_VOTE.equals(s.getNotificationClass())){
			activityText.append(context.getResources().getString(R.string.negative_vote));
			holder.activityIcon.setImageDrawable
			(context.getResources().getDrawable(R.drawable.no));
		}
		
		activityText.append(" ").append(context.getResources().getString(R.string.OnYourNews));
		holder.activity.setText(activityText.toString());
		
		if(Integer.valueOf(1).equals(s.getNotificationStatus())){
			rowView.setBackgroundResource(R.drawable.listview_selector_odd);
		}else{
			rowView.setBackgroundResource(R.drawable.listview_selector_even);
		}
		return rowView;
	}
	
	public void addAll(ArrayList<UserNotificationDto> list) {
		ArrayList<UserNotificationDto> filteredNotificationsList = new ArrayList<UserNotificationDto>();
		if(list!=null&&list.size()>0){
			for(UserNotificationDto notification: list){
				if(NotificationTypesConst.FEEDS.equals(notification.getNotificationType())){
					filteredNotificationsList.add(notification);
				}
			}
		}
		names.addAll(filteredNotificationsList);
	}
}
