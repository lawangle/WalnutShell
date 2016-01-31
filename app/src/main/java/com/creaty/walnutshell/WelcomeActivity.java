package com.creaty.walnutshell;


import com.creaty.walnutshell.alarm.AlarmFactory;
import com.creaty.walnutshell.content_provider.DataBridge;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class WelcomeActivity extends Activity implements AnimationListener {
	public static final String tag = "WelcomeActivity";
	private ImageView imageView = null;
	private Animation alphaAnimation = null;
	private InitNewspapersTask intask = null;
	private boolean isfinishAnim = false;

	public static final String PRE_IS_FIRST_START = "is_first_start";
	private boolean isFristStart = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		imageView = (ImageView) findViewById(R.id.welcome_image_view);

		intask = new InitNewspapersTask();
		intask.execute(true);

		alphaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.welcome_alpha);
		alphaAnimation.setFillEnabled(true);
		alphaAnimation.setFillAfter(true);
		imageView.setAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(this);
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		isfinishAnim = true;
		if (intask.getStatus().equals(AsyncTask.Status.FINISHED)) {
			startMainActivity();
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	private class InitNewspapersTask extends
			AsyncTask<Boolean, Integer, Integer> {

		@Override
		protected Integer doInBackground(Boolean... params) {
			// TODO Auto-generated method stub
			DataBridge db = new DataBridge(WelcomeActivity.this);
			SharedPreferences pref = WelcomeActivity.this
					.getPreferences(Context.MODE_PRIVATE);
			//SharedPreferences sp = WelcomeActivity.this.getSharedPreferences(, mode)
			Log.d(tag,"InitNewspapersTask");
			if (isFristStart = pref.getBoolean(PRE_IS_FIRST_START, true)) {
				Log.d(tag, "isFirst" + isFristStart );
				db.initNewspapers();
				PreferenceUtils.saveBoolean(pref, PRE_IS_FIRST_START, false);
			}
			db.testUpdate3();
			//AlarmFactory.sendAlarm(WelcomeActivity.this, 1, 1, new DayTime(1,1));
			AlarmFactory.sendAllAlarms(WelcomeActivity.this);
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (isfinishAnim) {
				startMainActivity();
			}
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}

	public void startMainActivity() {
		Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
		startActivity(i);
		finish();
		
	}

}