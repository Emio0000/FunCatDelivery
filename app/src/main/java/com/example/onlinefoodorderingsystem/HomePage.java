package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class HomePage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnMenu = findViewById(R.id.btnMenu);
        Button btnCart = findViewById(R.id.btnCart);
        Button btnOrders = findViewById(R.id.btnOrders);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnMenu.setOnClickListener(v -> startActivity(new Intent(HomePage.this, MenuPage.class)));
        btnCart.setOnClickListener(v -> startActivity(new Intent(HomePage.this, CartPage.class)));
        btnOrders.setOnClickListener(v -> startActivity(new Intent(HomePage.this, OrdersPage.class)));

        btnLogout.setOnClickListener(v -> {
            // TODO: Implement Firebase Sign Out Here
            Intent intent = new Intent(HomePage.this, LoginPage.class);
            startActivity(intent);
            finish(); // Close HomePage
        });
    }
}
