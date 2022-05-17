package com.example.application.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.application.R;
import com.example.application.function.ProfileActivity;
import com.example.application.model.ItemAnimation;
import com.example.application.model.SearchDetails;
import com.example.application.model.UserData;
import com.example.application.model.UserDetails;
import com.example.application.postmanage.Post;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter
        extends RecyclerView.Adapter<SearchAdapter.RecyclerviewHolder> {

    Context context;
    List<Post> postDataList;
    List<Post> filteredPostDataList;

    public SearchAdapter(Context context, List<Post> userDataList) {
        this.context = context;
        this.postDataList = userDataList;
        this.filteredPostDataList = userDataList;
    }


    @NonNull
    @Override
    public RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row_item_search, parent, false);
        return new RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewHolder holder, final int position) {

        holder.postName.setText(filteredPostDataList.get(position).getTitle());
        holder.postDesc.setText(filteredPostDataList.get(position).getUserName());


        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .transform(new RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .override(holder.postImage.getWidth(),holder.postImage.getHeight());
        Glide
                .with(context)
                .load(filteredPostDataList.get(position).getPicture())
                .apply(reqOpt)
                .into(holder.postImage);

        ItemAnimation.animateFadeIn(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("postname", filteredPostDataList.get(position).getUserName());
                intent.putExtra("postDesc", filteredPostDataList.get(position).getTitle());
                intent.putExtra("userId",filteredPostDataList.get(position).getUserId());
                intent.putExtra("nameUser",filteredPostDataList.get(position).getUserName());
                intent.putExtra("photoUser",filteredPostDataList.get(position).getUserPhoto());
                context.startActivity(intent);
            }
        });

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Post Name Clicked", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredPostDataList.size();
    }

    public static final class RecyclerviewHolder extends RecyclerView.ViewHolder {


        ImageView postImage;
        TextView postName, postDesc;

        public RecyclerviewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.search_image_post);
            postName = itemView.findViewById(R.id.search_textview_userName);
            postDesc = itemView.findViewById(R.id.search_textview_Desc);


        }
    }

    public Filter getFilter(){

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    filteredPostDataList = postDataList;
                }
                else{

                    List<Post> lstFiltered = new ArrayList<>();
                    for(Post row: postDataList){
                        if(row.getUserName().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);

                        }
                    }

                    filteredPostDataList = lstFiltered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredPostDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                filteredPostDataList = (List<Post>)filterResults.values;
                notifyDataSetChanged();

            }
        };

    }


}
