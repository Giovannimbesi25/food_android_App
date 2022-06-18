package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.mobileproject.Model.ProductOrdered;
import com.example.mobileproject.Model.ProductPreview;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodDetailAdmin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    ProductPreview productPreview;
    Intent intent;
    ImageView food_image;
    TextView food_title, food_description, food_quantity, food_price;
    Button minus_button, plus_button;
    FloatingActionButton btnCart;
    RatingBar food_rating;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail_admin);

        //GET THE PRODUCT TO DISPLAY FROM INTENT
        System.out.println("Ciao Sei nella Food Detail");
        intent = getIntent();
        productPreview = (ProductPreview) intent.getSerializableExtra("Prodotto");

        //AUTH & DATABASE
        database=FirebaseDatabase.getInstance();
        ref = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        //Layout element

        food_image = findViewById(R.id.img_food);
        toolbar = findViewById(R.id.toolbar);
        food_rating = findViewById(R.id.food_rating);
        food_description = findViewById(R.id.food_description);
        food_title = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        minus_button=findViewById(R.id.minus_button);
        plus_button=findViewById(R.id.plus_button);
        food_quantity = findViewById(R.id.food_quantity);
        btnCart = findViewById(R.id.btnCart);

        food_description.setText(productPreview.getDescrizione());
        food_title.setText(productPreview.getName());
        food_price.setText(productPreview.getPrezzo());
        food_rating.setRating(Float.valueOf(productPreview.getRating()));
        Glide.with(food_image.getContext()).load(productPreview.getImage()).into(food_image);



        //QUANTITY LOGIC
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = Integer.parseInt(food_quantity.getText().toString());
                quantity = quantity+1;
                food_quantity.setText(Integer.toString(quantity));

            }
        });

        minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = Integer.parseInt(food_quantity.getText().toString());
                if(quantity>0) {
                    quantity = quantity - 1;
                }
                food_quantity.setText(Integer.toString(quantity));
            }
        });






    }
}