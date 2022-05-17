package com.example.application.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.application.R;
import com.example.application.postmanage.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Categorychose extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    CheckBox chose1,chose2,chose3,chose4,chose5,chose6,chose7,chose8,chose9,chose10,chose11,chose12,chose13;
    Button accept;
    String A[];
    int count=3;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorychose);
         user = mAuth.getInstance().getCurrentUser();
        init();


    }

    //Listener nhận sự kiện khi các Checkbox thay đổi trạng thái
    CompoundButton.OnCheckedChangeListener m_listener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b) {
                Toast.makeText(compoundButton.getContext(),"Bạn chọn "+
                                compoundButton.getText(),
                        Toast.LENGTH_SHORT).show();
                i++;
            }else{
                Toast.makeText(compoundButton.getContext(),"Bạn hủy "+
                                compoundButton.getText(),
                        Toast.LENGTH_SHORT).show();
                i--;
            }
            if(i==count){
                if(chose1.isChecked()==false){
                    chose1.setEnabled(false);
                }
                if(chose2.isChecked()==false){
                    chose2.setEnabled(false);
                }
                if(chose3.isChecked()==false){
                    chose3.setEnabled(false);
                }
                if(chose4.isChecked()==false){
                    chose4.setEnabled(false);
                }
                if(chose5.isChecked()==false){
                    chose5.setEnabled(false);
                }
                if(chose6.isChecked()==false){
                    chose6.setEnabled(false);
                }
                if(chose7.isChecked()==false){
                    chose7.setEnabled(false);
                }
                if(chose8.isChecked()==false){
                    chose8.setEnabled(false);
                }
                if(chose9.isChecked()==false){
                    chose9.setEnabled(false);
                }
                if(chose10.isChecked()==false){
                    chose10.setEnabled(false);
                }
                if(chose11.isChecked()==false){
                    chose11.setEnabled(false);
                }
                if(chose12.isChecked()==false){
                    chose12.setEnabled(false);
                }
                if(chose13.isChecked()==false){
                    chose13.setEnabled(false);
                }
            }
            else{
                chose1.setEnabled(true);
                chose2.setEnabled(true);
                chose3.setEnabled(true);
                chose4.setEnabled(true);
                chose5.setEnabled(true);
                chose6.setEnabled(true);
                chose7.setEnabled(true);
                chose8.setEnabled(true);
                chose9.setEnabled(true);
                chose10.setEnabled(true);
                chose11.setEnabled(true);
                chose12.setEnabled(true);
                chose13.setEnabled(true);
            }
        }
    };


    void init() {
        A=new String[3];
        chose1    = findViewById(R.id.chose1);
        chose2    = findViewById(R.id.chose2);
        chose3    = findViewById(R.id.chose3);
        chose4    = findViewById(R.id.chose4);
        chose5    = findViewById(R.id.chose5);
        chose6    = findViewById(R.id.chose6);
        chose7    = findViewById(R.id.chose7);
        chose8    = findViewById(R.id.chose8);
        chose9    = findViewById(R.id.chose9);
        chose10    = findViewById(R.id.chose10);
        chose11    = findViewById(R.id.chose11);
        chose12    = findViewById(R.id.chose12);
        chose13    = findViewById(R.id.chose13);

        chose1.setOnCheckedChangeListener(m_listener);
        chose2.setOnCheckedChangeListener(m_listener);
        chose3.setOnCheckedChangeListener(m_listener);
        chose4.setOnCheckedChangeListener(m_listener);
        chose5.setOnCheckedChangeListener(m_listener);
        chose6.setOnCheckedChangeListener(m_listener);
        chose7.setOnCheckedChangeListener(m_listener);
        chose8.setOnCheckedChangeListener(m_listener);
        chose9.setOnCheckedChangeListener(m_listener);
        chose10.setOnCheckedChangeListener(m_listener);
        chose11.setOnCheckedChangeListener(m_listener);
        chose12.setOnCheckedChangeListener(m_listener);
        chose13.setOnCheckedChangeListener(m_listener);


        accept = findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=0;
                if(chose1.isChecked()){
                    A[i]=chose1.getText().toString();
                    i++;
                }
                if(chose2.isChecked()){
                    A[i]=chose2.getText().toString();
                    i++;
                }
                if(chose3.isChecked()){
                    A[i]=chose3.getText().toString();
                    i++;
                }
                if(chose4.isChecked()){
                    A[i]=chose4.getText().toString();
                    i++;
                }
                if(chose5.isChecked()){
                    A[i]=chose5.getText().toString();
                    i++;
                }
                if(chose6.isChecked()){
                    A[i]=chose6.getText().toString();
                    i++;
                }
                if(chose7.isChecked()){
                    A[i]=chose7.getText().toString();
                    i++;
                }
                if(chose8.isChecked()){
                    A[i]=chose8.getText().toString();
                    i++;
                }
                if(chose9.isChecked()){
                    A[i]=chose9.getText().toString();
                    i++;
                }
                if(chose10.isChecked()){
                    A[i]=chose10.getText().toString();
                    i++;
                }
                if(chose11.isChecked()){
                    A[i]=chose11.getText().toString();
                    i++;
                }
                if(chose12.isChecked()){
                    A[i]=chose12.getText().toString();
                    i++;
                }
                if(chose13.isChecked()){
                    A[i]=chose13.getText().toString();
                    i++;
                }
                Toast.makeText(Categorychose.this,"Bạn vừa chọn "+A[0]+", "+A[1]+", "+A[2],Toast.LENGTH_SHORT).show();
                Category a = new Category(A[0],A[1],A[2]);
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference("users_category");
                mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                        }
                        else{
                            myRef.child("users_category").child(user.getUid())
                                    .setValue(a).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    // ...
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Write failed
                                            // ...
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
            }
        });
    }

}