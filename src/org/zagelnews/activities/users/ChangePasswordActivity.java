package org.zagelnews.activities.users;

import org.json.JSONObject;
import org.zagelnews.activities.BaseActivity;
import org.zagelnews.activities.R;
import org.zagelnews.db.DatabaseHandler;
import org.zagelnews.delegates.UserFunctions;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePasswordActivity extends BaseActivity {

	private static String KEY_ERROR_CODE = "errorCode";
	private static String KEY_ERROR_MESSAGE = "errorMessage";
	private TextView errorMsg;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);

		//perform the change action
		final EditText inputOldPassword = (EditText) findViewById(R.id.oldPassword);
		inputOldPassword.setNextFocusDownId(R.id.newPassword);
		final EditText inputNewPassword = (EditText) findViewById(R.id.newPassword);
		inputNewPassword.setNextFocusDownId(R.id.newPasswordConf);
		final EditText inputNewPasswordConf = (EditText) findViewById(R.id.newPasswordConf);
		errorMsg = (TextView) findViewById(R.id.errorMsg);

		Button btnchg = (Button) findViewById(R.id.login);
		btnchg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				String oldPassword = inputOldPassword.getText().toString();
				String newPassword = inputNewPassword.getText().toString();
				String newPasswordConf = inputNewPasswordConf.getText().toString();

				if (oldPassword.trim().length()==0||newPassword.trim().length()==0||newPasswordConf.trim().length()==0)
				{
					errorMsg.setText(getResources().getString(R.string.MandatoryFields));
				}
				else if(!newPassword.toString().equals(newPasswordConf.toString())){
					errorMsg.setText(getResources().getString(R.string.passwordMismatch));
				}
				else{
					new ProcessChangePassword().execute(oldPassword, newPassword);
				}
			}
		});
	}









	/**
	 * Async Task to get and send data to My Sql database through JSON respone.
	 **/
	private class ProcessChangePassword extends AsyncTask<String, String, JSONObject> {

		/**
		 * Defining Process dialog
		 **/
		private ProgressDialog pDialog;
		String oldPassword;
		String newPassword;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			//init progress bar
			pDialog = new ProgressDialog(ChangePasswordActivity.this);
			pDialog.setTitle(getResources().getString(R.string.contacting_server));
			pDialog.setMessage(getResources().getString(R.string.registering));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			oldPassword = args[0];
			newPassword = args[1];
			UserFunctions userFunction = new UserFunctions(ChangePasswordActivity.this);
			JSONObject json = userFunction.changePassword(oldPassword.trim(), newPassword.trim());
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
				}else if(json!=null&&json.has("result")){
					String token = json.getString("result");
					new DatabaseHandler(getApplicationContext()).updateToken(token);
					pDialog.dismiss();
					finish();
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

	@Override
	public void getTaskResult(Object zagelnewsDto, Integer taskId) {

	}
}
