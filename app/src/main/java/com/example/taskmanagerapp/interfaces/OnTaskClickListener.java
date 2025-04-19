package com.example.taskmanagerapp.interfaces;

import com.example.taskmanagerapp.database.TaskModel;

public interface OnTaskClickListener {
    void onTaskClick(TaskModel task);
}