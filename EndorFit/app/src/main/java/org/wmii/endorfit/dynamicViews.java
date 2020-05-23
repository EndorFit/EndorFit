package org.wmii.endorfit;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class dynamicViews {
    Context ctx;

    public dynamicViews(Context ctx) {
        this.ctx = ctx;
    }
    public EditText descriptionTextView (Context context){
        final ViewGroup.LayoutParams lparams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText=new EditText(context);
        int id=0;
        editText.setId(id);
        editText.setMinEms(2);
        editText.setTextColor(Color.rgb(0,0,0));
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setHint("Name");
        return editText;
    }
    public EditText repsEditText(Context context){
        final ViewGroup.LayoutParams lparams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText=new EditText(context);
        int id=0;
        editText.setId(id);
        editText.setMinEms(2);
        editText.setTextColor(Color.rgb(0,0,0));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setHint("Reps");
        return editText;
    }
    public EditText setsEditText(Context context){
        final ViewGroup.LayoutParams lparams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText=new EditText(context);
        int id=0;
        editText.setId(id);
        editText.setMinEms(2);
        editText.setTextColor(Color.rgb(0,0,0));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setHint("Sets");
        return editText;
    }
    public EditText weightEditText(Context context){
        final ViewGroup.LayoutParams lparams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText=new EditText(context);
        int id=0;
        editText.setId(id);
        editText.setMinEms(2);
        editText.setTextColor(Color.rgb(0,0,0));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setHint("Weight/Time");
        return editText;
    }

    public Button buttonWorkout (Context context){
        final ViewGroup.LayoutParams lparams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final Button buttonWorkout=new Button(context);
        int id=0;
        buttonWorkout.setId(id);
        buttonWorkout.setMinEms(2);
        buttonWorkout.setBackgroundColor(Color.rgb(250,150,100));
        buttonWorkout.setWidth(30);
        buttonWorkout.setHeight(30);

        return buttonWorkout;
    }
}