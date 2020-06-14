package org.wmii.endorfit.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.wmii.endorfit.Activities.CalendarDateChoosingActivity;
import org.wmii.endorfit.DataClasses.Exercise;
import org.wmii.endorfit.R;
import org.wmii.endorfit.DataClasses.workoutPlan;

import java.util.ArrayList;

public class MainWindowActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageViewLeft, imageViewCenter, imageViewProfile;
    private ImageButton buttonToExercisesList;
    ImageButton imageButtonStartRun;
    ImageButton imageButtonCompletedPlans;
    private ImageButton imageButtonToDateSetting;
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
        if (currentUser == null) {
            Toast.makeText(this, "User is not sign in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainWindowActivity.this, MainActivity.class);
            startActivity(intent);
        }

        preparePreDefineExercise();
    }

    private void preparePreDefineExercise() {
        Cursor cursor = MainActivity.myDb.getDataForFirebase();
        ArrayList<Exercise> predefExercise = new ArrayList<>();
        while (cursor.moveToNext()) {
            predefExercise.add(new Exercise(cursor.getString(1), cursor.getString(2)));
        }
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        for (Exercise exercise : predefExercise) {
            DatabaseReference preExe = database.getReference("users/" + user.getUid() + "/exercises/" + exercise.getName());
            preExe.setValue(exercise);
        }
    }

    public void initWidgets() {
        imageViewProfile = findViewById(R.id.imageViewRightIcon);
        imageViewCenter = findViewById(R.id.imageViewCenterIcon);
        imageViewLeft = findViewById(R.id.imageViewLeftIcon);
        buttonToExercisesList = (ImageButton) findViewById(R.id.buttonToExercises);
        imageButtonStartRun = (ImageButton) findViewById(R.id.buttonStartRunningMode);
        imageButtonCompletedPlans = findViewById(R.id.buttonCompletedPlans);
        imageButtonToDateSetting = (ImageButton) findViewById(R.id.buttonSetTrainingDate);
    }

    public void setOnClickListener() {
        buttonToExercisesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWork = new Intent(MainWindowActivity.this, workoutPlan.class);
                intentWork.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentWork);
            }
        });

        imageButtonStartRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RunningModeActivity.class);
                startActivity(intent);
            }
        });

        imageButtonCompletedPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompletedPlansActivity.class);
                startActivity(intent);
            }
        });
        imageButtonToDateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWindowActivity.this, CalendarDateChoosingActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewRightIcon:
                Intent intentProfile = new Intent(MainWindowActivity.this, ProfileActivity.class);
                intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentProfile);
                break;
            case R.id.imageViewLeftIcon:
                Intent intentPlan = new Intent(MainWindowActivity.this, PlanActivity.class);
                intentPlan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPlan);
                break;
        }

    }
}
