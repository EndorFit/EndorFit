package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

public class ExercisesListActivity extends AppCompatActivity {
    private static String TAG = "ExercisesListActivity";
    RecyclerView exercisesListRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_list);
        Log.d(TAG,"onCreate: started");
        exercisesListRecyclerView = (RecyclerView)findViewById(R.id.exersisesListRecyclerView);

        ExercisesRecyclerViewAdapter adapter = new ExercisesRecyclerViewAdapter(this);
        exercisesListRecyclerView.setAdapter(adapter);
        exercisesListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setExerciseKnowledgeBases(MainActivity.myDb);
        //ArrayList<Exercise> exerciseArrayList = new ArrayList<>();

    }

}