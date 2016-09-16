package bluetoothconnection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import db.DBHandler;
import format.TransactionForm;
import parser.ParserV3;
import system.BluetoothConnectionMonitor;
import transation.TransactionMaster;

/**
 * Created by Melchior_S on 2016-09-02.
 */
public class Connector {

    //0xFF,0xFF 감지용 변수
    private final byte EOF = (byte) 0xFF;
    private ParserV3 mParser = new ParserV3();

    private BluetoothConnectionMonitor mBluetoothConnectionMonitor = new BluetoothConnectionMonitor();

    private ArrayList<Byte> slicedBytes = new ArrayList<>();
    private ByteBuffer accByteBuffer = ByteBuffer.allocate(1024);

    private TransactionForm mTransactionForm = new TransactionForm();
    private TransactionMaster mTransactionMaster = new TransactionMaster();

    private Context mContext;

    private BluetoothDevice mBluetoothDevice;

    private DBHandler mDBHandler = DBHandler.getInstance();

    /**
     * @param bluetoothDevice for Bluetooth Classic
     */
    public void initConnector(BluetoothDevice bluetoothDevice) {
        mBluetoothDevice = bluetoothDevice;
        mDBHandler.createDataTable(bluetoothDevice.getName());

    }

    /**
     * for Bluetooth LE
     *
     * @param bluetoothAdapter
     * @param bluetoothDeviceAddress
     */
    public void initConnector(BluetoothAdapter bluetoothAdapter, String bluetoothDeviceAddress) {
        mBluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothDeviceAddress);
        mDBHandler.createDataTable(mBluetoothDevice.getName());

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
            mTransactionMaster.offerQueue(mTransactionForm);

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
        mDBHandler.insertRow_SensorTable(mTransactionForm);
        mTransactionForm.reset();
    }

}
