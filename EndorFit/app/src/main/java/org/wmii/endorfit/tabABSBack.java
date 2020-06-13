package org.wmii.endorfit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tabABSBack#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tabABSBack extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    View v;
    public static final String TAG = "tabABSBack";
   // private RecyclerView exercisesListRecyclerView;
    public tabABSBack() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: started");
        v = inflater.inflate(R.layout.fragment_tab_chest, container, false);
        //ExercisesListActivity.exercisesListRecyclerView = (RecyclerView) itemList.findViewById(R.id.exersisesListRecyclerView);
        ExercisesRecyclerViewAdapter adapter = new ExercisesRecyclerViewAdapter(super.getContext(),2);
        ExercisesListActivity.RecyclerViewExercisesList = (RecyclerView) v.findViewById(R.id.exersisesListRecyclerView);
        ExercisesListActivity.RecyclerViewExercisesList.setAdapter(adapter);
        ExercisesListActivity.RecyclerViewExercisesList.setLayoutManager(new LinearLayoutManager(super.getContext()));
        adapter.setExerciseKnowledgeBases(MainActivity.myDb);
        return v;
    }
}