package adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.notemel.deviceappalphav02.R;

import java.util.ArrayList;

/**
 * Created by noteMel on 2016-09-28.
 */
public class ElementSelectListReAdapter extends RecyclerView.Adapter<ElementSelectListReAdapter.ViewHolder> {


    private final String TAG = "ES.ListReAdapter";
    private ArrayList<String> mDataSet;
    public static int mSelectedItem = -1;


    public ElementSelectListReAdapter (ArrayList<String> dataSet)
    {
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTV_elementName.setText(mDataSet.get(position));
        holder.mRBTN_elementSelection.setChecked(position == mSelectedItem);
        //RadioButton?
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTV_elementName;
        public RadioButton mRBTN_elementSelection;

        public ViewHolder(View itemView) {
            super(itemView);

            mTV_elementName = (TextView) itemView.findViewById(R.id.tv_element_select);
            mRBTN_elementSelection = (RadioButton) itemView.findViewById(R.id.rbt_element_select);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, mDataSet.size());
                    Log.e(TAG,"Position"+mSelectedItem);
                }
            };
            itemView.setOnClickListener(clickListener);
            mRBTN_elementSelection.setOnClickListener(clickListener);

        }
    }
}

