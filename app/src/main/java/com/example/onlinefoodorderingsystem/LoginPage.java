package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends Activity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        EditText etEmail = findViewById(R.id.username);
        EditText etPassword = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.LoginButton);
        Button btnSignUp = findViewById(R.id.signUpButton);  // New button to navigate to RegisterPage

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginPage.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Button to navigate to RegisterPage
        btnSignUp.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(registerIntent);
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginPage.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        // Redirect to HomePage
                        startActivity(new Intent(LoginPage.this, HomePage.class));
                        finish();
                    } else {
                        Toast.makeText(LoginPage.this, "Authentication Failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Remove the auto-login check, so login page is shown every time
    }
}
