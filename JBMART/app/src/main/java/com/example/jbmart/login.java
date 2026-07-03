package com.example.jbmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class login extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button button;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Handler handler = new Handler();
    private Runnable logoutRunnable;

    private static final long SESSION_TIMEOUT = 30L * 24 * 60 * 60 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);

        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkAutoLogout();
    }

    public void onLoginClick(View view) {
        if (validateInputs()) {
            showProgressBar();
            checkUser();
        }
    }

    private Boolean validateInputs() {
        return validateUsername() && validatePassword();
    }

    private Boolean validateUsername() {
        String val = username.getText().toString();
        if (val.isEmpty()) {
            username.setError("Username cannot be empty.");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("Password cannot be empty.");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private void checkUser() {
        String userUsername = username.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (userUsername.isEmpty() || userPassword.isEmpty()) {
            hideProgressBar();
            Toast.makeText(this, "Username or Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hideProgressBar();
                if (snapshot.exists()) {
                    username.setError(null);

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                        Log.d("DEBUG", "userUsername: " + userUsername);
                        Log.d("DEBUG", "passwordFromDB: " + passwordFromDB);
                        Log.d("DEBUG", "userPassword: " + userPassword);

                        if (passwordFromDB != null && userPassword != null) {
                            if (Objects.equals(passwordFromDB.trim(), userPassword.trim())) {
                                username.setError(null);
                                Log.d("DEBUG", "Login successful. Starting home activity.");


                                editor.putString("username", userUsername);
                                editor.putString("password", userPassword);
                                editor.putBoolean("isLoggedIn", true);
                                editor.putLong("loginTime", System.currentTimeMillis());
                                editor.apply();

                                startActivity(new Intent(login.this, home.class));
                                finish();

                                startAutoLogoutTimer();
                                return;
                            }
                        }
                    }

                    Log.d("DEBUG", "Invalid Password.");
                    password.setError("Invalid Password.");
                    password.requestFocus();
                } else {
                    Log.d("DEBUG", "User does not exist.");
                    username.setError("User does not exist.");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressBar();
                Toast.makeText(login.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void signup(View view) {
        startActivity(new Intent(login.this, signup.class));
    }

    public void forgot(View view) {
        startActivity(new Intent(login.this, Forgot_pass.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            long loginTime = sharedPreferences.getLong("loginTime", 0);
            if (System.currentTimeMillis() - loginTime > SESSION_TIMEOUT) {
                handleSessionExpiration();
            } else {
                startActivity(new Intent(login.this, home.class));
                finish();
            }
        }
    }

    private void startAutoLogoutTimer() {
        if (logoutRunnable != null) {
            handler.removeCallbacks(logoutRunnable);
        }

        logoutRunnable = new Runnable() {
            @Override
            public void run() {
                handleSessionExpiration();
            }
        };
        handler.postDelayed(logoutRunnable, SESSION_TIMEOUT);
    }

    private void checkAutoLogout() {
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            long loginTime = sharedPreferences.getLong("loginTime", 0);
            if (System.currentTimeMillis() - loginTime > SESSION_TIMEOUT) {
                handleSessionExpiration();
            }
        }
    }

    private void handleSessionExpiration() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


        Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(login.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack to prevent back navigation
        startActivity(intent);

        finish();
    }
}
