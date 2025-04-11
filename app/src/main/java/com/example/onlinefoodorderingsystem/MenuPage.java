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

        // Create a list of buttons dynamically by getting them from the layout
        ArrayList<Button> addButtons = new ArrayList<>();
        addButtons.add(findViewById(R.id.button1));
        addButtons.add(findViewById(R.id.button2));
        addButtons.add(findViewById(R.id.button3));
        addButtons.add(findViewById(R.id.button4));
        addButtons.add(findViewById(R.id.button5));
        addButtons.add(findViewById(R.id.button6));
        addButtons.add(findViewById(R.id.button7));
        addButtons.add(findViewById(R.id.button8));


        // Names and prices for the menu items
        String[] names = {
                "Chicken Chop", "Lamb Grilled", "Double Burger", "Deer Satay", "Salad", "Salmon Salad",
                "Coca Cola", "Iced Lemon Tea", "Green Tea", "Mineral Water"
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
