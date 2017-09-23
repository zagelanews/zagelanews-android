package org.zagelnews.activities.users;

import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONObject;
import org.zagelnews.activities.BaseActivity;
import org.zagelnews.activities.R;
import org.zagelnews.activities.feeds.MapActivity;
import org.zagelnews.db.DatabaseHandler;
import org.zagelnews.delegates.UserFunctions;
import org.zagelnews.utils.ClientConfiguration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {

	Button btnLogin;
	Button Btnregister;
	TextView BtnForgetPass;
	EditText inputEmail;
	EditText inputPassword;
	
	private TextView errorMsg;
	protected String zone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activitylogin);

		zone = TimeZone.getDefault().getID();

		checkUserCookies();

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		
		
		Btnregister = (Button) findViewById(R.id.register);
		//navigate to the register activity
		Btnregister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
				startActivityForResult(myIntent, 0);
				finish();
			}});
		
		
		
		BtnForgetPass = (TextView) findViewById(R.id.forgetPassbtn);
		//navigate to the forget activity
		BtnForgetPass.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), ForgetPasswordActivity.class);
				startActivityForResult(myIntent, 0);
			}});


		errorMsg = (TextView) findViewById(R.id.errorMsg);
		errorMsg.setVisibility(View.GONE);


		//perform the login action
		btnLogin = (Button) findViewById(R.id.login);
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();

				if (email.trim().length()>0&&password.trim().length()>0)
				{
					NetAsync(view);
				}else{
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(getResources().getString(R.string.MandatoryFields));
				}
			}
		});
	}
	
	/**
	 * check if the user alread logged in
	 */
	private void checkUserCookies() {
		ClientConfiguration.initConfiguration(this);
		HashMap<String,String> user = ClientConfiguration.getUser();
		if(user!=null&&user.get(DatabaseHandler.KEY_ID)!=null&&user.get(DatabaseHandler.KEY_ID).trim().length()>0){
			Intent feeds = new Intent(getApplicationContext(), MapActivity.class);
			feeds.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(feeds);
			finish();
		}
	}

	/**
	 * Async Task to get and send data to My Sql database through JSON respone.
	 **/
	private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


		String email,password;
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//init progress bar
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setTitle(getResources().getString(R.string.contacting_server));
			pDialog.setMessage(getResources().getString(R.string.registering));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			
			inputEmail = (EditText) findViewById(R.id.email);
			inputPassword = (EditText) findViewById(R.id.password);

			if(inputEmail!=null){
				email = inputEmail.getText().toString();
			}

			if(inputPassword!=null){
				password = inputPassword.getText().toString();
			}

		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions(LoginActivity.this);
			JSONObject json = userFunction.loginUser(email.trim(), password.trim());
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				pDialog.setTitle(getResources().getString(R.string.getData));
				pDialog.setMessage(getResources().getString(R.string.loadInfo));
				JSONObject json_user = json;

				if(json_user==null){
					pDialog.dismiss();
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(getResources().getString(R.string.genError));//genError
					return;
				}


				if (!json_user.has(KEY_ERROR_CODE)||json_user.getString(KEY_ERROR_CODE)==null||json_user.getString(KEY_ERROR_CODE).trim().length()==0) {

					DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					/**
					 * Clear all previous data in SQlite database.
					 **/
					UserFunctions logout = new UserFunctions(LoginActivity.this);
					logout.resetTables(getApplicationContext());
					db.addUser(
							json_user.getString("fullName"),
							json_user.getString("email"),
							json_user.getString("userId"), 
							json_user.getString("token"), 
							json_user.getString("sessionId"), 
							json_user.getString("profileSampleImageUrl"),
							json_user.getString("profileFullImageUrl"),
							json_user.getString("coverSampleImageUrl"),
							json_user.getString("coverFullImageUrl"));

					ClientConfiguration.initConfiguration(LoginActivity.this);

					/**
					 *If JSON array details are stored in SQlite it launches the User Panel.
					 **/
					Intent newInterest = new Intent(getApplicationContext(), MapActivity.class);
					newInterest.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(newInterest);
					/**
					 * Close LoginActivity Screen
					 **/
					pDialog.dismiss();
					finish();

				}else{
					pDialog.dismiss();
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(json_user.getString(KEY_ERROR_CODE)+":"+json_user.getString(KEY_ERROR_MESSAGE));//invalidLogin
				}
			} catch (Exception e) {
				pDialog.dismiss();
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText(getResources().getString(R.string.genError));//genError
			}catch (Throwable e) {
				pDialog.dismiss();
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText(getResources().getString(R.string.genError));//genError
			}
		}
	}
	public void NetAsync(View view){
		new ProcessLogin().execute();
	}
	
	@Override
	public void getTaskResult(Object result, Integer taskId) {
	}
}