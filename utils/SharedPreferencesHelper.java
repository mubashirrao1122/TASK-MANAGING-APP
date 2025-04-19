package com.example.taskmanagerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String PREFS_NAME = "TaskManagerPrefs";
    private static final String KEY_NOTIFICATION_PREF = "notification_pref";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setNotificationPreference(boolean isEnabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATION_PREF, isEnabled);
        editor.apply();
    }

    public boolean getNotificationPreference() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_PREF, true); // Default is true
    }
}