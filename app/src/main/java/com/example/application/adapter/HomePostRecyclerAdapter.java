package com.example.application.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.application.R;
import com.example.application.function.WritingActivity;
import com.example.application.postmanage.Post;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HomePostRecyclerAdapter extends RecyclerView.Adapter<HomePostRecyclerAdapter.ItemViewHolder> {

    Context context;
    List<Post> list;

    public HomePostRecyclerAdapter(Context context, List<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.cat_recycler_row_item,parent,false));
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_row_item_home,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Post post= list.get(position);

        holder.textTitle.setText(list.get(position).getTitle());
        holder.textName.setText(list.get(position).getUserName());
        Long a = Long.parseLong(list.get(position).getTimeStamp().toString());
        holder.time.setText(getDate(a));
        Glide.with(context).load(list.get(position).getPicture()).into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, WritingActivity.class);
                intent.putExtra("Id",post.getTimeStamp().toString());
                intent.putExtra("Content",post.getPostContentId());
                intent.putExtra("Id_user",post.getUserId());
                intent.putExtra("Name",post.getUserName());
                intent.putExtra("Image",post.getPicture());
                intent.putExtra("Photo",post.getUserPhoto());
                intent.putExtra("category",post.getCategory_post());
                intent.putExtra("time",post.getTimeStamp().toString());
                // đang test cái name: title là post_id
                intent.putExtra("Title",post.getTitle());
                /*
                // passing data to the book activity
                intent.putExtra("Title",mData.get(position).getTitle());
                intent.putExtra("Description",mData.get(position).getDescription());
                intent.putExtra("Thumbnail",mData.get(position).getThumbnail());
                */
                // start the activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder{


        ImageView imageView;
        LinearLayout linearLayout;
        TextView textName;
        TextView textTitle;
        TextView time;
        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);

            textTitle=itemView.findViewById(R.id.textview_Desc);
            linearLayout=itemView.findViewById(R.id.linear_item_home);
            imageView = itemView.findViewById(R.id.image_post);
            textName =itemView.findViewById(R.id.textview_userName);
            time =itemView.findViewById(R.id.timehome);
        }
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
}
