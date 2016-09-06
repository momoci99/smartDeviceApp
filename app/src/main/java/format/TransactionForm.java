package format;

/**
 * Created by noteMel on 2016-07-11.
 */
public class TransactionForm {

    private String address;
    private String name;
    private String[] mSID;

    public int getBoardVer() {
        return mBoardVer;
    }

    public void setBoardVer(int mBoardVer) {
        this.mBoardVer = mBoardVer;
    }

    private int mBoardVer;
    private int[] mIntData;
    private float[] mFloatData;


    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String[] getSID() {
        return mSID;
    }

    public int[] getIntData() {
        return mIntData;
    }

    public float[] getFloatData() {
        return mFloatData;
    }


    public void reset() {
        address = null;
        name = null;
        mBoardVer = -1;
        mSID = null;
        mIntData = null;
        mFloatData = null;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setSID(String[] mSID) {
        this.mSID = mSID;
    }


    public void setIntData(int[] mIntData) {
        this.mIntData = mIntData;
    }


    public void setFloatData(float[] mFloatData) {
        this.mFloatData = mFloatData;
    }


}
