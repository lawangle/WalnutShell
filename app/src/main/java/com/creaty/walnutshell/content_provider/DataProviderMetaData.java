package com.creaty.walnutshell.content_provider;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore.MediaColumns;

public class DataProviderMetaData {
	public static final String AUTHORITY = "com.creaty.walnutshell.DataProvider";

	public static final String DATABASE_NAME = "walnutshell.db";
	public static final int DATABASE_VERSION = 6;
	public static final String ALARM_TABLE_NAME = "alarms";
	public static final String NEWS_TABLE_NAME = "newspapers";
	public static final String SOURCES_TABLE_NAME = "sources";
	public static final String ENTRIES_TABLE_NAME = "entries";

	private DataProviderMetaData() {
	}

	// inner class describing columns and their types
	// public static final class NewsAlarmViewMetaData implements BaseColumns{
	// private NewsAlarmViewMetaData(){
	// }
	// public static final String VIEW_NAME = "news_alarms";
	// public static final Uri CONTENT_URI = Uri.parse("content://"
	// + AUTHORITY + "/news_alarms");
	// public static final String ALARMS_TYPE =
	// "vnd.android.cursor.dir/vnd.walnutshell.news_alarms";
	// public static final String ALARMS_ITEM_TYPE =
	// "vnd.android.cursor.item/vnd.walnutshell.news_alarm";
	// public static final String DEFAULT_SORT_ORDER = "created ASC";
	// }
	public static final class LogoTableMetaData implements MediaColumns {
		private LogoTableMetaData() {
		}

		public static final String TABLE_NAME = "logos";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/logos");
		public static final String LOGOS_TYPE = "vnd.android.cursor.dir/vnd.walnutshell.logos";
		public static final String LOGOS_ITEM_TYPE = "vnd.android.cursor.item/vnd.walnutshell.logo";
		public static final String DEFAULT_SORT_ORDER = "created DESC";
		
		public static final String SOURCE_ID = "source_id";
		public static final String CREATED_DATE = "created";
	}
	public static final class ImageTableMetaData implements MediaColumns {
		private ImageTableMetaData() {
		}

		public static final String TABLE_NAME = "images";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/images");
		public static final String IMAGES_TYPE = "vnd.android.cursor.dir/vnd.walnutshell.images";
		public static final String IMAGES_ITEM_TYPE = "vnd.android.cursor.item/vnd.walnutshell.image";
		public static final String DEFAULT_SORT_ORDER = "created DESC";
		
		public static final String ENTRY_ID = "entry_id";

		public static final String CREATED_DATE = "created";
	}

	public static final class AlarmTableMetaData implements BaseColumns {
		private AlarmTableMetaData() {
		}

		public static final String TABLE_NAME = "alarms";

		// uri and mine type definitions
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/alarms");
		public static final String ALARMS_TYPE = "vnd.android.cursor.dir/vnd.walnutshell.alarms";
		public static final String ALARMS_ITEM_TYPE = "vnd.android.cursor.item/vnd.walnutshell.alarm";
		public static final String DEFAULT_SORT_ORDER = "created DESC";

		// Additional Columns start here.
		public static final String NEWS_ID = "newspaper_id";
		// time type
		public static final String ALARM_TIME = "time";
		// Integer from System.currentTimeMillis()
		public static final String CREATED_DATE = "created";
	}

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
		public static final String IS_READ = "is_read";
		// Integer
		/** @deprecated */
		public static final String FREQENCE = "frequence";
		// string type
		/** @deprecated */
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
		// Integer
		public static final String IS_READ = "is_read";
		// string type
		public static final String PAGE_DESCRIPTION = "page_description";
		// string type
		@Deprecated
		public static final String PAGE_LOGO = "page_logo";
		// string type
		public static final String PAGE_ADDRESS = "page_address";
		// string type
		public static final String SOURCE_NAME = "source_name";
		// integer type
		public static final String SOURCE_SEQ = "source_sequence";
		// integer type
		public static final String SOURCE_STRATEGY = "source_strategy";
		// string type
		public static final String SOURCE_DESCRIPTION = "source_description";
		// Integer type
		public static final String TYPE = "source_type";
		// public static final String CONTENT_ADDRESS = "content_address";
		// Integer from System.currentTimeMillis()
		public static final String CREATED_DATE = "created";
		// Integer from System.currentTimeMillis()
		public static final String MODIFIED_DATE = "modified";
	}

	public static final class EntryTableMetaData implements BaseColumns {
		public static final String TABLE_NAME = "entries";

		// uri and mine type definitions
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/entries");
		public static final String ENTRY_TYPE = "vnd.android.cursor.dir/vnd.walnutshell.entries";
		public static final String ENTRY_ITEM_TYPE = "vnd.android.cursor.item/vnd.walnutshell.entry";

		public static final String DEFAULT_SORT_ORDER = "modified DESC";

		// Additional Columns start here.
		public static final String SOURCE_ID = "source_id";
		// string type
		public static final String NAME = "name";
		//Integer
		public static final String IS_READ = "is_read";
		// string type
		public static final String CONTENT_ADDRESS = "content_address";
		// string type
		public static final String DESCRIPTION = "description";
		// string type
		public static final String AUTHOR = "author";
		// string type
		// public static final String IMAGE_HREF = "image_href";
		// string type
		public static final String CONTENT = "content";
		// Integer from System.currentTimeMillis()
		public static final String CREATED_DATE = "created";
		// Integer from System.currentTimeMillis()
		public static final String MODIFIED_DATE = "modified";
	}
}
