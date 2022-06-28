package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText register_email, register_password,register_user, register_address;
    private Button btnRegister;
    private TextView textLogin;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Firebase reference to insert the new user into the database
        database=FirebaseDatabase.getInstance();
        ref = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_user = findViewById(R.id.register_user);
        register_address = findViewById(R.id.register_indirizzo);

        btnRegister  = findViewById(R.id.register_btn);
        textLogin = findViewById(R.id.text_login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });    }

    //This function checks if there is already an user with the same email. If not, and both email and password fields aren't empty, the user is registered
    private void Register() {
        String email = register_email.getText().toString().trim();
        String pass = register_password.getText().toString().trim();
        String username = register_user.getText().toString().trim();
        String address = register_address.getText().toString().trim();
        //User have to fill all the entries
        if(email.isEmpty() || pass.isEmpty() || username.isEmpty() || address.isEmpty())
            Toast.makeText(RegisterActivity.this, "Please fill all the entries", Toast.LENGTH_SHORT).show();
        else {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        User user = new User(email,pass,username,address);
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        FirebaseUser fuser = mAuth.getCurrentUser();
                        String fuid = fuser.getUid();
                        ref.child("users").child(fuid).setValue(user);
                        startActivity(new Intent(RegisterActivity.this, Home.class));
                        finish();
                    }
                    else
                        Toast.makeText(RegisterActivity.this, "Registration Failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}