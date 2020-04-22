package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddPlanActivity extends AppCompatActivity {
    ProgressBar progressBar;

    TextView txtViewExerciseType;
    EditText editTextTopLeft, editTextTopRight, editTextBotLeft, editTextPlanName;
    ImageView imageViewAddButton, imageViewSave;

    Spinner spinnerExercise;
    SpinnerAdapter adapter;
    ArrayList<String> spinnerData;

    RecyclerView recyclerViewExercises;
    PlanAdapter planAdapter;
    ArrayList<PlanItem> PlanItems;
    ArrayList<Exercise> planDB;

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference exercisesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        ///Check users authority
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user == null) {
                    Intent intent = new Intent(AddPlanActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        initializeObjects();

        progressBar.setVisibility(View.VISIBLE);

        buildRecyclerView();

        setListeners();
    }

    private void setListeners() {

        imageViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlanToDB();
            }
        });

        imageViewAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility()==View.VISIBLE) {return;}
                String name = spinnerExercise.getSelectedItem().toString();
                String type = txtViewExerciseType.getText().toString();
                String topLeft = editTextTopLeft.getText().toString();
                String topRight = editTextTopRight.getText().toString();
                String botLeft = editTextBotLeft.getText().toString();

                if(topLeft.isEmpty()){
                    editTextTopLeft.setError("Required");
                    editTextTopLeft.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(topRight.isEmpty()){
                    editTextTopRight.setError("Required");
                    editTextTopRight.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if( (type.equals("Exercise with weights") || type.equals("Exercise with time")) && botLeft.isEmpty()){
                    editTextBotLeft.setError("Required");
                    editTextBotLeft.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                switch (type){
                    case "Running":
                        PlanItems.add(new PlanItem(name,"name",topLeft,"time",topRight,"distance"));
                        planDB.add(new Exercise(name,type,Double.parseDouble(topLeft),Double.parseDouble(topRight)));
                        break;
                    case "Exercise with weights":
                        PlanItems.add(new PlanItem(name,"name",topLeft,"series",topRight,"reps",botLeft,"weights"));
                        planDB.add(new Exercise(name,type, Integer.parseInt(topLeft), Integer.parseInt(topRight), Double.parseDouble(botLeft)));
                        break;
                    case "Exercise without weights":
                        PlanItems.add(new PlanItem(name,"name",topLeft,"series",topRight,"reps"));
                        planDB.add(new Exercise(name,type, Integer.parseInt(topLeft), Integer.parseInt(topRight)));
                        break;
                    case "Exercise with time":
                        PlanItems.add(new PlanItem(name,"name",topLeft,"series",topRight,"reps",botLeft,"time"));
                        planDB.add(new Exercise(name,type, Integer.parseInt(topLeft), Integer.parseInt(topRight), Double.parseDouble(botLeft)));
                        break;
                }
                planAdapter.notifyItemInserted(PlanItems.size()-1);

            }
        });

        spinnerExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference exerciseRef = database.getReference("exercises/" + spinnerExercise.getSelectedItem().toString());
                exerciseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        txtViewExerciseType.setText(dataSnapshot.getValue(Exercise.class).getType().toString());
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


        txtViewExerciseType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(AddPlanActivity.this, "Edited", Toast.LENGTH_SHORT).show();
                editTextTopLeft.setText("");
                editTextTopRight.setText("");
                editTextBotLeft.setText("");
                switch(txtViewExerciseType.getText().toString())
                {
                    case "Running":
                        editTextTopLeft.setVisibility(View.VISIBLE);
                        editTextTopLeft.setHint("Time");
                        editTextTopRight.setVisibility(View.VISIBLE);
                        editTextTopRight.setHint("Distance");
                        editTextBotLeft.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        break;
                    case "Exercise with weights":
                        editTextTopLeft.setVisibility(View.VISIBLE);
                        editTextTopLeft.setHint("Series");
                        editTextTopRight.setVisibility(View.VISIBLE);
                        editTextTopRight.setHint("Reps");
                        editTextBotLeft.setVisibility(View.VISIBLE);
                        editTextBotLeft.setHint("Weights");
                        progressBar.setVisibility(View.GONE);
                        break;
                    case "Exercise without weights":
                        editTextTopLeft.setVisibility(View.VISIBLE);
                        editTextTopLeft.setHint("Series");
                        editTextTopRight.setVisibility(View.VISIBLE);
                        editTextTopRight.setHint("Reps");
                        editTextBotLeft.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        break;
                    case "Exercise with time":
                        editTextTopLeft.setVisibility(View.VISIBLE);
                        editTextTopLeft.setHint("Series");
                        editTextTopRight.setVisibility(View.VISIBLE);
                        editTextTopRight.setHint("Reps");
                        editTextBotLeft.setVisibility(View.VISIBLE);
                        editTextBotLeft.setHint("Time");
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void initializeObjects() {
        progressBar = findViewById(R.id.progressBar);

        editTextPlanName = findViewById(R.id.EditTxtPlanName);
        editTextTopLeft = findViewById(R.id.editTextTopLeft);
        editTextTopRight = findViewById(R.id.editTextTopRight);
        editTextBotLeft = findViewById(R.id.editTextBotLeft);

        txtViewExerciseType = findViewById(R.id.txtViewExerciseType);

        imageViewSave = findViewById(R.id.imageViewSave);
        imageViewAddButton = findViewById(R.id.imageViewAddExercise);

        spinnerExercise = findViewById(R.id.spinnerExercise);

        spinnerData = new ArrayList<>();
        planDB= new ArrayList<>();
        PlanItems = new ArrayList<>();

        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        exercisesRef = database.getReference("exercises/");

        exercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot item: dataSnapshot.getChildren()){
                    spinnerData.add(item.getKey());
                }
                adapter = new ArrayAdapter<>(AddPlanActivity.this, R.layout.spinner_item_20dp, spinnerData);
                spinnerExercise.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void addPlanToDB() {
        progressBar.setVisibility(View.VISIBLE);
        String planName = editTextPlanName.getText().toString();
        if(planName.isEmpty())
        {
            editTextPlanName.setError("Required");
            editTextPlanName.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(planDB.isEmpty())
        {
            Toast.makeText(AddPlanActivity.this, "Plan cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        DatabaseReference userPlansRef = database.getReference("users/" + user.getUid() + "/plans/" + planName);
        userPlansRef.setValue(planDB).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddPlanActivity.this, "Plan added successful", Toast.LENGTH_SHORT).show();
                    PlanItems.clear();
                    planAdapter.notifyDataSetChanged();
                    planDB.clear();
                    editTextTopLeft.setText("");
                    editTextTopRight.setText("");
                    editTextBotLeft.setText("");
                    editTextPlanName.setText("");
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(AddPlanActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }

    private void buildRecyclerView() {
        recyclerViewExercises = findViewById(R.id.recyclerViewExercises);
        recyclerViewExercises.setHasFixedSize(true);
        planAdapter = new PlanAdapter(PlanItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerViewExercises.setLayoutManager(mLayoutManager);
        recyclerViewExercises.setAdapter(planAdapter );

        planAdapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
            @Override
            public void deleteItem(int position) {
                PlanItems.remove(position);
                planAdapter.notifyItemRemoved(position);
                planDB.remove(position);
            }
        });
    }
}
