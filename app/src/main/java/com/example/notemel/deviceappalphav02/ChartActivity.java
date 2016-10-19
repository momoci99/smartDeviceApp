package com.example.notemel.deviceappalphav02;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import adapter.ChartStatsExpListAdapter;
import db.DBCommander;
import format.DBResultForm;

/**
 * Created by Melchior_S on 2016-10-01.
 */

public class ChartActivity extends AppCompatActivity {


    //https://github.com/PhilJay/MPAndroidChart
    //https://github.com/PhilJay/MPAndroidChart/wiki/Getting-Started

    private final String TAG = ChartActivity.this.getClass().getSimpleName();


    private ArrayList<String> mChartOptionList;

    private String mElement;
    private String[] mColumnsArray;

    private ArrayList<String> mDeviceList = new ArrayList<>();


    private String mStartDateString;
    private String mEndDateString;


    private BarChart mChart;
    ArrayList<IBarDataSet> mDataSets = new ArrayList<IBarDataSet>();


    private ExpandableListView mExpList;
    private ChartStatsExpListAdapter mChartStatsExpListAdapter;
    private ArrayList<String> mGroupList = new ArrayList<>();
    private ArrayList<ArrayList<String>> mChildList = new ArrayList<ArrayList<String>>();

    private HashMap<String, String> mMinDataMap = new HashMap<>();
    private HashMap<String, String> mMaxDataMap = new HashMap<>();
    private HashMap<String, String> mAverageDataMap = new HashMap<>();

    private String mIntentKey;

    //TODO:어댑터 등록하고 데이터 삽입, 갱신, 확인
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mIntentKey = getResources().getString(R.string.ToChartActivity);
        mChartOptionList = getIntent().getStringArrayListExtra(mIntentKey);


        mElement = mChartOptionList.get(0);
        mStartDateString = mChartOptionList.get(1);
        mEndDateString = mChartOptionList.get(2);

        mChartOptionList.remove(mElement);
        mChartOptionList.remove(mStartDateString);
        mChartOptionList.remove(mEndDateString);

        mDeviceList.addAll(mChartOptionList);
        mColumnsArray = DBCommander.getSensorElementData();
        mChart = (BarChart) findViewById(R.id.chart);



        if (mDeviceList.size() == 1) {
            drawSingleBarGraph();

        } else {

            drawMultiBarGraph();
        }

        //setLegend
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(10f);

        //ExpandableListView implements
        mExpList = (ExpandableListView) findViewById(R.id.explist_data_stats);
        mGroupList.add("Selected Element");
        mGroupList.add("Period");
        mGroupList.add("Element MAX");
        mGroupList.add("Element MIN");
        mGroupList.add("Element Average");

        getAttributeData();
        setListData();

        mExpList = (ExpandableListView) findViewById(R.id.explist_data_stats);
        mChartStatsExpListAdapter = new ChartStatsExpListAdapter(this, mGroupList, mChildList);
        mExpList.setAdapter(mChartStatsExpListAdapter);
        mChartStatsExpListAdapter.notifyDataSetChanged();
    }

    public void setListData() {
        ArrayList<String> elementList = new ArrayList<>();
        elementList.add(mElement);
        mChildList.add(elementList);

        ArrayList<String> periodList = new ArrayList<>();
        periodList.add("START : " + mStartDateString);
        periodList.add("END : " + mEndDateString);
        mChildList.add(periodList);

        ArrayList<String> maxList = new ArrayList<>();
        ArrayList<String> minList = new ArrayList<>();
        ArrayList<String> avgList = new ArrayList<>();
        for (int i = 0; i < mDeviceList.size(); i++) {
            maxList.add(mDeviceList.get(i) + " : " + mMaxDataMap.get(mDeviceList.get(i)));
            minList.add(mDeviceList.get(i) + " : " + mMinDataMap.get(mDeviceList.get(i)));
            avgList.add(mDeviceList.get(i) + " : " + mAverageDataMap.get(mDeviceList.get(i)));
        }
        mChildList.add(maxList);
        mChildList.add(minList);
        mChildList.add(avgList);


    }

    private void setDataMap(String deviceName, String targetColumn) {
        mMaxDataMap.put(deviceName, DBCommander.getMaxData(deviceName, targetColumn));
        mMinDataMap.put(deviceName, DBCommander.getMinData(deviceName, targetColumn));
        mAverageDataMap.put(deviceName, DBCommander.getAvgData(deviceName, targetColumn));

    }

    private void getAttributeData() {

        String targetColumn;
        ArrayList<ArrayList<DBResultForm>> sensorDataList = new ArrayList<>();

        for (int i = 0; i < mDeviceList.size(); i++) {
            sensorDataList.add(DBCommander.getSensorDataListTimeCondition(mDeviceList.get(i), mEndDateString, mStartDateString));
            if (mElement.equals(sensorDataList.get(i).get(i).getSensorName_1())) {
                targetColumn = "DATA_1";
                setDataMap(mDeviceList.get(i), targetColumn);

            } else if (mElement.equals(sensorDataList.get(i).get(i).getSensorName_2())) {
                targetColumn = "DATA_2";
                setDataMap(mDeviceList.get(i), targetColumn);

            } else if (mElement.equals(sensorDataList.get(i).get(i).getSensorName_3())) {
                targetColumn = "DATA_3";
                setDataMap(mDeviceList.get(i), targetColumn);

            } else if (mElement.equals(sensorDataList.get(i).get(i).getSensorName_4())) {
                targetColumn = "DATA_4";
                setDataMap(mDeviceList.get(i), targetColumn);

            }
        }


    }

    public void drawSingleBarGraph() {
        Random mColorRandom = new Random();
        List<BarEntry> entries = new ArrayList<BarEntry>();
        ArrayList<DBResultForm> sensorDataList;

        sensorDataList = DBCommander.getSensorDataListTimeCondition(mDeviceList.get(0), mEndDateString, mStartDateString);

        for (int i = 0; i < sensorDataList.size(); i++) {
            if (mElement.equals(sensorDataList.get(i).getSensorName_1())) {
                entries.add(new BarEntry(i, (float) sensorDataList.get(i).getSensorData_1()));
            } else if (mElement.equals(sensorDataList.get(i).getSensorName_2())) {
                entries.add(new BarEntry(i, (float) sensorDataList.get(i).getSensorData_2()));
            } else if (mElement.equals(sensorDataList.get(i).getSensorName_3())) {
                entries.add(new BarEntry(i, (float) sensorDataList.get(i).getSensorData_3()));
            } else if (mElement.equals(sensorDataList.get(i).getSensorName_4())) {
                entries.add(new BarEntry(i, (float) sensorDataList.get(i).getSensorData_4()));
            }

        }
        BarDataSet barDataSet = new BarDataSet(entries, mElement);
        barDataSet.setColor(Color.rgb(mColorRandom.nextInt(255), mColorRandom.nextInt(255), mColorRandom.nextInt(127)));
        BarData data = new BarData(barDataSet);

        mChart.setData(data);
        mChart.invalidate();

    }

    public void drawMultiBarGraph() {
        Random mColorRandom = new Random();
        ArrayList<ArrayList<BarEntry>> mBarEntryList = new ArrayList<>();
        ArrayList<ArrayList<DBResultForm>> sensorDataList = new ArrayList<>();
        ArrayList<BarDataSet> mBarDataSetList = new ArrayList<>();


        for (int i = 0; i < mDeviceList.size(); i++) {
            mBarEntryList.add(new ArrayList<BarEntry>());
            sensorDataList.add(DBCommander.getSensorDataListTimeCondition(mDeviceList.get(i), mEndDateString, mStartDateString));

            for (int j = 0; j < sensorDataList.get(i).size(); j++) {

                if (mElement.equals(sensorDataList.get(i).get(j).getSensorName_1())) {
                    mBarEntryList.get(i).add(new BarEntry(j, (float) sensorDataList.get(i).get(j).getSensorData_1()));

                } else if (mElement.equals(sensorDataList.get(i).get(j).getSensorName_2())) {
                    mBarEntryList.get(i).add(new BarEntry(j, (float) sensorDataList.get(i).get(j).getSensorData_2()));

                } else if (mElement.equals(sensorDataList.get(i).get(j).getSensorName_3())) {
                    mBarEntryList.get(i).add(new BarEntry(j, (float) sensorDataList.get(i).get(j).getSensorData_3()));

                } else if (mElement.equals(sensorDataList.get(i).get(j).getSensorName_4())) {
                    mBarEntryList.get(i).add(new BarEntry(j, (float) sensorDataList.get(i).get(j).getSensorData_4()));

                }

            }
        }

        for (int dataSetIndex = 0; dataSetIndex < mDeviceList.size(); dataSetIndex++) {
            mBarDataSetList.add(new BarDataSet(mBarEntryList.get(dataSetIndex), mDeviceList.get(dataSetIndex)));
            if (dataSetIndex % 2 == 0) {
                mBarDataSetList.get(dataSetIndex).setColor(Color.rgb(mColorRandom.nextInt(255), mColorRandom.nextInt(255), mColorRandom.nextInt(127)));
            } else {
                mBarDataSetList.get(dataSetIndex).setColor(Color.rgb(mColorRandom.nextInt(127), mColorRandom.nextInt(255), mColorRandom.nextInt(255)));
            }
            mDataSets.add(mBarDataSetList.get(dataSetIndex));
        }
        BarData data = new BarData(mDataSets);
        data.setValueFormatter(new LargeValueFormatter());


        //Typeface mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        // add space between the dataset groups in percent of bar-width
        //data.setValueTypeface(mTfLight);

        float groupSpace = 0.04f;
        float barSpace = 0.02f; // x3 dataset
        float barWidth = 0.3f; // x3 dataset

        mChart.setData(data);
        //mChart.groupBars((float)sensorDataList.get(0).get(0).getSensorData_1(), groupSpace, barSpace);
        mChart.groupBars(0, groupSpace, barSpace);

        mChart.invalidate();
        mChart.notifyDataSetChanged();
    }


}
