package com.example.notemel.deviceappalphav02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

/**
 * Created by Melchior_S on 2016-10-01.
 */

public class ChartActivity extends AppCompatActivity {


    //https://github.com/PhilJay/MPAndroidChart
    //https://github.com/PhilJay/MPAndroidChart/wiki/Getting-Started
    //TODO:Drawing GRAPH!
    private final String TAG = ChartActivity.this.getClass().getSimpleName();

    private ArrayList<String> mChartOptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mChartOptionList = getIntent().getStringArrayListExtra("ChartOption.list");
        Log.e(TAG,mChartOptionList.get(0));
        Log.e(TAG,mChartOptionList.get(1));

        LineChart chart = (LineChart) findViewById(R.id.chart);
        // get a layout defined in xml


    }
}
