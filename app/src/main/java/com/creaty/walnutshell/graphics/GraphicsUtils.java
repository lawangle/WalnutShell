package com.creaty.walnutshell.graphics;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.creaty.walnutshell.basic.Image;
import com.creaty.walnutshell.fang.InterfaceImplement;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;

public class GraphicsUtils {

	public static DisplayMetrics getDisplayMetrics(Activity context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		return displaymetrics;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static Bitmap decodeImageToBitmap(Context context, long id, Image img,
			int reqWidth, int reqHeight) {
		Bitmap bm = null;
		System.out.println( "decodeImage");
		File f = storeInTempFile(context, String.valueOf(id), img.is);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream( fis, null, options);
			if( fis != null )
				fis.close();
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			options.inJustDecodeBounds = false;
			fis = new FileInputStream(f);
			bm =  BitmapFactory.decodeStream(fis,null,options);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println("ABCKK" + e.toString());
		}finally{
			try{
			if( fis != null )
				fis.close();
			}catch( IOException e){
				e.printStackTrace();
			}
		}
		f.delete();
		return bm;
	}

	public static File storeInTempFile(Context context, String filename,
			InputStream is) {
		System.out.println("storeInTempFile begin");
		if (is != null) {
			File f = getTempFile(context, filename);
			BufferedOutputStream bos = null;
			int n = -1;
			int total = 0;
			byte[] buf = new byte[1024];
			try {
				bos = new BufferedOutputStream(new FileOutputStream(f));
				while ((n = is.read(buf)) != -1) {
					bos.write(buf, 0, n);
					total += n;
					System.out.println("storeInTempFile " + n);
				}
				System.out.println("storeInTempFile " + total);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
					if (bos != null)
						bos.close(); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//System.out.println("storeInTempFile " + f.getName());
			return f;
		}
		return null;
	}

	public static File getTempFile(Context context, String filename) {
		File file = null;
		try {
			String fileName = filename;
			file = File.createTempFile(fileName, null, context.getCacheDir());
		} catch (IOException e) {
			// Error while creating file
		}

		return file;
	}

	public static File getFile(Context context, String filename) {
		File file = null;
		file =  new File(context.getFilesDir(), filename);
		System.out.println("getFile "+file.getAbsolutePath());
		return file;
	}
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		System.out.println("calculateInSampleSize"+height+" "+width);
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
}
