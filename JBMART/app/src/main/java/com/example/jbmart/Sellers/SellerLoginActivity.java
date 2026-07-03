package com.example.jbmart.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jbmart.Admin.AdminLoginActivity;
import com.example.jbmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerLoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ProgressDialog loadingBar;
    private EditText emailInput, passwordInput;
    private Button sellerLoginBtn;
    private TextView txtViewSellerRegister;
    private TextView admin_panel_link;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        auth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.seller_mail_login);
        passwordInput = findViewById(R.id.seller_password_login);
        sellerLoginBtn = findViewById(R.id.seller_login_btn);
        txtViewSellerRegister = findViewById(R.id.txtView_seller_register);

        admin_panel_link = findViewById(R.id.admin_panel_link);




        sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });

        txtViewSellerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerLoginActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });

        admin_panel_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerLoginActivity.this, AdminLoginActivity.class));
            }
        });


    }

    private void loginSeller() {
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(SellerLoginActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(password)) {
            Toast.makeText(SellerLoginActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar = new ProgressDialog(SellerLoginActivity.this);
            loadingBar.setTitle("Seller Account Login");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        retrieveSellerData();
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(SellerLoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void retrieveSellerData() {
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sellers").child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);

                    loadingBar.dismiss();


                    Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("address", address);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    Toast.makeText(SellerLoginActivity.this, "You are logged in successfully", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(SellerLoginActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
                Toast.makeText(SellerLoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    } public void Register(View view) {
        startActivity(new Intent(SellerLoginActivity.this, SellerRegistrationActivity.class));
    }

    public void Admin(View view) {
        startActivity(new Intent(SellerLoginActivity.this, SellerRegistrationActivity.class));
    }


}
