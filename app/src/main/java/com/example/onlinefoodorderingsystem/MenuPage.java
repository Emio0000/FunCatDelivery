package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuPage extends Activity {

    private ArrayList<CartItem> cartItems; // List to hold CartItem objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize cartItems list
        cartItems = new ArrayList<>();

        // Find menu item buttons
        Button btnAddPizza = findViewById(R.id.btnAddPizza);
        Button btnAddBurger = findViewById(R.id.btnAddBurger);
        Button btnAddSalad = findViewById(R.id.btnAddSalad);

        // Add Pizza to the cart
        btnAddPizza.setOnClickListener(v -> {
            cartItems.add(new CartItem("Pizza", 12.99, 1)); // Add pizza with quantity 1
            Toast.makeText(MenuPage.this, "Pizza added to cart!", Toast.LENGTH_SHORT).show();
        });

        // Add Burger to the cart
        btnAddBurger.setOnClickListener(v -> {
            cartItems.add(new CartItem("Burger", 8.99, 1)); // Add burger with quantity 1
            Toast.makeText(MenuPage.this, "Burger added to cart!", Toast.LENGTH_SHORT).show();
        });

        // Add Salad to the cart
        btnAddSalad.setOnClickListener(v -> {
            cartItems.add(new CartItem("Salad", 5.99, 1)); // Add salad with quantity 1
            Toast.makeText(MenuPage.this, "Salad added to cart!", Toast.LENGTH_SHORT).show();
        });

        // Go to Cart Page
        Button btnGoToCart = findViewById(R.id.btnGoToCart);
        btnGoToCart.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPage.this, CartPage.class);
            intent.putExtra("cartItems", cartItems); // Pass cart items to CartPage
            startActivity(intent);
        });

        // Back to HomePage when Back button is clicked
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(MenuPage.this, HomePage.class);
            startActivity(homeIntent);
            finish(); // Close the current activity
        });
    }
}
