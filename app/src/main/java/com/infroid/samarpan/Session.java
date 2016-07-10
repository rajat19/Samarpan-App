package com.infroid.samarpan;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
	private SharedPreferences prefs;
	
	public Session(Context context) {
		// TODO Auto-generated constructor stub
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void setUserId(int userid) {
        prefs.edit().putInt("userid", userid).commit();
	}

	public Integer getUserId() {
        int id = prefs.getInt("userid", 0);
        return id;
	}

	public void setUserName(String username) {
		prefs.edit().putString("username", username).commit();
	}

	public String getUserName() {
		String name = prefs.getString("username", "");
		return name;
	}
}