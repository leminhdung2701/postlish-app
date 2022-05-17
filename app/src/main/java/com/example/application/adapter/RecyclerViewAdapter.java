package com.example.application.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.application.R;
import com.example.application.function.WritingActivity;
import com.example.application.postmanage.Post;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Post> mData ;


    public RecyclerViewAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Post post= mData.get(position);

        holder.txtTitle.setText(mData.get(position).getTitle());
        holder.txtName.setText(mData.get(position).getUserName());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, WritingActivity.class);
                // postId -> writingactivity
                /// add them cai content thi no eo nhan cai j, sua cai key id thành getPostcontent thì nó lại nhận được,
                intent.putExtra("Id",post.getTimeStamp().toString());
                intent.putExtra("Content",post.getPostContentId());
                intent.putExtra("Id_user",post.getUserId());
                intent.putExtra("Name",post.getUserName());
                intent.putExtra("Image",post.getPicture());
                intent.putExtra("Photo",post.getUserPhoto());
                // đang test cái name: title là post_id
                intent.putExtra("category",post.getCategory_post());
                intent.putExtra("time",post.getTimeStamp().toString());
                intent.putExtra("Title",post.getTitle());
                //intent.putExtra("Title",post.getTitle());
                /*
                // passing data to the book activity
                intent.putExtra("Title",mData.get(position).getTitle());
                intent.putExtra("Description",mData.get(position).getDescription());
                intent.putExtra("Thumbnail",mData.get(position).getThumbnail());
                */
                // start the activity
                mContext.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView ;
        TextView txtTitle, txtName;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.textview_title_id) ;
            txtName = (TextView) itemView.findViewById(R.id.textview_name_id) ;
            imageView = (ImageView) itemView.findViewById(R.id.book_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);

        }
    }


}
