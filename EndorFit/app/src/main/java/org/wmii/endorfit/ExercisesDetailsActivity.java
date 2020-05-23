package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

//
public class ExercisesDetailsActivity extends AppCompatActivity {
    ImageView exImage;
    TextView exTitleText, exCategoryAndDifficulty, exDescriptionText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_details);
        initWidgets();
        Intent intent = getIntent();
        int id = intent.getIntExtra("exerciseID",0);
        ExerciseKnowledgeBase thisExerciseKnowledgeBase = getOneExercise(id);
        exTitleText.setText(thisExerciseKnowledgeBase.getName());
        exDescriptionText.setText(thisExerciseKnowledgeBase.getDescription());
        exCategoryAndDifficulty.setText("Category: " + thisExerciseKnowledgeBase.getCategory() + ", Difficulty: " + thisExerciseKnowledgeBase.getDifficultyLevel());
    }
    public ExerciseKnowledgeBase getOneExercise(int id)
    {
        Cursor cursor = MainActivity.myDb.getOneRow(id);
        cursor.moveToFirst();
        StringBuilder description = new StringBuilder(cursor.getString(3));
        for(int i = 0;i < description.length();++i)
        {
            if(description.charAt(i) == '.')
            {
                description.insert(i,'\n');
            }
        }
        return new ExerciseKnowledgeBase(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(4), description.toString());
    }
    public void initWidgets()
    {
        exCategoryAndDifficulty = (TextView)findViewById(R.id.exDetailsCategoryAndDifficultyText);
        exImage = (ImageView)findViewById(R.id.exDetailsImage);
        exTitleText = (TextView)findViewById(R.id.exDetailsTitleText);
        exDescriptionText = (TextView)findViewById(R.id.exDetailsDescriptionText);
    }

}