package org.zagelnews.activities.users;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONObject;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.MapActivity;
import org.zagelnews.constants.Constants;
import org.zagelnews.db.DatabaseHandler;
import org.zagelnews.delegates.UserFunctions;
import org.zagelnews.ui.images.ImageLoader;
import org.zagelnews.utils.EncodingUtils;
import org.zagelnews.utils.OwnerInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private static final int PICK_IMAGE = 1;
	private static String KEY_ERROR_CODE = "errorCode";
	private static String KEY_ERROR_MESSAGE = "errorMessage";

	/**
	 * Defining layout items.
	 **/

	private EditText inputFullName;
	private EditText inputEmail;
	private EditText inputPassword;
	private EditText inputPasswordConfirmed;
	private Button btnRegister;
	private TextView errorMsg;
	
	
	private String imageURL;


	//json fields
	public static String KEY_ID = "userId";
	public static String KEY_FULLNAME = "fullName";
	public static String KEY_EMAIL = "email";
	public static String KEY_TOKEN = "token";
	public static String KEY_SESSION_ID = "sessionId";
	
	public static final String KEY_SAMPLE_PROFILE_IMAGE_URL = "sampleImageUrl";
	public static final String KEY_FULL_PROFILE_IMAGE_URL = "fullImageUrl";

	public static final String KEY_SAMPLE_COVER_IMAGE_URL = "sampleImageUrl";
	public static final String KEY_FULL_COVER_IMAGE_URL = "fullImageUrl";
	
	HashMap<String,String> user;


	//private Bitmap bitmap;
	private ImageView imgView;
	
	public ImageLoader imageLoader; 

	// in case of update user
	private String userId;
	
	protected String zone;
	
	byte [] fullImageRaw;
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		imageLoader = null;
		imgView = null;
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		 checkUserRegistration();
		
		zone = TimeZone.getDefault().getID();
		
		imageLoader = new ImageLoader(this.getApplicationContext(), ImageLoader.stub_id_defuser);

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		final HashMap<String,String> user = db.getUserDetails();
		userId = user.get(DatabaseHandler.KEY_ID);

		/**
		 * Defining all layout items
		 **/
		inputEmail = (EditText) findViewById(R.id.email);
		inputEmail.setNextFocusDownId(R.id.password);
		inputPassword = (EditText) findViewById(R.id.password);
		inputPasswordConfirmed = (EditText) findViewById(R.id.pwordConf);
		inputPasswordConfirmed.setNextFocusDownId(R.id.fname);
		inputFullName = (EditText) findViewById(R.id.fname);
		btnRegister = (Button) findViewById(R.id.register);
		TextView login = (TextView) findViewById(R.id.bktologin);
		
		
		
		imgView = (ImageView) findViewById(R.id.userImage);
		errorMsg = (TextView) findViewById(R.id.register_error);

		errorMsg.setVisibility(View.GONE);
		if(userId!=null&&userId.trim().length()>0){
			//load user information
			inputFullName.setText(user.get(DatabaseHandler.KEY_FULLNAME));
			inputEmail.setText(user.get(DatabaseHandler.KEY_EMAIL));

					//Bitmap bitmap = EncodingUtils.ImageIdToBitMap(user.get(DatabaseHandler.KEY_BUCK_NAME), user.get(DatabaseHandler.KEY_IMAGE_ID));
					//imgView.setImageBitmap(bitmap);
			imageURL = user.get(DatabaseHandler.KEY_FULL_PROFILE_IMAGE_URL);
			imageLoader.DisplayImage(imageURL, imgView, false);
			
			//Thread mythread = new Thread(runnable);
			//mythread.start();
			
			TextView passwordView = (TextView) findViewById(R.id.passwordView);
			TextView passwordConfView = (TextView) findViewById(R.id.passwordConfView);
			
			
			passwordConfView.setVisibility(View.GONE);
			inputPasswordConfirmed.setVisibility(View.GONE);
			inputPassword.setVisibility(View.GONE);
			passwordView.setVisibility(View.GONE);
			inputPassword.setNextFocusDownId(R.id.fname);
			login.setVisibility(View.GONE);

		}else{
			//set default values
			/*OwnerInfo info = new OwnerInfo(this);
			inputFullName.setText(info.name);
			inputEmail.setText(info.email);*/
			inputPassword.setNextFocusDownId(R.id.passwordConfView);
		}

		/**
		 * Button which Switches back to the login screen on clicked
		 **/


		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
				startActivityForResult(myIntent, 0);
				finish();
			}

		});

		/**
		 * Register Button click event.
		 * A Toast is set to alert when the fields are empty.
		 **/

		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (  
						( inputFullName.getText().toString().trim().equals("")) ||
						( inputEmail.getText().toString().trim().equals("")) )
				{
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(getResources().getString(R.string.MandatoryFields));
				}else if((userId==null||userId.trim().length()==0)&&
						( inputPassword.getText().toString().trim().equals("")
								||
								inputPasswordConfirmed.getText().toString().trim().equals(""))){
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(getResources().getString(R.string.MandatoryFields));
				}
				else if(!inputPassword.getText().toString().equals(inputPasswordConfirmed.getText().toString())&&(userId==null||userId.trim().length()==0)){
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(getResources().getString(R.string.passwordMismatch));
				}
				else
				{
					NetAsync(view);
				}
			}
		});

		//profile photo
		imgView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				try {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, getResources().getString(R.string.selectPic)),
							PICK_IMAGE);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.errorSelectPic),
							Toast.LENGTH_LONG).show();
				}}

		});

		ImageView clear = (ImageView) findViewById(R.id.clear);
		clear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				imgView.setImageDrawable(getResources().getDrawable(R.drawable.def_user));
			}
		});
	}


	private void checkUserRegistration() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		user = new HashMap<String, String>();
		user = db.getUserDetails();
		if(user!=null&&user.get(DatabaseHandler.KEY_ID)!=null&&user.get(DatabaseHandler.KEY_ID).trim().length()>0){
			boolean isFromMenue= getIntent().getBooleanExtra("fromMenue", false);
			if(!isFromMenue){
				Intent feeds = new Intent(getApplicationContext(), MapActivity.class);
				feeds.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(feeds);
				finish();
			}
		}else{
			return;//add new user
		}
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
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
						decodeFile(filePath);
					} else {
						imgView.setImageBitmap(null);
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Internal error",
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		default:
		}
	}


	private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

		/**
		 * Defining Process dialog
		 **/
		private ProgressDialog pDialog;

		String email,password,fname;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//check field still in memory
			if(inputPassword==null){
				inputPassword = (EditText) findViewById(R.id.password);
			}
			if(inputFullName==null){
				inputFullName = (EditText) findViewById(R.id.fname);
			}
			if(inputEmail==null){
				inputEmail = (EditText) findViewById(R.id.email);
			}

			fname = inputFullName.getText().toString();
			email = inputEmail.getText().toString();
			password = inputPassword.getText().toString();

			//init progress bar
			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setTitle(getResources().getString(R.string.contacting_server));
			pDialog.setMessage(getResources().getString(R.string.registering));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions(RegisterActivity.this);
			JSONObject json = null;
			Bitmap bitmap = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
			if(userId!=null&&userId.trim().length()>0){
				json = userFunction.updateUser(
						userId, 
						fname, 
						email.trim(), 
						EncodingUtils.BitMapToByteArray(bitmap),
						fullImageRaw,
						zone);
				if(imageURL!=null&&imageURL.length()>0){
					imageLoader.clearCache(imageURL);
				}else{
					imageLoader.clearCache();
				}
			}else{
				json = userFunction.registerUser(
						fname, 
						email.trim(), 
						password.trim(), 
						EncodingUtils.BitMapToByteArray(bitmap),
						fullImageRaw,
						zone);
			}
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			/**
			 * Checks for success message.
			 **/
			try {
				JSONObject json_user = json;

				if (!json_user.has(KEY_ERROR_CODE)&&!json_user.has(KEY_ERROR_MESSAGE)) {
					pDialog.setTitle(getResources().getString(R.string.getData));
					pDialog.setMessage(getResources().getString(R.string.loadInfo));

					if(userId==null||userId.trim().length()==0){
						Intent registered = new Intent(getApplicationContext(), MapActivity.class);

						/**
						 * Close all views before launching Uploader screen
						 **/
						registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						
						
						//add to sql lite

						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						/**
						 * Clear all previous data in SQlite database.
						 **/
						UserFunctions logout = new UserFunctions(RegisterActivity.this);
						logout.clearLocalUserData(getApplicationContext());
						JSONObject whatsupDtoObj = json_user;
						
						String fullProfileImageUrl="";
						String sampleProfileImageUrl="";
						String fullCoverImageUrl="";
						String sampleCoverImageUrl="";
						if(whatsupDtoObj.has("profileImageRaw")){
							JSONObject imageObj = whatsupDtoObj.getJSONObject("profileImageRaw");
							fullProfileImageUrl = imageObj.getString(KEY_SAMPLE_PROFILE_IMAGE_URL);
							sampleProfileImageUrl = imageObj.getString(KEY_SAMPLE_PROFILE_IMAGE_URL);
						}
						
						if(whatsupDtoObj.has("coverImageRaw")){
							JSONObject imageObj = whatsupDtoObj.getJSONObject("coverImageRaw");
							fullCoverImageUrl = imageObj.getString(KEY_SAMPLE_COVER_IMAGE_URL);
							sampleCoverImageUrl = imageObj.getString(KEY_SAMPLE_COVER_IMAGE_URL);
						}
						
						
						db.addUser(
								whatsupDtoObj.getString(KEY_FULLNAME),
								whatsupDtoObj.getString(KEY_EMAIL),
								whatsupDtoObj.getString(KEY_ID), 
								whatsupDtoObj.getString(KEY_TOKEN),
								whatsupDtoObj.getString(KEY_SESSION_ID),
								sampleProfileImageUrl,
								fullProfileImageUrl,
								sampleCoverImageUrl,
								fullCoverImageUrl
								);
					
						
						
						pDialog.dismiss();
						startActivity(registered);
					}else{
						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						/**
						 * Clear all previous data in SQlite database.
						 **/
						UserFunctions logout = new UserFunctions(RegisterActivity.this);
						logout.clearLocalUserData(getApplicationContext());
						JSONObject whatsupDtoObj = json_user;
						String fullProfileImageUrl="";
						String sampleProfileImageUrl="";
						String fullCoverImageUrl="";
						String sampleCoverImageUrl="";
						if(whatsupDtoObj.has("profileImageRaw")){
							JSONObject imageObj = whatsupDtoObj.getJSONObject("profileImageRaw");
							fullProfileImageUrl = imageObj.getString(KEY_FULL_PROFILE_IMAGE_URL);
							sampleProfileImageUrl = imageObj.getString(KEY_SAMPLE_PROFILE_IMAGE_URL);
						}
						
						if(whatsupDtoObj.has("coverImageRaw")){
							JSONObject imageObj = whatsupDtoObj.getJSONObject("coverImageRaw");
							fullCoverImageUrl = imageObj.getString(KEY_FULL_COVER_IMAGE_URL);
							sampleCoverImageUrl = imageObj.getString(KEY_SAMPLE_COVER_IMAGE_URL);
						}
						
						
						db.addUser(
								whatsupDtoObj.getString(KEY_FULLNAME),
								whatsupDtoObj.getString(KEY_EMAIL),
								whatsupDtoObj.getString(KEY_ID), 
								whatsupDtoObj.getString(KEY_TOKEN),
								whatsupDtoObj.getString(KEY_SESSION_ID),
								sampleProfileImageUrl,
								fullProfileImageUrl,
								sampleCoverImageUrl,
								fullCoverImageUrl
								);
					
					}
					pDialog.dismiss();
					finish();
				}
				else {
					pDialog.dismiss();
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(json_user.getString(KEY_ERROR_CODE)+":"+json_user.getString(KEY_ERROR_MESSAGE));
				}
			} catch (Exception e) {
				pDialog.dismiss();
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText(getResources().getString(R.string.genError));//genError
			}
		}}



	//get the profile image path
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	//decode and compress the photo
	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		fullImageRaw = EncodingUtils.BitMapToByteArray(BitmapFactory.decodeFile(filePath, o));

		o.inJustDecodeBounds = true;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < Constants.REQUIRED_SIZE && height_tmp < Constants.REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bos);
		imgView.setImageBitmap(bitmap);

	}
	
	@Override
	public void onBackPressed() {   
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
		}
	}
	
	
	public void NetAsync(View view){
		new ProcessRegister().execute();
	}
}


