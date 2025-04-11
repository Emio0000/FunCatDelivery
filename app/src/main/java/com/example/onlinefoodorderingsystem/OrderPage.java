package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderPage extends Activity {

    private ListView pastOrdersListView;
    private TextView tvNoOrdersMessage;
    private ArrayList<String> pastOrders;
    private Button btnGoToMenu;
    private Button btnGoToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        // Initialize the views
        pastOrdersListView = findViewById(R.id.pastOrdersListView);
        tvNoOrdersMessage = findViewById(R.id.tvNoOrdersMessage);
        btnGoToMenu = findViewById(R.id.btnGoToMenu);
        btnGoToHome = findViewById(R.id.btnGoToHome);

        // Load past orders from SharedPreferences
        loadPastOrders();

        // Display the past orders in ListView
        if (pastOrders.isEmpty()) {
            tvNoOrdersMessage.setText("No past orders found.");
        } else {
            // Set up ArrayAdapter to display past orders in the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pastOrders);
            pastOrdersListView.setAdapter(adapter);
        }

        // Back to Menu button click listener
        btnGoToMenu.setOnClickListener(v -> {
            // Intent to navigate to the Menu page
            Intent menuIntent = new Intent(OrderPage.this, MenuPage.class);
            startActivity(menuIntent);
            finish(); // Close the current activity
        });

        // Back to Home button click listener
        btnGoToHome.setOnClickListener(v -> {
            // Intent to navigate to the Home page
            Intent homeIntent = new Intent(OrderPage.this, HomePage.class);
            startActivity(homeIntent);
            finish(); // Close the current activity
        });
    }

    // Method to load past orders from SharedPreferences
    private void loadPastOrders() {
        SharedPreferences sharedPreferences = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        String orderData = sharedPreferences.getString("orderData", null);

        pastOrders = new ArrayList<>();

        if (orderData != null) {
            // Split the orders by newline and display each as a separate item
            String[] orders = orderData.split("\n");
            for (String order : orders) {
                pastOrders.add(order);
            }
        }
    }
}

