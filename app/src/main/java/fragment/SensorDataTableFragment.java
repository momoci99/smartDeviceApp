package fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.notemel.deviceappalphav02.R;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import adapter.SensorDataListReAdapter;
import db.DBCommander;
import format.DBResultForm;

/**
 * Created by Melchior_S on 2016-09-18.
 */
public class SensorDataTableFragment extends Fragment {

    private final String TAG = "SDTableFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    TextView mTV_sname1;
    TextView mTV_sname2;
    TextView mTV_sname3;
    TextView mTV_sname4;
    TextView mTV_currentPosition;

    Button mBTN_showNextSensorData;
    Button mBTN_showPrevSensorData;

    String mSelectedDevice;


    static Context mContext;


    private ArrayList<DBResultForm> sensorDataList;

    private int mOffset = 0;
    private int mRowCountValue = 20;
    private long mSensorDataRowCount = 0;


    public SensorDataTableFragment() {
    }

    public void setDeviceName(String deviceName) {
        mSelectedDevice = deviceName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensordata_table, container, false);


        mContext = view.getContext();
        sensorDataList = DBCommander.getSensorDataList(mSelectedDevice, String.valueOf(mRowCountValue), String.valueOf(mOffset));


        mTV_sname1 = (TextView) view.findViewById(R.id.tv_sname1);
        mTV_sname2 = (TextView) view.findViewById(R.id.tv_sname2);
        mTV_sname3 = (TextView) view.findViewById(R.id.tv_sname3);
        mTV_sname4 = (TextView) view.findViewById(R.id.tv_sname4);
        mTV_currentPosition = (TextView) view.findViewById(R.id.tv_total_row_count);


        mTV_sname1.setText(sensorDataList.get(0).getSensorName_1());
        mTV_sname2.setText(sensorDataList.get(0).getSensorName_2());
        mTV_sname3.setText(sensorDataList.get(0).getSensorName_3());
        mTV_sname4.setText(sensorDataList.get(0).getSensorName_4());

        mSensorDataRowCount = DBCommander.getSensorDataRowCount(mSelectedDevice);


        mTV_currentPosition.setText(String.valueOf(mSensorDataRowCount));

        mBTN_showNextSensorData = (Button) view.findViewById(R.id.btn_next_sdata);
        mBTN_showPrevSensorData = (Button) view.findViewById(R.id.btn_prev_sdata);

        mBTN_showPrevSensorData.setEnabled(false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.ry_sdata_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        mAdapter = new SensorDataListReAdapter(sensorDataList);
        mRecyclerView.setAdapter(mAdapter);

        //mAdapter.notifyDataSetChanged();

        //Show Next Rows
        mBTN_showNextSensorData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBTN_showPrevSensorData.setEnabled(true);
                mOffset += mRowCountValue;

                sensorDataList.clear();
                sensorDataList.addAll(DBCommander.getSensorDataList(mSelectedDevice, String.valueOf(mRowCountValue), String.valueOf(mOffset)));
                mAdapter.notifyDataSetChanged();

                if (mOffset >= mSensorDataRowCount) {
                    mBTN_showNextSensorData.setEnabled(false);
                }
            }
        });

        //Show Prev Rows
        mBTN_showPrevSensorData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBTN_showNextSensorData.setEnabled(true);
                mOffset -= mRowCountValue;
                sensorDataList.clear();
                sensorDataList.addAll(DBCommander.getSensorDataList(mSelectedDevice, String.valueOf(mRowCountValue), String.valueOf(mOffset)));
                mAdapter.notifyDataSetChanged();

                if (mOffset == 0) {
                    mBTN_showPrevSensorData.setEnabled(false);
                }
            }
        });

        return view;
    }
}
