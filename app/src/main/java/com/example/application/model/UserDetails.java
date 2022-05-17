package com.example.application.model;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class UserDetails extends AppCompatActivity {

    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        userName = findViewById(R.id.user_name);

        String s=getIntent().getStringExtra("username");
        String userdescription=getIntent().getStringExtra("username");

        userName.setText(s + userdescription);

    }
}