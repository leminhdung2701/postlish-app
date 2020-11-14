package com.example.application;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Login extends AppCompatActivity {

    TextView txtLogin, txtCo_Tk_chua;
    EditText edtDn, edtMk;
    Button btnQuenMk, btnTaoTk;
    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*
        try {
            PackageInfo info = null;
            try {
                info = getPackageManager().getPackageInfo(
                        "com.example.application",                  //Insert your own package name.
                        PackageManager.GET_SIGNATURES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {

        }
        */

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        Anhxa();
        loginButton.setReadPermissions(Arrays.asList("email","name"));
        setLogin_Button();

    }

    private void setLogin_Button() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                result();
                loginButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void result() {
        GraphRequest graphRequest =GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON",response.getJSONObject().toString());
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }

    public void Anhxa(){
        txtLogin =(TextView) findViewById(R.id.login_textView_login);
        txtCo_Tk_chua =(TextView) findViewById(R.id.login_textView_chuaTK);
        edtDn=(EditText)findViewById(R.id.login_edt_email_sdt);
        edtMk=(EditText) findViewById(R.id.login_edt_password);
        btnQuenMk=(Button) findViewById(R.id.login_btn_quenmk);
        btnTaoTk=(Button) findViewById(R.id.login_btn_taoTK);
        loginButton =(LoginButton) findViewById(R.id.login_button);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}