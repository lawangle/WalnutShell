package com.creaty.walnutshell;

import java.util.Calendar;

import com.creaty.tester.IReportBack;
import com.creaty.tester.MonitoredDebugActivity;
import com.creaty.walnutshell.alarm.AlarmFactory;
import com.creaty.walnutshell.basic.DayTime;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;
import com.creaty.walnutshell.content_provider.ProviderTest;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

public class HelloWorld extends MonitoredDebugActivity
implements IReportBack
{
	public static final String tag="HelloWorld";
	private ProviderTest providerTester = null;
	private static final int URL_LOADER = 0;
	
	public HelloWorld()
	{
		super(R.menu.main_menu,tag);
		this.retainState();
		providerTester = new ProviderTest(this,this);
	}
    protected boolean onMenuItemSelected(MenuItem item)
    {
    	Log.d(tag,item.getTitle().toString());
    	if (item.getItemId() == R.id.menu_add_source)
    	{
    		providerTester.addSource();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_display_sources)
    	{
    		providerTester.showSources();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_delete_source)
    	{
    		providerTester.removeSource();;
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_add_entry)
    	{
    		providerTester.addEntry();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_display_entries)
    	{
    		providerTester.showEntries();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_delete_entry)
    	{
    		providerTester.removeEntry();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_add_newspaper)
    	{
    		providerTester.addNewspaper();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_display_newspapers)
    	{
    		providerTester.showNewspapers();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_delete_newspaper)
    	{
    		providerTester.removeNewspaper();
    		return true;
    	}
    	if( item.getItemId() == R.id.menu_alarm_once){
    		AlarmFactory.sendAlarm(getApplicationContext(), 1,1, new DayTime(1, 1));
    	}
    	if( item.getItemId() == R.id.menu_multiple_alarm ){
    		testAlarm();
    	}
    	return true;
    }
	private void testAlarm()
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, 10);
		Calendar[] cs = new Calendar[ 4 ];
		int[] ids = new int[ cs.length ];
		for( int  i = 0; i < 4; i++ ){
			cs[ i ] = c;
			ids[ i ] = Integer.parseInt(
					String.valueOf( c.get(Calendar.HOUR_OF_DAY) )
					+ String.valueOf( c.get(Calendar.MINUTE) )
					+ String.valueOf( i )  );
		}
		AlarmFactory.sendMultiAlarm(this, cs, ids);
	}
}