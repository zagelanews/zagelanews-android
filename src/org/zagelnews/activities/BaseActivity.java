package org.zagelnews.activities;

import org.zagelnews.activities.feeds.NewFeedActivity;
import org.zagelnews.activities.users.UserNotificationsListActivity;
import org.zagelnews.tasks.users.ProcessGetNotificationsTask;
import org.zagelnews.utils.AndroidUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class BaseActivity extends Activity {

	public static String KEY_ERROR_CODE = "errorCode";
	public static String KEY_ERROR_MESSAGE = "errorMessage";

	protected Menu menu;
	
	public void redirectToLogin() {
		AndroidUtils.redirectToLogin(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.SettingNewFeed:
		{
			Intent i = new Intent(getApplicationContext(), NewFeedActivity.class);
			startActivity(i);
			return true;
		}
		case R.id.SettingUserFeedsNotifications:
		{
			Intent i = new Intent(getApplicationContext(), UserNotificationsListActivity.class);
			startActivity(i);
			return true;
		}
		case R.id.three_dots_item:
		{
			Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(i);
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		String activityName = this.getClass().getSimpleName() ;
		if(
				activityName.equals("LoginActivity")||
				activityName.equals("NewFeedActivity")||
				activityName.equals("ProfileFeedsListActivity")||
				activityName.equals("MyGroupFeedsListActivity")||
				activityName.equals("SettingsActivity")){
			return false;
		}
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.setting, menu);
		this.menu = menu;
		
		//load notification
		new ProcessGetNotificationsTask(menu, this).execute();
		
		return true;
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		//check if this is the last back press, so confirm the exit of the application
		if (isTaskRoot()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.sureExit))
			.setCancelable(false)
			.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					finish();
				}
			})
			.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		else
		{
			super.onBackPressed();
			finish();
		}
	}
	
	protected boolean editMode;
	
	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
	
	public abstract void getTaskResult(Object zagelnewsDto, Integer taskId);
}
