package org.zagelnews.utils;

import org.zagelnews.activities.users.LoginActivity;
import org.zagelnews.delegates.UserFunctions;
import org.zagelnews.ui.images.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.WindowManager;

public class AndroidUtils {
	/**
	 * redirect the user to the login activity if the current session has been invalidated
	 * @param context
	 */
	public static void redirectToLogin(Activity context) {
		UserFunctions logout = new UserFunctions(context);
		logout.resetTables(context.getApplicationContext());
		ClientConfiguration.resetUserInfo();
		//redirect
		Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);

		if(Build.VERSION.SDK_INT >= 11)
		{
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP/*|Intent.FLAG_ACTIVITY_CLEAR_TASK*/);
		}
		else
		{
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}

		context.startActivity(intent);
		context.finish();
	}
	
	/**
	 * clear the image caching
	 */
	public static void clearImageCache(Activity activity) {
		new ImageLoader(activity, ImageLoader.stub_id_noimage).clearCache();
	}
	
	/**
	 * This prevents my Nexus 7 running Android 4.4.2 from opening
	 * the soft keyboard when the app is launched rather than when
	 * an input field is selected.
	 * @param activity
	 */
	public static void disableSpftKeyboard(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
