package org.zagelnews.activities.feeds;

import java.util.List;

import org.zagelnews.activities.BaseFragmentActivity;
import org.zagelnews.activities.R;
import org.zagelnews.adapters.PlacesAutoCompleteAdapter;
import org.zagelnews.constants.MapConstants;
import org.zagelnews.dtos.PlaceDto;
import org.zagelnews.dtos.feeds.FeedsSummaryDto;
import org.zagelnews.dtos.geo.PointDto;
import org.zagelnews.tasks.feeds.LoadFeedsSummaryTask;
import org.zagelnews.tasks.users.AddNewInterestTask;
import org.zagelnews.utils.AndroidUtils;
import org.zagelnews.utils.DraggableCircle;
import org.zagelnews.utils.MyLocation;
import org.zagelnews.utils.MyLocation.LocationResult;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
public class MapActivity extends BaseFragmentActivity implements OnMarkerDragListener, OnMarkerClickListener{

	private DraggableCircle circle;

	//register the interest
	private Button registerInterest;
	private Button resetMapId;
	private Button showSummaryId;

	//to catch the failed added interest
	private Boolean interestAddedSucc = true;
	private String interestNameStr = "";

	
	MyLocation myLocation = new MyLocation();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_interest);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

		try { 

			//google map configuration 
			if (googleMap == null) {
				initMap();
			}
			/************************************* auto complete configuration *******************************************/
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
				}
			});


			AndroidUtils.disableSpftKeyboard(this);

			resetMapId = (Button) findViewById(R.id.resetMapId);
			resetMapId.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					googleMap.clear();	
					circle = null;
					resetMapId.setEnabled(false);
					//resetMapId.setTextColor(getResources().getColor(R.color.dark_grey));
				}
			});

			showSummaryId = (Button) findViewById(R.id.showSummaryId);
			showSummaryId.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					resetMapId.setEnabled(true);
					LoadFeedsSummaryTask loadFeedsSummaryTask = 
							new LoadFeedsSummaryTask
							(MapActivity.this, 
									googleMap.getCameraPosition().target.latitude, 
									googleMap.getCameraPosition().target.longitude, 
									365);
					loadFeedsSummaryTask.execute("");
				}
			});

			//set the interest registration button action
			registerInterest = (Button) findViewById(R.id.getFeeds);
			registerInterest.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					//check the location is selected
					if(circle==null||circle.getCircle().getCenter()==null||circle.getRadius()==null){
						//go to feeds
						Intent myIntent = new Intent(v.getContext(), FilteredFeedsListActivity.class);

						LatLng mapCente = googleMap.getCameraPosition().target;

						if(mapCente!=null){
							myIntent.putExtra("currentLatitude",mapCente.latitude);
							myIntent.putExtra("currentLongitude",mapCente.longitude);
						}



						startActivity(myIntent);
						googleMap.clear();
						circle = null;
						return;
					}

					//diaglog to get favorite name
					AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this);

					alert.setTitle(getResources().getString(R.string.Add_to_favorites));
					alert.setMessage(getResources().getString(R.string.Add_to_favorites_msg));

					// Set an EditText view to get user input 
					final EditText input = new EditText(MapActivity.this);
					input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
					alert.setView(input);

					alert.setPositiveButton(getResources().getString(R.string.Add_to_favorites), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							interestNameStr = input.getText().toString();

							//check the location is selected
							if(circle==null||circle.getCircle().getCenter()==null||circle.getRadius()==null){
								Toast.makeText(MapActivity.this, getResources().getString(R.string.missingInterestLocation), Toast.LENGTH_LONG).show();
								return;
							}

							//continue to the news page
							LatLng center = circle.getCircle().getCenter();
							Double radious = circle.getRadius();

							//save the interest to the favorites
							if(interestNameStr==null||interestNameStr.trim().length()==0){
								interestNameStr = center.toString();
							}
							new AddNewInterestTask(MapActivity.this, center, radious, interestNameStr).execute();

							if(interestAddedSucc){

								//go to feeds
								Intent myIntent = new Intent(v.getContext(), FilteredFeedsListActivity.class);
								//interest related info
								myIntent.putExtra("circleLatitude",center.latitude);
								myIntent.putExtra("circleLongitude",center.longitude);
								myIntent.putExtra("circleRadious",radious);
								startActivityForResult(myIntent, 0);
							}



						}
					});

					alert.setNegativeButton(getResources().getString(R.string.Contiue_To_News), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							//check the location is selected
							if(circle==null||circle.getCircle().getCenter()==null||circle.getRadius()==null){
								Toast.makeText(MapActivity.this, getResources().getString(R.string.missingInterestLocation), Toast.LENGTH_LONG).show();
								return;
							}

							//continue to the news page
							LatLng center = circle.getCircle().getCenter();
							Double radious = circle.getRadius();

							//go to feeds
							Intent myIntent = new Intent(v.getContext(), FilteredFeedsListActivity.class);
							//interest related info
							myIntent.putExtra("circleLatitude",center.latitude);
							myIntent.putExtra("circleLongitude",center.longitude);
							myIntent.putExtra("circleRadious",radious);
							startActivityForResult(myIntent, 0);
						}
					});

					alert.show();



				}
			});


		} catch (Exception e) {
			Toast.makeText(MapActivity.this, getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}

		///////////////Location service
		LocationResult locationResult = new LocationResult(){
		    @Override
		    public void gotLocation(Location location){
		    	if(location!=null){
		    	markerPoint = new LatLng(location.getLatitude(), location.getLongitude());
				googleMap.moveCamera(CameraUpdateFactory.
						newLatLngZoom(markerPoint, MapConstants.GOOGLE_MAPS_ZOOM));
		    	}
		    }
		};
		myLocation.getLocation(this, locationResult);


		googleMap.setOnMarkerDragListener(this);
		
		googleMap.setOnMarkerClickListener(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		onMarkerMoved(marker);
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		onMarkerMoved(marker);
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		onMarkerMoved(marker);
	}

	private void onMarkerMoved(Marker marker) {
		if (circle!=null) {
			circle.onMarkerMoved(marker);
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		if(point!=null){
			googleMap.clear();
			//PlaceDto placeDto = adapter.getItem(arg2);
			LatLng map_center = new LatLng(point.latitude,point.longitude);
			//the default circle
			//float zoom= googleMap.getCameraPosition().zoom;
			//double radiusD = Double.valueOf(ServerConstants.DEFAULT_RADIUS);
			VisibleRegion vr = googleMap.getProjection().getVisibleRegion();
			double left = vr.latLngBounds.southwest.longitude;

			Location MiddleLeftCornerLocation = new Location("");
			MiddleLeftCornerLocation.setLatitude(map_center.latitude);
			MiddleLeftCornerLocation.setLongitude(left);
			Location center=new Location("center");
			center.setLatitude(map_center.latitude);
			center.setLongitude(map_center.longitude);
			double dis = center.distanceTo(MiddleLeftCornerLocation);
			dis = dis*0.6;

			circle = new DraggableCircle(googleMap, map_center, dis, true);
			resetMapId.setEnabled(true);
			//resetMapId.setTextColor(getResources().getColor(R.color.black));
		}
	}


	/*
	 * Called when the Activity is no longer visible at all.
	 * Stop updates and disconnect.
	 */
	@Override
	public void onStop() {
		super.onStop();
	}
	/*
	 * Called when the Activity is going into the background.
	 * Parts of the UI may be visible, but the Activity is inactive.
	 */
	@Override
	public void onPause() {
		myLocation.cancelTimer();
		super.onPause();
	}

	/*
	 * Called when the Activity is restarted, even before it becomes visible.
	 */
	@Override
	public void onStart() {
		super.onStart();
		checkGps();

	}

	private void checkGps() {
		LocationManager lm = (LocationManager)MapActivity.this.getSystemService(Context.LOCATION_SERVICE);
		boolean gps_enabled = false;
		boolean network_enabled = false;

		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch(Exception ex) {}

		try {
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch(Exception ex) {}

		if(!gps_enabled && !network_enabled) {
			// notify user
			AlertDialog.Builder dialog = new AlertDialog.Builder(MapActivity.this);
			dialog.setMessage(MapActivity.this.getResources().getString(R.string.errorGpsDisabled));
			dialog.setPositiveButton(MapActivity.this.getResources().getString(R.string.errorGpsDisabled), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					// TODO Auto-generated method stub
					Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					MapActivity.this.startActivity(myIntent);
					//get gps
				}
			});
			dialog.setNegativeButton(MapActivity.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					// TODO Auto-generated method stub

				}
			});
			dialog.show();      
		}
	}
	/*
	 * Called when the system detects that this Activity is now visible.
	 */
	@Override
	public void onResume() {
		super.onResume();
		checkGps();


	}

	public Boolean getInterestAddedSucc() {
		return interestAddedSucc;
	}

	public void setInterestAddedSucc(Boolean interestAddedSucc) {
		this.interestAddedSucc = interestAddedSucc;
	}

	@Override
	public void getTaskResult(Object zagelnewsDto, Integer taskId) {
		@SuppressWarnings("unchecked")
		List<FeedsSummaryDto> summariesList = (List<FeedsSummaryDto>)zagelnewsDto;
		if(summariesList!=null&&summariesList.size()>0){
			for(FeedsSummaryDto dto : summariesList){
				PointDto pointDto = dto.getFeedLocation();
				if(pointDto!=null){
					MarkerOptions markerOptions = new MarkerOptions()
					.position(new LatLng(pointDto.getLatitude(), pointDto.getLongitude()))
					.draggable(false)
					.icon(dto.getAuthorSampleImageBitmap());
					googleMap.addMarker(markerOptions);
				}
			}
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		arg0.getId();
		return false;
	}
}
