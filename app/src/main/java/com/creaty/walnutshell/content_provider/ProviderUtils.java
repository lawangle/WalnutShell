package com.creaty.walnutshell.content_provider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.creaty.walnutshell.basic.DayTime;
import com.creaty.walnutshell.basic.StringAndImg;

public class ProviderUtils {

	public static String alarmTimeToString(ArrayList<DayTime> timelist) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<DayTime> i = timelist.iterator(); i.hasNext();) {
			sb.append(i.next().toString());
		}
		return sb.toString();
	}
	
	public static ArrayList<DayTime> stringToAlarmTime( String s ){
		ArrayList<DayTime> timeList = new ArrayList<DayTime>();
		StringTokenizer st = new StringTokenizer(s, ";");
		while( st.hasMoreTokens() )
		{
			timeList.add( new DayTime( st.nextToken() ));
		}
		return timeList;
	}

	public static String contentListToString( ArrayList<StringAndImg> content ){
		StringBuffer sb = new StringBuffer();
		int size = content.size();
		for( int i = 0 ; i < size; i++ ){
			sb.append( content.get( i ) ).append("\n\n");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	public static ArrayList<StringAndImg> stringToContentList( String s ){
		StringTokenizer st = new StringTokenizer(s, "\n\n");
		ArrayList<StringAndImg> als = new ArrayList<StringAndImg>();
		while( st.hasMoreTokens() ){
			als.add(new StringAndImg(st.nextToken()));
		}
		return als;
	}
	public static void writeContent( ContentResolver cr, Uri updateUri, ArrayList<String> content){
		try{
			OutputStream os = cr.openOutputStream( updateUri );
			for (Iterator<String> it = content.iterator(); it.hasNext();) {
				os.write(it.next().getBytes());
			}
			os.close();
		}catch( IOException e){
			e.printStackTrace();
		}
	}

	@Deprecated
	public static void writeContent(Context context, String filename,
			ArrayList<String> content) {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(context.openFileOutput(filename,
					Context.MODE_PRIVATE));
			for (Iterator<String> it = content.iterator(); it.hasNext();) {
				bos.write(it.next().getBytes());
			}
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getFileName(int id, String tableName) {
		return tableName + id;
	}
}
