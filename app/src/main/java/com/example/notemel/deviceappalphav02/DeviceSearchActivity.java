package com.example.notemel.deviceappalphav02;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.SearchedDeviceListAdapter;

/**
 * Created by noteMel on 2016-07-03.
 */
public class DeviceSearchActivity extends AppCompatActivity {

    /*UI Components*/
    private Button searchBtn;
    private Switch bluetoothOnOffSwitch;
    private ListView searchResultListView;
    private ProgressDialog progressDlg;

    private SearchedDeviceListAdapter listAdapter;

    //찾아낸 블루투스 디바이스가 저장될 리스트
    private ArrayList<BluetoothDevice> mSearchedDeviceList = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter mBluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searcheddevice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*UI Components init*/
        bluetoothOnOffSwitch = (Switch) findViewById(R.id.sw_bluetooth);
        searchResultListView = (ListView) findViewById(R.id.lv_paired);
        searchBtn            = (Button) findViewById(R.id.bt_scan);
        listAdapter          = new SearchedDeviceListAdapter(this);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //이미 켜진상태라면 스위치가 on쪽으로 향함
        if (mBluetoothAdapter.isEnabled()) {
            bluetoothOnOffSwitch.setChecked(true);
            searchBtn.setEnabled(true);
        } else {
            bluetoothOnOffSwitch.setChecked(false);
            searchBtn.setEnabled(false);
        }

        bluetoothOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //블루투스 ON
                if (isChecked) {
                    //이미 블루투스가 켜져있으면 블루투스를 끈다.
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }

                    //블루투스가 꺼져있다면 블루투스를 활성화한다.
                    else {
                        //스캔 버튼을 활성화
                        searchBtn.setEnabled(true);

                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, 1000);
                    }
                }
                //블루투스 OFF
                else {
                    showToast("OFF");
                    mBluetoothAdapter.disable();

                    //디바이스 목록 비우기
                    mSearchedDeviceList = new ArrayList<BluetoothDevice>();

                    //디바이스 리스트 갱신
                    listAdapter.setData(mSearchedDeviceList);

                    //리스트뷰 갱신 - 이렇게 해야 갱신됨
                    listAdapter.notifyDataSetChanged();
                    searchBtn.setEnabled(false);
                }
            }
        });


        //다이얼로그 정의
        progressDlg = new ProgressDialog(this);
        progressDlg.setMessage("탐색중...");
        progressDlg.setCancelable(false);
        progressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mBluetoothAdapter.cancelDiscovery();
            }
        });

        //스캔시작
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mBluetoothAdapter.startDiscovery();
            }
        });


        //블루투스가 지원 안될때 처리
        if (mBluetoothAdapter == null) {
            showUnsupported();
        }
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);

        searchResultListView.setAdapter(listAdapter);
        searchResultListView.setOnItemClickListener(mItemClickListener);

    }
    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();


            }
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showUnsupported() {
        showToast("블루투스 미지원기기입니다.");
        bluetoothOnOffSwitch.setEnabled(false);
        searchBtn.setEnabled(false);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");


                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mSearchedDeviceList = new ArrayList<BluetoothDevice>();

                progressDlg.show();

                //검색이 종료되었음을 감지하였을때
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                //다이얼로그 종료
                progressDlg.dismiss();

                //디바이스 리스트 갱신
                listAdapter.setData(mSearchedDeviceList);

                //리스트뷰 갱신 - 이렇게 해야 갱신됨
                listAdapter.notifyDataSetChanged();

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mSearchedDeviceList.add(device);

                showToast("Found device " + device.getName());
            }
        }
    };

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long l_position) {
            //선택한 기기를 전달할 전달전용 디바이스 리스트 생성
            ArrayList<BluetoothDevice> selectedDevice = new ArrayList<BluetoothDevice>();

            //기존 디바이스 리스트중 선택된 디바이스 1개를 찾아 전달용 디바이스 리스트에 저장
            //selectedDevice 는 선택한 기기 단 하나의 정보만 가진다.
            selectedDevice.add(mSearchedDeviceList.get(position));

            Intent intent = new Intent(DeviceSearchActivity.this, SelectedDeviceInfoActivity.class);


            intent.putParcelableArrayListExtra("device.list", selectedDevice);

            startActivity(intent);

        }
    };
}
