package com.creaty.walnutshell.UI;

import java.util.ArrayList;
import java.util.HashMap;

import com.creaty.walnutshell.AddSourceActivity;
import com.creaty.walnutshell.ArticleActivity;
import com.creaty.walnutshell.ChooseSourceActivity;
import com.creaty.walnutshell.MainActivity;
import com.creaty.walnutshell.R;
import com.creaty.walnutshell.basic.Entry;
import com.creaty.walnutshell.basic.PageDetail;
import com.creaty.walnutshell.basic.PrepareNewsTaskResult;
import com.creaty.walnutshell.basic.Source;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.EntryTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
	public static final String ARG_PREP_TASK_RESULT = "prep_task_result";
	View rootView;
	public boolean hasNews = false;
	SeparatedListAdapter adapter = null;
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
		Log.d(tag, "" + pntr);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (!pntr.sourceIds.isEmpty()) {
			rootView = inflater.inflate(R.layout.fragment_newspaper,
					container, false);
			hasNews = true;
		} else {
			rootView = inflater.inflate(R.layout.fragment_no_news,
					container, false);
			hasNews = false;
		}
		setHasOptionsMenu(true);

		// gridMenu = new GridMenu(getActivity());
		// gridMenu.update();

		// int i = getArguments().getInt(ARG_PLANET_NUMBER);
		// String planet =
		// getResources().getStringArray(R.array.planets_array)[i];
		// getActivity().setTitle(planet);

		// // 设置菜单键监听
		// rootView.setOnKeyListener(new View.OnKeyListener() {
		//
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		//
		// if (keyCode == KeyEvent.KEYCODE_MENU) {
		// Toast.makeText(getActivity().getApplicationContext(),
		// "菜单键", Toast.LENGTH_SHORT).show();
		// if (gridMenu.isShowing()) {
		// gridMenu.dismiss();
		// return true;
		// } else {
		// gridMenu.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
		// }
		// }
		// return false;
		// }
		// });

		getActivity().setTitle(pntr.newsName);
		return rootView;
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
					getActivity().startActivity(i);
					// Toast.makeText(getActivity().getApplicationContext(),
					// item,
					// Toast.LENGTH_SHORT).show();
				}

			});
		}
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
			getActivity().startActivityForResult(i, 0);
			return true;
		case R.id.menu_delsource:
			if( hasNews ){
			Bundle b = new Bundle();
				b.putInt(DelSourceFragement.ARG_NEWS_ID, pntr.newsId);
				DelSourceFragement dsf = new DelSourceFragement();
				dsf.setArguments(b);
				dsf.show(getFragmentManager(), "del");
			}else{
				Toast.makeText(getActivity(), "该报刊没有源可以删除", Toast.LENGTH_SHORT).show();
			}
			return true;
			// case R.id.menu_about:
			// return true;
			//
			// case R.id.menu_manage:
			// case R.id.menu_no_image:
			// case R.id.menu_quit:
			// Toast.makeText(getActivity().getApplicationContext(),
			// item.getTitle(), Toast.LENGTH_SHORT).show();
			// return true;
			// case R.id.menu_showmenu:
			//
			// Toast.makeText(getActivity().getApplicationContext(),
			// item.getTitle(), Toast.LENGTH_SHORT).show();
			// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private SeparatedListAdapter prepareNewspaperAdapter() {
		SeparatedListAdapter sla = new SeparatedListAdapter(getActivity(),
				R.layout.newspaper_section_item, R.id.newspaper_item_title_view);
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

	public void updateNewspaperAdapter() {

	}

	public class EntryAdapter extends BaseAdapter {
		Context mContext;
		int sourceId;
		ArrayList<Entry> entries;

		public EntryAdapter(Context context, int sourceId,
				ArrayList<Entry> entries) {
			mContext = context;
			this.sourceId = sourceId;
			this.entries = entries;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
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
			if (convertView == null) {
				LayoutInflater layinf = null;
				layinf = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View root = layinf.inflate(
						R.layout.newspaper_second_no_image_item, parent, false);
				TextView tvna = (TextView) root
						.findViewById(R.id.newspaper_sec_no_img_item_nameview);
				TextView tvinf = (TextView) root
						.findViewById(R.id.newspaper_sec_no_img_item_infoview);
				// ImageView iv = (ImageView)root
				// .findViewById(R.id.newspaper_sec_item_imgview);
				tvna.setText(entries.get(position).name);
				tvinf.setText(entries.get(position).description);
				return root;
			} else
				return convertView;
		}

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
									cr.delete(Uri.withAppendedPath(
											SourceTableMetaData.CONTENT_URI,
											String.valueOf(sourceId)), null,
											null);
									c.close();
									dialog.dismiss();
									MainActivity m = ((MainActivity) getActivity());
									m.selectItem(m.getDrawerSelectedPosition());
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									c.close();
									dialog.dismiss();
								}
							});
			return builder.create();
		}
	}
}
