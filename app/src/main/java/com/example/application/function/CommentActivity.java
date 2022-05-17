package com.example.application.function;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.application.adapter.CommentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.example.application.object.Comment;
import com.example.application.account.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    private EditText addComment;
    private CircleImageView imageProfile;
    private TextView post;

    private String postId;
    private String authorId;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bình luận");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        authorId = intent.getStringExtra("authorId");
        //
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //
        commentList = new ArrayList<>();
        getComment();


        addComment = findViewById(R.id.add_comment);
        imageProfile = findViewById(R.id.image_profile);
        post = findViewById(R.id.post);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        getUserImage();





        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(addComment.getText().toString())) {
                    Toast.makeText(CommentActivity.this, "No comment added!", Toast.LENGTH_SHORT).show();
                } else {
                    putComment();
                }
            }
        });




    }

    private void getComment() {

        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }


                commentAdapter = new CommentAdapter(CommentActivity.this, commentList, postId);

                recyclerView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void putComment() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
        /*
        HashMap<String, Object> hasMap = new HashMap<>();

        hasMap.put("comments",addComment.getText().toString());
        hasMap.put("publisher",fUser.getUid());

        ref.push().setValue(hasMap);
        addNotification();*/


        String id = ref.push().getKey();
        Comment a = new Comment(fUser.getUid(),id,addComment.getText().toString());
        addNotification();
        addComment.setText("");

        ref.child(id).setValue(a).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CommentActivity.this, "Comment added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getUserImage() {

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                    Picasso.get().load(user.getPhotoUrl()).into(imageProfile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //set Notification
    private void addNotification() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notifications").child(authorId);

        HashMap<String, Object> map = new HashMap<>();

        map.put("userid", fUser.getUid());
        map.put("text", "đã bình luận : "+addComment.getText().toString()+".");
        map.put("postid", postId);
        map.put("isPost", true);

        reference.push().setValue(map);
    }
}
