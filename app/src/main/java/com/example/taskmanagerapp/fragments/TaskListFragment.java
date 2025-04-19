package com.example.taskmanagerapp.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.adapters.TaskAdapter;
import com.example.taskmanagerapp.database.TaskDatabaseHelper;
import com.example.taskmanagerapp.database.TaskModel;
import com.example.taskmanagerapp.interfaces.OnTaskClickListener;
import com.example.taskmanagerapp.utils.SharedPreferencesHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskListFragment extends Fragment implements OnTaskClickListener {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private LinearLayout emptyStateView;
    private TaskDatabaseHelper databaseHelper;
    private List<TaskModel> allTasks = new ArrayList<>();
    private List<TaskModel> filteredTasks = new ArrayList<>();
    private TextView pendingCountView, highPriorityCountView, todayCountView;
    private ChipGroup filterChipGroup;
    private FloatingActionButton addTaskButton;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.task_recycler_view);
        emptyStateView = view.findViewById(R.id.empty_state);
        pendingCountView = view.findViewById(R.id.pending_count);
        highPriorityCountView = view.findViewById(R.id.high_priority_count);
        todayCountView = view.findViewById(R.id.today_count);
        filterChipGroup = view.findViewById(R.id.filter_chip_group);
        addTaskButton = view.findViewById(R.id.fab_add_task);

        // Initialize database and shared preferences
        databaseHelper = new TaskDatabaseHelper(requireContext());
        sharedPreferencesHelper = new SharedPreferencesHelper(requireContext());

        // Set up recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskAdapter = new TaskAdapter(filteredTasks, this);
        recyclerView.setAdapter(taskAdapter);

        // Set up add task button
        addTaskButton.setOnClickListener(v -> {
            // Navigate to add task fragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddTaskFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Set up chip filters
        setupFilterChips();

        // Add entrance animation
        animateEntrance(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }

    private void setupFilterChips() {
        Chip allChip = filterChipGroup.findViewById(R.id.chip_all);
        Chip todayChip = filterChipGroup.findViewById(R.id.chip_today);
        Chip highPriorityChip = filterChipGroup.findViewById(R.id.chip_high_priority);

        // Set up chip click listeners
        allChip.setOnClickListener(v -> filterTasks("all"));
        todayChip.setOnClickListener(v -> filterTasks("today"));
        highPriorityChip.setOnClickListener(v -> filterTasks("high"));
    }

    private void loadTasks() {
        // Load tasks from database
        allTasks = databaseHelper.getAllTasks();

        // Apply sorting based on user preference
        sortTasks();

        // Update summary counts
        updateTaskCounts();

        // Apply current filter
        int selectedChipId = filterChipGroup.getCheckedChipId();
        if (selectedChipId == R.id.chip_today) {
            filterTasks("today");
        } else if (selectedChipId == R.id.chip_high_priority) {
            filterTasks("high");
        } else {
            filterTasks("all");
        }
    }

    private void sortTasks() {
        int sortOption = sharedPreferencesHelper.getSortOption();

        if (sortOption == SharedPreferencesHelper.SORT_BY_PRIORITY) {
            // Sort by priority (high to low)
            allTasks.sort((task1, task2) -> task1.getPriority() - task2.getPriority());
        } else {
            // Default sort by date
            // Already sorted by date in the database query
        }
    }

    private void updateTaskCounts() {
        int pendingCount = allTasks.size();
        int highPriorityCount = 0;
        int todayCount = 0;

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        for (TaskModel task : allTasks) {
            if (task.getPriority() == 1) {
                highPriorityCount++;
            }

            if (task.getDate().startsWith(todayDate)) {
                todayCount++;
            }
        }

        // Update count views with animation
        animateCountChange(pendingCountView, pendingCount);
        animateCountChange(highPriorityCountView, highPriorityCount);
        animateCountChange(todayCountView, todayCount);
    }

    private void filterTasks(String filterType) {
        filteredTasks.clear();

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        switch (filterType) {
            case "today":
                for (TaskModel task : allTasks) {
                    if (task.getDate().startsWith(todayDate)) {
                        filteredTasks.add(task);
                    }
                }
                break;

            case "high":
                for (TaskModel task : allTasks) {
                    if (task.getPriority() == 1) {
                        filteredTasks.add(task);
                    }
                }
                break;

            case "all":
            default:
                filteredTasks.addAll(allTasks);
                break;
        }

        // Update adapter
        taskAdapter.notifyDataSetChanged();

        // Show empty state if needed
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (filteredTasks.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }

    private void animateCountChange(TextView textView, int newCount) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 1f, 1.2f, 1f);
        scaleX.setDuration(500);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 1f, 1.2f, 1f);
        scaleY.setDuration(500);

        textView.setText(String.valueOf(newCount));

        scaleX.start();
        scaleY.start();
    }

    private void animateEntrance(View view) {
        View summaryCard = view.findViewById(R.id.task_summary_card);
        ChipGroup chipGroup = view.findViewById(R.id.filter_chip_group);
        View tasksCard = view.findViewById(R.id.tasks_card);

        // Initial state - off screen
        summaryCard.setTranslationY(-100);
        summaryCard.setAlpha(0);

        chipGroup.setTranslationY(-50);
        chipGroup.setAlpha(0);

        tasksCard.setTranslationY(50);
        tasksCard.setAlpha(0);

        // Animate summary card
        summaryCard.animate()
                .translationY(0)
                .alpha(1)
                .setDuration(500)
                .setStartDelay(100);

        // Animate chip group
        chipGroup.animate()
                .translationY(0)
                .alpha(1)
                .setDuration(500)
                .setStartDelay(200);

        // Animate tasks card
        tasksCard.animate()
                .translationY(0)
                .alpha(1)
                .setDuration(500)
                .setStartDelay(300);
    }

    @Override
    public void onTaskClick(TaskModel task) {
        // Navigate to task detail fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);

        TaskDetailFragment detailFragment = new TaskDetailFragment();
        detailFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}