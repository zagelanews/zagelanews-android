package org.zagelnews.activities;

import org.zagelnews.utils.AndroidUtils;

import android.app.Activity;

public class BaseBorderlessActivity extends Activity{
	
	public static String KEY_ERROR_CODE = "errorCode";
	public static String KEY_ERROR_MESSAGE = "errorDesc";

	/**
	 * logout the user
	 */
	public void redirectToLogin() {
		AndroidUtils.redirectToLogin(this);
	}
}
