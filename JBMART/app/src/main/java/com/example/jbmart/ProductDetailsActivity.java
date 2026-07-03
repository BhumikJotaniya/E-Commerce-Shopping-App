package com.example.jbmart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jbmart.Model.Products;
import com.example.jbmart.Prevalent.Prevalent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;



public class ProductDetailsActivity extends AppCompatActivity {

    private Button decrementBtn, incrementBtn, addToCartBtn;
    private TextView quantityText, productName, productDescription, productPrice;
    private ImageView productImage;
    private String productID = "";
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");
        Log.d("ProductDetailsActivity", "Product ID: " + productID); // Add logging to verify

        if (productID != null) {
            getProductDetails(productID);
        } else {
            Toast.makeText(this, "Product ID is null", Toast.LENGTH_SHORT).show();
        }

        // Initialize UI components
        productName = findViewById(R.id.product_name_details);
        productDescription = findViewById(R.id.product_description_details);
        productPrice = findViewById(R.id.product_price_details);
        quantityText = findViewById(R.id.quantity_text);
        productImage = findViewById(R.id.product_image_details);
        addToCartBtn = findViewById(R.id.add_to_cart_btn_details);
        decrementBtn = findViewById(R.id.decrement_button);
        incrementBtn = findViewById(R.id.increment_button);

        addToCartBtn.setOnClickListener(v -> addProductToCartList());

        incrementBtn.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
        });

        decrementBtn.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });
    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products product = dataSnapshot.getValue(Products.class);
                    if (product != null) {
                        productName.setText(product.getProduct_name());
                        productPrice.setText(product.getProduct_price() + " ₹");
                        productDescription.setText(product.getProduct_description());
                        Picasso.get().load(product.getProduct_image()).into(productImage);
                    } else {
                        Toast.makeText(ProductDetailsActivity.this, "Product details not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductDetailsActivity.this, "Failed to load product details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProductToCartList() {
        if (Prevalent.currentOnlineUser == null) {
            Toast.makeText(ProductDetailsActivity.this, "Complete the all filed to add items to your cart.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProductDetailsActivity.this, ConfirmFinalOrderActivity.class);
            startActivity(intent);
            return;
        }

    }
}
