package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class workoutPlan extends AppCompatActivity implements View.OnClickListener {

    private Button button,starter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);
        starter=(Button) findViewById(R.id.buttonStart);
        button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                openActivityWorking();

            }
        }       );
        starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityTimer();
            }
        });
    }

    public void openActivityWorking() {
        Intent intent =new Intent(this,workoutGo.class);
        startActivity(intent);
    }

    public void openActivityTimer()
    {
        Intent intent =new Intent(this,workoutTimer.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {

    }
}

