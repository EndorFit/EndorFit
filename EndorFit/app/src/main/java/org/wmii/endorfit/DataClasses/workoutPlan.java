package org.wmii.endorfit.DataClasses;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.wmii.endorfit.Activities.MainActivity;
import org.wmii.endorfit.Activities.RunningModeActivity;
import org.wmii.endorfit.R;
import org.wmii.endorfit.Adapters.dynamicViews;
import org.wmii.endorfit.Activities.workoutTimerActivity;

import java.util.ArrayList;


public class workoutPlan extends AppCompatActivity implements View.OnClickListener {
    dynamicViews dnv;

    Context context;
    DatabaseReference Ref;
    FirebaseDatabase database;
    FirebaseUser user;
    FirebaseAuth mAuth;
    View view;
    GridLayout mleyout;
    ArrayList<String> planItems;
    ArrayList<String> planItemstype;
    ArrayList<Button> allExer;
    DataSnapshot dataSnapshot;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(workoutPlan.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        user = mAuth.getCurrentUser();

        initializeObjects();
        getDatafromDatabase();

    }

    public void openActivityTimer() {
        Intent intent = new Intent(this, workoutTimerActivity.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void addView(View view) {
        this.view = view;
        dnv = new dynamicViews(context);
        allExer.add(dnv.buttonWorkout(getApplicationContext()));
        mleyout.addView(allExer.get(i));
    }

    private void initializeObjects() {
        mleyout = (GridLayout) findViewById(R.id.gridLayoutPlans);
        planItems = new ArrayList<String>();
        planItemstype = new ArrayList<String>();
        allExer = new ArrayList<Button>();

        database = FirebaseDatabase.getInstance();
        Ref = database.getReference("users/" + user.getUid() + "/plans/");
    }

    private void getDatafromDatabase() {
        Ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                planItems.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    planItems.add(item.getKey());
                }
                addType();

                createViews();
                for (Button btn : allExer)
                    btn.setOnClickListener(btnListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void addType() {
        planItemstype.clear();
        for (int i = 0; i < planItems.size(); i++) {
            Ref = database.getReference("users/" + user.getUid() + "/plans/" + planItems.get(i));

            Ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Exercise tempExercise = item.getValue(Exercise.class);
                        planItemstype.add(tempExercise.getType());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createViews() {
        for (i = 0; i < planItems.size(); i++) {
            addView(view);
            allExer.get(i).setText(planItems.get(i));
        }
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            for (int i = 0; i < allExer.size(); i++) {

                if (v == allExer.get(i)) {

                    if (planItemstype.get(i).equals("Moving")) {
                        Intent intent = new Intent(getBaseContext(), RunningModeActivity.class);
                        intent.putExtra("EXTRA_WORKOUT_KEY", allExer.get(i).getText());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getBaseContext(), workoutTimerActivity.class);
                        intent.putExtra("EXTRA_WORKOUT_KEY", allExer.get(i).getText());
                        startActivity(intent);
                    }
                }
            }
        }
    };
}
