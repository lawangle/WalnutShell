package com.creaty.walnutshell.UI;

import com.creaty.walnutshell.R;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.EntryTableMetaData;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class EntryAdapter extends CursorAdapter{

	public Context mContext = null;
	public EntryAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	@Override
	public long getItemId(int position) {
		Cursor cursor = getCursor();
		cursor.moveToPosition(position);
		return cursor.getLong(cursor.getColumnIndex("_id"));
	}

	@Override
	public Object getItem(int position) {
		Cursor cursor = getCursor();
		cursor.moveToPosition(position);
		return Uri.withAppendedPath(EntryTableMetaData.CONTENT_URI,
				String.valueOf( cursor.getLong( cursor.getColumnIndex("_id") ) ) );
	}
	
	@Override
	public void bindView(View root, Context context, Cursor c) {
		// TODO Auto-generated method stub
		TextView tvna = (TextView) root
				.findViewById(R.id.newspaper_sec_item_nameview);
		TextView tvinf = (TextView) root
				.findViewById(R.id.newspaper_sec_item_infoview);
		tvna.setText(c.getString(c.getColumnIndex(EntryTableMetaData.NAME)));
		tvinf.setText("创建时间"
				+ Utils.getDateTimeString(c
						.getColumnIndex(EntryTableMetaData.CREATED_DATE)));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layinf = null;
		layinf = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View root = layinf.inflate(R.layout.newspaper_second_item,
				parent, false);
		return root;
	}

}
