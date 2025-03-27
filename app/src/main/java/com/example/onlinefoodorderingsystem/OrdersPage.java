package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrdersPage extends Activity {

    private Button btnBackToCart;  // Back button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        btnBackToCart = findViewById(R.id.btnBackToCart);  // Initialize back button

        // Back to CartPage when clicked
        btnBackToCart.setOnClickListener(v -> {
            Intent cartIntent = new Intent(OrdersPage.this, CartPage.class);
            startActivity(cartIntent);
        });
    }
}
