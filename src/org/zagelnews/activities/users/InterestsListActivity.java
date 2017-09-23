package org.zagelnews.activities.users;

import java.util.ArrayList;

import org.zagelnews.activities.BaseActivity;
//import android.view.ActionMode;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.FilteredFeedsListActivity;
import org.zagelnews.dtos.users.UserInterestDto;
import org.zagelnews.tasks.users.DeleteUserInterestTask;
import org.zagelnews.tasks.users.LoadUserInterestsTask;
import org.zagelnews.utils.GeneralUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
/**
 * Load the user areas of interests
 * @author oomran
 *
 */
public class InterestsListActivity extends BaseActivity {	

	public static class ViewHolder {
		public ListView v;
		public Button delInterest;
	}public ViewHolder holder;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.interest_activities);

		holder = new ViewHolder();
		holder.v = (ListView) findViewById(R.id.interestsList);//getListView();

		//to handle the view of the interest details
		holder.v.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//view the interest details
				UserInterestDto userInterest = ((UserInterestDto)parent.getItemAtPosition(position));
				Intent i = new Intent(getApplicationContext(), InterestDetailsActivity.class);
				//interest related info
				i.putExtra("latitude",userInterest.getLatitude());
				i.putExtra("longitude",userInterest.getLongitude());
				i.putExtra("radious",userInterest.getRadius());
				i.putExtra("interestName",userInterest.getInterestName());
				startActivity(i);				
			}


		});

		//go to feeds activity
		Button regInterest = (Button) findViewById(R.id.regInterest);
		regInterest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent feedIntent = new Intent(getApplicationContext(), FilteredFeedsListActivity.class);
				ArrayList<String> selectedItems = new ArrayList<String>();
				for (int i = 0; i < holder.v.getCount(); i++) {
					// Add sport if it is checked i.e.) == TRUE!
					UserInterestDto ui = (UserInterestDto)holder.v.getItemAtPosition(i);
					if (ui.isSelected()){
						selectedItems.add(""+ui.getInterestKey());
					}
				}
				// Create a bundle object
				Bundle b = new Bundle();
				String[] outputStrArr = new String[selectedItems.size()];
				outputStrArr = selectedItems.toArray(outputStrArr);

				if(outputStrArr.length==0){
					Toast.makeText(InterestsListActivity.this, getResources().getString(R.string.noFavoritesSelected), Toast.LENGTH_LONG).show();
					return;
				}
				b.putStringArray("interestIds", outputStrArr);

				// Add the bundle to the intent.
				feedIntent.putExtras(b);
				startActivity(feedIntent);

			}
		});

		//go to delete activity
		holder.delInterest = (Button) findViewById(R.id.delInterest);
		holder.delInterest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder adb=new AlertDialog.Builder(InterestsListActivity.this);
				adb.setTitle(getResources().getString(R.string.delete));
				adb.setMessage(getResources().getString(R.string.sure_delete));
				adb.setNegativeButton(getResources().getString(R.string.cancel), null);
				adb.setPositiveButton(getResources().getString(R.string.ok), new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						ArrayList<String> selectedItems = new ArrayList<String>();
						for (int i = 0; i < holder.v.getCount(); i++) {
							// Add sport if it is checked i.e.) == TRUE!
							UserInterestDto ui = (UserInterestDto)holder.v.getItemAtPosition(i);
							if (ui.isSelected()){
								selectedItems.add(""+ui.getInterestKey());
							}
						}
						String[] outputStrArr = new String[selectedItems.size()];
						outputStrArr = selectedItems.toArray(outputStrArr);
						if(outputStrArr.length==0){
							Toast.makeText(InterestsListActivity.this, getResources().getString(R.string.noFavoritesSelected), Toast.LENGTH_LONG).show();
							return;
						}
						String interestIds = GeneralUtils.arrToCommaSeperated(outputStrArr);
						new DeleteUserInterestTask(InterestsListActivity.this, interestIds).execute(interestIds);
					}});
				adb.show();
			}
		});

		new LoadUserInterestsTask(this).execute();
	}

	@Override
	public void getTaskResult(Object result, Integer taskId) {
	}
}