package bluetoothconnection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by noteMel on 2016-07-04.
 */
public class SocketTemp implements Runnable{

    private final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothSocket mBluetoothSocket = null;
    private BluetoothDevice mBluetoothDevice = null;

    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;


    private byte[] mReadBuffer = new byte[1024];
    private int mReadBufferPosition;
    private int mReceivedFrameLength=0;

    //0xFF,0xFF 감지용 변수
    private final byte EOF = (byte) 0xFF;
    private int EOFCounter = 0;
    private boolean firstFlag = false;

    private Queue<byte[]> mBufferQueue = new ConcurrentLinkedQueue<>();

    private class BufferCutterThread extends Thread {
        @Override
        public void run() {
            while (true)
            {   //TODO: 200ms 에서 작동하는지 테스트할것.
                byte[] bytePiece =  mBufferQueue.poll();

                if(null !=bytePiece) {
                    Log.d("버퍼큐 데이터갯수",mBufferQueue.size() +"개");
                    //processMessage(bytePiece);
                }

            }


        }
    }



    private class ConnectionThread extends Thread {
        public ConnectionThread() {
            //Log.d(TAG, "create ConnectedThread");
            // Get the BluetoothSocket input and output streams
            try {

                setupSocket();
                getStream();

            } catch (IOException e) {
                //Log.e(TAG, "temp sockets not created", e);
            }


        }
        public void setupSocket() throws IOException
        {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(mUUID);
            mBluetoothSocket.connect();
        }
        public void getStream()throws IOException
        {
            mOutputStream   = mBluetoothSocket.getOutputStream();
            mInputStream    = mBluetoothSocket.getInputStream();
        }
        @Override
        public void run() {
            // Keep listening to the InputStream while connected
            while (mBluetoothSocket != null) {

                try {
                    //얼마만큼의 데이터를 읽어왔는지 그 수를 반환
                    int bytesAvailable = mInputStream.available();
                    mReceivedFrameLength = mReceivedFrameLength + bytesAvailable;

                    if (bytesAvailable > 0) {
                        //버퍼용 byte 배열 생성. 배열의 크기는 읽어온 바이트 크기만큼
                        byte[] packetBytes = new byte[bytesAvailable];

                        //버퍼용 배열에 읽어들인 데이터 저장
                        mInputStream.read(packetBytes);

                        int i = 0;
                        while (true) {

                            //읽어들인 Data를 모두 readBuffer에 저장했다면
                            if (i >= bytesAvailable) {
                                //변환
                                byte[] encodedBytes = new byte[mReadBufferPosition];
                                System.arraycopy(mReadBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                Log.d(mBluetoothDevice.getAddress(), "주소");
                                //mBufferQueue.offer(encodedBytes);
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
                } catch (IOException ex) {

                }
            }
        }
    }

    @Override
    public void run() {

    }
}
