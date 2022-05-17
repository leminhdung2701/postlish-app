package com.example.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_passwordAcitivy extends AppCompatActivity {
    private FirebaseAuth auth;
    EditText edt_search;
    Button btn_return;
    Button btn_reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        AnhXa();
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forget_passwordAcitivy.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPass();
            }
        });
    }
    private void ResetPass(){
        String email = edt_search.getText().toString().trim();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Reset password instructions has sent to your email",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Forget_passwordAcitivy.this,LoginActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "Email don't exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AnhXa(){
        edt_search=(EditText) findViewById(R.id.edt_forget_search);
        btn_return=(Button) findViewById(R.id.btn_forget_return);
        btn_reset = (Button) findViewById(R.id.rsPass);
    }
}