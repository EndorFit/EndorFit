package org.wmii.endorfit.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wmii.endorfit.DataClasses.PlanItem;
import org.wmii.endorfit.R;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private ArrayList<PlanItem> mPlanList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void deleteItem(int position);
    }

   public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView adapterItemName;
        TextView adapterItemNameLabel;
        TextView adapterItemFirstCol;
        TextView adapterItemFirstColLabel;
        TextView adapterItemSecondCol;
        TextView adapterItemSecondColLabel;
        TextView adapterItemThirdCol;
        TextView adapterItemThirdColLabel;
        TextView adapterItemFull;
        ImageView adapterImageViewDelete;

        PlanViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            adapterItemName = itemView.findViewById(R.id.textViewPlanItemName);
            adapterItemNameLabel = itemView.findViewById(R.id.textViewPlanItemNameLabel);
            adapterItemFirstCol = itemView.findViewById(R.id.textViewPlanItemFirst);
            adapterItemFirstColLabel = itemView.findViewById(R.id.textViewPlanItemFirstLabel);
            adapterItemSecondCol = itemView.findViewById(R.id.textViewPlanItemSecond);
            adapterItemSecondColLabel = itemView.findViewById(R.id.textViewPlanItemSecondLabel);
            adapterItemThirdCol = itemView.findViewById(R.id.textViewPlanItemThird);
            adapterItemThirdColLabel = itemView.findViewById(R.id.textViewPlanItemThirdLabel);
            adapterImageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            adapterItemFull = itemView.findViewById(R.id.textViewFull);

            adapterImageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.deleteItem(position);
                        }
                    }
                }
            });
        }
    }

    public PlanAdapter(ArrayList<PlanItem> planList) {
        mPlanList = planList;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item, parent, false);
        PlanViewHolder pvh = new PlanViewHolder(v, mListener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        PlanItem currentItem = mPlanList.get(position);


        if (!currentItem.getSecondCol().isEmpty()) {
            holder.adapterItemName.setText(currentItem.getName());
            holder.adapterItemNameLabel.setText(currentItem.getNameLabel());
            holder.adapterItemFirstCol.setText(currentItem.getFirstCol());
            holder.adapterItemFirstColLabel.setText(currentItem.getFirstColLabel());
            holder.adapterItemSecondCol.setText(currentItem.getSecondCol());
            holder.adapterItemSecondColLabel.setText(currentItem.getSecondColLabel());
            holder.adapterItemThirdCol.setText(currentItem.getThirdCol());
            holder.adapterItemThirdColLabel.setText(currentItem.getThirdColLabel());
            holder.adapterItemFull.setVisibility(View.GONE);
            if (currentItem.isEnableDelete() == false) {
                holder.adapterImageViewDelete.setVisibility(View.GONE);
            }
        } else {
            holder.adapterItemFull.setText(currentItem.getName());
            holder.adapterItemFull.setVisibility(View.VISIBLE);
            if (currentItem.isEnableDelete() == false) {
                holder.adapterImageViewDelete.setVisibility(View.GONE);
            }
            holder.adapterItemName.setText("");
            holder.adapterItemNameLabel.setText("");
            holder.adapterItemFirstCol.setText("");
            holder.adapterItemFirstColLabel.setText("");
            holder.adapterItemSecondCol.setText("");
            holder.adapterItemSecondColLabel.setText("");
            holder.adapterItemThirdCol.setText("");
            holder.adapterItemThirdColLabel.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return mPlanList.size();
    }
}
