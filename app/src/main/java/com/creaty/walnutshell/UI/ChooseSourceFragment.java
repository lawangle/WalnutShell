package com.creaty.walnutshell.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.creaty.walnutshell.ChooseSourceActivity;
import com.creaty.walnutshell.R;
import com.creaty.walnutshell.basic.Entry;
import com.creaty.walnutshell.basic.PageDetail;
import com.creaty.walnutshell.basic.Source;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseSourceFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String tag = "ChooseSourceFragment";
	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String ARG_PAGE_TITLE = "page_title";
	public static final String ARG_SOURCE_TYPE = "source_type";
	public static final String ARG_SOURCES = "sources";
	public static final String ARG_NEWS_ID = "news_id";
	public static final String ARG_NEWS_CURSOR = "news_cursor";
	public static final String ARG_CHOOSE_RESULT = "choose_result";
	int sourceType = -1;
	// PageDetail pageDetail;
	String pageTitle = null;
	ArrayList<Source> sources;
	ArrayList<String> sections;
	SeparatedListAdapter mAdapter;
	SectionAdapter sectionAdapter;

	public ChooseSourceFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle b = getArguments();
		sourceType = b.getInt(ARG_SOURCE_TYPE);
		sources = (ArrayList<Source>) b.getSerializable(ARG_SOURCES);
		pageTitle = b.getString(ARG_PAGE_TITLE);
		// 截短标题，使用空格分隔
		int spaceoff = pageTitle.indexOf(' ', 1);
		if (spaceoff != -1) {
			pageTitle = pageTitle.substring(0, spaceoff);
		}
		// if (sourceType == Source.SELF_SOURCE) {
		// sources = pageDetail.ourSource;
		// } else {
		// sources = pageDetail.rssSource;
		// }
		if (sources != null && !sources.isEmpty()) {
			sections = new ArrayList<String>();
			int size = sources.size();
			// Log.d(tag, "sources" + sources.size());
			String s = null;
			for (int i = 0; i < size; i++) {
				if ((s = sources.get(i).name).isEmpty()) {
					sources.get(i).name = pageTitle + " 源" + (i + 1);
					// Warning! page_address = ""!!!!!!!!!!!!!!!!!!!!!
					Log.d(tag, sources.get(i).page_address);
					// sources.get(i).page_address = "www.abc.com";
				}
				sections.add(sources.get(i).name);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		// 如果sources不为空，进入选择界面，否则显示提示
		if (!isSourceEmpty(sources)) {
			rootView = inflater.inflate(R.layout.fragment_choose_source,
					container, false);
			// TextView dummyTextView = (TextView) rootView
			// .findViewById(R.id.section_label);

			ListView listview = (ListView) rootView
					.findViewById(R.id.choose_source_listview);
			mAdapter = prepareNewspaperAdapter();
			listview.setAdapter(mAdapter);

			initButtons(rootView);

		} else {
			rootView = inflater.inflate(R.layout.fragment_no_source, container,
					false);
		}
		// dummyTextView.setText(Integer.toString(getArguments().getInt(
		// ARG_SECTION_NUMBER)));
		return rootView;
	}

	private void initButtons(View rootView) {
		Button confirmButton = (Button) rootView
				.findViewById(R.id.button_choose_source_yes);
		Button cancelButton = (Button) rootView
				.findViewById(R.id.button_choose_source_cancel);
		// 确定按钮设置监听器，弹出报刊选择对话框
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Source> result = preparedChooseResult();
				if (!result.isEmpty()) {
					Bundle b = new Bundle();
					b.putInt(ARG_SOURCE_TYPE, sourceType);
					b.putSerializable(ARG_SOURCES, result);
					// getActivity().setResult(ARG_RESULT_OK,
					// new Intent().putExtra(ARG_CHOOSE_RESULT, b));
					DialogFragment newFragment = new ChooseNewsDialog();
					newFragment.setArguments(b);
					newFragment.show(getFragmentManager(),
							"choose_newspapers_dialog");
				} else {
					Toast.makeText(getActivity(),
							R.string.please_choose_one_source_at_least,
							Toast.LENGTH_SHORT);
				}

			}

			/**
			 * 准备用户选择后的结果
			 * 
			 * @return 用户选择的源
			 */
			private ArrayList<Source> preparedChooseResult() {
				ArrayList<Source> result = new ArrayList<Source>();
				SparseBooleanArray sba = sectionAdapter.getIsSelected();
				int size = sba.size();
				for (int i = 0; i < size; i++) {
					if (sba.get(i))
						result.add(sources.get(i));
				}
				return result;
			}

		});
		// 取消按钮设置监听器
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
	}

	private boolean isSourceEmpty(ArrayList<Source> sources) {
		boolean flag = false;
		if (sources != null && !sources.isEmpty()) {
			for (int i = 0; i < sources.size(); i++) {
				if (sources.get(i).entries == null || sources.isEmpty()) {
					flag = true;
					break;
				}
			}
		} else {
			flag = true;
		}
		return flag;
	}

	// 准备好带有分隔栏的ListView
	private SeparatedListAdapter prepareNewspaperAdapter() {
		// SeparatedListAdapter sla = new SeparatedListAdapter(getActivity(),
		// R.layout.choose_source_section_item,
		// R.id.choose_source_item_textview);
		sectionAdapter = new SectionAdapter(getActivity(),
				R.layout.choose_source_section_item,
				R.id.choose_source_item_textview, sections);
		SeparatedListAdapter sla = new SeparatedListAdapter(sectionAdapter,
				false);
		int size = sources.size();
		for (int i = 0; i < size; i++) {
			sla.addSection(sources.get(i).name, new ChooseSourceEntryAdapter(
					getActivity(), sources.get(i).entries));
		}
		return sla;
	}

	private class SectionAdapter extends ArrayAdapter<String> {
		// 填充数据的list
		private final ArrayList<String> list;
		// 用来控制CheckBox的选中状况
		private SparseBooleanArray isSelecteds;
		// 上下文
		//private Context context;
		// 用来导入布局
		//private LayoutInflater inflater = null;

		public SectionAdapter(Context context, int resource,
				int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			//inflater = LayoutInflater.from(context);
			isSelecteds = new SparseBooleanArray();
			list = (ArrayList<String>) objects;
			// 初始化数据
			initial(objects);
		}

		// 初始化isSelected的数据
		private void initial(List<?> list) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				getIsSelected().put(i, false);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			OnCheckedChanged onChecked = null;
			if (convertView == null) {
				// 获得ViewHolder对象
				holder = new ViewHolder();
				onChecked = new OnCheckedChanged(position);
				// 导入布局并赋值给convertview
				convertView = super.getView(position, convertView, parent);
				// convertView = inflater.inflate(
				// R.layout.choose_source_section_item, null);
				holder.tv = (TextView) convertView
						.findViewById(R.id.choose_source_item_textview);
				holder.cb = (CheckBox) convertView
						.findViewById(R.id.choose_source_item_cb);
				// holder.cb.setOnCheckedChangeListener(onChecked);
				holder.cb.setOnClickListener(onChecked);
				// 为view设置标签
				convertView.setTag(holder);
				convertView.setTag(holder.cb.getId(), onChecked);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
				onChecked = (OnCheckedChanged) convertView.getTag(holder.cb
						.getId());
			}

			// 设置list中TextView的显示
			holder.tv.setText(list.get(position));
			// 根据isSelected来设置checkbox的选中状况
			holder.cb.setChecked(getIsSelected().get(position));
			onChecked.setPosition(position);
			// holder.cb.setOnCheckedChangeListener(onChecked);
			return convertView;
		}

		private class ViewHolder {
			TextView tv;
			CheckBox cb;
		}

		private class OnCheckedChanged implements OnClickListener {

			int position;

			public OnCheckedChanged(int position) {
				this.position = position;
			}

			public void setPosition(int position) {
				this.position = position;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 当这个方法被调用时，CheckBox的状态已将改变，直接修改isSelecteds
				isSelecteds.put(position, ((CheckBox) v).isChecked());
				Log.d(tag,
						"OnCheckedChanged" + position + " "
								+ isSelecteds.get(position));
			}

		}

		public SparseBooleanArray getIsSelected() {
			return isSelecteds;
		}

		public void setIsSelected(SparseBooleanArray isSelected) {
			this.isSelecteds = isSelected;
		}

	}

	private class ChooseSourceEntryAdapter extends BaseAdapter {
		LayoutInflater inflater;
		ArrayList<Entry> list;

		public ChooseSourceEntryAdapter(Context context, ArrayList<Entry> list) {
			inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder vh = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.choose_source_second_item, parent, false);
				vh = new ViewHolder();
				vh.tv = (TextView) convertView
						.findViewById(R.id.choose_source_sec_item_textview);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			vh.tv.setText(list.get(position).name);
			return convertView;
		}

		private class ViewHolder {
			TextView tv;
		}
	}

	public static class ChooseNewsDialog extends DialogFragment {
		private Cursor c;
		private int newsId = -1;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			ContentResolver cr = getActivity().getContentResolver();
			c = cr.query(NewsTableMetaData.CONTENT_URI, null, null, null, null);

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.pick_a_newspaper)
					.setSingleChoiceItems(c, -1, NewsTableMetaData.NAME,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									c.moveToPosition(which);
									newsId = c.getInt(c
											.getColumnIndex(NewsTableMetaData._ID));
								}

							})
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (newsId != -1) {
										Bundle b = getArguments();

										Bundle newb = prepareBundleForNews(
												b.getInt(ARG_SOURCE_TYPE),
												(ArrayList<Source>) b
														.getSerializable(ARG_SOURCES));

										getActivity()
												.setResult(
														Activity.RESULT_OK,
														new Intent()
																.putExtra(
																		ARG_CHOOSE_RESULT,
																		newb));
										// dialog.dismiss();
										getActivity().finish();
									} else {
										Toast.makeText(
												getActivity(),
												getActivity()
														.getString(
																R.string.please_choose_target_newspaper),
												Toast.LENGTH_SHORT).show();
									}
								}

								private Bundle prepareBundleForNews(int type,
										ArrayList<Source> sources) {
									Bundle newb = new Bundle();
									// 赋予结果用户选择的报刊Id
									newb.putInt(ARG_NEWS_ID, newsId);
									// 在结果中放置PageDetail, 因为此时Bundle中已经包含选择的源
									// pageDetail中的源要赋予用户选择的源
									PageDetail pd = ((ChooseSourceActivity) getActivity())
											.getPageDetail();
									pd.ourSource = null;
									pd.rssSource = null;
									if (type == Source.SELF_SOURCE) {
										pd.ourSource = sources;
									} else {
										pd.rssSource = sources;
									}
									newb.putSerializable(
											ChooseSourceActivity.ARG_PAGEDETAIL,
											pd);
									return newb;
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

		@Override
		public void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			c.close();
		}
	}
}
