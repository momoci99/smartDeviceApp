package adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.notemel.deviceappalphav02.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by noteMel on 2016-09-28.
 */
public class ChartDeviceNameListReAdapter extends RecyclerView.Adapter<ChartDeviceNameListReAdapter.ViewHolder>{

    private static final String TAG = "CDN.ListReAdapter";
    private ArrayList<String> mDataSet;

    public static ArrayList<String> mSelectedDeviceList = new ArrayList<>();


    public ChartDeviceNameListReAdapter (ArrayList dataSet){
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chart_devicelist, parent, false);
        // set the view's size, margins, paddings and layout parameters


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mTV_elementName.setText(mDataSet.get(position));
        holder.mCKB_deviceSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, holder.getAdapterPosition() + "번 체크박스");
                Log.e(TAG, isChecked + "입니다.");

                if(isChecked)
                {
                    mSelectedDeviceList.add(mDataSet.get(holder.getAdapterPosition()));
                }
                else
                {
                    mSelectedDeviceList.remove(mDataSet.get(holder.getAdapterPosition()));
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTV_elementName;
        public CheckBox mCKB_deviceSelection;

        public ViewHolder(View itemView) {
            super(itemView);

            mTV_elementName = (TextView) itemView.findViewById(R.id.tv_chart_device_name);
            mCKB_deviceSelection = (CheckBox) itemView.findViewById(R.id.ckb_chart_device_check);



        }


    }
}
