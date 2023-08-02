package com.healthcareapp.app;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.healthcareapp.app.Model.User;
import com.healthcareapp.app.databinding.ActivityAddimageBinding;

import java.util.Objects;

public class AddImageActivity extends AppCompatActivity {

    ActivityAddimageBinding binding;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Button selectBttn;
    Button uploadBttn;
    Button cancelBttn;
    ImageView imageSelect;
    Uri imageURI;

    FirebaseUser firebaseUser;
    String username;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addimage);

        binding = ActivityAddimageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        selectBttn = findViewById(R.id.selectBttn);
        uploadBttn = findViewById(R.id.uploadBttn);
        cancelBttn = findViewById(R.id.cancelBttn);
        imageSelect = findViewById(R.id.imageSelect);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();


        cancelBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAbout = new Intent(AddImageActivity.this, ProfileActivity.class);
                startActivity(intentAbout);
            }
        });
        binding.selectBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        binding.uploadBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();

            }
        });

    }


    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK  && data!=null ){
            imageURI = data.getData();
            imageSelect.setImageURI(imageURI);

        }
    }
    private void uploadImage() {

        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        String fileExtension = mimeTypeMap.getMimeTypeFromExtension(resolver.getType(imageURI));

        StorageReference storage = storageReference.child(System.currentTimeMillis() + "." + fileExtension);
        StorageTask upload = storage.putFile(imageURI);

        upload.continueWithTask(new Continuation() {
            @Override
            public Task<Uri> then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }

                return storage.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String uri = task.getResult().toString();
                    FirebaseDatabase.getInstance("https://healthcare-app-bb72e-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference()
                            .child("Users")
                            .child(firebaseUser.getUid())
                            .child("imageURL")
                            .setValue(uri);

                    Intent intent = new Intent(AddImageActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(AddImageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
