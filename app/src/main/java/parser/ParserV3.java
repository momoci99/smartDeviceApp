package parser;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * Created by Melchior_S on 2016-05-19.
 * 예상 프레임 길이 산출방법
 * 프로토콜 ver1 기준
 * 기본 5Byte + 센서갯수 * 5Byte
 * ex ) 1개 센서 -> 10Byte
 * 2개 센서 -> 15Byte
 * 3개 센서 -> 20Byte
 * <p>
 * 센서필드의 위치는
 * 4,9,14,19...n+5 순이다.
 * List나 배열에서는 3,8,13,18..순
 * <p>
 * 0번 필드 - 프로토콜 버젼
 * 1번 필드 - 보드 버젼
 * 2번 필드 - 센서 갯수
 * <p>
 * 센서 ID (1Byte)
 * Data (4Byte)
 * <p>
 * v2 -> v3 변경점
 * 코드 옵티마이징
 */
public class ParserV3 {

    //파싱하는데 필요한 변수들

    //프로토콜 버젼(for future)
    static final byte PROTOCOL_VER1 = 0x01;//일반 블루투스 장치용
    static final byte PROTOCOL_VER2 = 0x02;//BLE 장치용

    static ArrayList<Byte> PROTOCOL_VER_LIST = new ArrayList<Byte>();

    static {
        PROTOCOL_VER_LIST.add(PROTOCOL_VER1);
        PROTOCOL_VER_LIST.add(PROTOCOL_VER2);

    }


    static final byte BOARD_TYPE1 = 0x01;//일반 블루투스 장치용
    static final byte BOARD_TYPE2 = 0x02;//BLE 장치용
    static final byte BOARD_TYPE3 = 0x03;
    static ArrayList<Byte> BOARD_TYPE_LIST = new ArrayList<Byte>();

    static {
        BOARD_TYPE_LIST.add(BOARD_TYPE1);
        BOARD_TYPE_LIST.add(BOARD_TYPE2);
        BOARD_TYPE_LIST.add(BOARD_TYPE3);
    }

    static final byte ZERO = 0x00;
    static final byte EOF = (byte) 0xFF;

    static final int StartIndexOfSensorID = 3;
    static final int StartIndexOfSensorDATA = 4;


    private int mProtocolVer = 0;
    private int mBoardType = 0;
    private int mSensorCount = 0;

    public String[] getSID() {
        return mSID;
    }

    public int[] getIntData() {
        return mIntData;
    }

    public float[] getFloatData() {
        return mFloatData;
    }

    private String[] mSID;
    private int[] mIntData;
    private float[] mFloatData;

    private String mMACAddress;
    private String mDeviceName;

    private BluetoothDevice mConnectedDevice;

    public void initializeParser(ArrayList<Byte> rawFrame, BluetoothDevice connectedDevice) {
        mRawFrame = rawFrame;
        mConnectedDevice = connectedDevice;
        mMACAddress = connectedDevice.getAddress();
        mDeviceName = connectedDevice.getName();

    }

    public boolean isValidProtocolVer(Byte protocolVer) {
        boolean matchFound = false;
        for (int i = 0; i < PROTOCOL_VER_LIST.size(); i++) {
            if (PROTOCOL_VER_LIST.get(i).compareTo(protocolVer) == 0) {
                matchFound = true;
            }
        }
        return matchFound;
    }


    //센서 ID 리스트 저장
    private SIDProcess mSensorIDList = new SIDProcess();

    public void setReceivedFrame(ArrayList<Byte> mReceivedFrame) {
        this.mRawFrame = mReceivedFrame;
    }

    //private int mExpectedCount = 0;
    private ArrayList<Byte> mRawFrame = new ArrayList<>();


    public boolean isLengthValid(Byte sensorCount, int frameLength) {
        boolean valid = false;
        int expectedLength = 5 + (sensorCount * 5);
        if (expectedLength == frameLength) {
            valid = true;
        }
        return valid;
    }

    //프로토콜 버젼과 프레임 길이를 검사
    //올바르지 않은 값이 검출되면 false를 반환
    public boolean checkValid() {

        try {
            //프로토콜 버젼 검사
            if (!isValidProtocolVer(mRawFrame.get(0))) {
                return false;
            }
            //mSensorCount = mRawFrame.get(2).intValue();

            if (!isLengthValid(mRawFrame.get(2), mRawFrame.size())) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getMACAddress() {
        return mMACAddress;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    private void parserProtocolV1() {
        //프로토콜 버젼
        mProtocolVer = mRawFrame.get(0).intValue();

        //보드버젼
        mBoardType = mRawFrame.get(1).intValue();

        //센서 갯수
        mSensorCount = mRawFrame.get(2).intValue();

        //센서ID
        mSID = new String[mSensorCount];

        //int형 데이터 저장용 배열 선언
        mIntData = new int[mSensorCount];

        //float형 데이터 저장용 배열 선언
        mFloatData = new float[mSensorCount];
        int DataFieldCounter = 0;

        //프로토콜 버젼은 처리하지 않는다. 위에서 처리함.
        for (int i = 1; i < mRawFrame.size(); i++) {
            if (i == 1) {
                //보드타입
                mBoardType = mRawFrame.get(i).intValue();
            } else if (i % 5 == StartIndexOfSensorID && 0 != mRawFrame.get(i).compareTo(EOF)) {
                //센서ID
                mSID[DataFieldCounter] = mSensorIDList.checkID(mRawFrame.get(i));
            }

            /*
             * INT Data와 Float Data의 구분은 다음과 같이 실시한다.
             * 센서 기기측에서 INT 값을 보낼때 4Byte 중에서 상위 2Byte는 0x00으로 보낸다.
             * 이를 바탕으로 EOF:0xFF 값이 아니고 Data 필드의 상위 2Byte 모두 0x00 일때
             * Int값으로 처리한다.
             *
             *
             *i%5 == 4 의 근거
             * 센서 데이터가 시작되는 필드는 4,9,14,19... 이다. (0부터 시작시)
             * 5씩증가하기 때문에 5로 나머지 연산을 하면 Data 시작 필드를 읽을수 있다.
             */
            else if (i % 5 == StartIndexOfSensorDATA) {
                if (0 != mRawFrame.get(i).compareTo(EOF) && 0 != mRawFrame.get(i + 1).compareTo(EOF)) {
                    if (0 == mRawFrame.get(i).compareTo(ZERO) && 0 == mRawFrame.get(i + 1).compareTo(ZERO)) {
                        //Log.d("INT", "INT로 변환");
                        byte[] tRawInt = new byte[4];
                        tRawInt[0] = mRawFrame.get(i);
                        tRawInt[1] = mRawFrame.get(i + 1);
                        tRawInt[2] = mRawFrame.get(i + 2);
                        tRawInt[3] = mRawFrame.get(i + 3);
                        mIntData[DataFieldCounter] = byte2Int(tRawInt);
                        mFloatData[DataFieldCounter] = -1;

                        DataFieldCounter++;
                        //int로 변환
                    } else {
                        byte[] tRawFloat = new byte[4];
                        tRawFloat[0] = mRawFrame.get(i);
                        tRawFloat[1] = mRawFrame.get(i + 1);
                        tRawFloat[2] = mRawFrame.get(i + 2);
                        tRawFloat[3] = mRawFrame.get(i + 3);

                        mFloatData[DataFieldCounter] = byte2Float(tRawFloat);
                        mIntData[DataFieldCounter] = -1;
                        DataFieldCounter++;
                        //Log.d("float","float로 변환");
                        //float로 변환
                    }
                }

            }

        }
    }

    private void parserProtocolV2() {
        //프로토콜 버젼
        mProtocolVer = mRawFrame.get(0).intValue();

        //보드버젼
        mBoardType = mRawFrame.get(1).intValue();

        //센서 갯수
        mSensorCount = mRawFrame.get(2).intValue();
        mSID = new String[mSensorCount];
        mIntData = new int[mSensorCount];
        mFloatData = new float[mSensorCount];
        int DataFieldCounter = 0;

        //프로토콜 버젼은 처리하지 않는다. 위에서 처리함.
        for (int i = 1; i < mRawFrame.size(); i++) {
            if (i == 1) {
                //보드버젼
                mBoardType = mRawFrame.get(i).intValue();
            } else if (i % 5 == StartIndexOfSensorID && 0 != mRawFrame.get(i).compareTo(EOF)) {
                //센서ID
                mSID[DataFieldCounter] = mSensorIDList.checkID(mRawFrame.get(i));
            }

            /*
             * INT Data와 Float Data의 구분은 다음과 같이 실시한다.
             * 센서 기기측에서 INT 값을 보낼때 4Byte 중에서 상위 2Byte는 0x00으로 보낸다.
             * 이를 바탕으로 EOF:0xFF 값이 아니고 Data 필드의 상위 2Byte 모두 0x00 일때
             * Int값으로 처리한다.
             *
             *
             *i%5 == 4 의 근거
             * 센서 데이터가 시작되는 필드는 4,9,14,19... 이다. (0부터 시작시)
             * 5씩증가하기 때문에 5로 나머지 연산을 하면 Data 시작 필드를 읽을수 있다.
             */
            else if (i % 5 == StartIndexOfSensorDATA) {
                if (0 != mRawFrame.get(i).compareTo(EOF) && 0 != mRawFrame.get(i + 1).compareTo(EOF)) {
                    if (0 == mRawFrame.get(i).compareTo(ZERO) && 0 == mRawFrame.get(i + 1).compareTo(ZERO)) {
                        Log.d("INT", "INT로 변환");
                        byte[] tRawInt = new byte[4];
                        tRawInt[0] = mRawFrame.get(i);
                        tRawInt[1] = mRawFrame.get(i + 1);
                        tRawInt[2] = mRawFrame.get(i + 2);
                        tRawInt[3] = mRawFrame.get(i + 3);
                        mIntData[DataFieldCounter] = byte2Int(tRawInt);
                        mFloatData[DataFieldCounter] = -1;

                        DataFieldCounter++;
                        //int로 변환
                    } else {
                        byte[] tRawFloat = new byte[4];
                        tRawFloat[0] = mRawFrame.get(i);
                        tRawFloat[1] = mRawFrame.get(i + 1);
                        tRawFloat[2] = mRawFrame.get(i + 2);
                        tRawFloat[3] = mRawFrame.get(i + 3);

                        mFloatData[DataFieldCounter] = byte2Float(tRawFloat);
                        mIntData[DataFieldCounter] = -1;
                        DataFieldCounter++;
                        //Log.d("float","float로 변환");
                        //float로 변환
                    }
                }

            }

        }
    }


    public void parseFrame() {

        if (checkValid()) {
            switch (mRawFrame.get(0).intValue()) {
                case 1:
                    parserProtocolV1();
                    break;
                case 2:
                    //for future
                    parserProtocolV2();
                default:
                    break;
            }

        }

    }


    public int byte2Int(byte[] src) {
        //여기서 & 0xFF 연산은 크게 의미가 없다.
        int s1 = src[0] & 0xFF;
        int s2 = src[1] & 0xFF;
        int s3 = src[2] & 0xFF;
        int s4 = src[3] & 0xFF;
        return ((s1 << 24) + (s2 << 16) + (s3 << 8) + (s4 << 0));
    }

    public float byte2Float(byte[] src) {
        float f = ByteBuffer.wrap(src).order(ByteOrder.BIG_ENDIAN).getFloat();
        return f;
    }

    /*
       센서 ID를 매칭하기위한 클래스

     */
    private class SIDProcess {

        static final byte S_CO = 0x31;
        static final byte S_TEMPER = 0x01;
        static final byte S_HUMIDITY = 0x02;
        static final byte S_OXYGEN = 0x04;

        /*
        public ArrayList<Byte> getSensorIDList() {
            return mSensorIDList;
        }*/

        private ArrayList<Byte> mSensorIDList = new ArrayList<>();

        /*
        센서마다 각각 다른 Byte 값을 저장한다.
         */
        public SIDProcess() {
            mSensorIDList.add(S_CO);
            mSensorIDList.add(S_TEMPER);
            mSensorIDList.add(S_HUMIDITY);
            mSensorIDList.add(S_OXYGEN);
        }

        //ID 필드를 읽어 센서명을 찾는다.
        public String checkID(Byte idField) {
            String result = "";

            for (int i = 0; i < mSensorIDList.size(); i++) {
                if (0 == idField.compareTo(mSensorIDList.get(i))) {
                    result = findID(i);
                    break;
                }
            }
            return result;
        }

        //PS. 좀더 유지보수가 편한 방법이 필요하다.
        //SIDProcess 함수내에서 리스트에 저장되는 순서와 맞출것
        private String findID(int index) {
            String result;
            switch (index) {
                case 0:
                    result = "일산화탄소";
                    break;
                case 1:
                    result = "온도";
                    break;
                case 2:
                    result = "습도";
                    break;
                case 3:
                    result = "산소";
                    break;
                default:
                    result = "NoMatched";
                    break;
            }
            return result;

        }
    }

}
