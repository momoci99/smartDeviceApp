package com.example.notemel.deviceappalphav02;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import db.DBHandler;
import format.DBResultForm;

/**
 * Created by Melchior_S on 2016-09-06.
 */
public class DetailDeviceInfo extends AppCompatActivity {

    TableLayout sensorData_tbl;

    TextView boardVerTV;



    String mSelectedDevice;
    String mIntentKey;

    String mNum="index";
    String sensor1="s1";
    String data1 = "data1";

    String sensor2="s2";
    String data2 = "data2";

    String sensor3="s3";
    String data3 = "data3";

    String sensor4="s4";
    String data4 = "data4";


    DBHandler mDBHandler = DBHandler.getInstance();
    CopyOnWriteArrayList<DBResultForm> sensorDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentKey= this.getResources().getString(R.string.ToDetailDeviceInfo);
        mSelectedDevice = getIntent().getStringExtra(mIntentKey);

        setContentView(R.layout.activity_detaildeviceinfo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mSelectedDevice + " Info");
        setSupportActionBar(toolbar);


        boardVerTV = (TextView)findViewById(R.id.tv_board_ver);




        initTableLayout();
        loadSensorDataAndShowBoardVer();
    }
    private void loadSensorDataAndShowBoardVer()
    {

        sensorDataList = mDBHandler.getSensorDataList(mSelectedDevice);

        if(sensorDataList.size()>0)
        {
            sensorDataList.get(0).getBoardVer();
        }

        for(int i =0; i<sensorDataList.size(); i++)
        {
            TableRow dataRow = new TableRow(this);
            TextView logNum = new TextView(this);
            logNum.setText(String.valueOf(sensorDataList.get(i).getLogNum()));

            //
            TextView sensor_1 = new TextView(this);
            sensor_1.setText(sensorDataList.get(i).getSensorName_1());

            TextView data_1 = new TextView(this);
            String d1_format = String.format(Locale.US,"%.2f",sensorDataList.get(i).getSensorData_1());
            data_1.setText(String.valueOf(d1_format));

            //


            TextView sensor_2 = new TextView(this);
            sensor_2.setText(sensorDataList.get(i).getSensorName_2());

            TextView data_2 = new TextView(this);
            String d2_format = String.format(Locale.US,"%.2f",sensorDataList.get(i).getSensorData_2());
            data_2.setText(String.valueOf(d2_format));



            TextView sensor_3 = new TextView(this);
            sensor_3.setText(sensorDataList.get(i).getSensorName_3());

            TextView data_3 = new TextView(this);
            String d3_format = String.format(Locale.US,"%.2f",sensorDataList.get(i).getSensorData_3());
            data_3.setText(String.valueOf(d3_format));


            TextView sensor_4 = new TextView(this);
            sensor_4.setText(sensorDataList.get(i).getSensorName_4());

            TextView data_4 = new TextView(this);
            String d4_format = String.format(Locale.US,"%.2f",sensorDataList.get(i).getSensorData_4());
            data_4.setText(String.valueOf(d4_format));

            TextView time = new TextView(this);
            time.setText(String.valueOf(sensorDataList.get(i).getTime()));

            dataRow.addView(logNum);
            dataRow.addView(sensor_1);
            dataRow.addView(data_1);
            dataRow.addView(sensor_2);
            dataRow.addView(data_2);
            dataRow.addView(sensor_3);
            dataRow.addView(data_3);
            dataRow.addView(sensor_4);
            dataRow.addView(data_4);
            dataRow.addView(time);
            sensorData_tbl.addView(dataRow);


        }
    }

    private void initTableLayout()
    {
        sensorData_tbl = (TableLayout)findViewById(R.id.tbl_sensordata);
        TableRow testRow0 = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText(mNum);
        tv0.setTextColor(Color.BLUE);
        testRow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText(sensor1);
        tv1.setTextColor(Color.BLUE);
        testRow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(data1);
        tv2.setTextColor(Color.BLUE);
        testRow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(sensor2);
        tv3.setTextColor(Color.BLUE);
        testRow0.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(data2);
        tv4.setTextColor(Color.BLUE);
        testRow0.addView(tv4);

        TextView tv5 = new TextView(this);
        tv5.setText(sensor3);
        tv5.setTextColor(Color.BLUE);
        testRow0.addView(tv5);

        TextView tv6 = new TextView(this);
        tv6.setText(data3);
        tv6.setTextColor(Color.BLUE);
        testRow0.addView(tv6);

        TextView tv7 = new TextView(this);
        tv7.setText(sensor4);
        tv7.setTextColor(Color.BLUE);
        testRow0.addView(tv7);

        TextView tv8 = new TextView(this);
        tv8.setText(data4);
        tv8.setTextColor(Color.BLUE);
        testRow0.addView(tv8);

        sensorData_tbl.addView(testRow0);
    }
}
