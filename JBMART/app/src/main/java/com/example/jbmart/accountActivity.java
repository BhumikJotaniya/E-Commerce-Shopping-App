package com.example.jbmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.jbmart.Sellers.SellerLoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class accountActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private ImageView profileImageView;
    private Button saveProfileButton;
    private Button logoutButton;
    private Button editProfileImageView;
    private Button seller_login_btn;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri profileImageUri;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        profileImageView = findViewById(R.id.profileImageView);
        saveProfileButton = findViewById(R.id.saveProfileButton);
        logoutButton = findViewById(R.id.logoutButton);
        editProfileImageView = findViewById(R.id.editProfileImageView);
        seller_login_btn = findViewById(R.id.seller_login_btn);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.account);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), home.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
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
                    return true;
                default:
                    return false;
            }
        });

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        fetchUserData();

        editProfileImageView.setOnClickListener(v -> enableEditMode(true));

        saveProfileButton.setOnClickListener(v -> {
            saveUserData();
            enableEditMode(false);
        });

        logoutButton.setOnClickListener(v -> logout());

        profileImageView.setOnClickListener(v -> openImageChooser());

        seller_login_btn.setOnClickListener(v -> {
            Intent intent = new Intent(accountActivity.this, SellerLoginActivity.class);
            startActivity(intent);
        });
    }

    private void fetchUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            usernameEditText.setText(user.getDisplayName());
            emailEditText.setText(user.getEmail());

            String phoneNumber = user.getPhoneNumber();
            phoneNumberEditText.setText(phoneNumber != null ? phoneNumber : "Phone number not available");

            Uri profileImageUrl = user.getPhotoUrl();
            if (profileImageUrl != null) {
                Glide.with(this).load(profileImageUrl).into(profileImageView);
            } else {
                profileImageView.setImageResource(R.drawable.baseline_account_circle_24); // Placeholder image
            }
        }
    }

    private void saveUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(usernameEditText.getText().toString())
                    .setPhotoUri(profileImageUri)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(accountActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(accountActivity.this, "Profile update failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void enableEditMode(boolean enable) {
        usernameEditText.setEnabled(enable);
        emailEditText.setEnabled(enable);
        phoneNumberEditText.setEnabled(enable);
        saveProfileButton.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    private void logout() {
        endSession();
    }

    private void endSession() {

        editor.clear();
        editor.apply();

        Log.d("Session", "Session data cleared");


        Toast.makeText(this, "Session ended. Please log in again.", Toast.LENGTH_SHORT).show();
        Log.d("Session", "Session ended toast shown");


        Intent intent = new Intent(accountActivity.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack and start a new task
        startActivity(intent);

        Log.d("Session", "Login activity started");

        // Finish current activity
        finish();
        Log.d("Session", "Current activity finished");
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            Glide.with(this).load(profileImageUri).into(profileImageView);
        }
    }
}
