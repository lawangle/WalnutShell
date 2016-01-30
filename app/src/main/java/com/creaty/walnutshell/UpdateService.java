package com.creaty.walnutshell;

import java.io.IOException;
import java.net.URL;

import com.creaty.walnutshell.UI.NewsPaperFragment;
import com.creaty.walnutshell.alarm.AlarmFactory;
import com.creaty.walnutshell.basic.Source;
import com.creaty.walnutshell.content_provider.DataBridge;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;
import com.creaty.walnutshell.fang.InterfaceImplement;
import com.creaty.walnutshell.fang.InternetInterface2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class UpdateService extends Service {
	static String tag = "UpdateService";
	int mStartMode; // indicates how to behave if the service is killed

	public static final int notifiId = 1;
	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		UpdateService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return UpdateService.this;
		}
	}

	NotificationManager mNotificationManager;
	DataBridge db;

	// SparseArray<UpdateSourceTask> tasks;

	@Override
	public void onCreate() {
		// The service is being created
		mStartMode = Service.START_NOT_STICKY;
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		db = new DataBridge(this);
		// tasks =new SparseArray<UpdateSourceTask>();
	}

	Handler handler = new Handler() {
		public static final String tag = "UpdateCompletedHandler";

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// super.handleMessage(msg);
			//Log.d(tag, "handle message" + msg.arg1 + " " + msg.arg2);
			if (msg.arg2 != 0) {
				ContentResolver cr = getContentResolver();
				ContentValues cv = new ContentValues();
				cv.put(NewsTableMetaData.IS_READ, 0);
				cr.update(Uri.withAppendedPath(NewsTableMetaData.CONTENT_URI,
						String.valueOf(msg.arg1)), cv, null, null);

				Cursor c = cr.query(NewsTableMetaData.CONTENT_URI,
						new String[] { NewsTableMetaData.NAME },
						NewsTableMetaData.IS_READ + " = " + 0, null, null);
				int count = c.getColumnCount();
				if (count == 1) {
					c.moveToFirst();
					showNotification(buildUpdateCompletedNotification(
							"您的报刊 "
									+ c.getString(c
											.getColumnIndex(NewsTableMetaData.NAME))
									+ " 有更新", "更新了" + msg.arg2 + "条信息源"));
				} else {
					showNotification(buildUpdateCompletedNotification(
							"您的报刊有更新", "更新了 " + count + " 份报刊"));
				}
			}
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// The service is starting, due to a call to startService()
		// Toast.makeText(getApplicationContext(), "Hello Service",
		// Toast.LENGTH_SHORT).show();
		// Log.d(tag,
		// "onstartCommand "
		// + intent.getExtras().getInt(AlarmFactory.ARG_NEWS_ID)
		// + " at "
		// + System.currentTimeMillis()
		// + " alarm_time: "
		// + intent.getExtras().getLong(
		// AlarmFactory.ARG_ALARM_TIME));
		Bundle b = intent.getExtras();
		int id = 0;

		if ((id = b.getInt(AlarmFactory.ARG_NEWS_ID, -1)) >= 0) {
			// Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
			new UpdateSourceTask(this, handler).execute(id, UpdateSourceTask.ALARM_TRIGER);

		}
		return mStartMode;
	}

	public void showNotification(Notification notification) {

		mNotificationManager.cancel(notifiId);
		mNotificationManager.notify(notifiId, notification);
	}

	public Notification buildUpdateCompletedNotification(String title,
			String content) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.logo_small)
				.setWhen(System.currentTimeMillis()).setContentTitle(title)
				.setContentText(content).setAutoCancel(true);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.putExtra(MainActivity.ARG_FROM_NOTIFY, true);
		// resultIntent.setData(NewsTableMetaData.CONTENT_URI);

		// The stack builder object will contain an artificial back stack for
		// the started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		return mBuilder.build();
	}

	@Override
	public void onDestroy() {
		// The service is no longer used and is being destroyed
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
