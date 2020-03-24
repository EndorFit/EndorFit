package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.security.PrivateKey;

public class workoutTimer extends AppCompatActivity implements timerDialog.timerDialogListener {
private TextView timeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);
        timeSet=(TextView) findViewById(R.id.editTimer);
        openDialog();
    }
    public void openDialog()
    {
timerDialog timerdialog=new timerDialog();
    timerdialog.show(getSupportFragmentManager(),"Timer set");
    }

    @Override
    public void aplytext(String seconds) {
timeSet.setText("czas odliczany  "+seconds);
    }
}
