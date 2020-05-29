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

public class MainWindowActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageViewLeft, imageViewCenter, imageViewProfile;
    private Button buttonToExercisesList;
    private Button buttonToDateSetting;
    FirebaseAuth mAuth;

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
    }
    public void initWidgets()
    {
        imageViewProfile = findViewById(R.id.imageViewRightIcon);
        imageViewCenter = findViewById(R.id.imageViewCenterIcon);
        imageViewLeft = findViewById(R.id.imageViewLeftIcon);
        buttonToExercisesList = (Button) findViewById(R.id.buttonToExercises);
        buttonToDateSetting = (Button) findViewById(R.id.buttonSetTrainingDate);
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
        buttonToDateSetting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWindowActivity.this, CalendarDateChoosingActivity.class);
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
