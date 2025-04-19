package com.example.taskmanagerapp.services;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.taskmanagerapp.MainActivity;
import com.example.taskmanagerapp.database.TaskDatabaseHelper;
import com.example.taskmanagerapp.database.TaskModel;

public class BackgroundService extends Service {

    private static final String CHANNEL_ID = "TaskManagerChannel";
    private TaskDatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new TaskDatabaseHelper(this);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkForUpcomingTasks();
        return START_STICKY;
    }

    private void checkForUpcomingTasks() {
        List<TaskModel> tasks = databaseHelper.getAllTasks();
        Calendar now = Calendar.getInstance();
        Calendar taskTime = Calendar.getInstance();

        for (TaskModel task : tasks) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                taskTime.setTime(sdf.parse(task.getDate()));

                if (taskTime.getTimeInMillis() - now.getTimeInMillis() <= 30 * 60 * 1000) {
                    sendNotification("Upcoming Task", "Task: " + task.getTitle() + " is due soon!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification(String title, String message) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Task Manager Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cleanup resources if needed
    }
}