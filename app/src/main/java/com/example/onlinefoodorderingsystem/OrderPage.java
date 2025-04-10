package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

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

        pastOrdersListView = findViewById(R.id.pastOrdersListView);
        tvNoOrdersMessage = findViewById(R.id.tvNoOrdersMessage);
        btnGoToMenu = findViewById(R.id.btnGoToMenu);
        btnGoToHome = findViewById(R.id.btnGoToHome);

        // Load past orders from SharedPreferences
        loadPastOrders();

        if (pastOrders.isEmpty()) {
            tvNoOrdersMessage.setText("No past orders found.");
        } else {
            // Display past orders in ListView
            PastOrderAdapter adapter = new PastOrderAdapter(this, pastOrders);
            pastOrdersListView.setAdapter(adapter);
        }

        // Back to Menu button click listener
        btnGoToMenu.setOnClickListener(v -> {
            Intent menuIntent = new Intent(OrderPage.this, MenuPage.class); // Replace with actual menu activity
            startActivity(menuIntent);
            finish(); // Close the current activity
        });

        // Back to Home button click listener
        btnGoToHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(OrderPage.this, HomePage.class); // Replace with actual home activity
            startActivity(homeIntent);
            finish(); // Close the current activity
        });
    }

    private void loadPastOrders() {
        SharedPreferences sharedPreferences = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        String orderData = sharedPreferences.getString("orderData", null);
        String paymentMethod = sharedPreferences.getString("paymentMethod", null);

        pastOrders = new ArrayList<>();

        if (orderData != null && paymentMethod != null) {
            pastOrders.add("Payment Method: " + paymentMethod);
            pastOrders.add(orderData);
        }
    }
}
