package com.example.notemel.deviceappalphav02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import adapter.ChartDeviceNameListReAdapter;
import adapter.ElementSelectListReAdapter;
import db.DBCommander;

/**
 * Created by Melchior_S on 2016-09-27.
 */
public class ChartOptionSelectActivity extends AppCompatActivity {

    private final String TAG = "ChartOption";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter_ElementList;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mRV_devcieList;
    private RecyclerView.Adapter mAdapter_DeviceList ;
    private RecyclerView.LayoutManager mLayoutManager_DeviceList;

    private Button mBTN_ShowChart;


    private ArrayList<String> mColumnsList = new ArrayList<>();
    private String[] mColumnsArray;

    private ArrayList<String> mDeviceList = new ArrayList<>();

    private DBCommander mDBCommander = DBCommander.getInstance();

    private ArrayList<String> mChartOptionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_option);

        mRecyclerView = (RecyclerView)findViewById(R.id.ry_element_list);
        mRecyclerView.setHasFixedSize(true) ;


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mColumnsArray = DBCommander.getSensorElementData();

        for(int i =0; i<mColumnsArray.length; i++)
        {
            mColumnsList.add(mColumnsArray[i]);
        }


        mRV_devcieList = (RecyclerView)findViewById(R.id.ry_chart_device_name_list);
        mLayoutManager_DeviceList = new LinearLayoutManager(this);
        mRV_devcieList.setLayoutManager(mLayoutManager_DeviceList);

        mDeviceList.addAll(mDBCommander.getFullDeviceList());
        Log.e(TAG,"mDeviceList 사이즈 : "+ mDeviceList.size());



        mAdapter_ElementList = new ElementSelectListReAdapter(mColumnsList);
        mRecyclerView.setAdapter(mAdapter_ElementList);

        mAdapter_DeviceList = new ChartDeviceNameListReAdapter(mDeviceList);
        mRV_devcieList.setAdapter(mAdapter_DeviceList);





        mBTN_ShowChart = (Button)findViewById(R.id.btn_show_chart);
        mBTN_ShowChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedElement = ElementSelectListReAdapter.mSelectedElement;
                ArrayList selectedDeviceList = ChartDeviceNameListReAdapter.mSelectedDeviceList;
                if(!selectedElement.equals("") && selectedDeviceList.size()>0)
                {
                    mChartOptionList.add(ElementSelectListReAdapter.mSelectedElement);
                    mChartOptionList.addAll(ChartDeviceNameListReAdapter.mSelectedDeviceList);


                    Intent intent = new Intent(ChartOptionSelectActivity.this, ChartActivity.class);
                    intent.putStringArrayListExtra("ChartOption.list", mChartOptionList);
                    startActivity(intent);

                    mChartOptionList.clear();

                }







            }
        });

    }
}
