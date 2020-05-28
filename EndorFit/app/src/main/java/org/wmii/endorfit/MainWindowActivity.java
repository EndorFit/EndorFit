package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainWindowActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageViewLeft, imageViewCenter, imageViewProfile;
    private Button buttonToExercisesList;
    Button startRun;
    FirebaseAuth mAuth;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        initWidgets();
        findViewById(R.id.imageViewRightIcon).setOnClickListener(this);
        findViewById(R.id.imageViewCenterIcon).setOnClickListener(this);
        findViewById(R.id.imageViewLeftIcon).setOnClickListener(this);
        setOnClickListener();
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Toast.makeText(this, "User is not sign in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainWindowActivity.this, MainActivity.class);
            startActivity(intent);
        }


        preparePreDefineExercise();

    }

    private void preparePreDefineExercise() {
        ArrayList<Exercise> predefExercise = new ArrayList<>();
        predefExercise.add(new Exercise("Bench press","Exercise with weights"));
        predefExercise.add(new Exercise("Single arm tricep pushdown","Exercise with weights"));
        predefExercise.add(new Exercise("Romanian deadlift","Exercise with weights"));
        predefExercise.add(new Exercise("Overhead Press","Exercise with weights"));
        predefExercise.add(new Exercise("Military Press","Exercise with weights"));
        predefExercise.add(new Exercise("Inner thigh","Exercise with weights"));
        predefExercise.add(new Exercise("Deadlift","Exercise with weights"));
        predefExercise.add(new Exercise("Lunges","Exercise without weights"));

        predefExercise.add(new Exercise("Dips","Exercise without weights"));
        predefExercise.add(new Exercise("Crunches","Exercise without weights"));
        predefExercise.add(new Exercise("Pull Up","Exercise without weights"));
        predefExercise.add(new Exercise("Barbell hip thrust","Exercise without weights"));
        predefExercise.add(new Exercise("Squat","Exercise without weights"));

        predefExercise.add(new Exercise("Walking","Running"));
        predefExercise.add(new Exercise("Cycling","Running"));
        predefExercise.add(new Exercise("Running","Running"));
        predefExercise.add(new Exercise("Roller skating","Running"));

        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        for(Exercise exercise : predefExercise)
        {
            DatabaseReference preExe = database.getReference("users/" + user.getUid() + "/exercises/" + exercise.getName());
            preExe.setValue(exercise);
        }
    }

    public void initWidgets()
    {
        imageViewProfile = findViewById(R.id.imageViewRightIcon);
        imageViewCenter = findViewById(R.id.imageViewCenterIcon);
        imageViewLeft = findViewById(R.id.imageViewLeftIcon);
        buttonToExercisesList = (Button) findViewById(R.id.buttonToExercises);
        startRun = (Button) findViewById(R.id.buttonStartRunningMode);
    }
    public void setOnClickListener()
    {
        buttonToExercisesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWindowActivity.this, ExercisesListActivity.class);
                // intent.putExtra(myDb);
                startActivity(intent);
            }
        });

        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RunningModeMap.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageViewRightIcon:
                Intent intentProfile = new Intent(MainWindowActivity.this,ProfileActivity.class);
                intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentProfile);
                break;
            case R.id.imageViewCenterIcon:
                Intent intentwork = new Intent(MainWindowActivity.this,workoutPlan.class);
                intentwork.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentwork);
                break;
            case R.id.imageViewLeftIcon:
                Intent intentPlan = new Intent(MainWindowActivity.this, PlanActivity.class);
                intentPlan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPlan);
                break;
        }

    }
}
