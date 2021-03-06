package com.example.notemel.deviceappalphav02;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

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

    private RecyclerView mRV_deviceList;
    private RecyclerView.Adapter mAdapter_DeviceList;
    private RecyclerView.LayoutManager mLayoutManager_DeviceList;

    private Button mBTN_ShowChart;

    private EditText mEdt_startDate;
    private EditText mEdt_endDate;

    private String mStartDateString;
    private long startMils = 0;
    private int startYear;
    private int startMonth;
    private int startDate;

    private String mEndDateString;

    private long minimumMilliseconds = Long.MAX_VALUE;
    private long maximumMilliseconds = Long.MIN_VALUE;

    private ArrayList<String> mColumnsList = new ArrayList<>();
    private String[] mColumnsArray;

    private ArrayList<String> mDeviceList = new ArrayList<>();
    private ArrayList<String> mChartOptionList = new ArrayList<>();

    private String mIntentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_option);

        mIntentKey = getResources().getString(R.string.ToChartActivity);

        mEdt_startDate = (EditText) findViewById(R.id.edt_start_date_input);
        mEdt_endDate = (EditText) findViewById(R.id.edt_end_date_input);


        mRecyclerView = (RecyclerView) findViewById(R.id.ry_element_list);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mColumnsArray = DBCommander.getSensorElementData();

        for (int i = 0; i < mColumnsArray.length; i++) {
            mColumnsList.add(mColumnsArray[i]);
        }


        mRV_deviceList = (RecyclerView) findViewById(R.id.ry_chart_device_name_list);
        mLayoutManager_DeviceList = new LinearLayoutManager(this);
        mRV_deviceList.setLayoutManager(mLayoutManager_DeviceList);

        mDeviceList.addAll(DBCommander.getFullDeviceList());
        Log.e(TAG, "mDeviceList 사이즈 : " + mDeviceList.size());


        mAdapter_ElementList = new ElementSelectListReAdapter(mColumnsList);
        mRecyclerView.setAdapter(mAdapter_ElementList);

        mAdapter_DeviceList = new ChartDeviceNameListReAdapter(mDeviceList);
        mRV_deviceList.setAdapter(mAdapter_DeviceList);


        mBTN_ShowChart = (Button) findViewById(R.id.btn_show_chart);
        mBTN_ShowChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedElement = ElementSelectListReAdapter.mSelectedElement;
                ArrayList selectedDeviceList = ChartDeviceNameListReAdapter.mSelectedDeviceList;
                if (!selectedElement.equals("") && selectedDeviceList.size() > 0) {

                    /*
                    mChartOptionList
                    Index 0 - Element
                    Index 1 - StartDate
                    Index 2 - EndDate
                    Index 3~ - DeviceList
                     */
                    mChartOptionList.add(ElementSelectListReAdapter.mSelectedElement);
                    mChartOptionList.add(mStartDateString);
                    mChartOptionList.add(mEndDateString);
                    mChartOptionList.addAll(ChartDeviceNameListReAdapter.mSelectedDeviceList);

                    //check empty list
                    //prevent draw empty chart
                    if (mDeviceList.size() == 1) {

                        if (DBCommander.getSensorDataListTimeCondition(mDeviceList.get(0), mEndDateString, mStartDateString).size() == 0) {
                            showAlertEmptyList();
                            //mChartOptionList.clear();
                        } else {
                            startChartActivity();
                        }
                    } else {
                        boolean isEmpty = false;
                        for (int i = 0; i < mDeviceList.size(); i++) {
                            if (DBCommander.getSensorDataListTimeCondition(mDeviceList.get(i), mEndDateString, mStartDateString).size() == 0) {
                                showAlertEmptyList();
                                isEmpty = true;
                            }
                        }
                        if (!isEmpty) {
                            startChartActivity();
                            //mChartOptionList.clear();
                        }

                    }
                    mChartOptionList.clear();

                }


            }
        });
        mEdt_startDate.setFocusableInTouchMode(false);
        mEdt_endDate.setFocusableInTouchMode(false);
        mEdt_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> timeList = new ArrayList<>();


                for (int i = 0; i < mDeviceList.size(); i++) {
                    timeList.add(DBCommander.getFirstSensorRecordTime(mDeviceList.get(i)));
                    timeList.add(DBCommander.getLastSensorRecordTime(mDeviceList.get(i)));

                }

                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                Date timeDate = null;
                Date minimumDate = null;

                for (int j = 0; j < timeList.size(); j++) {
                    try {

                        timeDate = timeFormat.parse(timeList.get(j));
                        long convertTime = timeDate.getTime();
                        if (maximumMilliseconds <= convertTime) {
                            maximumMilliseconds = convertTime;
                        }
                        if (minimumMilliseconds >= convertTime) {
                            minimumMilliseconds = convertTime;
                            minimumDate = timeDate;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(minimumDate);

                int startYear = calendar.get(calendar.YEAR);
                int startMonth = (calendar.get(calendar.MONTH) + 1);
                int startDate = calendar.get(calendar.DATE);

                DatePickerDialog dialog =
                        new DatePickerDialog(ChartOptionSelectActivity.this,
                                startDatePickerListener, startYear, startMonth, startDate);

                //TODO : 장치별 기한설정할것.
                dialog.getDatePicker().setMinDate(minimumMilliseconds);
                dialog.getDatePicker().setMaxDate(maximumMilliseconds);

                dialog.show();
            }
        });


        mEdt_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startMils != 0) {
                    DatePickerDialog dialog = new DatePickerDialog(ChartOptionSelectActivity.this, endDatePickerListener, startYear, startMonth, startDate);
                    dialog.getDatePicker().setMinDate(startMils);
                    dialog.getDatePicker().setMaxDate(maximumMilliseconds);

                    dialog.show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ChartOptionSelectActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("Please Input Start Day First");
                    alert.show();
                }

            }

        });
    }

    private DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
            mEdt_startDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            try {
                Date startDateObj;
                startDateObj = startDateFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);


                startMils = startDateObj.getTime();
                startYear = year;
                startMonth = monthOfYear + 1;
                startDate = dayOfMonth;
                mStartDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(startDateObj);
                mStartDateString += " 00:00:01";


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };
    private DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
            mEdt_endDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            try {
                Date endDateObj;
                endDateObj = startDateFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                mEndDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(endDateObj);
                mEndDateString += " 23:59:59";

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };

    public void showAlertEmptyList() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ChartOptionSelectActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //finish();
            }
        });
        alert.setMessage("Please Check Period (Empty List)");
        alert.show();

    }

    private void startChartActivity() {
        Intent intent = new Intent(ChartOptionSelectActivity.this, ChartActivity.class);
        intent.putStringArrayListExtra(mIntentKey, mChartOptionList);
        startActivity(intent);


        mChartOptionList.clear();
    }
}
