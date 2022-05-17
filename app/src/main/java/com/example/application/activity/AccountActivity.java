package com.example.application.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.application.R;
import com.example.application.account.User;
import com.example.application.update_account.DateActivity;
import com.example.application.update_account.NameActivity;
import com.example.application.update_account.PasswordActivity;
import com.example.application.update_account.SexActivity;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AccountActivity extends AppCompatActivity {

    private static final  int GALLERY_IMAGE_CODE = 100 ;
    private static final  int CAMERA_IMAGE_CODE = 200 ;
    Uri image_uri = null ;
    Button changeImg;
    // Button btn1;
    //  Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    TextView userid,usergmail,username,usergender,userdate,role,text_premium;
    ImageView avatar;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private User user1 = new User();
    Button deleteAccount;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Anhxa();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase1 = FirebaseDatabase.getInstance().getReference();
        mDatabase = database.getReference();
        // ...

        if(user != null) {
            mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user1 = dataSnapshot.getValue(User.class);
                    String photoURL = user1.getPhotoUrl();
                    Glide
                            .with(getApplicationContext())
                            .load(photoURL)
                            .fitCenter()
                            .placeholder(R.drawable.account)
                            .apply(new RequestOptions().override(80, 80))
                            .into(avatar);
                    String name = user1.getName();
                    String gmail = user.getEmail();
                    String date = user1.getDate();
                    boolean gender = user1.getGender();

                    userid.setText(user.getUid());
                    int a = user1.getRole();
                    if(a==0) {
                        role.setText("Tài khoản Thường");
                    }
                    else if(a==1){
                        checkPremiumTime();
                    }
                    else{
                        role.setText("Tài khoản Admin");
                    }
                    username.setText(name);
                    usergmail.setText(gmail);
                    userdate.setText(date);
                    if(gender == false) usergender.setText("Nữ");
                    else usergender.setText("Nam");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
            id = user.getUid();


        }
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("users_category").child(user.getUid()).removeValue();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });


                LoginManager.getInstance().logOut();
                mDatabase.child(id).removeValue();
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(AccountActivity.this,"Xóa tài khoản thành công",Toast.LENGTH_SHORT).show();
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permission();
                imagePickDialog();

            }
        });
        changeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar();
            }
        });
       /* btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, IDActivity.class);
                startActivity(intent);
            }
        });
        */
       /* btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, UserActivity.class);
                startActivity(intent);
            }
        }); */

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, PasswordActivity.class);
                startActivity(intent);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, NameActivity.class);
                startActivity(intent);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, SexActivity.class);
                startActivity(intent);
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });
    }

    public void Anhxa(){
        //  btn1=findViewById(R.id.btn_account_id);
        //  btn2=findViewById(R.id.btn_account_user);
        btn3=findViewById(R.id.btn_account_pw);
        btn4=findViewById(R.id.btn_account_name);
        btn5=findViewById(R.id.btn_account_sex);
        btn6=findViewById(R.id.btn_account_date);
        userid =findViewById(R.id.textview_userid);
        usergmail=findViewById(R.id.textview_tendangnhap);
        username=findViewById(R.id.textview_tentaikhoan);
        avatar=findViewById(R.id.img_avatar);
        usergender =findViewById(R.id.textview_gioitinh);
        userdate = findViewById(R.id.textview_ngaysinh);
        deleteAccount=findViewById(R.id.button_deleteAccount);
        role = findViewById(R.id.text_role);
        changeImg = findViewById(R.id.button_changeImg);
        text_premium=findViewById(R.id.text_premium);
    }

    private void checkPremiumTime(){
        Long date_rightnow = System.currentTimeMillis();
        FirebaseDatabase.getInstance().getReference().child("Premium_time").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {

                            if (snapshot1.exists()) {
                                Long a = snapshot1.getValue(Long.class);
                                if (date_rightnow >= a) {
                                    FirebaseDatabase.getInstance().getReference().child("users").child(id).child("role").setValue(0);
                                } else {


                                    Long dayLeft = a - date_rightnow;
                                    String convTime = null;
                                    long second = TimeUnit.MILLISECONDS.toSeconds(dayLeft);
                                    long minute = TimeUnit.MILLISECONDS.toMinutes(dayLeft);
                                    long hour = TimeUnit.MILLISECONDS.toHours(dayLeft);
                                    long day = TimeUnit.MILLISECONDS.toDays(dayLeft);

                                    if (second < 60) {
                                        convTime = second + " giây ";
                                    } else if (minute < 60) {
                                        convTime = minute + " phút ";
                                    } else if (hour < 24) {
                                        convTime = hour + " giờ ";
                                    } else if (day >= 7) {
                                        if (day > 365) {
                                            convTime = (day / 365) + " năm "+(day - 365*(day/365)) +" ngày";
                                        } else if (day > 30) {
                                            convTime = (day / 30) + " tháng "+(day - 30*(day/30)) +" ngày";
                                        } else {
                                            convTime = (day / 7) + " tuần "+(day - 7*(day/7)) +" ngày";
                                        }
                                    } else if (day < 7) {
                                        convTime = day + " ngày ";
                                    }

                                        role.setText("Tài khoản Premium ");
                                        text_premium.setText("Còn lại "+convTime);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
    private void imagePickDialog() {
        //here 0 is for camera and 1 is for gallery so please do it like me
        String[] options = {"Camera" , "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose image from");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    cameraPick();
                }
                if (which == 1){
                    galleryPick();

                }
            }
        });
        builder.create().show();

    }
    private void changeAvatar(){
        //getImage from Image view ;
        Bitmap bitmap = ((BitmapDrawable)avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG , 100 , baos);
        byte[] data = baos.toByteArray();
        FirebaseUser user = mAuth.getCurrentUser();
        // now we will creat the referense of storage in firebase as we have al ready added the libraries
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("user_photo").child(user.getUid());
        reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                String downloadUri = uriTask.getResult().toString();
                if (uriTask.isSuccessful()){

                    mDatabase.child("users").child(user.getUid()).child("photoUrl").setValue(downloadUri, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if(error == null){
                                Toast.makeText(AccountActivity.this,"Cập nhật ảnh đại diện thành công",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(AccountActivity.this,"Xảy ra lỗi",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                mDatabase.child("Posts").orderByChild("userId")
                        .equalTo(user.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                                    String key = postsnap.getKey();

                                    Toast.makeText(AccountActivity.this,key,Toast.LENGTH_SHORT).show();
                                    //
                                    mDatabase1.child("Posts").child(key)
                                            .child("userPhoto")
                                            .setValue(downloadUri);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }
        });

    }



    private void galleryPick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        //this is for all tape of images make sure you didnot make speeling msitake
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 80); // This sets the max width.
        intent.putExtra("aspectY", 80); // This sets the max height.
        intent.putExtra("outputX", 1);
        intent.putExtra("outputY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent , GALLERY_IMAGE_CODE);
    }

    private void cameraPick() {
        //here we will do this for camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE , "Temp Pick");
        contentValues.put(MediaStore.Images.Media.TITLE , "Temp desc");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT , image_uri);
        startActivityForResult(intent , CAMERA_IMAGE_CODE);
    }
    private void permission(){

        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken toke) {
                        toke.continuePermissionRequest();
                    }
                }).check();
        //hold alt key and press enter to import the library
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == GALLERY_IMAGE_CODE){
                image_uri = data.getData();
                avatar.setImageURI(image_uri);
            }
            if (requestCode == CAMERA_IMAGE_CODE){
                avatar.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseRequests();
    }
}