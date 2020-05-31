package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SetIntervalForTrainingActivity extends AppCompatActivity {
    public static final String TAG = "SetIntervalForTrainAct";
    private TextView textViewIntervalSetting, textViewIntervalDays;
    private Button buttonSetIntervalDone, buttonNoInterval;
    private EditText editTextTypeInYourInterval;
    private RadioGroup radioGroupInterval;
    private Button buttonChecked;
    private int interval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_interval_for_training);
        initWidgets();
        setOnClickListener();
    }
    public void initWidgets()
    {
        buttonNoInterval = (Button)findViewById(R.id.buttonNoInterval);
        buttonSetIntervalDone = (Button)findViewById(R.id.buttonSetIntervalDone);
        textViewIntervalDays = (TextView) findViewById(R.id.textViewIntervalDays);
        textViewIntervalSetting = (TextView) findViewById(R.id.textViewIntervalSetting);
        editTextTypeInYourInterval = (EditText) findViewById(R.id.editTextTypeInYourInterval);
        radioGroupInterval = (RadioGroup) findViewById(R.id.radioGroupInterval);
    }
    public void checkButton(View v)
    {
        int radioId = radioGroupInterval.getCheckedRadioButtonId();

        buttonChecked = findViewById(radioId);
    }
    public void setOnClickListener()
    {
        buttonSetIntervalDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetIntervalForTrainingActivity.this,MainWindowActivity.class);

                checkButton(v);
                try {
                    int[]yearMonthDay = getIntent().getIntArrayExtra("yearMonthDay");
                    Log.d(TAG,"DoneOnClick: getArrayExtra, " + "year: " + yearMonthDay[0] + "month: " + yearMonthDay[1] + "day: " + yearMonthDay[2]);
                    if(buttonChecked.getId() == R.id.radioButtonOther)
                    {
                        if(Integer.parseInt(editTextTypeInYourInterval.getText().toString()) > 0 && Integer.parseInt(editTextTypeInYourInterval.getText().toString()) <= 60)
                        {
                            Toast.makeText(v.getContext(),getString(R.string.ToastIntervalChosenSuccesfully1) + " " + editTextTypeInYourInterval.getText().toString() + " " + getString(R.string.ToastIntervalChosenSuccesfully2),Toast.LENGTH_SHORT).show();
                            interval = Integer.parseInt(editTextTypeInYourInterval.getText().toString());
                        }
                        else
                        {
                            Toast.makeText(v.getContext(),getString(R.string.ToastValueIncorrect),Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                    else
                    {
                        Toast.makeText(v.getContext(),getString(R.string.ToastIntervalChosenSuccesfully1) + " " + buttonChecked.getText().charAt(0) + " " + getString(R.string.ToastIntervalChosenSuccesfully2),Toast.LENGTH_SHORT).show();

                        interval = buttonChecked.getText().charAt(0);

                    }
                    setNotificationScheduler(getApplicationContext(),yearMonthDay[0],yearMonthDay[1],yearMonthDay[2], 0);
                    startActivity(intent);
                }
                catch (NullPointerException e)
                {
                    Log.d(TAG, "setOnclickListener: OnClick: " + e);
                    Toast.makeText(v.getContext(), getString(R.string.ToastNoIntervalChosen), Toast.LENGTH_SHORT).show();
                }
                catch (NumberFormatException e)
                {
                    Log.d(TAG, "setOnclickListener: OnClick: " + e);
                    Toast.makeText(v.getContext(),getString(R.string.ToastValueIncorrect),Toast.LENGTH_SHORT).show();
                }


            }
        });
        buttonNoInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[]yearMonthDay = getIntent().getIntArrayExtra("yearMonthDay");
                Intent intent = new Intent(SetIntervalForTrainingActivity.this,MainWindowActivity.class);
                Toast.makeText(v.getContext(),getString(R.string.ToastNoIntervalChosen),Toast.LENGTH_SHORT).show();
                setNotificationScheduler(getApplicationContext(),yearMonthDay[0],yearMonthDay[1],yearMonthDay[2], 0);
                startActivity(intent);
            }
        });
    }
    public void setNotificationScheduler(Context context, int year, int month, int day, int interval) {

        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(year, month,day,6,0);
        if(interval == 0)
        {
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),  alarmIntent);
        }
        else
        {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY*interval, alarmIntent);
        }

    }
//    public void setNotificationSchedulerWithoutRepeating(Context context, int year, int month, int day, int interval) {
//
//        Intent intent = new Intent(context, NotificationPublisher.class);
//        intent.setAction("MY_NOTIFICATION_MESSAGE");
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(year, month,day,6,0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),  alarmIntent);
//        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY*interval, alarmIntent);
//    }

}
