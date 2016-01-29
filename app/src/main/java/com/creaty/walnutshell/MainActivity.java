package com.creaty.walnutshell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.creaty.walnutshell.UI.AboutFragment;
import com.creaty.walnutshell.UI.ChooseSourceFragment;
import com.creaty.walnutshell.UI.NewsPaperFragment;
import com.creaty.walnutshell.UI.SeparatedListAdapter;
import com.creaty.walnutshell.UI.SystemAdapter;
import com.creaty.walnutshell.UI.Utils;
import com.creaty.walnutshell.UI.WaitingFragment;
import com.creaty.walnutshell.basic.*;
import com.creaty.walnutshell.content_provider.DataBridge;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.EntryTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	final String tag = "MainActivity";

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private SeparatedListAdapter mDrawerAdapter;
	private DrawerNewsAdapter newsAdapter;
	private ActionBarDrawerToggle mDrawerToggle;

	private Fragment currFragment = null;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private int drawerSelectedPosition;
	//private String[] mPlanetTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = getTitle();
		//mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerAdapter = prepareDrawerAdapter();
		mDrawerList.setAdapter(mDrawerAdapter);
		//prepareNewsLoaderCallBack();
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			Log.d(tag, "onCreate()");
			drawerSelectedPosition = 1;
			//selectItem(drawerSelectedPosition);
		}

	}
	//本次使用首次进入Activity
	boolean firstCome = true;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Log.d(tag, "onStart()" + drawerSelectedPosition );
		selectItem( drawerSelectedPosition );
		if( firstCome ){
			mDrawerLayout.openDrawer(mDrawerList);
			firstCome = false;
		}
	}

	public int getDrawerSelectedPosition(){
		return drawerSelectedPosition;
	}
	private SeparatedListAdapter prepareDrawerAdapter() {
		SeparatedListAdapter sla = new SeparatedListAdapter(this,
				R.layout.drawer_list_item, R.id.drawer_item_title_view);
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(NewsTableMetaData.CONTENT_URI, null, null, null, null);
		newsAdapter = new DrawerNewsAdapter(this, c,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		sla.addSection(getResources().getString(R.string.my_newspaper),
				newsAdapter);
//		sla.addSection(getResources().getString(R.string.system),
//				new SystemAdapter(this));
		return sla;
	}

	static final int newsLoaderId = 0;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		menu.setGroupVisible(R.id.menugroup_newspaper, !drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
//		case R.id.action_websearch:
//			// create intent to perform web search for this planet
//			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
//			// catch event that there's no activity to handle intent
//			if (intent.resolveActivity(getPackageManager()) != null) {
//				startActivity(intent);
//			} else {
//				Toast.makeText(this, R.string.app_not_available,
//						Toast.LENGTH_LONG).show();
//			}
//			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	public void selectItem(int position) {
		// update the main content by replacing fragments
		if (mDrawerAdapter.getItem(position) instanceof Uri) {
//			currFragment = new WaitingFragment();
//			new PrepareNewsTask(position).execute((Uri) mDrawerAdapter
//					.getItem(position));
			prepareNews( (Uri) mDrawerAdapter.getItem(position) );
		} else {
			currFragment = new AboutFragment();
		}
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, currFragment).commit();

		// update selected item and title, then close the drawer
		drawerSelectedPosition = position;
		Log.d(tag, "selectItem()" + drawerSelectedPosition);
		mDrawerList.setItemChecked(position, true);
		//setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	/** 准备报刊要显示的内容 */
	private void prepareNews(Uri uri) {
		ContentResolver cr = getContentResolver();

		Cursor nc = cr.query(uri, new String[] { NewsTableMetaData.NAME },
				null, null, null);
		nc.moveToFirst();
		String newsName = nc.getString(nc
				.getColumnIndex(NewsTableMetaData.NAME));

		int newsId = Integer.valueOf(uri.getLastPathSegment());
		Cursor sc = cr.query(SourceTableMetaData.CONTENT_URI, null,
				SourceTableMetaData.NEWS_ID + " = " + newsId, null, null);

		int idIndex = sc.getColumnIndex(SourceTableMetaData._ID);
		int sourceId;
		ArrayList<Integer> sIds = new ArrayList<Integer>();
		HashMap<Integer, Source> sources = new HashMap<Integer, Source>();
		DataBridge dm = new DataBridge(MainActivity.this);
		for (sc.moveToFirst(); !sc.isAfterLast(); sc.moveToNext()) {
			sourceId = Integer.valueOf(sc.getString(idIndex));
			Log.d(tag, "PrepareNewsTaskResult doInBackground() sourceId: "
					+ sourceId);
			sIds.add(sourceId);
			Source s = dm.getSource(sc, sourceId);
			sources.put(sourceId, s);
		}

		currFragment = new NewsPaperFragment();
		Bundle b = new Bundle();
		b.putSerializable(NewsPaperFragment.ARG_PREP_TASK_RESULT,
				new PrepareNewsTaskResult(newsId, newsName, sIds, sources));
		currFragment.setArguments(b);
	}

	/** 准备报刊要显示的内容
	 * @deprecated */
	private class PrepareNewsTask extends
			AsyncTask<Uri, Integer, PrepareNewsTaskResult> {

		int position;

		public PrepareNewsTask(int position) {
			this.position = position;
		}

		@Override
		protected PrepareNewsTaskResult doInBackground(Uri... params) {
			// TODO Auto-generated method stub
			ContentResolver cr = getContentResolver();

			Cursor nc = cr.query(params[0],
					new String[] { NewsTableMetaData.NAME }, null, null, null);
			nc.moveToFirst();
			String newsName = nc.getString(nc
					.getColumnIndex(NewsTableMetaData.NAME));

			int newsId = Integer.valueOf(params[0].getLastPathSegment());
			Cursor sc = cr.query(SourceTableMetaData.CONTENT_URI, null,
					SourceTableMetaData.NEWS_ID + " = " + newsId, null, null);

			int idIndex = sc.getColumnIndex(SourceTableMetaData._ID);
			int sourceId;
			ArrayList<Integer> sIds = new ArrayList<Integer>();
			HashMap<Integer, Source> sources = new HashMap<Integer, Source>();
			DataBridge dm = new DataBridge(MainActivity.this);
			for (sc.moveToFirst(); !sc.isAfterLast(); sc.moveToNext()) {
				sourceId = Integer.valueOf(sc.getString(idIndex));
				Log.d(tag, "PrepareNewsTaskResult doInBackground() sourceId: "
						+ sourceId);
				sIds.add(sourceId);
				Source s = dm.getSource(sc, sourceId);
				sources.put(sourceId, s);
			}

			return new PrepareNewsTaskResult(newsId, newsName, sIds, sources);
		}

		// This is called when doInBackground() is finished
		@Override
		protected void onPostExecute(PrepareNewsTaskResult result) {
			currFragment = new NewsPaperFragment();
			Bundle b = new Bundle();
			b.putSerializable(NewsPaperFragment.ARG_PREP_TASK_RESULT, result);
			currFragment.setArguments(b);
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, currFragment).commit();
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	//
	// @Override
	// public boolean onKeyUp(int keyCode, KeyEvent event) {
	// // TODO Auto-generated method stub
	// if( keyCode == KeyEvent.KEYCODE_MENU)
	// {
	// Toast.makeText(getApplicationContext(), "菜单键",
	// Toast.LENGTH_SHORT).show();
	// }
	// //else
	// return super.onKeyUp(keyCode, event);
	// }
//	@Override
//	public boolean onMenuOpened(int featureId, Menu menu) {
//
//		Toast.makeText(getApplicationContext(), "菜单", Toast.LENGTH_SHORT)
//				.show();
//
//		if (currFragment instanceof NewsPaperFragment) {
//			NewsPaperFragment npf = (NewsPaperFragment) currFragment;
//			if (npf.isMenuShowing()) {
//				npf.dismissMenu();
//			} else {
//				npf.showMenu(currFragment.getView(), Gravity.BOTTOM, 0, 0);
//			}
//		}
//		return true;// 返回为true 则显示系统menu
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if( resultCode == RESULT_OK ){
			Bundle b = data.getExtras().getBundle(ChooseSourceFragment.ARG_CHOOSE_RESULT);
			//int sourceType = b.getInt(ChooseSourceFragment.ARG_SOURCE_TYPE);
			//ArrayList<Source> result = (ArrayList<Source>) b.getSerializable(ChooseSourceFragment.ARG_SOURCES);
			AddSourceData asd= new AddSourceData();
			asd.newsId = b.getInt(ChooseSourceFragment.ARG_NEWS_ID);
			asd.pageDetail = (PageDetail) b.getSerializable(ChooseSourceActivity.ARG_PAGEDETAIL);
			new AddSourceToDatabase().execute( asd );
		}
	}
	private static class AddSourceData
	{
		public int newsId;
		public PageDetail pageDetail;
	}
	private class AddSourceToDatabase extends AsyncTask<AddSourceData, Integer, Integer>
	{

		int newsId;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, "正在更新信息源", Toast.LENGTH_SHORT ).show();
		}
		
		@Override
		protected Integer doInBackground(AddSourceData... params) {
			// TODO Auto-generated method stub
			newsId = params[ 0 ].newsId;
			PageDetail pageDetail = params[ 0 ].pageDetail;
			ArrayList<Source> sl = pageDetail.ourSource != null ? pageDetail.ourSource : pageDetail.rssSource;
			DataBridge db = new DataBridge(MainActivity.this);
			int size = sl.size();
			int k = 0;						//record success
			for( int i = 0; i < size; i++ ){
				if( db.addSource( newsId, pageDetail, sl.get( i ) ) != null ){
					k++;
				}
			}
			return k;
		}

		@Override
		protected void onPostExecute( Integer k ) {
			// TODO Auto-generated method stub
			int size = mDrawerAdapter.getCount();
			int i;
			for( i = 0; i < size; i++ ){
				
				if( ( mDrawerAdapter.getItem( i ) instanceof Uri ) 
						&& mDrawerAdapter.getItemId( i )  == newsId ){
					break;
				}
			}
			selectItem( i );
			Toast.makeText(MainActivity.this,
					k > 0 ? ("成功添加" + k +"个信息源,核桃壳将会适时更新信息源") : ("报刊中已经存在您选择的信息源"), 
					Toast.LENGTH_SHORT ).show();
		}
		
	}
	
	public static class DrawerNewsAdapter extends CursorAdapter {

		public static final String tag = "DrawerNewsAdapter";
		public LayoutInflater inflater = null;
		public Context mContext = null;
		//public static int ADD_NEWS_VIEW_ID = 998;
		
		public DrawerNewsAdapter(Context context, Cursor c, int flag) {
			super(context, c, flag);
			// TODO Auto-generated constructor stub
			mContext = context;
			inflater = LayoutInflater.from(context);
		}
		
		@Override
		public long getItemId(int position) {
			Cursor cursor = getCursor();
			cursor.moveToPosition(position);
			return cursor.getLong(cursor.getColumnIndex(NewsTableMetaData._ID));
		}

		@Override
		public Object getItem(int position) {
			Cursor cursor = getCursor();
			cursor.moveToPosition(position);
			return Uri.withAppendedPath(NewsTableMetaData.CONTENT_URI,
					String.valueOf( cursor.getLong( cursor.getColumnIndex(NewsTableMetaData._ID) ) ) );
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return super.getCount();// + 1;
		}
	//当ListView需要显示Cursor以外的内容使用该函数
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			if( position < getCount() - 1  ){
//				return super.getView(position, convertView, parent);
//			}
//			else{
//				if( convertView == null ){
//					convertView = inflater.inflate(R.layout.drawer_list_second_image_only_item,
//							parent, false);
//					ImageButton ib = (ImageButton) convertView.findViewById(R.id.drawer_sec_img_only_item_imgbutton);
//					ib.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							Toast.makeText(mContext, "添加报刊", Toast.LENGTH_SHORT).show();
//						}
//					});
//				}
//				//convertView.setId(ADD_NEWS_VIEW_ID);
//				return convertView;
//			}
//		}

		@Override
		public void bindView(View root, Context context, Cursor c) {
			// TODO Auto-generated method stub
			TextView tvna = (TextView) root
					.findViewById(R.id.drawer_sec_no_img_item_nameview);
/*			TextView tvinf = (TextView) root
					.findViewById(R.id.drawer_sec_no_img_item_infoview);*/
			tvna.setText(c.getString(c.getColumnIndex(NewsTableMetaData.NAME)));
			/*tvinf.setText("创建时间"
					+ Utils.getDateTimeString(c
							.getColumnIndex(NewsTableMetaData.CREATED_DATE)));*/
		}

		@Override
		public View newView(Context context, Cursor arg1, ViewGroup parent) {
			// TODO Auto-generated method stub
			View root = inflater.inflate(R.layout.drawer_list_seoncd_no_image_item,
					parent, false);
			return root;
		}

	}
}
