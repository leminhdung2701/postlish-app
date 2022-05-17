package com.example.application.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.application.account.User;
import com.example.application.function.PublishActivity;
import com.example.application.postmanage.Post;
import com.example.application.postmanage.PostContent;
import com.example.application.R;
import com.google.android.gms.tasks.OnFailureListener;
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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AddPost extends AppCompatActivity {
    TextView textView;
    EditText title_post , description_post ;
    Button upload ;
    ImageView post_image ;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    static String category;
    Uri image_uri = null ;
    ProgressDialog pd ;
    private static final  int GALLERY_IMAGE_CODE = 100 ;
    private static final  int CAMERA_IMAGE_CODE = 200 ;
    User user1 = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        permission();
        Anhxa();
        // set chủ đề cho addpost
        Intent intent = getIntent();
        category = intent.getStringExtra(PublishActivity.THEME);
        textView.setText("Chủ đề: "+category);
        //

        pd = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });

        //
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = title_post.getText().toString();
                String description = description_post.getText().toString();
                final String timeStamp = String.valueOf(System.currentTimeMillis());
                //here we will set the filepath of our image
                DatabaseReference contentdata = FirebaseDatabase.getInstance().getReference("PostsContent");
                PostContent a = new PostContent();
                a.setContent(description);
                contentdata.child(timeStamp).setValue(a)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                if (TextUtils.isEmpty(title)){
                    title_post.setError("Tiêu đề đang bỏ trống.");
                }
                else if (TextUtils.isEmpty(description)){
                    description_post.setError("Nội dung đang bỏ trống.");
                }
                else {
                    uploadData(title , timeStamp);
                }
            }
        });

    }

    private void uploadData(final String title, final String idcontentPost) {

        pd.setMessage("Đang xuất bản");
        pd.show();
        //here we will upload the data to firebase
        //firt we will get the time when user upload the post
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        //here we will set the filepath of our image
        String filepath = "Posts/"+"post_"+timeStamp;
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users");
        mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user1 = dataSnapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        if (post_image.getDrawable() != null){
            //getImage from Image view ;
            Bitmap bitmap = ((BitmapDrawable)post_image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG , 100 , baos);
            byte[] data = baos.toByteArray();

            // now we will creat the referense of storage in firebase as we have al ready added the libraries
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(filepath);
            reference.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            //here uri.Tast is not success to end the while loop so put not equal to sing !
                            while (!uriTask.isSuccessful());

                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()){
                                //uri is recieved post is publised to database

                                //now we will upload the daata to firebase database for


                               /* HashMap<String , Object> hashMap = new HashMap<>();

                                hashMap.put("uid" , user.getUid());
                                hashMap.put("uEmail" , user.getEmail());
                                hashMap.put("pId" , timeStamp);
                                hashMap.put("pTitle" , title);
                                hashMap.put("pImage" , downloadUri);
                                hashMap.put("pDescription" , description);
                                hashMap.put("pTime" ,  timeStamp); */
                                Post post = new Post(idcontentPost,title,downloadUri,user1.getId(),user1.getName(),user1.getPhotoUrl(),category);
                                post.setTimeStamp(timeStamp);

                                //now we will pust the data to firebase database

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                ref.child(post.getTimeStamp().toString()).setValue(post)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();;
                                                Toast.makeText(AddPost.this, "Bài biết đã được xuất bản", Toast.LENGTH_SHORT).show();
                                                title_post.setText("");
                                                description_post.setText("");
                                                post_image.setImageURI(null);
                                                image_uri = null ;

                                                //when post is publised user must go to home activity means main dashboad
                                                //startActivity(new Intent(AddPost.this , Postlish_main_testActivity.class));
                                                onBackPressed();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(AddPost.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPost.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
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

    private void galleryPick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        //this is for all tape of images make sure you didnot make speeling msitake
        intent.setType("image/*");
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
                post_image.setImageURI(image_uri);
            }
            if (requestCode == CAMERA_IMAGE_CODE){
                post_image.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Anhxa(){
        textView=findViewById(R.id.textview_chude);
        title_post = findViewById(R.id.title_post);
        description_post = findViewById(R.id.description_post);
        upload = findViewById(R.id.upload);
        post_image = findViewById(R.id.post_image_post);
    }
}