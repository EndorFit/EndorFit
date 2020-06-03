package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
        exImage.setImageBitmap(thisExerciseKnowledgeBase.getImage());
    }
    public ExerciseKnowledgeBase getOneExercise(int id)
    {
        Cursor cursor = MainActivity.myDb.getOneRow(id);
        cursor.moveToFirst();
        //Bitmap bitmap = DataBaseHelper.getImage(id,5);
        //BitmapFactory.decodeByteArray(cursor.getBlob(5), 0, cursor.getBlob(5).length)
        int exId = cursor.getInt(0);
        String name = cursor.getString(1);
        String category = cursor.getString(2);
        String difficultyLevel = cursor.getString(3);
        String description = cursor.getString(4);
        String internalType = cursor.getString(6);
        String imagePath = cursor.getString(5);
        cursor.close();
        return new ExerciseKnowledgeBase(exId,name,category ,description ,difficultyLevel, imagePath,internalType);
    }
    public void initWidgets()
    {
        exCategoryAndDifficulty = (TextView)findViewById(R.id.exDetailsCategoryAndDifficultyText);
        exImage = (ImageView)findViewById(R.id.exDetailsImage);
        exTitleText = (TextView)findViewById(R.id.exDetailsTitleText);
        exDescriptionText = (TextView)findViewById(R.id.exDetailsDescriptionText);
    }

}