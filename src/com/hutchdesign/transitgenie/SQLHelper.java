package com.hutchdesign.transitgenie;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class SQLHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "favorites.db";
	private static final int DATABASE_VERSION = 1;

	//Table Title
	public static final String TABLE = "favorites";	

	//Database fields (Table "Columns")
	public static final String NAME = "name";
	public static final String LAT = "latitude";
	public static final String LON = "longitude";

	public SQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE + "( " + BaseColumns._ID
				+ " integer primary key autoincrement, " 
				+ NAME + " text not null, "
				+ LAT + " double, "
				+ LON + " double);";
		Log.d("EventsData", "onCreate: " + sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;

		String sql = "Upgrading database from version " + oldVersion + " to " + newVersion;

		Log.d("EventsData", "onUpgrade	: " + sql);
		if (sql != null)
			db.execSQL(sql);
	}
    
}
