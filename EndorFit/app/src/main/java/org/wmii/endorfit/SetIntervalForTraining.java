package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SetIntervalForTraining extends AppCompatActivity {
    public static final String TAG = "SetIntervalForTraining";
    private TextView textViewIntervalSetting, textViewIntervalDays;
    private Button buttonSetIntervalDone, buttonNoInterval;
    private EditText editTextTypeInYourInterval;
    private RadioGroup radioGroupInterval;
    private Button buttonChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_interval_for_training);
        initWidgets();
        setOnClickListener();
    }
    public void initWidgets()
    {
        buttonNoInterval = (Button)findViewById(R.id.buttonNoInterval);
        buttonSetIntervalDone = (Button)findViewById(R.id.buttonSetIntervalDone);
        textViewIntervalDays = (TextView) findViewById(R.id.textViewIntervalDays);
        textViewIntervalSetting = (TextView) findViewById(R.id.textViewIntervalSetting);
        editTextTypeInYourInterval = (EditText) findViewById(R.id.editTextTypeInYourInterval);
        radioGroupInterval = (RadioGroup) findViewById(R.id.radioGroupInterval);
    }
    public void checkButton(View v)
    {
        int radioId = radioGroupInterval.getCheckedRadioButtonId();

        buttonChecked = findViewById(radioId);
    }
    public void setOnClickListener()
    {
        buttonSetIntervalDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetIntervalForTraining.this,MainWindowActivity.class);

                checkButton(v);
                try {
                    if(buttonChecked.getId() == R.id.radioButtonOther)
                    {
                        if(Integer.parseInt(editTextTypeInYourInterval.getText().toString()) > 0 && Integer.parseInt(editTextTypeInYourInterval.getText().toString()) <= 60)
                        {
                            Toast.makeText(v.getContext(),getString(R.string.ToastIntervalChosenSuccesfully1) + " " + editTextTypeInYourInterval.getText().toString() + " " + getString(R.string.ToastIntervalChosenSuccesfully2),Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(v.getContext(),getString(R.string.ToastValueIncorrect),Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                    else
                    {
                        Toast.makeText(v.getContext(),getString(R.string.ToastIntervalChosenSuccesfully1) + " " + buttonChecked.getText().charAt(0) + " " + getString(R.string.ToastIntervalChosenSuccesfully2),Toast.LENGTH_SHORT).show();
                    }

                    startActivity(intent);
                }
                catch (NullPointerException e)
                {
                    Log.d(TAG, "setOnclickListener: OnClick: " + e);
                    Toast.makeText(v.getContext(), getString(R.string.ToastNoIntervalChosen), Toast.LENGTH_SHORT).show();
                }
                catch (NumberFormatException e)
                {
                    Log.d(TAG, "setOnclickListener: OnClick: " + e);
                    Toast.makeText(v.getContext(),getString(R.string.ToastValueIncorrect),Toast.LENGTH_SHORT).show();
                }


            }
        });
        buttonNoInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetIntervalForTraining.this,MainWindowActivity.class);
                Toast.makeText(v.getContext(),getString(R.string.ToastNoIntervalChosen),Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

}
