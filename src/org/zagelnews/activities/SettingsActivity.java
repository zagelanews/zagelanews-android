package org.zagelnews.activities;

import org.json.JSONObject;
import org.zagelnews.activities.feeds.ProfileFeedsListActivity;
import org.zagelnews.activities.users.ChangePasswordActivity;
import org.zagelnews.activities.users.InterestsListActivity;
import org.zagelnews.activities.users.RegisterActivity;
import org.zagelnews.constants.FeedsConstants;
import org.zagelnews.db.DatabaseHandler;
import org.zagelnews.delegates.UserFunctions;
import org.zagelnews.utils.ClientConfiguration;
import org.zagelnews.utils.GeneralUtils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
public class SettingsActivity extends BaseActivity {

	static class ViewHolder {
		TextView myNews;
		ImageView myNewsImg;
		
		
		TextView favorites;
		ImageView favoritesImg;

		TextView updateProfile;
		ImageView updateProfileImg;
		
		TextView changePassword;
		ImageView changePasswordImg;

		TextView inviteFriends;
		ImageView inviteFriendsImg;
		
		
		TextView logout;
		ImageView logoutImg;
	}
	ViewHolder holder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		holder = new ViewHolder();

	/*	holder.inviteFriends= (TextView) findViewById(R.id.inviteFriendsId);
		holder.inviteFriends.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						if (AppInviteDialog.canShow()) {
							AppInviteContent content = new AppInviteContent.Builder()
							.setApplinkUrl("https://fb.me/1484516781615501")
							.setPreviewImageUrl("https://lh3.googleusercontent.com/3t9Rbh9wB24OBnpdMzRFwgHxsKcAfPqdn5LtOJRN5IQ20IzDh4WxKUQ7xxRLSajqonw=w300-rw")
							.build();
							AppInviteDialog.show(SettingsActivity.this, content);
						}
					}
				}
				);*/

		//my Lists
		holder.favorites= (TextView) findViewById(R.id.favoritesId);
		holder.favorites.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent myIntent = new Intent(view.getContext(), 
								InterestsListActivity.class);
						startActivityForResult(myIntent, 0);
					}
				}
				);



		//my News
		holder.myNews= (TextView) findViewById(R.id.myNewsId);
		holder.myNews.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent i = new Intent(getApplicationContext(), ProfileFeedsListActivity.class);
						Integer userIdL = null;
						String userId = ClientConfiguration.getUser(SettingsActivity.this).get(DatabaseHandler.KEY_ID);
						if(!GeneralUtils.isStringEmpty(userId)){
							userIdL = Integer.valueOf(userId);
						}
						i.putExtra("profileOwnerId", userIdL);
						i.putExtra("feedAuthorType", FeedsConstants.FeedAuthorTypeConst.USER);
						String userFullName = ClientConfiguration.getUser(SettingsActivity.this).get(DatabaseHandler.KEY_FULLNAME);
						i.putExtra("profileOwnerFullName", userFullName);
						startActivity(i);
					}
				}
				);

		//my News
		holder.myNewsImg= (ImageView) findViewById(R.id.myNewsIdImg);
		holder.myNewsImg.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent i = new Intent(getApplicationContext(), ProfileFeedsListActivity.class);
						Integer userIdL = null;
						String userId = ClientConfiguration.getUser(SettingsActivity.this).get(DatabaseHandler.KEY_ID);
						if(!GeneralUtils.isStringEmpty(userId)){
							userIdL = Integer.valueOf(userId);
						}
						i.putExtra("profileOwnerId", userIdL);
						i.putExtra("feedAuthorType", FeedsConstants.FeedAuthorTypeConst.USER);
						String userFullName = ClientConfiguration.getUser(SettingsActivity.this).get(DatabaseHandler.KEY_FULLNAME);
						i.putExtra("profileOwnerFullName", userFullName);
						startActivity(i);
					}
				}
				);

		//updateProfile
		holder.updateProfile= (TextView) findViewById(R.id.updateProfileId);
		holder.updateProfile.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
						i.putExtra("fromMenue", true);
						startActivity(i);
					}
				}
				);

		//changePassword
		holder.changePassword= (TextView) findViewById(R.id.changePasswordId);
		holder.changePassword.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
						startActivity(i);
					}
				}
				);

		//invite friends
		holder.inviteFriends= (TextView) findViewById(R.id.inviteFriendsId);
		holder.inviteFriends.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try { 
						    Intent i = new Intent(Intent.ACTION_SEND);  
						    i.setType("text/plain");
						    String appName = 
						    		SettingsActivity.
						    		this.
						    		getResources().
						    		getString(R.string.app_name);
						    i.putExtra(Intent.EXTRA_SUBJECT, appName);
						    String sAux = SettingsActivity.
						    		this.
						    		getResources().
						    		getString(R.string.inviteFriendsMsg);
						    sAux = sAux + "https://play.google.com/store/apps/details?id=org.zagelnews.activities \n\n";
						    i.putExtra(Intent.EXTRA_TEXT, sAux);  
						    startActivity(Intent.createChooser(i, "..."));
						} catch(Exception e) { 
						    //e.toString();
						}   

					}
				}
				);
		//logout
		holder.logout= (TextView) findViewById(R.id.logoutId);
		holder.logout.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String sessionId = ClientConfiguration.getUser().get(DatabaseHandler.KEY_SESSION_ID);
						if(sessionId!=null && sessionId.trim().length()>0){
							ProcessLogout processLogout = new ProcessLogout(sessionId);
							processLogout.execute();
						}
					}
				}
				);
	}


	@Override
	protected void onResume() {
		super.onResume();
	}



	@Override
	protected void onStart() {
		super.onStart();
	}



	@Override
	protected void onStop() {
		super.onStop();
	}



	private class ProcessLogout extends AsyncTask<String, String, JSONObject> {

		String sessionId;

		public ProcessLogout(String sessionId) {
			this.sessionId = sessionId;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions(SettingsActivity.this);
			JSONObject json = userFunction.logout(sessionId);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			redirectToLogin();
		}
	}



	@Override
	public void getTaskResult(Object zagelnewsDto, Integer taskId) {
		// TODO Auto-generated method stub

	}
}
