package com.creaty.walnutshell.content_provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataProviderMetaData {
	public static final String AUTHORITY = "com.creaty.walnutshell.DataProvider";

	public static final String DATABASE_NAME = "walnutshell.db";
	public static final int DATABASE_VERSION = 3;
	public static final String NEWS_TABLE_NAME= "newspapers";
	public static final String SOURCES_TABLE_NAME = "sources";
	public static final String ENTRIES_TABLE_NAME = "entries";
	
	private DataProviderMetaData() {
	}

	// inner class describing columns and their types
	public static final class NewsTableMetaData implements BaseColumns {
		private NewsTableMetaData() {
		}

		public static final String TABLE_NAME = "newspapers";

		// uri and mine type definitions
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/newspapers");
		public static final String NEWS_TYPE = "vnd.android.cursor.dir/vnd.walnutshell.newspapers";
		public static final String NEWS_ITEM_TYPE = "vnd.android.cursor.item/vnd.walnutshell.newspaper";

		public static final String DEFAULT_SORT_ORDER = "created ASC";

		// Additional Columns start here.
		// string type
		public static final String NAME = "name";
		// Integer 
		public static final String FREQENCE = "frequence";
		// string type
		public static final String ALARM_TIME = "alarm_time";
		// Integer from System.currentTimeMillis()
		public static final String CREATED_DATE = "created";

	}
	public static final class SourceTableMetaData implements BaseColumns {
		private SourceTableMetaData() {
		}

		public static final String TABLE_NAME = "sources";

		// uri and mine type definitions
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/sources");
		public static final String SOURCE_TYPE = "vnd.android.cursor.dir/vnd.walnutshell.sources";
		public static final String SOURCE_ITEM_TYPE = "vnd.android.cursor.item/vnd.walnutshell.source";

		public static final String DEFAULT_SORT_ORDER = "modified DESC";

		// Additional Columns start here.
		public static final String NEWS_ID = "newspaper_id";
		// string type
		public static final String PAGE_NAME = "page_name";
		// string type
		public static final String PAGE_DESCRIPTION = "page_description";
		// string type
		public static final String PAGE_LOGO = "page_logo";
		// string type
		public static final String PAGE_ADDRESS = "page_address";
		//string type
		public static final String SOURCE_NAME = "source_name";
		//integer type
		public static final String SOURCE_SEQ = "source_sequence";
		// integer type
		public static final String SOURCE_STRATEGY = "source_strategy";
		// string type
		public static final String SOURCE_DESCRIPTION = "source_description";
		//Integer type
		public static final String TYPE = "source_type";
		//public static final String CONTENT_ADDRESS = "content_address";
		// Integer from System.currentTimeMillis()
		public static final String CREATED_DATE = "created";
		// Integer from System.currentTimeMillis()
		public static final String MODIFIED_DATE = "modified";
	}
	public static final class EntryTableMetaData implements BaseColumns{
		public static final String TABLE_NAME = "entries";

		// uri and mine type definitions
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/entries");
		public static final String ENTRY_TYPE = "vnd.android.cursor.dir/vnd.walnutshell.entries";
		public static final String ENTRY_ITEM_TYPE = "vnd.android.cursor.item/vnd.walnutshell.entry";

		public static final String DEFAULT_SORT_ORDER = "modified ASC";

		// Additional Columns start here.
		public static final String SOURCE_ID = "source_id";
		// string type
		public static final String NAME = "name";
		// string type
		public static final String CONTENT_ADDRESS = "content_address";
		// string type
		public static final String DESCRIPTION = "description";
		// string type
		public static final String AUTHOR = "author";
		// string type
		//public static final String IMAGE_HREF = "image_href";
		// string type
		public static final String CONTENT = "content";
		// Integer from System.currentTimeMillis()
		public static final String CREATED_DATE = "created";
		// Integer from System.currentTimeMillis()
		public static final String MODIFIED_DATE = "modified";
	} 
}
