package com.healthcareapp.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNoteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    FloatingActionButton saveNoteBttn;
    EditText createTitle,contentCreateNote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnote);

        saveNoteBttn = findViewById(R.id.saveNoteBttn);
        createTitle = findViewById(R.id.createTitle);
        contentCreateNote = findViewById(R.id.contentCreateNote);

        Toolbar toolbar = findViewById(R.id.toolbarCreateNote);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        saveNoteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteTitle = createTitle.getText().toString();
                String noteContent = contentCreateNote.getText().toString();
                if (noteTitle.isEmpty() || noteContent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "To create a new note please fill both fields.",Toast.LENGTH_SHORT).show();
                }else{
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String , Object> note = new HashMap<>();
                    note.put("noteTitle",noteTitle);
                    note.put("noteContent", noteContent);
                     documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                             Toast.makeText(getApplicationContext(), "Note Created", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(CreateNoteActivity.this,NotesActivity.class));
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(getApplicationContext(), "Note Creation Failed", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(CreateNoteActivity.this,NotesActivity.class));
                         }
                     });


                }

                
                
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout_button:
                firebaseAuth.signOut();
                Intent intentLogout = new Intent(CreateNoteActivity.this, StartActivity.class);
                startActivity(intentLogout);
                finish();
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about_button:
                Intent intentAbout = new Intent(CreateNoteActivity.this, AboutActivity.class);
                startActivity(intentAbout);
                finish();
                break;
            case R.id.nav_home_button:
                Intent intentHome = new Intent(CreateNoteActivity.this, MainActivity.class);
                startActivity(intentHome);
                finish();
                break;
            case R.id.nav_profile_button:
                Intent intentProfile = new Intent(CreateNoteActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                finish();
                break;case R.id.nav_exercise_button:
                Intent intentExercise = new Intent(CreateNoteActivity.this, ExerciseActivity.class);
                startActivity(intentExercise);
                finish();
                break;
            case R.id.nav_settings_button:
                Intent intentSettings = new Intent(CreateNoteActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                finish();
                break;
            case R.id.nav_diets_button:
                Intent intentDiet = new Intent(CreateNoteActivity.this, DietActivity.class);
                startActivity(intentDiet);
                finish();
                break;
            case R.id.nav_notes_button:
                Intent intentNotes = new Intent(CreateNoteActivity.this, NotesActivity.class);
                startActivity(intentNotes);
                finish();
                break;

        }
        return true;
    }

}