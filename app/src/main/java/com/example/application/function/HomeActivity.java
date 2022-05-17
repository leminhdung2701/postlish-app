package com.example.application.function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.application.adapter.HomeMainRecyclerAdapter;
import com.example.application.model.AllPost;
import com.example.application.R;
import com.example.application.adapter.HomeDocumentAdapter;

import com.example.application.postmanage.Category;
import com.example.application.postmanage.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    HomeDocumentAdapter documentAdapter;
    TabLayout tabLayout, postTab;
    ViewPager documentViewPager;
    List<Post> list_1;
    List<Post>  list_2;
    List<Post>  list_3;
    FirebaseAuth mAuth;
    FirebaseUser user;
    HomeMainRecyclerAdapter mainRecyclerAdapter;
    RecyclerView mainRecycler;
    List<AllPost> allPostList;

    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        tabLayout = findViewById(R.id.tablayout_home_indicator);
        postTab =findViewById(R.id.tablayout_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        break;
                    case R.id.nav_library:
                        startActivity(new Intent(getApplicationContext(),LibraryActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
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

        tabLayout = findViewById(R.id.tablayout_home_indicator);
        postTab =findViewById(R.id.tablayout_home);
        //
        list_1 = new ArrayList<Post>();
        //
        list_2 = new ArrayList<Post>();
        //
        list_3 = new ArrayList<Post>();

        //

        postTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 1:
                        setDocumentAdapter(list_2);;
                        return;
                    case 2:
                        setDocumentAdapter(list_3);
                        return;
                    default:
                        setDocumentAdapter(list_1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //
        List<Post> postList1 =new ArrayList<>();
        List<Post> postList2 =new ArrayList<>();
        List<Post> postList3 =new ArrayList<>();
        List<Post> postList4 =new ArrayList<>();
        List<Post> postList5 =new ArrayList<>();
        allPostList =new ArrayList<>();
        //
        /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Posts");

        mDatabase.addValueEventListener(new ValueEventListener() {*/
        FirebaseDatabase.getInstance().getReference("Posts").limitToLast(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_1.clear();
                list_2.clear();
                list_3.clear();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    //
                    //
                    list_1.add(post);
                    //  list_2.add(post);

                    //  list_3.add(post);
                }
                //
                if(list_1.isEmpty()){

                }else{
                    setDocumentAdapter(list_1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = FirebaseDatabase.getInstance().getReference().child("CountViewAll")
                .orderByChild("count").limitToLast(5);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_2.clear();
                for (DataSnapshot a : snapshot.getChildren()) {
                    String key = a.getKey();
                    // Toast.makeText(HomeActivity.this, key, Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("Posts").child(key)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // your data here
                                    //  Toast.makeText(HomeActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                                    list_2.add(snapshot.getValue(Post.class));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
                Collections.reverse(list_2);
               /* for(int i=0;i<list_2.size();i++) {
                    Toast.makeText(HomeActivity.this, list_2.get(i).getUserName(), Toast.LENGTH_SHORT).show();

                }*/


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference("users_category").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Category a = dataSnapshot.getValue(Category.class);
                    String category[] = new String[3];
                    category[0] = a.getCategory1();
                    category[1] = a.getCategory2();
                    category[2] = a.getCategory3();
                    FirebaseDatabase.getInstance().getReference("Posts").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            postList1.clear();
                            postList2.clear();
                            postList3.clear();
                            postList4.clear();
                            allPostList.clear();
                            for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                                Post post = postsnap.getValue(Post.class);
                                //
                                postList1.add(post);
                                // postList2.add(post);
                                //postList3.add(post);
                                // postList4.add(post);
                                //
                            }
                            Collections.reverse(postList1);
                            for (Post a : postList1) {
                                if (a.getCategory_post().equals(category[0])) postList2.add(a);
                                if (a.getCategory_post().equals(category[1])) postList3.add(a);
                                if (a.getCategory_post().equals(category[2])) postList4.add(a);
                            }
                            allPostList.add(new AllPost(1, "Các bài viết", postList1));
                            allPostList.add(new AllPost(2, category[0], postList2));
                            allPostList.add(new AllPost(3, category[1], postList3));
                            allPostList.add(new AllPost(4, category[2], postList4));
                            allPostList.add(new AllPost(5, "Xem gần đây", postList5));
                            setMainRecycler(allPostList);
                /*
                String size = String.valueOf(postList1.size());
                Toast.makeText(HomeActivity.this,size,Toast.LENGTH_SHORT).show();*/


                            // homeAdapter = new HomeAdapter(HomeActivity.this,postDataList);
                            //postRecycler.setAdapter(homeAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            //   Toast.makeText(HomeActivity.this,category[0],Toast.LENGTH_SHORT).show();
            //   Toast.makeText(HomeActivity.this,category[1],Toast.LENGTH_SHORT).show();
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Query query1 = FirebaseDatabase.getInstance().getReference().child("HistoryRead").child(user.getUid())
                .orderByChild("timeread").limitToLast(5);

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList5.clear();
                for (DataSnapshot a : snapshot.getChildren()) {
                    String key = a.getKey();
                    //   Toast.makeText(HomeActivity.this, key, Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("Posts").child(key)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // your data here
                                    //  Toast.makeText(HomeActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                                    if(snapshot.exists()){
                                        postList5.add(snapshot.getValue(Post.class));
                                        if(postList5.isEmpty()){

                                        }
                                        else{
                                            if(mainRecyclerAdapter !=null)
                                            mainRecyclerAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
    }


    private void setDocumentAdapter(List<Post> list){
        documentViewPager = findViewById(R.id.viewpager_banner);
        documentAdapter = new HomeDocumentAdapter(this,list);
        documentViewPager.setAdapter(documentAdapter);
        tabLayout.setupWithViewPager(documentViewPager);

        Timer sliderTimer =new Timer();
        sliderTimer.scheduleAtFixedRate(new AutoSlider(),4000,6000);
        tabLayout.setupWithViewPager(documentViewPager,true);

        documentAdapter.notifyDataSetChanged();
    }

    class AutoSlider extends TimerTask {

        @Override
        public void run() {
            HomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(documentViewPager.getCurrentItem()<list_1.size()-1){
                        documentViewPager.setCurrentItem(documentViewPager.getCurrentItem()+1);

                    }else{
                        documentViewPager.setCurrentItem(0);
                    }
                }
            });
        }


    }

    public void setMainRecycler(List<AllPost> allPostList){

        mainRecycler=findViewById(R.id.recycler_main);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        mainRecycler.setLayoutManager(layoutManager);
        mainRecyclerAdapter = new HomeMainRecyclerAdapter(this, allPostList);
        mainRecycler.setAdapter(mainRecyclerAdapter);

        mainRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.nav_notification){
            Intent intent = new Intent(HomeActivity.this,NotificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}