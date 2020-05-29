package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

public class CalendarDateChoosingActivity extends AppCompatActivity {
    private Button buttonDoneWithDateSetting;
    private CalendarView calendarView;
    private int fromCalendarYear;
    private int fromCalendarMonth;
    private int fromCalendarDay;
    private int interval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        setOnDateChangeListener();
        setNotificationScheduler(getApplicationContext());
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
                fromCalendarDay = dayOfMonth;
                fromCalendarMonth = month;
                fromCalendarYear = year;
            }
        });
    }
    public void setOnClickListener()
    {
        buttonDoneWithDateSetting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarDateChoosingActivity.this, SetIntervalForTrainingActivity.class);
                Toast.makeText(v.getContext(),"Date set: " + fromCalendarDay + "." + fromCalendarMonth + "." + fromCalendarYear,Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }


    public int getFromCalendarYear() {
        return fromCalendarYear;
    }

    public int getFromCalendarMonth() {
        return fromCalendarMonth;
    }

    public int getFromCalendarDay() {
        return fromCalendarDay;
    }
}
