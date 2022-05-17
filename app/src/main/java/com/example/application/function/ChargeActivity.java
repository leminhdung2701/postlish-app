package com.example.application.function;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.application.R;
import com.example.application.account.User;
import com.example.application.object.Payment;
import com.example.application.update_account.NameActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChargeActivity extends AppCompatActivity {

    Button accept;
    CheckBox vina,viet,mobi;
    CheckBox k10,k20,k50,k100,k200,k500;
    String A[] = new String[3];
    TextView txtTen,txtGmail,txtSodu;
    private DatabaseReference mDatabase;

    FirebaseAuth mAuth;
    FirebaseUser user;
    //
    String userName, userGmail,userId,userPay;

    int i=0;
    int j=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        // lấy dữ liệu từ Activity

        Intent intent = getIntent();
        userId = intent.getExtras().getString("userId");
        userName = intent.getExtras().getString("nameUser");
        userGmail = intent.getExtras().getString("gmail");
        userPay=intent.getExtras().getString("payment");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //
        txtTen=findViewById(R.id.txt_tentk);
        txtSodu=findViewById(R.id.txt_sodu);
        txtGmail=findViewById(R.id.txt_gmail);
        //
        FirebaseDatabase.getInstance().getReference().child("Payment").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Payment a = snapshot.getValue(Payment.class);
                        txtSodu.setText("Số dư tài khoản: "+a.getBalance()+" vnđ.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });
        txtTen.setText("Tên tài khoản: "+userName);
        txtGmail.setText("Gmail: "+userGmail);

        //
        init();



    }



    CompoundButton.OnCheckedChangeListener m_listener1
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(compoundButton.isChecked()==true){
                mobi.setChecked(false);
                viet.setChecked(false);
                vina.setChecked(false);
                compoundButton.setChecked(true);
                A[0] = compoundButton.getText().toString();
                i=1;
            }
            if(b==false){
                i=0;
            }
        }
    };

    CompoundButton.OnCheckedChangeListener m_listener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(compoundButton.isChecked()==true){
                k10.setChecked(false);
                k20.setChecked(false);
                k50.setChecked(false);
                k100.setChecked(false);
                k200.setChecked(false);
                k500.setChecked(false);
                compoundButton.setChecked(true);
                A[1]= compoundButton.getText().toString();
                j=1;
            }
            if(b==false){
                j=0;
            }
        }
    };



    void init() {
        k10    = findViewById(R.id.k10);
        k20    = findViewById(R.id.k20);
        k50    = findViewById(R.id.k50);
        k100    = findViewById(R.id.k100);
        k200    = findViewById(R.id.k200);
        k500    = findViewById(R.id.k500);
        vina    = findViewById(R.id.vina);
        mobi    = findViewById(R.id.mobi);
        viet    = findViewById(R.id.viet);

        k10.setOnCheckedChangeListener(m_listener);
        k20.setOnCheckedChangeListener(m_listener);
        k50.setOnCheckedChangeListener(m_listener);
        k100.setOnCheckedChangeListener(m_listener);
        k200.setOnCheckedChangeListener(m_listener);
        k500.setOnCheckedChangeListener(m_listener);
        viet.setOnCheckedChangeListener(m_listener1);
        vina.setOnCheckedChangeListener(m_listener1);
        mobi.setOnCheckedChangeListener(m_listener1);

        accept = findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==1 && j==1){


                    A[2] = null;
                    buttonOpenDialogClicked();




                    } else if(i==1 && j==0){
                    Toast.makeText(ChargeActivity.this,"Bạn chưa chọn số tiền",Toast.LENGTH_SHORT).show();
                } else if(i==0 && j==1){
                    Toast.makeText(ChargeActivity.this,"Bạn chưa chọn loại thẻ ",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChargeActivity.this,"Mời chọn cách thanh toán",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void buttonOpenDialogClicked()  {
        CustomDialog.FullNameListener listener = new CustomDialog.FullNameListener() {
            @Override
            public void fullNameEntered(String fullName) {
                A[2] = fullName;

            }
        };



        final CustomDialog dialog = new CustomDialog(this, listener);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                double money = Double.parseDouble(A[1]);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseDatabase.getInstance().getReference().child("CheckCard").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            mDatabase.child("Payment").child(userId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            Payment a = snapshot.getValue(Payment.class);
                                            double m = a.getBalance();
                                            FirebaseDatabase.getInstance().getReference().child("Payment").child(userId).child("balance").setValue(m+money);


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }


                                    });
                            snapshot.getRef().setValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        }


}