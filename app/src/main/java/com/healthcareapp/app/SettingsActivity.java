package com.healthcareapp.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthcareapp.app.Model.User;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    NavigationView navigationView;
    TextView profile_name_header;
    ImageView profile_image_header;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        profile_name_header = hView.findViewById(R.id.profile_name_header);
        profile_image_header = hView.findViewById(R.id.profile_image_header);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance("https://healthcare-app-bb72e-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(firebaseUser.getUid());

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

                    Glide.with(SettingsActivity.this).load(user.getImageURL()).into(profile_image_header);
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
                Intent intentLogout = new Intent(SettingsActivity.this, StartActivity.class);
                startActivity(intentLogout);
                finish();
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about_button:
                Intent intentAbout = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intentAbout);
                finish();
                break;
            case R.id.nav_home_button:
                Intent intentHome = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intentHome);
                finish();
                break;
            case R.id.nav_profile_button:
                Intent intentProfile = new Intent(SettingsActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                finish();
                break;case R.id.nav_exercise_button:
                Intent intentExercise = new Intent(SettingsActivity.this, ExerciseActivity.class);
                startActivity(intentExercise);
                finish();
                break;
            case R.id.nav_settings_button:
                Intent intentSettings = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                finish();
                break;
            case R.id.nav_diets_button:
                Intent intentDiet = new Intent(SettingsActivity.this, DietActivity.class);
                startActivity(intentDiet);
                finish();
                break;
            case R.id.nav_notes_button:
                Intent intentNotes = new Intent(SettingsActivity.this, NotesActivity.class);
                startActivity(intentNotes);
                finish();
                break;
        }
        return true;
    }

}