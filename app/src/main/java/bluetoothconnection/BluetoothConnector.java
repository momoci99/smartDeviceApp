package bluetoothconnection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import db.DBCommander;
import format.TransactionForm;
import parser.ParserV3;
import system.BluetoothConnectionMonitor;
import system.ConditionChecker;
import system.MainMonitor;

/**
 * Created by Melchior_S on 2016-09-02.
 */
public class BluetoothConnector {


    private final String TAG = BluetoothConnector.this.getClass().getSimpleName();

    //0xFF,0xFF 감지용 변수
    private final byte EOF = (byte) 0xFF;
    private ParserV3 mParser = new ParserV3();

    private BluetoothConnectionMonitor mBluetoothConnectionMonitor = new BluetoothConnectionMonitor();
    private ConditionChecker mConditionChecker;
    private MainMonitor mMainMonitor;

    private ArrayList<Byte> slicedBytes = new ArrayList<>();
    private ByteBuffer accByteBuffer = ByteBuffer.allocate(1024);

    private TransactionForm mTransactionForm = new TransactionForm();


    private Context mContext;

    private BluetoothDevice mBluetoothDevice;



    public BluetoothConnector(Context context)
    {
        this.mContext =context;
        mConditionChecker = new ConditionChecker(mContext);
        mMainMonitor = new MainMonitor(mContext);
    }

    /**
     * @param bluetoothDevice for Bluetooth Classic
     */
    public void initConnector(BluetoothDevice bluetoothDevice) {
        mBluetoothDevice = bluetoothDevice;
        DBCommander.createDataTable(bluetoothDevice.getName());

    }

    /**
     * for Bluetooth LE
     *
     * @param bluetoothAdapter for Bluetooth Low Energy
     * @param bluetoothDeviceAddress for Bluetooth Low Energy
     */
    public void initConnector(BluetoothAdapter bluetoothAdapter, String bluetoothDeviceAddress) {
        mBluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothDeviceAddress);
        DBCommander.createDataTable(mBluetoothDevice.getName());

    }

    public void accumulateByte(byte[] bytePiece) {
        try {
            accByteBuffer.put(bytePiece);
        } catch (Exception e) {

            e.printStackTrace();
            Log.e("accByteBuffer", accByteBuffer.position() + ":포지션");
            Log.e("bytePiece", bytePiece.length + ":길이");
        }

    }

    public void sendAliveSignalToMonitor(String bluetoothDeviceMACAddress) {
        mBluetoothConnectionMonitor.sendAliveSignalQueue(bluetoothDeviceMACAddress);

    }

    private void sendParserAndGetResult(BluetoothDevice bluetoothDevice) {
        mParser.initializeParser(slicedBytes, bluetoothDevice);
        if (mParser.checkValid()) {
            mParser.parseFrame();
            mTransactionForm.setName(mParser.getDeviceName());
            mTransactionForm.setAddress(mParser.getMACAddress());
            mTransactionForm.setFloatData(mParser.getFloatData());
            mTransactionForm.setIntData(mParser.getIntData());
            mTransactionForm.setSID(mParser.getSID());
            mTransactionForm.setBoardVer(mParser.getBoardVer());


        }
        else
        {
            mTransactionForm.setError(true);
        }
        slicedBytes.clear();
    }


    /*A ByteBuffer is normally ready for read() (or for put()).

   flip() makes it ready for write() (or for get()).

   rewind() and compact() and clear() make it ready for read()/put() again after write() (or get()).

   */
    public void parseBytesAndUpdateDB() {
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
                sendParserAndGetResult(mBluetoothDevice);
                updateTransactionToDB();
                //Log.d("compact", "good");
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
    private void updateTransactionToDB()
    {
        if(mTransactionForm!=null && !mTransactionForm.isError())
        {
            try{
                DBCommander.insertRow_SensorTable(mTransactionForm);
                dataLoger();
                mConditionChecker.checkCondition(mTransactionForm);
                mMainMonitor.sendDataToMainActivate(mTransactionForm);

                mTransactionForm.reset();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }

    private void dataLoger()
    {
        Log.d(TAG,"-----------------------------");
        Log.d(TAG,"from Loger");
        Log.d(TAG,mTransactionForm.getAddress());
        Log.d(TAG,mTransactionForm.getName());


        for (int i = 0; i < mTransactionForm.getSID().length; i++) {
            if (mTransactionForm.getIntData()[i] == -1) {
                Log.d(TAG, mTransactionForm.getSID()[i] +" : "+mTransactionForm.getFloatData()[i]);
            } else if (mTransactionForm.getFloatData()[i] == -1) {
                Log.d(TAG, mTransactionForm.getSID()[i]+" : "+mTransactionForm.getIntData()[i]);
            }
        }


        Log.d(TAG,"-----------------------------");
    }


}
