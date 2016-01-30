package com.creaty.walnutshell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.creaty.walnutshell.UpdateService.LocalBinder;
import com.creaty.walnutshell.UI.AboutFragment;
import com.creaty.walnutshell.UI.ChooseSourceFragment;
import com.creaty.walnutshell.UI.DelNewsAlertDialogFragment;
import com.creaty.walnutshell.UI.DelNewsAlertDialogFragment.OnDelNewsConfirmListener;
import com.creaty.walnutshell.UI.NewsPaperFragment;
import com.creaty.walnutshell.UI.SeparatedListAdapter;
import com.creaty.walnutshell.alarm.AlarmFactory;
import com.creaty.walnutshell.basic.*;
import com.creaty.walnutshell.content_provider.DataBridge;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnDelNewsConfirmListener {
	static final String tag = "MainActivity";
	public static final String ARG_MAIN_HANDLER = "main_handler";
	public static final int REQUEST_ADD_NEWSPAPER = 1;
	public static final int REQUEST_ADD_SOURCE = 2;
	public static final int REQUEST_GET_ENTRY = 3;
	public static final String PRE_IS_FIRST_COME = "is_first_come";
	public static final String ARG_FROM_NOTIFY = "from_notification";
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private SeparatedListAdapter mDrawerAdapter;
	private DrawerNewsAdapter newsAdapter;
	private ActionBarDrawerToggle mDrawerToggle;

	private Fragment currFragment = null;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private int drawerSelectedPosition;

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.arg2 != 0){
				selectItem(drawerSelectedPosition);
				Toast.makeText(MainActivity.this, "成功更新"+msg.arg2+"条信息源", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(MainActivity.this, "没有发现可更新的源", Toast.LENGTH_SHORT).show();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = getTitle();

		initDrawer();

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState == null) {
			Log.d(tag, "onCreate()");
			drawerSelectedPosition = 1;
			selectItem(drawerSelectedPosition);
		}

	}

	/**
	 * 初始化抽屉。设置外观、监听器、触发器
	 */
	private void initDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerAdapter = prepareDrawerAdapter();
		mDrawerList.setAdapter(mDrawerAdapter);

		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				selectItem(position);
			}
		});
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
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Log.d(tag, "onStart()" + drawerSelectedPosition );

		if (getIntent().getBooleanExtra(ARG_FROM_NOTIFY, false)) {
			mDrawerLayout.openDrawer(mDrawerList);
		} else {
			SharedPreferences pref = MainActivity.this
					.getPreferences(Context.MODE_PRIVATE);
			// 首次进入MainActivity?
			if (pref.getBoolean(PRE_IS_FIRST_COME, true)) {
				mDrawerLayout.openDrawer(mDrawerList);
				PreferenceUtils.saveBoolean(pref, PRE_IS_FIRST_COME, false);
			}
		}

	}



	public int getDrawerSelectedPosition() {
		return drawerSelectedPosition;
	}

	private SeparatedListAdapter prepareDrawerAdapter() {
		SeparatedListAdapter sla = new SeparatedListAdapter(this,
				R.layout.drawer_list_item, R.id.drawer_item_title_view, false);
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(NewsTableMetaData.CONTENT_URI, null, null, null,
				null);
		newsAdapter = new DrawerNewsAdapter(this, c,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		sla.addSection(getResources().getString(R.string.my_newspaper),
				newsAdapter);
		return sla;
	}

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
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		menu.setGroupVisible(R.id.menugroup_drawer, drawerOpen);
		menu.setGroupVisible(R.id.menugroup_newspaper, !drawerOpen);
		if (drawerOpen) {
			int size = menu.size();
			int count = newsAdapter.getCount();
			for (int i = 0; i < size; i++) {
				MenuItem mi = menu.getItem(i);
				if (mi.getItemId() == R.id.menu_delete_newspaper) {
					mi.setEnabled(count > 0 ? true : false);
					mi.setIcon(count > 0 ? R.drawable.ic_cancel
							: R.drawable.ic_launcher);
					break;
				}
			}
		}
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
		case R.id.menu_addnews:
			Toast.makeText(this, "添加报刊", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, AddNewsActivity.class);
			startActivityForResult(i, REQUEST_ADD_NEWSPAPER);
			return true;
		case R.id.menu_delnews:
			new DelNewsFragment()
					.show(getFragmentManager(), "delete_newspaper");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void selectItem(int position) {
		// update the main content by replacing fragments
		if (mDrawerAdapter.getItem(position) instanceof Uri) {

			Uri uri = (Uri) mDrawerAdapter.getItem(position);
			// 更新报刊阅读状态
			ContentResolver cr = getContentResolver();
			ContentValues cv = new ContentValues();
			cv.put(NewsTableMetaData.IS_READ, 1);
			cr.update(uri, cv, null, null);
			// 准备fragment
			Bundle b = prepareNews(uri);
			currFragment = new NewsPaperFragment();
			currFragment.setArguments(b);
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
		// setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	/** 准备报刊要显示的内容 */
	private Bundle prepareNews(Uri uri) {
		ContentResolver cr = getContentResolver();

		Cursor nc = cr.query(uri, new String[] { NewsTableMetaData.NAME },
				null, null, null);
		nc.moveToFirst();
		String newsName = nc.getString(nc
				.getColumnIndex(NewsTableMetaData.NAME));
		nc.close();

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
		sc.close();
		Bundle b = new Bundle();
		b.putSerializable(NewsPaperFragment.ARG_PREP_TASK_RESULT,
				new PrepareNewsTaskResult(newsId, newsName, sIds, sources));
		return b;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_ADD_SOURCE) {
				Bundle b = data.getExtras().getBundle(
						ChooseSourceFragment.ARG_CHOOSE_RESULT);
				// int sourceType =
				// b.getInt(ChooseSourceFragment.ARG_SOURCE_TYPE);
				// ArrayList<Source> result = (ArrayList<Source>)
				// b.getSerializable(ChooseSourceFragment.ARG_SOURCES);
				AddSourceData asd = new AddSourceData();
				asd.newsId = b.getInt(ChooseSourceFragment.ARG_NEWS_ID);
				asd.pageDetail = (PageDetail) b
						.getSerializable(ChooseSourceActivity.ARG_PAGEDETAIL);
				new AddSourceToDatabase().execute(asd);
			}
			if (requestCode == REQUEST_ADD_NEWSPAPER) {
				//
				Log.d(tag, "for result" + REQUEST_ADD_NEWSPAPER);
				ContentResolver cr = getContentResolver();
				Cursor c = cr.query(NewsTableMetaData.CONTENT_URI, null, null,
						null, null);
				newsAdapter.changeCursor(c);
				mDrawerAdapter.notifyDataSetChanged();
				// creates call to onPrepareOptionsMenu()
				invalidateOptionsMenu();
			}
			if(requestCode == REQUEST_GET_ENTRY ){
				Log.d(tag, "for result" + REQUEST_GET_ENTRY);
				if( currFragment instanceof NewsPaperFragment){
					((NewsPaperFragment)currFragment).setEntryViewRead();
				}
			}
		}
	}

	private static class AddSourceData {
		public int newsId;
		public PageDetail pageDetail;
	}

	private class AddSourceToDatabase extends
			AsyncTask<AddSourceData, Integer, Integer> {

		int newsId;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, "正在更新信息源", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		protected Integer doInBackground(AddSourceData... params) {
			// TODO Auto-generated method stub
			newsId = params[0].newsId;
			PageDetail pageDetail = params[0].pageDetail;
			ArrayList<Source> sl = pageDetail.ourSource != null ? pageDetail.ourSource
					: pageDetail.rssSource;
			DataBridge db = new DataBridge(MainActivity.this);
			int size = sl.size();
			int k = 0; // record success
			for (int i = 0; i < size; i++) {
				Log.d(tag, sl.get(i).toString());
				if (db.addSource(newsId, pageDetail, sl.get(i)) != null) {
					k++;
				}
			}
			return k;
		}

		@Override
		protected void onPostExecute(Integer k) {
			// TODO Auto-generated method stub
			int size = mDrawerAdapter.getCount();
			int i;
			for (i = 0; i < size; i++) {

				if ((mDrawerAdapter.getItem(i) instanceof Uri)
						&& mDrawerAdapter.getItemId(i) == newsId) {
					break;
				}
			}
			selectItem(i);
			Toast.makeText(
					MainActivity.this,
					k > 0 ? ("成功添加" + k + "个信息源,核桃壳将会适时更新信息源")
							: ("报刊中已经存在您选择的信息源"), Toast.LENGTH_SHORT).show();
		}

	}

	public class DrawerViewHolder {
		ImageView iv;
		TextView tv;
	}

	public class DrawerNewsAdapter extends CursorAdapter {

		public static final String tag = "DrawerNewsAdapter";
		public LayoutInflater inflater = null;
		public Context mContext = null;

		// public static int ADD_NEWS_VIEW_ID = 998;

		public DrawerNewsAdapter(Context context, Cursor c, int flag) {
			super(context, c, flag);
			// TODO Auto-generated constructor stub
			mContext = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public long getItemId(int position) {
			if (position < getCount()) { // - 1) {
				Cursor cursor = getCursor();
				cursor.moveToPosition(position);
				return cursor.getLong(cursor
						.getColumnIndex(NewsTableMetaData._ID));
			}
			return position;
		}

		@Override
		public Object getItem(int position) {
			if (position < getCount()) {// - 1) {
				Cursor cursor = getCursor();
				cursor.moveToPosition(position);
				return Uri.withAppendedPath(NewsTableMetaData.CONTENT_URI,
						String.valueOf(cursor.getLong(cursor
								.getColumnIndex(NewsTableMetaData._ID))));
			}
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return super.getCount();// + 1;
		}

		// // 当ListView需要显示Cursor以外的内容使用该函数
		// @Override
		// public View getView(int position, View convertView, ViewGroup parent)
		// {
		// // TODO Auto-generated method stub
		// Log.d(tag, "psoition " + position + " " + "count" + getCount());
		// if (position < getCount() - 1) {
		// // return super.getView(position, convertView, parent);
		// Cursor c = getCursor();
		// c.moveToPosition(position);
		// convertView = newView(mContext, c, parent);
		// bindView(convertView, mContext, c);
		// } else {
		// convertView = inflater.inflate(
		// R.layout.drawer_list_second_image_only_item, parent,
		// false);
		// ImageButton ib = (ImageButton) convertView
		// .findViewById(R.id.drawer_sec_img_only_item_imgview);
		// ib.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Toast.makeText(mContext, "添加报刊", Toast.LENGTH_SHORT)
		// .show();
		// Intent i = new Intent(mContext, AddNewsActivity.class);
		// ((Activity) mContext).startActivityForResult(i,
		// REQUEST_ADD_NEWSPAPER);
		// }
		// });
		// // convertView.setId(ADD_NEWS_VIEW_ID);
		// }
		// return convertView;
		// }

		@Override
		public void bindView(View root, Context context, Cursor c) {
			// TODO Auto-generated method stub
			DrawerViewHolder dvh = (DrawerViewHolder) root.getTag();

			dvh.tv.setText(c.getString(c.getColumnIndex(NewsTableMetaData.NAME)));
			int flag = c.getInt(c.getColumnIndex(NewsTableMetaData.IS_READ));
			int id = (int) (5 * Math.random());

			switch (id % 5) {
			case 0:
				dvh.iv.setImageResource(flag == 0 ? R.drawable.dot_purple
						: R.drawable.dot_purple_ring);
				break;
			case 1:
				dvh.iv.setImageResource(flag == 0 ? R.drawable.dot_blue
						: R.drawable.dot_blue_ring);
				break;
			case 2:
				dvh.iv.setImageResource(flag == 0 ? R.drawable.dot_red
						: R.drawable.dot_red_ring);
				break;
			case 3:
				dvh.iv.setImageResource(flag == 0 ? R.drawable.dot_gray
						: R.drawable.dot_gray_ring);
				break;
			case 4:
				dvh.iv.setImageResource(flag == 0 ? R.drawable.dot_yellow
						: R.drawable.dot_yellow_ring);
				break;
			default:
				dvh.iv.setImageResource(flag == 0 ? R.drawable.dot_red
						: R.drawable.dot_red_ring);
				break;
			}

			/*
			 * tvinf.setText("创建时间" + Utils.getDateTimeString(c
			 * .getColumnIndex(NewsTableMetaData.CREATED_DATE)));
			 */
		}

		@Override
		public View newView(Context context, Cursor arg1, ViewGroup parent) {
			// TODO Auto-generated method stub
			View root = inflater.inflate(R.layout.drawer_list_second_item,
					parent, false);
			DrawerViewHolder dvh = new DrawerViewHolder();
			dvh.tv = (TextView) root
					.findViewById(R.id.drawer_sec_item_nameview);
			dvh.iv = (ImageView) root
					.findViewById(R.id.drawer_sec_item_imgview);
			root.setTag(dvh);
			return root;
		}

	}

	public static class DelNewsFragment extends DialogFragment {
		private Cursor c;
		private int del_newsId = -1;
		private String del_newsname = "";

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			ContentResolver cr = getActivity().getContentResolver();
			c = cr.query(NewsTableMetaData.CONTENT_URI, null, null, null, null);

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("选择要删除的报刊")
					.setSingleChoiceItems(c, -1, NewsTableMetaData.NAME,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									c.moveToPosition(which);
									del_newsId = c.getInt(c
											.getColumnIndex(NewsTableMetaData._ID));
									del_newsname = c.getString(c
											.getColumnIndex(NewsTableMetaData.NAME));
								}

							})
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (del_newsId != -1) {
										Bundle b = new Bundle();
										b.putInt(
												DelNewsAlertDialogFragment.ARG_ALERT_NEWS_ID,
												del_newsId);
										b.putString(
												DelNewsAlertDialogFragment.ARG_ALERT_NEWS_NAME,
												del_newsname);
										DelNewsAlertDialogFragment adf = new DelNewsAlertDialogFragment();
										adf.setArguments(b);
										adf.show(getFragmentManager(),
												"sure_to _delete_newspaper");
									} else {
										Toast.makeText(
												getActivity(),
												R.string.please_choose_target_newspaper,
												Toast.LENGTH_SHORT).show();
									}
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// dialog.dismiss();
								}
							});
			return builder.create();
		}
	}

	@Override
	public void onDelNewsConfirm(Uri uri) {
		// TODO Auto-generated method stub
		ContentResolver cr = getContentResolver();
		Log.d(tag, "uri " + uri + "delete " + cr.delete(uri, null, null));
		newsAdapter.changeCursor(cr.query(NewsTableMetaData.CONTENT_URI, null, null, null, null));
		mDrawerAdapter.notifyDataSetChanged();
	}


}
