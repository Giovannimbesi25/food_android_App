package com.example.mobileproject;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.Interface.OnItemClickListener;
import com.example.mobileproject.Model.PendingOrder;
import com.example.mobileproject.Model.ProductOrdered;
import com.example.mobileproject.Model.ProductPreview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PO_Adapter extends RecyclerView.Adapter<PO_Adapter.MyViewHolder> {

    OnItemClickListener listener;
    Context context;
    ArrayList<PendingOrder> list;

    private FirebaseUser fuser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    public PO_Adapter(){}

    public PO_Adapter(Context context, ArrayList<PendingOrder> list, OnItemClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pending_order_layout, parent, false);
        return new MyViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PendingOrder pendingOrder = list.get(position);

        holder.Indirizzo.setText("Address: "+ pendingOrder.getIndirizzo());
        holder.DateTime.setText("Date: "+pendingOrder.getDateTime());
        holder.Totale.setText("Total "+pendingOrder.getTotale());

        holder.bind(list.get(position), listener);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder  extends  RecyclerView.ViewHolder{
        TextView Totale, DateTime, Indirizzo;

        OnItemClickListener o;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            Totale = itemView.findViewById(R.id.pending_order_total);
            DateTime = itemView.findViewById(R.id.pending_order_date);
            Indirizzo = itemView.findViewById(R.id.pending_order_address);

            o= onItemClickListener;
        }



        public void bind(final PendingOrder item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(getAbsoluteAdapterPosition());
                }
            });
        }
    }
}
