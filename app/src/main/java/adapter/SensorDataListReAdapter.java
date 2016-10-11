package adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notemel.deviceappalphav02.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import format.DBResultForm;

/**
 * Created by Melchior_S on 2016-09-20.
 */
public class SensorDataListReAdapter extends RecyclerView.Adapter<SensorDataListReAdapter.ViewHolder> {

    private final String TAG = "S.DListReAdapter";
    private ArrayList<DBResultForm> mDataSet;

    public SensorDataListReAdapter(ArrayList<DBResultForm> dataSet)
    {
        mDataSet = dataSet;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_sensordata, parent, false);
        // set the view's size, margins, paddings and layout parameters


        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mTV_sData1.setText(String.format(Locale.US,"%.2f", mDataSet.get(position).getSensorData_1()));
        holder.mTV_sData2.setText(String.format(Locale.US,"%.2f", mDataSet.get(position).getSensorData_2()));
        holder.mTV_sData3.setText(String.format(Locale.US,"%.2f", mDataSet.get(position).getSensorData_3()));
        holder.mTV_sData4.setText(String.format(Locale.US,"%.2f", mDataSet.get(position).getSensorData_4()));
        holder.mTV_time.setText(mDataSet.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTV_sData1;
        public TextView mTV_sData2;
        public TextView mTV_sData3;
        public TextView mTV_sData4;
        public TextView mTV_time;

        public ViewHolder(View itemView) {
            super(itemView);

            mTV_sData1 = (TextView) itemView.findViewById(R.id.tv_ry_sensor_data1);
            mTV_sData2 = (TextView) itemView.findViewById(R.id.tv_ry_sensor_data2);
            mTV_sData3 = (TextView) itemView.findViewById(R.id.tv_ry_sensor_data3);
            mTV_sData4 = (TextView) itemView.findViewById(R.id.tv_ry_sensor_data4);
            mTV_time = (TextView) itemView.findViewById(R.id.tv_ry_time);

        }
    }
}
