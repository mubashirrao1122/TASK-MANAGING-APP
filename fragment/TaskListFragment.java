package com.example.taskmanagerapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.adapters.TaskAdapter;
import com.example.taskmanagerapp.database.TaskDatabaseHelper;
import com.example.taskmanagerapp.database.TaskModel;

import java.util.ArrayList;

public class TaskListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskModel> taskList;
    private TaskDatabaseHelper databaseHelper;
    private TextView noTasksTextView;

    public TaskListFragment() {
        // Required empty public constructor
    }
    public interface OnTaskClickListener {
        void onTaskClick(com.example.taskmanagerapp.database.TaskModel task);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_tasks);
        noTasksTextView = view.findViewById(R.id.tv_no_tasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseHelper = new TaskDatabaseHelper(getActivity());
        loadTasks();

        return view;
    }

    private void loadTasks() {
        taskList = new ArrayList<>(databaseHelper.getAllTasks());

        if (taskList.isEmpty()) {
            noTasksTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noTasksTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            taskAdapter = new TaskAdapter(taskList, task -> {
                // Navigate to TaskDetailFragment
                TaskDetailFragment detailFragment = TaskDetailFragment.newInstance(task);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            });
            recyclerView.setAdapter(taskAdapter);
        }
    }
}