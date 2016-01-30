package com.creaty.walnutshell;

import java.io.IOException;
import java.net.URL;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.creaty.walnutshell.basic.Source;
import com.creaty.walnutshell.content_provider.DataBridge;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;
import com.creaty.walnutshell.fang.InterfaceImplement;
import com.creaty.walnutshell.fang.InternetInterface2;

public class UpdateSourceTask extends AsyncTask<Integer, Void, Integer> {
	public static final int ALARM_TRIGER = 101;
	public static final int NEWS_FRAG_TRIGER = 102;
	public static final int NET_ERROR = -1;
	Context mContext;
	Handler completeHandler;
	int newsId = -1;
	int netErrorCode = 0;
	int triger = -1; // 记录是什么组件触发该任务

	public UpdateSourceTask(Context context, Handler h) {
		mContext = context;
		completeHandler = h;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO 自动生成的方法存根
		//Log.d(tag, "start UpdateSourceTask");
		InternetInterface2 ii = new InterfaceImplement();
		ContentResolver cr = mContext.getContentResolver();
		newsId = params[0]; // 报刊ID
		triger = params[1];
		Source s = null;
		//Log.d(tag, "start UpdateSourceTask news: " + newsId);
		Cursor c = cr.query(SourceTableMetaData.CONTENT_URI, new String[] {
				SourceTableMetaData._ID, SourceTableMetaData.PAGE_ADDRESS,
				SourceTableMetaData.TYPE, SourceTableMetaData.SOURCE_SEQ,
				SourceTableMetaData.SOURCE_STRATEGY },
				SourceTableMetaData.NEWS_ID + " = " + newsId, null, null); //
		int iid = c.getColumnIndex(SourceTableMetaData._ID);
		int ipa = c.getColumnIndex(SourceTableMetaData.PAGE_ADDRESS);
		int itype = c.getColumnIndex(SourceTableMetaData.TYPE);
		int iseq = c.getColumnIndex(SourceTableMetaData.SOURCE_SEQ);
		int istra = c.getColumnIndex(SourceTableMetaData.SOURCE_STRATEGY);
		int update = 0; // 成功更新的源的数目
		DataBridge db = new DataBridge(mContext);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			try {
				if (c.getInt(itype) == Source.RSS_SOURCE) {
					//Log.d(tag, "source is rss" + c.getString(ipa));
					s = ii.getRssSource(new URL(c.getString(ipa)));
				} else {
					//Log.d(tag, "source is self");
					s = ii.getSelfSource(new URL(c.getString(ipa)),
							c.getInt(iseq), c.getInt(istra));
				}
				db.updateSource(c.getInt(iid), s);
				update++;
			} catch (IOException e) {
				netErrorCode = NET_ERROR;
				//Log.d(tag, "net error");
				e.printStackTrace();
			}
		}
		c.close();
		db = null;
		// DataBridge db = new DataBridge(mContext);
		// db.testUpdate2();
		//Log.d(tag, "UpdateSourceTask finished");
		return update;// update;
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		// Toast.makeText(UpdateService.this, "" + result,
		// Toast.LENGTH_SHORT)
		// .show();
		mContext = null;
		completeHandler.sendMessage(completeHandler.obtainMessage(triger,
				newsId, result));
	}

}