package com.example.mobileproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobileproject.Interface.OnItemClickListener;
import com.example.mobileproject.Model.PendingOrder;
import com.example.mobileproject.Model.ProductOrdered;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PendingOrderFragment extends Fragment  implements OnItemClickListener {

    OnItemClickListener listener;
    RecyclerView pending_order_recycle_view;
    ArrayList<PendingOrder> list;
    PO_Adapter adapter;
    TextView pending_order_fragment_title;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    public PendingOrderFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("Ciao sono nel fragment pending order");
        View view = inflater.inflate(R.layout.fragment_pending_order, container, false);

        //Initialize layout object and PO_Adapter
        pending_order_fragment_title = view.findViewById(R.id.pending_order_fragment_title);
        pending_order_recycle_view = view.findViewById(R.id.pending_order_recycle_view);
        list = new ArrayList<>();
        pending_order_recycle_view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new PO_Adapter(view.getContext(), list, this::onItemClick);
        pending_order_recycle_view.setAdapter(adapter);

        //Database reference
        ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fuser = mAuth.getCurrentUser();
        String fuid = fuser.getUid();



        ref.child("users").child(fuid).child("PendingsOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PendingOrder pendingOrder = dataSnapshot.getValue(PendingOrder.class);
                    list.add(pendingOrder);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }

    @Override
    public void onItemClick(int position) {
        System.out.println("Cliccato");
    }
}