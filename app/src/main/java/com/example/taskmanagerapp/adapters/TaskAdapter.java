package com.example.taskmanagerapp.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.database.TaskModel;
import com.example.taskmanagerapp.interfaces.OnTaskClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskModel> taskList;
    private OnTaskClickListener listener;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

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

        // Set title
        holder.titleTextView.setText(task.getTitle());

        // Format and set date
        try {
            Date date = inputFormat.parse(task.getDate());
            holder.dateTextView.setText(outputFormat.format(date));
        } catch (ParseException e) {
            holder.dateTextView.setText(task.getDate());
        }

        // Set priority indicator and text
        View priorityIndicator = holder.itemView.findViewById(R.id.priority_indicator);
        GradientDrawable priorityBadge = (GradientDrawable) holder.priorityTextView.getBackground();

        int priorityColor;
        String priorityText;

        switch (task.getPriority()) {
            case 1:
                priorityColor = holder.itemView.getContext().getResources().getColor(R.color.taskHighPriority);
                priorityText = "HIGH";
                break;
            case 2:
                priorityColor = holder.itemView.getContext().getResources().getColor(R.color.taskMediumPriority);
                priorityText = "MEDIUM";
                break;
            case 3:
            default:
                priorityColor = holder.itemView.getContext().getResources().getColor(R.color.taskLowPriority);
                priorityText = "LOW";
                break;
        }

        priorityIndicator.setBackgroundColor(priorityColor);
        priorityBadge.setColor(priorityColor);
        holder.priorityTextView.setText(priorityText);

        // Set click listener
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
}