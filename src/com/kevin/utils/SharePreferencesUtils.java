package com.kevin.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtils {

	private SharedPreferences mPreferences;

	private static final String PREFERENCE_NAME = "downloader";

	public SharePreferencesUtils(Context context) {

		mPreferences = context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
	}

	public void save(String key, boolean value) {

		mPreferences.edit().putBoolean(key, value).commit();

	}

	public boolean isSuccess(String key) {
		return mPreferences.getBoolean(key, false);
	}

}
