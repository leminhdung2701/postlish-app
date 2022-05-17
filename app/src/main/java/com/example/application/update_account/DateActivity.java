package com.example.application.update_account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
public class DateActivity extends AppCompatActivity {
    Button back,save;
    EditText editText;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;
    //User user1 = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        Anhxa();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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

            }
        });
    }
    public boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    // e2.printStackTrace();
                }
            }
        }

        return false;
    }

    public void change(){
        String date = editText.getText().toString();
        if(isValidFormat("dd/MM/yyyy", date, Locale.ENGLISH)) {
            mDatabase.child("users").child(user.getUid()).child("date").setValue(date, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null ) {
                        Toast.makeText(DateActivity.this, "Cập nhật thành công ", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(DateActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(DateActivity.this, "Bạn phải nhập ngày sinh đúng định dạng dd/mm/yyyy", Toast.LENGTH_SHORT).show();
        }
    }

    public void Anhxa() {
        back = findViewById(R.id.btn_date_back);
        save = findViewById(R.id.btn_date_save);
        editText=findViewById(R.id.edt_date);
    }
}