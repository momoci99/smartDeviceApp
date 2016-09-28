package adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.notemel.deviceappalphav02.R;

import java.util.ArrayList;

/**
 * Created by noteMel on 2016-09-28.
 */
public class ChartDeviceNameListReAdapter extends RecyclerView.Adapter<ChartDeviceNameListReAdapter.ViewHolder>{

    private final String TAG = "CDN.ListReAdapter";
    private ArrayList<String> mDataSet;
    private ArrayList<String> mCheckList = new ArrayList<>();
    private int mSlectPosition = -1;


    public ChartDeviceNameListReAdapter (ArrayList dataSet){
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chart_element_select, parent, false);
        // set the view's size, margins, paddings and layout parameters


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        mSlectPosition = position;
        holder.mTV_elementName.setText(mDataSet.get(position));


        //RadioButton?
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTV_elementName;
        public CheckBox mCKB_deviceSelection;

        public ViewHolder(View itemView) {
            super(itemView);

            mTV_elementName = (TextView) itemView.findViewById(R.id.tv_chart_device_name);
            mCKB_deviceSelection = (CheckBox) itemView.findViewById(R.id.ckb_chart_device_check);



        }

        @Override
        public void onClick(View view) {
            if(mCKB_deviceSelection.isChecked());
            {
                Log.e(TAG,"got!" + mSlectPosition);
            }
            //mCheckList.add(mDataSet.get(mSlectPosition));
    }
    }
}
