package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddNewExerciseActivity extends AppCompatActivity {
    ProgressBar progressBar;

    Spinner spinnerExerciseType;
    SpinnerAdapter adapter;

    Button buttonCreateNewPlan, buttonSeeYourPlans, buttonAddExercise;

    ImageView imageViewLeftIcon, imageViewCenterIcon, imageViewRightIcon;

    EditText editTextExerciseName;

    FirebaseDatabase database;
    DatabaseReference exerciseRef;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_exercise);
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(AddNewExerciseActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        user = mAuth.getCurrentUser();

        initializeObjects();

        setListeners();
    }

    private void setListeners() {
        buttonAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextExerciseName.getText().toString();
                String type = spinnerExerciseType.getSelectedItem().toString();
                if(name.isEmpty())
                {
                    editTextExerciseName.setError("Name is required");
                    editTextExerciseName.requestFocus();
                    return;
                }
                //Checking if exercise exists in DB is not needed, because we can't storage two exercise with the same name, so new exercise will just updated existing one.
                exerciseRef = database.getReference("users/" + user.getUid() + "/exercises/" + name);
                Exercise newExercise = new Exercise(name,type);
                exerciseRef.setValue(newExercise).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddNewExerciseActivity.this, "Exercise added", Toast.LENGTH_SHORT).show();
                            editTextExerciseName.setText("");
                        }
                        else {
                            Toast.makeText(AddNewExerciseActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        buttonCreateNewPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCreateNewPlan = new Intent(AddNewExerciseActivity.this, CreateNewPlanActivity.class);
                intentCreateNewPlan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentCreateNewPlan);
            }
        });

        buttonSeeYourPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddNewExercise = new Intent(AddNewExerciseActivity.this,PlanActivity.class);
                intentAddNewExercise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAddNewExercise);
            }
        });

        imageViewLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddNewExercise = new Intent(AddNewExerciseActivity.this,PlanActivity.class);
                intentAddNewExercise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAddNewExercise);
            }
        });

        imageViewCenterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMainWindow = new Intent(AddNewExerciseActivity.this,MainWindowActivity.class);
                intentMainWindow.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMainWindow);
            }
        });

        imageViewRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(AddNewExerciseActivity.this,ProfileActivity.class);
                intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentProfile);
            }
        });

    }

    private void initializeObjects() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        spinnerExerciseType = findViewById(R.id.spinnerExerciseType);
        adapter = ArrayAdapter.createFromResource(this, R.array.exercisesType, R.layout.spinner_item_20dp);
        spinnerExerciseType.setAdapter(adapter);

        buttonCreateNewPlan = findViewById(R.id.buttonCreateNewPlan);
        buttonSeeYourPlans = findViewById(R.id.buttonSeeYourPlans);
        buttonAddExercise = findViewById(R.id.buttonAddExercise);

        imageViewLeftIcon = findViewById(R.id.imageViewLeftIcon);
        imageViewCenterIcon = findViewById(R.id.imageViewCenterIcon);
        imageViewRightIcon = findViewById(R.id.imageViewRightIcon);


        editTextExerciseName = findViewById(R.id.editTextExerciseName);

        database = FirebaseDatabase.getInstance();

        exerciseRef = database.getReference("users/" + user.getUid() + "/exercises/");

        progressBar.setVisibility(View.GONE);
    }
}
