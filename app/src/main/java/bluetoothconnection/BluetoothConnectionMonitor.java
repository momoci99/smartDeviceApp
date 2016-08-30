package bluetoothconnection;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by noteMel on 2016-07-12.
 */
public class BluetoothConnectionMonitor implements Runnable {


    //TODO : 메서드 이름정리
    //TODO : singleton 으로의 전환 고려
    static final String TAG = "커넥션모니터";

    private String WATCHING ="WATCHING";
    private String RETRYING ="RETRYING";
    private String LOSTCONNECT = "LOSTCONNECT";
    private String ALIVE = "ALIVE";
    static final int STATUS_UPDATE = 55;

    private long mLastStatusSentTime = 0;



    private volatile static BluetoothConnectionMonitor objectInstance;


    public static BluetoothConnectionMonitor getInstance(){
        if(objectInstance == null){
            synchronized (BluetoothConnectionMonitor.class) {
                if(objectInstance == null){
                    objectInstance = new BluetoothConnectionMonitor();

                }
            }
        }
        return objectInstance;
    }

    //생존신호 큐
    static ConcurrentLinkedQueue<String> mAliveSignalQueue = new ConcurrentLinkedQueue<>();

    //마지막으로 신호를 받은 시간
    static ConcurrentHashMap<String, Long> mLatestReceivedTimeTable = new ConcurrentHashMap<>();

    //현재 살아있는 장치 목록
    static CopyOnWriteArrayList<String> mAliveDeviceList = new CopyOnWriteArrayList<String>(); //현재 살아있는 장치 목록




    //재연결 시도중인 기기 리스트
    static CopyOnWriteArrayList<String> mRetryingConnectionList = new CopyOnWriteArrayList<String>();

    //마지막으로 재시도후 경과한 시간
    static ConcurrentHashMap<String, Long> mRetryElapseTimeTable = new ConcurrentHashMap<>();

    //기기별 재시도 횟수
    static ConcurrentHashMap<String, Integer> mRetryCount = new ConcurrentHashMap<>();





    //모든 기기 목록(연결이 살아있든 죽어있든간에 일단 등록)
    static CopyOnWriteArrayList<String> mTotalDeviceList =new CopyOnWriteArrayList<>();

    //모든 기기 연결 상태 Table
    static ConcurrentHashMap<String, String> mConnectionStatusTable = new ConcurrentHashMap<>();




    public void setTargetActivityHandler(Handler mTargetHandler) {
        this.mTargetActivityHandler = mTargetHandler;

    }

    private Handler mTargetActivityHandler;

    private BluetoothReconnector mBluetoothReconnector = new BluetoothReconnector();

    private final int WARNING_TIME_BOUND = 10000;       //10sec
    private final int DISCONNECTED_TIME_BOUND = 20000;  //20sec
    private final int RETRY_INTERVAL_TIME = 50000;      //50sec

    private final int DEFAULT_RETRY_COUNT = 1;

    private boolean mWarningActivatedFlag = false;

    private synchronized void checkRetryingDevice() {
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

            if ((pastTime > RETRY_INTERVAL_TIME) && (RETRY_COUNT > 0)) {
                Log.e(TAG, MACAddress + "장치의 연결 복구를 시도했지만 해당 장치와의 연결이 확인되지 않았습니다. 재시도합니다.");
                Log.e(TAG, "남은 재시도 횟수 : " + RETRY_COUNT);


                //재연결 시도
                tryReconnect(mRetryingConnectionList.get(i));

                //마지막 재연결 시간 갱신
                mRetryElapseTimeTable.put(MACAddress,currentTime);

                //재시도 횟수 감소
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
                //mTotalDeviceList.add(MACAddress);
                updateConnectionStatusTable(MACAddress, LOSTCONNECT);

            }


        }
    }

    private synchronized void checkAliveDevice() {

        for (int i = 0; i < mAliveDeviceList.size(); i++) {
            long lastedReceivedTime = mLatestReceivedTimeTable.get(mAliveDeviceList.get(i));
            String MACAddress = mAliveDeviceList.get(i);


            long currentTime = System.currentTimeMillis();
            long pastTime = currentTime - lastedReceivedTime;

            //Warning Case
            if (pastTime >= WARNING_TIME_BOUND && pastTime < DISCONNECTED_TIME_BOUND) {

                if (!mWarningActivatedFlag) {
                    Log.e(TAG,"mAliveDeviceList 사이즈 갯수 : "+mAliveDeviceList.size());
                    Log.e(TAG, MACAddress + "에서 데이터를 보내지 않습니다.");
                    updateConnectionStatusTable(MACAddress,WATCHING);
                    sendStatusChangedSignalToMainActivity();
                    mWarningActivatedFlag = true;
                }

            }

            //try reconnect Case - 재시도하는 경우
            //경고 플래그 리셋
            //마지막으로 받은 생존신호 테이블에서 해당기기 삭제(끊어진것으로 간주)
            //연결된 기기 목록에서 삭제
            //재연결 시도 목록에 추가
            //재연결시도 경과시간 테이블에 추가
            else if (pastTime >= DISCONNECTED_TIME_BOUND) {
                mWarningActivatedFlag = false;


                mLatestReceivedTimeTable.remove(MACAddress);
                mAliveDeviceList.remove(MACAddress);


                mRetryElapseTimeTable.put(MACAddress, currentTime);
                mRetryCount.put(MACAddress, DEFAULT_RETRY_COUNT);

                mRetryingConnectionList.add(MACAddress);

                tryReconnect(MACAddress);
                updateConnectionStatusTable(MACAddress,RETRYING);
                sendStatusChangedSignalToMainActivity();
                break;
            }
            else
            {
                //정상적인 - 살아있는경우
                updateConnectionStatusTable(MACAddress,ALIVE );
                //sendStatusChangedSignalToMainActivity();
            }
        }

    }

    private synchronized void handleAliveSignal() {

        String receivedSignal = mAliveSignalQueue.poll();
        if (receivedSignal != null) {
            //Log.d(TAG, "시그널 큐 크기" + mAliveSignalQueue.size());

            registerAndUpdateConnectedDevice(receivedSignal);


        }
    }

    private void registerAndUpdateConnectedDevice(String MACAddress) {
        boolean isDuplicate = false;
        boolean isReConnected = false;
        long registeredTime;

        for (int i = 0; i < mAliveDeviceList.size(); i++) {
            if (MACAddress.equals(mAliveDeviceList.get(i))) {
                isDuplicate = true;
            }
        }

        //신규기기
        if (!isDuplicate) {

            //연결이 복구된 기기인지 확인한다.
            for(int i =0; i<mTotalDeviceList.size(); i++)
            {
                if(MACAddress.equals(mTotalDeviceList.get(i)))
                {
                    isReConnected = true;
                }
            }
            if(!isReConnected)
            {
                mTotalDeviceList.add(MACAddress);

            }
            registerDevice(MACAddress);
            Log.e(TAG,"mTotalDeviceList 사이즈는 : "+mTotalDeviceList.size());

            //기존기기
        } else if (isDuplicate) {

            registeredTime = mLatestReceivedTimeTable.get(MACAddress);
            long currentTime = System.currentTimeMillis();
            long pastTime = currentTime - registeredTime;

            //safeCase
            if ((pastTime < WARNING_TIME_BOUND) && (pastTime > 0)) {
                updateConnectedDevice(MACAddress);
            }

        }

    }
    private void updateConnectedDevice(String MACAddress)
    {
        Log.d(TAG, MACAddress + "의 연결상태 : 정상");
        mLatestReceivedTimeTable.put(MACAddress, System.currentTimeMillis());

        //정상적인 연결이 확인되었으므로 재시도 연결 리스트에서 해당 기기 제거
        if (mRetryingConnectionList.size() > 0) {
            mRetryingConnectionList.remove(MACAddress);
            mRetryElapseTimeTable.remove(MACAddress);
            mRetryCount.remove(MACAddress);

            updateConnectionStatusTable(MACAddress,ALIVE);
            sendStatusChangedSignalToMainActivity();
        }
    }
    private void registerDevice(String MACAddress)
    {
        mAliveDeviceList.add(MACAddress);
        mLatestReceivedTimeTable.put(MACAddress, System.currentTimeMillis());
        updateConnectionStatusTable(MACAddress,ALIVE);


        sendStatusChangedSignalToMainActivity();

    }



    private void tryReconnect(String MACAddress) {
        mBluetoothReconnector.sendReconnectRequest(MACAddress);
    }

    private void updateConnectionStatusTable(String MACAddress, String Status)
    {
        mConnectionStatusTable.put(MACAddress,Status);
    }


    public CopyOnWriteArrayList getAliveDeviceList()
    {
        return mAliveDeviceList;
    }



    public static CopyOnWriteArrayList<String> getTotalDeviceList() {
        return mTotalDeviceList;
    }

    public static ConcurrentHashMap<String, String> getConnectionStatusTable() {
        if(mConnectionStatusTable.size()==0)
        {
            Log.e(TAG,"mConnectionStatusTable 갯수가 0개입니다.");
        }
        return mConnectionStatusTable;
    }

    //메인 액티비티로 상태변경을 알림
    public synchronized void sendStatusChangedSignalToMainActivity()
    {

        Message SignalMessage = Message.obtain();
        SignalMessage.what = STATUS_UPDATE;
        mTargetActivityHandler.sendMessage(SignalMessage);

    }
    public synchronized void signalSender()
    {
        long currentTime = System.currentTimeMillis();


        //일반적인경우 30초 간격으로 업데이트
        //단 WATCHING,RETRYING,LOSTCONNECT, 재연결의 경우 바로 업데이트한다.
        if((currentTime - mLastStatusSentTime > 30000) &&  (mTotalDeviceList.size() > 0) && (mConnectionStatusTable.size() > 0))
        {
            Log.e(TAG,"signalSender() 내부 if문 동작함");
            sendStatusChangedSignalToMainActivity();

        }
        mLastStatusSentTime = currentTime;


    }
    public void sendAliveSignalQueue(String mMACAddress) {
        mAliveSignalQueue.offer(mMACAddress);
    }

    @Override
    public void run() {
        while (true) {
            handleAliveSignal();
            checkAliveDevice();
            checkRetryingDevice();
            signalSender();

        }
    }
}
