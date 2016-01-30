package com.creaty.walnutshell;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.creaty.walnutshell.UI.ArticleFragment;
import com.creaty.walnutshell.UI.WaitingFragment;
import com.creaty.walnutshell.basic.ContentDetail;
import com.creaty.walnutshell.content_provider.DataBridge;
import com.creaty.walnutshell.fang.InterfaceImplement;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ArticleActivity extends Activity {

	public static String tag = "ArticleActivity";
	public static String ARG_SOURCE_NAME = "source_name";
	public static String ARG_ENTRY_URI = "entry_uri";
	
	Fragment currFragment;
	PrepArticleTask patask = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		currFragment = new WaitingFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.article_frame, currFragment).commit();
		Log.d(tag,
				((Uri) getIntent().getExtras().get(ARG_ENTRY_URI)).toString());
		patask = new PrepArticleTask();
		patask.execute((Uri) getIntent().getExtras().get(
				ARG_ENTRY_URI));

		setTitle(getIntent().getExtras().getString(ARG_SOURCE_NAME));
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if( !patask.isCancelled() ){
			patask.cancel(true);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		getActionBar().setTitle(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.article_menu, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			// NavUtils.navigateUpFromSameTask(this);
			this.finish();
			return true;
		case R.id.menu_collect_article:
			Toast.makeText(this, R.string.menu_collect_article,
					Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class PrepArticleTask extends
			AsyncTask<Uri, Integer, ContentDetail> {

		@Override
		protected ContentDetail doInBackground(Uri... params) {
			// TODO Auto-generated method stub
			DataBridge ab = new DataBridge(ArticleActivity.this);
			ContentDetail cd = ab.getEntry(params[0]);
			// 正文内容不存在
			if (cd.content == null || cd.content.isEmpty()) {
				com.creaty.walnutshell.fang.InternetInterface2 ii = new InterfaceImplement();
				try {
					Log.d(tag, cd.basicInfor.href);
					cd = ii.getContent(new URL(cd.basicInfor.href));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ( cd.content != null && !cd.content.isEmpty() ) {
					ab.updateEntry(params[0], cd);
					// 再次获取正文
					cd = ab.getEntry(params[0]);
				}
			}
			// Log.d(tag, cd.toString());
			return cd;
		}

		@Override
		protected void onPostExecute(ContentDetail result) {
			// TODO Auto-generated method stub
			if (result.content != null && !result.content.isEmpty()) {
				currFragment = new ArticleFragment();
				Bundle b = new Bundle();
				b.putSerializable(ArticleFragment.ARG_CONTENT_DETAIL, result);
				currFragment.setArguments(b);
			}else{
				currFragment = new NoContentFragment();
			}
			Toast.makeText( ArticleActivity.this, result.basicInfor.href, Toast.LENGTH_SHORT).show();
			//Log.d(tag, result.toString());
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.article_frame, currFragment).commit();
		}
	}

	public static class NoContentFragment extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub

			View rootView = inflater.inflate(R.layout.fragment_no_content,
					container, false);
			TextView tv= (TextView) rootView.findViewById(R.id.no_contant_textview);
			tv.setText(R.string.no_content_to_show);
			return rootView;
		}
	}
}
