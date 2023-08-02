package com.healthcareapp.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthcareapp.app.Fragments.DiabetesFragment;
import com.healthcareapp.app.Fragments.HeartFragment;
import com.healthcareapp.app.Model.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String dT = "Diabetes" ;
    private String hT = "Heart Conditons";
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
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        profile_name_header = hView.findViewById(R.id.profile_name_header);
        profile_image_header = hView.findViewById(R.id.profile_image_header);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://healthcare-app-bb72e-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(firebaseUser.getUid());

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_nav,R.string.close_nav);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle.syncState();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        SpannableStringBuilder spannableStringBuilderB = new SpannableStringBuilder(dT);
        SpannableStringBuilder spannableStringBuilderR = new SpannableStringBuilder(hT);

        ForegroundColorSpan blue = new ForegroundColorSpan(Color.rgb(102,102,255));
        ForegroundColorSpan red = new ForegroundColorSpan(Color.rgb(255,51,51));

        spannableStringBuilderB.setSpan(blue,
                0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilderR.setSpan(red,0,15,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        viewPagerAdapter.addFragment(new DiabetesFragment(), dT );
        viewPagerAdapter.addFragment(new HeartFragment(), hT);

        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> {


            if (viewPagerAdapter.getPageTitle(position).equals(dT)) {
                tab.setText(spannableStringBuilderB);
            }else if(viewPagerAdapter.getPageTitle(position).equals(hT)){
                tab.setText(spannableStringBuilderR);
            }
            else {
                tab.setText(viewPagerAdapter.getPageTitle(position));
            }
        }).attach();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                profile_name_header.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image_header.setImageResource(R.mipmap.nav_profile);
                }else{

                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image_header);
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
                Intent intentLogout = new Intent(MainActivity.this, StartActivity.class);
                startActivity(intentLogout);
                finish();
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about_button:
                Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentAbout);
                finish();
                break;
            case R.id.nav_home_button:
                Intent intentHome = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intentHome);
                finish();
                break;
            case R.id.nav_profile_button:
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                finish();
                break;
            case R.id.nav_exercise_button:
                Intent intentExercise = new Intent(MainActivity.this, ExerciseActivity.class);
                startActivity(intentExercise);
                finish();
                break;
            case R.id.nav_settings_button:
                Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                finish();
                break;
            case R.id.nav_diets_button:
                Intent intentDiet = new Intent(MainActivity.this, DietActivity.class);
                startActivity(intentDiet);
                finish();
                break;
            case R.id.nav_notes_button:
                Intent intentNotes = new Intent(MainActivity.this, NotesActivity.class);
                startActivity(intentNotes);
                finish();
                break;
        }
        return true;
    }

    static class ViewPagerAdapter extends FragmentStateAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList <String> titles;


        ViewPagerAdapter (FragmentManager fm, Lifecycle lifecycle){
            super(fm, lifecycle);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }
        @Override
        public int getItemCount(){
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }
        public String getPageTitle (int position){
            return titles.get(position);

        }
    }





}