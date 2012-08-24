package com.gloptic.measurements;

import java.nio.*;
import java.util.Arrays;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.gloptic.measurements.contentprovider.MeasurementsContentProvider;
import com.gloptic.measurements.database.MeasurementsTable;



/*
 * TodoDetailActivity allows to enter a new todo item 
 * or to change an existing
 */
public class MeasurementsDetailActivity extends Activity {
  private EditText mName;
  private TextView mDateTime;

  private Uri todoUri;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.measurements_edit);

    mName = (EditText) findViewById(R.id.mesurements_edit_name);
    mDateTime = (TextView) findViewById(R.id.mesurements_edit_datetime);
    Button confirmButton = (Button) findViewById(R.id.measurements_edit_button);

    Bundle extras = getIntent().getExtras();

    // Check from the saved Instance
    todoUri = (bundle == null) ? null : (Uri) bundle
        .getParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE);

    // Or passed from the other activity
    if (extras != null) {
      todoUri = extras
          .getParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE);

      fillData(todoUri);
    }
    
    
    confirmButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        if (TextUtils.isEmpty(mName.getText().toString())) {
          makeToast();
        } else {
          setResult(RESULT_OK);
          finish();
        }
      }

    });
  }

  private void fillData(Uri uri) {
    String[] projection = { MeasurementsTable.COLUMN_NAME,  MeasurementsTable.COLUMN_DATETIME};
    Cursor cursor = getContentResolver().query(uri, projection, null, null,
        null);
    if (cursor != null) {
      cursor.moveToFirst();
      String name = cursor.getString(cursor
          .getColumnIndexOrThrow(MeasurementsTable.COLUMN_NAME));
      String time = cursor.getString(cursor
              .getColumnIndexOrThrow(MeasurementsTable.COLUMN_DATETIME));

      mName.setText(cursor.getString(cursor
    		  .getColumnIndexOrThrow(MeasurementsTable.COLUMN_NAME)));
      mDateTime.setText(cursor.getString(cursor
    		  .getColumnIndexOrThrow(MeasurementsTable.COLUMN_DATETIME)));
      // Always close the cursor
      cursor.close();
    }
  }

  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    saveState();
    outState.putParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE, todoUri);
  }

  @Override
  protected void onPause() {
    super.onPause();
    saveState();
  }

  private void saveState() {
    String name = mName.getText().toString();
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String datetime = dateFormat.format(new Date());
    double[] waves = new double[401];
    double[] values = new double[401];
    
    Random randomGenerator = new Random();
    for(int i=0; i<=400; i++)
    {
    	waves[i] = i+300;
    	values[i] = randomGenerator.nextInt(100);    	
    }
    
    byte[] bytesW = new byte[waves.length * Double.SIZE/8];
    ByteBuffer bufWaves = ByteBuffer.wrap(bytesW);
    for (double d : waves)
        bufWaves.putDouble(d);
    byte[] bytesV = new byte[values.length * Double.SIZE/8];
    ByteBuffer bufValues = ByteBuffer.wrap(bytesV);
    for (double d : values)
        bufValues.putDouble(d);

    // Only save if either summary or description
    // is available

    if (name.length() == 0) {
      return;
    }

    ContentValues cValues = new ContentValues();
    cValues.put(MeasurementsTable.COLUMN_NAME, name);
    cValues.put(MeasurementsTable.COLUMN_DATETIME, datetime);
    cValues.put(MeasurementsTable.COLUMN_WAVES, bytesW);
    cValues.put(MeasurementsTable.COLUMN_VALUES, bytesV);

    if (todoUri == null) {
      // New todo
      todoUri = getContentResolver().insert(MeasurementsContentProvider.CONTENT_URI, cValues);
    } else {
      // Update todo
      getContentResolver().update(todoUri, cValues, null, null);
    }
  }

  private void makeToast() {
    Toast.makeText(MeasurementsDetailActivity.this, "Please maintain a name",
        Toast.LENGTH_LONG).show();
  }
} 