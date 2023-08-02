package com.healthcareapp.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.healthcareapp.app.Model.User;

import java.util.HashMap;
import java.util.Map;

public class NoteDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    NavigationView navigationView;
    TextView profile_name_header;
    ImageView profile_image_header;

    DatabaseReference reference;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    EditText detailsTitle;
    EditText contentDetailsNote;
    FloatingActionButton saveNoteBttn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        profile_name_header = hView.findViewById(R.id.profile_name_header);
        profile_image_header = hView.findViewById(R.id.profile_image_header);

        detailsTitle = findViewById(R.id.detailsTitle);
        contentDetailsNote = findViewById(R.id.contentDetailsNote);
        saveNoteBttn = findViewById(R.id.saveNoteBttn);
        Intent data = getIntent();
        detailsTitle.setText(data.getStringExtra("title"));
        contentDetailsNote.setText(data.getStringExtra("content"));


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = FirebaseDatabase.getInstance("https://healthcare-app-bb72e-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(firebaseUser.getUid());
        saveNoteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = detailsTitle.getText().toString();
                String newContent = contentDetailsNote.getText().toString();

                if(newTitle.isEmpty() ||newContent.isEmpty() ){
                    Toast.makeText(NoteDetailsActivity.this, "Please fill both fields", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String, Object> note = new HashMap<>();
                    note.put("noteTitle",newTitle);
                    note.put("noteContent", newContent);
                    Intent intent = new Intent(NoteDetailsActivity.this, NotesActivity.class);
                    startActivity(intent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(NoteDetailsActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_nav,R.string.close_nav);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle.syncState();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                profile_name_header.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image_header.setImageResource(R.mipmap.nav_profile);
                }else{

                    Glide.with(NoteDetailsActivity.this).load(user.getImageURL()).into(profile_image_header);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                Intent intentLogout = new Intent(NoteDetailsActivity.this, StartActivity.class);
                startActivity(intentLogout);
                finish();
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about_button:
                Intent intentAbout = new Intent(NoteDetailsActivity.this, AboutActivity.class);
                startActivity(intentAbout);
                finish();
                break;
            case R.id.nav_home_button:
                Intent intentHome = new Intent(NoteDetailsActivity.this, MainActivity.class);
                startActivity(intentHome);
                finish();
                break;
            case R.id.nav_profile_button:
                Intent intentProfile = new Intent(NoteDetailsActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                finish();
                break;case R.id.nav_exercise_button:
                Intent intentExercise = new Intent(NoteDetailsActivity.this, ExerciseActivity.class);
                startActivity(intentExercise);
                finish();
                break;
            case R.id.nav_settings_button:
                Intent intentSettings = new Intent(NoteDetailsActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                finish();
                break;
            case R.id.nav_diets_button:
                Intent intentDiet = new Intent(NoteDetailsActivity.this, DietActivity.class);
                startActivity(intentDiet);
                finish();
                break;
            case R.id.nav_notes_button:
                Intent intentNotes = new Intent(NoteDetailsActivity.this, NotesActivity.class);
                startActivity(intentNotes);
                finish();
                break;
        }
        return true;
    }

}