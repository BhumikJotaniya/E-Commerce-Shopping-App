package com.example.jbmart.Sellers;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import com.example.jbmart.databinding.ActivitySellerRegistrationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private ActivitySellerRegistrationBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Sellers");
        loadingBar = new ProgressDialog(this);

        binding.sellerRegisterBtn.setOnClickListener(v -> onRegisterClick());
        binding.sellerHaveAccount.setOnClickListener(v -> onLoginClick());
    }

    public void onRegisterClick() {
        String name = binding.sellerName.getText().toString();
        String email = binding.sellerMail.getText().toString();
        String phone = binding.sellerPhone.getText().toString();
        String password = binding.sellerPassword.getText().toString();
        String address = binding.sellerAddress.getText().toString();

        if (validateInputs(name, email, phone, password, address)) {
            registerUser(name, email, phone, password, address);
        }
    }

    public void onLoginClick() {
        startActivity(new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class));
    }

    private void registerUser(String name, String email, String phone, String password, String address) {
        loadingBar.setTitle("Create Seller Account");
        loadingBar.setMessage("Please wait, while we are checking the credentials");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loadingBar.dismiss();
                    if (task.isSuccessful()) {
                        saveUserToDatabase(name, email, phone, password, address);
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed.";
                        Toast.makeText(SellerRegistrationActivity.this, "Firebase Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(String name, String email, String phone, String password, String address) {
        HashMap<String, Object> sellerMap = new HashMap<>();
        sellerMap.put("name", name);
        sellerMap.put("phone", phone);
        sellerMap.put("email", email);
        sellerMap.put("password", password);
        sellerMap.put("address", address);

        reference.child(phone).setValue(sellerMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                clearInputFields();
                Toast.makeText(SellerRegistrationActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class));
            } else {
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Saving user failed.";
                Toast.makeText(SellerRegistrationActivity.this, "Database error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearInputFields() {
        binding.sellerName.setText("");
        binding.sellerMail.setText("");
        binding.sellerPhone.setText("");
        binding.sellerPassword.setText("");
        binding.sellerAddress.setText("");
    }

    private boolean validateInputs(String name, String email, String phone, String password, String address) {
        if (TextUtils.isEmpty(name)) {
            binding.sellerName.setError("Name is required");
            return false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.sellerMail.setError("Valid email is required");
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            binding.sellerPhone.setError("Phone number is required");
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6 ||
                !password.matches(".*[!@#$%^&*+=?-].*") || !password.matches(".*\\d.*") ||
                !password.matches(".*[a-zA-Z].*")) {
            binding.sellerPassword.setError("Password must be at least 6 characters long, include a special symbol, a digit, and a character");
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            binding.sellerAddress.setError("Address is required");
            return false;
        }

        return true;
    }

    public void SellerLogin(View view) {
        startActivity(new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class));
    }
}
