package org.zagelnews.activities.users;

import org.zagelnews.activities.BaseFragmentActivity;
import org.zagelnews.activities.R;
import org.zagelnews.constants.MapConstants;
import org.zagelnews.utils.AndroidUtils;
import org.zagelnews.utils.DraggableCircle;
import org.zagelnews.utils.MapUtils;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
public class InterestDetailsActivity extends BaseFragmentActivity {

	private TextView interestName;
	private DraggableCircle circle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interest_details);
		try { 

			//google map configuration 
			if (googleMap == null) {
				initMap();
			}

			AndroidUtils.disableSpftKeyboard(this);

			/************************************ to handle view or update************************************/
			Intent myIntent = getIntent(); // gets the previously created intent
			String latitude = ""+myIntent.getDoubleExtra("latitude",-1); 
			String longitude= ""+myIntent.getDoubleExtra("longitude",-1);
			String radious= ""+myIntent.getDoubleExtra("radious",-1);
			String interestName= myIntent.getStringExtra("interestName");


			//draw the related interest circle if there is a one
			if(latitude!=null&&latitude.trim().length()>0&&
					longitude!=null&&longitude.trim().length()>0&&
					radious!=null&&radious.trim().length()>0&&
					interestName!=null&&interestName.trim().length()>0){
				Double radiusD = Double.valueOf(radious);
				circle = new DraggableCircle(googleMap, new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)), radiusD, false);

				Float radiusF = Float.valueOf(radious);

				final LatLngBounds bounds = MapUtils.boundsWithCenterAndLatLngDistance(circle.getCircle().getCenter(), radiusF*2, radiusF*2);

				googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
					@Override
					public void onMapLoaded() {
						googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
					}
				});


				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(circle.getCircle().getCenter(), MapConstants.GOOGLE_MAPS_ZOOM));
				if(this.interestName==null){
					this.interestName = (TextView) findViewById(R.id.intName);
				}
				this.interestName.setText(interestName); 
			}

		} catch (Exception e) {
			Toast.makeText(InterestDetailsActivity.this, getResources().getString(R.string.genError), Toast.LENGTH_LONG).show();
		}
	}

	/*	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}	
	    return super.onOptionsItemSelected(item);
	}*/

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void getTaskResult(Object zagelnewsDto, Integer taskId) {

	}

}
