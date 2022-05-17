package com.example.application.function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.application.R;
import com.example.application.adapter.SearchAdapter;
import com.example.application.model.AllPost;
import com.example.application.model.UserData;
import com.example.application.postmanage.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView postRecycler;
    SearchAdapter searchAdapter;
    EditText searchView;
    CharSequence search="";
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_library:
                        startActivity(new Intent(getApplicationContext(),LibraryActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_search:
                        break;
                    case R.id.nav_publish:
                        startActivity(new Intent(getApplicationContext(),PublishActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_menu:
                        startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });


        searchView = findViewById(R.id.search_bar);

        List<Post> postDataList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Posts");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    postDataList.add(post);
                }
                setPostRecycler(postDataList);

                //

                searchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        searchAdapter.getFilter().filter(charSequence);
                        search = charSequence;
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                //


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private  void  setPostRecycler(List<Post> postDataList){
        postRecycler = findViewById(R.id.userRecycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        postRecycler.setLayoutManager(layoutManager);
        searchAdapter = new SearchAdapter(SearchActivity.this,postDataList);
        postRecycler.setAdapter(searchAdapter);
    }
}