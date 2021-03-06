package org.wmii.endorfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.wmii.endorfit.R;
import org.wmii.endorfit.DataClasses.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTxtEmail, editTxtPassword;
    ProgressBar progressBar;

    Button btnSignUp, btnGoToLogin;

    FirebaseDatabase database;

    public FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeObjects();

        editTxtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    registerUser();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGotoLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btnSignUp:
                registerUser();
                break;
        }
    }

    private void initializeObjects() {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTxtEmail = findViewById(R.id.editTxtEmail);
        editTxtPassword = findViewById(R.id.editTxtPassword);
        progressBar = findViewById(R.id.progressBar);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnGoToLogin = findViewById(R.id.btnGotoLogin);

        btnSignUp.setOnClickListener(this);
        btnGoToLogin.setOnClickListener(this);

    }

    private void registerUser() {
        final String email = editTxtEmail.getText().toString().trim();
        final String password = editTxtPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTxtEmail.setError("Email is required");
            editTxtPassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTxtEmail.setError("Please enter a valid email");
            editTxtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTxtPassword.setError("Password is required");
            editTxtPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTxtPassword.setError("Minimum length of password should be 6");
            editTxtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, "User created", Toast.LENGTH_SHORT).show();
                addUserToDatabase(email);
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUserToDatabase(String email) {
        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference usersRef = database.getReference("users/" + currentUserId);
        User newUser = new User("None", "Other", 0, 0.0, 0.0);
        usersRef.setValue(newUser);
    }

}