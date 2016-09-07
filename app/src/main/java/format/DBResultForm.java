package format;

/**
 * Created by Melchior_S on 2016-09-07.
 */
public class DBResultForm {

    public int getLogNum() {
        return mLogNum;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public int getBoardVer() {
        return mBoardVer;
    }

    public String getSensorName_1() {
        return mSensorName_1;
    }

    public double getSensorData_1() {
        return mSensorData_1;
    }

    public String getSensorName_2() {
        return mSensorName_2;
    }

    public double getSensorData_2() {
        return mSensorData_2;
    }

    public String getSensorName_3() {
        return mSensorName_3;
    }

    public double getSensorData_3() {
        return mSensorData_3;
    }

    public String getSensorName_4() {
        return mSensorName_4;
    }

    public double getSensorData_4() {
        return mSensorData_4;
    }

    public String getTime() {
        return mTime;
    }

    private int mLogNum;
    private String mDeviceName;
    private int mBoardVer;
    private String mSensorName_1;
    private double mSensorData_1;

    private String mSensorName_2;
    private double mSensorData_2;

    private String mSensorName_3;
    private double mSensorData_3;

    private String mSensorName_4;
    private double mSensorData_4;

    private String mTime;

    public DBResultForm(int logNum,String deviceName, int boardVer,
                        String sensorName_1,double sensorData_1,
                        String sensorName_2,double sensorData_2,
                        String sensorName_3,double sensorData_3,
                        String sensorName_4,double sensorData_4,String time)
    {
        this.mLogNum = logNum;
        this.mDeviceName = deviceName;
        this.mBoardVer = boardVer;
        this.mSensorName_1 = sensorName_1;
        this.mSensorData_1 = sensorData_1;

        this.mSensorName_2 = sensorName_2;
        this.mSensorData_2 = sensorData_2;

        this.mSensorName_3 = sensorName_3;
        this.mSensorData_3 = sensorData_3;

        this.mSensorName_4 = sensorName_4;
        this.mSensorData_4 = sensorData_4;

        this.mTime = time;

    }

}
