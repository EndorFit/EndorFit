package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class workoutGo extends AppCompatActivity {
    private GridLayout mleyout;
    Button addstuff,saveWorkout;
    dynamicViews dnv;
    Context context;
    Exercise[] tab;
    TextView name;
    Integer howManyExer=0;
DatabaseReference databaseWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_go);
        mleyout=(GridLayout)findViewById(R.id.leyout);
        addstuff=(Button)findViewById(R.id.addstuff);
        saveWorkout=(Button)findViewById(R.id.submitButton);
        name=(TextView) findViewById(R.id.nameWorkout);
        databaseWorkout= FirebaseDatabase.getInstance().getReference("workouts");
        addstuff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {howManyExer=howManyExer+1;
                dnv=new dynamicViews(context);
                mleyout.addView(dnv.descriptionTextView(getApplicationContext()),4);
                mleyout.addView(dnv.repsEditText(getApplicationContext()),5);
                mleyout.addView(dnv.setsEditText(getApplicationContext()),6);
                mleyout.addView(dnv.weightEditText(getApplicationContext()),7);

            }

        });
saveWorkout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       boolean allGood= saveExerc();
        if (allGood) {addWorkout();}

    }
});
    }
    private boolean saveExerc(){
       if (howManyExer>0) {
           tab = new Exercise[howManyExer];
           for (int i=0; i<tab.length; i++) {
                tab[i].exerciseName="lolo";
                tab[i].Reps=3;
               tab[i].Sets=3;
               tab[i].Weight=3.9;

           }

       }
       return true;
    }
    private  void addWorkout(){
String nameWorkout=name.getText().toString().trim();
if (!TextUtils.isEmpty(nameWorkout))
{
    String id =databaseWorkout.push().getKey();
    Workout workout= new Workout(id,nameWorkout,tab);
    databaseWorkout.child(id).setValue(workout);
    Toast.makeText(this,"Workout added",Toast.LENGTH_LONG).show();
}
else
{Toast.makeText(this,"Shit hit the fan",Toast.LENGTH_LONG).show();}


    }
}
