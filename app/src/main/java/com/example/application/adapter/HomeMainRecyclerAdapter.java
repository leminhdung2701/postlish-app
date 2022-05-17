package com.example.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.model.AllPost;
import com.example.application.postmanage.Post;

import java.util.List;

public class HomeMainRecyclerAdapter extends RecyclerView.Adapter<HomeMainRecyclerAdapter.MainViewHolder> {

    Context context;
    List<AllPost> allPostList;

    public HomeMainRecyclerAdapter(Context context, List<AllPost> allPostList) {
        this.context = context;
        this.allPostList = allPostList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_recycler_row_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.postName.setText(allPostList.get(position).getTitle());
        setItemRecycler(holder.recyclerView, allPostList.get(position).getPostList());
    }

    @Override
    public int getItemCount() {
        return allPostList.size();
    }

    public static final class MainViewHolder  extends RecyclerView.ViewHolder{

        TextView postName;
        RecyclerView recyclerView;

        public MainViewHolder(@NonNull View itemView){
            super(itemView);

            postName =itemView.findViewById(R.id.cat_title);
            recyclerView =itemView.findViewById(R.id.recycler_view);
        }
    }

    private void setItemRecycler(RecyclerView recyclerView, List<Post> postList){

        HomePostRecyclerAdapter itemRecyclerAdapter = new HomePostRecyclerAdapter(context,postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(itemRecyclerAdapter);
    }
}
