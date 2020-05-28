package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

public class Calendar extends AppCompatActivity {
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
                Intent intent = new Intent(Calendar.this,MainWindowActivity.class);
                startActivity(intent);
            }
        });
    }
}
