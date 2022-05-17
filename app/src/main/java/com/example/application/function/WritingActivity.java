package com.example.application.function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.application.R;

import com.example.application.account.User;
import com.example.application.object.CountView;

import com.example.application.object.countViewOnedayUser;
import com.example.application.postmanage.Post;
import com.example.application.postmanage.PostContent;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WritingActivity extends AppCompatActivity {

    PostContent postContent = new PostContent("1", "Some people believe that there are no compelling reasons for us to protect animal species from extinction as it occurs naturally. I personally disagree with this conviction and will support my argument in the essay below.\n" +
            "\n" +
            "It is true that millions of years ago, many ancient species of animals, such as dinosaurs, were wiped out due to a gradual shift in climate and changing sea levels, according to some hypotheses. However, these environmental factors are not the primary contributor to the disappearance of certain species nowadays. Industrial activities have been devastating the natural habitats of wildlife and disturbing the food chain, causing the mass extinction of countless species. The increased demand for goods made from animals’ products, such as skins and horns, also leads to the rampant poaching of wild, endangered animals, rhinos for instance. In this regard, humans are held accountable and should do what is needed to rectify the situation.\n" +
            "\n" +
            "Other justifications for saving wild animals involve the significant roles that they play in not only the balance of the ecosystem but also our lives. Everything in nature is connected, and if one species becomes extinct, many other animals and even plants will suffer as the food chain is disrupted. Wild animals also have great aesthetic and socio-cultural values. They contribute to our rich bio-diversity that makes this planet a beautiful place. In numerous places around the world, many types of animals play an important role in different cultures. For example, in some religions, cows are revered and worshiped as gods.\n" +
            "\n" +

            "The disappearance of many animal species does not always occur as a natural process but as a conse quence of our doings. It is our obligation to help preserve wild animals because their extinction will have a severe influence on many important aspects of our lives.");
    ;

    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    ;

    private DatabaseReference mDatabase;
    public ImageView imageProfile;
    public ImageView postImage;
    public ImageView like;
    public ImageView comment;
    public ImageView save;
    public ImageView more;
    public TextView title;
    String id, id_user,postcontent_id;
    public TextView username;
    public TextView view_count;
    public TextView noOfLikes;
    public TextView noOfComments;
    public TextView time;
    public TextView category;

    SocialTextView description;

    List<Post> posts;

    //
    String userName = "No Author";
    String Title = "";
    String image = "";
    String photo = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAMFBMVEXFxcX////CwsLGxsb7+/vT09PJycn19fXq6urb29ve3t7w8PDOzs7n5+f5+fnt7e30nlkBAAAFHUlEQVR4nO2dC5qqMAyFMTwUBdz/bq+VYYrKKJCkOfXmXwHna5uTpA+KwnEcx3Ecx3Ecx3Ecx3Ecx3Ecx3Ecx3Ecx3EcA2iO9cdIc5PUdO257y+BU39u66b4HplE3fk6VIcnqmNfl1+gksr6+iIucjl3WYukor7+re6Hoe1y1UhNO3zUd+fUFRmKpOa0Tt6dY5ubRCrOG/QFLk1WGmnt/JxzykcjdZ/jyxJDLlOV2l36AtcsJJb9boG3YcR3DuqODIE3ztYKPkDdmwRmpUToUaSaq++AvRgZMWbOpbQW8hdCAm8ZDugoikzREdCJ2okJPBx6azFLNOwoOgcxojJ98JkaTSJxMpklKrCAKhZGI0drTY/wU5lXoJYibannV9NYy4oozNEAkPHTjop+DTDxVGkIgYJNoyQQJtiIW+EMjGAjm649AjGIaqswcEFQKJ2QPlJbqytki6ZXAAZRJ52J2McaUowzAfs+uFzrYhnzaapphiPWdaJWShqxjqa6kTTQ205TVbsfMa6htL0iYOsXpJrQjHSmCkv1QGPtiHqlYcQ21Gj7fcDU8xOEUuNgSltPzexh+HqFlanCBHZ4OLhCV+gK/3OF6vWvucLv98MUOY2pwu/PS/+D2qJU7pYGbOvDFDW+bbON9p3o3oRxn0bfLgZTgSn6pSfrtr56qLHemtHPTK2319SzGvtjQ9qeb39WgS66Cm073nd0U1PzDdJCO3Gzn6TKpl9Zq7ujGWsQhlA3NwWIMwG9zM08Y/tBrR9VWeczv5CSQuuUNKIUTk23ZJ5RKfVhjnkXotfWIlgX2BSCDYbZR+QTcLhb3dKZDUY2M0d4KWItwhHRah/zsrOgKw4wycwjcgEVcgQDQo23CqSiWEJkFAfod2oE1uIFdA1OsCPqFXYNTjCfb8Ez+iX2x5sKLlVbhtqdDcar9ZevhnbZxoBUD35k23t0d304LYs1ELVbnfFaZ/REJJX9niP8Q19moZGo3m8XR/yBvOnjFfsXcI2c8ZuNo7WMP5HQh6yRGrlmFOJTnyTcT+zRlqPUBI2gTVWNUzUna1ERgecgF4GpNBQ38jGqxVLzQA1A31Rrhk6Yz9QEh/WND0GnuG9huhiTXJkxfAizTHLr6cbJKN6UCU6x/2DTRE1xEeEXi3O0ZUqBN4nJRzHhFB1JPlFTBZlI2kQ8zc3KJ1Le8DIRmFJiknuVS6RK4Ej/JtBfJErDSzOBiY4wJHX6Z1I4v1GUmdCPNirnLLeg3oJLcbX5PcpHNbRvOa1A956QmRPOUXVF+zkaUJynpkYR0bOMJH2nNej1pqyV/aKkz9jr5yj5vrXXz1F5SQLACiMapmierj2ikLyleKdlA/I/2oFxiglxx9B+mHwz0lf34IZQfhDRhlD6bhvgEAoPYooHkTczSIZTLC+cEExsoNKZiGBiY9cCfo/Y/SjIOBMQizWWTe73CMUasJx7jlD+DdKdWUKoY4PRYFtGpO0G1Lx4RaadgTtJhf4fiGqGIwKWCGuGIwKWqP+7IxYCzygjR9IAO5pC7Da9g70TBVpWRNgFBlgT8RV2WxHbKwJMv4BOaEaYaU2K16yZMN/qgV+G7IWIvwyZCxHeDQMsR8wg0DBDDXB5H2EV+hkEGmaoySHQsEJNFoGGFWrAq98JRhUMX1iMMMqLLEIpK5jCbd4vw9nSt/72lewXiN6jmdjfq8Hdknlk92ZwJnbIMMRM7JBhiFlUFoHd1UWaP1QKsPsHA5mkNB+Smn9JqV3wskatnQAAAABJRU5ErkJggg==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        posts = new ArrayList<>();

        imageProfile = findViewById(R.id.image_profile);
        postImage = findViewById(R.id.post_image);
        like = findViewById(R.id.like);
        comment = findViewById(R.id.comment);
        save = findViewById(R.id.save);
        more = findViewById(R.id.more);
        title =findViewById(R.id.title);
        username = findViewById(R.id.username);
        noOfLikes = findViewById(R.id.no_of_likes);
        view_count = findViewById(R.id.view_count);
        noOfComments = findViewById(R.id.no_of_comments);
        description = findViewById(R.id.description);
        time=findViewById(R.id.timehome);
        category=findViewById(R.id.category);
        //
        Intent intent = getIntent();
        id = intent.getExtras().getString("Id");
        postcontent_id = intent.getExtras().getString("Content");
        if (intent.getExtras().getString("Photo") != null) {
            photo = intent.getExtras().getString("Photo");
        }
        if (intent.getExtras().getString("Id_user") != null) {
            id_user = intent.getExtras().getString("Id_user");
        }
        if (intent.getExtras().getString("Image") != null) {
            image = intent.getExtras().getString("Image");
        }
        if (intent.getExtras().getString("Name") != null) {
            userName = intent.getExtras().getString("Name");
        }
        if (intent.getExtras().getString("Title") != null) {
            Title = intent.getExtras().getString("Title");
        }
        if (intent.getExtras().getString("category") != null) {
            category.setText(intent.getExtras().getString("category"));
        }

        if (intent.getExtras().getString("time") != null) {
             Long a = Long.parseLong(intent.getExtras().getString("time"));
             time.setText(getDate(a));
        }
        if (intent.getExtras().getString("Title") != null) {
            Title = intent.getExtras().getString("Title");
        }
        if(id_user.equals(firebaseUser.getUid())){
            addHistory();
        }else {
            checkRole();
        }
        String Description = "";
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = database.getReference("PostsContent");
        // Toast.makeText(this,Id_user, Toast.LENGTH_SHORT).show();
        mDatabase.child(postcontent_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    PostContent a = dataSnapshot.getValue(PostContent.class);
                    description.setText(a.getContent());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        //
        registerForContextMenu(more);
        //
        username.setText(userName);

        title.setText(Title);
        //   description.setText(Description);
        Glide.with(this).load(photo).into(imageProfile);
        Glide.with(this).load(image).into(postImage);

        //noOfComments lấy comment database equals id;
        isSaved(id, save);
        noOfLikes(id, noOfLikes);
        getComments(id, noOfComments);
        isLiked(id, like);
        //
        String finalTitle = Title;
        String finalId_user = id_user;
        countView();
        mDatabase = database.getReference("CountViewAll");
        // Toast.makeText(this,Id_user, Toast.LENGTH_SHORT).show();
        mDatabase.child(id).child("count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    int a = dataSnapshot.getValue(Integer.class);
                    view_count.setText(String.valueOf(a)+" lượt xem");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        //
        FirebaseDatabase.getInstance().getReference().child("Posts").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WritingActivity.this, ProfileActivity.class);
                        intent.putExtra("userId",id_user);
                        intent.putExtra("nameUser",userName);
                        intent.putExtra("photoUser",photo);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //comment
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CommentActivity.class);
                intent.putExtra("postId", id);
                intent.putExtra("authorId", finalId_user);
                startActivity(intent);
            }
        });
        //like
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //like();
                if (like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(id).child(firebaseUser.getUid()).setValue(true);

                    addNotification(id, id_user);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(id).child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        //save
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(id).setValue(true);

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(id).removeValue();
                }
            }
        });





    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", cal).toString();
        String convert = covertTimeToText(date);
        return convert;
    }
    public String covertTimeToText(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "trước";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " giây " + suffix;
            } else if (minute < 60) {
                convTime = minute + " phút "+suffix;
            } else if (hour < 24) {
                convTime = hour + " giờ "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " năm " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " tháng " + suffix;
                } else {
                    convTime = (day / 7) + " tuần " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" ngày "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }
    private void checkRole(){
        FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User a = snapshot.getValue(User.class);
                int role = a.getRole();
                if(role==0){
                    checkuserRead();
                }
                addHistory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addHistory(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Long date_rightnow = System.currentTimeMillis();
        date_rightnow = date_rightnow*-1;
        database.getReference("HistoryRead").child(firebaseUser.getUid()).child(id).child("timeread").setValue(date_rightnow);


    }
    private void checkuserRead(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase2;
        DatabaseReference mDatabase3;
        mDatabase2 = database.getReference("CountreadUser").child(firebaseUser.getUid());
        mDatabase3 = database.getReference("CountreadUser").child(firebaseUser.getUid()).child(id);
        mDatabase3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(WritingActivity.this,dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                if(dataSnapshot.exists()){

                }
                else {
                    mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getChildrenCount()>=3){
                                checkPostRead();
                                Toast.makeText(WritingActivity.this,"Bạn đã hết lượt đọc trong ngày hôm nay.",Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                            else {
                                checkPostRead();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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
    // set view
    // limit bài viết đọc trong ngày
    public void setViewbyUser1(int count,String key){
        if(count <3){
            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            mDatabase = database1.getReference("CountreadUserby");
            count = count +1;
            mDatabase.child(firebaseUser.getUid()).child(key).child("count_day").setValue(count);
            addViewall();}

    }
    public void countViewOnedayUser(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CountreadUserby");
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase1;
        mDatabase1 = database1.getReference("CountreadUser").child(firebaseUser.getUid());
        mDatabase = database1.getReference("CountreadUserby").child(firebaseUser.getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    String a = dataSnapshot.getValue().toString();
                    int b = a.indexOf("day");
                    int c= a.indexOf("=");
                    String key=a.substring(1,c);
                    a=a.substring(b+4,b+5);
                    int count = Integer.parseInt(a);
                    String finalKey = key;
                    Long date_old =  Long.parseLong(finalKey);
                    long date_new = date_old + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);

                    Long date_rightnow = System.currentTimeMillis();
                    if(date_rightnow>=date_new){
                        ref.child(firebaseUser.getUid()).child(key).removeValue();
                        mDatabase1.removeValue();
                        mDatabase1.child(id).child("count").setValue(1);
                        countViewOnedayUser A = new countViewOnedayUser(1,date_rightnow);
                        ref.child(firebaseUser.getUid()).child(String.valueOf(date_rightnow)).setValue(A).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    else{
                        if(count<3) {
                            setViewbyUser1(count,key);
                        }
                    }
                }
                else{
                    Long date_rightnow = System.currentTimeMillis();
                    countViewOnedayUser a = new countViewOnedayUser(1,date_rightnow);
                    ref.child(firebaseUser.getUid()).child(String.valueOf(date_rightnow)).setValue(a).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void checkPostRead(){
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CountreadUser").child(firebaseUser.getUid());
        mDatabase = database1.getReference("CountreadUser").child(firebaseUser.getUid()).child(id);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(WritingActivity.this,dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                if(dataSnapshot.exists()){

                }
                else {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {

                            if(snapshot1.getChildrenCount()>=3){

                            }
                            else{
                                ref.child(id).child("count").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        // ...
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        // ...
                                    }
                                });

                            }
                            countViewOnedayUser();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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
    //đếm view
    public void setViewAll(int count){
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        mDatabase = database1.getReference("CountViewAll");
        mDatabase.child(id).child("count").setValue(count).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void setViewbyUser(int count,String key){
        if(count <3){
            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            mDatabase = database1.getReference("CountbyUserView");
            count = count +1;
            mDatabase.child(id).child(firebaseUser.getUid()).child(key).child("count").setValue(count);
            addViewall();}

    }
    public void addViewall(){
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CountViewAll");
        mDatabase = database1.getReference("CountViewAll").child(id).child("count");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(WritingActivity.this,dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                if(dataSnapshot.exists()){
                    int count = dataSnapshot.getValue(Integer.class);
                    //  Toast.makeText(WritingActivity.this,String.valueOf(count),Toast.LENGTH_SHORT).show();
                    count = count+1;
                    setViewAll(count);

                }
                else{
                    ref.child(id).child("count").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void countView(){
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CountbyUserView").child(id).child(firebaseUser.getUid());
        DatabaseReference mDatabase1;

        mDatabase1=database1.getReference("CountbyUserView");
        DatabaseReference mDatabase2;

        mDatabase2=database1.getReference("CountbyUserView");
        mDatabase = database1.getReference("CountbyUserView");
        mDatabase.keepSynced(false);
        mDatabase1.keepSynced(false);
        mDatabase2.keepSynced(false);
        mDatabase.child(id).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                  /*  for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                        CountView a = postsnap.getValue(CountView.class);
                        if(a.getCount() <=3) {a.setCount(a.getCount()+1);
                        setViewbyUser(a.getCount());
                        addViewall();}
                    }*/
                    mDatabase2.child(id).child(firebaseUser.getUid()).orderByChild("count")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //  Toast.makeText(WritingActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();

                                    String key = dataSnapshot.getValue().toString();
                                    int b = key.indexOf("=");
                                    key=key.substring(1,b);
                                    // Toast.makeText(WritingActivity.this,key, Toast.LENGTH_SHORT).show();
                                 /*   for (DataSnapshot postsnap : dataSnapshot.getChildren()) {

                                         key.add(postsnap.getKey()) ;
                                        Toast.makeText(WritingActivity.this,key.get(0), Toast.LENGTH_SHORT).show();
                                        //
                                    }*/
                                    String finalKey = key;
                                    Long date_old =  Long.parseLong(finalKey);
                                    long date_new = date_old + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
                                    //   Toast.makeText(WritingActivity.this,String.valueOf(date_old), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(WritingActivity.this,String.valueOf(date_new), Toast.LENGTH_SHORT).show();
                                    Long date_rightnow = System.currentTimeMillis();
                                    // Toast.makeText(WritingActivity.this,String.valueOf(date_rightnow), Toast.LENGTH_SHORT).show();
                                    if(date_rightnow<date_new) {
                                        mDatabase1.child(id).child(firebaseUser.getUid()).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                CountView a = snapshot.getValue(CountView.class);
                                                setViewbyUser(a.getCount(), finalKey);


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    else{
                                        mDatabase1.child(id).child(firebaseUser.getUid()).child(key).removeValue();
                                        CountView a = new CountView(1);
                                        final String timeStamp = String.valueOf(date_rightnow);
                                        a.setTimestamp(timeStamp);
                                        ref.child(a.getTimestamp().toString()).setValue(a).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                        addViewall();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                }

                else{
                    CountView a = new CountView(1);
                    final String timeStamp = String.valueOf(System.currentTimeMillis());
                    a.setTimestamp(timeStamp);
                    ref.child(a.getTimestamp().toString()).setValue(a).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    setViewAll(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    //set like
    private void isLiked(String postId, final ImageView imageView) {

        FirebaseDatabase.getInstance().getReference().child("Likes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // set account_like
    private void noOfLikes(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + " lượt thích");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //
    private void getComments(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText("Xem tất cả " + dataSnapshot.getChildrenCount() + " bình luận");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // set saved
    private void isSaved(final String postId, final ImageView image) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).exists()) {
                    image.setImageResource(R.drawable.ic_save_black);
                    image.setTag("saved");
                } else {
                    image.setImageResource(R.drawable.ic_save);
                    image.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //set Notification
    private void addNotification(String postId, String publisherId) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userid", firebaseUser.getUid());
        map.put("text", "đã thích bài viết này.");
        map.put("postid", postId);
        map.put("isPost", true);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(publisherId).push().setValue(map);
    }
    // điều kiện bị xóa bài: bài viết trên 100 view và có 40% lượt rp
    private void check_report(){
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CountViewAll");
        mDatabase = database1.getReference("CountViewAll").child(id);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(WritingActivity.this,dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                if(dataSnapshot.exists()){
                    int count_view = dataSnapshot.getValue(Integer.class);
                    if(count_view>=100) {
                        //  Toast.makeText(WritingActivity.this,String.valueOf(count),Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference().child("report_post").child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Long count_rp = dataSnapshot.getChildrenCount();
                                    double divide = count_rp / count_view;
                                    if (divide >= 0.4) {
                                        HashMap<String, Object> map = new HashMap<>();

                                        map.put("userid", "9b95nebBJvdDqH3zXt4q3eMxLo82");
                                        map.put("text", "bài viết của bạn đã bị xóa bởi Mod do vi phạm ... j đó éo biết viết");
                                        map.put("postid", id);
                                        map.put("isPost", true);

                                        FirebaseDatabase.getInstance().getReference().child("Notifications").child(id_user).push().setValue(map);
                                        FirebaseDatabase.getInstance().getReference().child("Posts").child(id).removeValue();

                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    private void report_post(){
        FirebaseDatabase.getInstance().getReference().child("report_post").child(id).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(WritingActivity.this,"Bạn đã report bài viết này",Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("report_post").child(id).child(firebaseUser.getUid()).setValue(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //more

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Thao tác với bài biết");
        getMenuInflater().inflate(R.menu.menu_writing, menu);
    }
    public void deletePost(){
        FirebaseDatabase.getInstance().getReference().child("PostsContent").child(postcontent_id).removeValue();
        FirebaseDatabase.getInstance().getReference().child("CountViewAll").child(id).removeValue();
        FirebaseDatabase.getInstance().getReference().child("CountbyUserView").child(id).removeValue();
        FirebaseDatabase.getInstance().getReference().child("Notifications").child(id_user).orderByChild("postid").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                   // for(DataSnapshot a :snapshot.getChildren()){
                   //     FirebaseDatabase.getInstance().getReference().child("Notifications").child(id_user).child(a.getKey()).removeValue();
                   // }
                    snapshot.getRef().setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Notifications").orderByChild("postid").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot a :snapshot.getChildren()){
                        Toast.makeText(WritingActivity.this,a.getKey(),Toast.LENGTH_SHORT);
                        //FirebaseDatabase.getInstance().getReference().child("Notifications").child(id_user).child(a.getKey()).removeValue();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Likes").child(id).removeValue();
        FirebaseDatabase.getInstance().getReference().child("HistoryRead").orderByValue().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Posts").child(id).removeValue();

    }

    public void onDeleteClick() {


        AlertDialog.Builder alert = new AlertDialog.Builder(WritingActivity.this);
        alert.setTitle("Xóa bài viết");
        alert.setMessage("Bạn có chắc muốn xóa bài viết không");
        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference().child("Notifications").orderByChild("postid").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for(DataSnapshot a :snapshot.getChildren()){
                                Toast.makeText(WritingActivity.this,a.getKey(),Toast.LENGTH_SHORT);
                                //FirebaseDatabase.getInstance().getReference().child("Notifications").child(id_user).child(a.getKey()).removeValue();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                onBackPressed();
                deletePost();
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
    public void onReportClick() {


        AlertDialog.Builder alert = new AlertDialog.Builder(WritingActivity.this);
        alert.setTitle("Báo cáo");
        alert.setMessage("Bạn có chắc muốn báo cáo bài viết không");
        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                report_post();
                onBackPressed();

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
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_edit_post:
                Toast.makeText(this,"Chức năng chưa được áp dụng",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_delete_post:
                if(id_user.equals(firebaseUser.getUid())){
                    onDeleteClick();
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int role = snapshot.getValue(Integer.class);
                            if(role==2) {
                                onDeleteClick();
                            }
                            else{
                                Toast.makeText(WritingActivity.this,"Bạn không có quyền xóa bài viết này",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                return true;
            case R.id.nav_report_post:
                onReportClick();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

}