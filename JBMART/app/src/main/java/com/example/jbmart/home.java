package com.example.jbmart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.jbmart.Categories.beauty_care;
import com.example.jbmart.Categories.electronics;
import com.example.jbmart.Categories.groceries;
import com.example.jbmart.Categories.laptop;
import com.example.jbmart.Categories.man_clothes;
import com.example.jbmart.Categories.mobile;
import com.example.jbmart.Categories.watch;
import com.example.jbmart.Categories.woman_clothes;
import com.example.jbmart.Model.Products;
import com.example.jbmart.ViewHolder.ProductViewHolder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class home extends AppCompatActivity {

    private final int numOfColums = 2;
    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    private ArrayList<Products> arrayList;
    private ProductsAdapter adapter;

    private ViewPager2 viewPager2;
    private final Handler slideHandler = new Handler();
    ImageView beautycare;
    ImageView woman_clothes;
    ImageView man_clothes;
    ImageView mobile;
    ImageView laptop;
    ImageView watch;
    ImageView grocery;
    ImageView electronics;

    private TextView searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase and RecyclerView
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        ProductRef.keepSynced(true);
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numOfColums));

        arrayList = new ArrayList<>();
        adapter = new ProductsAdapter(arrayList);
        recyclerView.setAdapter(adapter);

        fetchProducts();

        // Initialize ViewPager2 and ImageView click listeners
        beautycare = findViewById(R.id.beauty);
        woman_clothes = findViewById(R.id.woman_clothes);
        man_clothes = findViewById(R.id.man_clothes);
        mobile = findViewById(R.id.mobile);
        laptop = findViewById(R.id.laptop);
        watch = findViewById(R.id.watch);
        grocery = findViewById(R.id.groceries);
        electronics = findViewById(R.id.electronics);

        viewPager2 = findViewById(R.id.viewPagerImageSlider);

        searchview = findViewById(R.id.searchview);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.cloth));
        sliderItems.add(new SliderItem(R.drawable.headphone));
        sliderItems.add(new SliderItem(R.drawable.laptop));
        sliderItems.add(new SliderItem(R.drawable.watch2));
        sliderItems.add(new SliderItem(R.drawable.mega_sale));
        sliderItems.add(new SliderItem(R.drawable.phone));
        sliderItems.add(new SliderItem(R.drawable.toys));



        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable, 3000);
            }
        });


        beautycare.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, beauty_care.class);
            startActivity(intent);
        });

        woman_clothes.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, com.example.jbmart.Categories.woman_clothes.class);
            startActivity(intent);
        });

        man_clothes.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, com.example.jbmart.Categories.man_clothes.class);
            startActivity(intent);
        });

        mobile.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, com.example.jbmart.Categories.mobile.class);
            startActivity(intent);
        });

        laptop.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, com.example.jbmart.Categories.laptop.class);
            startActivity(intent);
        });

        watch.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, com.example.jbmart.Categories.watch.class);
            startActivity(intent);
        });

        grocery.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, groceries.class);
            startActivity(intent);
        });

        electronics.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, com.example.jbmart.Categories.electronics.class);
            startActivity(intent);
        });

        searchview.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, searchActivity.class);
            startActivity(intent);
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), searchActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.categories:
                    startActivity(new Intent(getApplicationContext(), categories.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.cart:
                    startActivity(new Intent(getApplicationContext(), cartActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.account:
                    startActivity(new Intent(getApplicationContext(), accountActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                default:
                    return false;
            }
        });
    }

    private void fetchProducts() {
        ProductRef.orderByChild("productState").equalTo("Approved").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Products product = ds.getValue(Products.class);
                    if (product != null) {
                        product.setProduct_id(ds.getKey());  // Ensure product_id is set
                        Log.d("FirebaseDebug", "Product ID: " + ds.getKey());
                        Log.d("FirebaseDebug", "Product Name: " + product.getProduct_name());
                        arrayList.add(product);
                    } else {
                        Log.e("FirebaseDebug", "Product is null for key: " + ds.getKey());
                    }
                }
                Collections.shuffle(arrayList); // Shuffle the list
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(home.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable, 3000);
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager2.getCurrentItem();
            int itemCount = viewPager2.getAdapter().getItemCount();
            viewPager2.setCurrentItem(currentItem == itemCount - 1 ? 0 : currentItem + 1);
        }
    };

    private class ProductsAdapter extends RecyclerView.Adapter<ProductViewHolder> {
        private List<Products> productList;

        public ProductsAdapter(List<Products> productList) {
            this.productList = productList;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            final Products product = productList.get(position);
            holder.txtProductName.setText(product.getProduct_name());
            holder.txtProductPrice.setText("Price = " + product.getProduct_price() + "₹");
            holder.txtProductDescription.setText(product.getProduct_description());
            Picasso.get().load(product.getProduct_image()).into(holder.imageView);

            // Inside onBindViewHolder or similar method in home activity
            holder.itemView.setOnClickListener(v -> {
                Log.d("ProductAdapter", "Product ID: " + product.getProduct_id());
                Intent intent = new Intent(home.this, ProductDetailsActivity.class);
                intent.putExtra("pid", product.getProduct_id());  // Ensure the key is "pid"
                startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }
    }
}
