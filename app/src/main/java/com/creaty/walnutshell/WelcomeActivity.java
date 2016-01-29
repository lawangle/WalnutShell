package com.creaty.walnutshell;

import com.creaty.walnutshell.content_provider.DataBridge;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * ��ӭ����
 * @author С����Ҷ
 *
 */
public class WelcomeActivity extends Activity implements AnimationListener {
	private ImageView  imageView = null;
	private Animation alphaAnimation = null;
	private InitNewspapersTask intask = null;
	private boolean isfinishAnim = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		imageView = (ImageView)findViewById(R.id.welcome_image_view);
		
		intask = new InitNewspapersTask();
		intask.execute(true);
		
		alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);
		alphaAnimation.setFillEnabled(true); //����Fill����
		alphaAnimation.setFillAfter(true);  //���ö��������һ֡�Ǳ�����View����
		imageView.setAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(this);  //Ϊ�������ü���
 	}
	
	@Override
	public void onAnimationStart(Animation animation) {
		
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		isfinishAnim = true;
		if( intask.getStatus().equals( AsyncTask.Status.FINISHED ) ){
			Intent i = new Intent(this, MainActivity.class);
			this.startActivity(i);
			this.finish();
		}
	}
	
	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
	private class InitNewspapersTask extends AsyncTask<Boolean, Integer, Integer>
	{

		@Override
		protected Integer doInBackground(Boolean... params) {
			// TODO Auto-generated method stub
			DataBridge db = new DataBridge(WelcomeActivity.this);
			db.initNewspapers();
			return 1; 
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if( isfinishAnim ){
				Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//�ڻ�ӭ��������BACK��
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}
	
}