package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class workoutTimer extends AppCompatActivity implements timerDialog.timerDialogListener {
private TextView timeSet,nameWorkout;
private Button openTimer;
public static final String TIMER_VALUE ="org.wmii.endorfit.TIMER_VALUE";
String timerValue;
    private DatabaseReference Ref;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    String workoutKey;
    DataSnapshot dataSnapshot;
    DatabaseReference planContentRef;
    ArrayList<PlanItem> planItems;
    RecyclerView recyclerViewPlan;
    AdapterWorkout workoutAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);
        timeSet=(TextView) findViewById(R.id.editTimer);
        nameWorkout=(TextView) findViewById(R.id.nameWorkout);
        openTimer=findViewById(R.id.buttonTimer);
        recyclerViewPlan = findViewById(R.id.recyclerViewPlan);
        workoutAdapter = new AdapterWorkout(planItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPlan.setHasFixedSize(true);
        recyclerViewPlan.setLayoutManager(mLayoutManager);
        recyclerViewPlan.setAdapter(workoutAdapter);
        planItems=new  ArrayList<PlanItem>();
        openDialog();
        buildRecyclerView();
        openTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opentimerWindow();
            }
        });
        workoutKey = getIntent().getStringExtra("EXTRA_WORKOUT_KEY");
        nameWorkout.setText(workoutKey);
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
                        case "Running":
                            time = String.valueOf(tempExercise.getTime());
                            distance = String.valueOf(tempExercise.getDistance());
                            planItems.add(new PlanItem(name, "name", time, "time", distance, "distance", false));
                            break;
                        case "Exercise with weights":
                            sets = String.valueOf(tempExercise.getSets());
                            reps = String.valueOf(tempExercise.getReps());
                            weights = String.valueOf(tempExercise.getWeight());
                            planItems.add(new PlanItem(name, "name", sets, "sets", reps, "reps", weights, "weights", false));
                            break;
                        case "Exercise without weights":
                            sets = String.valueOf(tempExercise.getSets());
                            reps = String.valueOf(tempExercise.getReps());
                            planItems.add(new PlanItem(name, "name", sets, "sets", reps, "reps", false));
                            break;
                        case "Exercise with time":
                            sets = String.valueOf(tempExercise.getSets());
                            reps = String.valueOf(tempExercise.getReps());
                            time = String.valueOf(tempExercise.getTime());
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

    public void opentimerWindow() {
        Intent intent =new Intent(this,timerWindow.class);
        intent.putExtra(TIMER_VALUE,timerValue);
        startActivity(intent);
    }
    public void openDialog()
    {
timerDialog timerdialog=new timerDialog();
    timerdialog.show(getSupportFragmentManager(),"Timer set");
    }

    @Override
    public void aplytext(String seconds) {
        timerValue=seconds;
    }

    private void buildRecyclerView() {

        recyclerViewPlan.setHasFixedSize(true);
        workoutAdapter = new AdapterWorkout(planItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPlan.setLayoutManager(mLayoutManager);
        recyclerViewPlan.setAdapter(workoutAdapter);

    }
}
