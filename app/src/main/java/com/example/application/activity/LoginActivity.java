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
import com.example.application.account.User;
import com.example.application.function.HomeActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class LoginActivity extends AppCompatActivity  {
    CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    EditText edtTenDn, edtMk;
    Button btnQuenMk, btnDn, btnDk;
    SignInButton btnloginByGoogle;
    LoginButton btnloginByFb;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN =1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa();
        // của fb

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        btnloginByFb = (LoginButton)findViewById(R.id.btn_login_byFB);

        btnloginByFb.setReadPermissions("email","public_profile");
        btnloginByFb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this,"Hủy",Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                // ...
            }
        });

        //

        mAuth = FirebaseAuth.getInstance();
        btnQuenMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Forget_passwordAcitivy.class);
                startActivity(intent);
            }
        });
        btnDk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnDn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        createRequestGoogle();
        btnloginByGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }
    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            upDatabase();
                            // xong
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {

                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // code của đăng nhập google
    private void createRequestGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            upDatabase();
                            // Sign in success, update UI with the signed-in user's information
                            // đẩy dữ liệu người dùng  lên firebase ở mục users
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user
                            Toast.makeText(LoginActivity.this,"Xảy ra lỗi",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    // code đăng nhập bt = gmail và pass
    public void login(){
        String email = edtTenDn.getText().toString();
        String password = edtMk.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if(mAuth.getCurrentUser().isEmailVerified()) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mAuth.signOut();
                                Toast.makeText(LoginActivity.this,"Vui lòng xác nhận gmail trước khi đăng nhập",Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,"Sai mật khẩu hoặc gmail",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    // Ánh xạ find id
    public void upDatabase(){
        FirebaseUser user = mAuth.getCurrentUser();
        User users = new User();
        users.setId(user.getUid());
        users.setName(user.getDisplayName());
        users.setGmail(user.getEmail());
       if(user.getPhotoUrl() !=null) users.setPhotoUrl(user.getPhotoUrl().toString());
       else users.setPhotoUrl("https://ephoto360.com/uploads/worigin/2020/03/23/tao-avatar-mac-dinh-facebook-thay-nen-cuc-hot5e7838ae39057_96eb8aef68a3aa00523448390b49fbcb.jpg");
        users.setRole(0);
        users.setDate("01/01/2001"); //xét mặc định, người dùng về sau chỉnh lại
        users.setGender(false); // xét mặc định, người dùng về sau chỉnh lại
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                    }
                    else{
                        myRef.child("users").child(users.getId())
                                .setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void Anhxa(){
        edtTenDn=(EditText)findViewById(R.id.edt_login_email_sdt);
        edtMk=(EditText) findViewById(R.id.edt_login_password);
        btnQuenMk=(Button) findViewById(R.id.btn_login_quenmk);
        btnDn=(Button) findViewById(R.id.btn_login_dn);
        btnDk=(Button) findViewById(R.id.btn_login_dk);
        btnloginByGoogle = (SignInButton) findViewById(R.id.btn_login_byGoogle);

    }



}
