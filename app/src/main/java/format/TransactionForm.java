package format;

/**
 * Created by noteMel on 2016-07-11.
 */
public class TransactionForm {

    private String address;
    private String name;
    private String[] mSID;
    private int[] mIntData;
    private float[] mFloatData;

    public void reset()
    {
        address = null;
        name = null;
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
