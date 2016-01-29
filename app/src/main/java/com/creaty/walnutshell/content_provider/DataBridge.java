package com.creaty.walnutshell.content_provider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.creaty.walnutshell.basic.ContentDetail;
import com.creaty.walnutshell.basic.Entry;
import com.creaty.walnutshell.basic.Newspaper;
import com.creaty.walnutshell.basic.PageDetail;
import com.creaty.walnutshell.basic.Source;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.EntryTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;

public class DataBridge {
	static String tag = "DataBridge";
	Context mContext;

	public DataBridge(Context context) {
		mContext = context;
	}

	public void initNewspapers() {
		ContentResolver cr = this.mContext.getContentResolver();
		Cursor c = cr.query(NewsTableMetaData.CONTENT_URI, null, null, null,
				null);
		String[] name = {"新闻", "IT", "生活", "体育", "时尚", "科学", "杂谈" };
		if (c.getCount() == 0) {
			ContentValues cv = new ContentValues();
			for( int i = 0; i < 7; i++){
				cv.put(NewsTableMetaData.NAME, name[ i ]);
				cv.put(NewsTableMetaData.FREQENCE, 3);
				cv.put(NewsTableMetaData.ALARM_TIME, "8:0;17:30;21:00");
				cv.put(NewsTableMetaData.CREATED_DATE, System.currentTimeMillis());
				Uri uri = NewsTableMetaData.CONTENT_URI;
				Log.d(tag, "newspaper insert uri:" + uri);
				Uri insertedUri = cr.insert(uri, cv);
			}
		}
		c.close();
	}

	public void addNewspaper(Newspaper news) {
		Log.d(tag, "Adding a newspaper");
		ContentValues cv = new ContentValues();
		cv.put(NewsTableMetaData.NAME, news.name);
		cv.put(NewsTableMetaData.FREQENCE, news.frequence);
		cv.put(NewsTableMetaData.ALARM_TIME,
				ProviderUtils.alarmTimeToString(news.alarmTime));
		cv.put(NewsTableMetaData.CREATED_DATE, System.currentTimeMillis());

		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = NewsTableMetaData.CONTENT_URI;
		Log.d(tag, "newspaper insert uri:" + uri);
		Uri insertedUri = cr.insert(uri, cv);
		Log.d(tag, "inserted uri:" + insertedUri);
	}

	/**
	 * @retrun true if add sucessfully
	 */
	public Uri addSource(int news_id, PageDetail page, Source source) {
		Log.d(tag, "Adding a source");

		ContentResolver cr = this.mContext.getContentResolver();
		// 检查是否有相同源
		Cursor c = cr.query(SourceTableMetaData.CONTENT_URI,
				new String[] { SourceTableMetaData._ID },
				SourceTableMetaData.PAGE_ADDRESS + " LIKE '"
						+ source.page_address + "' AND "
						+ SourceTableMetaData.SOURCE_SEQ + " = "
						+ source.sourceSeq + " AND "
						+ SourceTableMetaData.NEWS_ID + " = " + news_id, null,
				null);
		if (c.getCount() == 0) {
			ContentValues cv = new ContentValues();
			cv.put(SourceTableMetaData.NEWS_ID, news_id);
			cv.put(SourceTableMetaData.PAGE_NAME, page.pageName);
			cv.put(SourceTableMetaData.PAGE_DESCRIPTION, page.description);
			cv.put(SourceTableMetaData.PAGE_LOGO, page.logo);
			cv.put(SourceTableMetaData.PAGE_ADDRESS, source.page_address);
			cv.put(SourceTableMetaData.SOURCE_NAME, source.name);
			cv.put(SourceTableMetaData.SOURCE_SEQ, source.sourceSeq);
			cv.put(SourceTableMetaData.SOURCE_STRATEGY, source.strategyType);
			cv.put(SourceTableMetaData.SOURCE_DESCRIPTION, source.description);
			cv.put(SourceTableMetaData.TYPE, source.type);
			cv.put(SourceTableMetaData.CREATED_DATE, System.currentTimeMillis());
			cv.put(SourceTableMetaData.MODIFIED_DATE,
					System.currentTimeMillis());

			Uri uri = SourceTableMetaData.CONTENT_URI;
			Log.d(tag, "source insert uri:" + uri);
			Uri insertedUri = cr.insert(uri, cv);
			Log.d(tag, "inserted uri:" + insertedUri);
			// add entries
			int source_id = Integer.valueOf(insertedUri.getLastPathSegment());
			for (Iterator<Entry> iter = source.entries.iterator(); iter
					.hasNext();) {
				addEntry(source_id, iter.next());
			}
			c.close();
			return insertedUri;
		} else {
			c.close();
			return null;
		}
	}

	public Uri addEntry(int source_id, Entry en) {
		Log.d(tag, "Adding a entry");
		ContentResolver cr = this.mContext.getContentResolver();
		// 检查是否有相同entry
		Cursor c = cr.query(EntryTableMetaData.CONTENT_URI,
				new String[] { EntryTableMetaData._ID },
				EntryTableMetaData.CONTENT_ADDRESS + " LIKE '" + en.href + "'",
				null, null);
		if (c.getCount() == 0) {
			ContentValues cv = new ContentValues();
			cv.put(EntryTableMetaData.NAME, en.name);
			cv.put(EntryTableMetaData.SOURCE_ID, source_id);
			cv.put(EntryTableMetaData.CONTENT_ADDRESS, en.href);
			cv.put(EntryTableMetaData.DESCRIPTION, en.description);
			cv.put(EntryTableMetaData.CREATED_DATE, System.currentTimeMillis());
			cv.put(EntryTableMetaData.MODIFIED_DATE, System.currentTimeMillis());

			Uri uri = EntryTableMetaData.CONTENT_URI;
			Log.d(tag, "entry insert uri:" + uri);
			Uri insertedUri = cr.insert(uri, cv);
			Log.d(tag, "inserted uri:" + insertedUri);
			c.close();
			return insertedUri;
		} else {
			c.close();
			return null;
		}
	}

	public void removeNewspaper(int id) {
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = NewsTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(id));

		cr.delete(delUri, null, null);

	}

	public void removeSource(int id) {
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = SourceTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(id));
		cr.delete(delUri, null, null);

	}

	public void removeEntry(int id) {
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = EntryTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(id));

		cr.delete(delUri, null, null);

	}

	public void updateNewspaper(int id, Newspaper news) {

		ContentValues cv = new ContentValues();
		if (!news.name.isEmpty())
			cv.put(NewsTableMetaData.NAME, news.name);
		if (news.frequence >= 0)
			cv.put(NewsTableMetaData.FREQENCE, news.frequence);
		if (!news.alarmTime.isEmpty())
			cv.put(NewsTableMetaData.ALARM_TIME,
					ProviderUtils.alarmTimeToString(news.alarmTime));

		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = NewsTableMetaData.CONTENT_URI;
		Uri updateUri = Uri.withAppendedPath(uri, Integer.toString(id));
		cr.update(updateUri, cv, null, null);

	}

	public int updateEntry(Uri entryUri, ContentDetail cd) {
		Entry en = cd.basicInfor;
		ContentValues cv = new ContentValues();
		// if (en != null) {
		// if (en.name != null && !en.name.isEmpty())
		// cv.put(EntryTableMetaData.NAME, en.name);
		// if (en.href != null && !en.href.isEmpty())
		// cv.put(EntryTableMetaData.CONTENT_ADDRESS, en.href);
		// if (en.description != null && !en.description.isEmpty())
		// cv.put(EntryTableMetaData.DESCRIPTION, en.description);
		// }
		if (cd.author != null && !cd.author.isEmpty())
			cv.put(EntryTableMetaData.AUTHOR, cd.author);
		if (cd.content != null && !cd.content.isEmpty())
			cv.put(EntryTableMetaData.CONTENT,
					ProviderUtils.listToString(cd.content));
		cv.put(EntryTableMetaData.MODIFIED_DATE, System.currentTimeMillis());

		ContentResolver cr = this.mContext.getContentResolver();
		Uri updateUri = entryUri;

		// write content
		// ProviderUtils.writeContent(cr, updateUri, cd.content);
		// cv.put(EntryTableMetaData.CONTENT, cd.content);
		Log.d(tag, "inserted uri:" + updateUri);
		return cr.update(updateUri, cv, null, null);
	}

	public Source getSource(Cursor c, int sourceId) {
		int iid = c.getColumnIndex(SourceTableMetaData._ID);
		try {
			if (c.getInt(iid) != sourceId)
				throw new Exception(
						"Source cursor has not moved to correct position. source_id: "
								+ sourceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// int inews_id = c.getColumnIndex(SourceTableMetaData.NEWS_ID);
		// int ipage_name = c.getColumnIndex(SourceTableMetaData.PAGE_NAME);
		int ipage_address = c.getColumnIndex(SourceTableMetaData.PAGE_ADDRESS);
		int isource_name = c.getColumnIndex(SourceTableMetaData.SOURCE_NAME);
		int isource_des = c
				.getColumnIndex(SourceTableMetaData.SOURCE_DESCRIPTION);
		int itype = c.getColumnIndex(SourceTableMetaData.TYPE);
		// int icreated = c.getColumnIndex(SourceTableMetaData.CREATED_DATE);
		int imodified = c.getColumnIndex(SourceTableMetaData.MODIFIED_DATE);

		Source s = new Source();
		s.page_address = c.getString(ipage_address);
		s.sourceSeq = 0; // not implement
		s.name = c.getString(isource_name);
		s.description = c.getString(isource_des);
		s.type = c.getInt(itype);
		s.modified_date = c.getLong(imodified);

		// Log.d(tag, "source modified time: " + s.modified_date);

		ContentResolver cr = mContext.getContentResolver();
		Cursor ec = cr.query(EntryTableMetaData.CONTENT_URI, new String[] {
				EntryTableMetaData._ID, EntryTableMetaData.NAME,
				EntryTableMetaData.DESCRIPTION,
				EntryTableMetaData.CONTENT_ADDRESS },
				EntryTableMetaData.SOURCE_ID + " = " + sourceId, null, null);

		int ie_id = ec.getColumnIndex(EntryTableMetaData._ID);
		int ie_name = ec.getColumnIndex(EntryTableMetaData.NAME);
		int ie_add = ec.getColumnIndex(EntryTableMetaData.CONTENT_ADDRESS);
		int ie_des = ec.getColumnIndex(EntryTableMetaData.DESCRIPTION);

		ArrayList<Entry> es = new ArrayList<Entry>();
		Entry e = null;
		for (ec.moveToFirst(); !ec.isAfterLast(); ec.moveToNext()) {
			Log.d(tag, "entry id " + ec.getLong(ie_id));
			e = new Entry(ec.getLong(ie_id), ec.getString(ie_name),
					ec.getString(ie_add), ec.getString(ie_des));
			Log.d(tag, e.toString());
			es.add(e);
		}
		s.entries = es;

		// close cusor
		ec.close();

		return s;
	}

	public String getSourceName(int sourceId) {
		ContentResolver cr = mContext.getContentResolver();
		Cursor c = cr.query(
				Uri.withAppendedPath(SourceTableMetaData.CONTENT_URI,
						String.valueOf(sourceId)),
				new String[] { SourceTableMetaData.SOURCE_NAME }, null, null,
				null);
		c.moveToFirst();
		int isource_name = c.getColumnIndex(SourceTableMetaData.SOURCE_NAME);

		c.close();

		return c.getString(isource_name);
	}

	public ContentDetail getEntry(Uri entryUri) {
		ContentResolver cr = mContext.getContentResolver();
		Cursor ec = cr.query(entryUri, null, null, null, null);
		ec.moveToFirst();
		// Log.d(tag, "getEntry count: " + ec.getCount() );
		int iid = ec.getColumnIndex(EntryTableMetaData._ID);
		int iname = ec.getColumnIndex(EntryTableMetaData.NAME);
		int iadd = ec.getColumnIndex(EntryTableMetaData.CONTENT_ADDRESS);
		int ides = ec.getColumnIndex(EntryTableMetaData.DESCRIPTION);
		int iaut = ec.getColumnIndex(EntryTableMetaData.AUTHOR);
		int icont = ec.getColumnIndex(EntryTableMetaData.CONTENT);
		int imod = ec.getColumnIndex(EntryTableMetaData.MODIFIED_DATE);
		ContentDetail cd = new ContentDetail();
		cd.basicInfor = new Entry(ec.getLong(iid), ec.getString(iname),
				ec.getString(iadd), ec.getString(ides));
		cd.author = ec.getString(iaut);
		cd.modified_date = ec.getLong(imod);
		if (!ec.isNull(icont)) {
			cd.content = ProviderUtils.stringToList(ec.getString(icont));
		}
		ec.close();

		return cd;
	}

	// //数据库支持的文件读写
	// public void setContent( ArrayList<String> content, Uri entryUri ){
	// ContentResolver cr = mContext.getContentResolver();
	// try {
	// BufferedOutputStream bos = new BufferedOutputStream(
	// cr.openOutputStream(entryUri) );
	// int size = content.size();
	// for( int i = 0; i < size; i++){
	// bos.write( ( (String)content.get(i) + "\r\n").getBytes() );
	// }
	// bos.close();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// public ArrayList<String> getContent( Uri entryUri ){
	// ContentResolver cr = mContext.getContentResolver();
	// ArrayList<String> content = null;
	// try {
	// BufferedReader br = new BufferedReader(
	// new InputStreamReader( cr.openInputStream(entryUri) ) );
	// content = new ArrayList<String>();
	// String p = null;
	// while( ( p = br.readLine() ) != null ){
	// content.add( p );
	// }
	// br.close();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return content;
	// }

	public int getNewspaperCount() {
		Uri uri = NewsTableMetaData.CONTENT_URI;
		Activity a = (Activity) this.mContext;
		// managedQuery():
		// Warning: Do not call close() on a cursor obtained using this method,
		// because the activity will do that for you at the appropriate time.
		// However, if you call stopManagingCursor(Cursor) on a cursor from a
		// managed query,
		// the system will not automatically close the cursor and,
		// in that case, you must call close().
		Cursor c = a.managedQuery(uri, null, // projection
				null, // selection string
				null, // selection args array of strings
				null); // sort order
		int numberOfRecords = c.getCount();

		return numberOfRecords;
	}

	public int getSourceCount() {
		Uri uri = SourceTableMetaData.CONTENT_URI;
		Activity a = (Activity) this.mContext;
		// managedQuery():
		// Warning: Do not call close() on a cursor obtained using this method,
		// because the activity will do that for you at the appropriate time.
		// However, if you call stopManagingCursor(Cursor) on a cursor from a
		// managed query,
		// the system will not automatically close the cursor and,
		// in that case, you must call close().

		Cursor c = a.managedQuery(uri, null, // projection
				null, // selection string
				null, // selection args array of strings
				null); // sort order
		int numberOfRecords = c.getCount();

		return numberOfRecords;
	}

	public int getEntryCount() {
		Uri uri = EntryTableMetaData.CONTENT_URI;
		Activity a = (Activity) this.mContext;
		// managedQuery():
		// Warning: Do not call close() on a cursor obtained using this method,
		// because the activity will do that for you at the appropriate time.
		// However, if you call stopManagingCursor(Cursor) on a cursor from a
		// managed query,
		// the system will not automatically close the cursor and,
		// in that case, you must call close().
		Cursor c = a.managedQuery(uri, null, // projection
				null, // selection string
				null, // selection args array of strings
				null); // sort order
		int numberOfRecords = c.getCount();

		return numberOfRecords;
	}
}
