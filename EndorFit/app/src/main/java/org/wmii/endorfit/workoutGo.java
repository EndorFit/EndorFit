package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class workoutGo extends AppCompatActivity {
    private GridLayout mleyout;
    Button addstuff;
    dynamicViews dnv;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_go);
        mleyout=(GridLayout)findViewById(R.id.leyout);
        addstuff=(Button)findViewById(R.id.addstuff);

        addstuff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                dnv=new dynamicViews(context);
                mleyout.addView(dnv.descriptionTextView(getApplicationContext()),4);
                mleyout.addView(dnv.repsEditText(getApplicationContext()),5);
                mleyout.addView(dnv.setsEditText(getApplicationContext()),6);
                mleyout.addView(dnv.weightEditText(getApplicationContext()),7);

            }

        });

    }
}
