package com.example.application.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application.R;
import com.example.application.adapter.RecyclerViewAdapter;
import com.example.application.postmanage.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PublishFragment extends Fragment {

    List<Post> postList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    public static PublishFragment getInstance() {
        PublishFragment publishFragment =new PublishFragment();
        return publishFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_publish, container, false);

        //Create list
        postList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase1 = database.getReference("Posts");
        mDatabase1.orderByChild("userId").equalTo(mAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    postList.add(post);


                }
                RecyclerView myrv = (RecyclerView) view.findViewById(R.id.recyclerview_id);
                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(view.getContext(), postList);
                myrv.setLayoutManager(new GridLayoutManager(view.getContext(),3));
                myrv.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }
}