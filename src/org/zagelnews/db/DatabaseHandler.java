package org.zagelnews.db;

/**
 * Created by Raj Amal on 5/30/13.
 */




import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "Akhpardb";

	// LoginActivity table name
	private static final String TABLE_LOGIN = "login";

	// LoginActivity Table Columns names
	public static final String KEY_ID = "id";
	public static final String KEY_FULLNAME = "fname";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_TOKEN = "token";
	public static final String KEY_SESSION_ID = "sessionId";

	public static final String KEY_SAMPLE_PROFILE_IMAGE_URL = "sampleProfileImageUrl";
	public static final String KEY_FULL_PROFILE_IMAGE_URL = "fullProfileImageUrl";

	public static final String KEY_SAMPLE_COVER_IMAGE_URL = "sampleCoverImageUrl";
	public static final String KEY_FULL_COVER_IMAGE_URL = "fullCoverImageUrl";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_FULLNAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE,"
				+ KEY_TOKEN + " TEXT,"
				+ KEY_SESSION_ID + " TEXT,"
				+ KEY_SAMPLE_PROFILE_IMAGE_URL + " TEXT,"
				+ KEY_FULL_PROFILE_IMAGE_URL + " TEXT,"
				+ KEY_SAMPLE_COVER_IMAGE_URL + " TEXT,"
				+ KEY_FULL_COVER_IMAGE_URL + " TEXT"
				+")";
		db.execSQL(CREATE_LOGIN_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

		// Create tables again
		onCreate(db);
	}


	/**
	 * Storing user details in database
	 * */
	public void updateToken(String token) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TOKEN, token); // FirstName

		// Inserting Row
		db.update(TABLE_LOGIN, values, null, null);
		db.close(); // Closing database connection
	}


	/**
	 * Storing user details in database
	 * @param string 
	 * @param string2 
	 * */
	public void addUser(
			String fname, 
			String email, 
			String userId, 
			String token, 
			String sessionId, 
			String sampleProfileImageUrl, 
			String fullProfileImageUrl, 
			String sampleCoverImageUrl, 
			String fullCoverImageUrl) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FULLNAME, fname); // FirstName
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_ID, userId); // userId
		values.put(KEY_TOKEN, token); // user token
		values.put(KEY_SESSION_ID, sessionId); // sessionId
		
		values.put(KEY_SAMPLE_PROFILE_IMAGE_URL, sampleProfileImageUrl); 
		values.put(KEY_FULL_PROFILE_IMAGE_URL, fullProfileImageUrl); 

		values.put(KEY_SAMPLE_COVER_IMAGE_URL, sampleCoverImageUrl); 
		values.put(KEY_FULL_COVER_IMAGE_URL, fullCoverImageUrl); 
		
		// Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection
	}


	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String,String> user = new HashMap<String,String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Move to first row
		cursor.moveToFirst();
		if(cursor.getCount() > 0){
			user.put(KEY_ID, cursor.getString(0));
			user.put(KEY_FULLNAME, cursor.getString(1));
			user.put(KEY_EMAIL, cursor.getString(2));
			user.put(KEY_TOKEN, cursor.getString(3));
			user.put(KEY_SESSION_ID, cursor.getString(4));
			
			
			user.put(KEY_SAMPLE_PROFILE_IMAGE_URL, cursor.getString(5));
			user.put(KEY_FULL_PROFILE_IMAGE_URL, cursor.getString(6));
			user.put(KEY_SAMPLE_COVER_IMAGE_URL, cursor.getString(7));
			user.put(KEY_FULL_COVER_IMAGE_URL, cursor.getString(8));
			
		}
		cursor.close();
		db.close();
		// return user
				return user;
	}


	/**
	 * Getting user login status
	 * return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}


	/**
	 * Re crate database
	 * Delete all tables and create them again
	 * */
	public void resetTables(){
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();
	}

}
