package com.creaty.walnutshell.UI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.creaty.walnutshell.R;
import com.creaty.walnutshell.AddNewsActivity.AlertDialogFragment;
import com.creaty.walnutshell.basic.DayTime;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;

public class NewsConfigFragment extends Fragment implements DialogListener {
	
	public final static String tag = "NewsConfigFragment";
	
	public final static String HOUR_OF_DAY = "hourOfDay";
	public final static String MINUTES = "minutes";
	public final static String ARG_INDEX = "index";
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	SeparatedListAdapter sla;
	public AddNewsUpdateConfiAdapter anuca;
	public AddNewsDescriConfiAdapter andca;
	


	//private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment NewsConfigFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static NewsConfigFragment newInstance(String param1, String param2) {
		NewsConfigFragment fragment = new NewsConfigFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public NewsConfigFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView =  inflater
				.inflate(R.layout.fragment_news_config, container, false);
		initListView( rootView );
		return rootView;
	}

	private void initListView( View rootView ) {
		ListView lv = (ListView) rootView.findViewById(R.id.config_news_listview);
		sla = new SeparatedListAdapter(
				new ArrayAdapter<String>(getActivity(), R.layout.config_news_section_item,
						R.id.config_news_section_textview), false);
		andca = new AddNewsDescriConfiAdapter(getActivity());
		sla.addSection(getString(R.string.description), andca);
		anuca = new AddNewsUpdateConfiAdapter(getActivity(), sla);
		sla.addSection(getString(R.string.update), anuca);
		lv.setAdapter(sla);
	}

//	// TODO: Rename method, update argument and hook method into UI event
//	public void onButtonPressed(Uri uri) {
//		if (mListener != null) {
//			mListener.onFragmentInteraction(uri);
//		}
//	}

//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
//	}

//	@Override
//	public void onDetach() {
//		super.onDetach();
//		mListener = null;
//	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
//	public interface OnFragmentInteractionListener {
//		// TODO: Update argument type and name
//		public void onFragmentInteraction(Uri uri);
//	}

	public String getNewsName(){
		return andca.newsName;
	}
	public ArrayList<DayTime> getTimeList(){
		return anuca.timeList;
	}
	public class SecEdittextViewHolder {
		TextView tv;
		EditText et;
	}

	public class AddNewsDescriConfiAdapter extends BaseAdapter {

		LayoutInflater layinf;
		Context mContext;

		public String newsName;

		public AddNewsDescriConfiAdapter(Context context) {
			mContext = context;
			layinf = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return newsName;
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			SecEdittextViewHolder sevh = null;
			if (convertView == null) {
				convertView = layinf.inflate(
						R.layout.config_news_sec_edittext_item, parent, false);
				sevh = new SecEdittextViewHolder();
				sevh.tv = (TextView) convertView
						.findViewById(R.id.config_news_sec_edittext_textview);
				sevh.et = (EditText) convertView
						.findViewById(R.id.config_news_sec_edittext_edittext);
				sevh.et.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						newsName = s.toString();
					}
				});
				convertView.setTag(sevh);
			} else {
				sevh = (SecEdittextViewHolder) convertView.getTag();
			}
			sevh.tv.setText(R.string.name);
			return convertView;
		}

	}

	public class SecButtonViewHolder {
		TextView tv;
		Button b;
	}

	public class SecSpinnerViewHolder {
		TextView tv;
		Spinner sp;
	}

	private class AddNewsUpdateConfiAdapter extends BaseAdapter {
		LayoutInflater layinf;
		Context mContext;
		BaseAdapter parentAdapter;
		int frequence;
		ArrayList<DayTime> timeList;
		/** 在更新时间列表之前的列表项 */
		int num = 1;
		CharSequence[] defaultTime;
		String secButtonText;
		String[] secSpinnerTexts;

		public AddNewsUpdateConfiAdapter(Context context, BaseAdapter parent) {
			mContext = context;
			layinf = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			frequence = 2;
			parentAdapter = parent;

			defaultTime = mContext.getResources().getTextArray(
					R.array.default_times);
			timeList = new ArrayList<DayTime>();
			timeList.add(new DayTime(defaultTime[0].toString()));
			timeList.add(new DayTime(defaultTime[1].toString()));

			secButtonText = mContext.getString(R.string.time);
			secSpinnerTexts = new String[num];
			secSpinnerTexts[0] = mContext.getString(R.string.everyday);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return timeList.size() + num;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (position >= num && position < (num + timeList.size())) {
				return timeList.get(position - num);
			} else if (position < num) {
				return new Integer(frequence);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (position >= num && position < (num + timeList.size())) {
				SecButtonViewHolder sbvh = null;
				if (convertView == null) {
					convertView = layinf.inflate(
							R.layout.add_news_sec_button_item, parent, false);
					sbvh = new SecButtonViewHolder();
					sbvh.tv = (TextView) convertView
							.findViewById(R.id.add_news_sec_button_textview);
					sbvh.b = (Button) convertView
							.findViewById(R.id.add_news_sec_button_button);
					sbvh.b.setOnClickListener(new ButtonOnClickListener(
							position - num));
					convertView.setTag(sbvh);
				} else {
					sbvh = (SecButtonViewHolder) convertView.getTag();
				}
				sbvh.tv.setText(secButtonText + (position - num + 1));
				sbvh.b.setText(getTimeString(position - num));
			} else {
				SecSpinnerViewHolder ssvh = null;
				if (convertView == null) {
					convertView = layinf.inflate(
							R.layout.config_news_sec_spinner_item, parent, false);
					ssvh = new SecSpinnerViewHolder();
					ssvh.tv = (TextView) convertView
							.findViewById(R.id.config_news_sec_spinner_textview);
					ssvh.sp = (Spinner) convertView
							.findViewById(R.id.config_news_sec_spinner_spinner);

					ArrayAdapter<CharSequence> adapter = ArrayAdapter
							.createFromResource(mContext, R.array.frequence,
									android.R.layout.simple_spinner_item);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					ssvh.sp.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							// Toast.makeText(mContext, "id" + id,
							// Toast.LENGTH_SHORT).show();
							frequence = (int) (id + 1);
							while (frequence < timeList.size()) {
								timeList.remove(timeList.size() - 1);
							}
							while (frequence > timeList.size()) {
								timeList.add(new DayTime(defaultTime[timeList
										.size()].toString()));
							}
							// AddNewsUpdateConfiAdapter.this.notifyDataSetChanged();
							parentAdapter.notifyDataSetChanged();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}

					});
					ssvh.sp.setAdapter(adapter);
					convertView.setTag(ssvh);
				} else {
					ssvh = (SecSpinnerViewHolder) convertView.getTag();
				}
				ssvh.sp.setSelection(frequence - 1);
				ssvh.tv.setText(secSpinnerTexts[0]);
			}
			return convertView;
		}

		private String getTimeString(int index) {
			DayTime dt = timeList.get(index);
			SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
			Date d = new Date(0, 0, 1, dt.hours, dt.minutes);
			return sdf.format(d);
		}

		private class ButtonOnClickListener implements OnClickListener {

			DayTime dayTime;
			int index;

			public ButtonOnClickListener(int index) {
				this.index = index;
				dayTime = timeList.get(this.index);
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putInt(HOUR_OF_DAY, dayTime.hours);
				b.putInt(MINUTES, dayTime.minutes);
				b.putInt(ARG_INDEX, index);
				// Log.d(tag, ""+ dayTime.hours+" "+dayTime.minutes);
				TimeDialogFragment tdf = new TimeDialogFragment();
				tdf.setArguments(b);
				tdf.show(getActivity().getFragmentManager(), "choose_update_time");
			}
		}
	}

	public static class TimeDialogFragment extends DialogFragment implements
			OnTimeSetListener {

		DialogListener mListener;
		public int index;
		public DayTime dayTime;
		boolean changed;

		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			try {
				// Instantiate the NoticeDialogListener so we can send events to
				// the host
				mListener = (DialogListener) activity;
			} catch (ClassCastException e) {
				// The activity doesn't implement the interface, throw exception
				throw new ClassCastException(activity.toString()
						+ " must implement NoticeDialogListener");
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceResolver) {
			Bundle b = getArguments();
			dayTime = new DayTime(b.getInt(HOUR_OF_DAY), b.getInt(MINUTES));
			index = b.getInt(ARG_INDEX);
			changed = false;
			TimePickerDialog tpd = new TimePickerDialog(getActivity(), this,
					dayTime.hours, dayTime.minutes, true);
			return tpd;
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			// TODO Auto-generated method stub
			super.onDismiss(dialog);
			if (changed) {
				mListener.onDialogPositiveClick(this);
			} else {
				mListener.onDialogNegativeClick(this);
			}
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			// Log.d(tag, "onTimeSet"+hourOfDay);
			dayTime.hours = hourOfDay;
			dayTime.minutes = minute;
			changed = true;
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		Log.d(tag, "onDialogPositiveClick");
		if (dialog instanceof TimeDialogFragment) {
			TimeDialogFragment tdf = (TimeDialogFragment) dialog;
			// Log.d(tag, "onDialogPositiveClick "
			// +anuca.timeList.get(tdf.index));
			// Log.d(tag, "onDialogPositiveClick " +tdf.dayTime);
			if (anuca.timeList.get(tdf.index).updateTime(tdf.dayTime.hours,
					tdf.dayTime.minutes)) {
				Log.d(tag, "onDialogPositiveClick");
				sla.notifyDataSetChanged();
			}
		}
		if (dialog instanceof AlertDialogFragment) {
			if (((SecEdittextViewHolder) andca.getView(0, null, null).getTag()).et
					.requestFocus()) {
				Log.d(tag, "requestFocus");
			}
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub

	}
}
