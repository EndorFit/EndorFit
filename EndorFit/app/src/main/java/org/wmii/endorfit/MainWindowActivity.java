package org.wmii.endorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainWindowActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageViewLeft, imageViewCenter, imageViewProfile;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        findViewById(R.id.imageViewRightIcon).setOnClickListener(this);
        findViewById(R.id.imageViewCenterIcon).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Toast.makeText(this, "User is not sign in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainWindowActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageViewRightIcon:
                Intent intent = new Intent(MainWindowActivity.this,ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.imageViewCenterIcon:
                Intent intentwork = new Intent(MainWindowActivity.this,workoutPlan.class);
                intentwork.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentwork);
                break;
        }

    }
}
