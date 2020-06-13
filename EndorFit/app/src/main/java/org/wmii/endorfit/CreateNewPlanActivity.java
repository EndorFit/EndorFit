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
import java.util.Objects;

public class CreateNewPlanActivity extends AppCompatActivity {
    ProgressBar progressBar;

    TextView txtViewExerciseType;
    EditText editTextTopLeft, editTextTopRight, editTextBotLeft, editTextPlanName;
    ImageView imageViewAddButton, imageViewSave;

    ImageView imageViewLeftIcon, imageViewCenterIcon, imageViewRightIcon;

    Spinner spinnerExercise;
    SpinnerAdapter spinnerAdapter;
    ArrayList<String> spinnerData_full;
    ArrayList<String> spinnerData_gym;
    ArrayList<String> spinnerData_moving;

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
        setContentView(R.layout.activity_create_new_plan);
        ///Check users authority
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user == null) {
                    Intent intent = new Intent(CreateNewPlanActivity.this, MainActivity.class);
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

                if(topLeft.isEmpty() && !type.equals("Moving")){
                    editTextTopLeft.setError("Required");
                    editTextTopLeft.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(topRight.isEmpty() && !type.equals("Moving")){
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
                    case "Moving":
                        PlanItems.add(new PlanItem(name,"","","","",""));
                        planDB.add(new Exercise(name,type,0,0));
                        editTextPlanName.setText(name);
                        editTextPlanName.setEnabled(false);
                        break;
                    case "Exercise with weights":
                        PlanItems.add(new PlanItem(name,"name",topLeft,"sets",topRight,"reps",botLeft,"weights"));
                        planDB.add(new Exercise(name,type, Integer.parseInt(topLeft), Integer.parseInt(topRight), Double.parseDouble(botLeft)));
                        break;
                    case "Exercise without weights":
                        PlanItems.add(new PlanItem(name,"name",topLeft,"sets",topRight,"reps"));
                        planDB.add(new Exercise(name,type, Integer.parseInt(topLeft), Integer.parseInt(topRight)));
                        break;
                    case "Exercise with time":
                        PlanItems.add(new PlanItem(name,"name",topLeft,"sets",topRight,"reps",botLeft,"time"));
                        planDB.add(new Exercise(name,type, Integer.parseInt(topLeft), Integer.parseInt(topRight), Double.parseDouble(botLeft)));
                        break;
                }
                planAdapter.notifyItemInserted(PlanItems.size()-1);

                planChanged(false);

            }
        });

        spinnerExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference exerciseRef = database.getReference("users/" + user.getUid() + "/exercises/" + spinnerExercise.getSelectedItem().toString());
                exerciseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        txtViewExerciseType.setText(Objects.requireNonNull(dataSnapshot.getValue(Exercise.class)).getType());
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
                editTextTopLeft.setText("");
                editTextTopRight.setText("");
                editTextBotLeft.setText("");
                switch(txtViewExerciseType.getText().toString())
                {
                    case "Moving":
                        editTextTopLeft.setVisibility(View.VISIBLE);
                        editTextTopLeft.setHint("");
                        editTextTopLeft.setEnabled(false);
                        editTextTopRight.setVisibility(View.VISIBLE);
                        editTextTopRight.setEnabled(false);
                        editTextTopRight.setHint("");
                        editTextBotLeft.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        break;
                    case "Exercise with weights":
                        editTextTopLeft.setVisibility(View.VISIBLE);
                        editTextTopLeft.setHint("Series");
                        editTextTopLeft.setEnabled(true);
                        editTextTopRight.setVisibility(View.VISIBLE);
                        editTextTopRight.setHint("Reps");
                        editTextTopRight.setEnabled(true);
                        editTextBotLeft.setVisibility(View.VISIBLE);
                        editTextBotLeft.setHint("Weights");
                        progressBar.setVisibility(View.GONE);
                        break;
                    case "Exercise without weights":
                        editTextTopLeft.setVisibility(View.VISIBLE);
                        editTextTopLeft.setHint("Series");
                        editTextTopLeft.setEnabled(true);
                        editTextTopRight.setVisibility(View.VISIBLE);
                        editTextTopRight.setHint("Reps");
                        editTextTopRight.setEnabled(true);
                        editTextBotLeft.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        break;
                    case "Exercise with time":
                        editTextTopLeft.setVisibility(View.VISIBLE);
                        editTextTopLeft.setHint("Series");
                        editTextTopLeft.setEnabled(true);
                        editTextTopRight.setVisibility(View.VISIBLE);
                        editTextTopRight.setHint("Reps");
                        editTextTopRight.setEnabled(true);
                        editTextBotLeft.setVisibility(View.VISIBLE);
                        editTextBotLeft.setHint("Time");
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        });

        imageViewLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddNewExercise = new Intent(CreateNewPlanActivity.this,PlanActivity.class);
                intentAddNewExercise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAddNewExercise);
            }
        });

        imageViewCenterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMainWindow = new Intent(CreateNewPlanActivity.this,MainWindowActivity.class);
                intentMainWindow.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMainWindow);
            }
        });

        imageViewRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(CreateNewPlanActivity.this,ProfileActivity.class);
                intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentProfile);
            }
        });

    }

    private void planChanged(boolean isMinus) {
        if(planDB.size() == 0)
        {
            spinnerAdapter = new ArrayAdapter<>(CreateNewPlanActivity.this, R.layout.spinner_item_20dp, spinnerData_full);
            spinnerExercise.setAdapter(spinnerAdapter);
            imageViewAddButton.setEnabled(true);
            spinnerExercise.setEnabled(true);
            editTextPlanName.setText("");
            editTextPlanName.setEnabled(true);
        }
        else if(planDB.size() == 1)
        {
            if(txtViewExerciseType.getText().toString().equals("Moving"))
            {
                spinnerExercise.setEnabled(false);
                imageViewAddButton.setEnabled(!imageViewAddButton.isEnabled());
            }
            else if(!isMinus)
            {
                spinnerAdapter = new ArrayAdapter<>(CreateNewPlanActivity.this, R.layout.spinner_item_20dp, spinnerData_gym);
                spinnerExercise.setAdapter(spinnerAdapter);
                imageViewAddButton.setEnabled(true);
                spinnerExercise.setEnabled(true);
            }
        }
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

        imageViewLeftIcon = findViewById(R.id.imageViewLeftIcon);
        imageViewCenterIcon = findViewById(R.id.imageViewCenterIcon);
        imageViewRightIcon = findViewById(R.id.imageViewRightIcon);

        spinnerExercise = findViewById(R.id.spinnerExercise);


        spinnerData_full = new ArrayList<>();
        spinnerData_gym = new ArrayList<>();
        planDB= new ArrayList<>();
        PlanItems = new ArrayList<>();

        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        exercisesRef = database.getReference( "users/" + user.getUid() + "/exercises/");
        exercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    spinnerData_full.add(item.getKey());
                    String name = "";
                    String type = "";
                    for(DataSnapshot temp : item.getChildren())
                    {
                        if(temp.getKey().equals("name")) name = temp.getValue().toString();
                        if(temp.getKey().equals("type")) type = temp.getValue().toString();
                    }
                    if(!type.equals("Moving")) spinnerData_gym.add(name);
                }
                if(spinnerData_full.isEmpty()){
                    Toast.makeText(CreateNewPlanActivity.this, "There is no exercise. Add some and comeback", Toast.LENGTH_LONG).show();
                    Intent backIntent = new Intent(CreateNewPlanActivity.this, PlanActivity.class);
                    startActivity(backIntent);
                }
                spinnerAdapter = new ArrayAdapter<>(CreateNewPlanActivity.this, R.layout.spinner_item_20dp, spinnerData_full);
                spinnerExercise.setAdapter(spinnerAdapter);
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
            Toast.makeText(CreateNewPlanActivity.this, "Plan cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        DatabaseReference userPlansRef = database.getReference("users/" + user.getUid() + "/plans/" + planName);
        userPlansRef.setValue(planDB).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CreateNewPlanActivity.this, "Plan added successful", Toast.LENGTH_SHORT).show();
                    PlanItems.clear();
                    planAdapter.notifyDataSetChanged();
                    planDB.clear();
                    editTextTopLeft.setText("");
                    editTextTopRight.setText("");
                    editTextBotLeft.setText("");
                    editTextPlanName.setText("");
                    progressBar.setVisibility(View.GONE);
                    planChanged(false);
                }
                else {
                    Toast.makeText(CreateNewPlanActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                planChanged(true);
            }
        });
    }
}
