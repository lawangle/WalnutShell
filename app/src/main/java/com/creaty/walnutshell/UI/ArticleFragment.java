package com.creaty.walnutshell.UI;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import com.creaty.walnutshell.R;
import com.creaty.walnutshell.UI.NewsPaperFragment.LogoWorkerTask;
import com.creaty.walnutshell.basic.ContentDetail;
import com.creaty.walnutshell.basic.Image;
import com.creaty.walnutshell.basic.StringAndImg;
import com.creaty.walnutshell.content_provider.DataBridge;
import com.creaty.walnutshell.content_provider.ProviderUtils;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.LogoTableMetaData;
import com.creaty.walnutshell.fang.InterfaceImplement;
import com.creaty.walnutshell.fang.InternetUtils;
import com.creaty.walnutshell.graphics.BitmapWorkerTask;
import com.creaty.walnutshell.graphics.GraphicsUtils;

import android.app.Activity;
import android.app.Fragment;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class ArticleFragment extends Fragment {

	public static String tag = "ArticleFragment"; 
	public static String ARG_CONTENT_DETAIL = "content_detail";
	private ContentDetail cd = null;
	//SparseArray<File> imgFileMap;
	ArticleAdapter articleAdapter;
	ArrayList<StringAndImg> contentList;
	ImageView iv;
	int winHeight;
	int winWidth;
	int netType;		//网络类型

	private LruCache<String, Bitmap> mMemoryCache;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
	   // imgFileMap = new SparseArray<File>();
	    //获取窗口大小
		DisplayMetrics dm = GraphicsUtils.getDisplayMetrics(getActivity());
		winHeight = dm.heightPixels;
		winWidth = dm.widthPixels;
		//Log.d(tag, "height " + winHeight +" width " + winWidth);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		cd = (ContentDetail) getArguments().getSerializable(ARG_CONTENT_DETAIL);
		//准备ListView的头
		View header = inflater.inflate(R.layout.article_header, null, false);
		TextView title = (TextView) header.findViewById(R.id.article_titleview );
		title.setText(cd.basicInfor.name);
		TextView secTitle = (TextView) header.findViewById(R.id.article_sec_titleview);
		secTitle.setText("来源"+cd.basicInfor.href);
		iv = (ImageView) header.findViewById(R.id.article_header_img);
		
		View v = inflater.inflate(R.layout.fragment_article2, container, false);
		ListView lv = (ListView) v.findViewById(R.id.article_listView);
		lv.addHeaderView(header, cd.basicInfor, false);
		lv.setAdapter(new ArticleAdapter(cd.content, getActivity()));
		return v;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		netType = InternetUtils.getConnectedType(getActivity());

		//new BitmapWorkerTask(iv, getActivity()).execute(R.drawable.creaty_logo);
	} 
	
//	public void loadBitmap(int id, int resId, ImageView imageView) {
//	    final String imageKey = String.valueOf(resId);
//
//	    final Bitmap bitmap = getBitmapFromMemCache(imageKey);
//	    if (bitmap != null) {
//	    	imageView.setImageBitmap(bitmap);
//	    } else {
//	    	imageView.setImageResource(R.drawable.ic_launcher);
//	        BitmapWorkerTask task = new BitmapWorkerTask(imageView,getActivity());
//	        task.execute(resId, winWidth, winHeight );
//	    }
//	}
	public void simpleLoadBitmap(String url, ImageView imageView) {
		final String imageKey = String.valueOf(String.valueOf(url));

		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			//imageView.setImageURI(null);

			ImageWorkerTask task = new ImageWorkerTask(imageView, getActivity());
			Log.d(tag, url);
			task.execute(url);
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
	
	public static class ArtiViewHolder {
		TextView tv;
		ImageView iv;
	}
	
	public  class ArticleAdapter extends BaseAdapter {
		ArrayList<StringAndImg> contentList;
		// Context mContext;
		LayoutInflater layinf;

		public ArticleAdapter(ArrayList<StringAndImg> contentList,
				Context context) {
			this.contentList = contentList;
			layinf = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contentList.size();
		}
		
		@Override
		public boolean areAllItemsEnabled(){
			return false;
		}
		@Override
		public boolean isEnabled(int position){
			return false;//contentList.get(position).type == StringAndImg.TYPE_URL;
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return contentList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ArtiViewHolder avh = null;
			if (convertView == null) {
				convertView = 	layinf.inflate(R.layout.article_paragrap_view, parent, false);
				avh = new ArtiViewHolder();
				avh.tv = (TextView) convertView.findViewById(R.id.article_paragraph_textview);
				avh.iv = (ImageView) convertView.findViewById(R.id.article_paragraph_imgview);
				convertView.setTag(avh);
			}else{
				avh = (ArtiViewHolder) convertView.getTag();
			}
			StringAndImg sai = contentList.get(position);
			if( sai.type == StringAndImg.TYPE_TEXT ){
				if( avh.tv.getVisibility() != View.VISIBLE )
					avh.tv.setVisibility(View.VISIBLE);
				if( avh.iv.getVisibility() != View.GONE )
					avh.iv.setVisibility(View.GONE);
				avh.tv.setText(sai.content);
			}else if( sai.type == StringAndImg.TYPE_URL ){
				if( avh.tv.getVisibility() != View.GONE )
					avh.tv.setVisibility(View.GONE);
				if( avh.iv.getVisibility() != View.VISIBLE )
					avh.iv.setVisibility(View.VISIBLE);
				//avh.tv.setText(null);
				Log.d(tag, sai.content);
				if( netType == ConnectivityManager.TYPE_WIFI ){
					simpleLoadBitmap(sai.content, avh.iv);
				}
				//loadBitmap(position, R.drawable.creaty_logo, avh.iv );
			}else{
				//avh.tv.setVisibility(View.GONE);
				//avh.iv.setVisibility(View.GONE);
			}
			return convertView;
		}
		
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		getActivity().setResult(Activity.RESULT_OK);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	public class ImageWorkerTask extends AsyncTask<String, Void, Bitmap> {
		public final int reqWidth;
		public final int reqHeight;
		public static final String tag = "ImageWorkerTask";
		private final WeakReference<ImageView> imageViewReference;
		DataBridge db;
		String imageUrl = null;
		//Integer sourceId = -1;

		public ImageWorkerTask(ImageView imageView, Context context) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
			db = new DataBridge(context);
			reqWidth = winWidth*4/5;
			reqHeight = winHeight*2/3;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			imageUrl = params[0];
			//int logoId = db.getLogoId(sourceId);
//			Log.d(tag, "simpleLoadBitmap " + sourceId + " logo address "
//					+ logoAdd);
			InterfaceImplement ii = new InterfaceImplement();
			Bitmap bm = null;
			try {
				Image img = ii.getImage(new URL(imageUrl));
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

			if (imageViewReference != null && bitmap != null) {
				 Log.d(tag,
				 "size " + bitmap.getByteCount() + " width "
				 + bitmap.getWidth() + " height "
				 + bitmap.getHeight());
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					// Log.d(tag, "BitmapWorkerTask finished");
					imageView.setImageBitmap(bitmap);
					addBitmapToMemoryCache(String.valueOf(imageUrl), bitmap);
				}
			}
		}

	}
}
