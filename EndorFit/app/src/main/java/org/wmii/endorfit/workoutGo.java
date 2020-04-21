package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class workoutGo extends AppCompatActivity {
    private GridLayout mleyout;
    Button addstuff, saveWorkout;
    dynamicViews dnv;
    Context context;
    Exercise[] tab;
    TextView name;
    DatabaseReference exerciseRef;
    FirebaseDatabase database;
    Spinner spinnerType;
    View view;
    List<dynamicViews> allViews = new ArrayList<dynamicViews>();

    int counter=4;

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

              addView(view);
                counter+=4;
            }

        });


        spinnerType = findViewById(R.id.spinnerType);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.exercisesType, R.layout.spinner_item_30dp);
        spinnerType.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();

        saveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Exercise exercise = new Exercise(spinnerType.getSelectedItem().toString());
                tab= new Exercise[allViews.size()];

                for(int i=0; i < allViews.size(); i++)
                {
               tab[i]=new Exercise(allViews.get(i).descriptionTextView(allViews.get(i).ctx).getText().toString());
               //This is not working and i dont know why
                    //TODO pull data from dynamicly created xml -> put this to table (table is workout aka table of exercises ) -> send it to database (master parent user->workout->exercise)

                }

                for (int i=0;i<allViews.size();i++)
                {
                    exerciseRef = database.getReference("exercises/" + tab[i].type);

                    exerciseRef.setValue(tab[i]);

                }

            }
        });


    }
    public void addView(View view)
    {
        this.view=view;
        dnv = new dynamicViews(context);
        mleyout.addView(dnv.descriptionTextView(getApplicationContext()), counter);
        mleyout.addView(dnv.repsEditText(getApplicationContext()), counter+1);
        mleyout.addView(dnv.setsEditText(getApplicationContext()), counter+2);
        mleyout.addView(dnv.weightEditText(getApplicationContext()), counter+3);
        allViews.add(dnv);

    }
}