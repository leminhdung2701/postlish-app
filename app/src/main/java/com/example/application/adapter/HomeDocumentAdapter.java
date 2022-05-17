package com.example.application.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.application.R;
import com.example.application.function.WritingActivity;
import com.example.application.postmanage.Post;

import java.util.List;

public class HomeDocumentAdapter extends PagerAdapter {

    Context context;
    List<Post> list;

    public HomeDocumentAdapter(Context context, List<Post> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_documnet_layout,null);
        Post post= list.get(position);
        ImageView bannerImage = view.findViewById(R.id.image_banner_document);
        bannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WritingActivity.class);
                intent.putExtra("Id",post.getTimeStamp().toString());
                intent.putExtra("Content",post.getPostContentId());
                intent.putExtra("Id_user",post.getUserId());
                intent.putExtra("Name",post.getUserName());
                intent.putExtra("Image",post.getPicture());
                intent.putExtra("category",post.getCategory_post());
                intent.putExtra("time",post.getTimeStamp().toString());
                intent.putExtra("Photo",post.getUserPhoto());
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
        if(list.get(position).getPicture()!=null){
        Glide.with(context).load(list.get(position).getPicture()).into(bannerImage);}
        container.addView(view);

        return view;

    }
}
