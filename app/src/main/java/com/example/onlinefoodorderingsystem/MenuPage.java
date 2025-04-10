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

        // Add item buttons
        ArrayList<Button> addButtons = new ArrayList<>();
        addButtons.add(findViewByIndex(0)); // Chicken Chop
        addButtons.add(findViewByIndex(1)); // Lamb Grilled
        addButtons.add(findViewByIndex(2)); // Double Burger
        addButtons.add(findViewByIndex(3)); // Deer Satay
        addButtons.add(findViewByIndex(4)); // Salad
        addButtons.add(findViewByIndex(5)); // Salmon Salad

        // Food names and prices
        String[] names = {"Chicken Chop", "Lamb Grilled", "Double Burger", "Deer Satay", "Salad", "Salmon Salad"};
        double[] prices = {12.90, 40.00, 15.50, 20.00, 5.00, 25.00};

        // Add click listeners
        for (int i = 0; i < addButtons.size(); i++) {
            final int index = i;
            addButtons.get(i).setOnClickListener(v -> {
                CartItem item = new CartItem(names[index], prices[index], 1);
                CartManager.addItem(MenuPage.this, item);
                Toast.makeText(MenuPage.this, names[index] + " added to cart!", Toast.LENGTH_SHORT).show();
            });
        }

        // Go to Cart
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPage.this, CartPage.class);
            startActivity(intent);
        });
    }

    private Button findViewByIndex(int index) {
        int[] ids = {
                R.id.button1,
                R.id.button2,
                R.id.button3,
                R.id.button4,
                R.id.button5,
                R.id.button6
        };
        return findViewById(ids[index]);
    }
}
