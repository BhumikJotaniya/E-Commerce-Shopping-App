package com.example.jbmart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.jbmart.Categories.beauty_care;
import com.example.jbmart.Categories.electronics;
import com.example.jbmart.Categories.glasses;
import com.example.jbmart.Categories.groceries;
import com.example.jbmart.Categories.hats_caps;
import com.example.jbmart.Categories.headphone;
import com.example.jbmart.Categories.laptop;
import com.example.jbmart.Categories.man_clothes;
import com.example.jbmart.Categories.mobile;
import com.example.jbmart.Categories.purses;
import com.example.jbmart.Categories.shoes;
import com.example.jbmart.Categories.sports_tshirts;
import com.example.jbmart.Categories.sweaters;
import com.example.jbmart.Categories.toys;
import com.example.jbmart.Categories.watch;
import com.example.jbmart.Categories.woman_clothes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class categories extends AppCompatActivity {

    CardView clothingCard;
    CardView manclothingCard;
    CardView watchCard;
    CardView beautyCard;
    CardView laptopCard;
    CardView groceriesCard;
    CardView mobileCard;
    CardView electronicsCard;
    CardView headphoneCard;
    CardView hat_capCard;
    CardView pursesCard;
    CardView sweatersCard;
    CardView shoesCart;
    CardView sports_tshirtsCard;
    CardView glasses;
    CardView toys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories);

        clothingCard =findViewById(R.id.clothingCard);
        manclothingCard = findViewById(R.id.manclothesCard);
        watchCard = findViewById(R.id.watchCard);
        beautyCard = findViewById(R.id.beautyCard);
        laptopCard = findViewById(R.id.laptopCard);
        groceriesCard = findViewById(R.id.groceriesCard);
        mobileCard = findViewById(R.id.mobileCard);
        electronicsCard = findViewById(R.id.electronicsCard);
        headphoneCard = findViewById(R.id.headphoneCard);
        hat_capCard = findViewById(R.id.hats_capsCard);
        pursesCard = findViewById(R.id.pursesCard);
        sweatersCard = findViewById(R.id.sweatersCard);
        shoesCart = findViewById(R.id.shoesCard);
        sports_tshirtsCard = findViewById(R.id.sports_tshirtsCard);
        glasses = findViewById(R.id.glassesCard);
        toys = findViewById(R.id.toysCard);



        clothingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(categories.this, woman_clothes.class);
                startActivity(intent);
            }
        });

        manclothingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, man_clothes.class);
                startActivity(intent);
            }
        });

        watchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, watch.class);
                startActivity(intent);
            }
        });

        beautyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, beauty_care.class);
                startActivity(intent);
            }
        });

        laptopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, laptop.class);
                startActivity(intent);
            }
        });

        groceriesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, groceries.class);
                startActivity(intent);
            }
        });

        mobileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, mobile.class);
                startActivity(intent);
            }
        });

        electronicsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, electronics.class);
                startActivity(intent);
            }
        });

        headphoneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, headphone.class);
                startActivity(intent);
            }
        });

        hat_capCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, hats_caps.class);
                startActivity(intent);
            }
        });

        pursesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, purses.class);
                startActivity(intent);
            }
        });

        sweatersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, sweaters.class);
                startActivity(intent);
            }
        });

        shoesCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, shoes.class);
                startActivity(intent);
            }
        });

        sports_tshirtsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, sports_tshirts.class);
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, glasses.class);
                startActivity(intent);
            }
        });

        toys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, toys.class);
                startActivity(intent);
            }
        });





        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.categories);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), home.class));

                    finish();
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), searchActivity.class));

                    finish();
                    return true;
                case R.id.categories:

                    return true;
                case R.id.cart:
                    startActivity(new Intent(getApplicationContext(), cartActivity.class));

                    finish();
                    return true;
                case R.id.account:
                    startActivity(new Intent(getApplicationContext(), accountActivity.class));

                    finish();
                    return true;
                default:
                    return false;
            }
        });

    }
}