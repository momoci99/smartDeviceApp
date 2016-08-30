package adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.notemel.deviceappalphav02.R;

import java.util.List;

public class SearchedDeviceListAdapter extends BaseAdapter {


	private LayoutInflater mInflater;
	//LayoutInflater - xml 문서에 명시된 레이아웃을 구현하여 메모리에 적재.

	private List<BluetoothDevice> mData;


	public SearchedDeviceListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }
	
	public void setData(List<BluetoothDevice> data) {
		mData = data;
	}





	@Override
	public int getCount() {
		return (mData == null) ? 0 : mData.size();
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



	/**
	설명
	ListView에서 데이터를 스크롤 하면 데이터를 볼때마다 View를 inflate 하게되는데
	이는 메모리와 성능상에 상당한 손실을 가져온다.

	볼때마다 inflate 하면 ㅎㄷㄷ;;;

	무튼 이 View를 재활용하기위해
	ListView는 아이템의 크기만큼이 아닌 화면에 보여지는 아이템 갯수 만큼의
	View를 그려놓은 후 재활용하는 구조로 설계되었다.

	실제 화면에 그려지는 아이템 - ScrapView라는 배열로 관리
	 ListView의 재활용 View인 ScrapView는 Adapter의 getView()를 통해서 관리

	 ScrapView가 convertView이다.

	에..요지는 결국 최초 한번만 inflate 해두고 계속 재활용한다
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {			
			convertView			=  mInflater.inflate(R.layout.list_item_device, null);
			
			holder 				= new ViewHolder();
			
			holder.nameTv		= (TextView) convertView.findViewById(R.id.tv_device_name);
			holder.addressTv 	= (TextView) convertView.findViewById(R.id.tv_data);

			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		BluetoothDevice device	= mData.get(position);
		
		holder.nameTv.setText(device.getName());
		holder.addressTv.setText(device.getAddress());






        return convertView;



	}

	//ViewHolder Pattern - 빠르게 불러올수있다.
	static class ViewHolder {
		TextView nameTv;
		TextView addressTv;


	}


}