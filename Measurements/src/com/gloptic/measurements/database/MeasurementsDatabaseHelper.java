package com.gloptic.measurements.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MeasurementsDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "measurements.db";
	private static final int DATABASE_VERSION = 17;

	public MeasurementsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		MeasurementsTable.onCreate(database);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		MeasurementsTable.onUpgrade(database, oldVersion, newVersion);
	}

}
