package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompletedPlansActivity extends AppCompatActivity {
    ProgressBar progressBar;


    ViewPager viewPager;
    ArrayList<CompletedPlan> completedPlans;
    ViewPagerCompletedPlansAdapter viewPagerAdapter;

    FirebaseDatabase database;
    DatabaseReference plansRef;
    FirebaseAuth mAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_plans);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();


        InitializeObjects();
    }

    private void InitializeObjects() {
        viewPager = findViewById(R.id.viewPagerListy);
        completedPlans = new ArrayList<>();
        final ArrayList<Exercise> exerciseArrayList = new ArrayList<>();


        final DatabaseReference completedPlansRef = database.getReference("users/" + user.getUid() + "/completed/");
        completedPlansRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot date : dataSnapshot.getChildren())
                {
                    String planDate = date.getKey();
                    String planName = "";
                    String stateWorkout = "";
                    ArrayList<Exercise> tempExerciseList = new ArrayList<>();
                    for(DataSnapshot item : date.getChildren())
                    {
                        if(!item.getKey().toString().equals("stateWorkout"))
                        {
                            planName = item.getKey().toString();
                            for(DataSnapshot exercise : item.getChildren())
                            {
                                Exercise tempExercise = exercise.getValue(Exercise.class);
                                tempExerciseList.add(tempExercise);
                            }
                        }
                        else
                        {
                            stateWorkout = item.getKey().toString();
                        }

                    }
                    completedPlans.add(new CompletedPlan(planDate,planName,tempExerciseList));
                }

                viewPagerAdapter = new ViewPagerCompletedPlansAdapter(completedPlans, CompletedPlansActivity.this);
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.setPadding(80,0,80,0);

                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
