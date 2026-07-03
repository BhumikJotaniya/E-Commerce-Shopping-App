package com.example.jbmart.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jbmart.Sellers.SellerLoginActivity;
import com.example.jbmart.home;
import com.example.jbmart.login;
import com.example.jbmart.R;

public class AdminHomeActivity extends AppCompatActivity {

    private Button adminLogoutBtn, adminMaintainProductBtn, checkApproveNewProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        adminLogoutBtn = findViewById(R.id.admin_logout);
        adminMaintainProductBtn = findViewById(R.id.admin_maintainProduct);
        checkApproveNewProductsBtn = findViewById(R.id.admin_check_approve_products);

        adminLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, home.class);
                startActivity(intent);
                finish();
            }
        });

        adminMaintainProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminAllProductsActivity.class);
                startActivity(intent);
            }
        });

        checkApproveNewProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminCheckNewProductsActivity.class);
                startActivity(intent);
            }
        });
    }
}