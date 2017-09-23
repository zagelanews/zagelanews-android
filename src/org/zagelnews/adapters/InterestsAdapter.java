package org.zagelnews.adapters;

import java.util.ArrayList;

import org.zagelnews.dtos.users.UserInterestDto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.zagelnews.activities.R;

public class InterestsAdapter extends ArrayAdapter<UserInterestDto> {
	private final Activity context;
	private final ArrayList<UserInterestDto> names;

	static class ViewHolder {
		public TextView text;	
		CheckBox cb;
		LinearLayout card;
	}

	public InterestsAdapter(Activity context, ArrayList<UserInterestDto> names) {
		super(context, R.layout.interest_activities_raws, names);
		this.context = context;
		this.names = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		// reuse views
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.interest_activities_raws, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.label);
			viewHolder.cb = (CheckBox) rowView.findViewById(R.id.check);
			//set listener
			viewHolder.cb.setOnClickListener( new View.OnClickListener() { 
				public void onClick(View v) { 
					CheckBox cb = (CheckBox) v ; 
					UserInterestDto ui = (UserInterestDto) cb.getTag(); 
					ui.setSelected(cb.isChecked());
				} 
			}); 
			
			viewHolder.card = (LinearLayout) rowView.findViewById(R.id.card);

			rowView.setBackgroundResource(R.drawable.listview_selector_odd);

			rowView.setTag(viewHolder);
		}

		// fill data
		ViewHolder holder = (ViewHolder) rowView.getTag();
		UserInterestDto s = names.get(position);
		holder.text.setText(s.getInterestName());
		holder.cb.setTag(s);
		holder.cb.setChecked(s.isSelected());
		return rowView;
	}
	
	public void addAll(ArrayList<UserInterestDto> list) {
		names.addAll(list);
	}
}
