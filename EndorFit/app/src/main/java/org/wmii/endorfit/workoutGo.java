package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class workoutGo extends AppCompatActivity {
    private GridLayout mleyout;
    Button addstuff, saveWorkout;
    dynamicViews dnv;
    Context context;
    Exercise[] tab;
    TextView name;
    Integer howManyExer = 0;
    DatabaseReference exerciseRef;
    FirebaseDatabase database;
    Spinner spinnerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_go);
        mleyout = (GridLayout) findViewById(R.id.leyout);
        addstuff = (Button) findViewById(R.id.addstuff);
        saveWorkout = (Button) findViewById(R.id.submitButton);
        name = (TextView) findViewById(R.id.nameWorkout);
        addstuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howManyExer = howManyExer + 1;
                dnv = new dynamicViews(context);
                mleyout.addView(dnv.descriptionTextView(getApplicationContext()), 4);
                mleyout.addView(dnv.repsEditText(getApplicationContext()), 5);
                mleyout.addView(dnv.setsEditText(getApplicationContext()), 6);
                mleyout.addView(dnv.weightEditText(getApplicationContext()), 7);

            }

        });

        spinnerType = findViewById(R.id.spinnerType);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.exercisesType, R.layout.spinner_item);
        spinnerType.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();

        saveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseRef = database.getReference("exercises/" + name.getText().toString());
                Exercise exercise = new Exercise(spinnerType.getSelectedItem().toString());
                exerciseRef.setValue(exercise);
            }
        });


    }
}