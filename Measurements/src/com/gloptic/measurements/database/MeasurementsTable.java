package com.gloptic.measurements.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MeasurementsTable {

  // Database table
  public static final String TABLE_MEASUREMENTS = "measurements";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_DATETIME = "datetime";
  public static final String COLUMN_WAVES = "waves";
  public static final String COLUMN_VALUES = "val";

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_MEASUREMENTS
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_NAME+ " text not null, " 
      + COLUMN_DATETIME + " text not null, "
      + COLUMN_WAVES + " BLOB, "
      + COLUMN_VALUES + " BLOB"
      + ");";

  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(MeasurementsTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEASUREMENTS);
    onCreate(database);
  }
} 