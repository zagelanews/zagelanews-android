package org.zagelnews.adapters;

import java.util.ArrayList;

import org.zagelnews.dtos.feeds.FeedTypeDto;
import org.zagelnews.utils.ClientConfiguration;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeedTypesSpinnerAdapter extends ArrayAdapter<FeedTypeDto> {
	private final Activity context;
	private final ArrayList<FeedTypeDto> names;

	public FeedTypesSpinnerAdapter(Activity context, int textViewResourceId, ArrayList<FeedTypeDto> names) {
		super(context, textViewResourceId, names);
		this.context = context;
		this.names = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        FeedTypeDto s = names.get(position);
        
        String feedTypeDesc = "";
		if(ClientConfiguration.lang.contains("ar")){
			feedTypeDesc = s.getFeedTypeAdesc();	
		}else{
			feedTypeDesc = s.getFeedTypeEdesc();
		}
		
		
		label.setText(feedTypeDesc);
        label.setTextSize(16);
        label.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return label;
	}
	
	public void addAll(ArrayList<FeedTypeDto> list) {
		names.addAll(list);
	}
	
	 @Override
     public View getDropDownView(int position, View convertView,ViewGroup parent) {
         // TODO Auto-generated method stub

         View view = super.getView(position, convertView, parent);

          TextView text = (TextView)view.findViewById(android.R.id.text1);
          text.setTextColor(Color.BLACK);
          text.setBackgroundColor(Color.WHITE);
          text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

          return view;

     }
}
