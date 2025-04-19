package com.example.taskmanagerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String PREF_NAME = "task_manager_prefs";
    private static final String PREF_NOTIFICATIONS = "notifications_enabled";
    private static final String PREF_SORT_OPTION = "sort_option";

    public static final int SORT_BY_DATE = 0;
    public static final int SORT_BY_PRIORITY = 1;

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean getNotificationPreference() {
        return sharedPreferences.getBoolean(PREF_NOTIFICATIONS, true);
    }

    public void setNotificationPreference(boolean enabled) {
        sharedPreferences.edit().putBoolean(PREF_NOTIFICATIONS, enabled).apply();
    }

    public int getSortOption() {
        return sharedPreferences.getInt(PREF_SORT_OPTION, SORT_BY_DATE);
    }

    public void setSortOption(int option) {
        sharedPreferences.edit().putInt(PREF_SORT_OPTION, option).apply();
    }
}