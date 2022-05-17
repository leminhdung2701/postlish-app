package com.example.application.update_account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class UserActivity extends AppCompatActivity {

    Button btnReturn;
    EditText edtUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnReturn=findViewById(R.id.btn_user);
        edtUser=findViewById(R.id.edt_user);


        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(UserActivity.this, AccountActivity.class);
                startActivity(intent);*/
                onBackPressed();
            }
        });

        edtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtUser.getText().clear();
            }
        });

    }


}