package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import java.util.Calendar;

public class CalendarDateChoosing extends AppCompatActivity {
    private Button buttonDoneWithDateSetting;
    private CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        setOnClickListener();
    }
    public void initWidgets()
    {
        calendarView = (CalendarView) findViewById(R.id.choosingCalendar);
        buttonDoneWithDateSetting = (Button) findViewById(R.id.buttonDoneWithDateSetting);
    }
    public void setOnDateChangeListener()
    {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });
    }
    public void setOnClickListener()
    {
        buttonDoneWithDateSetting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarDateChoosing.this,SetIntervalForTraining.class);
                startActivity(intent);
            }
        });
    }
//    public static void setNotificationScheduler(Context context) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, NotificationPublisher.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 999999, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        // Set the alarm to start at 00:00
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
//    }
//    public class NotificationPublisher extends BroadcastReceiver {
//
//        public static String NOTIFICATION_ID = "notification-id";
//        public static String NOTIFICATION = "notification";
//
//        public void onReceive(Context context, Intent intent) {
//
//            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//            Notification notification = intent.getParcelableExtra(NOTIFICATION);
//            int id = intent.getIntExtra(NOTIFICATION_ID, 0);
//            notificationManager.notify(id, notification);
//
//        }
//    }
}
