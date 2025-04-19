package com.example.taskmanagerapp.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.database.TaskModel;

public class AddTaskFragment extends Fragment {

    public interface OnTaskSavedListener {
        void onTaskSaved(TaskModel task);
    }

    private OnTaskSavedListener listener;

    public AddTaskFragment(OnTaskSavedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        EditText titleEditText = view.findViewById(R.id.task_title);
        EditText descriptionEditText = view.findViewById(R.id.task_description);
        EditText dateEditText = view.findViewById(R.id.task_date);
        EditText priorityEditText = view.findViewById(R.id.task_priority);
        Button saveButton = view.findViewById(R.id.save_task_button);

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();
            String priorityStr = priorityEditText.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(date) || TextUtils.isEmpty(priorityStr)) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int priority;
            try {
                priority = Integer.parseInt(priorityStr);
                if (priority < 1 || priority > 3) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Priority must be 1 (High), 2 (Medium), or 3 (Low)", Toast.LENGTH_SHORT).show();
                return;
            }

            TaskModel task = new TaskModel(0, title, description, date, priority);
            if (listener != null) {
                listener.onTaskSaved(task);
            }

            Toast.makeText(getContext(), "Task saved successfully", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}