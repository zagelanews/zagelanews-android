package org.zagelnews.activities.feeds;

import java.util.ArrayList;
import java.util.List;

import org.zagelnews.activities.BaseFragmentActivity;
import org.zagelnews.activities.R;
import org.zagelnews.constants.MapConstants;
import org.zagelnews.dtos.feeds.SampleFullImageDto;
import org.zagelnews.tasks.feeds.PostNewFeedTask;
import org.zagelnews.utils.AndroidUtils;
import org.zagelnews.utils.FetchPath;
import org.zagelnews.utils.ImageUtils;
import org.zagelnews.utils.MyLocation;
import org.zagelnews.utils.MyLocation.LocationResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * post new feed to the server
 * @author oomran
 *
 */
public class NewFeedActivity extends BaseFragmentActivity {

	public static class ViewHolder {
		public Button sendFeed;
		public EditText newFeed;
		public TextView locationDisabled;
		public LinearLayout linlaFooterProgress;
	} public ViewHolder holder;


	//feed images list
	private List<SampleFullImageDto> feedImgsList = new ArrayList<SampleFullImageDto>();


	//for internal use
	private String selectedImg;
	private Boolean editMode = true;

	MyLocation myLocation = new MyLocation();

	//action definition
	protected static final int PICK_IMAGE = 1;
	protected static final int PICK_LOCATION = 2;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_feeds);

		//clear the cached images, so if someone updated his image it will get updated
		AndroidUtils.clearImageCache(this);

		holder = new ViewHolder();
		holder.newFeed= (EditText) findViewById(R.id.newFeed);
		holder.locationDisabled= (TextView) findViewById(R.id.errorMsg);
		holder.sendFeed = (Button) findViewById(R.id.sendFeed);

		setEditMode(true);

		//images view
		holder.linlaFooterProgress = (LinearLayout) findViewById(R.id.feedImgsListLayout);

		/****************group activeGroupsSpinner**********/


		/***********************************Initialize the listeners*******************************************************/
		// get the location
		holder.sendFeed.setEnabled(false);
		holder.locationDisabled.setVisibility(View.VISIBLE);

		holder.sendFeed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(holder.newFeed==null||holder.newFeed.getText().toString().trim().length()==0){
					return;
				}else{
					double latitude=markerPoint.latitude;
					double longitude=markerPoint.longitude;
					new PostNewFeedTask(NewFeedActivity.this,latitude, longitude).execute();
					setPageEnabled(false);
				}

			}
		});


		/////////////////////Map 
		if (googleMap == null) {
			initMap();
			if(googleMap!=null){
				googleMap.getUiSettings().setScrollGesturesEnabled(false);
				googleMap.getUiSettings().setZoomGesturesEnabled(false);
				googleMap.getUiSettings().setZoomControlsEnabled(false);
			}
		}

		///////////////Location service
		LocationResult locationResult = new LocationResult(){
			@Override
			public void gotLocation(Location location){
				if(location!=null){
					markerPoint = new LatLng(location.getLatitude(), location.getLongitude());
					googleMap.moveCamera(CameraUpdateFactory.
							newLatLngZoom(markerPoint, MapConstants.GOOGLE_MAPS_EXTRA_ZOOM));
					googleMap.addMarker(new MarkerOptions()
					.position(markerPoint)
					.draggable(true));
				}

			}
		};
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult);


		AndroidUtils.disableSpftKeyboard(this);
	}


	/**
	 * enable/disable the page to prevent the duplicate adding of the feed 
	 * @param enabled
	 */
	public void setPageEnabled(boolean enabled) {
		holder.newFeed.setEnabled(enabled);
		holder.sendFeed.setEnabled(enabled);
		holder.locationDisabled.setVisibility(enabled?View.GONE:View.VISIBLE);

		editMode = enabled;
	}


	@Override
	protected void onResume() {
		super.onResume();

		checkGps();
	}


	private void checkGps() {
		LocationManager lm = (LocationManager)NewFeedActivity.this.getSystemService(Context.LOCATION_SERVICE);
		boolean gps_enabled = false;
		boolean network_enabled = false;

		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch(Exception ex) {}

		try {
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch(Exception ex) {}

		if(!gps_enabled && !network_enabled) {
			NewFeedActivity.this.finish();

			// notify user
			AlertDialog.Builder dialog = new AlertDialog.Builder(NewFeedActivity.this);
			dialog.setMessage(NewFeedActivity.this.getResources().getString(R.string.errorGpsDisabled));
			dialog.setPositiveButton(NewFeedActivity.this.getResources().getString(R.string.errorGpsDisabled), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					// TODO Auto-generated method stub
					Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					NewFeedActivity.this.startActivity(myIntent);
					//get gps
				}
			});
			dialog.setNegativeButton(NewFeedActivity.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					// TODO Auto-generated method stub
					NewFeedActivity.this.finish();
				}
			});
			dialog.show();      
		}else{
			holder.locationDisabled.setVisibility(View.GONE);
			holder.sendFeed.setEnabled(true);
		}
	}

	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	/*********************************New Feed Menu********************************************/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.browse:
		{
			try {
				/*				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, getResources().getString(R.string.selectPic)),
						PICK_IMAGE);*/
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, PICK_IMAGE);

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.errorSelectPic),
						Toast.LENGTH_LONG).show();
			}
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.news_setting, menu);
		return true;
	}


	/*********************************Image Context Menu********************************************/

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(editMode){
			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.image_menu, menu);
		}
	}

	public boolean onContextItemSelected (MenuItem item){
		if (item.getItemId() == R.id.menuitem2_delete)
		{
			if( selectedImg!=null&&selectedImg.trim().length()>0){
				removeImage();
			}
			selectedImg = null;
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void removeImage() {
		for(int i=0; i<feedImgsList.size();i++){
			SampleFullImageDto imageDto = feedImgsList.get(i);
			if(imageDto!=null&&selectedImg.equalsIgnoreCase(imageDto.getImagePath())){
				imageDto.getSampleImg().setVisibility(View.GONE);
				feedImgsList.remove(i);
			}
		}

	}


	/*****************************Communicate with other activities****************************************************/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				Uri photoUri = data.getData();
				if (photoUri != null) {
					String filePath = FetchPath.getPath(this, photoUri);
					SampleFullImageDto feedImageDto = ImageUtils.decodeFile(this, filePath);
					ImageView img = feedImageDto.getSampleImg();
					final String imageDesc = filePath;
					img.setContentDescription(imageDesc);
					img.setOnLongClickListener(new OnLongClickListener() {
						@Override
						public boolean onLongClick(View arg0) {
							selectedImg = imageDesc;
							return true;
						}
					});
					holder.linlaFooterProgress.addView(img);
					feedImgsList.add(feedImageDto);
					registerForContextMenu(img);
				}

				/*
				Uri selectedImageUri = data.getData();
				String filePath = null;

				try {
					// OI FILE Manager
					String filemanagerstring = selectedImageUri.getPath();

					// MEDIA GALLERY
					String selectedImagePath = getPath(selectedImageUri);

					if (selectedImagePath != null) {
						filePath = selectedImagePath;
					} else if (filemanagerstring != null) {
						filePath = filemanagerstring;
					} else {
						Toast.makeText(getApplicationContext(), "Unknown path",
								Toast.LENGTH_LONG).show();
					}

					if (filePath != null) {
						SampleFullImageDto feedImageDto = ImageUtils.decodeFile(this, filePath);
						ImageView img = feedImageDto.getSampleImg();
						final String imageDesc = filePath;
						img.setContentDescription(imageDesc);
						img.setOnLongClickListener(new OnLongClickListener() {
							@Override
							public boolean onLongClick(View arg0) {
								selectedImg = imageDesc;
								return true;
							}
						});
						holder.linlaFooterProgress.addView(img);
						feedImgsList.add(feedImageDto);
						registerForContextMenu(img);
					} 
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Internal error",
							Toast.LENGTH_LONG).show();
				}
				 */}
			break;
		default:
		}
	}

	//get the profile image path
	protected String getPath(Uri uri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		if(cursor!=null){
			if(cursor.moveToFirst()){;
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
			}
			cursor.close();
		}
		return res;
	}


	/*************************************************fields accesses*********************************/

	public ViewHolder getHolder() {
		return holder;
	}


	public void setHolder(ViewHolder holder) {
		this.holder = holder;
	}

	public List<SampleFullImageDto> getFeedImgsList() {
		return feedImgsList;
	}


	public void setFeedImgsList(List<SampleFullImageDto> feedImgsList) {
		this.feedImgsList = feedImgsList;
	}

	public String getSelectedImg() {
		return selectedImg;
	}


	public void setSelectedImg(String selectedImg) {
		this.selectedImg = selectedImg;
	}


	@Override
	public void getTaskResult(Object result, Integer taskId) {

	}

	@Override
	public void onPause() {
		myLocation.cancelTimer();
		super.onPause();
	}
}