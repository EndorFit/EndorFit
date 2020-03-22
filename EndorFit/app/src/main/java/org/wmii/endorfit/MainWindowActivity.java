package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainWindowActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageViewLeft, imageViewCenter, imageViewProfile;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null) {
            Intent intent = new Intent(MainWindowActivity.this, MainActivity.class);
            startActivity(intent);
        }

        findViewById(R.id.imageViewRightIcon).setOnClickListener(this);
        findViewById(R.id.imageViewCenterIcon).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageViewRightIcon:
                Intent intent = new Intent(MainWindowActivity.this,ProfileActivity.class);
                startActivity(intent);
            case R.id.imageViewCenterIcon:
                Intent intentwork = new Intent(MainWindowActivity.this,workoutPlan.class);
                intentwork.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentwork);
        }

<<<<<<< HEAD
=======
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user == null) {
                    Intent intent = new Intent(MainWindowActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
>>>>>>> 08c580a090fc95c252363bdf84f5e95051e73094

    }
}
