package org.wmii.endorfit;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class dynamicViews {
    Context ctx;

    public dynamicViews(Context ctx) {
        this.ctx = ctx;
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

    public CheckBox checkSet (Context context){
        final ViewGroup.LayoutParams lparams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final CheckBox checkSet=new CheckBox(context);
        int id=0;
        checkSet.setId(id);
        checkSet.setBackgroundColor(Color.rgb(250,150,100));
        checkSet.setWidth(10);
        checkSet.setHeight(10);
        checkSet.setPadding(10,10,10,10);

        return checkSet;
    }
}