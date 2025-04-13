package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnCart = findViewById(R.id.btnCart);
        Button btnBack = findViewById(R.id.btnBack); // ✅ Back button added

        // ✅ Back button click handler
        btnBack.setOnClickListener(v -> {
            finish(); // Closes MenuPage and returns to previous activity
        });

        // Menu item buttons
        ArrayList<Button> addButtons = new ArrayList<>();
        addButtons.add(findViewById(R.id.button1));
        addButtons.add(findViewById(R.id.button2));
        addButtons.add(findViewById(R.id.button3));
        addButtons.add(findViewById(R.id.button4));
        addButtons.add(findViewById(R.id.button5));
        addButtons.add(findViewById(R.id.button6));
        addButtons.add(findViewById(R.id.button7));
        addButtons.add(findViewById(R.id.button8));
        addButtons.add(findViewById(R.id.button9));
        addButtons.add(findViewById(R.id.button10));

        // Names and prices for the menu items
        String[] names = {
                "Chicken Chop", "Lamb Grilled", "Double Burger", "Deer Satay", "Salad", "Salmon Salad",
                "Orange Juice", "Dragon Juice", "Green Tea", "Vanilla Coffe"
        };
        double[] prices = {
                12.90, 40.00, 15.50, 20.00, 5.00, 25.00,
                2.50, 3.00, 2.80, 1.50
        };

        // Set click listeners for the buttons
        for (int i = 0; i < addButtons.size(); i++) {
            final int index = i;
            addButtons.get(i).setOnClickListener(v -> {
                CartItem item = new CartItem(names[index], prices[index], 1);
                CartManager.addItem(MenuPage.this, item);
                Toast.makeText(MenuPage.this, names[index] + " added to cart!", Toast.LENGTH_SHORT).show();
            });
        }

        // Navigate to the CartPage
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPage.this, CartPage.class);
            startActivity(intent);
        });
    }
}
