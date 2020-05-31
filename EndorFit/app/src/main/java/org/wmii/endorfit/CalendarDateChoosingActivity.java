package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarDateChoosingActivity extends AppCompatActivity {
    private Button buttonDoneWithDateSetting;
    private CalendarView calendarView;
    private int fromCalendarYear;
    private int fromCalendarMonth;
    private int fromCalendarDay;
    public static final String TAG = "CalendarDateChoosingAct";
    private int interval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        setOnDateChangeListener();
        //setNotificationScheduler(getApplicationContext());
        setOnClickListener();
        //Toast.makeText(this, Calendar.DAY_OF_MONTH Calendar.MONTH Calendar.YEAR;)
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
                SimpleDateFormat sdformat = new SimpleDateFormat("YYYY-MM-DD");
                Date dateGottenFromCalendar = null;
                Calendar comparisonCalendar = Calendar.getInstance();
                //comparisonCalendar.
                Date todaysDate = new Date();
                Date dateOnlyZeros = new Date();
                try {
                     dateGottenFromCalendar = sdformat.parse(fromCalendarYear + "-" + fromCalendarMonth + "-" + fromCalendarDay);
                    //todaysDate = sdformat.parse(todaysDate.getYear() + "-" + todaysDate.getMonth() + "-" + todaysDate.getDay());
                    dateOnlyZeros = sdformat.parse(0 + "-" + 0 + "-" + 0);

                }
                catch (ParseException e)
                {
                    Log.d(TAG,"setOnClickListener: parseException");
                }
                //Toast.makeText(v.getContext(), "Today's date: " +String.format(String.valueOf(todaysDate)),Toast.LENGTH_SHORT).show();
                if(dateGottenFromCalendar.equals(dateOnlyZeros))
                {
                    fromCalendarDay = comparisonCalendar.get(Calendar.DAY_OF_MONTH);
                    fromCalendarMonth = comparisonCalendar.get(Calendar.MONTH);
                    fromCalendarYear = comparisonCalendar.get(Calendar.YEAR);
                }
//                if(dateGottenFromCalendar.compareTo(todaysDate) < 0)
//                {
//                    Toast.makeText(v.getContext(),getString(R.string.ToastDateFromPast),Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Intent intent = new Intent(CalendarDateChoosingActivity.this, SetIntervalForTrainingActivity.class);
                int[]yearMonthDay = {fromCalendarYear,fromCalendarMonth,fromCalendarDay};
                //TODO date < current date
                intent.putExtra("yearMonthDay",yearMonthDay);
                Toast.makeText(v.getContext(),getString(R.string.ToastDateSet) + " " + fromCalendarDay + "." + (fromCalendarMonth+1) + "." + fromCalendarYear,Toast.LENGTH_SHORT).show();
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
