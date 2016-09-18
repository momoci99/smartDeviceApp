package bluetoothconnection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;

import format.TransactionForm;
import parser.ParserV3;


/**
 * Created by noteMel on 2016-07-04.
 */
public class BlueToothClassic extends BluetoothConnector implements Runnable {

    //통신을 위한 객체

    static String TAG = "블루투스소켓커넥터";

    private final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothSocket mBluetoothSocket = null;
    private BluetoothDevice mBluetoothDevice = null;

    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;


    private byte[] mReadBuffer = new byte[1024];
    private int mReadBufferPosition;
    private int mReceivedFrameLength = 0;


    private ArrayList<Byte> slicedBytes = new ArrayList<>();
    private ByteBuffer accByteBuffer = ByteBuffer.allocate(1024);


    //0xFF,0xFF 감지용 변수
    private final byte EOF = (byte) 0xFF;


    private ParserV3 mParser = new ParserV3();
    private TransactionForm mTransactionForm = new TransactionForm();

    private Handler mTargetActivityHandler = null;



    private boolean isNormalConnection = false;

    private final int CONNECT_SUCCESS = 1;
    private final int CONNECT_FAIL = -1;


    public void configConnection(BluetoothDevice targetDevice, Handler signalReceiverHandler, boolean isNormalConnection) {
        setBluetoothDevice(targetDevice);
        setTargetActivityHandler(signalReceiverHandler);
        setIsNormalConnection(isNormalConnection);
    }

    //for repaired connection
    public void configConnection(BluetoothDevice targetDevice) {
        setBluetoothDevice(targetDevice);
        setTargetActivityHandler(null);
        setIsNormalConnection(false);
    }

    public void setBluetoothDevice(BluetoothDevice mBluetoothDevice) {
        this.mBluetoothDevice = mBluetoothDevice;
    }

    public void setTargetActivityHandler(Handler mTargetActivityHandler) {
        this.mTargetActivityHandler = mTargetActivityHandler;
    }

    public void setIsNormalConnection(boolean isNormalConnection) {
        this.isNormalConnection = isNormalConnection;
    }

    public boolean createConnection() {
        boolean isSuccess = false;

        isSuccess = setupSocket();
        if (isSuccess) {
            isSuccess = getStream();
            if (isSuccess) {
                if (isNormalConnection) {
                    SendSignalToActivity(CONNECT_SUCCESS);
                }
            }
        }
        return isSuccess;
    }


    public boolean setupSocket() {
        boolean isSuccess = false;
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(mUUID);
            mBluetoothSocket.connect();
            isSuccess = true;
        } catch (IOException e) {
            Log.e(TAG, "setupSocket 에러", e);
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean getStream() {
        boolean isSuccess = false;
        try {
            mOutputStream = mBluetoothSocket.getOutputStream();
            mInputStream = mBluetoothSocket.getInputStream();
            isSuccess = true;
        } catch (IOException e) {
            Log.e(TAG, "getStream 에러", e);
            e.printStackTrace();
        }
        return isSuccess;
    }

    /*기기와 연결이 끊어지면 소켓 및 스트림이 끊어진다. 자동으로 복구는 안됨     */
    public void processData() {

        while (mBluetoothSocket != null) {


            //얼마만큼의 데이터를 읽어왔는지 그 수를 반환
            int bytesAvailable = 0; //TODO: 장치 연결이 끊어지고 재시도할때 상대기기가 연결안될때를 상정할것. 각 상황별 시그널을 Thread Monitor에 전달할
            try {
                bytesAvailable = mInputStream.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mReceivedFrameLength = mReceivedFrameLength + bytesAvailable;

            if (bytesAvailable > 0) {
                //버퍼용 byte 배열 생성. 배열의 크기는 읽어온 바이트 크기만큼
                byte[] packetBytes = new byte[bytesAvailable];

                //버퍼용 배열에 읽어들인 데이터 저장
                try {
                    mInputStream.read(packetBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int i = 0;
                while (true) {

                    //읽어들인 Data를 모두 readBuffer에 저장했다면
                    if (i >= bytesAvailable) {
                        //변환
                        byte[] encodedBytes = new byte[mReadBufferPosition];
                        System.arraycopy(mReadBuffer, 0, encodedBytes, 0, encodedBytes.length);

                        //Log.d(mBluetoothDevice.getAddress(), "주소");
                        sendAliveSignalToMonitor(mBluetoothDevice.getAddress());
                        accumulateByte(encodedBytes);
                        parseBytesAndUpdateDB();


                        mReadBufferPosition = 0;
                        break;
                    } else {
                        //데이터수만큼 readBuffer에 저장
                        byte b = packetBytes[i];
                        mReadBuffer[mReadBufferPosition++] = b;
                    }
                    i++;
                }
            }
        }

    }
    public void SendSignalToActivity(int Signal) {
        Message SignalMessage = Message.obtain();
        SignalMessage.what = Signal;
        mTargetActivityHandler.sendMessage(SignalMessage);

    }

    @Override
    public void run() {
        if (createConnection()) {

            initConnector(mBluetoothDevice);
            processData();
        } else {
            if (mTargetActivityHandler != null) {
                SendSignalToActivity(CONNECT_FAIL);
                Log.e(TAG, "커넥션 실패");
            }
        }
        Log.e(TAG, "Thread 종료.");

    }
}
