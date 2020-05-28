package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.text.DynamicLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class workoutGo extends AppCompatActivity {
    private GridLayout mleyout;
    Button addstuff, saveWorkout;
    dynamicViews dnv;
    Context context;
    Exercise[] tab;
    TextView name;
    DatabaseReference exerciseRef;
    FirebaseDatabase database;
    FirebaseUser users;
    Spinner spinnerType;
    View view;
    ArrayList<EditText> allExer = new ArrayList<EditText>();

    int counter = 0;

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
                counter += 4;

            }

        });


        spinnerType = findViewById(R.id.spinnerType);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.exercisesType, R.layout.spinner_item);
        //spinnerType.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        users = FirebaseAuth.getInstance().getCurrentUser();

        saveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  tab= new Exercise[counter/4];
                if (allExer.get(0) == null) {
                    name.setText("nope");
                } else {
                    name.setText(allExer.get(0).getText() );

                }


                //todo delete this file ?


            }
        });


    }

    public void addView(View view) {
        this.view = view;
        dnv = new dynamicViews(context);


        mleyout.addView(allExer.get(counter), counter);
        mleyout.addView(allExer.get(counter+1), counter + 1);
        mleyout.addView(allExer.get(counter+2), counter + 2);
        mleyout.addView(allExer.get(counter+3), counter + 3);

    }
}