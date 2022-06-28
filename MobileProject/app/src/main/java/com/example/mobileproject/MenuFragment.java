//The Menu Fragment show all the product in the database, when the user click an item, he'll sees the corresponding details activity
package com.example.mobileproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.Adapter.MyAdapter;
import com.example.mobileproject.Interface.OnItemClickListener;
import com.example.mobileproject.Model.ProductPreview;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MenuFragment extends Fragment implements  OnItemClickListener{

    private OnItemClickListener listener;
    private RecyclerView recyclerView;
    private ArrayList<ProductPreview> list;
    private DatabaseReference databaseReference;
    private MyAdapter adapter;
    private FirebaseAuth mAuth;
    private FloatingActionButton admin_insert;


    public MenuFragment() {
        // Required empty public constructor
    }

    //Initialize all layout elements
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycle_view);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new MyAdapter(view.getContext(), list, this::onItemClick);
        recyclerView.setAdapter(adapter);

        admin_insert = view.findViewById(R.id.admin_insert);
        if(mAuth.getCurrentUser().getUid().equals("42ng3HSgdmPUil5K6Zs4HoQW1kx1")) {
            Toast.makeText(view.getContext(), "Ciao BOSS" , Toast.LENGTH_SHORT).show();

            admin_insert.setVisibility(View.VISIBLE);
            admin_insert.setClickable(true);
        }

        //When button is clicked it calls the right function
        admin_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InsertNewProduct(list);
            }
        });

        //For each change in the list, the list is clear and fill again, to commit changes.
        databaseReference.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
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

    //When a preview is clicked the user is sent to the corresponding FoodDetails Activity
    @Override
    public void onItemClick(int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        System.out.println(list.get(position).getDescrizione());

        Intent intent = new Intent(getContext(), FoodDetails.class);
        intent.putExtra("Prodotto", list.get(position));
        startActivity(intent);

    }


    //When the insert button is clicked, allow the admin(and only him) to insert a new Product
    //The logic is very simple, it creates a dialog with some entries which have to be filled.
    private boolean InsertNewProduct(ArrayList<ProductPreview> list){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        System.out.println(list.size());

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());

        LinearLayout lila1= new LinearLayout(getContext());
        lila1.setOrientation(LinearLayout.VERTICAL);
        EditText inputName = new EditText(getContext());
        EditText inputDescription = new EditText(getContext());
        EditText inputPrezzo = new EditText(getContext());
        EditText inputImage = new EditText(getContext());
        EditText inputRating = new EditText(getContext());
        inputName.setHint("Product Name: Pizza Norma");
        inputDescription.setHint("Description: Salsa di pomodoro, mozzarella, melanzane ..");
        inputPrezzo.setHint("Product Price: 4.50");
        inputRating.setHint("Rating: 0, 0.5, 1, 1.5, ... 5");
        inputImage.setHint("Product Image URL!");
        lila1.addView(inputName);
        lila1.addView(inputDescription);
        lila1.addView(inputPrezzo);
        lila1.addView(inputRating);
        lila1.addView(inputImage);

        builder.setView(lila1);
        builder.setTitle("Insert new Product");
        builder.setMessage("Follow Hints Format");

        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'Yes' Button


                        //Each field have to be filled
                        if(inputPrezzo.getText().toString().isEmpty() == false && inputName.getText().toString().isEmpty() == false &&
                                inputImage.getText().toString().isEmpty() == false && inputDescription.getText().toString().isEmpty() == false
                                && inputRating.getText().toString().isEmpty() == false) {

                            ProductPreview productPreview = new ProductPreview();

                            productPreview.setName(inputName.getText().toString());
                            productPreview.setImage(inputImage.getText().toString());
                            productPreview.setDescrizione(inputDescription.getText().toString());
                            productPreview.setPrezzo(inputPrezzo.getText().toString());
                            productPreview.setRating(inputRating.getText().toString());

                            //A new ProductPreview child  is added to Products root of Firebase'database
                            databaseReference.child("Products").child(productPreview.getName()).setValue(productPreview);

                            Toast.makeText(getContext(), "New Product Created", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(getContext(), FoodDetails.class);
                            intent.putExtra("Prodotto", productPreview);
                            startActivity(intent);

                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Please Fill Each Field", Toast.LENGTH_SHORT).show();

                    }
                });

        //Creating dialog box
        androidx.appcompat.app.AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();

        return true;
    }
}

