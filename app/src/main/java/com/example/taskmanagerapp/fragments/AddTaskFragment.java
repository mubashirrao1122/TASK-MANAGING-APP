package com.example.taskmanagerapp.fragments;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.database.TaskModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskFragment extends Fragment {

    public interface OnTaskSavedListener {
        void onTaskSaved(TaskModel task);
    }

    private OnTaskSavedListener listener;
    private TextInputEditText titleEditText, descriptionEditText, dateEditText;
    private RadioGroup priorityRadioGroup;
    private MaterialButton saveButton;
    private TaskModel taskToEdit; // Store the task to edit
    private boolean isEditMode = false;

    // Add a no-argument constructor
    public AddTaskFragment() {
        // Required empty public constructor
    }

    // Keep the existing constructor for direct use
    public AddTaskFragment(OnTaskSavedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Try to get the listener from the context if not already set
        if (listener == null && context instanceof OnTaskSavedListener) {
            listener = (OnTaskSavedListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if we're editing an existing task
        if (getArguments() != null && getArguments().containsKey("task_to_edit")) {
            taskToEdit = (TaskModel) getArguments().getSerializable("task_to_edit");
            isEditMode = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        // Initialize views
        titleEditText = view.findViewById(R.id.task_title);
        descriptionEditText = view.findViewById(R.id.task_description);
        dateEditText = view.findViewById(R.id.task_date);
        priorityRadioGroup = view.findViewById(R.id.priority_radio_group);
        saveButton = view.findViewById(R.id.save_task_button);

        // Set up date picker
        setupDatePicker();

        // If we're in edit mode, populate the form
        if (isEditMode && taskToEdit != null) {
            populateFormWithTask(taskToEdit);
            
            // Update title text after the view is created
            TextView titleView = view.findViewById(R.id.title_text);
            if (titleView != null) {
                titleView.setText("Edit Task");
            }
        }

        // Animate card entrance
        animateCardEntrance(view);

        // Set up save button
        saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveTask();
                animateSaveButton();
            }
        });

        return view;
    }

    private void populateFormWithTask(TaskModel task) {
        titleEditText.setText(task.getTitle());
        descriptionEditText.setText(task.getDescription());
        dateEditText.setText(task.getDate());
    
        // Set the correct priority radio button
        switch (task.getPriority()) {
            case 1:
                priorityRadioGroup.check(R.id.radio_high);
                break;
            case 2:
                priorityRadioGroup.check(R.id.radio_medium);
                break;
            case 3:
                priorityRadioGroup.check(R.id.radio_low);
                break;
        }
    
        // Update title and button in onCreateView instead
        saveButton.setText("Update Task");
    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(calendar);
        };

        // Show date picker when clicking on the date field
        dateEditText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            // Set minimum date to today
            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.show();
        });

        // Show date picker when clicking on the end icon
        TextInputLayout dateInputLayout = (TextInputLayout) dateEditText.getParent().getParent();
        dateInputLayout.setEndIconOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            // Set minimum date to today
            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.show();
        });

        // Set today's date as default if not in edit mode
        if (!isEditMode) {
            updateLabel(calendar);
        }
    }

    private void updateLabel(Calendar calendar) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        dateEditText.setText(sdf.format(calendar.getTime()));
    }

    private boolean validateInputs() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            titleEditText.setError("Title is required");
            titleEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError("Description is required");
            descriptionEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(date)) {
            dateEditText.setError("Date is required");
            dateEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void saveTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();

        // Get priority from radio group
        int priority;
        int selectedId = priorityRadioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radio_high) {
            priority = 1; // High priority
        } else if (selectedId == R.id.radio_medium) {
            priority = 2; // Medium priority
        } else {
            priority = 3; // Low priority (default)
        }

        // Create or update the TaskModel
        TaskModel task;
        if (isEditMode && taskToEdit != null) {
            task = new TaskModel(taskToEdit.getId(), title, description, date, priority);
        } else {
            task = new TaskModel(0, title, description, date, priority);
        }

        if (listener != null) {
            listener.onTaskSaved(task);
        } else {
            Toast.makeText(requireContext(), "Error: Listener not attached", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(requireContext(), 
                isEditMode ? "Task updated successfully" : "Task saved successfully", 
                Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void animateSaveButton() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(saveButton, "scaleX", 1f, 0.9f, 1f);
        scaleX.setDuration(300);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(saveButton, "scaleY", 1f, 0.9f, 1f);
        scaleY.setDuration(300);

        scaleX.start();
        scaleY.start();
    }

    private void animateCardEntrance(View view) {
        // Try to find the CardView safely
        CardView cardView = null;

        // First try direct lookup - if you've set an ID in your layout
        cardView = view.findViewById(R.id.add_task_card);

        // If direct lookup fails, try to find through parent hierarchy
        if (cardView == null) {
            try {
                // Get the first CardView parent of the save button
                View buttonView = view.findViewById(R.id.save_task_button);
                if (buttonView != null) {
                    ViewParent parent = buttonView.getParent();
                    while (parent != null && !(parent instanceof CardView)) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof CardView) {
                        cardView = (CardView) parent;
                    }
                }
            } catch (Exception e) {
                // Fallback - just animate the root view
                cardView = null;
            }
        }

        // If we couldn't find the CardView, animate the whole view
        final View viewToAnimate = (cardView != null) ? cardView : view;

        viewToAnimate.setAlpha(0f);
        viewToAnimate.setTranslationY(100f);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0f, 1f);
        fadeIn.setDuration(800);

        ObjectAnimator moveUp = ObjectAnimator.ofFloat(viewToAnimate, "translationY", 100f, 0f);
        moveUp.setDuration(800);

        fadeIn.start();
        moveUp.start();
    }
}