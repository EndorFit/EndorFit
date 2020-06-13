package org.wmii.endorfit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import org.wmii.endorfit.DataClasses.CompletedPlan;
import org.wmii.endorfit.DataClasses.Exercise;
import org.wmii.endorfit.DataClasses.PlanItem;
import org.wmii.endorfit.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerCompletedPlansAdapter extends PagerAdapter {

    private List<CompletedPlan> list;
    private LayoutInflater layoutInflater;
    private Context context;


    public ViewPagerCompletedPlansAdapter(List<CompletedPlan> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_completed_plans, container, false);

        TextView data = view.findViewById(R.id.textViewDate);
        TextView nazwa = view.findViewById(R.id.textViewPlanName);
        ;
        RecyclerView testRec = view.findViewById(R.id.recyclerViewExercises);

        data.setText(list.get(position).getDate());
        nazwa.setText(list.get(position).getName());
        ArrayList<Exercise> listaCwiczen = list.get(position).getExercises();
        ArrayList<PlanItem> listaPlanItems = new ArrayList<>();

        for (Exercise item : listaCwiczen) {
            String weight = String.valueOf(item.getWeight());
            String time = String.valueOf(item.getTime());
            String avg = String.valueOf((item.getDistance() / item.getTime()));
            String distance = String.valueOf(item.getDistance());
            String sets = String.valueOf(item.getSets());
            String reps = String.valueOf(item.getReps());
            String name = item.getName();
            String type = item.getType();
            switch (type) {
                case "Moving":
                    listaPlanItems.add(new PlanItem(name, "name", distance, "distance", time, "time", avg, "AVG speed", false));
                    break;
                case "Exercise with weights":
                    listaPlanItems.add(new PlanItem(name, "name", sets, "sets", reps, "reps", weight, "weights", false));
                    break;
                case "Exercise without weights":
                    listaPlanItems.add(new PlanItem(name, "name", sets, "sets", reps, "reps", false));
                    break;
                case "Exercise with time":
                    listaPlanItems.add(new PlanItem(name, "name", sets, "sets", reps, "reps", time, "time", false));
                    break;
            }
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);

        testRec.setLayoutManager(manager);
        PlanAdapter adapter = new PlanAdapter(listaPlanItems);
        testRec.setAdapter(adapter);

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
