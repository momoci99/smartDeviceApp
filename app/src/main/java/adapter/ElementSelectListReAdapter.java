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
    private int mSelectedItemNumber = -1;
    public static String mSelectedElement = "";


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
        holder.mRBTN_elementSelection.setChecked(position == mSelectedItemNumber);
        //RadioButton?
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTV_elementName;
        RadioButton mRBTN_elementSelection;

        ViewHolder(View itemView) {
            super(itemView);

            mTV_elementName = (TextView) itemView.findViewById(R.id.tv_element_select);
            mRBTN_elementSelection = (RadioButton) itemView.findViewById(R.id.rbt_element_select);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedItemNumber = getAdapterPosition();
                    mSelectedElement = mDataSet.get(getAdapterPosition());

                    notifyItemRangeChanged(0, mDataSet.size());
                    Log.e(TAG,"selected item : "+mDataSet.get(getAdapterPosition()));

                }
            };
            itemView.setOnClickListener(clickListener);
            mRBTN_elementSelection.setOnClickListener(clickListener);

        }
    }
}

