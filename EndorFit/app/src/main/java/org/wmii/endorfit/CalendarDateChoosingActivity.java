package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarDateChoosingActivity extends AppCompatActivity {
    private Spinner spinnerTrainingToDate;
    private Button buttonDoneWithDateSetting;
    private Button buttonDontWantToSetDate;
    private CalendarView calendarView;
    private int fromCalendarYear;
    private int fromCalendarMonth;
    private int fromCalendarDay;
    public static final String TAG = "CalendarDateChoosingAct";
    ViewPager viewPager;
    ArrayList<String> completedPlansNames;

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        getPlanNamesForSpinner();
        initWidgets();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,completedPlansNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrainingToDate.setAdapter(arrayAdapter);
        spinnerTrainingToDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(getString(R.string.SpinnerTrainingToDate))
                ) {
                }else {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Selected: " + item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setOnDateChangeListener();
        //setNotificationScheduler(getApplicationContext());
        setOnClickListener();
        //Toast.makeText(this, Calendar.DAY_OF_MONTH Calendar.MONTH Calendar.YEAR;)

    }
    public void initWidgets()
    {
        calendarView = (CalendarView) findViewById(R.id.choosingCalendar);
        buttonDoneWithDateSetting = (Button) findViewById(R.id.buttonDoneWithDateSetting);
        buttonDontWantToSetDate = (Button) findViewById(R.id.buttonDontWantToDateSetting);
        spinnerTrainingToDate = (Spinner)findViewById(R.id.spinner2);
        //TODO training list

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
        buttonDontWantToSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarDateChoosingActivity.this, MainWindowActivity.class);
                startActivity(intent);
            }
        });
    }



    private void getPlanNamesForSpinner() {
        viewPager = findViewById(R.id.viewPagerListy);
        completedPlansNames = new ArrayList<>();
        final DatabaseReference completedPlansRef = database.getReference("users/" + user.getUid() + "/plans/");
        completedPlansNames.add(0,getString(R.string.SpinnerTrainingToDate));
        completedPlansRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot date : dataSnapshot.getChildren())
                {
                    completedPlansNames.add(date.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
