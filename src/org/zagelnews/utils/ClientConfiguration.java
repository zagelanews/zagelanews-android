package org.zagelnews.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import org.zagelnews.db.DatabaseHandler;
import org.zagelnews.dtos.feeds.UserNotificationDto;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ClientConfiguration {

	//device default info
	public static String email;
	public static String name;
	public static String zone;
	public static String cntCode;
	public static String lang;


	//database stored user info
	private static HashMap<String,String> user;
	
	public static ArrayList<UserNotificationDto> userFeedsNotifications = new ArrayList<UserNotificationDto>();

	public ClientConfiguration(Activity MainActivity) {}

	public static void initConfiguration(Activity MainActivity){
		//get device default info
		getDefaultDeviceConfiguration(MainActivity);

		//get database stored user info
		if(user==null||user.get(DatabaseHandler.KEY_ID)==null||user.get(DatabaseHandler.KEY_ID).trim().length()==0){
			getDatabaseUserInfo(MainActivity);
		}
	}

	/**
	 * get database stored user info
	 * @param mainActivity
	 */
	private static void getDatabaseUserInfo(Activity mainActivity) {
		DatabaseHandler db = new DatabaseHandler(mainActivity.getApplicationContext());
		user = db.getUserDetails();
	}

	/**
	 * get device default info
	 * @param MainActivity
	 */
	private static void getDefaultDeviceConfiguration(Activity MainActivity){
		//get the default user info
		final AccountManager manager = AccountManager.get(MainActivity);
/*		final Account[] accounts = manager.getAccountsByType("com.google");
		if(accounts!=null&&accounts.length>0){
			if (accounts[0].name != null) {
				String accountName = accounts[0].name;

				ContentResolver cr = MainActivity.getContentResolver();
				Cursor emailCur = cr.query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Email.DATA + " = ?",
						new String[] { accountName }, null);
				while (emailCur.moveToNext()) {
					email = emailCur
							.getString(emailCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

					String newName = emailCur
							.getString(emailCur
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					if (name == null || newName.length() > name.length())
						name = newName;
				}

				emailCur.close();
			}
		}
*/
		//get the default zone info
		zone = TimeZone.getDefault().getID();

		//get the default country code
		cntCode = MainActivity.getResources().getConfiguration().locale.getCountry();

		//get the default language
		lang = MainActivity.getResources().getConfiguration().locale.getLanguage();
	}
	
	public static HashMap<String, String> getUser() {
		return user;
	}
	
	public static HashMap<String, String> getUser(Activity activity) {
		if(user==null||user.get(DatabaseHandler.KEY_ID)==null||user.get(DatabaseHandler.KEY_ID).trim().length()==0){
			initConfiguration(activity);
		}
		
		if((user==null||user.get(DatabaseHandler.KEY_ID)==null||user.get(DatabaseHandler.KEY_ID).trim().length()==0)&&activity!=null){
			AndroidUtils.redirectToLogin(activity);
			activity.finish();
			user = new HashMap<String, String>();
		}
		return user;
	}

	public static void resetUserInfo() {
		user=null;
	}
}
