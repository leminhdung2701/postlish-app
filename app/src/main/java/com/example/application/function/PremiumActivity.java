package com.example.application.function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.application.account.User;
import com.example.application.object.Payment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PremiumActivity extends AppCompatActivity {

    String userName,userGmail,userId;
    Button btnDKSD;
    TextView txtAdd,txtBalance,txtHello_name;
    RelativeLayout premium12,premium6,premium1,premiumgd;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        //

        // lấy dữ liệu từ MenuActivity
        Intent intent = getIntent();
        userId = intent.getExtras().getString("userId");
        userName = intent.getExtras().getString("nameUser");
        userGmail = intent.getExtras().getString("gmail");
        //
        btnDKSD=findViewById(R.id.đksd);
        txtAdd=findViewById(R.id.txt_add);
        txtBalance=findViewById(R.id.balance);
        txtHello_name=findViewById(R.id.hello);
        premium1=findViewById(R.id.relative_3);
        premiumgd=findViewById(R.id.relative_4);
        premium6=findViewById(R.id.relative_2);
        premium12=findViewById(R.id.relative_1);
        //
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Payment").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.exists()){
                    Payment a = snapshot.getValue(Payment.class);
                    double payment = a.getBalance();
                    if(payment == 0){
                        txtBalance.setText("Số dư tài khoản: "+"0"+" điểm.");
                    }else {
                        txtBalance.setText("Số dư tài khoản: " + String.valueOf(payment) + " vnđ.");
                    }
                }
                else{
                    Payment a =new Payment(userId);
                    FirebaseDatabase.getInstance().getReference().child("Payment").child(userId).setValue(a);

                }

                txtAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Payment a = snapshot.getValue(Payment.class);
                        double payment = a.getBalance();
                        String balance;
                        if(payment == 0){
                            balance ="0";
                        }else {
                            balance=String.valueOf(payment) ;
                        }
                        Intent intent = new Intent(PremiumActivity.this,ChargeActivity.class);
                        intent.putExtra("userId",userId );
                        intent.putExtra("nameUser",userName );
                        intent.putExtra("payment",balance);
                        intent.putExtra("gmail",userGmail);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //
        checkPostView();
        txtAdd=findViewById(R.id.txt_add);
        txtBalance=findViewById(R.id.balance);
        txtHello_name=findViewById(R.id.hello);
        ///////
        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PremiumActivity.this,ChargeActivity.class));
            }
        });

        txtHello_name.setText("Xin chào "+userName+" !");
        //
        btnDKSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PremiumActivity.this,DksdActivity.class));
            }
        });
        premium12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setonClick(499000,12);
            }
        });
        premium6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setonClick(279000,6);
            }
        });
        premium1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setonClick(49000,1);
            }
        });
        premiumgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setonClick(89000,1);
            }
        });
    }
    public void setPremium(int time){
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("role").setValue(1);
        Long date_rightnow = System.currentTimeMillis();
        //long second = TimeUnit.MILLISECONDS.toSeconds(date_rightnow);
        //Calendar calendar = Calendar.getInstance();
       // calendar.add(Calendar.MONTH, time);
      //  Date date = calendar.getTime();
        Long month =Long.parseLong("2629800000");
       long date_new = date_rightnow+ month*time;
        FirebaseDatabase.getInstance().getReference().child("Premium_time").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Long a = snapshot.getValue(Long.class);
                    a = a+month*time;
                    FirebaseDatabase.getInstance().getReference().child("Premium_time").child(userId).setValue(a);
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Premium_time").child(userId).setValue(date_new);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void checkOut(double payment,int time){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Payment").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Payment a = snapshot.getValue(Payment.class);
                        double realMoney = a.getBalance();
                        if(realMoney>=payment){
                            FirebaseDatabase.getInstance().getReference().child("Payment").child(userId).child("balance").setValue(realMoney-payment);
                            setPremium(time);
                            Toast.makeText(PremiumActivity.this,"Thanh toán thành công",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(PremiumActivity.this,"Tài khoản của bạn không đủ tiền để thanh toán",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });
    }

    public void setonClick(double payment,int time) {


        AlertDialog.Builder alert = new AlertDialog.Builder(PremiumActivity.this);
        alert.setTitle("Thanh toán");
        alert.setMessage("Bạn có chắc muốn mua gói Premium này không");
        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkOut(payment,time);
                dialog.dismiss();

            }
        });

        alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
    public void Paymoney(String keypost,double money,int milestone){
        FirebaseDatabase.getInstance().getReference().child("Posts").child(keypost).child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = snapshot.getValue(String.class);
                FirebaseDatabase.getInstance().getReference().child("MileStone").child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    if(snapshot.getValue(Integer.class) < milestone){
                                        FirebaseDatabase.getInstance().getReference().child("Payment").child(id)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            Payment a = snapshot.getValue(Payment.class);
                                                            a.deposit(money);
                                                            FirebaseDatabase.getInstance().getReference().child("Payment").child(id).child("balance").setValue(a.getBalance());
                                                            FirebaseDatabase.getInstance().getReference().child("MileStone").child(id).setValue(milestone);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                    }
                                }else{
                                    FirebaseDatabase.getInstance().getReference().child("Payment").child(id)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        Payment a = snapshot.getValue(Payment.class);
                                                        a.deposit(money);
                                                        FirebaseDatabase.getInstance().getReference().child("Payment").child(id).child("balance").setValue(a.getBalance());
                                                        FirebaseDatabase.getInstance().getReference().child("MileStone").child(id).setValue(milestone);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void checkOutmoney(int countview, String keypost) {
                if(countview<100){

                }
                else if (countview > 100 && countview < 200) {
                    Paymoney(keypost, 5000,1);
                }
                else if(countview > 200 && countview <300){
                    Paymoney(keypost, 10000,2);
                }
                else if(countview > 300 && countview <500){
                    Paymoney(keypost, 20000,3);
                }
                else if(countview >500 && countview <800){
                    Paymoney(keypost, 40000,4);
                }
                else if(countview>800){
                    Paymoney(keypost, 80000,5);
                }
            }

            public void checkPostView() {
                FirebaseDatabase.getInstance().getReference().child("CountViewAll").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot a : snapshot.getChildren()) {
                            String key = a.getKey();
                            FirebaseDatabase.getInstance().getReference().child("CountViewAll").child(key).child("count").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int a = snapshot.getValue(Integer.class);
                                    checkOutmoney(a, key);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


    }



