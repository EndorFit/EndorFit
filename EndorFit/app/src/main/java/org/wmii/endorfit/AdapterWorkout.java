package org.wmii.endorfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterWorkout extends RecyclerView.Adapter<AdapterWorkout.ExercViewHolder> {
    public ArrayList<CheckBox> getAllSets() {
        return allSets;
    }

    private ArrayList<PlanItem> mPlanList;
    private OnItemClickListener mListener;
    private View view;
    dynamicViews dnv;
    Context context;
    ArrayList<CheckBox> allSets = new ArrayList<CheckBox>();
    int i=0;
    void setOnItemClickListener(AdapterWorkout.OnItemClickListener listener){
        mListener = listener;
    }

    public static class ExercViewHolder extends RecyclerView.ViewHolder{
        TextView adapterItemName;
        TextView adapterItemNameLabel;
        TextView adapterItemSecondCol;
        TextView adapterItemSecondColLabel;
        TextView adapterItemThirdCol;
        TextView adapterItemThirdColLabel;
        GridLayout placeForCheckbox;


        ExercViewHolder(@NonNull View itemView, final AdapterWorkout.OnItemClickListener listener) {
            super(itemView);
            adapterItemName = itemView.findViewById(R.id.textViewPlanItemName);
            adapterItemNameLabel = itemView.findViewById(R.id.textViewPlanItemNameLabel);
            adapterItemSecondCol = itemView.findViewById(R.id.textViewPlanItemSecond);
            adapterItemSecondColLabel = itemView.findViewById(R.id.textViewPlanItemSecondLabel);
            adapterItemThirdCol = itemView.findViewById(R.id.textViewPlanItemThird);
            adapterItemThirdColLabel = itemView.findViewById(R.id.textViewPlanItemThirdLabel);
            placeForCheckbox =  itemView.findViewById(R.id.placeCheck);

        }
    }
    AdapterWorkout(ArrayList<PlanItem> planList) {
        mPlanList = planList;
    }

    @NonNull
    @Override
    public ExercViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        ExercViewHolder pvh = new ExercViewHolder(v,(AdapterWorkout.OnItemClickListener) mListener);
        context=parent.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExercViewHolder holder, int position) {

        dnv=new dynamicViews(context);
        PlanItem currentItem = mPlanList.get(position);

        holder.adapterItemName.setText(currentItem.getName());
        holder.adapterItemNameLabel.setText(currentItem.getNameLabel());
        holder.adapterItemSecondCol.setText(currentItem.getSecondCol());
        holder.adapterItemSecondColLabel.setText(currentItem.getSecondColLabel());
        holder.adapterItemThirdCol.setText(currentItem.getThirdCol());
        holder.adapterItemThirdColLabel.setText(currentItem.getThirdColLabel());
        if(currentItem.getFirstColLabel()=="sets") {
            for (int a = 0; a < Integer.parseInt(currentItem.getFirstCol()); a++) {
                allSets.add(dnv.checkSet(context));
                holder.placeForCheckbox.addView(allSets.get(i));
                i++;
            }

        }
    }

    @Override
    public int getItemCount() {
        return mPlanList.size();
    }

    public class OnItemClickListener {
    }
}
