package com.gloptic.measurements;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MeasurementAdapter extends ArrayAdapter<MeasurementRow>{

    Context context;
    int layoutResourceId;   
    MeasurementRow data[] = null;
   
    public MeasurementAdapter(Context context, int layoutResourceId, MeasurementRow [] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RowHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new RowHolder();
            holder.txtWave = (TextView)row.findViewById(R.id.txtWave);
            holder.txtValue= (TextView)row.findViewById(R.id.txtValue);
           
            row.setTag(holder);
        }
        else
        {
            holder = (RowHolder)row.getTag();
        }
       
        MeasurementRow rowM= data[position];
        holder.txtWave.setText(Double.toString(rowM.wave)+"  "+Double.toString(rowM.value));
        //holder.txtValue.setText(Double.toString(rowM.value));
       
        return row;
    }
   
    static class RowHolder
    {
    	TextView txtWave;
        TextView txtValue;
    }
}