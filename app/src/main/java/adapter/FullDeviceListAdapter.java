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
 * Created by Melchior_S on 2016-09-07.
 */
public class FullDeviceListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    public FullDeviceListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    //모든 기기 목록
    private CopyOnWriteArrayList<String> mFullDeviceList = new CopyOnWriteArrayList<>();

    @Override
    public int getCount() {
        return (mFullDeviceList == null) ? 0 : mFullDeviceList.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.list_item_full_device, parent, false);

            holder = new ViewHolder();

            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_device_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String deviceName = mFullDeviceList.get(position);

        holder.nameTv.setText(deviceName);

        return convertView;
    }

    public void setData(CopyOnWriteArrayList<String> fullDeviceList) {
        mFullDeviceList = fullDeviceList;

    }

    static class ViewHolder {
        TextView nameTv;
    }
}
