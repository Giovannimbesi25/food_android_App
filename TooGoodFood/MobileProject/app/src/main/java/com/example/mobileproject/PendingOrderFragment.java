
//This Fragment show all the orders submitted by the user
//It consists of a title and a recycle view
package com.example.mobileproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.Adapter.PO_Adapter;
import com.example.mobileproject.Interface.OnItemClickListener;
import com.example.mobileproject.Model.PendingOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PendingOrderFragment extends Fragment  implements OnItemClickListener {

    private OnItemClickListener listener;
    private RecyclerView pending_order_recycle_view;
    private ArrayList<PendingOrder> list;
    private PO_Adapter adapter;
    private TextView pending_order_fragment_title;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    public PendingOrderFragment() {
        // Required empty public constructor
    }


    //Initialize all layout elements
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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


        //This function manages the recycle view and shows all the submitted orders
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

    //At this time the click on the Product in the PendingOrderFragment isn't managed, but in the future may be a new functionality

    @Override
    public void onItemClick(int position) {
        System.out.println("Cliccato");
    }
}