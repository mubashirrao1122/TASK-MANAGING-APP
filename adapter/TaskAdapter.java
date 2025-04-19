package com.example.taskmanagerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.database.TaskModel;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskModel> taskList;
    private OnTaskClickListener listener;

    public TaskAdapter(List<TaskModel> taskList, OnTaskClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.dateTextView.setText(task.getDate());

        // Set priority indicator
        switch (task.getPriority()) {
            case 1:
                holder.priorityTextView.setText("High");
                holder.priorityTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.taskHighPriority));
                break;
            case 2:
                holder.priorityTextView.setText("Medium");
                holder.priorityTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.taskMediumPriority));
                break;
            case 3:
                holder.priorityTextView.setText("Low");
                holder.priorityTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.taskLowPriority));
                break;
        }

        holder.itemView.setOnClickListener(v -> listener.onTaskClick(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView priorityTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.task_title);
            dateTextView = itemView.findViewById(R.id.task_date);
            priorityTextView = itemView.findViewById(R.id.task_priority);
        }
    }

    public interface OnTaskClickListener {
        void onTaskClick(TaskModel task);
    }
}