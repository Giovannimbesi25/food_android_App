
//This Fragment simply shows the current users properties, allows him to change is address, email, password or logout
package com.example.mobileproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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

    private TextView profile_fragment_email, profile_fragment_name, profile_fragment_address;
    private Button profile_fragment_logoutBTN, profile_fragment_addressbtn, profile_fragment_emailbtn, profile_fragment_passwbtn;
    private String nuovo_indirizzo, nuova_email, nuova_password;
    private String current_email, current_password;


    public ProfileFragment() {
        // Required empty public constructor
    }

    //Initialize all the elements
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Initialize layout element
        profile_fragment_addressbtn = view.findViewById(R.id.profile_fragment_addressbtn);
        profile_fragment_email = view.findViewById(R.id.profile_fragment_email);
        profile_fragment_name = view.findViewById(R.id.profile_fragment_name);
        profile_fragment_logoutBTN = view.findViewById(R.id.profile_fragment_logoutBTN);
        profile_fragment_address = view.findViewById(R.id.profile_fragment_address);
        profile_fragment_emailbtn = view.findViewById(R.id.profile_fragment_emailbtn);
        profile_fragment_passwbtn = view.findViewById(R.id.profile_fragment_passbtn);


        fuser = mAuth.getCurrentUser();
        String fuid = fuser.getUid();

        //If the current user exist, report his information
        if (fuser != null) {
            ref = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = (String) dataSnapshot.child("username").getValue();
                    String email = (String) dataSnapshot.child("email").getValue();
                    String address = (String) dataSnapshot.child("address").getValue();
                    String password = (String) dataSnapshot.child("password").getValue();

                    current_email = email;
                    current_password = password;


                    profile_fragment_email.setText("Email \n " + email);
                    profile_fragment_name.setText("Username \n" + name);
                    profile_fragment_address.setText("Address \n" + address);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Buttons onclick initialization
            profile_fragment_emailbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeEmail();
                }
            });

            profile_fragment_passwbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePassword();
                }
            });


            //The button logout allow the user to exit and login with another account

            profile_fragment_logoutBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutAlert();
                }
            });


            //This button allow the user to change is address
            profile_fragment_addressbtn.setOnClickListener(new View.OnClickListener() {
                String fuid = mAuth.getCurrentUser().getUid();

                @Override
                public void onClick(View v) {
                    //Create a simple alert to insert the new address
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Change Address");
                    final EditText input = new EditText(view.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nuovo_indirizzo = input.getText().toString();
                            //Change layout element edit text
                            profile_fragment_address.setText("Address: " + nuovo_indirizzo);
                            //Commit changes to database
                            ref = database.getReference();
                            ref.child("users").child(fuid).child("address").setValue(nuovo_indirizzo);
                            Toast.makeText(view.getContext(), "Address Changed", Toast.LENGTH_SHORT).show();

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

    private void logoutAlert() {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Do you want to exit?");
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


    public void changeEmail() {
        String fuid = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("users").child(fuid);

        //First we need the current user credentials
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_email = (String) dataSnapshot.child("email").getValue();
                current_password = (String) dataSnapshot.child("password").getValue();

                System.out.println(current_email + current_password );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //Simple dialog to insert the new value
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Email");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nuova_email = input.getText().toString();


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Get auth credentials from the user for re-authentication
                AuthCredential credential = EmailAuthProvider.getCredential(current_email, current_password); // Current Login Credentials
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Update the email with the updateEmail Firebase's built in method
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(nuova_email);

                        profile_fragment_email.setText("Email: " + nuova_email);
                        ref = database.getReference();
                        ref.child("users").child(fuid).child("email").setValue(nuova_email);
                    }
                });

                Toast.makeText(getContext(), "Email Changed", Toast.LENGTH_SHORT).show();
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


    public void changePassword(){
        String fuid = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("users").child(fuid);

        //First we need the current user credentials
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_email = (String) dataSnapshot.child("email").getValue();
                current_password = (String) dataSnapshot.child("password").getValue();

                System.out.println(current_email + current_password );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //With an alert we can insert a new value for password
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Password");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nuova_password = input.getText().toString();


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Get auth credentials from the user for re-authentication
                AuthCredential credential = EmailAuthProvider.getCredential(current_email, current_password); // Current Login Credentials
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Now change your email address
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updatePassword(nuova_password);


                        ref = database.getReference();
                        ref.child("users").child(fuid).child("password").setValue(nuova_password);
                    }
                });
                Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_SHORT).show();
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
}
