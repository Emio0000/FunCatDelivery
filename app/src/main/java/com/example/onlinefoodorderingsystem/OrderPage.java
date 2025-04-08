package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderPage extends Activity {

    private ListView pastOrdersListView;
    private TextView tvNoOrdersMessage;
    private ArrayList<String> pastOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        pastOrdersListView = findViewById(R.id.pastOrdersListView);
        tvNoOrdersMessage = findViewById(R.id.tvNoOrdersMessage);

        // Load past orders from SharedPreferences
        loadPastOrders();

        if (pastOrders.isEmpty()) {
            tvNoOrdersMessage.setText("No past orders found.");
        } else {
            // Display past orders in ListView
            PastOrderAdapter adapter = new PastOrderAdapter(this, pastOrders);
            pastOrdersListView.setAdapter(adapter);
        }
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
