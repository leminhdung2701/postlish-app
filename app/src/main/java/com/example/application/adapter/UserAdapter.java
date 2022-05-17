package com.example.application.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.account.User;

import com.example.application.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUsers;


    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item , parent , false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);
        holder.btnFollow.setVisibility(View.VISIBLE);

        holder.username.setText(user.getName());
        holder.date.setText(user.getDate());

        Picasso.get().load(user.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);

        isFollowed(user.getId() , holder.btnFollow);

        if (user.getId().equals(firebaseUser.getUid())){
            holder.btnFollow.setVisibility(View.GONE);
        }


        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnFollow.getText().toString().equals(("Theo dõi"))){
                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child((firebaseUser.getUid())).child("following").child(user.getId()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child(user.getId()).child("followers").child(firebaseUser.getUid()).setValue(true);

                    addNotification(user.getId());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child((firebaseUser.getUid())).child("following").child(user.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child(user.getId()).child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });




    }

    private void isFollowed(final String id, final Button btnFollow) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                .child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(id).exists())
                    btnFollow.setText("Đang theo dõi");
                else
                    btnFollow.setText("Theo dõi");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView username;
        public TextView date;
        public Button btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            date = itemView.findViewById(R.id.fullname);
            btnFollow = itemView.findViewById(R.id.btn_follow);
        }
    }

    private void addNotification(String userId) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userid", userId);
        map.put("text", "đã bắt đầu theo dõi bạn.");
        map.put("postid", "");
        map.put("isPost", false);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(map);
    }

}
