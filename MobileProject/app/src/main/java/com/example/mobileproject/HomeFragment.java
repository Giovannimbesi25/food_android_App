package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mobileproject.Interface.OnItemClickListener;
import com.example.mobileproject.Model.ProductPreview;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements  OnItemClickListener{

    OnItemClickListener listener;
    RecyclerView recyclerView;
    ArrayList<ProductPreview> list;
    DatabaseReference databaseReference;
    MyAdapter adapter;
    FirebaseAuth mAuth;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("Ciao sono nel fragment home");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycle_view);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new MyAdapter(view.getContext(), list, this::onItemClick);
        recyclerView.setAdapter(adapter);



        /*recyclerView.setAdapter(new MyAdapter(view.getContext(), list, new MyAdapter().OnItemClickListener(){
            public void onItemClick(ProductPreview item) {
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
            }
        }));  */




        databaseReference.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ProductPreview productPreview = dataSnapshot.getValue(ProductPreview.class);
                    list.add(productPreview);
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
        System.out.println(list.get(position).getDescrizione());
        if(mAuth.getUid().equals("42ng3HSgdmPUil5K6Zs4HoQW1kx1")) {
            Intent intent = new Intent(getContext(), FoodDetailAdmin.class);
            intent.putExtra("Prodotto", list.get(position));
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getContext(), FoodDetail.class);
            intent.putExtra("Prodotto", list.get(position));
            startActivity(intent);
        }
    }
}