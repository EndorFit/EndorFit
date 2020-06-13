package org.wmii.endorfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class workoutTimer extends AppCompatActivity implements timerDialog.timerDialogListener {
    private TextView timeSet, nameWorkout;
    private Button openTimer;
    public static final String TIMER_VALUE = "org.wmii.endorfit.TIMER_VALUE";
    String timerValue;
    private DatabaseReference Ref;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    String workoutKey;
    DataSnapshot dataSnapshot;
    DatabaseReference planContentRef;
    ArrayList<PlanItem> planItems;
    ArrayList<Exercise> planItemsExercise;
    RecyclerView recyclerViewPlan;
    AdapterWorkout workoutAdapter;
    ArrayList<CheckBox> allSets;
    ArrayList<Boolean> doneSets;
    Workout dbSave;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupAll();
        openDialog();
        dataGet();

        buildRecyclerView();
        openTimer.setOnClickListener(new View.OnClickListener() {
            boolean clicked = true;

            public void onClick(View v) {
                if (clicked == true) {
                    setListeners();
                    recyclerViewPlan.setVisibility(View.VISIBLE);
                    clicked = false;
                    for (int i = 0; i < allSets.size(); i++) {
                        allSets.get(i).setChecked(false);
                    }
                } else if (!clicked) {
                    saveWorkout();
                    finish();
                }
            }
        });

    }

    private void saveWorkout() {
        String data = java.text.DateFormat.getDateTimeInstance().format(new Date());
        planContentRef = database.getReference("users/" + user.getUid() + "/completed/" + data + "/" + workoutKey);

        dbSave = new Workout(state, planItemsExercise);
        planContentRef.setValue(dbSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(workoutTimer.this, "Saved your progress", Toast.LENGTH_SHORT).show();
                    planItems.clear();
                } else {
                    Toast.makeText(workoutTimer.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    public void opentimerWindow() {
        Intent intent = new Intent(this, timerWindowActivity.class);
        intent.putExtra(TIMER_VALUE, timerValue);
        startActivity(intent);
    }

    public void openDialog() {
        timerDialog timerdialog = new timerDialog();
        timerdialog.show(getSupportFragmentManager(), "Timer set");

    }

    @Override
    public void applyText(String seconds) {
        timerValue = seconds;
    }

    private void buildRecyclerView() {
        recyclerViewPlan.setHasFixedSize(true);
        workoutAdapter = new AdapterWorkout(planItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPlan.setLayoutManager(mLayoutManager);
        recyclerViewPlan.setAdapter(workoutAdapter);

    }

    private View.OnClickListener checkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < allSets.size(); i++) {

                if (v == allSets.get(i)) {
                    doneSets.add(true);
                    opentimerWindow();
                    state = doneSets.size() + "/" + allSets.size();
                    openTimer.setText(state);

                    if (doneSets.size() == allSets.size()) {
                        openTimer.setBackgroundColor(-16711936);
                        openTimer.setText("Save Progres");
                    }
                }
            }
        }
    };

    private void setListeners() {
        allSets = workoutAdapter.getAllSets();
        doneSets = new ArrayList<>();
        for (CheckBox btn : allSets)
            btn.setOnClickListener(checkListener);
    }

    private void SetupAll() {
        setContentView(R.layout.activity_workout_timer);
        timeSet = (TextView) findViewById(R.id.editTimer);
        nameWorkout = (TextView) findViewById(R.id.textViewNameWorkout);
        openTimer = findViewById(R.id.buttonTimer);
        recyclerViewPlan = findViewById(R.id.recyclerViewPlan);
        workoutAdapter = new AdapterWorkout(planItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPlan.setHasFixedSize(true);
        recyclerViewPlan.setLayoutManager(mLayoutManager);
        recyclerViewPlan.setAdapter(workoutAdapter);
        planItems = new ArrayList<PlanItem>();
        planItemsExercise = new ArrayList<Exercise>();
        allSets = new ArrayList<CheckBox>();
        workoutKey = getIntent().getStringExtra("EXTRA_WORKOUT_KEY");
        nameWorkout.setText(workoutKey);
    }

    private void dataGet() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(workoutTimer.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        planContentRef = database.getReference("users/" + user.getUid() + "/plans/" + workoutKey);

        planContentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                planItems.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Exercise tempExercise = item.getValue(Exercise.class);
                    String type = tempExercise.getType();
                    String name = tempExercise.getName();
                    String time;
                    String sets;
                    String reps;
                    String weights;
                    String distance;

                    switch (type) {
                        case "Moving":
                            time = String.valueOf(tempExercise.getTime());
                            distance = String.valueOf(tempExercise.getDistance());
                            planItemsExercise.add(new Exercise(name, Double.parseDouble(time), Double.parseDouble(distance)));
                            planItems.add(new PlanItem(name, "name", time, "time", distance, "distance", false));
                            break;
                        case "Exercise with weights":
                            sets = String.valueOf(tempExercise.getSets());
                            reps = String.valueOf(tempExercise.getReps());
                            weights = String.valueOf(tempExercise.getWeight());
                            planItemsExercise.add(new Exercise(name, type, Integer.parseInt(sets), Integer.parseInt(reps), Double.parseDouble(weights)));
                            planItems.add(new PlanItem(name, "name", sets, "sets", reps, "reps", weights, "weights", false));
                            break;
                        case "Exercise without weights":
                            sets = String.valueOf(tempExercise.getSets());
                            reps = String.valueOf(tempExercise.getReps());
                            planItemsExercise.add(new Exercise(name, type, Integer.parseInt(sets), Integer.parseInt(reps)));
                            planItems.add(new PlanItem(name, "name", sets, "sets", reps, "reps", false));
                            break;
                        case "Exercise with time":
                            sets = String.valueOf(tempExercise.getSets());
                            reps = String.valueOf(tempExercise.getReps());
                            time = String.valueOf(tempExercise.getTime());
                            planItemsExercise.add(new Exercise(name, type, Integer.parseInt(sets), Integer.parseInt(reps), Double.parseDouble(time)));
                            planItems.add(new PlanItem(name, "name", sets, "sets", reps, "reps", time, "time", false));
                            break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}