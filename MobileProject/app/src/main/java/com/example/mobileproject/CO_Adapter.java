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
import com.example.mobileproject.Model.ProductOrdered;
import com.example.mobileproject.Model.ProductPreview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CO_Adapter extends RecyclerView.Adapter<CO_Adapter.MyViewHolder> {

    OnItemClickListener listener;
    Context context;
    ArrayList<ProductOrdered> list;

    private FirebaseUser fuser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    public CO_Adapter(){}

    public CO_Adapter(Context context, ArrayList<ProductOrdered> list, OnItemClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_ordered_layout, parent, false);
        return new MyViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductOrdered productOrdered = list.get(position);

        holder.Name.setText(productOrdered.getName());
        holder.Prezzo.setText("Price: "+productOrdered.getPrezzo());
        holder.Quantity.setText("Quantity: "+productOrdered.getQuantity());
        holder.Total.setText("Totale Prodotti: "+productOrdered.getTotale());

        holder.bind(list.get(position), listener);

        holder.product_order_deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Deleted Order Button clicked");
                showSimpleDialog(list, productOrdered);
                System.out.println("Function Finished");

            }
        });

    }

    private void showSimpleDialog(ArrayList<ProductOrdered> list, ProductOrdered productOrdered){
        System.out.println("Sono nella show simple Dialog di CO ADAPTER");
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure to delete this ?");
        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context, "Product removed from order", Toast.LENGTH_SHORT).show();
                        database=FirebaseDatabase.getInstance();
                        ref = database.getReference();
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser fuser = mAuth.getCurrentUser();
                        String fuid = fuser.getUid();
                        System.out.println("Time before remove");

                        ref.child("users").child(fuid).child("CurrentOrder").child(productOrdered.getName()).removeValue();
                        list.remove(productOrdered);
                        System.out.println("Element removed");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Message Title");
        alert.show();
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder  extends  RecyclerView.ViewHolder{
        TextView Name, Prezzo, Quantity, Total;
        Button product_order_deleteBTN;

        OnItemClickListener o;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            Name = itemView.findViewById(R.id.product_order_name);
            Prezzo = itemView.findViewById(R.id.product_order_price);
            Quantity = itemView.findViewById(R.id.product_order_quantity);
            Total = itemView.findViewById(R.id.product_order_total);
            product_order_deleteBTN = itemView.findViewById(R.id.product_order_deleteBTN);

            o= onItemClickListener;
        }



        public void bind(final ProductOrdered item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(getAbsoluteAdapterPosition());
                }
            });
        }
    }
}
