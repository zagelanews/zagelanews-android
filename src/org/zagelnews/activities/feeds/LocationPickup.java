/*package org.zagelnews.activities.feeds;

import org.zagelnews.activities.BaseFragmentActivity;
import org.zagelnews.activities.R;
import org.zagelnews.adapters.PlacesAutoCompleteAdapter;
import org.zagelnews.constants.MapConstants;
import org.zagelnews.dtos.PlaceDto;
import org.zagelnews.utils.AndroidUtils;
import org.zagelnews.utils.LocationUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class LocationPickup extends BaseFragmentActivity {

	private Button registerFeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.location_pickup);
		try { 

			//google map configuration 
			if (googleMap == null) {
				initMap();
			}
			*//************************************* auto complete configuration *******************************************//*
			final PlacesAutoCompleteAdapter adapter = new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
			AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.geocode_input);
			autoCompView.setAdapter(adapter);

			//handle the selection of the place 
			autoCompView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					PlaceDto placeDto = adapter.getItem(arg2);
					LatLng map_center = new LatLng(placeDto.getLat(),placeDto.getLon());
					googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(map_center, MapConstants.GOOGLE_MAPS_ZOOM));

					googleMap.clear();
					markerPoint = new LatLng(map_center.latitude,map_center.longitude);
					googleMap.addMarker(new MarkerOptions()
					.position(markerPoint)
					.draggable(false));
				
				}
			});


			AndroidUtils.disableSpftKeyboard(this);

			//set the interest registration button action
			registerFeed = (Button) findViewById(R.id.registerFeedId);
			registerFeed.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					//check the location is selected
					if(markerPoint!=null){
						double latitude = markerPoint.latitude;
						double longitude = markerPoint.longitude;
						//new AsyncNewFeed(latitude, longitude).execute();
						Intent returnIntent = new Intent();
						returnIntent.putExtra("latitude",latitude);
						returnIntent.putExtra("longitude",longitude);
						setResult(RESULT_OK,returnIntent);
						finish();
					}else{
						Toast.makeText(LocationPickup.this, getResources().getString(R.string.locationEmpty), Toast.LENGTH_LONG).show();	
					}
				}
			});



			/////////////maps

			// Create a new global location parameters object
			LocationRequest mLocationRequest = LocationRequest.create();

			
			 * Set the update interval
			 
			mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

			// Use high accuracy
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

			// Set the interval ceiling to one minute
			mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

			
		} catch (Exception e) {
			Toast.makeText(LocationPickup.this, getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	 * Called when the Activity is no longer visible at all.
	 * Stop updates and disconnect.
	 
	@Override
	public void onStop() {
		super.onStop();
	}
	
	 * Called when the Activity is going into the background.
	 * Parts of the UI may be visible, but the Activity is inactive.
	 
	@Override
	public void onPause() {
		super.onPause();
	}

	
	 * Called when the Activity is restarted, even before it becomes visible.
	 
	@Override
	public void onStart() {
		super.onStart();
	}
	
	 * Called when the system detects that this Activity is now visible.
	 
	@Override
	public void onResume() {
		super.onResume();
	}

	
	 * Handle results returned to this Activity by other Activities started with
	 * startActivityForResult(). In particular, the method onConnectionFailed() in
	 * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to
	 * start an Activity that handles Google Play services problems. The result of this
	 * call returns here, to onActivityResult.
	 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		// Choose what to do based on the request code
		switch (requestCode) {

		// If the request code matches the code sent in onConnectionFailed
		case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

			switch (resultCode) {
			// If Google Play services resolved the problem
			case Activity.RESULT_OK:
				break;
			default:
				break;
			}

			// If any other request code was received
		default:
			break;
		}
	}
	
	
	@Override
	public void onMapClick(LatLng point) {
		setEditMode(true);
		super.onMapClick(point);
	}
	
	@Override
	public void onConnected(Bundle bundle) {
		LatLng currLocation = getCurrLocation();
		if(currLocation!=null){
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, MapConstants.GOOGLE_MAPS_ZOOM));
		}else{
			LocationUtils.showSettingsAlert(this);
		}
	}
	
	@Override
	public void getTaskResult(Object zagelnewsDto, Integer taskId) {
	}
}
*/