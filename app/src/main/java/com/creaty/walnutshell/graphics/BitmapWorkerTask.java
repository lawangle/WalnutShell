package com.creaty.walnutshell.graphics;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	public static final String tag = "BitmapWorkerTask";
	private final WeakReference<ImageView> imageViewReference;
	private int data = 0;
	Context context;

	public BitmapWorkerTask(ImageView imageView, Context context) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.context = context;
	}

	// Decode image in background.
	@Override
	protected Bitmap doInBackground(Integer... params) {
		data = params[0];
		return GraphicsUtils.decodeSampledBitmapFromResource(
				context.getResources(), data, 100, 100 );
	}

	// Once complete, see if ImageView is still around and set bitmap.
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		Log.d(tag, "BitmapWorkerTask finished");
		if (imageViewReference != null && bitmap != null) {
			Log.d(tag, "BitmapWorkerTask finished");
			final ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				Log.d(tag, "BitmapWorkerTask finished");
				imageView.setImageBitmap(bitmap);
			}
			context = null;
		}
	}
}