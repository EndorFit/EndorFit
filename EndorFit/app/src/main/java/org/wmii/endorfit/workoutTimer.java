package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.PrivateKey;

public class workoutTimer extends AppCompatActivity implements timerDialog.timerDialogListener {
private TextView timeSet;
private Button openTimer;
public static final String TIMER_VALUE ="org.wmii.endorfit.TIMER_VALUE";
String timerValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);
        timeSet=(TextView) findViewById(R.id.editTimer);
        openTimer=findViewById(R.id.buttonTimer);
        openDialog();
        openTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opentimerWindow();
            }
        });

    }
    public void opentimerWindow() {
        Intent intent =new Intent(this,timerWindow.class);
        intent.putExtra(TIMER_VALUE,timerValue);
        startActivity(intent);
    }
    public void openDialog()
    {
timerDialog timerdialog=new timerDialog();
    timerdialog.show(getSupportFragmentManager(),"Timer set");
    }

    @Override
    public void aplytext(String seconds) {
timerValue=seconds;
    }

}
