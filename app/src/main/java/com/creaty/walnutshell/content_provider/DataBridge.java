package com.creaty.walnutshell.content_provider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

import com.creaty.walnutshell.R;
import com.creaty.walnutshell.alarm.AlarmUtils;
import com.creaty.walnutshell.basic.ContentDetail;
import com.creaty.walnutshell.basic.DayTime;
import com.creaty.walnutshell.basic.Entry;
import com.creaty.walnutshell.basic.Newspaper;
import com.creaty.walnutshell.basic.PageDetail;
import com.creaty.walnutshell.basic.Source;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.AlarmTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.EntryTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.LogoTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;

public class DataBridge {
	static final String tag = "DataBridge";
	Context mContext;
	ContentResolver cr;

	public DataBridge(Context context) {
		mContext = context;
		cr = mContext.getContentResolver();
	}

	public void initNewspapers() {
		Log.d(tag, "initNewspapers");
		Cursor c = cr.query(NewsTableMetaData.CONTENT_URI, null, null, null,
				null);
		String[] name = { "新闻", "互联网", "生活", "体育", "时尚", "科学", "杂谈" };
		String[] alarm = mContext.getResources().getStringArray(
				R.array.default_times);
		// String alarmString = alarm[ 0 ] + alarm[ 1 ] + alarm[ 2 ];
		ArrayList<DayTime> defaultAlarm = new ArrayList<DayTime>();
		defaultAlarm.add(new DayTime(alarm[0]));
		defaultAlarm.add(new DayTime(alarm[1]));
		defaultAlarm.add(new DayTime(alarm[2]));
		Log.d(tag, "Cursor count " + c.getCount());
		if (c.getCount() == 0) {
			for (int i = 0; i < 7; i++) {
				Uri insertedUri = addNewspaper(new Newspaper(name[i],
						defaultAlarm, System.currentTimeMillis()));
				Log.d(tag, insertedUri.toString());
			}
		}
		c.close();
		// testUpdate1();
	}

	public void testUpdate1() {
		PageDetail pd = new PageDetail();
		pd.pageName = "36�";
		pd.lastModifiedTime = System.currentTimeMillis();
		Source s = new Source();
		s.page_address = "http://www.36kr.com";
		s.name = "Դ1";
		s.type = Source.SELF_SOURCE;
		s.sourceSeq = 1;
		s.strategyType = Source.TABLE_STRATEGY;
		s.entries = new ArrayList<Entry>();
		ArrayList<Entry> en = s.entries;
		for (int i = 0; i < 10; i++) {
			en.add(new Entry(1, "��Ŀ" + i, "www.dasfafews.coane/dasdas" + i,
					"����" + i));
		}
		pd.ourSource = new ArrayList<Source>();
		pd.ourSource.add(s);
		System.out.println(tag + " testUpdate1 insert" + addSource(1, pd, s));
	}

	public void testUpdate3() {
		Cursor c = cr.query(EntryTableMetaData.CONTENT_URI,
				new String[] { EntryTableMetaData._ID },
				EntryTableMetaData.NAME + " LIKE '%30%' ", null, null);
		if (c.getCount() != 0) {
			c.moveToFirst();
			int id = c.getInt(c.getColumnIndex(EntryTableMetaData._ID));
			removeEntry(id);
		}
		c.close();

	}

	public void testUpdate2() {
		Source s = new Source();
		s.page_address = "http://www.36kr.com";
		s.name = "Դ1";
		s.type = Source.SELF_SOURCE;
		s.sourceSeq = 1;
		s.strategyType = Source.TABLE_STRATEGY;
		s.entries = new ArrayList<Entry>();
		ArrayList<Entry> en = s.entries;
		for (int i = 10; i < 20; i++) {
			en.add(new Entry(1, "��Ŀ" + i, "www.dasfafews.coane/dasdas" + i,
					"����" + i));
		}
		Cursor c = cr.query(SourceTableMetaData.CONTENT_URI,
				new String[] { SourceTableMetaData._ID }, null, null, null);
		c.moveToFirst();

		updateSource(c.getInt(c.getColumnIndex(SourceTableMetaData._ID)), s);
		c.close();
	}

	public Uri addNewspaper(Newspaper news) {
		Log.d(tag, "Adding a newspaper");
		ContentValues cv_news = new ContentValues();
		cv_news.put(NewsTableMetaData.NAME, news.name);
		cv_news.put(NewsTableMetaData.IS_READ, 1);
		// cv.put(NewsTableMetaData.FREQENCE, news.frequence);
		// cv.put(NewsTableMetaData.ALARM_TIME,
		// ProviderUtils.alarmTimeToString(news.alarmTime));
		cv_news.put(NewsTableMetaData.CREATED_DATE, System.currentTimeMillis());

		Uri uri = NewsTableMetaData.CONTENT_URI;
		// Log.d(tag, "newspaper insert uri:" + uri);
		Uri insertedUri = cr.insert(uri, cv_news);
		Log.d(tag, "inserted uri:" + insertedUri);
		// insert alarms
		if (insertedUri != null) {
			int size = news.alarmTime.size();
			for (int i = 0; i < size; i++) {
				addAlarm(Integer.parseInt((insertedUri.getLastPathSegment())),
						news.alarmTime.get(i));
			}
		}
		return insertedUri;
	}

	/**
	 * @retrun return Uri if add sucessfully, otherwise return null
	 */
	public Uri addAlarm(int news_id, DayTime dayTime) {
		ContentValues cv = new ContentValues();
		cv.put(AlarmTableMetaData.NEWS_ID, news_id);
		cv.put(AlarmTableMetaData.ALARM_TIME, dayTime.toString());
		cv.put(AlarmTableMetaData.CREATED_DATE, System.currentTimeMillis());
		Uri insertedUri = cr.insert(AlarmTableMetaData.CONTENT_URI, cv);
		return insertedUri;
	}

	/**
	 * @retrun return Uri if add sucessfully, otherwise return null
	 */
	public Uri addSource(int news_id, PageDetail page, Source source) {
		Log.d(tag, "Adding a source");

		// ����Ƿ�����ͬԴ
		Cursor c = cr.query(SourceTableMetaData.CONTENT_URI,
				new String[] { SourceTableMetaData._ID },
				SourceTableMetaData.PAGE_ADDRESS + " LIKE '"
						+ source.page_address + "' AND "
						+ SourceTableMetaData.SOURCE_SEQ + " = "
						+ source.sourceSeq + " AND "
						+ SourceTableMetaData.NEWS_ID + " = " + news_id, null,
				null);
		Uri insertedUri = null;
		if (c.getCount() == 0) {
			ContentValues cv = new ContentValues();
			cv.put(SourceTableMetaData.NEWS_ID, news_id);
			cv.put(SourceTableMetaData.PAGE_NAME, page.pageName);
			cv.put(SourceTableMetaData.IS_READ, 1);
			cv.put(SourceTableMetaData.PAGE_DESCRIPTION, page.description);
			cv.put(SourceTableMetaData.PAGE_LOGO, page.logoAddress);
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
			insertedUri = cr.insert(uri, cv);
			Log.d(tag, "inserted uri:" + insertedUri);
			if (insertedUri != null) {

				int source_id = Integer.valueOf(insertedUri
						.getLastPathSegment());
				// add logo
				addLogo(page.logoAddress, source_id);
				// add entries
				ArrayList<Entry> es = source.entries;
				int size = es.size();
				// ������ҳ������Ŀһ��ĸ��µ�ʱ��˳�򣬽��µ���Ŀ������
				// ��������Դ��˳��Ӻ���ǰ
				for (int i = size - 1; i >= 0; i--) {
					addEntry(source_id, es.get(i));
				}
			}
		}
		c.close();
		return insertedUri;
	}

	public Uri addEntry(int source_id, Entry en) {
		Log.d(tag, "Adding a entry");

		// ����Ƿ�����ͬentry
		Cursor c = cr.query(EntryTableMetaData.CONTENT_URI,
				new String[] { EntryTableMetaData._ID },
				EntryTableMetaData.CONTENT_ADDRESS + " LIKE '" + en.href + "'",
				null, null);
		Uri insertedUri = null;
		if (c.getCount() == 0) {
			long now = System.currentTimeMillis();

			ContentValues cv = new ContentValues();
			cv.put(EntryTableMetaData.NAME, en.name);
			cv.put(EntryTableMetaData.IS_READ, 0);
			cv.put(EntryTableMetaData.SOURCE_ID, source_id);
			cv.put(EntryTableMetaData.CONTENT_ADDRESS, en.href);
			cv.put(EntryTableMetaData.DESCRIPTION, en.description);
			cv.put(EntryTableMetaData.CREATED_DATE, now);
			cv.put(EntryTableMetaData.MODIFIED_DATE, now);

			Uri uri = EntryTableMetaData.CONTENT_URI;
			Log.d(tag, "entry insert uri:" + uri);
			insertedUri = cr.insert(uri, cv);
			Log.d(tag, "time: " + now + " inserted uri:" + insertedUri);
		}
		c.close();
		return insertedUri;
	}

	public Uri addLogo(String LogoAddress, int source_id) {
		// ����Ƿ����Ѿ���Logo
		Cursor c = cr.query(LogoTableMetaData.CONTENT_URI,
				new String[] { LogoTableMetaData._ID },
				LogoTableMetaData.SOURCE_ID + " = " + source_id, null, null);
		Uri insertedUri = null;
		if (c.getCount() == 0) {
			long now = System.currentTimeMillis();
			ContentValues cv = new ContentValues();
			cv.put(LogoTableMetaData.DATA, LogoAddress);
			cv.put(LogoTableMetaData.SOURCE_ID, source_id);
			cv.put(LogoTableMetaData.DISPLAY_NAME, LogoAddress);
			cv.put(LogoTableMetaData.CREATED_DATE, now);
			insertedUri = cr.insert(LogoTableMetaData.CONTENT_URI, cv);
		}
		c.close();
		return insertedUri;
	}

	public void removeNewspaper(int id) {

		Uri uri = NewsTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(id));

		cr.delete(delUri, null, null);

	}

	public void removeAllAlarm(int news_id) {
		Uri uri = AlarmTableMetaData.CONTENT_URI;
		cr.delete(uri, AlarmTableMetaData.NEWS_ID + " = " + news_id, null);
	}

	public void removeAlarm(int id) {
		Uri uri = AlarmTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(id));

		cr.delete(delUri, null, null);
	}

	public void removeSource(int id) {

		Uri uri = SourceTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(id));
		cr.delete(delUri, null, null);

	}

	public void removeEntry(int id) {

		Uri uri = EntryTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(id));

		cr.delete(delUri, null, null);

	}

	/**
	 * ���±�������
	 * 
	 * @param id
	 *            ����id
	 * @param news
	 *            �����úõı���
	 */
	public void updateNewspaper(int id, Newspaper news) {

		ContentValues cv = new ContentValues();
		if (!news.name.isEmpty())
			cv.put(NewsTableMetaData.NAME, news.name);

		removeAllAlarm(id);
		int size = news.alarmTime.size();
		for (int i = 0; i < size; i++) {
			addAlarm(id, news.alarmTime.get(i));
		}
		// if (news.frequence >= 0)
		// cv.put(NewsTableMetaData.FREQENCE, news.frequence);
		// if (!news.alarmTime.isEmpty())
		// cv.put(NewsTableMetaData.ALARM_TIME,
		// ProviderUtils.alarmTimeToString(news.alarmTime));

		Uri uri = NewsTableMetaData.CONTENT_URI;
		Uri updateUri = Uri.withAppendedPath(uri, Integer.toString(id));
		cr.update(updateUri, cv, null, null);
	}
	public void updateNewsPaper( int id, boolean isRead){
		ContentValues cv = new ContentValues();
		cv.put(NewsTableMetaData.IS_READ, isRead ? 1 : 0);
		Uri uri = NewsTableMetaData.CONTENT_URI;
		Uri updateUri = Uri.withAppendedPath(uri, Integer.toString(id));
		cr.update(updateUri, cv, null, null);
	}

	/**
	 * ����Դ�а�������Ŀ
	 * 
	 * @return ������Ŀ����Ŀ
	 */
	public int updateSource(int id, Source s) {
		int nc = 0;
		if (s != null && s.entries != null && !s.entries.isEmpty()) {

			// ����Source
			ContentValues cv = new ContentValues();
			cv.put(SourceTableMetaData.MODIFIED_DATE,
					System.currentTimeMillis());
			cv.put(SourceTableMetaData.IS_READ, 0);
			cr.update(Uri.withAppendedPath(SourceTableMetaData.CONTENT_URI,
					String.valueOf(id)), cv, null, null);

			int size = s.entries.size();
			Uri uri = null;
			// ������ҳ������Ŀһ��ĸ��µ�ʱ��˳�򣬽��µ���Ŀ������
			// ��������Դ��˳��Ӻ���ǰ
			for (int i = size - 1; i >= 0; i--) {
				// �Ѿ����ڵ�entry���������
				if ((uri = addEntry(id, s.entries.get(i))) != null) {
					nc++;
				}
			}
		}
		return nc;
	}

	/**
	 * �������ߺ���������
	 */
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
		if (cd.content != null && !cd.content.isEmpty()){
			cv.put(EntryTableMetaData.CONTENT,
					ProviderUtils.contentListToString(cd.content));
			//������Ŀ��Ӧ���Ѿ��Ķ�
			cv.put(EntryTableMetaData.IS_READ, 1);
		}
		cv.put(EntryTableMetaData.MODIFIED_DATE, System.currentTimeMillis());

		Uri updateUri = entryUri;

		// write content
		// ProviderUtils.writeContent(cr, updateUri, cd.content);
		// cv.put(EntryTableMetaData.CONTENT, cd.content);
		Log.d(tag, "inserted uri:" + updateUri);
		return cr.update(updateUri, cv, null, null);
	}

	public int getLogoId(int sourceId) {
		Cursor c = cr.query(LogoTableMetaData.CONTENT_URI,
				new String[] { LogoTableMetaData.SOURCE_ID },
				LogoTableMetaData.SOURCE_ID + " = " + sourceId, null, null);
		c.moveToFirst();
		int id = c.getInt(c.getColumnIndex(LogoTableMetaData.SOURCE_ID));
		c.close();
		return id;
	}

	public OutputStream getLogoOutputStream(Uri logoUri) {
		OutputStream os = null;
		try {
			os = cr.openOutputStream(logoUri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(tag, "file not found " + logoUri, e);
		}
		return os;
	}

	public boolean updateLogo(Uri logoUri, InputStream is) {

		boolean flag = true;
		OutputStream os = null;
		byte[] buf = new byte[1024];
		int n = -1;
		try {
			os = cr.openOutputStream(logoUri);
			while ((n = is.read(buf)) != -1) {
				os.write(buf);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(tag, "file not found " + logoUri, e);
			flag = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(tag, "IOException " + logoUri, e);
			flag = false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null)
					os.close();
			} catch (IOException e) {
				Log.e("tag", "could not close stream", e);
			}
		}
		// bm.compress(, 100, stream);
		// Uri insertedUri = null;
		// if (bm != null) {
		// ContentValues cv = new ContentValues();
		// cv.put(LogoTableMetaData.SIZE, bm.getByteCount());
		// cv.put(LogoTableMetaData.WIDTH, bm.getWidth());
		// cv.put(LogoTableMetaData.HEIGHT, bm.getHeight());
		// insertedUri = cr.insert(logoUri, cv);
		// }
		return flag;
	}

	public String getLogoAddress(Uri logoUri) {
		Cursor c = cr.query(logoUri, new String[] { LogoTableMetaData.DATA },
				null, null, null);
		c.moveToFirst();
		String address = c.getString(c.getColumnIndex(LogoTableMetaData.DATA));
		c.close();
		return address;
	}

	public Bitmap getLogo(Uri logoUri) {
		InputStream inStream = null;
		Bitmap bm = null;
		try {
			inStream = cr.openInputStream(logoUri);
			// what to do with the stream is up to you
			// I simply create a bitmap to display it
			bm = BitmapFactory.decodeStream(inStream);
		} catch (FileNotFoundException e) {
			Log.e(tag, "file not found " + logoUri, e);
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					Log.e("tag", "could not close stream", e);
				}
			}
		}
		return bm;
	}

	public ArrayList<DayTime> getAlarms(int news_id) {
		ArrayList<DayTime> dayTimes = new ArrayList<DayTime>();
		Cursor c = cr.query(AlarmTableMetaData.CONTENT_URI,
				new String[] { AlarmTableMetaData.ALARM_TIME },
				AlarmTableMetaData.NEWS_ID + " = " + news_id, null, null);
		int iat = c.getColumnIndex(AlarmTableMetaData.ALARM_TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			dayTimes.add(new DayTime(c.getString(iat)));
		}
		c.close();
		return dayTimes;
	}

	// public ArrayList<Newspaper> getNewsPapers() {
	//
	// Cursor c = cr.query(NewsTableMetaData.CONTENT_URI, null, null, null,
	// null);
	// int iname = c.getColumnIndex(NewsTableMetaData.NAME);
	// // int ifreq = c.getColumnIndex(NewsTableMetaData.FREQENCE);
	// // int ialarm = c.getColumnIndex(NewsTableMetaData.ALARM_TIME);
	// int icrea = c.getColumnIndex(NewsTableMetaData.CREATED_DATE);
	// ArrayList<Newspaper> newspapers = new ArrayList<Newspaper>();
	//
	// // int count = c.getCount();
	// // for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
	// // newspapers.add(new Newspaper(c.getString(iname), c.getInt(ifreq),
	// // ProviderUtils.stringToAlarmTime(c.getString(ialarm)), c
	// // .getLong(icrea)));
	// // }
	// c.close();
	// return newspapers;
	// }

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
			// Log.d(tag, "entry id " + ec.getLong(ie_id));
			e = new Entry(ec.getLong(ie_id), ec.getString(ie_name),
					ec.getString(ie_add), ec.getString(ie_des));
			// Log.d(tag, e.toString());
			es.add(e);
		}
		s.entries = es;

		// close cusor
		ec.close();

		return s;
	}

	public String getSourceName(int sourceId) {

		Cursor c = cr.query(
				Uri.withAppendedPath(SourceTableMetaData.CONTENT_URI,
						String.valueOf(sourceId)),
				new String[] { SourceTableMetaData.SOURCE_NAME }, null, null,
				null);
		c.moveToFirst();
		int isource_name = c.getColumnIndex(SourceTableMetaData.SOURCE_NAME);
		String name = c.getString(isource_name);
		c.close();

		return name;
	}

	public String getNewsName(int news_id) {
		Cursor c = cr.query(
				Uri.withAppendedPath(NewsTableMetaData.CONTENT_URI,
						String.valueOf(news_id)),
				new String[] { NewsTableMetaData.NAME }, null, null, null);
		c.moveToFirst();
		int inews_name = c.getColumnIndex(NewsTableMetaData.NAME);
		String name = c.getString(inews_name);
		c.close();

		return name;
	}

	public ContentDetail getEntry(Uri entryUri) {

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

			cd.content = ProviderUtils.stringToContentList(ec.getString(icont));
		}
		ec.close();

		return cd;
	}

	// //���ݿ�֧�ֵ��ļ���д
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
