//The goal of this class is to manage te recycle view showed in Current Order Fragments

package com.example.mobileproject.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.Interface.OnItemClickListener;
import com.example.mobileproject.Model.ProductOrdered;
import com.example.mobileproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CO_Adapter extends RecyclerView.Adapter<CO_Adapter.MyViewHolder> {

    //Basic Parameters

    private final OnItemClickListener listener;
    private final Context context;
    private final ArrayList<ProductOrdered> list;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    //The CO_Adapter Class has a list which contains all the current order products selected by the user
    //the listener is used to manage the onclick method
    public CO_Adapter(Context context, ArrayList<ProductOrdered> list, OnItemClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.list = list;
    }

    //Set the Layout
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_ordered_layout, parent, false);
        return new MyViewHolder(v, listener);
    }


    //For each product selected, create a new ProductOrdered object to store its features which will be showed
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductOrdered productOrdered = list.get(position);

        holder.Name.setText(productOrdered.getName());
        holder.Prezzo.setText("Price: "+productOrdered.getPrezzo());
        holder.Quantity.setText("Quantity: "+productOrdered.getQuantity());
        holder.Total.setText("Totale Prodotti: "+productOrdered.getTotale());

        holder.bind(listener);


        //When delete button icon is clicked the product will be removed from the current order
        holder.product_order_deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProductFromCurrentOrder(list, productOrdered);

            }
        });

    }


    //This method allow the user to delete a product from the current order
    private void deleteProductFromCurrentOrder(ArrayList<ProductOrdered> list, ProductOrdered productOrdered){
        //Create an alert to prevent possible involuntary click
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Product");
        builder.setMessage("Delete product from order?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Get the reference to the product selected and delete it
                        Toast.makeText(context, "Product removed from order", Toast.LENGTH_SHORT).show();
                        database=FirebaseDatabase.getInstance();
                        ref = database.getReference();
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser fuser = mAuth.getCurrentUser();
                        assert fuser != null;
                        String fuid = fuser.getUid();
                        //Delete the product from the root CurrentOrder at the UID specified

                        ref.child("users").child(fuid).child("CurrentOrder").child(productOrdered.getName()).removeValue();

                        //then remove the product from the list
                        list.remove(productOrdered);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    //This class initialize all the ProductOrdered features
    public static class MyViewHolder  extends  RecyclerView.ViewHolder{
        //Every productOrdered object has a Name, a price a quantity and a corresponding total
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


        //This method is used to manage the onclick method of OnItemClickListener interface
        public void bind(final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(getAbsoluteAdapterPosition());
                }
            });
        }
    }
}
