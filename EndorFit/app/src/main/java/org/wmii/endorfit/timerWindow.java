package org.wmii.endorfit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class timerWindow extends AppCompatActivity {
    private Button startTimer;
    private TextView countdownText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMiliseconds;
    private long TValue;
    private boolean isItRunning;
    private ProgressBar progressBar;
    Vibrator v;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_window);
        countdownText = findViewById(R.id.countdownText);
        startTimer = findViewById(R.id.buttonStart);
        progressBar=findViewById(R.id.progressTimer);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Intent intent=getIntent();
        String TimerValue=intent.getStringExtra(workoutTimer.TIMER_VALUE);
         TValue=Long.parseLong(TimerValue);
        progressBar.setMax((int)TValue*1000);
        timeLeftInMiliseconds=TValue*1000;
        countdownText.setText(""+TValue+":00");
        startTimer.setOnClickListener(new View.OnClickListener() {
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
        startTimer.setText("START");
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
                countdownText.setText("00:00");
                progressBar.setProgress((int)TValue*1000);
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                finish();
            }


        }.start();
        startTimer.setText("STOP");
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
countdownText.setText(TimeLefttext);
        progressBar.setProgress((int)((TValue*1000)-timeLeftInMiliseconds));
    }
}
