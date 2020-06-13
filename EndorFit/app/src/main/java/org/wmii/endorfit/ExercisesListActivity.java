package org.wmii.endorfit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ExercisesListActivity extends AppCompatActivity {
    private static String TAG = "ExercisesListActivity";
    public static RecyclerView RecyclerViewExercisesList;
    public Bitmap exerciseDetailsImage;
    private TabLayout tabLayout;
    private TabItem tabChest, tabLegs, tabABSBack, tabShoulders, tabArms;
    private ViewPager viewPager;
    public PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_list);
        Log.d(TAG, "onCreate: started");
        initWidgets();

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setElevation(0);
        //ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 2) {
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 3) {
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 4) {
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void initWidgets() {
        //exercisesListRecyclerView = (RecyclerView)findViewById(R.id.exersisesListRecyclerView);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabChest = (TabItem) findViewById(R.id.tabChest);
        tabABSBack = (TabItem) findViewById(R.id.tabABSBack);
        tabArms = (TabItem) findViewById(R.id.tabArms);
        tabShoulders = (TabItem) findViewById(R.id.tabShoulders);
        tabLegs = (TabItem) findViewById(R.id.tabLegs);
        viewPager = (ViewPager) findViewById(R.id.viewPagerExercises);
        RecyclerViewExercisesList = (RecyclerView) findViewById(R.id.exersisesListRecyclerView);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
    }

}
