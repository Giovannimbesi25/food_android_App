//Very close to the CO_Adapter and MyAdapter but with different elements

package com.example.mobileproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.Interface.OnItemClickListener;
import com.example.mobileproject.Model.PendingOrder;
import com.example.mobileproject.R;

import java.util.ArrayList;

public class PO_Adapter extends RecyclerView.Adapter<PO_Adapter.MyViewHolder> {


    //This adapter takes as parameter a list of PendingOrder objects
    private OnItemClickListener listener;
    private Context context;
    private ArrayList<PendingOrder> list;


    public PO_Adapter(){}

    //The adapter takes a list of PenginOrder objects. The listener could be used to perform the onclick method of OnItemClickListener interface
    public PO_Adapter(Context context, ArrayList<PendingOrder> list, OnItemClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.list = list;
    }

    //Layout settings
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pending_order_layout, parent, false);
        return new MyViewHolder(v, listener);
    }


    //Bind the holder to the Pending Order object
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PendingOrder pendingOrder = list.get(position);

        holder.Indirizzo.setText("Address: "+ pendingOrder.getIndirizzo());
        holder.DateTime.setText("Date: "+pendingOrder.getDateTime());
        holder.Totale.setText("Total "+pendingOrder.getTotale());

        holder.bind(list.get(position), listener);

    }

    //Each PendingOrder takes three parameters: a total, date and Address.
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder  extends  RecyclerView.ViewHolder{
        TextView Totale, DateTime, Indirizzo;

        OnItemClickListener o;


        //Initialize the elements of the Pending Order
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
