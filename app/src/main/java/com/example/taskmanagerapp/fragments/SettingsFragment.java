package com.example.taskmanagerapp.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.utils.SharedPreferencesHelper;
import com.google.android.material.button.MaterialButton;

public class SettingsFragment extends Fragment {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private SwitchCompat notificationSwitch;
    private RadioGroup sortOptionGroup;
    private MaterialButton saveButton;
    private CardView settingsCard;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settingfragment, container, false);

        // Initialize SharedPreferencesHelper
        sharedPreferencesHelper = new SharedPreferencesHelper(requireContext());

        // Find views
        notificationSwitch = view.findViewById(R.id.switch_notifications);
        sortOptionGroup = view.findViewById(R.id.radio_sort_option);
        saveButton = view.findViewById(R.id.btn_save_settings);
        settingsCard = view.findViewById(R.id.settings_card);

        // Load saved preferences
        loadSettings();

        // Set up save button click listener
        saveButton.setOnClickListener(v -> {
            saveSettings();
            animateSaveButton();
        });

        // Add entrance animation for the card
        animateCardEntrance();

        return view;
    }

    private void loadSettings() {
        // Load notification preference
        notificationSwitch.setChecked(sharedPreferencesHelper.getNotificationPreference());

        // Load sort preference
        int sortOption = sharedPreferencesHelper.getSortOption();
        if (sortOption == SharedPreferencesHelper.SORT_BY_PRIORITY) {
            sortOptionGroup.check(R.id.radio_sort_priority);
        } else {
            sortOptionGroup.check(R.id.radio_sort_date);
        }
    }

    private void saveSettings() {
        // Save notification preference
        boolean notificationsEnabled = notificationSwitch.isChecked();
        sharedPreferencesHelper.setNotificationPreference(notificationsEnabled);

        // Save sort preference
        int selectedSortOption = sortOptionGroup.getCheckedRadioButtonId();
        int sortOption = (selectedSortOption == R.id.radio_sort_priority)
                ? SharedPreferencesHelper.SORT_BY_PRIORITY
                : SharedPreferencesHelper.SORT_BY_DATE;
        sharedPreferencesHelper.setSortOption(sortOption);

        Toast.makeText(requireContext(), "Settings saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void animateCardEntrance() {
        settingsCard.setAlpha(0f);
        settingsCard.setTranslationY(100f);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(settingsCard, "alpha", 0f, 1f);
        fadeIn.setDuration(800);

        ObjectAnimator moveUp = ObjectAnimator.ofFloat(settingsCard, "translationY", 100f, 0f);
        moveUp.setDuration(800);

        fadeIn.start();
        moveUp.start();
    }

    private void animateSaveButton() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(saveButton, "scaleX", 1f, 0.9f, 1f);
        scaleX.setDuration(300);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(saveButton, "scaleY", 1f, 0.9f, 1f);
        scaleY.setDuration(300);

        scaleX.start();
        scaleY.start();
    }
}