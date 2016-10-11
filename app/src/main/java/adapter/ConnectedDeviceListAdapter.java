package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notemel.deviceappalphav02.R;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by noteMel on 2016-07-20.
 */
public class ConnectedDeviceListAdapter extends BaseAdapter {

    private static String TAG = "ConnectedD.ListAdapter";
    private String WATCHING = "WATCHING";
    private String RETRYING = "RETRYING";
    private String LOSTCONNECT = "LOSTCONNECT";
    private String ALIVE = "ALIVE";


    private LayoutInflater mInflater;


    public ConnectedDeviceListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    //모든 기기 목록
    private CopyOnWriteArrayList<String> mTotalDeviceList = new CopyOnWriteArrayList<>();

    //모든 기기 연결 상태 Table
    private ConcurrentHashMap<String, String> mConnectionStatusTable = new ConcurrentHashMap<>();

    //모든 기기 목록 - Name
    private static CopyOnWriteArrayList<String> mTotalDeviceNameList = new CopyOnWriteArrayList<>();

    public void setData(CopyOnWriteArrayList<String> totalDeviceList, CopyOnWriteArrayList<String> totalDeviceNameList, ConcurrentHashMap<String, String> connectionStatusTable) {
        Log.e(TAG, "setData mTotalDeviceList : " + mTotalDeviceList.size());
        mTotalDeviceList = totalDeviceList;
        mConnectionStatusTable = connectionStatusTable;
        mTotalDeviceNameList = totalDeviceNameList;
    }


    @Override
    public int getCount() {
        return (mTotalDeviceList == null) ? 0 : mTotalDeviceList.size();
        //mData == null -> return 0
        //mData =! null -> return mData 의 요소 갯수
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.list_item_connected_device, parent, false);

            holder = new ViewHolder();

            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_status_device_name);
            holder.connection_status_Img = (ImageView) convertView.findViewById(R.id.iv_connection_status);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String deviceName = mTotalDeviceNameList.get(position);
        String deviceAddress = mTotalDeviceList.get(position);
        //상황에 맞게 변경이 필요함
        holder.nameTv.setText(deviceName);
        if (mConnectionStatusTable.get(deviceAddress).equals(ALIVE)) {
            holder.connection_status_Img.setBackgroundResource(R.drawable.connected);
        } else if (mConnectionStatusTable.get(deviceAddress).equals(WATCHING)) {
            holder.connection_status_Img.setBackgroundResource(R.drawable.watching);
        } else if (mConnectionStatusTable.get(deviceAddress).equals(RETRYING)) {
            holder.connection_status_Img.setBackgroundResource(R.drawable.retrying);
        } else if (mConnectionStatusTable.get(deviceAddress).equals(LOSTCONNECT)) {
            holder.connection_status_Img.setBackgroundResource(R.drawable.lostconnect);
        }

        return convertView;


    }

    //ViewHolder Pattern
    static class ViewHolder {
        TextView nameTv;
        ImageView connection_status_Img;


    }

}
