package org.wmii.endorfit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class timerWindowActivity extends AppCompatActivity {
    private Button buttonStartTimer;
    private TextView textViewCountdown;
    private CountDownTimer countDownTimer;
    private long timeLeftInMiliseconds;
    private long TValue;
    private boolean isItRunning;
    private ProgressBar progressBar;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_window);
        textViewCountdown = findViewById(R.id.countdownText);
        buttonStartTimer = findViewById(R.id.buttonStart);
        progressBar=findViewById(R.id.progressTimer);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Intent intent=getIntent();
        String TimerValue=intent.getStringExtra(workoutTimer.TIMER_VALUE);
         TValue=Long.parseLong(TimerValue);
        progressBar.setMax((int)TValue*1000);
        timeLeftInMiliseconds=TValue*1000;
        textViewCountdown.setText(""+TValue+":00");
        buttonStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });
        startStop();
    }

    public void startStop() {
        if (isItRunning) {
            stopTimer();
        } else {
            startTime();
        }

    }

    public void stopTimer() {
        countDownTimer.cancel();
        isItRunning=false;
        buttonStartTimer.setText("START");
    }

    public void startTime() {
        countDownTimer = new CountDownTimer(timeLeftInMiliseconds,1) {
            @Override
            public void onTick(long l) {
            timeLeftInMiliseconds=l;
            UpdateTimer();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFinish() {
                textViewCountdown.setText("00:00");
                progressBar.setProgress((int)TValue*1000);
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                finish();
            }

        }.start();
        buttonStartTimer.setText("STOP");
        isItRunning=true;
    }
    public  void UpdateTimer()
    {
        int seconds =(int) timeLeftInMiliseconds/1000;
        int milis =(int) timeLeftInMiliseconds/10%100;

        String TimeLefttext;
        if(seconds<10){TimeLefttext="0"+seconds;}
        else {
            TimeLefttext ="" +seconds;
        }
        TimeLefttext+=":";
        if(milis<10){TimeLefttext+="0";}

        TimeLefttext+=milis;
        textViewCountdown.setText(TimeLefttext);
        progressBar.setProgress((int)((TValue*1000)-timeLeftInMiliseconds));
    }
}
