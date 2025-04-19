package com.example.taskmanagerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.database.TaskModel;

public class TaskDetailFragment extends Fragment {

    private static final String ARG_TASK = "task";

    private TaskModel task;

    public static TaskDetailFragment newInstance(TaskModel task) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            task = (TaskModel) getArguments().getSerializable(ARG_TASK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_details, container, false);

        TextView titleTextView = view.findViewById(R.id.task_title);
        TextView descriptionTextView = view.findViewById(R.id.task_description);
        TextView dateTextView = view.findViewById(R.id.task_date);
        TextView priorityTextView = view.findViewById(R.id.task_priority);

        if (task != null) {
            titleTextView.setText(task.getTitle());
            descriptionTextView.setText(task.getDescription());
            dateTextView.setText(task.getDate());
            priorityTextView.setText(getPriorityText(task.getPriority()));
        }

        return view;
    }

    private String getPriorityText(int priority) {
        switch (priority) {
            case 1:
                return "High";
            case 2:
                return "Medium";
            case 3:
                return "Low";
            default:
                return "Unknown";
        }
    }
}