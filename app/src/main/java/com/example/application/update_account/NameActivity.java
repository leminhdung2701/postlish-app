package com.example.application.update_account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.activity.AccountActivity;
import com.example.application.postmanage.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NameActivity extends AppCompatActivity {

    Button back,save;
    EditText editText;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    FirebaseAuth mAuth;
    FirebaseUser user;
    //User user1 = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        Anhxa();
        String edt = editText.getText().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase1 = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

          /*  mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user1 = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
            String dateuser = user1.getDate().toString();
            editText.setText(dateuser); */


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change();
                NameActivity.super.onBackPressed();
            }
        });
    }

    public void change(){
        String edt = editText.getText().toString();
        mDatabase.child("users").child(user.getUid()).child("name").setValue(edt, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error == null){
                    Toast.makeText(NameActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(NameActivity.this,"Xảy ra lỗi",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDatabase.child("Posts").orderByChild("userId")
                .equalTo(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                            String key = postsnap.getKey();


                            //
                            mDatabase1.child("Posts").child(key)
                                    .child("userName")
                                    .setValue(edt);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void Anhxa() {
        back = findViewById(R.id.btn_name_back);
        save = findViewById(R.id.btn_name_save);
        editText=findViewById(R.id.edt_name);
    }
}