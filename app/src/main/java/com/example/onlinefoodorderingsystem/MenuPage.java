package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuPage extends Activity {

    private ArrayList<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Your XML file name

        cartItems = new ArrayList<>();

        // Menu item Add buttons (ensure these IDs match the ones in the XML)
        Button btnCart = findViewById(R.id.btnCart);

        // Menu item buttons (ensure correct IDs)
        ArrayList<Button> addButtons = new ArrayList<>();
        addButtons.add(findViewByIndex(0)); // Chicken Chop
        addButtons.add(findViewByIndex(1)); // Lamb Grilled
        addButtons.add(findViewByIndex(2)); // Double Burger
        addButtons.add(findViewByIndex(3)); // Deer Satay
        addButtons.add(findViewByIndex(4)); // Salad
        addButtons.add(findViewByIndex(5)); // Salmon Salad

        // Menu item names and prices
        String[] names = {"Chicken Chop", "Lamb Grilled", "Double Burger", "Deer Satay", "Salad", "Salmon Salad"};
        double[] prices = {12.90, 40.00, 15.50, 20.00, 5.00, 25.00};

        // Add the items to the cart
        for (int i = 0; i < addButtons.size(); i++) {
            final int index = i;
            addButtons.get(i).setOnClickListener(v -> {
                cartItems.add(new CartItem(names[index], prices[index], 1));
                Toast.makeText(MenuPage.this, names[index] + " added to cart!", Toast.LENGTH_SHORT).show();
            });
        }

        // Go to CartPage when clicked
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPage.this, CartPage.class);
            intent.putExtra("cartItems", cartItems); // Pass cart items to CartPage
            startActivity(intent);
        });
    }

    // Helper method to get Add button by position in XML
    private Button findViewByIndex(int index) {
        // The order of buttons in XML matches the order here
        int[] ids = {
                R.id.button1, // Update these to actual IDs in XML
                R.id.button2,
                R.id.button3,
                R.id.button4,
                R.id.button5,
                R.id.button6
        };

        return findViewById(ids[index]);
    }
}
