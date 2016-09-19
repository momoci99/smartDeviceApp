package fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.notemel.deviceappalphav02.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import adapter.SensorDataListReAdapter;
import db.DBHandler;
import format.DBResultForm;

/**
 * Created by Melchior_S on 2016-09-18.
 */
public class SensorDataTableFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter ;
    private RecyclerView.LayoutManager mLayoutManager;


    TextView mTV_sname1;
    TextView mTV_sname2;
    TextView mTV_sname3;
    TextView mTV_sname4;

    TextView boardVerTV;


    String mSelectedDevice;



    static Context mContext;

    DBHandler mDBHandler = DBHandler.getInstance();
    CopyOnWriteArrayList<DBResultForm> sensorDataList;

    public SensorDataTableFragment(){}

    public void setDeviceName(String deviceName)
    {
        mSelectedDevice = deviceName;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensordata_table, container, false);


        mContext =  view.getContext();
        sensorDataList = mDBHandler.getSensorDataList(mSelectedDevice);



        mTV_sname1 = (TextView)view.findViewById(R.id.tv_sname1);
        mTV_sname2 = (TextView)view.findViewById(R.id.tv_sname2);
        mTV_sname3 = (TextView)view.findViewById(R.id.tv_sname3);
        mTV_sname4 = (TextView)view.findViewById(R.id.tv_sname4);

        mTV_sname1.setText(sensorDataList.get(0).getSensorName_1());
        mTV_sname2.setText(sensorDataList.get(0).getSensorName_2());
        mTV_sname3.setText(sensorDataList.get(0).getSensorName_3());
        mTV_sname4.setText(sensorDataList.get(0).getSensorName_4());







        mRecyclerView = (RecyclerView)view.findViewById(R.id.ry_sdata_view);





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


        return view;
    }
    //TODO: 정해진 갯수만큼 레코드 로드하는 쿼리 및 처리 코드 그리고 변경된 리스트 업데이트하는코드
    //TODO: 리스트는 깊은복사할것




}
