package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class workoutPlan extends AppCompatActivity implements View.OnClickListener {
    dynamicViews dnv;
    private Button button,starter;
    Context context;
    DatabaseReference exerciseRef;
    FirebaseDatabase database;
    FirebaseUser user;
    FirebaseAuth mAuth;
    View view;
    LinearLayout mleyout;
    ArrayList<PlanItem> planItems;
    ArrayList<Button> allExer = new ArrayList<Button>();
    ArrayList<String> plansList;
    DataSnapshot dataSnapshot;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);
        starter=(Button) findViewById(R.id.buttonStart);
        button =(Button) findViewById(R.id.button);
        mleyout = (LinearLayout) findViewById(R.id.leyout);
        user = mAuth.getCurrentUser();
        DatabaseReference planContentRef = database.getReference("users/" + user.getUid() + "/plans/" );
        plansList.clear();

        for(DataSnapshot item: dataSnapshot.getChildren()){
            plansList.add(item.getKey());
        }

        int licznik=plansList.size();
for ( i=0;i<licznik;i++)
{
    addView(view);
}


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
        mleyout.addView(allExer.get(i), i);


    }
}

