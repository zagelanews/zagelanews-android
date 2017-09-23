package org.zagelnews.activities;

import org.zagelnews.activities.feeds.NewFeedActivity;
import org.zagelnews.activities.users.UserNotificationsListActivity;
import org.zagelnews.adapters.MySupportMapFragment;
import org.zagelnews.constants.MapConstants;
import org.zagelnews.tasks.users.ProcessGetNotificationsTask;
import org.zagelnews.utils.AndroidUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public abstract class BaseFragmentActivity 
extends FragmentActivity 
implements 
OnMapClickListener{


	public static String KEY_ERROR_CODE = "errorCode";
	public static String KEY_ERROR_MESSAGE = "errorMessage";

	//map related fields
	protected GoogleMap googleMap;
	protected LatLng markerPoint;

	protected Menu menu;

	protected boolean editMode;
	protected float mapZoom = MapConstants.GOOGLE_MAPS_ZOOM;

	

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


	@Override
	protected void onResume() {
		super.onResume();
		initMap();
	}

	/**
	 * initialize the map
	 */
	protected void initMap() {
		setUpMapIfNeeded();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**************************Map elated Methods**************************************/


	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (googleMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			MySupportMapFragment custogoogleMapFragment = 
					((MySupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

			if(custogoogleMapFragment!=null){
				googleMap = custogoogleMapFragment.getMap();


				// Check if we were successful in obtaining the map.
				if (googleMap != null) {
					googleMap.setMyLocationEnabled(true);

					// Enable or disable current location
					googleMap.setMyLocationEnabled(true);

					// Initialize type of map
					googleMap.setMapType(MapConstants.MAP_TYPE);

					// Initialize 3D buildings enabled for map view
					//googleMap.setBuildingsEnabled(false);

					// Initialize whether indoor maps are shown if available
					googleMap.setIndoorEnabled(false);

					// Initialize traffic overlay
					googleMap.setTrafficEnabled(false);

					// Enable rotation gestures
					googleMap.getUiSettings().setRotateGesturesEnabled(true);

					googleMap.setOnMapClickListener(this);
				}
			}
		}
	}




	@Override
	public void onMapClick(LatLng point) {
		if(point!=null){
			if(editMode){
				googleMap.clear();
				markerPoint = new LatLng(point.latitude,point.longitude);
				googleMap.addMarker(new MarkerOptions()
				.position(markerPoint)
				.draggable(false));
			}else{
				String uri = "geo:"+ markerPoint.latitude + "," + markerPoint.longitude+"?q="+markerPoint.latitude + "," + markerPoint.longitude;
				startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
			}
		}
	}


	/*************************************************fields accesses*********************************/

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public abstract void getTaskResult(Object zagelnewsDto, Integer taskId);	
}