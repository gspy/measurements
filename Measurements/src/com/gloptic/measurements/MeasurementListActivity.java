package com.gloptic.measurements;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import com.gloptic.measurements.contentprovider.MeasurementsContentProvider;
import com.gloptic.measurements.database.MeasurementsTable;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MeasurementListActivity extends Activity {

    private ListView listView1;
    private Uri mUri;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measurement_list);
       
        Bundle extras = getIntent().getExtras();

        // Check from the saved Instance
        mUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
            .getParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
          mUri = extras
              .getParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE);

//          MeasurementRow m_data[] = null;
//          
//          String[] projection = { MeasurementsTable.COLUMN_NAME,  MeasurementsTable.COLUMN_DATETIME};
//          Cursor cursor = getContentResolver().query(mUri, projection, null, null,
//              null);
//          if (cursor != null) {
//            cursor.moveToFirst();
//            byte [] waves = cursor.getBlob(cursor
//                .getColumnIndexOrThrow(MeasurementsTable.COLUMN_WAVES));
//            byte [] values= cursor.getBlob(cursor
//                    .getColumnIndexOrThrow(MeasurementsTable.COLUMN_VALUES));
//
//            int count = waves.length / Double.SIZE/8;
//            m_data = new MeasurementRow[count];
//            	
//            
//            ByteBuffer bufWaves = ByteBuffer.wrap(waves);
//            DoubleBuffer bufDoubleWaves = bufWaves.asDoubleBuffer();
//            
//            for (int i =0; i<bufDoubleWaves.capacity(); i++)
//            {
//            		m_data[i].wave = bufDoubleWaves.get(i);
//              		m_data[i].value = bufDoubleWaves.get(i);
//            }
//            
//            // Always close the cursor
//            cursor.close();
//          }
        
        MeasurementRow m_data[] = new MeasurementRow []
        {
            new MeasurementRow(234, 345),
            new MeasurementRow(235, 334),
            new MeasurementRow(236, 234),
            new MeasurementRow(237, 378),
            new MeasurementRow(238, 123),
            new MeasurementRow(239, 345),
            new MeasurementRow(240, 334),
            new MeasurementRow(241, 234),
            new MeasurementRow(242, 378),
            new MeasurementRow(243, 123),
            new MeasurementRow(244, 345),
            new MeasurementRow(245, 334),
            new MeasurementRow(246, 234),
            new MeasurementRow(247, 378),
            new MeasurementRow(278, 123),
            new MeasurementRow(279, 345),
            new MeasurementRow(250, 334),
            new MeasurementRow(251, 234),
            new MeasurementRow(252, 378),
            new MeasurementRow(253, 123),
            
        };
       
        MeasurementAdapter adapter = new MeasurementAdapter(this,
                R.layout.listview_item_row, m_data);
       
       
        listView1 = (ListView)findViewById(R.id.listView1);
        
        View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        listView1.addHeaderView(header);
       
        listView1.setAdapter(adapter);
        }
    }
 }