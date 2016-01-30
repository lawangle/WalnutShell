package com.creaty.walnutshell;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtils {

	public static void saveBoolean(SharedPreferences preferences, String key,
			boolean value) {

		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void saveString(SharedPreferences preferences, String key,
			String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

}
