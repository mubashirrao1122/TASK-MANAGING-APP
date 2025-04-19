package com.example.taskmanagerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.utils.SharedPreferencesHelper;

public class SettingsFragment extends Fragment {

    private SharedPreferencesHelper sharedPreferencesHelper;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settingfragment, container, false);

        sharedPreferencesHelper = new SharedPreferencesHelper(requireContext());

        Switch notificationSwitch = view.findViewById(R.id.switch_notifications);
        notificationSwitch.setChecked(sharedPreferencesHelper.getNotificationPreference());

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferencesHelper.setNotificationPreference(isChecked);
            String message = isChecked ? "Notifications Enabled" : "Notifications Disabled";
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}