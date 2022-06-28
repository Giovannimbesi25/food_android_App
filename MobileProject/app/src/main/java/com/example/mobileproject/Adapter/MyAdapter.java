
//The MyAdapter class manages all the product in the HomeFragment

package com.example.mobileproject.Adapter;

import android.content.Context;
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
import com.example.mobileproject.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private OnItemClickListener listener;
    private Context context;
    private ArrayList<ProductPreview> list;

    public MyAdapter(){}

    //The adapter takes a list as parameter to manage the list of ProductPreview and a listener to perform onclick method of OnItemClickListener Interface
    public MyAdapter(Context context, ArrayList<ProductPreview> list, OnItemClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.list = list;
    }

    //Set the layout of each item(CardView)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_items_layout, parent, false);
        return new MyViewHolder(v, listener);
    }


    //This function bind the holder to the product preview returned by the list at a certain position
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

    //Name and Image are product's attributes
    public static class MyViewHolder  extends  RecyclerView.ViewHolder{
        TextView Name;
        ImageView Image;
        OnItemClickListener o;


        //All the product in the recycle view has a title and an image
        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            Name = itemView.findViewById(R.id.product_name);
            Image = itemView.findViewById(R.id.product_image);
            o= onItemClickListener;
        }


        //The Bind function is very usefull to manage the click action
        public void bind(final ProductPreview item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(getAbsoluteAdapterPosition());
                }
            });
        }


    }




}


























