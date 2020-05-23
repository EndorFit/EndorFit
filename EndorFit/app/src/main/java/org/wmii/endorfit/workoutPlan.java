package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class workoutPlan extends AppCompatActivity implements View.OnClickListener {
    dynamicViews dnv;
    private Button button,starter;
    Context context;
    DatabaseReference Ref;
    FirebaseDatabase database;
    FirebaseUser user;
    FirebaseAuth mAuth;
    AdapterView adapterView;
    View view;
    LinearLayout mleyout;
    ArrayList<String> planItems;
    ArrayList<Button> allExer = new ArrayList<Button>();
    DataSnapshot dataSnapshot;
    TextView lol;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);
        starter=(Button) findViewById(R.id.buttonStart);
        button =(Button) findViewById(R.id.button);
        mleyout = (LinearLayout) findViewById(R.id.leyout);
        planItems=new ArrayList<String>();

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                openActivityWorking();

            }
        }      );

        starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityTimer();
            }
        });


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

        database = FirebaseDatabase.getInstance();
        Ref = database.getReference("users/" +user.getUid() + "/plans/");

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    planItems.add(item.getKey());
                }
                if(planItems.isEmpty()){
                    Toast.makeText(workoutPlan.this, "There is no plans. Add some and comeback", Toast.LENGTH_LONG).show();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
            });
        Toast.makeText(workoutPlan.this, "There is this many items : "+planItems.size(), Toast.LENGTH_LONG).show();

        for (i=0;i<planItems.size();i++)
        {   addView(view);

        }

    }

    public void openActivityWorking() {
        Intent intent =new Intent(this,workoutGo.class);
        startActivity(intent);
    }

    public void openActivityTimer()
    {
        Intent intent =new Intent(this,workoutTimer.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {

    }
    public void addView(View view) {
        this.view = view;
        dnv = new dynamicViews(context);
        allExer.add(dnv.buttonWorkout(getApplicationContext()));
        mleyout.addView(allExer.get(i));


    }
}
