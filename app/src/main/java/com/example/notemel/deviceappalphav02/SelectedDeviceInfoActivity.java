package com.example.notemel.deviceappalphav02;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bluetoothconnection.BlueToothSocketConnector;
import bluetoothconnection.BluetoothLeConnector;
import bluetoothconnection.PresetUUIDList;
import system.ThreadManager;


/**
 * Created by Melchior_S on 2016-05-11.
 *
 * DeviceConnect 에서 선택한 장치의 정보를 보여주는 액티비티
 * 해당 기기와의 페어링 설정
 * 헤당 기기와의 연결 설정
 */

//콜백 일어나지 않던 원인 : UUID 불일치
public class SelectedDeviceInfoActivity extends AppCompatActivity {

    private final String TAG = "선택된 디바이스 액티비티";

    private ArrayList<BluetoothDevice> mSelectedDevice;
    private BluetoothDevice mTargetDevice;

    private TextView deviceNameTv;
    private TextView deviceAddressTv;
    private Button startConnectionBtn;
    private Button startPairBtn;
    private Button testButton;

    //통신을 위한 객체
    private BlueToothSocketConnector mSocketConnector;
    private BluetoothLeConnector mBluetoothLeConnector;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private ThreadManager mThreadManager;

    private SignalReceiverHandler mSignalReceiverHandler;


    //Signal
    private final int CONNECT_SUCCESS = 1;

    private final int CONNECT_FAIL = -1;

    private ProgressDialog mProgressDialog;

    private boolean mConnected = false;

    private List<BluetoothGattService> mReceivedGATTserviceList = new ArrayList<>();
    private Context mContext;

    public final static UUID BLE_DEVICE_CHARACTERISTIC_UUID =
            UUID.fromString(PresetUUIDList.BLE_DEVICE_CHARACTERISTIC_UUID);
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeConnector.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                //updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeConnector.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                //updateConnectionState(R.string.disconnected);
                //invalidateOptionsMenu();
                //clearUI();
            } else if (BluetoothLeConnector.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.

                //현재 현결가능한 GATT서비스 가져오기 ㅇㅎ
                //displayGattServices(mBluetoothLeConnector.getSupportedGattServices());
                mReceivedGATTserviceList = mBluetoothLeConnector.getSupportedGattServices();
                setNotification();
                Log.d("받은 서비스","GATT서비스 가져옴");

            } else if (BluetoothLeConnector.ACTION_DATA_AVAILABLE.equals(action)) {
                //displayData(intent.getStringExtra(BluetoothLeConnector.EXTRA_DATA));
                //Log.d("받은 데이터",intent.getStringExtra(BluetoothLeConnector.EXTRA_DATA));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecteddevice);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSelectedDevice = getIntent().getExtras().getParcelableArrayList("device.list");

        deviceNameTv     = (TextView)findViewById(R.id.tv_device_name);
        deviceAddressTv  = (TextView)findViewById(R.id.tv_data);
        startConnectionBtn     = (Button)findViewById(R.id.btn_connect);
        startPairBtn        = (Button)findViewById(R.id.btn_pair);
        testButton = (Button)findViewById(R.id.btn_bletest);


        mTargetDevice = mSelectedDevice.get(0);

        deviceNameTv.setText(mTargetDevice.getName());
        deviceAddressTv.setText(mTargetDevice.getAddress());

        mSignalReceiverHandler = new SignalReceiverHandler();
        mContext = getApplicationContext();
        mBluetoothLeConnector = new BluetoothLeConnector(mContext);





        //페어링했던 기기인지 체크
        if(mTargetDevice.getBondState() == BluetoothDevice.BOND_BONDED )
        {
            //페어링한 기기면 커넥트 버튼 활성화
            //페어링 한 기기이기 떄문에 Pair 버튼을 'Unpair'로 표시
            startPairBtn.setText("Unpair");
            startConnectionBtn.setEnabled(true);
        }
        else
        {
            //커넥트 버튼 비활성화
            //페어링 하지 않은 기기이기 떄문에 Pair 버튼을 'Unpair'로 표시
            startPairBtn.setText("Pair");
            startConnectionBtn.setEnabled(false);
        }


        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        startConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mTargetDevice.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC)
                {
                    if (mTargetDevice.getBondState() == BluetoothDevice.BOND_BONDED) {

                        //이미 연결된 기기인지 확인. 이미 연결된 기기가 아니면 연결된 장치목록에 추가
                        // if(!mConnectionInfo.isDuplicatedDevice(mTargetDevice)) {
                        boolean isNormalConnection = true;
                        mSocketConnector = new BlueToothSocketConnector();
                        mSocketConnector.configConnection(mTargetDevice,mSignalReceiverHandler,isNormalConnection);

                        mThreadManager = ThreadManager.getInstance();
                        mThreadManager.ActiveThread(mSocketConnector);

                        mProgressDialog = new ProgressDialog(SelectedDeviceInfoActivity.this);
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setMessage(mTargetDevice.getName().toString() + "과(와) 연결중입니다.");
                        mProgressDialog.show();
                    }
                }
                else if(mTargetDevice.getType() == BluetoothDevice.DEVICE_TYPE_LE)
                {
                    showToast("BLE장치 입니다.");
                    if(mBluetoothLeConnector !=null )
                    {
                        setBLEConnection();
                    }
                }

            }
        });

        startPairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTargetDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    unpairDevice(mTargetDevice);
                } else {
                    showToast("Pairing...");

                    pairDevice(mTargetDevice);
                }
            }
        });

        //페어링용 리시버 등록
        registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));

    }

    public boolean setBLEConnection()
    {
        boolean isConnected = false;
        if (!mBluetoothLeConnector.initialize()) {
            Log.e("연결 실패여", "Unable to initialize Bluetooth");
            isConnected = false;

        }
        // Automatically connects to the device upon successful start-up initialization.
        if(mBluetoothLeConnector.connect(mTargetDevice.getAddress()))
        {
            isConnected = true;
        }
        return isConnected;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mPairReceiver);
        Log.e(TAG,"onDestroy");
        super.onDestroy();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            //BluetoothDevice의 메서드를 써먹겠다
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);


                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    showToast("Paired");
                    startPairBtn.setText("Unpair");
                    startConnectionBtn.setEnabled(true);

                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    showToast("Unpaired");
                    startPairBtn.setText("Pair");
                    startConnectionBtn.setEnabled(false);
                }


            }
        }
    };
    private class SignalReceiverHandler extends Handler
    {

        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what){
                case 0:
                    break;
                case CONNECT_SUCCESS:
                    mProgressDialog.dismiss();
                    showToast("연결완료");
                    break;
                case CONNECT_FAIL:
                    mProgressDialog.dismiss();
                    showToast("연결을 실패하였습니다. 장치연결을 다시확인해주세요.");
                    break;
                default:
                    break;

            }

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeConnector != null) {
            final boolean result = mBluetoothLeConnector.connect(mTargetDevice.getAddress().toString());
            Log.d("커텍션 리퀘스트 결과", "Connect request result=" + result);
        }
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeConnector.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeConnector.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeConnector.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeConnector.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public void setNotification()
    {
        UUID targetUUID = BLE_DEVICE_CHARACTERISTIC_UUID;
        BluetoothGattCharacteristic target = null;
        for(int i = 0; i< mReceivedGATTserviceList.size(); i++)
        {
            target =  mReceivedGATTserviceList.get(i).getCharacteristic(targetUUID);
        }
        if (target != null) {

            //현재 선택한 특성을 가져옴
            BluetoothGattCharacteristic characteristic = target;

            //특성의 속성값 가져옴
            int charaProp = characteristic.getProperties();

            //Read | Notify = 18
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) == 18) {
                mNotifyCharacteristic = characteristic; //노티 특성 변수에 해당 특성을 저장
                mBluetoothLeConnector.setCharacteristicNotification(characteristic, true);//노티설정루틴으로 넘어감
            }
        }

    }


}
