package com.example.application.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.application.R;
import com.example.application.adapter.RecyclerViewAdapter;
import com.example.application.model.AllPost;
import com.example.application.postmanage.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SavedFragment extends Fragment {

    List<Post> postList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private String post_id;
    public static SavedFragment getInstance() {
        SavedFragment savedFragment = new SavedFragment();
        return savedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_saved, container, false);

        //Create list
        postList = new ArrayList<>();

        //Set up
        /*RecyclerView myrv = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(view.getContext(), postList);
        myrv.setLayoutManager(new GridLayoutManager(view.getContext(),3));
        myrv.setAdapter(myAdapter);*/
        ArrayList<String> save = new ArrayList();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Saves");
        mDatabase1 = database.getReference("Posts");
        mDatabase.child(mAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                save.clear();
               for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    String value = postsnap.getKey().toString();
                    //Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
                    //int xoa = value.indexOf("=");
                    // post_id = value.substring(1, xoa);
                    //  Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
                    save.add(value);
                }

              //  Toast.makeText(getContext(),post_id,Toast.LENGTH_SHORT).show();
                mDatabase1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                        for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                            Post post = postsnap.getValue(Post.class);
                            for(int i=0;i<save.size();i++){
                                if(post.getTimeStamp().toString().equals(save.get(i))) postList.add(post);
                            }

                          //  Toast.makeText(getContext(),save.get(0),Toast.LENGTH_SHORT).show();
                            //

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //
        return view;
    }

}