package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText login_email, login_password;
    private Button login_btn;
    private TextView text_register;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        login_email = findViewById(R.id.login_email);
        login_btn = findViewById(R.id.login_btn);
        login_password = findViewById(R.id.login_password);
        text_register = findViewById(R.id.text_register);

        text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });



    }

    private void login() {
        String user = login_email.getText().toString().trim();
        String pass = login_password.getText().toString().trim();

        if(user.isEmpty()){
            login_email.setError("Email can not be empty");
        }
        if(pass.isEmpty()){
            login_password.setError("Password can not be empty");
        }
        else{
            mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        if(mAuth.getUid().equals("42ng3HSgdmPUil5K6Zs4HoQW1kx1")) {
                            Toast.makeText(LoginActivity.this, "Ciao Bosss!", Toast.LENGTH_LONG).show();
                            
                        }
                        else {
                            System.out.println("Utente normale");
                            Toast.makeText(LoginActivity.this, "Sign In Successfully!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, Home.class));
                            finish();
                        }
                    }
                    else
                        Toast.makeText(LoginActivity.this, "Sign In Failed!"+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }


}