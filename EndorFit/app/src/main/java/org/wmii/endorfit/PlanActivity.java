package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class PlanActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonAddExercise;
    EditText editTxtExerciseName;
    Spinner spinnerExerciseType;

    FirebaseDatabase database;
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
                if(user == null) {
                    Intent intent = new Intent(PlanActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        user = mAuth.getCurrentUser();


        database = FirebaseDatabase.getInstance();


        buttonAddExercise = findViewById(R.id.buttonAddExercise);
            buttonAddExercise.setOnClickListener(this);
        editTxtExerciseName = findViewById(R.id.editTxtExerciseName);
        spinnerExerciseType = findViewById(R.id.spinnerExerciseType);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.exercisesType, R.layout.spinner_item_20dp);
        spinnerExerciseType.setAdapter(adapter);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddExercise:
                if(editTxtExerciseName.getVisibility() == View.GONE){
                    editTxtExerciseName.setVisibility(View.VISIBLE);
                    spinnerExerciseType.setVisibility(View.VISIBLE);
                }
                else
                {
                    AddExerciseToDatabase();
                }
                break;
            case R.id.imageViewCenterIcon:
                break;
        }
    }

    private void AddExerciseToDatabase() {
        //VALIDACJA (SPR CZY WPISANE DANE ORAZ CZY DANE NIE ZNAJDUJA JUZ SIE W BAZIE DANYCH)
        String name = editTxtExerciseName.getText().toString();
        String type = spinnerExerciseType.getSelectedItem().toString();

        if(name.isEmpty())
        {
            editTxtExerciseName.setError("Name is required");
            editTxtExerciseName.requestFocus();
            return;
        }
        //Checking if exercise exists in DB is not needed, because we can't storage two exercise with the same name, so new exercise will just updated existing one.
        DatabaseReference exerciseRef = database.getReference("exercises/" + name);
        Exercise newExercise = new Exercise(type);
        exerciseRef.setValue(newExercise).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PlanActivity.this, "Exercise added", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PlanActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editTxtExerciseName.setText("");
        editTxtExerciseName.setVisibility(View.GONE);
        spinnerExerciseType.setVisibility(View.GONE);

    }
}
