package com.creaty.walnutshell;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creaty.walnutshell.UI.ChooseSourceFragment;
import com.creaty.walnutshell.basic.PageDetail;
import com.creaty.walnutshell.basic.Source;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddSourceActivity extends Activity {

	public static final String tag = "AddSourceActivity";
	public static final String PRE_DEFAULT_ADDRESS = "pref_default_address";
	EditText searchEditText = null;
	SharedPreferences pref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pref = getPreferences(Context.MODE_PRIVATE);
		setContentView(R.layout.activity_add_source);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		//-----------------------------显示待选网址的LOGO--------------------------------------------
				GridView gridview = (GridView) findViewById(R.id.logos_gridview);
				int[] pics=new int[]{
						R.drawable.rec_36kr, R.drawable.rec_geekpark, 
						R.drawable.rec_guokr,R.drawable.rec_ifanr, R.drawable.rec_infzm,
			            R.drawable.rec_sohu, };

				String[] dcrp = new String[]{"36氪","极客公园","果壳网","爱范儿","南方周末","搜狐门户"};
				
				List<Map<String, Object>> items=new ArrayList<Map<String,Object>>();
				for(int i = 0; i < pics.length ; i++){
					Map<String, Object> item=new HashMap<String, Object>();
					item.put("pic", pics[i]);
					item.put("descrption", (String)(dcrp[i]));
					items.add(item);
				}
				SimpleAdapter adapter=new SimpleAdapter(this,items, R.layout.grid_item, new String[]{"pic","descrption"}, 
						new int[]{R.id.imageshow,R.id.name});
			    gridview.setAdapter(adapter);
			    
			    gridview.setOnItemClickListener(new OnItemClickListener()
			    {
			    	@Override
			        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			    		
			    		String[] url = new String[]{"http://www.36kr.com/","http://www.geekpark.net/",
			    				"http://www.guokr.com/","http://www.ifanr.com/","http://www.infzm.com/",
			    				"http://www.sohu.com/"};
			    		Intent i = new Intent(v.getContext(),
								ChooseSourceActivity.class);
						try {
							i.putExtra(ChooseSourceActivity.ARG_URL,new URL(url[position] ));
						} catch (MalformedURLException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						((Activity) v.getContext())
								.startActivityForResult(i, 0);
			    		
			            //Toast.makeText(AddSourceActivity.this, "" + position, Toast.LENGTH_SHORT).show();
			        }	
			    });
				//----------------------------------------------------------------------------------------------------------
		
		
		initEditText();
		initButtons();

	}
	/**
	 * 初始化搜索框，将历史输入填充到搜索框中
	 */
	private void initEditText() {

		searchEditText = (EditText) findViewById(R.id.add_source_editText);
		String s = null;
		if (  (s = pref.getString(PRE_DEFAULT_ADDRESS, "")) != null 
				&& ! s.isEmpty() ) {
			searchEditText.setText(s);
		}
	}

	private void initButtons() {
		// 确定键
		Button gotoButton = (Button) findViewById(R.id.button_add_source_goto);
		gotoButton.setOnClickListener(new OnClickListener() {

			// 处理输入的链接格式不正确问题
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String urlstr = searchEditText.getText().toString();
				if (urlstr == null || urlstr.isEmpty()) {
					new AddSourceAlertDialog().setMessageCode(
							AddSourceAlertDialog.NO_URL).show(
							getFragmentManager(), "add_source_alert_dialog");

				} else if (urlstr.indexOf(".") == -1) {
					new AddSourceAlertDialog().setMessageCode(
							AddSourceAlertDialog.UNKNOWN_URL).show(
							getFragmentManager(), "add_source_alert_dialog");
				} else {

					if (!urlstr.startsWith("http://"))
						urlstr = "http://" + urlstr;
					try {
						URL url = new URL(urlstr);
						PreferenceUtils.saveString(pref, PRE_DEFAULT_ADDRESS, urlstr);
						Intent i = new Intent(v.getContext(),
								ChooseSourceActivity.class);
						i.putExtra(ChooseSourceActivity.ARG_URL, url);
						((Activity) v.getContext())
								.startActivityForResult(i, 0);

					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						new AddSourceAlertDialog().setMessageCode(
								AddSourceAlertDialog.UNKNOWN_URL)
								.show(getFragmentManager(),
										"add_source_alert_dialog");
					}
				}
			}
		});
		// 取消键
		Button cancelButton = (Button) findViewById(R.id.button_add_source_cancel);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK, data);
			finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			// NavUtils.navigateUpFromSameTask(this);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class AddSourceAlertDialog extends DialogFragment {
		public static final int NO_URL = -1;
		public static final int UNKNOWN_URL = -2;

		int messageId = 0;

		public AddSourceAlertDialog setMessageCode(int code) {
			switch (code) {
			case NO_URL:
				messageId = R.string.no_URL_input;
				break;
			case UNKNOWN_URL:
				messageId = R.string.unknown_URL;
				break;
			}
			return this;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.alert)
					.setMessage(messageId)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
			return builder.create();
		}
	}

}
