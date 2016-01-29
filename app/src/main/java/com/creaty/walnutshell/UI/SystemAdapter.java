package com.creaty.walnutshell.UI;

import com.creaty.walnutshell.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @deprecated*/
public class SystemAdapter extends BaseAdapter{

	Context context;
	int num;
	String[] titles;
	int[] imgId = { R.drawable.app_logo,
			R.drawable.app_logo,
			R.drawable.app_logo};
	
	public SystemAdapter( Context context ){
		this.context = context;
		titles = context.getResources().getStringArray(R.array.system_array);
		this.num = titles.length;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return num;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return titles[ position ];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if( convertView == null ){
			LayoutInflater layinf = null;  
		    layinf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		    View root = layinf.inflate(R.layout.drawer_list_second_item,
		    		parent, false);
		    TextView tv = (TextView) root.findViewById(R.id.drawer_sec_item_nameview);
		    tv.setText( titles[ position ]);
		    ImageView iv = (ImageView) root.findViewById(R.id.drawer_sec_item_imgview);
		    iv.setImageResource( imgId[ position] );
		   return root;
		}
		else
			return convertView;
	}

}
