package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.Interface.OnItemClickListener;
import com.example.mobileproject.Model.ProductPreview;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    OnItemClickListener listener;
    Context context;
    ArrayList<ProductPreview> list;

    public MyAdapter(){}

    public MyAdapter(Context context, ArrayList<ProductPreview> list, OnItemClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_items_layout, parent, false);
        return new MyViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductPreview productPreview = list.get(position);

        holder.Name.setText(productPreview.getName());
        Glide.with(holder.Image.getContext()).load(productPreview.getImage()).into(holder.Image);
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder  extends  RecyclerView.ViewHolder{
        TextView Name;
        ImageView Image;
        OnItemClickListener o;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            Name = itemView.findViewById(R.id.product_name);
            Image = itemView.findViewById(R.id.product_image);
            o= onItemClickListener;
        }

        public void bind(final ProductPreview item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(getAbsoluteAdapterPosition());
                }
            });
        }


    }




}


























