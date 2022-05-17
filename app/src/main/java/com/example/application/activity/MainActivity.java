package com.example.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.function.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CountDownTimer(500,250) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                if(user == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference("users_category");
                    mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(MainActivity.this, Categorychose.class);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }
        }.start();
    }
}