package com.healthcareapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText inputUsername,inputEmail,inputPassword;
    Button bttn_register;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        bttn_register = findViewById(R.id.bttn_register);
        auth = FirebaseAuth.getInstance();

        bttn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = inputUsername.getText().toString();
                String txt_email = inputEmail.getText().toString();
                String txt_password = inputPassword.getText().toString();

                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Please make sure to fill all the fields.", Toast.LENGTH_SHORT).show();

                }else if (txt_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password need to have at least 6 characters", Toast.LENGTH_SHORT).show();

                }else{
                    register(txt_username,txt_email,txt_password);
                }
            }
        });

    }
    private void register (String inputUsername, String inputEmail, String inputPassword){

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance("https://healthcare-app-bb72e-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userid);

                            HashMap <String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",inputUsername);
                            hashMap.put("imageURL","default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    System.out.println(task.isSuccessful());
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "Register Info are Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}