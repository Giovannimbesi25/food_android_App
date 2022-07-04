//Activity which show all the details of a product clicked in HomeFragment
package com.example.mobileproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

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

public class FoodDetails extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ProductPreview productPreview;
    private Intent intent;
    private ImageView food_image;
    private TextView food_title, food_description, food_quantity, food_price;
    private Button minus_button, plus_button, ratingbtn;
    private FloatingActionButton admin_modify, admin_delete, cartbtn;
    private RatingBar food_rating;
    private Toolbar toolbar;

    //initialize all the layout elements
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        //GET THE PRODUCT TO DISPLAY FROM INTENT
        intent = getIntent();
        productPreview = (ProductPreview) intent.getSerializableExtra("Prodotto");
        //AUTH & DATABASE
        database=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();


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
        admin_modify = findViewById(R.id.admin_modify);
        admin_delete = findViewById(R.id.admin_delete);
        cartbtn = findViewById(R.id.btnCart);
        ratingbtn = findViewById(R.id.ratingbtn);


        //if the current user has the admin UID, so the button modify and delete will be Visible and clickable
        if( mAuth.getCurrentUser().getUid().equals("42ng3HSgdmPUil5K6Zs4HoQW1kx1")) {
            admin_modify.setVisibility(View.VISIBLE);
            admin_modify.setClickable(true);
            admin_delete.setVisibility(View.VISIBLE);
            admin_delete.setClickable(true);

        }


        food_description.setText(productPreview.getDescrizione());
        food_title.setText(productPreview.getName());
        food_price.setText(productPreview.getPrezzo());
        food_rating.setRating(Float.valueOf(productPreview.getRating()));
        Glide.with(food_image.getContext()).load(productPreview.getImage()).into(food_image);

        admin_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyProduct(productPreview);
            }
        });

        admin_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(productPreview);
            }
        });


        //ADDING A RATING TO A SPECIFIC PRODUCT
        ratingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProductRating(productPreview);
            }
        });

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


        //The cart button allow the users to insert the product in their current order
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser fuser = mAuth.getCurrentUser();
                String fuid = fuser.getUid();

                //Create a new object to store only essential data for the order
                ProductOrdered productOrdered = new ProductOrdered();
                productOrdered.setPrezzo(productPreview.getPrezzo());
                productOrdered.setName(productPreview.getName());
                productOrdered.setQuantity(food_quantity.getText().toString());

                //Get the Product's Price from string
                Integer quantity = Integer.parseInt(productOrdered.getQuantity());
                float flt = 0;
                //Creating a pattern to identify floats
                Pattern pat = Pattern.compile("[-]?[0-9]*\\.?[0-9]+");
                //matching the string with the pattern
                Matcher m = pat.matcher(productOrdered.getPrezzo());
                //extracting and storing the float values
                while(m.find()) {
                    flt = Float.parseFloat(m.group());
                }
                String totale = String.valueOf(quantity*flt);
                productOrdered.setTotale(totale);

                //Adding product to list
                if(Integer.parseInt(productOrdered.getQuantity()) > 0) {
                    ref.child("users").child(fuid).child("CurrentOrder").child(productPreview.getName()).setValue(productOrdered);
                    Toast.makeText(FoodDetails.this, "Product Added to Cart", Toast.LENGTH_SHORT).show();


                    finish();
                }
                else
                    Toast.makeText(FoodDetails.this, "Quantità non valida", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setProductRating(ProductPreview productPreview) {
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("UserRatings").child(productPreview.getName()).child(mAuth.getCurrentUser().getUid());
        //A simple dialog is built in order to prevent involuntary click.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Review this Product!");
        builder.setMessage("Follow Hint");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("0, 0.5, 1, 1.5...5");
        builder.setView(input);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                System.out.println("The product is "+ productPreview.getName());
                //ref.child("Products").child(productPreview.getName()).removeValue();
                System.out.println("My Rating" + input.getText().toString());
                if(input.getText().toString().isEmpty()) {}
                else{
                    ref.setValue(input.getText().toString());
                }

                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }


    //The admin is allowed to delete a product
    private void deleteProduct(ProductPreview productPreview) {

        //A simple dialog is built in order to prevent involuntary click.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete Product");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                ref.child("Products").child(productPreview.getName()).removeValue();
                Toast.makeText(FoodDetails.this, "Product Deleted", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FoodDetails.this, "Product Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    //Also an admin can modify a product adding. A dialog will appears and the admin can modify all the details(or just one)
    private void modifyProduct(ProductPreview productPreview){
        ref = ref.child("Products").child(productPreview.getName());

        //Create the alert dialog as a vertical LinearLayout of EditView
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LinearLayout lila1= new LinearLayout(this);
        lila1.setOrientation(LinearLayout.VERTICAL); //1 is for vertical orientation
        lila1.setVerticalScrollBarEnabled(true);
        EditText inputName = new EditText(this); EditText inputDescription = new EditText(this);
        EditText inputPrezzo = new EditText(this); EditText inputImage = new EditText(this);
        EditText inputRating = new EditText(this);
        inputName.setHint("Product Name: Pizza Margherita");
        inputDescription.setHint("Product Description: Salsa di pomodoro, mozzarella ..");
        inputPrezzo.setHint("Product Price: 4.50");
        inputImage.setHint("URL Product Images!");
        inputRating.setHint("Rating: from 0 to 5 ");
        lila1.addView(inputName); lila1.addView(inputDescription); lila1.addView(inputPrezzo);
        lila1.addView(inputImage); lila1.addView(inputRating);
        builder.setView(lila1);
        builder.setTitle("Change Food Properties");
        builder.setMessage("Follow Hints Format");
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    //only filled field are used to change the corresponding product's characteristics
                    public void onClick(DialogInterface dialog, int id) {
                        if(!inputPrezzo.getText().toString().isEmpty())    productPreview.setPrezzo(inputPrezzo.getText().toString());
                        if(!inputName.getText().toString().isEmpty()) productPreview.setName(inputName.getText().toString());
                        if(!inputImage.getText().toString().isEmpty()) productPreview.setImage(inputImage.getText().toString());
                        if(!inputDescription.getText().toString().isEmpty()) productPreview.setDescrizione(inputDescription.getText().toString());
                        if(!inputRating.getText().toString().isEmpty()) productPreview.setRating(inputRating.getText().toString());

                        ref.setValue(productPreview);
                        Toast.makeText(FoodDetails.this, "Procuct Modified", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        builder.show();
    }


}
