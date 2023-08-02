package com.healthcareapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeUsernameActivity extends AppCompatActivity {

    Button confirm_button;
    EditText change_username_box;
    Button cancelBttn;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        confirm_button = findViewById(R.id.confirm_button);
        cancelBttn = findViewById(R.id.cancelBttn);
        change_username_box = findViewById(R.id.change_username_box);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://healthcare-app-bb72e-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(firebaseUser.getUid());



        cancelBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAbout = new Intent(ChangeUsernameActivity.this, ProfileActivity.class);
                startActivity(intentAbout);
            }
        });
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = change_username_box.getText().toString();

                if (TextUtils.isEmpty(txt_username)){
                    Toast.makeText(ChangeUsernameActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else {
                    reference.child("username").setValue(txt_username);
                    Intent intentAbout = new Intent(ChangeUsernameActivity.this, ProfileActivity.class);
                    startActivity(intentAbout);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }
}