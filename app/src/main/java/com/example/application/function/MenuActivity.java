package com.example.application.function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.application.R;
import com.example.application.account.User;
import com.example.application.activity.AccountActivity;
import com.example.application.activity.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {

    private User user1 = new User();
    private DatabaseReference mDatabase;
    LinearLayout linearLayout_header;
    ImageView imageView_header;
    ImageView img_avatar;
    TextView textView_name;
    TextView textView_gmail;
    FirebaseAuth mAuth;
    Button logoutAccount;
    CardView notification,saved,premium,setting,language,wiget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_menu);

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
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_publish:
                        startActivity(new Intent(getApplicationContext(),PublishActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_menu:
                        break;
                }
                return false;
            }
        });


        linearLayout_header = findViewById(R.id.linear_click);
        imageView_header =findViewById(R.id.image_menu_header);
        textView_name=findViewById(R.id.textview_name);
        textView_gmail=findViewById(R.id.textview_gmail);
        img_avatar=findViewById(R.id.img_avatar);
        logoutAccount=findViewById(R.id.button_logout);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        if(user != null) {
            mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user1 = dataSnapshot.getValue(User.class);

                    String name = user1.getName();
                    textView_name.setText(name);

                    if (user1.getGmail() != null) {
                        textView_gmail.setText(user1.getGmail());
                    }

                    if (user1.getPhotoUrl() != null) {
                        String photoURL = user1.getPhotoUrl().toString();
                        Glide.with(MenuActivity.this).load(photoURL).into(img_avatar);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
        // onclick avatar to go ProfileActivity
        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userCurrent= snapshot.getValue(User.class);
                img_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                        intent.putExtra("userId",userCurrent.getId());
                        intent.putExtra("nameUser",userCurrent.getName());
                        intent.putExtra("photoUser",userCurrent.getPhotoUrl());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // onclick premium to go PremiumActivity
        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userCurrent= snapshot.getValue(User.class);
                premium.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuActivity.this, PremiumActivity.class);
                        intent.putExtra("userId",userCurrent.getId());
                        intent.putExtra("nameUser",userCurrent.getName());
                        intent.putExtra("gmail",userCurrent.getGmail());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // onclick linear  to go AccountActivity
        linearLayout_header.setClickable(true);
        linearLayout_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, AccountActivity.class));
            }
        });
        logoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                mAuth.signOut();
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(MenuActivity.this,"Đăng xuất thành công",Toast.LENGTH_SHORT).show();
            }
        });

        notification =findViewById(R.id.menu_notification);
        premium=findViewById(R.id.menu_premium);
        saved=findViewById(R.id.menu_saved);
        setting=findViewById(R.id.setting);
        language=findViewById(R.id.language);
        wiget=findViewById(R.id.wiget);

        //
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, NotificationActivity.class));
            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, LibraryActivity.class));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, SettingActivity.class));
            }
        });
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, LanguageActivity.class));
            }
        });
        wiget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, WigetsActivity.class));
            }
        });
    }
}
