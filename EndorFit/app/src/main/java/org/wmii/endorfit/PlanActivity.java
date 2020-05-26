package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PlanActivity extends AppCompatActivity {
    ProgressBar progressBar;

    Spinner spinnerPlanName;
    SpinnerAdapter adapter;
    ArrayList<String> plansList;

    RecyclerView recyclerViewPlan;
    PlanAdapter planAdapter;
    ArrayList<PlanItem> planItems;
    ArrayList<Exercise> planContentList;

    Button buttonCreateNewPlan, buttonAddNewExercise;

    ImageView imageViewDeletePlan;

    ImageView imageViewLeftIcon, imageViewCenterIcon, imageViewRightIcon;

    FirebaseDatabase database;
    DatabaseReference plansRef;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(PlanActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        user = mAuth.getCurrentUser();

        initializeObjects();
        progressBar.setVisibility(View.VISIBLE);
        setListeners();
        progressBar.setVisibility(View.GONE);

    }

    private void setListeners() {
        plansRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                plansList.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    plansList.add(item.getKey());
                }
                adapter = new ArrayAdapter<>(PlanActivity.this, R.layout.spinner_item_20dp, plansList);
                spinnerPlanName.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        spinnerPlanName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference planContentRef = database.getReference("users/" + user.getUid() + "/plans/" + spinnerPlanName.getSelectedItem());
                planContentRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        planItems.clear();
                        for(DataSnapshot item: dataSnapshot.getChildren()){
                            Exercise tempExercise = item.getValue(Exercise.class);
                            String type = tempExercise.getType();
                            String name = tempExercise.getName();
                            String time;
                            String sets;
                            String reps;
                            String weights;
                            String distance;

                            switch (type){
                                case "Running":
                                    time = String.valueOf(tempExercise.getTime());
                                    distance = String.valueOf(tempExercise.getDistance());
                                    planItems.add(new PlanItem(name,"name",time,"time",distance,"distance",false));
                                    break;
                                case "Exercise with weights":
                                    sets = String.valueOf(tempExercise.getSets());
                                    reps = String.valueOf(tempExercise.getReps());
                                    weights = String.valueOf(tempExercise.getWeight());
                                    planItems.add(new PlanItem(name,"name",sets,"sets",reps,"reps",weights,"weights",false));
                                    break;
                                case "Exercise without weights":
                                    sets = String.valueOf(tempExercise.getSets());
                                    reps = String.valueOf(tempExercise.getReps());
                                    planItems.add(new PlanItem(name,"name",sets,"sets",reps,"reps",false));
                                    break;
                                case "Exercise with time":
                                    sets = String.valueOf(tempExercise.getSets());
                                    reps = String.valueOf(tempExercise.getReps());
                                    time = String.valueOf(tempExercise.getTime());
                                    planItems.add(new PlanItem(name,"name",sets,"sets",reps,"reps",time,"time",false));
                                    break;
                            }
                            //planAdapter.notifyItemInserted(planItems.size()-1);
                        }

                        buildRecyclerView();
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageViewDeletePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( spinnerPlanName.getSelectedItem() != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlanActivity.this, R.style.AlertDialog)
                            .setMessage("Are you sure you want to delete this plan?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference planToDeleteRef = database.getReference("users/" + user.getUid() + "/plans/" + spinnerPlanName.getSelectedItem().toString());
                                    planToDeleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(PlanActivity.this, "Plan deleted successful", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(PlanActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        buttonCreateNewPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCreateNewPlan = new Intent(PlanActivity.this, CreateNewPlanActivity.class);
                intentCreateNewPlan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentCreateNewPlan);
            }
        });

        buttonAddNewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddNewExercise = new Intent(PlanActivity.this,AddNewExerciseActivity.class);
                intentAddNewExercise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAddNewExercise);
            }
        });


        imageViewCenterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMainWindow = new Intent(PlanActivity.this,MainWindowActivity.class);
                intentMainWindow.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMainWindow);
            }
        });

        imageViewRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(PlanActivity.this,ProfileActivity.class);
                intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentProfile);
            }
        });
    }

    private void buildRecyclerView() {
        progressBar.setVisibility(View.VISIBLE);

        planAdapter = new PlanAdapter(planItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPlan.setHasFixedSize(true);
        recyclerViewPlan.setLayoutManager(mLayoutManager);
        recyclerViewPlan.setAdapter(planAdapter );
        progressBar.setVisibility(View.GONE);
        recyclerViewPlan = findViewById(R.id.recyclerViewPlan);

    }

    private void initializeObjects() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        spinnerPlanName = findViewById(R.id.spinnerPlanName);
        plansList = new ArrayList<>();
        recyclerViewPlan = findViewById(R.id.recyclerViewPlan);
        planContentList = new ArrayList<>();
        planItems = new ArrayList<>();

        buttonCreateNewPlan = findViewById(R.id.buttonCreateNewPlan);
        buttonAddNewExercise = findViewById(R.id.buttonAddNewExercise);

        imageViewDeletePlan = findViewById(R.id.imageViewDeletePlan);

        imageViewLeftIcon = findViewById(R.id.imageViewLeftIcon);
        imageViewCenterIcon = findViewById(R.id.imageViewCenterIcon);
        imageViewRightIcon = findViewById(R.id.imageViewRightIcon);

        database = FirebaseDatabase.getInstance();

        plansRef = database.getReference("users/" + user.getUid() + "/plans/");

        progressBar.setVisibility(View.GONE);
    }
}
