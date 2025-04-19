package com.example.taskmanagerapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taskmanagerapp.database.TaskDatabaseHelper;
import com.example.taskmanagerapp.database.TaskModel;
import com.example.taskmanagerapp.fragments.AddTaskFragment;
import com.example.taskmanagerapp.fragments.HomeFragment;
import com.example.taskmanagerapp.fragments.SettingsFragment;
import com.example.taskmanagerapp.fragments.TaskDetailFragment;
import com.example.taskmanagerapp.fragments.TaskListFragment;
import com.example.taskmanagerapp.interfaces.OnTaskClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements AddTaskFragment.OnTaskSavedListener, OnTaskClickListener {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;

    private BottomNavigationView bottomNavigationView;
    private TaskDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database helper
        databaseHelper = new TaskDatabaseHelper(this);

        // Request permissions
        requestPermissions();

        // Set up the BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment(); // Displays motivational quotes
            } else if (item.getItemId() == R.id.nav_tasks) {
                selectedFragment = new TaskListFragment(); // Displays the task list
            } else if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new SettingsFragment(); // Displays notification settings
            }

            return loadFragment(selectedFragment);
        });

        // Load the Task List Fragment by default
        loadFragment(new TaskListFragment());
    }

    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
                break;
            }
        }

        // Request notification permission for Android 13 or higher
        requestNotificationPermission();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

    // Add a new task
    public void addTask(String title, String description, String date, int priority) {
        long result = databaseHelper.addTask(title, description, date, priority);
        if (result != -1) {
            Toast.makeText(this, "Task added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add task.", Toast.LENGTH_SHORT).show();
        }
    }

    // Update an existing task
    public void updateTask(int id, String title, String description, String date, int priority) {
        int rowsAffected = databaseHelper.updateTask(id, title, description, date, priority);
        if (rowsAffected > 0) {
            Toast.makeText(this, "Task updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update task.", Toast.LENGTH_SHORT).show();
        }
    }

    // Delete a task
    public void deleteTask(int id) {
        int rowsDeleted = databaseHelper.deleteTask(id);
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Task deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete task.", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle task save from AddTaskFragment
    @Override
    public void onTaskSaved(TaskModel task) {
        if (task.getId() == 0) {
            // Add new task
            addTask(task.getTitle(), task.getDescription(), task.getDate(), task.getPriority());
        } else {
            // Update existing task
            updateTask(task.getId(), task.getTitle(), task.getDescription(), task.getDate(), task.getPriority());
        }

        // Reload the Task List Fragment
        loadFragment(new TaskListFragment());
    }

    // Handle task click from TaskListFragment
    @Override
    public void onTaskClick(TaskModel task) {
        TaskDetailFragment detailFragment = TaskDetailFragment.newInstance(task);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

}