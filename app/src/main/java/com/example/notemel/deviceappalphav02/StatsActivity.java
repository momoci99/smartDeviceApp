package com.example.notemel.deviceappalphav02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import adapter.ChartDeviceNameListReAdapter;
import adapter.ElementSelectListReAdapter;
import adapter.SensorDataListReAdapter;
import db.DBCommander;

/**
 * Created by Melchior_S on 2016-09-27.
 */
public class StatsActivity extends AppCompatActivity {

    private final String TAG = "StatsActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter ;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mRV_devcieList;
    private RecyclerView.Adapter mAdapter_DeviceList ;
    private RecyclerView.LayoutManager mLayoutManager_DeviceList;

    private ArrayList<String> mColumnsList = new ArrayList<>();
    private String[] mColumnsArray;

    private ArrayList<String> mDeviceList = new ArrayList<>();

    private DBCommander mDBCommander = DBCommander.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        mRecyclerView = (RecyclerView)findViewById(R.id.ry_element_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true) ;

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mColumnsArray = DBCommander.getSensorElementData();

        for(int i =0; i<mColumnsArray.length; i++)
        {
            mColumnsList.add(mColumnsArray[i]);
        }
        Log.e(TAG,"mColumns 사이즈 : "+ mColumnsList.size());
        mAdapter = new ElementSelectListReAdapter(mColumnsList);
        mRecyclerView.setAdapter(mAdapter);




        mRV_devcieList = (RecyclerView)findViewById(R.id.ry_chart_device_name_list);
        mRV_devcieList.setLayoutManager(mLayoutManager_DeviceList);
        mLayoutManager_DeviceList = new LinearLayoutManager(this);

        mDeviceList.addAll(mDBCommander.getFullDeviceList());

        mAdapter_DeviceList = new ChartDeviceNameListReAdapter(mDeviceList);
        mRV_devcieList.setAdapter(mAdapter_DeviceList);

    }
}
