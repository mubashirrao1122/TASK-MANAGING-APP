package com.example.taskmanagerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskmanagerapp.MainActivity;
import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.database.TaskModel;
import com.google.android.material.button.MaterialButton;

public class TaskDetailFragment extends Fragment {

    private static final String ARG_TASK = "task";

    private TaskModel task;
    private MaterialButton editButton;
    private MaterialButton deleteButton;

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

        // Initialize views
        TextView titleTextView = view.findViewById(R.id.task_title);
        TextView descriptionTextView = view.findViewById(R.id.task_description);
        TextView dateTextView = view.findViewById(R.id.task_date);
        TextView priorityTextView = view.findViewById(R.id.task_priority);
        editButton = view.findViewById(R.id.edit_task_button);
        deleteButton = view.findViewById(R.id.delete_task_button);

        // Set task data if available
        if (task != null) {
            titleTextView.setText(task.getTitle());
            descriptionTextView.setText(task.getDescription());
            dateTextView.setText(task.getDate());
            priorityTextView.setText(getPriorityText(task.getPriority()));
            
            // Setup edit button
            editButton.setOnClickListener(v -> {
                AddTaskFragment editFragment = new AddTaskFragment();
                Bundle args = new Bundle();
                args.putSerializable("task_to_edit", task);
                editFragment.setArguments(args);
                
                requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, editFragment)
                    .addToBackStack(null)
                    .commit();
            });
            
            // Setup delete button
            deleteButton.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).deleteTask(task.getId());
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            });
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