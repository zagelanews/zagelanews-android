package org.zagelnews.activities.users;

import org.json.JSONObject;
import org.zagelnews.activities.R;
import org.zagelnews.delegates.UserFunctions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgetPasswordActivity extends Activity {

	private static String KEY_ERROR_CODE = "errorCode";
	private static String KEY_ERROR_MESSAGE = "errorMessage";
	private TextView errorMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		
		//perform the change action
		final EditText inputEmail = (EditText) findViewById(R.id.email);
		errorMsg = (TextView) findViewById(R.id.errorMsg);
		errorMsg.setVisibility(View.GONE);
		
		Button btnfrg = (Button) findViewById(R.id.frgPass);
		btnfrg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				String email = inputEmail.getText().toString();

				if (email.trim().length()>0)
				{
					new NetCheck().execute(email);
				}else{
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(getResources().getString(R.string.MandatoryFields));
				}
			}
		});
	}


	
	//async actions
	/**
	 * Async Task to check whether internet connection is working
	 **/

	private class NetCheck extends AsyncTask<String,String,Boolean>
	{
		String email;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}

		/**
		 * Gets current device state and checks for working internet connection by trying Google.
		 **/
		@Override
		protected Boolean doInBackground(String... args){
			email= args[0];
			return true;
		}

		@Override
		protected void onPostExecute(Boolean th){

			if(th == true){
				new ProcessChangePassword().execute(email);
			}
			else{
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText(getResources().getString(R.string.netError));
			}
		}
	}

	/**
	 * Async Task to get and send data to My Sql database through JSON respone.
	 **/
	private class ProcessChangePassword extends AsyncTask<String, String, JSONObject> {


		String email;
		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//init progress bar
			pDialog = new ProgressDialog(ForgetPasswordActivity.this);
			pDialog.setTitle(getResources().getString(R.string.contacting_server));
			pDialog.setMessage(getResources().getString(R.string.registering));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			email = args[0];
			UserFunctions userFunction = new UserFunctions(ForgetPasswordActivity.this);
			JSONObject json = userFunction.forgetPassword(email);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			
			try {
				pDialog.setTitle(getResources().getString(R.string.getData));
				pDialog.setMessage(getResources().getString(R.string.loadInfo));

				JSONObject json_container = json;//(json!=null&&json.has("androidDto"))?json.getJSONObject("androidDto"):null;
				if (json_container!=null&&json_container.has(KEY_ERROR_CODE)&&json_container.getString(KEY_ERROR_CODE)!=null&&json_container.getString(KEY_ERROR_CODE).trim().length()>0) {
					String errorCode = json_container.getString(KEY_ERROR_CODE);
					String errorMessage = json_container.getString(KEY_ERROR_MESSAGE);
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(errorCode+":"+errorMessage);
				}else if(json!=null&&json.has("resultCode")&&json.getLong("resultCode")==1L){
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(getResources().getString(R.string.forgetEmailSent));
				}else{
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText(getResources().getString(R.string.genError));//genError	
				}
				pDialog.dismiss();
			} catch (Exception e) {
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText(getResources().getString(R.string.genError));//genError
				pDialog.dismiss();
			}
		}
	}

}
