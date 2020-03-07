package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.io.FileInputStream;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTxtEmail, editTxtPassword;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTxtEmail = findViewById(R.id.editTxtEmail);
        editTxtPassword = findViewById(R.id.editTxtPassword);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnGotoLogin).setOnClickListener(this);
        findViewById(R.id.btnSignUp).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private void registerUser() {
        String email = editTxtEmail.getText().toString().trim();
        String password = editTxtPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTxtEmail.setError("Email is required");
            editTxtPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTxtEmail.setError("Please enter a valid email");
            editTxtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTxtPassword.setError("Password is required");
            editTxtPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTxtPassword.setError("Minimum length of password should be 6");
            editTxtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(SignUpActivity.this, MainWindowActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(SignUpActivity.this, "User already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGotoLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btnSignUp:
                registerUser();
                break;

        }
    }

}
