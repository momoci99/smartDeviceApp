package bluetoothconnection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import system.ThreadManager;

/**
 * Created by noteMel on 2016-07-13.
 */

public class BluetoothReconnector implements Runnable {
    /*
    private String TAG = "커넥션리커넥터";


    private ThreadManager mThreadManager;

    private BlueToothClassic mSocketConnector;
    private BluetoothAdapter mBluetoothAdapter;

    static Queue<String> mReconnectStandingQueue = new ConcurrentLinkedQueue<>();


    public boolean tryReconnectClassicBluetoothDevice(String requestDeviceAddress) {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "리커넥션 시도 실패 - Adapter 획득실패");
            return false;
        }

        BluetoothDevice mTargetDevice = mBluetoothAdapter.getRemoteDevice(requestDeviceAddress);
        if (mTargetDevice == null) {
            Log.e(TAG, "리커넥션 시도 실패 - 리모트디바이스 획득 실패");
            return false;
        }

        mSocketConnector = new BlueToothClassic();
        mSocketConnector.configConnection(mTargetDevice);

        mThreadManager = ThreadManager.getInstance();
        mThreadManager.ActiveThread(mSocketConnector);
        Log.d(TAG, "리커넥션 시도 성공");


        return true;

    }

    //재연결 처리
    public synchronized void processReconnection() {
        String targetDeviceAddress = mReconnectStandingQueue.poll();



        if (targetDeviceAddress != null) {

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Log.d(TAG, "리커넥션 시도 실패 - Adapter 획득실패");
            }

            BluetoothDevice mTargetDevice = mBluetoothAdapter.getRemoteDevice(targetDeviceAddress);
            if (mTargetDevice == null) {
                Log.e(TAG, "리커넥션 시도 실패 - 리모트디바이스 획득 실패");
            }

            if(mTargetDevice.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC)
            {
                tryReconnectClassicBluetoothDevice(targetDeviceAddress);
            }
            else if(mTargetDevice.getType() == BluetoothDevice.DEVICE_TYPE_LE)
            {
                Log.e(TAG, "리커넥션 시도 예외 - BLE장치 재연결미지원");
                //BLE 미지원
            }

        }
    }

    public void sendReconnectRequest(String requestDeviceAddress) {
        mReconnectStandingQueue.offer(requestDeviceAddress);
    }
*/
    @Override
    public void run() {
        while (true) {
        //     processReconnection();
        }
    }
}
