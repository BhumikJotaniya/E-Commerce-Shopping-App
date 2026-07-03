package com.example.jbmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.jbmart.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.signupName.getText().toString();
                String email = binding.signupEmail.getText().toString();
                String phone = binding.signupPhone.getText().toString();
                String username = binding.signupUsername.getText().toString();
                String password = binding.signupPassword.getText().toString();

                if (validateInputs(name, email, username, password, phone)) {
                    registerUser(name, email, phone, username, password);
                }
            }
        });
    }

    private void registerUser(String name, String email, String phone, String username, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserToDatabase(name, email, phone, username, password);
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed.";
                            Toast.makeText(signup.this, "Firebase Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToDatabase(String name, String email, String phone, String username, String password) {
        database user = new database(name, email, phone, username, password);
        reference.child(phone).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            clearInputFields();

                            Toast.makeText(signup.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(signup.this, login.class));
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Saving user failed.";
                            Toast.makeText(signup.this, "Database error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearInputFields() {
        binding.signupName.setText("");
        binding.signupEmail.setText("");
        binding.signupPhone.setText("");
        binding.signupUsername.setText("");
        binding.signupPassword.setText("");
    }

    private boolean validateInputs(String name, String email, String username, String password, String phone) {
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            binding.signupName.setError("Name is required");
            return false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.signupEmail.setError("Valid email is required");
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            binding.signupPhone.setError("Phone number is required");
            return false;
        }

        if (TextUtils.isEmpty(username)) {
            binding.signupUsername.setError("Username is required");
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6 ||
                !password.matches(".*[!@#$%^&*+=?-].*") || !password.matches(".*\\d.*") ||
                !password.matches(".*[a-zA-Z].*")) {
            binding.signupPassword.setError("Password must be at least 6 characters long, include a special symbol, a digit, and a character");
            return false;
        }

        return true;
    }

    public void login(View view) {
        startActivity(new Intent(signup.this, login.class));
    }


}
