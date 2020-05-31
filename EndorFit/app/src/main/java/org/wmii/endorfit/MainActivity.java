package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTxtEmail, editTxtPassword;
    ProgressBar progressBar;
    Button btnLogin, btnGoToSignUp;
    public static DataBaseHelper myDb;
    public final static String TAG = "MainActivity";
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Toast.makeText(this, "User already signed in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, MainWindowActivity.class);
            startActivity(intent);
        }
        initializeObjects();
        myDb = new DataBaseHelper(this);
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", true);
        if ( firstRun )
        {
            // here run your first-time instructions, for example :
            addToDataBase(this);
        }
        editTxtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    userLogin();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void initializeObjects() {
        editTxtEmail = findViewById(R.id.editTxtEmail);
        editTxtPassword = findViewById(R.id.editTxtPassword);
        progressBar = findViewById(R.id.progressBar);

        btnLogin = findViewById(R.id.btnLogin);
        btnGoToSignUp = findViewById(R.id.btnGotoSignUp);

        btnLogin.setOnClickListener(this);
        btnGoToSignUp.setOnClickListener(this);
    }

    private void userLogin() {
        String email = editTxtEmail.getText().toString().trim();
        String password = editTxtPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTxtEmail.setError("Email is required");
            editTxtPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTxtEmail.setError("Please enter a valid email");
            editTxtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTxtPassword.setError("Password is required");
            editTxtPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTxtPassword.setError("Minimum length of password should be 6");
            editTxtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, MainWindowActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGotoSignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.btnLogin:
                userLogin();
                break;

        }
    }
    public boolean addToDataBase(Context context)
    {
        Context thisContext = context;
        ArrayList<Boolean> booleansCat = new ArrayList<>();
        booleansCat.add(myDb.insertDataCategoryTable(getString(R.string.exCategoryChest)));
        booleansCat.add(myDb.insertDataCategoryTable(getString(R.string.exCategoryArms)));
        booleansCat.add(myDb.insertDataCategoryTable(getString(R.string.exCategoryABSAndBack)));
        booleansCat.add(myDb.insertDataCategoryTable(getString(R.string.exCategoryShoulders)));
        booleansCat.add(myDb.insertDataCategoryTable(getString(R.string.exCategoryLegs)));
        booleansCat.add(myDb.insertDataCategoryTable(getString(R.string.exCategoryMoving)));
        int trueCounterCat = 0;
        for (;trueCounterCat < booleansCat.size();++trueCounterCat)
        {
            if(booleansCat.get(trueCounterCat) != true) break;
        }
        if(trueCounterCat != booleansCat.size())
        {
            Log.d(TAG,"addToDataBase: Category: fail");
        }
        ArrayList<Boolean> booleansDiff = new ArrayList<>();
        booleansDiff.add(myDb.insertDataDifficultyTable(getString(R.string.exDifficultyEasy)));
        booleansDiff.add(myDb.insertDataDifficultyTable(getString(R.string.exDifficultyMedium)));
        booleansDiff.add(myDb.insertDataDifficultyTable(getString(R.string.exDifficultyHard)));
        booleansDiff.add(myDb.insertDataDifficultyTable(getString(R.string.exDifficultyVeryHard)));
        int trueCounterDiff = 0;
        for (;trueCounterDiff < booleansDiff.size();++trueCounterDiff)
        {
            if(booleansDiff.get(trueCounterDiff) != true) break;
        }
        if(trueCounterDiff != booleansDiff.size())
        {
            Log.d(TAG,"addToDataBase: Difficulty: fail");
        }
        String[] internalTypes = context.getResources().getStringArray(R.array.exercisesType);
        ArrayList<Boolean> booleansInterTypes = new ArrayList<>();
        booleansInterTypes.add(myDb.insertDataInternalTypeTable(internalTypes[0]));
        booleansInterTypes.add(myDb.insertDataInternalTypeTable(internalTypes[1]));
        booleansInterTypes.add(myDb.insertDataInternalTypeTable(internalTypes[2]));
        booleansInterTypes.add(myDb.insertDataInternalTypeTable(internalTypes[3]));
        int trueCounterInterTypes = 0;
        for (;trueCounterInterTypes < booleansInterTypes.size();++trueCounterInterTypes)
        {
            if(booleansInterTypes.get(trueCounterInterTypes) != true) break;
        }
        if(trueCounterInterTypes != booleansInterTypes.size())
        {
            Log.d(TAG,"addToDataBase: InterTypes: fail");
        }
        ArrayList<Boolean> booleans = new ArrayList<>();
        this.getClass().getResource("cycling.jpg");
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex1Name), 2,2,getString(R.string.ex1Description),myDb.getPicturePath(R.drawable.single_arm_tricep_pushdown,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.single_arm_tricep_pushdown)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex2Name), 5,2,getString(R.string.ex2Description),myDb.getPicturePath(R.drawable.inner_thigh,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.inner_thigh)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex3Name), 4,1,getString(R.string.ex3Description),myDb.getPicturePath(R.drawable.standing_shoulder_press,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.standing_shoulder_press)),1));
       booleans.add(myDb.insertDataMainTable(getString(R.string.ex4Name), 2,3,getString(R.string.ex4Description),null,1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex5Name), 1,2,getString(R.string.ex5Description),myDb.getPicturePath(R.drawable.push_up,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.push_up)),2));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex6Name), 3,3,getString(R.string.ex6Description),myDb.getPicturePath(R.drawable.pull_up,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.pull_up)),2));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex7Name), 5,2,getString(R.string.ex7Description),myDb.getPicturePath(R.drawable.squat,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.squat)),2));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex8Name), 3,1,getString(R.string.ex8Description),myDb.getPicturePath(R.drawable.plank,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.plank)),2));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex9Name), 3,1,getString(R.string.ex9Description),myDb.getPicturePath(R.drawable.crunches,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.crunches)),2));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex10Name), 1,1,getString(R.string.ex10Description),myDb.getPicturePath(R.drawable.bench_press,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.bench_press)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex11Name), 3,4,getString(R.string.ex11Description),myDb.getPicturePath(R.drawable.deadlift,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.deadlift)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex12Name), 1,2,getString(R.string.ex12Description),myDb.getPicturePath(R.drawable.dips,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.dips)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex13Name), 4,2,getString(R.string.ex13Description),myDb.getPicturePath(R.drawable.overhead_press,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.overhead_press)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex14Name), 3,2,getString(R.string.ex14Description),myDb.getPicturePath(R.drawable.romanian_deadlift,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.romanian_deadlift)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex15Name), 5,1,getString(R.string.ex15Description),myDb.getPicturePath(R.drawable.farmers_walk,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.farmers_walk)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex16Name), 3,2,getString(R.string.ex16Description),myDb.getPicturePath(R.drawable.barbell_hip_thrust,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.barbell_hip_thrust)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex17Name), 6,1,getString(R.string.ex17Description),myDb.getPicturePath(R.drawable.walking,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.walking)),3));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex18Name), 5,2,getString(R.string.ex18Description),myDb.getPicturePath(R.drawable.lunges,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.lunges)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex19Name), 4,2,getString(R.string.ex19Description),myDb.getPicturePath(R.drawable.military_press,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.military_press)),1));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex20Name), 6,2,getString(R.string.ex20Description),myDb.getPicturePath(R.drawable.running,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.running)),4));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex21Name), 6,1,getString(R.string.ex21Description),myDb.getPicturePath(R.drawable.cycling,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.cycling)),3));
        booleans.add(myDb.insertDataMainTable(getString(R.string.ex22Name), 6,2,getString(R.string.ex22Description),myDb.getPicturePath(R.drawable.roller_skating,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.roller_skating)),3));
        int trueCounterMain = 0;
        for (;trueCounterMain < booleans.size();++trueCounterMain)
        {
            if(booleans.get(trueCounterMain) != true) break;
        }
        if(trueCounterMain == booleans.size())
        {
            Log.d(TAG,"addToDataBase: succeeded");
            SharedPreferences settings = getSharedPreferences("prefs", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
            return true;
        }
        else
        {
            Log.e(TAG, "addToDataBase: Main: failed, fail at position: " + trueCounterMain);
            return false;
        }
    }

}
