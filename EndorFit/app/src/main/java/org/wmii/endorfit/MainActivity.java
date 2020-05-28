package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
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
        String[] internalTypes = context.getResources().getStringArray(R.array.exercisesType);
        ArrayList<Boolean> booleans = new ArrayList<>();
        this.getClass().getResource("cycling.jpg");
        booleans.add(myDb.insertData(getString(R.string.ex1Name), getString(R.string.exCategoryArms),getString(R.string.exDifficultyMedium),getString(R.string.ex1Description),myDb.getPicturePath(R.drawable.single_arm_tricep_pushdown,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.single_arm_tricep_pushdown)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex2Name), getString(R.string.exCategoryLegs),getString(R.string.exDifficultyMedium),getString(R.string.ex2Description),myDb.getPicturePath(R.drawable.inner_thigh,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.inner_thigh)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex3Name), getString(R.string.exCategoryShoulders),getString(R.string.exDifficultyEasy),getString(R.string.ex3Description),myDb.getPicturePath(R.drawable.standing_shoulder_press,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.standing_shoulder_press)),internalTypes[0]));
       booleans.add(myDb.insertData(getString(R.string.ex4Name), getString(R.string.exCategoryArms),getString(R.string.exDifficultyHard),getString(R.string.ex4Description),null,internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex5Name), getString(R.string.exCategoryChest),getString(R.string.exDifficultyMedium),getString(R.string.ex5Description),myDb.getPicturePath(R.drawable.push_up,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.push_up)),internalTypes[1]));
        booleans.add(myDb.insertData(getString(R.string.ex6Name), getString(R.string.exCategoryABSAndBack),getString(R.string.exDifficultyHard),getString(R.string.ex6Description),myDb.getPicturePath(R.drawable.pull_up,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.pull_up)),internalTypes[1]));
        booleans.add(myDb.insertData(getString(R.string.ex7Name), getString(R.string.exCategoryLegs),getString(R.string.exDifficultyMedium),getString(R.string.ex7Description),myDb.getPicturePath(R.drawable.squat,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.squat)),internalTypes[1]));
        booleans.add(myDb.insertData(getString(R.string.ex8Name), getString(R.string.exCategoryABSAndBack),getString(R.string.exDifficultyEasy),getString(R.string.ex8Description),myDb.getPicturePath(R.drawable.plank,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.plank)),internalTypes[1]));
        booleans.add(myDb.insertData(getString(R.string.ex9Name), getString(R.string.exCategoryABSAndBack),getString(R.string.exDifficultyEasy),getString(R.string.ex9Description),myDb.getPicturePath(R.drawable.crunches,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.crunches)),internalTypes[1]));
        booleans.add(myDb.insertData(getString(R.string.ex10Name), getString(R.string.exCategoryChest),getString(R.string.exDifficultyMedium),getString(R.string.ex10Description),myDb.getPicturePath(R.drawable.bench_press,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.bench_press)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex11Name), getString(R.string.exCategoryABSAndBack),getString(R.string.exDifficultyVeryHard),getString(R.string.ex11Description),myDb.getPicturePath(R.drawable.deadlift,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.deadlift)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex12Name), getString(R.string.exCategoryChest),getString(R.string.exDifficultyMedium),getString(R.string.ex12Description),myDb.getPicturePath(R.drawable.dips,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.dips)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex13Name), getString(R.string.exCategoryShoulders),getString(R.string.exDifficultyMedium),getString(R.string.ex13Description),myDb.getPicturePath(R.drawable.overhead_press,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.overhead_press)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex14Name), getString(R.string.exCategoryABSAndBack),getString(R.string.exDifficultyMedium),getString(R.string.ex14Description),myDb.getPicturePath(R.drawable.romanian_deadlift,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.romanian_deadlift)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex15Name), getString(R.string.exCategoryLegs),getString(R.string.exDifficultyEasy),getString(R.string.ex15Description),myDb.getPicturePath(R.drawable.farmers_walk,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.farmers_walk)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex16Name), getString(R.string.exCategoryABSAndBack),getString(R.string.exDifficultyMedium),getString(R.string.ex16Description),myDb.getPicturePath(R.drawable.barbell_hip_thrust,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.barbell_hip_thrust)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex17Name), getString(R.string.exCategoryLegs),getString(R.string.exDifficultyEasy),getString(R.string.ex17Description),myDb.getPicturePath(R.drawable.walking,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.walking)),internalTypes[2]));
        booleans.add(myDb.insertData(getString(R.string.ex18Name), getString(R.string.exCategoryLegs),getString(R.string.exDifficultyMedium),getString(R.string.ex18Description),myDb.getPicturePath(R.drawable.lunges,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.lunges)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex19Name), getString(R.string.exCategoryShoulders),getString(R.string.exDifficultyMedium),getString(R.string.ex19Description),myDb.getPicturePath(R.drawable.military_press,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.military_press)),internalTypes[0]));
        booleans.add(myDb.insertData(getString(R.string.ex20Name), getString(R.string.exCategoryLegs),getString(R.string.exDifficultyMedium),getString(R.string.ex20Description),myDb.getPicturePath(R.drawable.running,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.running)),internalTypes[3]));
        booleans.add(myDb.insertData(getString(R.string.ex21Name), getString(R.string.exCategoryLegs),getString(R.string.exDifficultyEasy),getString(R.string.ex21Description),myDb.getPicturePath(R.drawable.cycling,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.cycling)),internalTypes[2]));
        booleans.add(myDb.insertData(getString(R.string.ex22Name), getString(R.string.exCategoryLegs),getString(R.string.exDifficultyMedium),getString(R.string.ex22Description),myDb.getPicturePath(R.drawable.roller_skating,thisContext,BitmapFactory.decodeResource(getResources(),R.drawable.roller_skating)),internalTypes[2]));
        int trueCounter = 0;
        for (;trueCounter < booleans.size();++trueCounter)
        {
            if(booleans.get(trueCounter) != true) break;
        }
        if(trueCounter == booleans.size())
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
            Log.e(TAG, "addToDataBase: failed, fail at position: " + trueCounter);
            return false;
        }
    }

}
