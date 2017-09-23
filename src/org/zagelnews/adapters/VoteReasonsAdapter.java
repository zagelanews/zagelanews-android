package org.zagelnews.adapters;

import java.util.ArrayList;

import org.zagelnews.dtos.feeds.VoteReasonDto;
import org.zagelnews.utils.ClientConfiguration;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VoteReasonsAdapter extends ArrayAdapter<VoteReasonDto> {
	private final Activity context;
	private final ArrayList<VoteReasonDto> names;

	public VoteReasonsAdapter(Activity context, int textViewResourceId, ArrayList<VoteReasonDto> names) {
		super(context, textViewResourceId, names);
		this.context = context;
		this.names = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        VoteReasonDto s = names.get(position);
        if(ClientConfiguration.lang.toLowerCase().contains("ar")){
        	label.setText(s.getReasonAdesc());	
        }else{
        	label.setText(s.getReasonEdesc());	
        }
        
        label.setTextColor(Color.WHITE);
        label.setTextSize(18);
        label.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return label;
	}
	
	public void addAll(ArrayList<VoteReasonDto> list) {
		names.addAll(list);
	}
}
