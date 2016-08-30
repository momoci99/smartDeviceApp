package bluetoothconnection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import format.TransactionForm;
import parser.ParserV3;
import transation.TransactionMaster;

/**
 * Created by noteMel on 2016-07-06.
 */
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeConnector
        implements Runnable {
    private final static String TAG = BluetoothLeConnector.class.getSimpleName();

    //private BluetoothConnectionMonitor mBluetoothConnectionMonitor = new BluetoothConnectionMonitor();


    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";


    private ArrayList<Byte> slicedBytes = new ArrayList<>();
    private ByteBuffer accByteBuffer = ByteBuffer.allocate(1024);

    //0xFF,0xFF 감지용 변수
    private final byte EOF = (byte) 0xFF;
    private ParserV3 mParser = new ParserV3();


    private TransactionForm mTransactionForm = new TransactionForm();
    private TransactionMaster mTransactionMaster = new TransactionMaster();


    private Context mContext;

    public BluetoothLeConnector(Context context) {
        mContext = context;
    }

    public void accumulateByte(byte[] bytePiece) {

        try {
            accByteBuffer.put(bytePiece);
            //Log.d("accumulateByte","포지션 : " + accByteBuffer.position());
        } catch (Exception e) {

            e.printStackTrace();
            Log.e("accByteBuffer", accByteBuffer.position() + ":포지션");
            Log.e("bytePiece", bytePiece.length + ":길이");
        }

        //System.arraycopy(bytePiece, 0, accByteArray, 0, bytePiece.length);
    }

    public void parseBytes() {
        /*되는지확인 - get 작업후 compact 사용하고 get 작업전에는 flip 사용*/

        //Log.d("CopyEOF","good");
        boolean isFirstEOF = false;
        int distanceEOF2EOF = 0;
        accByteBuffer.flip();


        for (int i = 0; i < accByteBuffer.limit(); i++) {
            Byte data = accByteBuffer.get();
            if ((data.compareTo(EOF) == 0) && distanceEOF2EOF == 1) {
                distanceEOF2EOF = 0;
                isFirstEOF = false;
                slicedBytes.add(data);
                sendParserAndGetResult();

                Log.d("compact", "good");
                break;
            } else if ((data.compareTo(EOF) != 0) == (isFirstEOF == true)) {
                distanceEOF2EOF++;
                slicedBytes.add(data);
            } else if (data.compareTo(EOF) == 0) {
                isFirstEOF = true;
                slicedBytes.add(data);
            } else {
                slicedBytes.add(data);
            }
        }
        accByteBuffer.compact(); //get 사용후 compact 호출
    }

    public void sendParserAndGetResult() {

        //mParser.setReceivedFrame(slicedBytes);
        Log.d("slicedBytes", slicedBytes.size() + "");

        mParser.initializeParser(slicedBytes, mBluetoothAdapter.getRemoteDevice(mBluetoothDeviceAddress));
        if (mParser.checkValid()) {
            Log.d(TAG, "파서 good");

            mParser.parseFrame();
            mTransactionForm.setName(mParser.getDeviceName());
            mTransactionForm.setAddress(mParser.getMACAddress());
            mTransactionForm.setFloatData(mParser.getFloatData());
            mTransactionForm.setIntData(mParser.getIntData());
            mTransactionForm.setSID(mParser.getSID());
            mTransactionMaster.offerQueue(mTransactionForm);
            mTransactionForm.reset();
        }
        slicedBytes.clear();
    }

    public final static UUID BLE_DEVICE_CHARACTERISTIC_UUID =
            UUID.fromString(PresetUUIDList.BLE_DEVICE_CHARACTERISTIC_UUID);

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }


        //특성을 읽을때 발생하는 콜백
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e("찾은 uuid", characteristic.getUuid().toString());
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }


        //노티피케이션(특성의 밸류값 변경 감지)가 여기서 처리됨
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        mContext.sendBroadcast(intent);
    }


    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        //Log.d(TAG, "브로드캐스트 업데이트");
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (BLE_DEVICE_CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {

            final byte[] heartRate = characteristic.getValue();
            Log.d(TAG, "" + heartRate.length);
            accumulateByte(heartRate);
            parseBytes();
            sendAliveSignal();
        }
        mContext.sendBroadcast(intent);
    }

    @Override
    //run 코드가 비워져 있으면 쓰레드가 바로 죽는다.
    public void run() {
        long currentMillis;
        long previousMillis = 0;
        while (true) {
            currentMillis = System.currentTimeMillis();
            if (currentMillis - previousMillis >= 2000) {
                Log.d(TAG, "Alive!");
                previousMillis = currentMillis;
            }
        }
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.

        //핸들러로 처리하자.
        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);

        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        //해당 특성에 대한 디스크럽터를 등록해서 뭐하는지 모르겠다. 일단 등록은 하는듯
        if (BLE_DEVICE_CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor
                    = characteristic.getDescriptor(UUID.fromString(PresetUUIDList.CLIENT_CHARACTERISTIC_CONFIG));

            for (BluetoothGattDescriptor descriptors : characteristic.getDescriptors()) {
                Log.e(TAG, "BluetoothGattDescriptor: " + descriptors.getUuid().toString());
            }

            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

    public void sendAliveSignal() {
        //mBluetoothConnectionMonitor.sendAliveSignalQueue(mBluetoothDeviceAddress);

    }
}
