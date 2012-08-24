package com.gloptic.measurements.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.gloptic.measurements.database.MeasurementsDatabaseHelper;
import com.gloptic.measurements.database.MeasurementsTable;

public class MeasurementsContentProvider extends ContentProvider {

  // database
  private MeasurementsDatabaseHelper database;

  // Used for the UriMacher
  private static final int TODOS = 10;
  private static final int TODO_ID = 20;

  private static final String AUTHORITY = "com.gloptic.measurements.contentprovider";

  private static final String BASE_PATH = "measurements";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
      + "/" + BASE_PATH);

  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
      + "/measurements";
  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/measurements";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH, TODOS);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODO_ID);
  }

  @Override
  public boolean onCreate() {
    database = new MeasurementsDatabaseHelper(getContext());
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Uisng SQLiteQueryBuilder instead of query() method
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // Check if the caller has requested a column which does not exists
    checkColumns(projection);

    // Set the table
    queryBuilder.setTables(MeasurementsTable.TABLE_MEASUREMENTS);

    int uriType = sURIMatcher.match(uri);
    switch (uriType) {
    case TODOS:
      break;
    case TODO_ID:
      // Adding the ID to the original query
      queryBuilder.appendWhere(MeasurementsTable.COLUMN_ID + "="
          + uri.getLastPathSegment());
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection,
        selectionArgs, null, null, sortOrder);
    // Make sure that potential listeners are getting notified
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    //int rowsDeleted = 0;
    long id = 0;
    switch (uriType) {
    case TODOS:
      id = sqlDB.insert(MeasurementsTable.TABLE_MEASUREMENTS, null, values);
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return Uri.parse(BASE_PATH + "/" + id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    switch (uriType) {
    case TODOS:
      rowsDeleted = sqlDB.delete(MeasurementsTable.TABLE_MEASUREMENTS, selection,
          selectionArgs);
      break;
    case TODO_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
    	  rowsDeleted = sqlDB.delete(MeasurementsTable.TABLE_MEASUREMENTS,
    			  MeasurementsTable.COLUMN_ID + "=" + id, 
    			  null);
      } else {
    	  rowsDeleted = sqlDB.delete(MeasurementsTable.TABLE_MEASUREMENTS,
    			  MeasurementsTable.COLUMN_ID + "=" + id 
    			  + " and " + selection,
    			  selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {

    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsUpdated = 0;
    switch (uriType) {
    case TODOS:
      rowsUpdated = sqlDB.update(MeasurementsTable.TABLE_MEASUREMENTS, 
          values, 
          selection,
          selectionArgs);
      break;
    case TODO_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(MeasurementsTable.TABLE_MEASUREMENTS, 
            values,
            MeasurementsTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsUpdated = sqlDB.update(MeasurementsTable.TABLE_MEASUREMENTS, 
            values,
            MeasurementsTable.COLUMN_ID + "=" + id 
            + " and " 
            + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }

  private void checkColumns(String[] projection) {
    String[] available = { MeasurementsTable.COLUMN_NAME,
    		MeasurementsTable.COLUMN_DATETIME, MeasurementsTable.COLUMN_ID };
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
      // Check if all columns which are requested are available
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException("Unknown columns in projection");
      }
    }
  }

} 