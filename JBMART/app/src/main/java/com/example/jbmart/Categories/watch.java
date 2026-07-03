package com.example.jbmart.Categories;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jbmart.Model.Products;
import com.example.jbmart.ProductDetailsActivity;
import com.example.jbmart.R;
import com.example.jbmart.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class watch extends AppCompatActivity {

    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Products> options;
    private FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);  // Ensure this is the correct layout file

        // Initialize Firebase Database
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Set up FirebaseRecyclerOptions to query for products in the "watches" category
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductRef.orderByChild("category").equalTo("Watches"), Products.class)
                .build();

        // Set up FirebaseRecyclerAdapter
        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                holder.txtProductName.setText(model.getProduct_name());
                holder.txtProductPrice.setText("Price = " + model.getProduct_price() + " ₹");
                holder.txtProductDescription.setText(model.getProduct_description());
                Picasso.get().load(model.getProduct_image()).into(holder.imageView);

                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(watch.this, ProductDetailsActivity.class);
                    intent.putExtra("pid", model.getProduct_id());
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(watch.this).inflate(R.layout.product_items_layout, parent, false);
                return new ProductViewHolder(view);
            }
        };

        // Set adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        // Start listening for changes
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for changes
        adapter.stopListening();
    }
}