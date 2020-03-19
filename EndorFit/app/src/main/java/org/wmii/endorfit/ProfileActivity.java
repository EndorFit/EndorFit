package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import android.app.ProgressDialog;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProfileActivity";
    private static final int REQUEST_CODE = 420;
    public  Uri uriProfileImage;

    EditText editTxtName, editTxtAge, editTxtHeight, editTxtWeight;
    Button btnEdit, btnLogout, btnSave, btnCancel;
    ImageView imageViewProfileImage;
    Spinner spinnerGender;

    FirebaseDatabase database;
    DatabaseReference databaseRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Initialize all items
        editTxtName = findViewById(R.id.editTxtName);
        editTxtAge = findViewById(R.id.editTxtAge);
        editTxtHeight = findViewById(R.id.editTxtHeight);
        editTxtWeight = findViewById(R.id.editTxtWeight);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerGender.setEnabled(false);
        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        imageViewProfileImage = findViewById(R.id.imageViewProfileImage);
        //Spinner config
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.genders, R.layout.spinner_item);
        spinnerGender.setAdapter(adapter);
        //SetOnClickListener for clickable items
        btnEdit.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        //Initialize firebase items
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();
        storageRef = FirebaseStorage.getInstance().getReference("profileImages/" + user.getUid());
        //This listener will execute whenever database changes
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    showData(dataSnapshot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEdit:
                editInformation();
                break;
            case R.id.btnSave:
                updateInformation();
                break;
            case R.id.btnLogout:
                logout();
                break;
            case R.id.btnCancel:
                cancelEditing();
                break;
            case R.id.imageViewProfileImage:
                fileChooser();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void showData(DataSnapshot dataSnapshot) throws IOException {
        String userId = user.getUid();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String name = Objects.requireNonNull(ds.child(userId).getValue(User.class)).getName();
            String gender = Objects.requireNonNull(ds.child(userId).getValue(User.class)).getGender();
            Integer age = Objects.requireNonNull(ds.child(userId).getValue(User.class)).getAge();
            Double height = Objects.requireNonNull(ds.child(userId).getValue(User.class)).getHeight();
            Double weight = Objects.requireNonNull(ds.child(userId).getValue(User.class)).getWeight();

            int genderPosition = 0;
            switch (gender){
                case "Female":
                    genderPosition = 0;
                    break;
                case "Male":
                    genderPosition = 1;
                    break;
                case "Other":
                    genderPosition = 2;
                    break;
            }

            editTxtName.setText(name);
            spinnerGender.setSelection(genderPosition);
            editTxtAge.setText(age.toString());
            editTxtHeight.setText(height.toString());
            editTxtWeight.setText(weight.toString());
            updateImageView();
            showInformation();
        }
    }

    private void updateImageView() throws IOException {
        final File imageTemp = File.createTempFile(user.getUid(),"jpeg");
        storageRef.getFile(imageTemp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageTemp.getPath());
                imageViewProfileImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Updating photo failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select profile photo..."), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                imageViewProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cancelEditing() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    showData(dataSnapshot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(this, "SignOut successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateInformation() {

        String name = editTxtName.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        int age = Integer.parseInt(editTxtAge.getText().toString());
        double height = Double.parseDouble(editTxtHeight.getText().toString());
        double weight = Double.parseDouble(editTxtWeight.getText().toString());

        if(name.isEmpty()) {
            editTxtName.setError("Name cannot be empty");
            editTxtName.requestFocus();
            return;
        }
        if(age < 0 || age > 150){
            editTxtAge.setError("Between 0 and 150");
            editTxtAge.requestFocus();
            return;
        }
        if(height < 50.0 || height > 250.0){
            editTxtHeight.setError("Between 0 and 250");
            editTxtHeight.requestFocus();
            return;
        }
        if(weight < 10.0 || weight > 1000.0){
            editTxtWeight.setError("Between 0 and 1000");
            editTxtWeight.requestFocus();
            return;
        }

        uploadImage();

        User editedUser = new User(name,gender,age,height,weight);
        DatabaseReference userRef = database.getReference("users/" + user.getUid());
        userRef.setValue(editedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                Toast.makeText(ProfileActivity.this, "Show Data", Toast.LENGTH_SHORT).show();
                                showData(dataSnapshot);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadImage() {
        if (uriProfileImage != null) {
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            // adding listeners on upload
            // or failure of image
            storageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(
                                UploadTask.TaskSnapshot taskSnapshot)
                        {
                            // Image uploaded successfully
                            // Dismiss dialog
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT) .show();
                }}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });
        }
    }




    private void showInformation() throws IOException {
        btnEdit.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);

        editTxtName.setFocusableInTouchMode(false);
        editTxtName.setFocusable(false);
        spinnerGender.setEnabled(false);
        editTxtAge.setFocusableInTouchMode(false);
        editTxtAge.setFocusable(false);
        editTxtHeight.setFocusableInTouchMode(false);
        editTxtHeight.setFocusable(false);
        editTxtWeight.setFocusableInTouchMode(false);
        editTxtWeight.setFocusable(false);

        imageViewProfileImage.setOnClickListener(null);
    }


    private void editInformation() {
        btnEdit.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        editTxtName.setFocusableInTouchMode(true);
        editTxtName.setFocusable(true);
        spinnerGender.setEnabled(true);
        editTxtAge.setFocusableInTouchMode(true);
        editTxtAge.setFocusable(true);
        editTxtHeight.setFocusableInTouchMode(true);
        editTxtHeight.setFocusable(true);
        editTxtWeight.setFocusableInTouchMode(true);
        editTxtWeight.setFocusable(true);

        imageViewProfileImage.setOnClickListener(this);
    }

}