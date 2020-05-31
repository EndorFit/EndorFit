package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.inputmethodservice.ExtractEditText;
import android.location.Location;
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
import java.util.Vector;

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
                            for(DataSnapshot exerciseList : item.getChildren())
                            {
                                for(DataSnapshot exercise : exerciseList.getChildren())
                                {
                                    String name = "";
                                    String type = "";
                                    int sets = 0;
                                    int reps = 0;
                                    double weight = 0;
                                    double distance = 0;
                                    double time = 0;
                                    Vector<Location> route = new Vector<>();
                                    for(DataSnapshot typ : exercise.getChildren())
                                    {
                                        if(typ.getKey().equals("distance")) distance = Double.parseDouble(typ.getValue().toString());
                                        if(typ.getKey().equals("weight")) weight = Double.parseDouble(typ.getValue().toString());
                                        if(typ.getKey().equals("time")) time = Double.parseDouble(typ.getValue().toString());
                                        if(typ.getKey().equals("sets")) sets = Integer.parseInt(typ.getValue().toString());
                                        if(typ.getKey().equals("reps")) reps = Integer.parseInt(typ.getValue().toString());
                                        if(typ.getKey().equals("type")) type = typ.getValue().toString();
                                        if(typ.getKey().equals("name")) name = typ.getValue().toString();
                                        if(typ.getKey().equals("route"))
                                        {
                                            for(DataSnapshot location : typ.getChildren())
                                            {
                                                Location temp = new Location(String.valueOf(location.getChildren()));
                                                route.add(temp);
                                            }
                                        }
                                    }

                                    switch (type){
                                        case "Moving":
                                            tempExerciseList.add(new Exercise(name, type, distance, time, route));
                                            break;
                                        case "Exercise with weights":
                                            tempExerciseList.add(new Exercise(name, type, sets, reps, weight));
                                            break;
                                        case "Exercise without weights":
                                            tempExerciseList.add(new Exercise(name, type, sets, reps));
                                            break;
                                        case "Exercise with time":
                                            tempExerciseList.add(new Exercise(name, type, sets, reps, time));
                                            break;
                                    }
                                }
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
