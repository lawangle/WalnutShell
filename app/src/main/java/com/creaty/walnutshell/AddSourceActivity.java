package com.creaty.walnutshell;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddSourceActivity extends Activity {

	public static final String tag = "AddSourceActivity";
	public static final String REF_DEFAULT_ADDRESS = "ref_default_address";
	EditText searchEditText = null;
	Reference ref = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_source);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ref = new Reference(this);
		searchEditText = (EditText) findViewById(R.id.add_source_editText);
		String s = null;
		if( ( s = ref.getString(REF_DEFAULT_ADDRESS)) != null ){
			searchEditText.setText(s);
		}
//		searchEditText.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				((EditText)v).selectAll();
//			}
//		});
//		searchEditText.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				v.requestFocus();
//				
//				return false;
//			}
//		});
		Button gotoButton = (Button) findViewById(R.id.button_add_source_goto);
		gotoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String urlstr =searchEditText.getText().toString(); 
				if( urlstr == null || urlstr.isEmpty()){
					new AddSourceAlertDialog()
						.setMessageCode(AddSourceAlertDialog.NO_URL)
						.show(getFragmentManager(), "add_source_alert_dialog");
					
				}else if(urlstr.indexOf(".") == -1){
					new AddSourceAlertDialog()
					.setMessageCode(AddSourceAlertDialog.UNKNOWN_URL)
					.show(getFragmentManager(), "add_source_alert_dialog");
				}else{
					
					if( ! urlstr.startsWith("http://" ) ) 
							urlstr = "http://" + urlstr;
					try {
						URL url  = new URL(urlstr);
						ref.saveString(REF_DEFAULT_ADDRESS, urlstr);
						Intent i = new Intent(v.getContext(), ChooseSourceActivity.class);
						i.putExtra(ChooseSourceActivity.ARG_URL, url );
						((Activity) v.getContext()).startActivityForResult(i, 0);
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						new AddSourceAlertDialog()
							.setMessageCode(AddSourceAlertDialog.UNKNOWN_URL )
							.show(getFragmentManager(), "add_source_alert_dialog");
					}
				}
			}
		});
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
		if( resultCode == RESULT_OK ){
			setResult(RESULT_OK, data);
			finish();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        //NavUtils.navigateUpFromSameTask(this);
	    	this.finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	public static class AddSourceAlertDialog extends DialogFragment
	{
		public static final int NO_URL = -1;
		public static final int UNKNOWN_URL = -2;
		
		int messageId  = 0;
		
		public AddSourceAlertDialog setMessageCode( int  code ){
			switch( code ){
			case NO_URL:
				messageId = R.string.no_URL_input; break;
			case UNKNOWN_URL:
				messageId = R.string.unknown_URL; break;
			}
			return this;
		}
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
		    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    builder.setTitle(R.string.alert)
		    	.setMessage(messageId)
		    	.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							
						}
					});
		    return builder.create();
		}
	}
	public class Reference {
		private Activity context;
		public Reference(Activity context){
			this.context = context;
		}
		public void saveString(String key,String value){
			SharedPreferences preferences = context.getPreferences(Context.MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putString(key, value);
			editor.commit();
		}
		public String getString(String key){
			SharedPreferences preferences = 
					context.getPreferences( Context.MODE_PRIVATE);
			return preferences.getString(key, null);
		}
	}
}
