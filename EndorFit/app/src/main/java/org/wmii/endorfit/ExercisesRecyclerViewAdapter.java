package org.wmii.endorfit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExercisesRecyclerViewAdapter extends RecyclerView.Adapter<ExercisesRecyclerViewAdapter.ViewHolder>{
    public final static String TAG = "ExercisesRecViewAdapter";
    private Context context;
    private int tab;
    private ArrayList<ExerciseKnowledgeBase> exerciseKnowledgeBases = new ArrayList<>();

    public ExercisesRecyclerViewAdapter(Context context, int tab) {
        this.context = context;
        this.tab = tab;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_exercise_rec_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesRecyclerViewAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.exerciseName.setText(exerciseKnowledgeBases.get(position).getName());
        holder.exerciseImage.setImageBitmap(exerciseKnowledgeBases.get(position).getImage());
        holder.exerciseCategoryAndDifficulty.setText("Category: " + exerciseKnowledgeBases.get(position).getCategory() + "\nDifficulty: " + exerciseKnowledgeBases.get(position).getDifficultyLevel());
        holder.exerciseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExercisesDetailsActivity.class);
                intent.putExtra("exerciseID", exerciseKnowledgeBases.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseKnowledgeBases.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView exerciseCard;
        private ImageView exerciseImage;
        private TextView exerciseName, exerciseCategoryAndDifficulty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseCard = (CardView) itemView.findViewById(R.id.exerciseCardView);
            exerciseImage = (ImageView) itemView.findViewById(R.id.exerciseImage);
            exerciseCategoryAndDifficulty = (TextView) itemView.findViewById(R.id.exerciseCategoryAndDifficultyTextView);
            exerciseName = (TextView) itemView.findViewById(R.id.exerciseNameTextView);
            exerciseImage = (ImageView)itemView.findViewById(R.id.exerciseImage);
            exerciseImage = (ImageView)itemView.findViewById(R.id.exerciseImage);
        }
    }
    public void setExerciseKnowledgeBases(DataBaseHelper db)
    {
        Cursor result = db.getCategorizedData(tab);
        if(result.getCount() == 0){
            Log.d(TAG, "setExercises: Empty dataBase");
            return;
        }
        Log.d(TAG, "setExercises getCount" + result.getCount());
        result.moveToFirst();
        //StringBuffer buffer = new StringBuffer();
        int id;
        String name = "";
        String category = "";
        String difficultyLevel = "";
        String description = "";
        //Bitmap image;
        String internalType = "";
        while (result.moveToNext())
        {
            //Log.d(TAG, "setExercises: Column name " + result.getColumnName());
            id = result.getInt(0);
            name = result.getString(1);
            category = result.getString(2);
            difficultyLevel = result.getString(3);
            description = result.getString(4);
            internalType = result.getString(6);
            ExerciseKnowledgeBase exerciseKnowledgeBase = new ExerciseKnowledgeBase(id, name,category,description,difficultyLevel,DataBaseHelper.getImageToList(id,5),internalType);
            Log.d(TAG, "setExercises: Name: " + exerciseKnowledgeBase.getName() + ", category: " + exerciseKnowledgeBase.getCategory());
            exerciseKnowledgeBases.add(exerciseKnowledgeBase);
        }
        result.close();
        notifyDataSetChanged();
    }
//
}