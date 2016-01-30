package com.creaty.walnutshell.UI;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.creaty.walnutshell.AddSourceActivity;
import com.creaty.walnutshell.ArticleActivity;
import com.creaty.walnutshell.MainActivity;
import com.creaty.walnutshell.R;
import com.creaty.walnutshell.UpdateService;
import com.creaty.walnutshell.UpdateSourceTask;
import com.creaty.walnutshell.basic.Entry;
import com.creaty.walnutshell.basic.Image;
import com.creaty.walnutshell.basic.PrepareNewsTaskResult;
import com.creaty.walnutshell.basic.Source;
import com.creaty.walnutshell.content_provider.DataBridge;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.EntryTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.LogoTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;
import com.creaty.walnutshell.fang.InterfaceImplement;
import com.creaty.walnutshell.graphics.BitmapWorkerTask;
import com.creaty.walnutshell.graphics.GraphicsUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewsPaperFragment extends Fragment {
	final static String tag = "NewsPaperFragment";
	// public static final String ARG_PLANET_NUMBER = "planet_number";
	// public static final String ARG_NEWSPAPER_ID = "newspaper_id";
	public static final String ARG_NEWS_ID = "newsfrag_news_id";
	public static final String ARG_PREP_TASK_RESULT = "prep_task_result";
	ActionBar actionBar;
	View rootView;
	public boolean hasNews = false;
	SeparatedListAdapter adapter = null;
	public int currentReadEntryPosition = -1;
	UpdateSourceTask task = null;

	private LruCache<String, Bitmap> mMemoryCache;
	// GridMenu gridMenu = null;

	PrepareNewsTaskResult pntr = null;

	public NewsPaperFragment() {
		// Empty constructor required for fragment subclasses

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if ((pntr = (PrepareNewsTaskResult) getArguments().getSerializable(
				ARG_PREP_TASK_RESULT)) == null) {
			try {
				throw new Exception("PrepareNewsTaskResult is null !");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Get max available VM memory, exceeding this amount will throw an
		// OutOfMemory exception. Stored in kilobytes as LruCache takes an
		// int in its constructor.
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getByteCount() / 1024;
			}
		};
		// printPntrSources();
		// Log.d(tag, "" + pntr.sources );
	}

	// private void printPntrSources(){
	// for( Integer i : pntr.sources.keySet()){
	// int size = pntr.sources.get( i ).entries.size();
	// for( int k = 0; k < size; k++ ){
	// Log.d(tag, "" + pntr.sources.get( i ).entries.get( k ).name );
	// }
	// }
	// }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (!pntr.sourceIds.isEmpty()) {
			rootView = inflater.inflate(R.layout.fragment_newspaper, container,
					false);
			hasNews = true;
		} else {
			rootView = inflater.inflate(R.layout.fragment_no_news, container,
					false);
			hasNews = false;
		}
		setHasOptionsMenu(true);

		getActivity().setTitle(pntr.newsName);

		prepareListView();
		return rootView;
	}

	private void prepareListView() {
		if (hasNews) {
			ListView listView = (ListView) rootView;
			adapter = prepareNewspaperAdapter();

			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long duration) {
					// TODO Auto-generated method stub
					// String item = (String) adapter.getItem(position);
					Intent i = new Intent().setClass(getActivity(),
							ArticleActivity.class);
					i.putExtra(ArticleActivity.ARG_ENTRY_URI,
							(Uri) adapter.getItem(position));
					i.putExtra(ArticleActivity.ARG_SOURCE_NAME,
							adapter.getSectionName(position));
					// 记录当前阅读的条目位置，将来可能改变View的状态
					currentReadEntryPosition = position;

					// 若成功获取条目，表示用户已经阅读完文章，NewsFragment应该改变相应的View状态
					getActivity().startActivityForResult(i,
							MainActivity.REQUEST_GET_ENTRY);
					// Toast.makeText(getActivity().getApplicationContext(),
					// item,
					// Toast.LENGTH_SHORT).show();
				}

			});
		}
	}

	// public boolean isMenuShowing() {
	// return gridMenu.isShowing();
	// }
	// public void dismissMenu(){
	// gridMenu.dismiss();
	// }
	// public void showMenu( View v, int gravity, int x, int y ){
	// gridMenu.showAtLocation( v, gravity, x, y );
	// }

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.newspaper_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// Handle action buttons
		switch (item.getItemId()) {
		// case R.id.action_websearch:
		// return true;
		case R.id.menu_addsource:
			// Toast.makeText(getActivity().getApplicationContext(),
			// item.getTitle(), Toast.LENGTH_SHORT).show();
			Intent i = new Intent().setClass(getActivity(),
					AddSourceActivity.class);
			getActivity().startActivityForResult(i,
					MainActivity.REQUEST_ADD_SOURCE);
			return true;
		case R.id.menu_delsource:
			if (hasNews) {
				Bundle b = new Bundle();
				b.putInt(DelSourceFragement.ARG_NEWS_ID, pntr.newsId);
				DelSourceFragement dsf = new DelSourceFragement();
				dsf.setArguments(b);
				dsf.show(getFragmentManager(), "del");
			} else {
				Toast.makeText(getActivity(), "该报刊没有源可以删除", Toast.LENGTH_SHORT)
						.show();
			}
			return true;
		case R.id.menu_update:
			if (hasNews) {
				task = new UpdateSourceTask(getActivity(), ((MainActivity)getActivity()).handler);
				task.execute(pntr.newsId, UpdateSourceTask.NEWS_FRAG_TRIGER);
				// Intent in = new Intent(getActivity(), UpdateService.class);
				// in.putExtra(ARG_NEWS_ID, pntr.newsId);
				// getActivity().startService(in);
				Toast.makeText(getActivity(), "已经开始更新报刊", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "该报刊没有源可以更新", Toast.LENGTH_SHORT)
						.show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		int size = menu.size();
		for (int i = 0; i < size; i++) {
			MenuItem mi = menu.getItem(i);
			switch (mi.getItemId()) {
			case R.id.menu_delsource:
				mi.setEnabled(hasNews ? true : false);
				mi.setIcon(hasNews ? R.drawable.ic_cancel
						: R.drawable.ic_cancel_dark);
				break;
			case R.id.menu_update:
				mi.setEnabled(hasNews ? true : false);
				mi.setIcon(hasNews ? R.drawable.ic_refresh
						: R.drawable.ic_refresh_dark);
				break;
			}
		}
	}

	private SeparatedListAdapter prepareNewspaperAdapter() {
		SeparatedListAdapter sla = new SeparatedListAdapter(new SectionAdapter(
				getActivity(), R.layout.newspaper_section_item,
				R.id.newspaper_item_title_view, pntr.sourceIds), false);
		// for (int i = 0; i < 3; i++) {
		// sla.addSection(getResources().getString(R.string.my_newspaper) + i,
		// new ArrayAdapter<String>(getActivity(),
		// R.layout.newspaper_second_item,
		// R.id.newspaper_sec_item_nameview, new String[] {
		// "文章1", "文章2", "文章3" } ));
		// }

		ArrayList<Integer> sourceIds = pntr.sourceIds;
		HashMap<Integer, Source> sources = pntr.sources;
		int size = sourceIds.size();
		int sourceId = 0;
		Source s;
		for (int i = 0; i < size; i++) {
			sourceId = sourceIds.get(i);
			s = sources.get(sourceId);
			if (s.entries != null) {
				sla.addSection(s.name, new EntryAdapter(getActivity(),
						sourceId, s.entries));
			}
		}
		return sla;
	}

	public void simpleLoadBitmap(int sourceId, ImageView imageView) {
		final String imageKey = String.valueOf(String.valueOf(sourceId));

		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.ic_launcher);

			LogoWorkerTask task = new LogoWorkerTask(imageView, getActivity());
			task.execute(sourceId);
		}
	}

	public void loadBitmap(int id, int resId, ImageView imageView) {
		final String imageKey = String.valueOf(resId);

		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.ic_launcher);
			LogoWorkerTask task = new LogoWorkerTask(imageView, getActivity());
			// task.execute(resId, 32, 32);
		}
	}

	public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	public class SectionViewHolder {
		ImageView iv;
		TextView tv;
		ImageButton ib;
	}

	public class SectionAdapter extends ArrayAdapter<String> {
		// 填充数据的list
		/** 源的Id */
		private final ArrayList<Integer> list;

		public SectionAdapter(Context context, int resource,
				int textViewResourceId, ArrayList<Integer> sourceIds) {
			super(context, resource, textViewResourceId);
			// TODO Auto-generated constructor stub
			list = sourceIds;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SectionViewHolder holder = null;
			if (convertView == null) {
				// 获得ViewHolder对象
				holder = new SectionViewHolder();
				// 导入布局并赋值给convertview
				convertView = super.getView(position, convertView, parent);
				// convertView = inflater.inflate(
				// R.layout.choose_source_section_item, null);
				holder.tv = (TextView) convertView
						.findViewById(R.id.newspaper_item_title_view);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.newspaper_item_logo_view);	
				convertView.setTag(holder);
				// holder.cb.setOnCheckedChangeListener(onChecked);
				// 为view设置标签
				
				
				final ImageView imageView = holder.iv;
				ViewTreeObserver vto2 = imageView.getViewTreeObserver();   
		        vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		            @Override   
		            public void onGlobalLayout() { 
		                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);   
		                final int height = imageView.getHeight(); 
		                int width = imageView.getWidth(); 
		                LayoutParams params = (LayoutParams) imageView.getLayoutParams();
						params.width = height;
		            }   
		        });   
		        
			} else {
				// 取出holder
				holder = (SectionViewHolder) convertView.getTag();
			}
			simpleLoadBitmap(list.get(position), holder.iv);
			
			// holder.cb.setOnCheckedChangeListener(onChecked);
			return convertView;
		}
	}

	public class EntryHolder {
		TextView titleView;
		TextView infoView;
	}

	public class EntryAdapter extends BaseAdapter {

		public static final String tag = "EntryAdapter";
		LayoutInflater layinf;
		Context mContext;
		int sourceId;
		ArrayList<Entry> entries;

		public EntryAdapter(Context context, int sourceId,
				ArrayList<Entry> entries) {
			mContext = context;
			layinf = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.sourceId = sourceId;
			this.entries = entries;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// Log.d(tag, "EntryCount" +entries.size() );
			return entries.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Uri.withAppendedPath(EntryTableMetaData.CONTENT_URI,
					String.valueOf(getItemId(position)));
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			long id = entries.get(position).id;
			Log.d(tag, "EntryAdapter " + position + " entryId: " + id);
			return id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			EntryHolder eh = null;
			if (convertView == null) {

				convertView = layinf.inflate(
						R.layout.newspaper_second_no_image_item, parent, false);
				eh = new EntryHolder();
				eh.titleView = (TextView) convertView
						.findViewById(R.id.newspaper_sec_no_img_item_nameview);
				eh.infoView = (TextView) convertView
						.findViewById(R.id.newspaper_sec_no_img_item_infoview);
				// ImageView iv = (ImageView)root
				// .findViewById(R.id.newspaper_sec_item_imgview);

				Log.d(tag, "" + position + " " + entries.get(position).name);
				convertView.setTag(eh);
			} else {
				eh = (EntryHolder) convertView.getTag();
			}
			eh.titleView.setText(entries.get(position).name);
			if(entries.get(position).description.isEmpty()){
				eh.infoView.setVisibility(View.GONE);
			}
			else{
				eh.infoView.setText(entries.get(position).description);
			}
			return convertView;
		}

	}

	public class LogoWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		public static final int reqWidth = 32;
		public static final int reqHeight = 32;
		public static final String tag = "LogoWorkerTask";
		private final WeakReference<ImageView> imageViewReference;
		DataBridge db;
		Uri logoUri = null;
		String logoURL = null;
		Integer sourceId = -1;

		public LogoWorkerTask(ImageView imageView, Context context) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
			db = new DataBridge(context);
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			sourceId = params[0];
			int logoId = db.getLogoId(sourceId);
			logoUri = Uri.withAppendedPath(LogoTableMetaData.CONTENT_URI,
					String.valueOf(logoId));
			logoURL = db.getLogoAddress(logoUri);
			// Log.d(tag, "simpleLoadBitmap " + sourceId + " logo address "
			// + logoAdd);
			InterfaceImplement ii = new InterfaceImplement();
			Bitmap bm = null;
			try {
				Image img = ii.getImage(new URL(logoURL));
				// Log.d(tag, "image2 " + img.size);
				// Log.d(tag,"image2"+(img.is == null));
				bm = GraphicsUtils.decodeImageToBitmap(getActivity(), Thread
						.currentThread().getId(), img, reqWidth, reqHeight);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Toast.makeText(getActivity(), e.toString(),
				// Toast.LENGTH_SHORT);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bm;
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(final Bitmap bitmap) {
			Log.d(tag, "BitmapWorkerTask finished");
			//OutputStream os = db.getLogoOutputStream(logoUri);
			// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
//			try {
//				if (os != null)
//					os.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			if (imageViewReference != null && bitmap != null) {
				// Log.d(tag,
				// "size " + bitmap.getByteCount() + " width "
				// + bitmap.getWidth() + " height "
				// + bitmap.getHeight());

				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					// Log.d(tag, "BitmapWorkerTask finished");
					imageView.setImageBitmap(bitmap);
					addBitmapToMemoryCache(String.valueOf(sourceId), bitmap);
				}
			}
		}

	}

	public void setEntryViewRead() {
		// 未实现
	}

	public static class DelSourceFragement extends DialogFragment {
		public static final String ARG_NEWS_ID = "news_id";
		private Cursor c;
		private int sourceId = -1;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			int newsId = getArguments().getInt(ARG_NEWS_ID);
			final ContentResolver cr = getActivity().getContentResolver();
			c = cr.query(SourceTableMetaData.CONTENT_URI, null,
					SourceTableMetaData.NEWS_ID + " = " + newsId, null, null);

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.delete_source)
					.setSingleChoiceItems(c, -1,
							SourceTableMetaData.SOURCE_NAME,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									c.moveToPosition(which);
									sourceId = c.getInt(c
											.getColumnIndex(SourceTableMetaData._ID));
								}

							})
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (sourceId != -1) {
										cr.delete(
												Uri.withAppendedPath(
														SourceTableMetaData.CONTENT_URI,
														String.valueOf(sourceId)),
												null, null);
										c.close();
										// dialog.dismiss();
										MainActivity m = ((MainActivity) getActivity());
										m.selectItem(m
												.getDrawerSelectedPosition());
									}
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									c.close();
									// dialog.dismiss();
								}
							});
			return builder.create();
		}
	}
}
