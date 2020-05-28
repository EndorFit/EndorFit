package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
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


public class workoutPlan extends AppCompatActivity implements View.OnClickListener {
    dynamicViews dnv;
    private Button button,starter;
    Context context;
    DatabaseReference Ref;
    FirebaseDatabase database;
    FirebaseUser user;
    FirebaseAuth mAuth;
    View view;
    GridLayout mleyout;
    ArrayList<String> planItems;
    ArrayList<Button> allExer ;
    DataSnapshot dataSnapshot;
    TextView lol;


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
    private void initializeObjects()
    {
        starter=(Button) findViewById(R.id.buttonStart);
        button =(Button) findViewById(R.id.buttonStartRunningMode);
        mleyout = (GridLayout) findViewById(R.id.leyout);
        planItems=new ArrayList<String>();
        allExer = new ArrayList<Button>();

        database = FirebaseDatabase.getInstance();
        Ref = database.getReference("users/" +user.getUid() + "/plans/");

    }
    private void getDatafromDatabase()
    {
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                planItems.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    planItems.add(item.getKey());
                }
                createViews();
                for(Button btn:allExer)
                    btn.setOnClickListener(btnListener);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void createViews()

    {
        Toast.makeText(workoutPlan.this, "There is this many items : "+planItems.size(), Toast.LENGTH_LONG).show();

        for (i=0;i<planItems.size();i++)
        {
            addView(view);
            allExer.get(i).setText(planItems.get(i));

        }
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            for(int i = 0 ; i < allExer.size() ; i++){

                if(v == allExer.get(i)){


                    Intent intent =new Intent(getBaseContext(),workoutTimer.class);
                    intent.putExtra("EXTRA_WORKOUT_KEY", allExer.get(i).getText());
                    startActivity(intent);

                }
            }

        }
    };
}
