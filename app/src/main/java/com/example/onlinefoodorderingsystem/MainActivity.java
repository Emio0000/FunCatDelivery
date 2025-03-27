package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnCart = findViewById(R.id.btnCart);
        Button btnOrders = findViewById(R.id.btnOrders);
        Button btnPayment = findViewById(R.id.btnPayment);
        Button btnMenu = findViewById(R.id.btnMenu); // New button to navigate to MenuPage

        // Navigate to Login Page
        btnLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginPage.class)));

        // Navigate to Register Page
        btnRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterPage.class)));

        // Navigate to Home Page
        btnHome.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HomePage.class)));

        // Navigate to Cart Page
        btnCart.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartPage.class)));

        // Navigate to Orders Page
        btnOrders.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, OrdersPage.class)));

        // Navigate to Payment Page
        btnPayment.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PaymentPage.class)));

        // Navigate to Menu Page
        btnMenu.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MenuPage.class)));
    }
}
