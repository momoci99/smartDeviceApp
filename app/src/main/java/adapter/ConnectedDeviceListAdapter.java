package adapter;

import android.content.Context;
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

    private String WATCHING = "WATCHING";
    private String RETRYING = "RETRYING";
    private String LOSTCONNECT = "LOSTCONNECT";
    private String ALIVE = "ALIVE";


    private LayoutInflater mInflater;
    //LayoutInflater - xml 문서에 명시된 레이아웃을 구현하여 메모리에 적재.


    public ConnectedDeviceListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    //모든 기기 목록(연결이 살아있든 죽어있든간에 일단 등록)
    private CopyOnWriteArrayList<String> mTotalDeviceList = new CopyOnWriteArrayList<>();

    //모든 기기 연결 상태 Table
    private ConcurrentHashMap<String, String> mConnectionStatusTable = new ConcurrentHashMap<>();


    public void setData(CopyOnWriteArrayList<String> totalDeviceList, ConcurrentHashMap<String, String> connectionStatusTable) {
        mTotalDeviceList = totalDeviceList;
        mConnectionStatusTable = connectionStatusTable;

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
            convertView = mInflater.inflate(R.layout.list_item_connected_device, null);

            holder = new ViewHolder();

            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_status_device_name);
            holder.connection_status_Img = (ImageView) convertView.findViewById(R.id.iv_connection_status);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String deviceName = mTotalDeviceList.get(position);

        //상황에 맞게 변경이 필요함
        holder.nameTv.setText(deviceName);
        if (mConnectionStatusTable.get(deviceName).equals(ALIVE)) {
            holder.connection_status_Img.setBackgroundResource(R.drawable.connected);
        } else if (mConnectionStatusTable.get(deviceName).equals(WATCHING)) {
            holder.connection_status_Img.setBackgroundResource(R.drawable.watching);
        } else if (mConnectionStatusTable.get(deviceName).equals(RETRYING)) {
            holder.connection_status_Img.setBackgroundResource(R.drawable.retrying);
        } else if (mConnectionStatusTable.get(deviceName).equals(LOSTCONNECT)) {
            holder.connection_status_Img.setBackgroundResource(R.drawable.lostconnect);
        }
        //holder.connection_status_Img.setBackgroundResource();


        return convertView;


    }

    //ViewHolder Pattern - 빠르게 불러올수있다.
    static class ViewHolder {
        TextView nameTv;
        ImageView connection_status_Img;


    }

}
