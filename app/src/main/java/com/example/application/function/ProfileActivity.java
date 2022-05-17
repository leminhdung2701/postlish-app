package com.example.application.function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.application.R;
import com.example.application.adapter.HomePostRecyclerAdapter;
import com.example.application.adapter.RecyclerViewAdapter;
import com.example.application.postmanage.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    List<Post> postList;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private CircleImageView imageProfile;
    private ImageView options;
    private TextView followers;
    private TextView following;
    private TextView posts;
    private TextView username;
    private Button editProfile;
    private FirebaseUser fUser;


    private ImageView myPictures;
    private ImageView savedPictures;

    String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        options = findViewById(R.id.options);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        posts=findViewById(R.id.posts);
        username=findViewById(R.id.username);
        imageProfile=findViewById(R.id.image_profile);
        editProfile = findViewById(R.id.edit_profile);

        //
        String userName,userPhoto,profileID;
        //
        Intent intent = getIntent();
        profileID = intent.getExtras().getString("userId");
        userName = intent.getExtras().getString("nameUser");
        userPhoto = intent.getExtras().getString("photoUser");

        //
        profileId = profileID;
        username.setText(userName);

        Glide.with(this).load(userPhoto).into(imageProfile);

        //
        getPostCount();
        getFollowersAndFollowingCount();
        getPostCount();
       //

        if (profileId.equals(fUser.getUid())) {
            editProfile.setText("Chỉnh sửa trang cá nhân");
        } else {
            checkFollowingStatus();
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = editProfile.getText().toString();

                if (btnText.equals("Chỉnh sửa trang cá nhân")) {
                } else {
                    if (btnText.equals("Theo dõi")) {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                                .child("following").child(profileId).setValue(true);

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                                .child("followers").child(fUser.getUid()).setValue(true);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                                .child("following").child(profileId).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                                .child("followers").child(fUser.getUid()).removeValue();
                    }
                }
            }
        });
        //
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FollowersActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("title", "Người theo dõi");
                startActivity(intent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FollowersActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("title", "Người đang theo dõi");
                startActivity(intent);
            }
        });

        //Create list
        postList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = database.getReference("Posts");
        mDatabase.orderByChild("userId").equalTo(profileID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    postList.add(post);


                }
                Collections.reverse(postList);
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview_id);
                HomePostRecyclerAdapter myAdapter = new HomePostRecyclerAdapter(ProfileActivity.this, postList);
                recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this,RecyclerView.VERTICAL,false));
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //
    private void getPostCount() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getUserId().equals(profileId)) counter ++;
                }

                posts.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkFollowingStatus() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileId).exists()) {
                    editProfile.setText("Đang theo dõi");
                } else {
                    editProfile.setText("Theo dõi");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getFollowersAndFollowingCount() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId);

        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}