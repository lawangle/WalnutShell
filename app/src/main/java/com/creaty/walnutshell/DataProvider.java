package com.creaty.walnutshell;

import java.io.FileNotFoundException;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;

import com.creaty.walnutshell.alarm.AlarmFactory;
import com.creaty.walnutshell.content_provider.DataProviderMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.AlarmTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.EntryTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.ImageTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.LogoTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;

public class DataProvider extends ContentProvider {
	// Logging helper tag. No significance to providers.
	private static final String TAG = "DataProvider";

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return this.openFileHelper(uri, mode);
	}

	// Projection maps are similar to "as" const
	// in an sql statement where by you can rename the
	// columns.
	private static HashMap<String, String> sLogosProjectionMap;
	static {
		sLogosProjectionMap = new HashMap<String, String>();
		sLogosProjectionMap.put(LogoTableMetaData._ID, LogoTableMetaData._ID);
		sLogosProjectionMap.put(LogoTableMetaData.MIME_TYPE,
				LogoTableMetaData.MIME_TYPE);
		sLogosProjectionMap.put(LogoTableMetaData.DISPLAY_NAME,
				LogoTableMetaData.DISPLAY_NAME); // ¥Ê¥¢Õº∆¨¡¥Ω”µÿ÷∑
		sLogosProjectionMap.put(LogoTableMetaData.DATA, LogoTableMetaData.DATA);
		sLogosProjectionMap.put(LogoTableMetaData.HEIGHT,
				LogoTableMetaData.HEIGHT);
		sLogosProjectionMap.put(LogoTableMetaData.WIDTH,
				LogoTableMetaData.WIDTH);
		sLogosProjectionMap.put(LogoTableMetaData.SIZE, LogoTableMetaData.SIZE);
		sLogosProjectionMap.put(LogoTableMetaData.SOURCE_ID,
				LogoTableMetaData.SOURCE_ID);
		sLogosProjectionMap.put(LogoTableMetaData.CREATED_DATE,
				LogoTableMetaData.CREATED_DATE);
	}
	private static HashMap<String, String> sImagesProjectionMap;
	static {
		sImagesProjectionMap = new HashMap<String, String>();
		sImagesProjectionMap
				.put(ImageTableMetaData._ID, ImageTableMetaData._ID);
		sImagesProjectionMap.put(ImageTableMetaData.MIME_TYPE,
				ImageTableMetaData.MIME_TYPE);
		sImagesProjectionMap.put(ImageTableMetaData.DISPLAY_NAME,
				ImageTableMetaData.DISPLAY_NAME); // ¥Ê¥¢Õº∆¨¡¥Ω”µÿ÷∑
		sImagesProjectionMap.put(ImageTableMetaData.DATA,
				ImageTableMetaData.DATA);
		sImagesProjectionMap.put(ImageTableMetaData.HEIGHT,
				ImageTableMetaData.HEIGHT);
		sImagesProjectionMap.put(ImageTableMetaData.WIDTH,
				ImageTableMetaData.WIDTH);
		sImagesProjectionMap.put(ImageTableMetaData.SIZE,
				ImageTableMetaData.SIZE);
		sImagesProjectionMap.put(ImageTableMetaData.ENTRY_ID,
				ImageTableMetaData.ENTRY_ID);
		sImagesProjectionMap.put(ImageTableMetaData.CREATED_DATE,
				ImageTableMetaData.CREATED_DATE);
	}
	private static HashMap<String, String> sAlarmsProjectionMap;
	static {
		sAlarmsProjectionMap = new HashMap<String, String>();
		sAlarmsProjectionMap
				.put(AlarmTableMetaData._ID, AlarmTableMetaData._ID);
		sAlarmsProjectionMap.put(AlarmTableMetaData.NEWS_ID,
				AlarmTableMetaData.NEWS_ID);
		sAlarmsProjectionMap.put(AlarmTableMetaData.ALARM_TIME,
				AlarmTableMetaData.ALARM_TIME);
		sAlarmsProjectionMap.put(AlarmTableMetaData.CREATED_DATE,
				AlarmTableMetaData.CREATED_DATE);
	}
	private static HashMap<String, String> sNewsProjectionMap;
	static {
		sNewsProjectionMap = new HashMap<String, String>();
		sNewsProjectionMap.put(NewsTableMetaData._ID, NewsTableMetaData._ID);

		sNewsProjectionMap.put(NewsTableMetaData.NAME, NewsTableMetaData.NAME);
		sNewsProjectionMap.put(NewsTableMetaData.IS_READ, NewsTableMetaData.IS_READ);
		sNewsProjectionMap.put(NewsTableMetaData.FREQENCE,
				NewsTableMetaData.FREQENCE);
		sNewsProjectionMap.put(NewsTableMetaData.ALARM_TIME,
				NewsTableMetaData.ALARM_TIME);
		sNewsProjectionMap.put(NewsTableMetaData.CREATED_DATE,
				NewsTableMetaData.CREATED_DATE);
	}
	private static HashMap<String, String> sSourcesProjectionMap;
	static {
		sSourcesProjectionMap = new HashMap<String, String>();
		sSourcesProjectionMap.put(SourceTableMetaData._ID,
				SourceTableMetaData._ID);

		// page_name, page_description, page_logo_fref, page_address,
		// source_name, source_description, type
		sSourcesProjectionMap.put(SourceTableMetaData.NEWS_ID,
				SourceTableMetaData.NEWS_ID);
		sSourcesProjectionMap.put(SourceTableMetaData.PAGE_NAME,
				SourceTableMetaData.PAGE_NAME);
		sSourcesProjectionMap.put(SourceTableMetaData.IS_READ,
				SourceTableMetaData.IS_READ);
		sSourcesProjectionMap.put(SourceTableMetaData.PAGE_DESCRIPTION,
				SourceTableMetaData.PAGE_DESCRIPTION);
		sSourcesProjectionMap.put(SourceTableMetaData.PAGE_LOGO,
				SourceTableMetaData.PAGE_LOGO);
		sSourcesProjectionMap.put(SourceTableMetaData.PAGE_ADDRESS,
				SourceTableMetaData.PAGE_ADDRESS);
		sSourcesProjectionMap.put(SourceTableMetaData.SOURCE_NAME,
				SourceTableMetaData.SOURCE_NAME);
		sSourcesProjectionMap.put(SourceTableMetaData.SOURCE_SEQ,
				SourceTableMetaData.SOURCE_SEQ);
		sSourcesProjectionMap.put(SourceTableMetaData.SOURCE_STRATEGY,
				SourceTableMetaData.SOURCE_STRATEGY);
		sSourcesProjectionMap.put(SourceTableMetaData.SOURCE_DESCRIPTION,
				SourceTableMetaData.SOURCE_DESCRIPTION);
		sSourcesProjectionMap.put(SourceTableMetaData.TYPE,
				SourceTableMetaData.TYPE);

		// created date, modified date
		sSourcesProjectionMap.put(SourceTableMetaData.CREATED_DATE,
				SourceTableMetaData.CREATED_DATE);
		sSourcesProjectionMap.put(SourceTableMetaData.MODIFIED_DATE,
				SourceTableMetaData.MODIFIED_DATE);
	}
	private static HashMap<String, String> sEntriesProjectionMap;
	static {
		sEntriesProjectionMap = new HashMap<String, String>();
		sEntriesProjectionMap.put(EntryTableMetaData._ID,
				EntryTableMetaData._ID);

		// page_name, page_description, page_logo_fref, page_address
		sEntriesProjectionMap.put(EntryTableMetaData.SOURCE_ID,
				EntryTableMetaData.SOURCE_ID);
		sEntriesProjectionMap.put(EntryTableMetaData.NAME,
				EntryTableMetaData.NAME);
		sEntriesProjectionMap.put(EntryTableMetaData.IS_READ,
				EntryTableMetaData.IS_READ);
		sEntriesProjectionMap.put(EntryTableMetaData.CONTENT_ADDRESS,
				EntryTableMetaData.CONTENT_ADDRESS);
		sEntriesProjectionMap.put(EntryTableMetaData.DESCRIPTION,
				EntryTableMetaData.DESCRIPTION);
		sEntriesProjectionMap.put(EntryTableMetaData.AUTHOR,
				EntryTableMetaData.AUTHOR);
		sEntriesProjectionMap.put(EntryTableMetaData.CONTENT,
				EntryTableMetaData.CONTENT);

		// created date, modified date
		sEntriesProjectionMap.put(EntryTableMetaData.CREATED_DATE,
				EntryTableMetaData.CREATED_DATE);
		sEntriesProjectionMap.put(EntryTableMetaData.MODIFIED_DATE,
				EntryTableMetaData.MODIFIED_DATE);
	}

	// Provide a mechanism to identify
	// all the incoming uri patterns.
	private static final UriMatcher sUriMatcher;
	private static final int INCOMING_SOURCE_COLLECTION_URI_INDICATOR = 1;
	private static final int INCOMING_SINGLE_SOURCE_URI_INDICATOR = 2;
	private static final int INCOMING_ENTRY_COLLECTION_URI_INDICATOR = 3;
	private static final int INCOMING_SINGLE_ENTRY_URI_INDICATOR = 4;
	private static final int INCOMING_NEWS_COLLECTION_URI_INDICATOR = 5;
	private static final int INCOMING_SINGLE_NEWS_URI_INDICATOR = 6;
	private static final int INCOMING_ALARMS_COLLECTION_URI_INDICATOR = 7;
	private static final int INCOMING_SINGLE_ALARM_URI_INDICATOR = 8;
	private static final int INCOMING_IMAGES_COLLECTION_URI_INDICATOR = 9;
	private static final int INCOMING_SINGLE_IMAGE_URI_INDICATOR = 10;
	private static final int INCOMING_LOGOS_COLLECTION_URI_INDICATOR = 11;
	private static final int INCOMING_SINGLE_LOGO_URI_INDICATOR = 12;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "sources",
				INCOMING_SOURCE_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "sources/#",
				INCOMING_SINGLE_SOURCE_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "entries",
				INCOMING_ENTRY_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "entries/#",
				INCOMING_SINGLE_ENTRY_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "newspapers",
				INCOMING_NEWS_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "newspapers/#",
				INCOMING_SINGLE_NEWS_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "alarms",
				INCOMING_ALARMS_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "alarms/#",
				INCOMING_SINGLE_ALARM_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "images",
				INCOMING_IMAGES_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "images/#",
				INCOMING_SINGLE_IMAGE_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "logos",
				INCOMING_LOGOS_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DataProviderMetaData.AUTHORITY, "logos/#",
				INCOMING_SINGLE_LOGO_URI_INDICATOR);
	}

	/**
	 * This class helps open, create, and upgrade the database file.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DataProviderMetaData.DATABASE_NAME, null,
					DataProviderMetaData.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "inner oncreate called");
			db.execSQL("CREATE TABLE " + NewsTableMetaData.TABLE_NAME + " ("
					+ NewsTableMetaData._ID + " INTEGER PRIMARY KEY,"
					+ NewsTableMetaData.NAME + " TEXT NOT NULL,"
					+ NewsTableMetaData.IS_READ + " INTEGER,"
					+ NewsTableMetaData.FREQENCE + " INTEGER,"
					+ NewsTableMetaData.ALARM_TIME + " TEXT,"
					+ NewsTableMetaData.CREATED_DATE + " INTEGER NOT NULL"
					+ ");");

			db.execSQL("CREATE TABLE " + AlarmTableMetaData.TABLE_NAME + " ("
					+ AlarmTableMetaData._ID + " INTEGER PRIMARY KEY,"
					+ AlarmTableMetaData.NEWS_ID + " INTERGER NOT NULL "
					+ "REFERENCES " + NewsTableMetaData.TABLE_NAME + "("
					+ NewsTableMetaData._ID + ") ON DELETE CASCADE,"
					+ AlarmTableMetaData.ALARM_TIME + " TEXT NOT NULL,"
					+ AlarmTableMetaData.CREATED_DATE + " INTEGER NOT NULL"
					+ ");");

			db.execSQL("CREATE TABLE " + SourceTableMetaData.TABLE_NAME + " ("
					+ SourceTableMetaData._ID + " INTEGER PRIMARY KEY,"
					+ SourceTableMetaData.NEWS_ID + " INTERGER NOT NULL "
					+ "REFERENCES " + NewsTableMetaData.TABLE_NAME + "("
					+ NewsTableMetaData._ID + ") ON DELETE CASCADE,"
					+ SourceTableMetaData.PAGE_NAME + " TEXT,"
					+ SourceTableMetaData.IS_READ + " INTEGER,"
					+ SourceTableMetaData.PAGE_DESCRIPTION + " TEXT,"
					+ SourceTableMetaData.PAGE_LOGO + " TEXT,"
					+ SourceTableMetaData.PAGE_ADDRESS + " TEXT NOT NULL,"
					+ SourceTableMetaData.SOURCE_NAME + " TEXT,"
					+ SourceTableMetaData.SOURCE_SEQ + " INTEGER NOT NULL,"
					+ SourceTableMetaData.SOURCE_STRATEGY
					+ " INTEGER NOT NULL,"
					+ SourceTableMetaData.SOURCE_DESCRIPTION + " TEXT,"
					+ SourceTableMetaData.TYPE + " INTEGER NOT NULL,"
					+ SourceTableMetaData.CREATED_DATE + " INTEGER NOT NULL,"
					+ SourceTableMetaData.MODIFIED_DATE + " INTEGER );");

			db.execSQL("CREATE TABLE " + EntryTableMetaData.TABLE_NAME + " ("
					+ EntryTableMetaData._ID + " INTEGER PRIMARY KEY,"
					+ EntryTableMetaData.SOURCE_ID + " INTEGER NOT NULL "
					+ "REFERENCES " + SourceTableMetaData.TABLE_NAME + "("
					+ SourceTableMetaData._ID + ") ON DELETE CASCADE,"
					+ EntryTableMetaData.NAME + " TEXT NOT NULL,"
					+ EntryTableMetaData.IS_READ + " INTEGER,"
					+ EntryTableMetaData.CONTENT_ADDRESS + " TEXT NOT NULL,"
					+ EntryTableMetaData.DESCRIPTION + " TEXT,"
					+ EntryTableMetaData.AUTHOR + " TEXT,"
					+ EntryTableMetaData.CONTENT + " TEXT,"
					+ EntryTableMetaData.CREATED_DATE + " INTEGER NOT NULL,"
					+ EntryTableMetaData.MODIFIED_DATE + " INTEGER );");
			db.execSQL("CREATE TABLE " + LogoTableMetaData.TABLE_NAME + " ("
					+ LogoTableMetaData._ID + " INTEGER PRIMARY KEY,"
					+ LogoTableMetaData.SOURCE_ID + " INTERGER "
					+ "REFERENCES " + SourceTableMetaData.TABLE_NAME + "("
					+ SourceTableMetaData._ID + ") ON DELETE CASCADE,"
					+ LogoTableMetaData.MIME_TYPE + " TEXT,"
					+ LogoTableMetaData.DISPLAY_NAME + " TEXT NOT NULL,"
					+ LogoTableMetaData.DATA + " TEXT NOT NULL,"
					+ LogoTableMetaData.HEIGHT + " INTEGER,"
					+ LogoTableMetaData.WIDTH + " INTEGER,"
					+ LogoTableMetaData.SIZE + " INTEGER,"
					+ LogoTableMetaData.CREATED_DATE + " INTEGER NOT NULL"
					+ ");");
			db.execSQL("CREATE TABLE " + ImageTableMetaData.TABLE_NAME + " ("
					+ ImageTableMetaData._ID + " INTEGER PRIMARY KEY,"
					+ ImageTableMetaData.ENTRY_ID + " INTERGER "
					+ "REFERENCES " + EntryTableMetaData.TABLE_NAME + "("
					+ EntryTableMetaData._ID + ") ON DELETE CASCADE,"
					+ ImageTableMetaData.MIME_TYPE + " TEXT,"
					+ ImageTableMetaData.DISPLAY_NAME + " TEXT NOT NULL,"
					+ ImageTableMetaData.DATA + " TEXT NOT NULL,"
					+ ImageTableMetaData.HEIGHT + " INTEGER,"
					+ ImageTableMetaData.WIDTH + " INTEGER,"
					+ ImageTableMetaData.SIZE + " INTEGER,"
					+ ImageTableMetaData.CREATED_DATE + " INTEGER NOT NULL"
					+ ");");
		}

		@Override
		public void onOpen(SQLiteDatabase db) {

			super.onOpen(db);

			if (!db.isReadOnly()) {

				// Enable foreign key constraints
				db.execSQL("PRAGMA foreign_keys=ON;");

			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "inner onupgrade called");
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			// db.execSQL("DROP VIEW IF EXISTS " +
			// NewsAlarmViewMetaData.VIEW_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + AlarmTableMetaData.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + SourceTableMetaData.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + LogoTableMetaData.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + EntryTableMetaData.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + SourceTableMetaData.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + NewsTableMetaData.TABLE_NAME);

			onCreate(db);
		}
	}

	private DatabaseHelper mOpenHelper;

	@Override
	public boolean onCreate() {
		Log.d(TAG, "main onCreate called");
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	public void setTableAndProMap(String tableName,
			HashMap<String, String> projectionMap) {

	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		int match = sUriMatcher.match(uri);
		switch (match) {
		case INCOMING_SOURCE_COLLECTION_URI_INDICATOR:
			qb.setTables(SourceTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sSourcesProjectionMap);
			break;

		case INCOMING_SINGLE_SOURCE_URI_INDICATOR:
			qb.setTables(SourceTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sSourcesProjectionMap);
			qb.appendWhere(SourceTableMetaData._ID + "="
					+ uri.getPathSegments().get(1));
			break;

		case INCOMING_ENTRY_COLLECTION_URI_INDICATOR:
			qb.setTables(EntryTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sEntriesProjectionMap);
			break;

		case INCOMING_SINGLE_ENTRY_URI_INDICATOR:
			qb.setTables(EntryTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sEntriesProjectionMap);
			qb.appendWhere(SourceTableMetaData._ID + "="
					+ uri.getPathSegments().get(1));
			break;

		case INCOMING_NEWS_COLLECTION_URI_INDICATOR:
			qb.setTables(NewsTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sNewsProjectionMap);
			break;

		case INCOMING_SINGLE_NEWS_URI_INDICATOR:
			qb.setTables(NewsTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sNewsProjectionMap);
			qb.appendWhere(NewsTableMetaData._ID + "="
					+ uri.getPathSegments().get(1));
			break;

		case INCOMING_ALARMS_COLLECTION_URI_INDICATOR:
			qb.setTables(AlarmTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sAlarmsProjectionMap);
			break;

		case INCOMING_SINGLE_ALARM_URI_INDICATOR:
			qb.setTables(AlarmTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sAlarmsProjectionMap);
			qb.appendWhere(AlarmTableMetaData._ID + "="
					+ uri.getPathSegments().get(1));
			break;

		case INCOMING_LOGOS_COLLECTION_URI_INDICATOR:
			qb.setTables(LogoTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sLogosProjectionMap);
			break;

		case INCOMING_SINGLE_LOGO_URI_INDICATOR:
			qb.setTables(LogoTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sLogosProjectionMap);
			qb.appendWhere(LogoTableMetaData._ID + "="
					+ uri.getPathSegments().get(1));
			break;

		case INCOMING_IMAGES_COLLECTION_URI_INDICATOR:
			qb.setTables(ImageTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sImagesProjectionMap);
			break;

		case INCOMING_SINGLE_IMAGE_URI_INDICATOR:
			qb.setTables(ImageTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sImagesProjectionMap);
			qb.appendWhere(ImageTableMetaData._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// If no sort order is specified use the default
		String orderBy = null;
		if (TextUtils.isEmpty(sortOrder)) {
			if (match == INCOMING_SOURCE_COLLECTION_URI_INDICATOR
					|| match == INCOMING_SINGLE_SOURCE_URI_INDICATOR)
				orderBy = SourceTableMetaData.DEFAULT_SORT_ORDER;
			else if (match == INCOMING_NEWS_COLLECTION_URI_INDICATOR
					|| match == INCOMING_SINGLE_NEWS_URI_INDICATOR)
				orderBy = NewsTableMetaData.DEFAULT_SORT_ORDER;
			else if (match == INCOMING_ENTRY_COLLECTION_URI_INDICATOR
					|| match == INCOMING_SINGLE_ENTRY_URI_INDICATOR)
				orderBy = EntryTableMetaData.DEFAULT_SORT_ORDER;
			else {
			}
		} else {
			orderBy = sortOrder;
		}

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);

		// example of getting a count
		// int i = c.getCount();

		// Tell the cursor what uri to watch,
		// so it knows when its source data changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case INCOMING_SOURCE_COLLECTION_URI_INDICATOR:
			return SourceTableMetaData.SOURCE_TYPE;

		case INCOMING_SINGLE_SOURCE_URI_INDICATOR:
			return SourceTableMetaData.SOURCE_ITEM_TYPE;

		case INCOMING_ENTRY_COLLECTION_URI_INDICATOR:
			return EntryTableMetaData.ENTRY_TYPE;

		case INCOMING_SINGLE_ENTRY_URI_INDICATOR:
			return EntryTableMetaData.ENTRY_ITEM_TYPE;

		case INCOMING_NEWS_COLLECTION_URI_INDICATOR:
			return NewsTableMetaData.NEWS_TYPE;

		case INCOMING_SINGLE_NEWS_URI_INDICATOR:
			return NewsTableMetaData.NEWS_ITEM_TYPE;

		case INCOMING_ALARMS_COLLECTION_URI_INDICATOR:
			return AlarmTableMetaData.ALARMS_TYPE;

		case INCOMING_SINGLE_ALARM_URI_INDICATOR:
			return AlarmTableMetaData.ALARMS_ITEM_TYPE;

		case INCOMING_LOGOS_COLLECTION_URI_INDICATOR:
			return LogoTableMetaData.LOGOS_TYPE;

		case INCOMING_SINGLE_LOGO_URI_INDICATOR:
			return LogoTableMetaData.LOGOS_ITEM_TYPE;

		case INCOMING_IMAGES_COLLECTION_URI_INDICATOR:
			return ImageTableMetaData.IMAGES_TYPE;

		case INCOMING_SINGLE_IMAGE_URI_INDICATOR:
			return ImageTableMetaData.IMAGES_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// Validate the requested uri
		int flag = sUriMatcher.match(uri);
		if (flag != INCOMING_SOURCE_COLLECTION_URI_INDICATOR
				&& flag != INCOMING_ENTRY_COLLECTION_URI_INDICATOR
				&& flag != INCOMING_NEWS_COLLECTION_URI_INDICATOR
				&& flag != INCOMING_ALARMS_COLLECTION_URI_INDICATOR
				&& flag != INCOMING_LOGOS_COLLECTION_URI_INDICATOR
				&& flag != INCOMING_IMAGES_COLLECTION_URI_INDICATOR) {
			throw new IllegalArgumentException("Illegal URI to insert " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			throw new IllegalArgumentException(
					" initialValues cannnot be null ");
		}
		//
		// Long now = Long.valueOf(System.currentTimeMillis());
		//
		// // Make sure that the fields are all set
		// if (values.containsKey(SourceTableMetaData.CREATED_DATE) == false) {
		// values.put(SourceTableMetaData.CREATED_DATE, now);
		// }
		//
		// if (values.containsKey(SourceTableMetaData.MODIFIED_DATE) == false) {
		// values.put(SourceTableMetaData.MODIFIED_DATE, now);
		// }

		// if (flag == INCOMING_SOURCE_COLLECTION_URI_INDICATOR) {
		// if (values.containsKey(SourceTableMetaData.PAGE_NAME ) == false) {
		// throw new SQLException(
		// "Failed to insert row because Page Name is needed "
		// + uri);
		// }

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Uri insertedDataUri = null;
		if (flag == INCOMING_SOURCE_COLLECTION_URI_INDICATOR) {
			insertedDataUri = insertData(uri, values,
					SourceTableMetaData.TABLE_NAME,
					SourceTableMetaData.CONTENT_URI);
		} else if (flag == INCOMING_NEWS_COLLECTION_URI_INDICATOR) {

			insertedDataUri = insertData(uri, values,
					NewsTableMetaData.TABLE_NAME, NewsTableMetaData.CONTENT_URI);
		} else if (flag == INCOMING_ENTRY_COLLECTION_URI_INDICATOR) {
			insertedDataUri = insertData(uri, values,
					EntryTableMetaData.TABLE_NAME,
					EntryTableMetaData.CONTENT_URI);
		} else if (flag == INCOMING_ALARMS_COLLECTION_URI_INDICATOR) {
			insertedDataUri = insertData(uri, values,
					AlarmTableMetaData.TABLE_NAME,
					AlarmTableMetaData.CONTENT_URI);
		} else if (flag == INCOMING_LOGOS_COLLECTION_URI_INDICATOR) {
			insertedDataUri = insertData(uri, values,
					LogoTableMetaData.TABLE_NAME, LogoTableMetaData.CONTENT_URI);
		} else {
			insertedDataUri = insertData(uri, values,
					ImageTableMetaData.TABLE_NAME,
					ImageTableMetaData.CONTENT_URI);
		}
		return insertedDataUri;
	}

	private Uri insertData(Uri insertUri, ContentValues values,
			String tableName, Uri contentUri) throws SQLException {

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(tableName, null, values);
		if (rowId < 0) {
			throw new SQLException("Failed to insert row into " + insertUri);
		}
		Uri insertedDataUri = ContentUris.withAppendedId(contentUri, rowId);
		getContext().getContentResolver().notifyChange(insertedDataUri, null);
		return insertedDataUri;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {

		int count;
		switch (sUriMatcher.match(uri)) {
		case INCOMING_SOURCE_COLLECTION_URI_INDICATOR:
			count = delete(false, uri, SourceTableMetaData.TABLE_NAME,
					SourceTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_SOURCE_URI_INDICATOR:
			count = delete(true, uri, SourceTableMetaData.TABLE_NAME,
					SourceTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_ENTRY_COLLECTION_URI_INDICATOR:
			count = delete(false, uri, EntryTableMetaData.TABLE_NAME,
					EntryTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_ENTRY_URI_INDICATOR:
			count = delete(true, uri, EntryTableMetaData.TABLE_NAME,
					EntryTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_NEWS_COLLECTION_URI_INDICATOR:
			count = delete(false, uri, NewsTableMetaData.TABLE_NAME,
					NewsTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_NEWS_URI_INDICATOR:
			count = delete(true, uri, NewsTableMetaData.TABLE_NAME,
					NewsTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_ALARMS_COLLECTION_URI_INDICATOR:
			count = delete(false, uri, AlarmTableMetaData.TABLE_NAME,
					AlarmTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_ALARM_URI_INDICATOR:
			count = delete(true, uri, AlarmTableMetaData.TABLE_NAME,
					AlarmTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_LOGOS_COLLECTION_URI_INDICATOR:
			count = delete(false, uri, LogoTableMetaData.TABLE_NAME,
					LogoTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_LOGO_URI_INDICATOR:
			count = delete(true, uri, LogoTableMetaData.TABLE_NAME,
					LogoTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_IMAGES_COLLECTION_URI_INDICATOR:
			count = delete(false, uri, ImageTableMetaData.TABLE_NAME,
					ImageTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_IMAGE_URI_INDICATOR:
			count = delete(true, uri, ImageTableMetaData.TABLE_NAME,
					ImageTableMetaData._ID, where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	public int delete(boolean isSingle, Uri uri, String tableName,
			String primaryKey, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		if (isSingle) {
			String rowId = uri.getPathSegments().get(1);
			count = db.delete(tableName,
					primaryKey
							+ "="
							+ rowId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
		} else {
			count = db.delete(tableName, where, whereArgs);
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		int count;
		switch (sUriMatcher.match(uri)) {
		case INCOMING_SOURCE_COLLECTION_URI_INDICATOR:
			count = update(false, uri, values, SourceTableMetaData.TABLE_NAME,
					SourceTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_SOURCE_URI_INDICATOR:
			count = update(true, uri, values, SourceTableMetaData.TABLE_NAME,
					SourceTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_ENTRY_COLLECTION_URI_INDICATOR:
			count = update(false, uri, values, EntryTableMetaData.TABLE_NAME,
					EntryTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_ENTRY_URI_INDICATOR:
			count = update(true, uri, values, EntryTableMetaData.TABLE_NAME,
					EntryTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_NEWS_COLLECTION_URI_INDICATOR:
			count = update(false, uri, values, NewsTableMetaData.TABLE_NAME,
					NewsTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_NEWS_URI_INDICATOR:
			count = update(true, uri, values, NewsTableMetaData.TABLE_NAME,
					NewsTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_ALARMS_COLLECTION_URI_INDICATOR:
			count = update(false, uri, values, AlarmTableMetaData.TABLE_NAME,
					AlarmTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_ALARM_URI_INDICATOR:
			count = update(true, uri, values, AlarmTableMetaData.TABLE_NAME,
					AlarmTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_LOGOS_COLLECTION_URI_INDICATOR:
			count = update(false, uri, values, LogoTableMetaData.TABLE_NAME,
					LogoTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_LOGO_URI_INDICATOR:
			count = update(true, uri, values, LogoTableMetaData.TABLE_NAME,
					LogoTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_IMAGES_COLLECTION_URI_INDICATOR:
			count = update(false, uri, values, ImageTableMetaData.TABLE_NAME,
					ImageTableMetaData._ID, where, whereArgs);
			break;

		case INCOMING_SINGLE_IMAGE_URI_INDICATOR:
			count = update(true, uri, values, ImageTableMetaData.TABLE_NAME,
					ImageTableMetaData._ID, where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	public int update(boolean isSingle, Uri uri, ContentValues values,
			String tableName, String primaryKey, String where,
			String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		if (isSingle) {
			String rowId = uri.getPathSegments().get(1);
			count = db.update(tableName, values,
					primaryKey
							+ "="
							+ rowId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
		} else {
			count = db.update(tableName, values, where, whereArgs);
		}
		return count;
	}
}
