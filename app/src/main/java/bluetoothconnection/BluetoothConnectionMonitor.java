package bluetoothconnection;

import android.util.Log;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by noteMel on 2016-07-12.
 */
public class BluetoothConnectionMonitor implements Runnable {

    private String TAG = "커넥션모니터";
    static Queue<String> mAliveSignalQueue = new ConcurrentLinkedQueue<>();
    static ConcurrentHashMap<String, Long> mLatestReceivedTimeTable = new ConcurrentHashMap<>();
    static CopyOnWriteArrayList<String> mConnectedDeviceList = new CopyOnWriteArrayList<String>();


    static CopyOnWriteArrayList<String> mRetryingConnectionList = new CopyOnWriteArrayList<String>();
    static ConcurrentHashMap<String, Long> mRetryElapseTimeTable = new ConcurrentHashMap<>();
    static ConcurrentHashMap<String, Integer> mRetryCount = new ConcurrentHashMap<>();


    private BluetoothReconnector mBluetoothReconnector = new BluetoothReconnector();

    private final int WARNING_TIME_BOUND = 10000;       //10sec
    private final int DISCONNECTED_TIME_BOUND = 20000;  //20sec
    private final int RETRY_INTERVAL_TIME = 50000;      //50sec

    private final int DEFAULT_RETRY_COUNT = 2;

    private boolean outputFlag = false;

    public synchronized void checkRetryingDevice() {
        for (int i = 0; i < mRetryingConnectionList.size(); i++) {
            String MACAddress = mRetryingConnectionList.get(i);

            long lastedTriedTime = 0;
            try {
                lastedTriedTime = mRetryElapseTimeTable.get(MACAddress);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            long currentTime = System.currentTimeMillis();
            long pastTime = currentTime - lastedTriedTime;

            int RETRY_COUNT = mRetryCount.get(MACAddress);

            if (pastTime > RETRY_INTERVAL_TIME && RETRY_COUNT > 0) {
                Log.e(TAG, MACAddress + "장치의 연결 복구를 시도했지만 해당 장치와의 연결이 확인되지 않았습니다. 재시도합니다.");
                Log.e(TAG, "남은 재시도 횟수 : " + RETRY_COUNT);

                tryReconnect(mRetryingConnectionList.get(i));
                mRetryElapseTimeTable.put(MACAddress,currentTime);
                mRetryCount.put(MACAddress, RETRY_COUNT - 1);


            }
            else if(pastTime > RETRY_INTERVAL_TIME && RETRY_COUNT <=0)
            {
                Log.e(TAG, MACAddress + "장치 연결 복구 횟수를 초과하였습니다. 완전히 끊어진것같습니다.");
                //야 관리자야 장치 완전히 끊어졌어 ㅅㅂ;;;;;;;;
                //서버에 통보하는 알고리즘 필요
                //이 장치는 글러먹은 장치니까.. ㅈㅈ
                mRetryingConnectionList.remove(MACAddress);
                mRetryElapseTimeTable.remove(MACAddress);
                mRetryCount.remove(MACAddress);
            }


        }
    }

    public synchronized void checkAliveDevice() {

        for (int i = 0; i < mConnectedDeviceList.size(); i++) {
            long lastedReceivedTime = mLatestReceivedTimeTable.get(mConnectedDeviceList.get(i));
            String MACAddress = mConnectedDeviceList.get(i);


            long currentTime = System.currentTimeMillis();
            long pastTime = currentTime - lastedReceivedTime;

            //Warning Case
            if (pastTime >= WARNING_TIME_BOUND && pastTime < DISCONNECTED_TIME_BOUND) {

                if (!outputFlag) {
                    Log.e(TAG, MACAddress + "에서 데이터를 보내지 않습니다.");
                    outputFlag = true;
                }

            }

            //try reconnect Case
            //마지막으로 받은 생존신호 테이블에서 해당기기 삭제(끊어진것으로 간주)
            //연결된 기기 목록에서 삭제
            //재연결 시도 목록에 추가
            //재연결시도 경과시간 테이블에 추가
            else if (pastTime >= DISCONNECTED_TIME_BOUND) {
                outputFlag = false;


                mLatestReceivedTimeTable.remove(MACAddress);
                mConnectedDeviceList.remove(MACAddress);


                mRetryElapseTimeTable.put(MACAddress, currentTime);
                mRetryCount.put(MACAddress, DEFAULT_RETRY_COUNT);

                mRetryingConnectionList.add(MACAddress);
                tryReconnect(MACAddress);
                break;
            }
        }

    }

    public synchronized void processAliveSignal() {

        String receivedSignal = mAliveSignalQueue.poll();
        if (receivedSignal != null) {
            //Log.d(TAG, "시그널 큐 크기" + mAliveSignalQueue.size());

            registerAndUpdateConnectedDevice(receivedSignal);


        }
    }

    public void registerAndUpdateConnectedDevice(String MACAddress) {
        boolean isDuplicate = false;
        long registeredTime;

        for (int i = 0; i < mConnectedDeviceList.size(); i++) {
            if (MACAddress.equals(mConnectedDeviceList.get(i))) {
                isDuplicate = true;
            }
        }

        if (!isDuplicate) {
            mConnectedDeviceList.add(MACAddress);
            mLatestReceivedTimeTable.put(MACAddress, System.currentTimeMillis());

        } else if (isDuplicate) {

            registeredTime = mLatestReceivedTimeTable.get(MACAddress);
            long currentTime = System.currentTimeMillis();
            long pastTime = currentTime - registeredTime;

            //safeCase
            if (pastTime < WARNING_TIME_BOUND && pastTime > 0) {
                Log.d(TAG, MACAddress + "의 연결상태 : 정상");
                mLatestReceivedTimeTable.put(MACAddress, System.currentTimeMillis());

                //정상적인 연결이 확인되었으므로 재시도 연결 리스트에서 해당 기기 제거
                if (mRetryingConnectionList.size() > 0) {
                    mRetryingConnectionList.remove(MACAddress);
                    mRetryElapseTimeTable.remove(MACAddress);
                    mRetryCount.remove(MACAddress);
                }
            }

        }

    }

    public void tryReconnect(String MACAddress) {
        mBluetoothReconnector.sendReconnectRequest(MACAddress);
    }


    public void sendAliveSignalQueue(String mMACAddress) {
        mAliveSignalQueue.offer(mMACAddress);
    }

    @Override
    public void run() {
        while (true) {
            processAliveSignal();
            checkAliveDevice();
            checkRetryingDevice();
        }
    }
}
