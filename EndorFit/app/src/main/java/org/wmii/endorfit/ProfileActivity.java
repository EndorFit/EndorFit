package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 420;

    public  Uri uriProfileImage;
    public Boolean isImageChanged;
    public String nameBeforeEdit, genderBeforeEdit, nameAfterEdit, genderAfterEdit;
    public int ageBeforeEdit, ageAfterEdit;
    public double heightBeforeEdit, weightBeforeEdit, heightAfterEdit ,weightAfterEdit;
    Bitmap imageBeforeEdit, imageAfterEdit;

    EditText editTxtName, editTxtAge, editTxtHeight, editTxtWeight;
    Button btnEdit, btnLogout, btnSave, btnDelete;
    ImageView imageViewProfileImage;
    Spinner spinnerGender;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference databaseRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private StorageReference storageRef;

    @Override
    public void onBackPressed() {
        if(editTxtName.isFocusable()){
            cancelEditing();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        isImageChanged = true;
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
        btnDelete = findViewById(R.id.btnDelete);
        imageViewProfileImage = findViewById(R.id.imageViewProfileImage);
        progressBar = findViewById(R.id.progressBar);
        //Spinner config
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.genders, R.layout.spinner_item_30dp);
        spinnerGender.setAdapter(adapter);
        //SetOnClickListener for clickable items
        btnEdit.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        //Initialize firebase items
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();
        storageRef = FirebaseStorage.getInstance().getReference("profileImages/" + user.getUid());

        //This listener will execute whenever database changes


        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user == null) {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        callListenerForSingleEvent(databaseRef);

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
                mAuth.signOut();
                break;
            case R.id.btnDelete:
                deleteAccount();
                break;
            case R.id.imageViewProfileImage:
                fileChooser();
                break;
        }
    }

    private void deleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog)
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String uId = user.getUid();
                        final StorageReference uRef = FirebaseStorage.getInstance().getReference("profileImages/" + uId);
                        uRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uRef.delete();
                            }
                        });
                        DatabaseReference uInfoRef = database.getReference("users/" + uId);
                        uInfoRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                user.delete();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ProfileActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showData(DataSnapshot dataSnapshot) throws IOException {
        progressBar.setVisibility(View.VISIBLE);
        String userId = user.getUid();

        String name = dataSnapshot.getValue(User.class).getName();
        String gender = dataSnapshot.getValue(User.class).getGender();
        Integer age = dataSnapshot.getValue(User.class).getAge();
        Double height = dataSnapshot.getValue(User.class).getHeight();
        Double weight = dataSnapshot.getValue(User.class).getWeight();

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
            if(isImageChanged) {
                updateImageView();
                isImageChanged = false;
            }
            showInformation();
            progressBar.setVisibility(View.GONE);

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
                imageViewProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon_default));
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
        callListenerForSingleEvent(databaseRef);
    }


    private void updateInformation() {
        if(!getDataAndValid()) {return;}
        User editedUser = new User(nameAfterEdit,genderAfterEdit,ageAfterEdit,heightAfterEdit,weightAfterEdit);
        DatabaseReference userRef = database.getReference("users/" + user.getUid());
        userRef.setValue(editedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    uploadImage();
                } else {
                    Toast.makeText(ProfileActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * @return true if data are validated
     */
    private boolean getDataAndValid() {
        nameAfterEdit = editTxtName.getText().toString();
        genderAfterEdit = spinnerGender.getSelectedItem().toString();
        ageAfterEdit = Integer.parseInt(editTxtAge.getText().toString());
        heightAfterEdit = Double.parseDouble(editTxtHeight.getText().toString());
        weightAfterEdit = Double.parseDouble(editTxtWeight.getText().toString());
        imageAfterEdit = ((BitmapDrawable) imageViewProfileImage.getDrawable()).getBitmap();

        if(nameAfterEdit.isEmpty()) {
            editTxtName.setError("Name cannot be empty");
            editTxtName.requestFocus();
            return false;
        }
        if(ageAfterEdit < 0 || ageAfterEdit > 150){
            editTxtAge.setError("Between 0 and 150");
            editTxtAge.requestFocus();
            return false;
        }
        if(heightAfterEdit < 50.0 || heightAfterEdit > 250.0){
            editTxtHeight.setError("Between 0 and 250");
            editTxtHeight.requestFocus();
            return false;
        }
        if(weightAfterEdit < 10.0 || weightAfterEdit > 1000.0){
            editTxtWeight.setError("Between 0 and 1000");
            editTxtWeight.requestFocus();
            return false;
        }
        isImageChanged = !imageAfterEdit.equals(imageBeforeEdit);

        return true;
    }

    private void uploadImage() {
        if (uriProfileImage != null && isImageChanged) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            storageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            progressDialog.dismiss();
                            callListenerForSingleEvent(databaseRef);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT) .show();
                }}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });
        } else {
            callListenerForSingleEvent(databaseRef);
        }
    }

    private void callListenerForSingleEvent(DatabaseReference databaseRef) {
        databaseRef = database.getReference("users/" + user.getUid());
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
                cancelEditing();
            }
        });
    }

    private void showInformation(){
        btnEdit.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);

        editTxtName.setFocusableInTouchMode(false);
        editTxtName.setFocusable(false);
        spinnerGender.setEnabled(false);
        editTxtAge.setFocusableInTouchMode(false);
        editTxtAge.setFocusable(false);
        editTxtHeight.setFocusableInTouchMode(false);
        editTxtHeight.setFocusable(false);
        editTxtWeight.setFocusableInTouchMode(false);
        editTxtWeight.setFocusable(false);

        spinnerGender.setVisibility(View.VISIBLE);
        imageViewProfileImage.setOnClickListener(null);
    }

    private void editInformation() {
        btnEdit.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);

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
        getDataBeforeEdit();
    }

    private void getDataBeforeEdit() {
        nameBeforeEdit = editTxtName.getText().toString();
        genderBeforeEdit = spinnerGender.getSelectedItem().toString();
        ageBeforeEdit = Integer.parseInt(editTxtAge.getText().toString());
        weightBeforeEdit = Double.parseDouble(editTxtWeight.getText().toString());
        heightBeforeEdit = Double.parseDouble(editTxtHeight.getText().toString());
        imageBeforeEdit = ((BitmapDrawable) imageViewProfileImage.getDrawable()).getBitmap();
    }

}


