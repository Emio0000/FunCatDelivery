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
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish(); // Back to previous page
        });

        // Menu buttons
        ArrayList<Button> addButtons = new ArrayList<>();
        addButtons.add(findViewById(R.id.button1)); // Chicken Chop
        addButtons.add(findViewById(R.id.button2)); // Lamb Grilled
        addButtons.add(findViewById(R.id.button3)); // Double Burger
        addButtons.add(findViewById(R.id.button4)); // Deer Satay
        addButtons.add(findViewById(R.id.button5)); // Salad
        addButtons.add(findViewById(R.id.button6)); // Salmon Salad
        addButtons.add(findViewById(R.id.button7)); // Orange Juice
        addButtons.add(findViewById(R.id.button8)); // Dragon Juice
        addButtons.add(findViewById(R.id.button9)); // Ice Matcha / Green Tea
        addButtons.add(findViewById(R.id.button10)); // Vanilla Coffee

        String[] names = {
                "Chicken Chop", "Lamb Grilled", "Double Burger", "Deer Satay", "Salad", "Salmon Salad",
                "Orange Juice", "Dragon Juice", "Ice Matcha", "Vanilla Coffe"
        };

        double[] prices = {
                12.90, 40.00, 15.50, 20.00, 5.00, 25.00,
                2.50, 3.00, 2.80, 1.50
        };

        // Corresponding drawable resources (must be in res/drawable)
        int[] imageResIds = {
                R.drawable.chicken,  // Chicken Chop
                R.drawable.lamb,     // Lamb Grilled
                R.drawable.burger,   // Double Burger
                R.drawable.satay,    // Deer Satay
                R.drawable.salad,    // Salad
                R.drawable.salmon,   // Salmon Salad
                R.drawable.drink1,   // Orange Juice
                R.drawable.drink2,   // Dragon Juice
                R.drawable.drink3,   // Ice Matcha
                R.drawable.drink4    // Vanilla Coffe
        };

        // Attach logic to each button
        for (int i = 0; i < addButtons.size(); i++) {
            final int index = i;
            addButtons.get(i).setOnClickListener(v -> {
                CartItem item = new CartItem(names[index], prices[index], 1, imageResIds[index]);
                CartManager.addItem(MenuPage.this, item);
                Toast.makeText(MenuPage.this, names[index] + " added to cart!", Toast.LENGTH_SHORT).show();
            });
        }

        // Navigate to Cart Page
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPage.this, CartPage.class);
            startActivity(intent);
        });
    }
}

