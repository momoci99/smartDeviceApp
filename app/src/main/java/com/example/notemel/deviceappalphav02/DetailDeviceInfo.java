package com.example.notemel.deviceappalphav02;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Melchior_S on 2016-09-06.
 */
public class DetailDeviceInfo extends AppCompatActivity {

    TableLayout sensorData_tbl;
    String mSelectedDevice;
    String mIntentKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentKey= this.getResources().getString(R.string.MainToDetailDeviceInfo_String);
        mSelectedDevice = getIntent().getStringExtra(mIntentKey);

        setContentView(R.layout.activity_detaildeviceinfo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mSelectedDevice);
        setSupportActionBar(toolbar);


        sensorData_tbl = (TableLayout)findViewById(R.id.tbl_sensordata);


        TableRow testRow0 = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText(" Sl.No ");
        tv0.setTextColor(Color.BLUE);
        testRow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Product ");
        tv1.setTextColor(Color.BLUE);
        testRow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Unit Price ");
        tv2.setTextColor(Color.BLUE);
        testRow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText(" Stock Remaining ");
        tv3.setTextColor(Color.BLUE);
        testRow0.addView(tv3);

        sensorData_tbl.addView(testRow0);
    }
}
