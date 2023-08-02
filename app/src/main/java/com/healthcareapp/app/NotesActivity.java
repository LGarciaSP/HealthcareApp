package com.healthcareapp.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.Query;
import com.healthcareapp.app.Model.FirebaseModel;
import com.healthcareapp.app.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    NavigationView navigationView;
    TextView profile_name_header;
    ImageView profile_image_header;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<FirebaseModel,NoteViewHolder> noteAdapter;

    RecyclerView noteRecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FloatingActionButton createNoteBttn;
    String noteTitle;
    String description;
    long createdTime;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        profile_name_header = hView.findViewById(R.id.profile_name_header);
        profile_image_header = hView.findViewById(R.id.profile_image_header);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://healthcare-app-bb72e-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(firebaseUser.getUid());

        firebaseFirestore = FirebaseFirestore.getInstance();

        createNoteBttn = findViewById(R.id.createNoteBttn);

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

                    Glide.with(NotesActivity.this).load(user.getImageURL()).into(profile_image_header);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        createNoteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesActivity.this, CreateNoteActivity.class));
            }
        });

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("noteTitle",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<FirebaseModel> allNotes = new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query,FirebaseModel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(allNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull FirebaseModel model) {

//                int colourcode = getRandomColour();
//                holder.lnote.setBackgroundColor(holder.itemView.getResources().getColor(colourcode,null));

                ImageView menupopbutton = holder.itemView.findViewById(R.id.menupopbutton);


                holder.noteTitle.setText(model.getNoteTitle());
                holder.noteContent.setText(model.getNoteContent());


                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(),NoteDetailsActivity.class);
                        intent.putExtra("title", model.getNoteTitle());
                        intent.putExtra("content", model.getNoteContent());
                        intent.putExtra("noteId", docId );

                        view.getContext().startActivity(intent);
                    }
                });

                menupopbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                        popupMenu.setGravity(Gravity.END);

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(NotesActivity.this, "The note has been deleted.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NotesActivity.this, "Failed to delete.", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                return false;
                            }
                        });
                            popupMenu.show();
                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent, false);
                return new NoteViewHolder(view);
            }
        };

        noteRecyclerView = findViewById(R.id.notesRecyclerView);
        noteRecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        noteRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        noteRecyclerView.setAdapter(noteAdapter);

    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView noteTitle;
        private TextView noteContent;
        LinearLayout lnote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            lnote = itemView.findViewById(R.id.note);

        }
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
                Intent intentLogout = new Intent(NotesActivity.this, StartActivity.class);
                startActivity(intentLogout);
                finish();
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about_button:
                Intent intentAbout = new Intent(NotesActivity.this, AboutActivity.class);
                startActivity(intentAbout);
                finish();
                break;
            case R.id.nav_home_button:
                Intent intentHome = new Intent(NotesActivity.this, MainActivity.class);
                startActivity(intentHome);
                finish();
                break;
            case R.id.nav_profile_button:
                Intent intentProfile = new Intent(NotesActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                finish();
                break;case R.id.nav_exercise_button:
                Intent intentExercise = new Intent(NotesActivity.this, ExerciseActivity.class);
                startActivity(intentExercise);
                finish();
                break;
            case R.id.nav_settings_button:
                Intent intentSettings = new Intent(NotesActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                finish();
                break;
            case R.id.nav_diets_button:
                Intent intentDiet = new Intent(NotesActivity.this, DietActivity.class);
                startActivity(intentDiet);
                finish();
                break;
            case R.id.nav_notes_button:
                Intent intentNotes = new Intent(NotesActivity.this, NotesActivity.class);
                startActivity(intentNotes);
                finish();
                break;

        }
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();
        noteAdapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.stopListening();
        }
    }

    private int getRandomColour(){

        List<Integer> colourcode = new ArrayList<>();
        colourcode.add(R.color.pastel_blue);
        colourcode.add(R.color.pastel_green);
        colourcode.add(R.color.pastel_red);
        colourcode.add(R.color.pastel_orange);
        colourcode.add(R.color.pastel_yellow);

        Random random = new Random();
        int rnumber = random.nextInt(colourcode.size());

        return rnumber;
    }


}