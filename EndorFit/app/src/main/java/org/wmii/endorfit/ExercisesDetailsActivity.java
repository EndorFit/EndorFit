package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

//
public class ExercisesDetailsActivity extends AppCompatActivity {
    ImageView imageViewExImage;
    TextView textViewExTitleText, textViewExCategoryAndDifficulty, textViewExDescriptionText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_details);
        initWidgets();
        Intent intent = getIntent();
        int id = intent.getIntExtra("exerciseID",0);
        ExerciseKnowledgeBase thisExerciseKnowledgeBase = getOneExercise(id);
        textViewExTitleText.setText(thisExerciseKnowledgeBase.getName());
        textViewExDescriptionText.setText(thisExerciseKnowledgeBase.getDescription());
        textViewExCategoryAndDifficulty.setText("Category: " + thisExerciseKnowledgeBase.getCategory() + ", Difficulty: " + thisExerciseKnowledgeBase.getDifficultyLevel());
        imageViewExImage.setImageBitmap(thisExerciseKnowledgeBase.getImage());
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
        textViewExCategoryAndDifficulty = (TextView)findViewById(R.id.exDetailsCategoryAndDifficultyText);
        imageViewExImage = (ImageView)findViewById(R.id.exDetailsImage);
        textViewExTitleText = (TextView)findViewById(R.id.exDetailsTitleText);
        textViewExDescriptionText = (TextView)findViewById(R.id.exDetailsDescriptionText);
    }

}