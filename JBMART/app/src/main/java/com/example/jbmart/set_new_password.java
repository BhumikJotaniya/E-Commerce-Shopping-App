package com.example.jbmart;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class set_new_password extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText newPassword;
    private EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        progressBar = findViewById(R.id.progressBar);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);

        findViewById(R.id.set_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateOptionsMenu() || !validateConfirmPassword()) {
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                String _newPassword = newPassword.getText().toString().trim();
                String _phone = getIntent().getStringExtra("phoneNo");

                // Log the phone number for debugging
                Log.d("set_new_password", "Phone number received: " + _phone);

                // Check if _phone is null or empty
                if (TextUtils.isEmpty(_phone)) {
                    Log.e("set_new_password", "Phone number is missing or invalid");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(set_new_password.this, "Phone number is missing or invalid", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update the password in Firebase
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.child(_phone).child("password").setValue(_newPassword)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(set_new_password.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(set_new_password.this, login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(set_new_password.this, "Failed to update password. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private boolean validateOptionsMenu() {
        String password = newPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password) || password.length() < 6 ||
                !password.matches(".*[!@#$%^&*+=?-].*") || !password.matches(".*\\d.*") ||
                !password.matches(".*[a-zA-Z].*")) {
            newPassword.setError("Password must be at least 6 characters long, include a special symbol, a digit, and a character");
            return false;
        }
        return true;
    }

    private boolean validateConfirmPassword() {
        String password = newPassword.getText().toString().trim();
        String confirm = confirmPassword.getText().toString().trim();
        if (!password.equals(confirm)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}
