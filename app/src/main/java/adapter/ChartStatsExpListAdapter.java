package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.notemel.deviceappalphav02.R;

import java.util.ArrayList;

/**
 * Created by Melchior_S on 2016-10-10.
 * 참조 : http://straight-strange.tistory.com/13
 */

public class ChartStatsExpListAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> mGroupList = new ArrayList<>();
    private ArrayList<ArrayList<String>> mChildList = new ArrayList<>();

    private LayoutInflater mInflater;
    private Context mContext;

    public ChartStatsExpListAdapter(Context context, ArrayList<String>groupList, ArrayList<ArrayList<String>>childList)
    {
        this.mContext = context;
        this.mGroupList = groupList;
        this.mChildList = childList;
        //mInflater = LayoutInflater.from(context);

    }
    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater groupInfla = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = groupInfla.inflate(R.layout.explist_group_item_chart_stats, parent, false);
        }
        TextView groupTitle = (TextView)convertView.findViewById(R.id.tv_group_title);
        groupTitle.setText(mGroupList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater childInfla = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = childInfla.inflate(R.layout.explist_child_item_chart_stats, parent, false);
        }
        TextView childTitle = (TextView)convertView.findViewById(R.id.tv_child_name);
        childTitle.setText(getChild(groupPosition,childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
