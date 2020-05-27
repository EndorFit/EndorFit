package org.wmii.endorfit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        ExercisesRecyclerViewAdapter adapter = new ExercisesRecyclerViewAdapter(super.getContext(),3);
        ExercisesListActivity.exercisesListRecyclerView = (RecyclerView) v.findViewById(R.id.exersisesListRecyclerView);
        ExercisesListActivity.exercisesListRecyclerView.setAdapter(adapter);
        ExercisesListActivity.exercisesListRecyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
        adapter.setExerciseKnowledgeBases(MainActivity.myDb);
        return v;
    }
}
