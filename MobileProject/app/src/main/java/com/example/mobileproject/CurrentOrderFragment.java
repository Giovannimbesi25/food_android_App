//This Fragment Show all products added by the user in the current order
package com.example.mobileproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.Adapter.CO_Adapter;
import com.example.mobileproject.Interface.OnItemClickListener;
import com.example.mobileproject.Model.PendingOrder;
import com.example.mobileproject.Model.ProductOrdered;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CurrentOrderFragment extends Fragment  implements OnItemClickListener {


    private RecyclerView recyclerView;
    private ArrayList<ProductOrdered> list;
    private CO_Adapter adapter;
    private TextView current_order_title;
    private Button current_order_btn;
    float totale_ordini;

    private DatabaseReference ref;



    public CurrentOrderFragment() {
        // Required empty public constructor
    }

    //Initialize all layout elements and fragment parameters
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_order, container, false);

        current_order_title = view.findViewById(R.id.current_order_title);
        current_order_btn = view.findViewById(R.id.current_order_btn);
        recyclerView = view.findViewById(R.id.recycle_view_current_order);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new CO_Adapter(view.getContext(), list, this::onItemClick);
        recyclerView.setAdapter(adapter);


        //Firebase reference
        ref = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser fuser = mAuth.getCurrentUser();
        String fuid = fuser.getUid();

        //This Method allow to show the current order preview displaying all products added.
        ref.child("users").child(fuid).child("CurrentOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean trovato=false;
                Integer position = 0;
                totale_ordini = 0;

                //this Script allow to not show double elements if there is some change(product deleted or modified)
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ProductOrdered productOrdered = dataSnapshot.getValue(ProductOrdered.class);

                    for (ProductOrdered po : list) {
                        if (po.getName() == productOrdered.getName()) {
                            trovato = true;
                            position = list.indexOf(productOrdered);
                        }
                    }
                    if (trovato == false) {
                        list.add(productOrdered);
                        totale_ordini += Float.parseFloat(productOrdered.getTotale());

                    }
                    else {
                        list.remove(position);
                        totale_ordini += Float.parseFloat(productOrdered.getTotale());
                }

                }
                updateOrderTotal(totale_ordini);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //This method allow the user to submit an order(only if it is not empty)
        current_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference();

                if(totale_ordini > 0) {

                    String string_total_order = String.valueOf(totale_ordini);

                    ref.child("users").child(fuid).child("address").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                            } else {

                                //Get current Date
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
                                String address = (String) task.getResult().getValue();
                                Date date = new Date(System.currentTimeMillis());

                                //Get address of current user
                                String dataOra = (formatter.format(date));

                                //Create new object PendingOrder
                                PendingOrder pendingOrder = new PendingOrder(string_total_order, dataOra, address);


                                ref = FirebaseDatabase.getInstance().getReference();
                                ref.child("users").child(fuid).child("PendingsOrder").child(pendingOrder.getDateTime()).setValue(pendingOrder);

                                Toast.makeText(view.getContext(), "Order Submitted", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                    //The object is added to the root Current order at the user specified by UID
                    ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("users").child(fuid).child("CurrentOrder").removeValue();
                    list.clear();
                }



                else {
                    Toast.makeText(view.getContext(), "Please insert at least one product", Toast.LENGTH_SHORT).show();
                }

            }

        });
        return view;
    }


    //Update the Total every time a new product is added, deleted or the order is submitted
    private void updateOrderTotal(float totale_ordini) {
        current_order_title.setText("Totale Ordine \n     " + String.format("%.2f", totale_ordini));
    }

    //At this time the onClick method in the CurrentOrderFragment isn't managed, but in the future may be implemented a new functionality
    @Override
    public void onItemClick(int position) {
        System.out.println("Item clicked");
    }
}