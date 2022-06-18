package com.example.mobileproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobileproject.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FirebaseUser fuser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    TextView profile_fragment_email, profile_fragment_name, profile_fragment_address;
    Button  profile_fragment_logoutBTN, profile_fragment_addressbtn;
    String nuovo_indirizzo;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //elementi grafici edit = view.find

        nuovo_indirizzo = "";

        database=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Initialize layout element
        profile_fragment_addressbtn = view.findViewById(R.id.profile_fragment_addressbtn);
        profile_fragment_email = view.findViewById(R.id.profile_fragment_email);
        profile_fragment_name = view.findViewById(R.id.profile_fragment_name);
        profile_fragment_logoutBTN = view.findViewById(R.id.profile_fragment_logoutBTN);
        profile_fragment_address = view.findViewById(R.id.profile_fragment_address);
        profile_fragment_logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleDialog();
            }
        });



        fuser = mAuth.getCurrentUser();
        String fuid = fuser.getUid();

        if (fuser != null) {
           ref=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
           ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = (String) dataSnapshot.child("username").getValue();
                    String email = (String) dataSnapshot.child("email").getValue();
                    String address = (String) dataSnapshot.child("address").getValue();


                    System.out.println(name);
                    System.out.println(email);
                    System.out.println(address);


                    profile_fragment_email.setText("Email \n " + email);
                    profile_fragment_name.setText("Username \n"+ name);
                    profile_fragment_address.setText("Address \n"+address);

                     Integer count = 0;
               }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

           profile_fragment_logoutBTN.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    showSimpleDialog();
               }
           });

           profile_fragment_addressbtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                   builder.setTitle("Change Address");
                   final EditText input = new EditText(view.getContext());
                   input.setInputType(InputType.TYPE_CLASS_TEXT);
                   builder.setView(input);
                   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           System.out.println("Sono dentro l'OK");
                           nuovo_indirizzo = input.getText().toString();
                           System.out.println("NUOVO INDIRIZZO "+nuovo_indirizzo);
                           profile_fragment_address.setText("Address: "+nuovo_indirizzo);
                           System.out.println(profile_fragment_address.getText().toString());
                           ref = database.getReference();
                           ref.child("users").child(fuid).child("address").setValue(nuovo_indirizzo);
                       }
                   });
                   builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                       }
                   });

                   builder.show();




               }
           });


        }

        return view;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private void showSimpleDialog(){
        System.out.println("Ciaooo");

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Do you want to exit?");
        System.out.println("Ciaooo");
        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'Yes' Button
                        //exit application
                        logout();
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

}