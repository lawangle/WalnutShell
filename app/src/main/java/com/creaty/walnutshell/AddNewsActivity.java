package com.creaty.walnutshell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.creaty.walnutshell.UI.DialogListener;
import com.creaty.walnutshell.UI.NewsConfigFragment;
import com.creaty.walnutshell.UI.SeparatedListAdapter;
import com.creaty.walnutshell.basic.DayTime;
import com.creaty.walnutshell.basic.Newspaper;
import com.creaty.walnutshell.content_provider.DataBridge;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddNewsActivity extends Activity {
	public final static String tag = "AddNewsActivity";
	NewsConfigFragment addNewsFragment;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_news);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setTitle(R.string.add_news);
		
		replaceFragment();

		initButton();

	}

	private void replaceFragment(){
		addNewsFragment = new NewsConfigFragment(); 
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.add_news_frame, addNewsFragment).commit();
	}
	@Override
	public void setTitle(int resId) {
		super.setTitle(resId);
		actionBar.setTitle(resId);
	}



	private void initButton() {
		Button positiveButton = (Button) findViewById(R.id.button_add_news_confirm);
		Button negativeButton = (Button) findViewById(R.id.button_add_news_cancel);
		positiveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = addNewsFragment.getNewsName();
				if (name.isEmpty()) {
					AlertDialogFragment a = new AlertDialogFragment();
					a.show(getFragmentManager(), "not_be_named_alert_dialog");
				} else {
					DataBridge db = new DataBridge(AddNewsActivity.this);
					db.addNewspaper(new Newspaper(name,
							addNewsFragment.getTimeList(), System.currentTimeMillis()));
					// 成功返回主界面
					setResult(RESULT_OK);

					AddNewsActivity.this.finish();
				}
			}
		});
		negativeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 取消任务
				setResult(RESULT_CANCELED);

				AddNewsActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_news, menu);
		return true;
	}

	

	public static class AlertDialogFragment extends DialogFragment {

		DialogListener mListener;

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
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.alert)
					.setMessage(R.string.not_be_named_yet)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dismiss();
								}

							});
			return builder.create();
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			// TODO Auto-generated method stub
			super.onDismiss(dialog);
			mListener.onDialogPositiveClick(this);

		}
	}



}
