package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class OrderPage extends Activity {

    private TextView tvOrderSummary, tvPastOrdersTitle;
    private ListView pastOrdersListView;
    private Button btnGoToMenu, btnGoToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);  // Updated layout name here

        tvOrderSummary = findViewById(R.id.tvOrderSummary);
        tvPastOrdersTitle = findViewById(R.id.tvPastOrdersTitle);
        pastOrdersListView = findViewById(R.id.pastOrdersListView);
        btnGoToMenu = findViewById(R.id.btnGoToMenu);
        btnGoToHome = findViewById(R.id.btnGoToHome);

        // Set up current order display
        displayCurrentOrder();

        // Load past orders from SharedPreferences and set up the adapter for the ListView
        loadPastOrders();

        // Back to Menu button click listener
        btnGoToMenu.setOnClickListener(v -> {
            startActivity(new Intent(OrderPage.this, MenuPage.class));
            finish();
        });

        // Back to Home button click listener
        btnGoToHome.setOnClickListener(v -> {
            startActivity(new Intent(OrderPage.this, HomePage.class));
            finish();
        });

        // Item click listener for past orders
        pastOrdersListView.setOnItemClickListener((parent, view, position, id) -> {
            String orderDetails = (String) parent.getItemAtPosition(position);
            Intent intent = new Intent(OrderPage.this, OrderTrackingPage.class);
            intent.putExtra("orderDetails", orderDetails);
            startActivity(intent);
        });
    }

    // Display the current order summary (e.g., items, status)
    private void displayCurrentOrder() {
        Intent intent = getIntent();
        String currentOrder = intent.getStringExtra("orderSummary");
        if (currentOrder != null) {
            tvOrderSummary.setText("Your Current Order:\n" + currentOrder);
        } else {
            tvOrderSummary.setText("No active order.");
        }
    }

    // Load past orders from SharedPreferences
    private void loadPastOrders() {
        SharedPreferences prefs = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        String orderData = prefs.getString("orderData", "");
        String[] orders = orderData.split("\n");

        // If there are past orders, display them in the ListView
        if (orders.length > 0) {
            ArrayList<String> pastOrders = new ArrayList<>();
            for (String order : orders) {
                pastOrders.add(order);
            }

            PastOrderAdapter adapter = new PastOrderAdapter(this, pastOrders);
            pastOrdersListView.setAdapter(adapter);
        } else {
            tvPastOrdersTitle.setText("No past orders found.");
        }
    }
}
