package org.wmii.endorfit.Tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.wmii.endorfit.Activities.ExercisesListActivity;
import org.wmii.endorfit.Activities.MainActivity;
import org.wmii.endorfit.Adapters.ExercisesRecyclerViewAdapter;
import org.wmii.endorfit.R;

public class tabShoulders extends Fragment {
    View v;
    public static final String TAG = "tabShoulders";

    //private RecyclerView exercisesListRecyclerView;
    public tabShoulders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: started");
        v = inflater.inflate(R.layout.fragment_tab_chest, container, false);
        //ExercisesListActivity.exercisesListRecyclerView = (RecyclerView) itemList.findViewById(R.id.exersisesListRecyclerView);
        ExercisesRecyclerViewAdapter adapter = new ExercisesRecyclerViewAdapter(super.getContext(), 3);
        ExercisesListActivity.RecyclerViewExercisesList = (RecyclerView) v.findViewById(R.id.exersisesListRecyclerView);
        ExercisesListActivity.RecyclerViewExercisesList.setAdapter(adapter);
        ExercisesListActivity.RecyclerViewExercisesList.setLayoutManager(new LinearLayoutManager(super.getContext()));
        adapter.setExerciseKnowledgeBases(MainActivity.myDb);
        return v;
    }
}
