package org.zagelnews.adapters;

import java.util.ArrayList;

import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.utils.GeneralUtils;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import org.zagelnews.activities.R;

public class BaseAdapter extends ArrayAdapter<ZagelnewsDto>{
	
	protected Context context;
	protected ArrayList<ZagelnewsDto> names;

	public BaseAdapter(Activity context, int resourceId, ArrayList<ZagelnewsDto> zagelnewsDtos) {
		super(context, resourceId, zagelnewsDtos);
	}

	protected String getTruncatedText(String str, int limit){
		if(GeneralUtils.isStringEmpty(str)){
			return "";
		}
		if(str.length()<=limit){
			return str;
		}
		StringBuffer stringBuffer = new StringBuffer();

		String truncatedString = str.substring(0, limit);
		stringBuffer.append(truncatedString).append("...").append(getContext().getResources().getString(R.string.moreText));

		return stringBuffer.toString();
	}
}
