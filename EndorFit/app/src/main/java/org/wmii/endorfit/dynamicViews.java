package org.wmii.endorfit;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class dynamicViews {
    Context ctx;

    public dynamicViews(Context ctx) {
        this.ctx = ctx;
    }


    public Button buttonWorkout (Context context){
        final ViewGroup.LayoutParams lparams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        context.setTheme(R.style.AppTheme);
        final Button buttonWorkout=new Button(context);
        int id=0;
        buttonWorkout.setId(id);
        buttonWorkout.setBackgroundColor(Color.rgb(250,150,100));
        buttonWorkout.setWidth(450);
        buttonWorkout.setHeight(200);



        return buttonWorkout;
    }

    public CheckBox checkSet (Context context){
        final ViewGroup.LayoutParams lparams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final CheckBox checkSet=new CheckBox(context);
        int id=0;
        checkSet.setId(id);
        checkSet.setWidth(100);
        checkSet.setHeight(100);
        return checkSet;
    }
}