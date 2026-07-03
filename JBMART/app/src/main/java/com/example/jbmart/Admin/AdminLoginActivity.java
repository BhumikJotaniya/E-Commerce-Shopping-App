package com.example.jbmart.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jbmart.R;
import com.example.jbmart.Sellers.SellerLoginActivity;


public class AdminLoginActivity extends AppCompatActivity {
    
    private EditText AdminPhone;
    private EditText AdminPassword;
    private Button AdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);
        
        AdminPhone = findViewById(R.id.adminphone);
        AdminPassword = findViewById(R.id.adminpassword);
        AdminLogin = findViewById(R.id.adminloginbutton);


        
        
        AdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                
                if(AdminPhone.getText().toString().equals("9874563219") && AdminPassword.getText().toString().equals("admin@123"))
                {
                    Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
                    startActivity(intent);

                    Toast.makeText(AdminLoginActivity.this, "Admin Login Successfully", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(AdminLoginActivity.this, "Phone number Or Password is Invalid.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }

    public void NotAdmin(View view) {
        startActivity(new Intent(AdminLoginActivity.this, SellerLoginActivity.class));
    }

}