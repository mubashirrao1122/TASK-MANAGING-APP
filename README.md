# Task Manager App

A comprehensive Android application for managing tasks with an elegant, animated UI, motivational quotes, and customizable settings.

## Overview

Task Manager App is a robust task management solution designed to help users stay organized. The app features a modern, visually appealing interface with smooth animations, intuitive task creation, and a motivational home screen with inspirational quotes.

## Features

### Task Management
- **Create Tasks**: Add new tasks with title, description, due date, and priority level
- **Edit Tasks**: Update task details at any time
- **Delete Tasks**: Remove tasks that are no longer needed
- **View Task Details**: See complete information about any task
- **Priority Levels**: Categorize tasks as High, Medium, or Low priority
- **Due Dates**: Set and organize tasks by date

### Task Organization
- **Filter Tasks**: 
  - View all tasks
  - View only today's tasks
  - View only high-priority tasks
- **Sort Options**: 
  - Sort by date
  - Sort by priority

### Home Screen
- **Motivational Quotes**: Daily inspirational quotes to keep you motivated
- **Quote Sharing**: Long-press on any quote to share it with others
- **Refresh Quotes**: Get a new quote with a tap of the refresh button

### Settings
- **Notification Controls**: Enable or disable task reminders
- **Sort Preferences**: Choose default sort method for tasks
- **Persistent Settings**: Your preferences are saved between sessions

## Technical Features

### UI/UX
- **Material Design**: Modern UI with Material components
- **Smooth Animations**: 
  - Card entrance animations
  - Text fade-in effects
  - Count number animations
  - Button interactions
- **Intuitive Navigation**: Bottom navigation for easy switching between screens
- **Responsive Layouts**: Adapts to different screen sizes
- **Visual Feedback**: Clear indications for user actions

### Architecture
- **Fragment-Based Navigation**: Modular screen components
- **SQLite Database**: Local storage for task persistence
- **Retrofit Integration**: API client for fetching motivational quotes
- **Shared Preferences**: Storing user settings
- **Handler-Based Animations**: Smooth, non-blocking UI transitions

### Backend Features
- **Date Handling**: Proper formatting and validation for due dates
- **Data Validation**: Input checking for required fields
- **Error Handling**: Graceful handling of network failures
- **Permission Management**: Runtime permission handling for notifications

## App Structure

### Fragments
- **HomeFragment**: Displays motivational quotes with refresh and share options
- **TaskListFragment**: Shows list of tasks with filtering and sorting capabilities
- **AddTaskFragment**: Form for creating and editing tasks
- **TaskDetailFragment**: Shows comprehensive task information with edit/delete options
- **SettingsFragment**: Interface for configuring app preferences

### Database
- **TaskDatabaseHelper**: SQLite database interaction for CRUD operations
- **TaskModel**: Data model representing task objects

### Adapters
- **TaskAdapter**: RecyclerView adapter for displaying tasks in a list

### Network
- **ApiClient**: Retrofit setup for network requests
- **ApiService**: API endpoints definition
- **QuoteResponse**: Model for quote data from API

## Getting Started

### Prerequisites
- Android Studio (latest version recommended)
- Minimum SDK: API 24 (Android 7.0 Nougat)
- Target SDK: API 35

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle dependencies
4. Build and run the application on your device or emulator

## Usage Examples

### Creating a Task
1. Navigate to the Tasks tab
2. Tap the floating action button (+ icon)
3. Fill in task details (title, description, date, priority)
4. Tap "Save Task"

### Editing a Task
1. Tap on a task in the list
2. Tap the "Edit" button in the task details screen
3. Update task information
4. Tap "Update Task"

### Deleting a Task
1. Tap on a task in the list
2. Tap the "Delete" button in the task details screen

### Changing App Settings
1. Navigate to the Settings tab
2. Toggle notifications on/off
3. Select your preferred sort option
4. Tap "Save Settings"

### Getting New Quotes
1. Navigate to the Home tab
2. Tap the refresh button for a new quote
3. Long-press on any quote to share it

## Permissions
- **Internet**: For fetching motivational quotes
- **Storage**: For accessing and saving data
- **Notifications**: For task reminders

## Dependencies
- AndroidX AppCompat
- Material Components
- RecyclerView
- CardView
- ConstraintLayout
- Retrofit2
- GSON
- Handler & Looper

## Future Enhancements
- Task categories/tags
- Dark theme support
- Cloud synchronization
- Recurring tasks
- Task completion tracking and statistics
- Calendar view
- Task sharing

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgements
- Quotes provided by ZenQuotes API
- Material Design by Google

---

*Task Manager App - Stay organized, motivated, and productive!*
