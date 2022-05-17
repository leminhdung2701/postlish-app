package com.example.application.function;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.R;
import com.example.application.object.Payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormatSymbols;

public class CustomDialog extends Dialog {

    interface FullNameListener {
        public void fullNameEntered(String fullName);
    }

    public Context context;

    private EditText editTextFullName;
    private Button buttonOK;
    private Button buttonCancel;

    private CustomDialog.FullNameListener listener;

    public CustomDialog(Context context, CustomDialog.FullNameListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_custom_dialog);

        this.editTextFullName = (EditText) findViewById(R.id.editText_fullName);
        this.buttonOK = (Button) findViewById(R.id.button_ok);
        this.buttonCancel  = (Button) findViewById(R.id.button_cancel);

        this.buttonOK .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOKClick();
            }
        });
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCancelClick();
            }
        });
    }

    public boolean isStringNumeric(String str) {
        DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
        char localeMinusSign = currentLocaleSymbols.getMinusSign();

        if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign)
            return false;

        boolean isDecimalSeparatorFound = false;
        char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

        for (char c : str.substring(1).toCharArray()) {
            if (!Character.isDigit(c)) {
                if (c == localeDecimalSeparator && !isDecimalSeparatorFound) {
                    isDecimalSeparatorFound = true;
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    // User click "OK" button.
    private void buttonOKClick()  {
        String fullName = this.editTextFullName.getText().toString();

        if(fullName== null || fullName.isEmpty())  {
            Toast.makeText(this.context, "Xin vui lòng nhập mã thẻ", Toast.LENGTH_LONG).show();
            return;
        }
        this.dismiss(); // Close Dialog

        if(this.listener!= null && isStringNumeric(fullName))  {
            this.listener.fullNameEntered(fullName);
            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
            b.setTitle("Xác nhận");
            b.setMessage("Cảm ơn đã ủng hộ Postblish");
            FirebaseDatabase.getInstance().getReference().child("CheckCard").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
            b.setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                }
            });
            AlertDialog al = b.create();
            al.show();
        }else{
            Toast.makeText(this.context, "Nhập sai cú pháp", Toast.LENGTH_LONG).show();
        }
    }

    // User click "Cancel" button.
    private void buttonCancelClick()  {
        this.dismiss();
    }

}