package com.creaty.walnutshell.content_provider;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.creaty.tester.BaseTester;
import com.creaty.tester.BookProviderMetaData;
import com.creaty.tester.IReportBack;
import com.creaty.walnutshell.basic.Source;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.EntryTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;

public class ProviderTest extends BaseTester{
	private static String tag = "Provider Tester";
	
	public ProviderTest(Context ctx, IReportBack target)
	{
		super(ctx, target);
	}
	public void addNewspaper()
	{

		Log.d(tag,"Adding a newspaper");
		ContentValues cv = new ContentValues();
		cv.put(NewsTableMetaData.NAME, "newspaper" + getNewspaperCount() );
		cv.put(NewsTableMetaData.FREQENCE, 2);
		cv.put(NewsTableMetaData.ALARM_TIME, "18:56;8:0;");
		cv.put(NewsTableMetaData.CREATED_DATE, System.currentTimeMillis());
		
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = NewsTableMetaData.CONTENT_URI;
		Log.d(tag,"newspaper insert uri:" + uri);
		Uri insertedUri = cr.insert(uri, cv);
		Log.d(tag,"inserted uri:" + insertedUri);
		this.reportString("Inserted Uri:" + insertedUri);
	}
	public void addSource()
	{
		Log.d(tag,"Adding a source");
		ContentValues cv = new ContentValues();
		cv.put(SourceTableMetaData.NEWS_ID, 1); 
		cv.put(SourceTableMetaData.PAGE_NAME, "pagename1");
		cv.put(SourceTableMetaData.PAGE_DESCRIPTION, "unkonwn");
		cv.put(SourceTableMetaData.PAGE_LOGO, "xx/xx/xx");
		cv.put(SourceTableMetaData.PAGE_ADDRESS,"http://dsda.com");
		cv.put(SourceTableMetaData.SOURCE_NAME, "dad?");
		cv.put(SourceTableMetaData.SOURCE_DESCRIPTION,"?ddddddddddddd");
		cv.put(SourceTableMetaData.TYPE, Source.RSS_SOURCE);
		cv.put(SourceTableMetaData.CREATED_DATE, System.currentTimeMillis());
		cv.put(SourceTableMetaData.MODIFIED_DATE, System.currentTimeMillis());
		
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = SourceTableMetaData.CONTENT_URI;
		Log.d(tag,"source insert uri:" + uri);
		Uri insertedUri = cr.insert(uri, cv);
		Log.d(tag,"inserted uri:" + insertedUri);
		this.reportString("Inserted Uri:" + insertedUri);
	}
	public void addEntry()
	{
		Log.d(tag,"Adding a entry");
		ContentValues cv = new ContentValues();
		cv.put(EntryTableMetaData.NAME, "name2");
		cv.put(EntryTableMetaData.DESCRIPTION, "ddddddefe");
		cv.put(EntryTableMetaData.SOURCE_ID, 1);
		cv.put(EntryTableMetaData.CONTENT_ADDRESS, "http://sdsa.com");
		cv.put(EntryTableMetaData.CONTENT, "//////\nasdasda\nssssssssssssss");
		cv.put(EntryTableMetaData.CREATED_DATE, System.currentTimeMillis());
		cv.put(EntryTableMetaData.MODIFIED_DATE, System.currentTimeMillis());
		
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = EntryTableMetaData.CONTENT_URI;
		Log.d(tag,"entry insert uri:" + uri);
		Uri insertedUri = cr.insert(uri, cv);
		Log.d(tag,"inserted uri:" + insertedUri);
		this.reportString("Inserted Uri:" + insertedUri);
	}
	public void removeNewspaper()
	{
		int i = getNewspaperCount();
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = NewsTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(i));
		reportString("Del Uri:" + delUri);
		cr.delete(delUri, null, null);
		this.reportString("Newcount:" + i);
	}
	public void removeSource()
	{
		int i = getSourceCount();
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = SourceTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(i));
		reportString("Del Uri:" + delUri);
		cr.delete(delUri, null, null);
		this.reportString("Newcount:" + i);
	}
	public void removeEntry()
	{
		int i = getEntryCount();
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = EntryTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(i));
		reportString("Del Uri:" + delUri);
		cr.delete(delUri, null, null);
		this.reportString("Newcount:" + i );
	}
	public void showNewspapers()
	{
		Uri uri = NewsTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		/* managedQuery():
		Warning: Do not call close() on a cursor obtained using this method,
		because the activity will do that for you at the appropriate time.
		However, if you call stopManagingCursor(Cursor) on a cursor from a managed query,
		the system will not automatically close the cursor and,
		in that case, you must call close(). */
		Cursor c = a.managedQuery(uri, 
				null, //projection
				null, //selection string
				null, //selection args array of strings
				null); //sort order
		int iid = c.getColumnIndex(NewsTableMetaData._ID);
		int iname = c.getColumnIndex(NewsTableMetaData.NAME);
		int ifreqence = c.getColumnIndex(NewsTableMetaData.FREQENCE);
		int ialarm_time = c.getColumnIndex(NewsTableMetaData.ALARM_TIME);
		
		//Report your indexes
		reportString("id, name, freqence, alarm_time:"
				+ iid +" " +  iname +" " + ifreqence +" " + ialarm_time );
		
		//walk through the rows based on indexes
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			//Gather values
			String id = c.getString(iid);
			String name = c.getString(iname);
			int freqence = c.getInt(ifreqence);
			String alarm_time = c.getString(ialarm_time);
			
			//Report or log the row
			StringBuffer cbuf = new StringBuffer(id);
			cbuf.append(",").append(name);
			cbuf.append(",").append(freqence);
			cbuf.append(",").append(alarm_time);
			reportString(cbuf.toString());
		}
		
		//Report how many rows have been read
		int numberOfRecords = c.getCount();
		reportString("Num of Records:" + numberOfRecords);
		
	}
	public void showSources()
	{
		Uri uri = SourceTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		/* managedQuery():
		Warning: Do not call close() on a cursor obtained using this method,
		because the activity will do that for you at the appropriate time.
		However, if you call stopManagingCursor(Cursor) on a cursor from a managed query,
		the system will not automatically close the cursor and,
		in that case, you must call close(). */
		Cursor c = a.managedQuery(uri, 
				null, //projection
				null, //selection string
				null, //selection args array of strings
				null); //sort order
		int iid = c.getColumnIndex(SourceTableMetaData._ID);
		int inews_id = c.getColumnIndex(SourceTableMetaData.NEWS_ID);
		int ipage_name = c.getColumnIndex(SourceTableMetaData.PAGE_NAME);
		int ipage_address = c.getColumnIndex(SourceTableMetaData.PAGE_ADDRESS);
		
		//Report your indexes
		reportString("new_id, page_name, page_address"
				+ inews_id+" "+ipage_name + " " +  ipage_address );
		
		//walk through the rows based on indexes
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			//Gather values
			String id = c.getString(iid);
			int news_id = c.getInt(inews_id);
			String page_name = c.getString(ipage_name);
			String page_address = c.getString(ipage_address);
			
			//Report or log the row
			StringBuffer cbuf = new StringBuffer(id);
			cbuf.append(",").append(news_id)
				.append(",").append(page_name)
				.append(",").append(page_address);
			reportString(cbuf.toString());
		}
		
		//Report how many rows have been read
		int numberOfRecords = c.getCount();
		reportString("Num of Records:" + numberOfRecords);
		
	}
	public void showEntries()
	{
		Uri uri = EntryTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		/* managedQuery():
		Warning: Do not call close() on a cursor obtained using this method,
		because the activity will do that for you at the appropriate time.
		However, if you call stopManagingCursor(Cursor) on a cursor from a managed query,
		the system will not automatically close the cursor and,
		in that case, you must call close(). */
		Cursor c = a.managedQuery(uri, 
				null, //projection
				null, //selection string
				null, //selection args array of strings
				null); //sort order
		int iid = c.getColumnIndex(EntryTableMetaData._ID);
		int isource_id = c.getColumnIndex(EntryTableMetaData.SOURCE_ID);
		int iname = c.getColumnIndex(EntryTableMetaData.NAME);
		int iadd = c.getColumnIndex(EntryTableMetaData.CONTENT_ADDRESS);
		int icontent = c.getColumnIndex(EntryTableMetaData.CONTENT);
		
		//Report your indexes
		reportString("source_id,name,content:" + isource_id + iname + icontent);
		
		//walk through the rows based on indexes
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			//Gather values
			String id = c.getString(iid);
			String source_id = c.getString(isource_id);
			String name = c.getString(iname);
			String address = c.getString(iadd);
			String content = c.getString(icontent);
			
			//Report or log the row
			StringBuffer cbuf = new StringBuffer(id);
			cbuf.append(",").append(source_id)
				.append(",").append(name)
				.append(",").append(address)
				.append(",").append(content);
			
			reportString(cbuf.toString());
		}
		
		//Report how many rows have been read
		int numberOfRecords = c.getCount();
		reportString("Num of Records:" + numberOfRecords);
		
	}
	private int getNewspaperCount()
	{
		Uri uri = NewsTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		//managedQuery():
		//Warning: Do not call close() on a cursor obtained using this method,
		//because the activity will do that for you at the appropriate time.
		//However, if you call stopManagingCursor(Cursor) on a cursor from a managed query,
		//the system will not automatically close the cursor and,
		//in that case, you must call close().
		Cursor c = a.managedQuery(uri, 
				null, //projection
				null, //selection string
				null, //selection args array of strings
				null); //sort order
		int numberOfRecords = c.getCount();

		return numberOfRecords;
	}
	private int getSourceCount()
	{
		Uri uri = SourceTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		//managedQuery():
		//Warning: Do not call close() on a cursor obtained using this method,
		//because the activity will do that for you at the appropriate time.
		//However, if you call stopManagingCursor(Cursor) on a cursor from a managed query,
		//the system will not automatically close the cursor and,
		//in that case, you must call close().
		Cursor c = a.managedQuery(uri, 
				null, //projection
				null, //selection string
				null, //selection args array of strings
				null); //sort order
		int numberOfRecords = c.getCount();

		return numberOfRecords;
	}
	private int getEntryCount()
	{
		Uri uri = EntryTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		//managedQuery():
		//Warning: Do not call close() on a cursor obtained using this method,
		//because the activity will do that for you at the appropriate time.
		//However, if you call stopManagingCursor(Cursor) on a cursor from a managed query,
		//the system will not automatically close the cursor and,
		//in that case, you must call close().
		Cursor c = a.managedQuery(uri, 
				null, //projection
				null, //selection string
				null, //selection args array of strings
				null); //sort order
		int numberOfRecords = c.getCount();

		return numberOfRecords;
	}
	
	private void report(int stringid)
	{
		this.mReportTo.reportBack(tag,this.mContext.getString(stringid));
	}
	private void reportString(String s)
	{
		this.mReportTo.reportBack(tag,s);
	}
	private void reportString(String s, int stringid)
	{
		this.mReportTo.reportBack(tag,s);
		report(stringid);
	}
}
