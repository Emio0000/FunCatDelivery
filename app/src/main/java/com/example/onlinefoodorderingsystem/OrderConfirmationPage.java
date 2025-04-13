package com.example.onlinefoodorderingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OrderConfirmationPage extends AppCompatActivity {

    private ListView orderConfirmationListView;
    private TextView tvConfirmationMessage, tvPaymentMethod, tvTotalAmount;
    private Button btnGoToMenu, btnGoToHome, btnTrackOrder;
    private ArrayList<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        orderConfirmationListView = findViewById(R.id.orderConfirmationListView);
        tvConfirmationMessage = findViewById(R.id.tvConfirmationMessage);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnGoToMenu = findViewById(R.id.btnGoToMenu);
        btnGoToHome = findViewById(R.id.btnGoToHome);
        btnTrackOrder = findViewById(R.id.btnTrackOrder);

        SharedPreferences prefs = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        String cartJson = prefs.getString("orderData", "");
        String paymentMethod = prefs.getString("paymentMethod", "Unknown");

        cartItems = parseOrderData(cartJson);

        ArrayList<String> itemDetails = new ArrayList<>();
        double total = 0;
        for (CartItem item : cartItems) {
            itemDetails.add(item.getItemName() + " x" + item.getQuantity() + " - RM" + String.format("%.2f", item.getItemPrice()));
            total += item.getItemPrice() * item.getQuantity();
        }

        orderConfirmationListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemDetails));
        tvConfirmationMessage.setText("Thank you for your order!");
        tvPaymentMethod.setText("Paid with: " + paymentMethod);
        tvTotalAmount.setText("Total: RM" + String.format("%.2f", total));

        btnGoToMenu.setOnClickListener(v -> startActivity(new Intent(this, MenuPage.class)));
        btnGoToHome.setOnClickListener(v -> startActivity(new Intent(this, HomePage.class)));
        btnTrackOrder.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "Nothing to track!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences trackPrefs = getSharedPreferences("OrderTrackingPrefs", MODE_PRIVATE);
            trackPrefs.edit()
                    .putString("trackingFood", cartItems.get(0).getItemName())
                    .putInt("trackingStatus", 0)
                    .apply();

            startActivity(new Intent(this, OrderTrackingPage.class));
        });
    }

    private ArrayList<CartItem> parseOrderData(String jsonData) {
        if (jsonData == null || jsonData.isEmpty()) return new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        return gson.fromJson(jsonData, type);
    }
}
