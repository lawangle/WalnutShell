package com.creaty.walnutshell;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	public static final String tag = "AlarmReceiver";
	public AlarmReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast. 
		Toast.makeText(context, tag, Toast.LENGTH_SHORT).show();
		Log.d(tag, "onstartCommand" + intent.getExtras().getInt("id"));
//		throw new UnsupportedOperationException("Not yet implemented");
	}
}
