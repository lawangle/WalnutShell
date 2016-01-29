package com.creaty.walnutshell;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;

import com.creaty.walnutshell.UI.ChooseSourceFragment;
import com.creaty.walnutshell.UI.WaitingFragmentV4;
import com.creaty.walnutshell.basic.Entry;
import com.creaty.walnutshell.basic.PageDetail;
import com.creaty.walnutshell.basic.Source;
import com.creaty.walnutshell.fang.InterfaceImplement;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseSourceActivity extends FragmentActivity implements
		ActionBar.TabListener {
	public static final String tag = "ChooseSourceActivity";
	public static final String ARG_URL = "url";
	public static final String ARG_PAGEDETAIL = "page_detail";
	public static final String ARG_NET_ERROR = "net_error";
	public static final int NET_ERROR = -1;
	public static final int NET_ADDRESS_ERROR = -2;
	public static final int NET_ACCESS_ERROR = -3;

	int netErrorCode = 0;

	public ActionBar actionBar = null;
	public boolean isfinished = false;
	public PageDetail pageDetail = null;
	private Fragment currFragment = null;
	PrimitiveSourceTask pstask = null;
	// final WaitingFragmentV4 waitngFragment = new WaitingFragmentV4();
	// final ChooseSourceFragment selfSourceFragment = new
	// ChooseSourceFragment();
	// final ChooseSourceFragment rssSourceFragment = new
	// ChooseSourceFragment();
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_source);

		// Set up the action bar.
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.choose_source_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		URL url = (URL) getIntent().getExtras().getSerializable(ARG_URL);
		pstask = new PrimitiveSourceTask();
		pstask.execute(url);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (!pstask.isCancelled()) {
			pstask.cancel(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public PageDetail getPageDetail() {
		return pageDetail;
	}

	private class PrimitiveSourceTask extends
			AsyncTask<URL, Integer, PageDetail> {

		@Override
		protected PageDetail doInBackground(URL... params) {
			// TODO Auto-generated method stub
			com.creaty.walnutshell.fang.InternetInterface2 ii = new InterfaceImplement();
			Log.d(tag, "PrimitiveSourceTask do" + params[0]);
			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// PageDetail pd =new PageDetail();
			// Source s;
			// pd.ourSource = new ArrayList<Source>();
			// for( int i = 0; i < 100; i++){
			// s= new Source();
			// s.name = "source"+i;
			// s.type = Source.SELF_SOURCE;
			// s.entries = new ArrayList<Entry>();
			// for( int j = 0; j < 4; j++){
			// s.entries.add(new Entry(-1, ""+j, "", ""));
			// }
			// pd.ourSource.add(s);
			// }
			// pd.rssSource = new ArrayList<Source>();
			// for( int i = 0; i < 4; i++){
			// s= new Source();
			// s.name = "source"+i;
			// s.type = Source.RSS_SOURCE;
			// s.entries = new ArrayList<Entry>();
			// for( int j = 0; j < 4; j++){
			// s.entries.add(new Entry(-1, ""+j, "", ""));
			// }
			// pd.rssSource.add(s);
			// }
			PageDetail pd = null;
			try {
				pd = ii.getPrimitiveSources(params[0]);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				netErrorCode = NET_ADDRESS_ERROR;
				e.printStackTrace();
			} catch (UnknownHostException e) {
				netErrorCode = NET_ACCESS_ERROR;
				e.printStackTrace();
			} catch (IOException e) {
				netErrorCode = NET_ERROR;
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(tag, "Exception");
			}
//			if (pd != null) {
//				Log.d(tag, "pageDetail is not null");
//				if (pd.ourSource != null) {
//					Log.d(tag, "ourSource is not null" + pd.ourSource.size());
//				} else if (pd.rssSource != null) {
//					Log.d(tag, "rssSource is not null" + pd.rssSource.size());
//				}
//			}
			return pd;
		}

		@Override
		protected void onPostExecute(PageDetail result) {
			// TODO Auto-generated method stub
			pageDetail = result;
			isfinished = true;
			mSectionsPagerAdapter.notifyDataSetChanged();
			if ( pageDetail != null 
					&&  pageDetail.rssSource != null 
					&& !pageDetail.rssSource.isEmpty()) {
				actionBar.selectTab(actionBar.getTabAt(1));
			}
			Log.d(tag, "task finished");
		}

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			return super.instantiateItem(container, position);
		}

		// �ú������Է��ظ��������λ�ã������������� instantiateItem() �ķ���ֵ��
		// �� ViewPager.dataSetChanged() �н��Ըú����ķ���ֵ�����жϣ��Ծ����Ƿ����մ���
		// PagerAdapter.instantiateItem() ������
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return PagerAdapter.POSITION_NONE;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			// Log.d(tag, "getItem");
			if (isfinished && netErrorCode >= 0) {
				fragment = new ChooseSourceFragment();
				// Log.d(tag, "isfinished true");
				Bundle b = new Bundle();
				switch (position) {
				case 0:
					b.putInt(ChooseSourceFragment.ARG_SOURCE_TYPE,
							Source.SELF_SOURCE);
					// Log.d(tag, pageDetail.ourSource.toString());
					b.putSerializable(ChooseSourceFragment.ARG_SOURCES,
							pageDetail.ourSource);
					break;
				case 1:
					b.putInt(ChooseSourceFragment.ARG_SOURCE_TYPE,
							Source.RSS_SOURCE);
					b.putSerializable(ChooseSourceFragment.ARG_SOURCES,
							pageDetail.rssSource);
					break;
				}
				b.putString(ChooseSourceFragment.ARG_PAGE_TITLE, pageDetail.pageName);
				fragment.setArguments(b);
			} else if (!isfinished && netErrorCode >= 0) {
				fragment = new WaitingFragmentV4();
				Log.d(tag, "isfinished false");
			} else {
				fragment = new NetErrorFragment();
				Bundle b = new Bundle();
				switch (netErrorCode) {
				case NET_ACCESS_ERROR:
					b.putInt(ARG_NET_ERROR, NET_ACCESS_ERROR);
					break;
				case NET_ADDRESS_ERROR:
					b.putInt(ARG_NET_ERROR, NET_ADDRESS_ERROR);
					break;
				case NET_ERROR:
					b.putInt(ARG_NET_ERROR, NET_ERROR);
					break;
				}
				fragment.setArguments(b);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section_self_selected_source)
						.toUpperCase(l);
			case 1:
				return getString(R.string.title_section_RSS).toUpperCase(l);
			}
			return null;
		}
	}

	public static class NetErrorFragment extends Fragment {
		private TextView mTextView;
		private View rootView;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			rootView = inflater.inflate(R.layout.fragment_net_error, container,
					false);
			// getActivity().setTitle(getResources().getString(R.string.menu_about));

			mTextView = (TextView) rootView
					.findViewById(R.id.net_error_textview);
			switch (getArguments().getInt(ARG_NET_ERROR)) {
			case NET_ACCESS_ERROR:
				mTextView.setText(R.string.net_access_error);
				break;
			case NET_ADDRESS_ERROR:
				mTextView.setText(R.string.net_address_error);
				break;
			case NET_ERROR:
				mTextView.setText(R.string.net_error);
				break;
			}
			return rootView;
		}
	}

}
