package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartPage extends Activity {

    private ListView cartListView;
    private Button btnProceedToOrder;
    private Button btnBackToMenu;
    private ArrayList<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        btnProceedToOrder = findViewById(R.id.btnProceedToOrder);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

        // Get the cart items passed from MenuPage
        Intent intent = getIntent();
        cartItems = (ArrayList<CartItem>) intent.getSerializableExtra("cartItems");

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Set custom adapter for ListView
        CartAdapter adapter = new CartAdapter(cartItems);
        cartListView.setAdapter(adapter);

        // Proceed to PaymentPage when clicked
        btnProceedToOrder.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(CartPage.this, "You need to add something to the cart before proceeding to payment.", Toast.LENGTH_SHORT).show();
            } else {
                Intent paymentIntent = new Intent(CartPage.this, PaymentPage.class);
                paymentIntent.putExtra("cartItems", cartItems); // Pass cart items to PaymentPage
                startActivity(paymentIntent);
            }
        });

        // Back to MenuPage when clicked
        btnBackToMenu.setOnClickListener(v -> {
            Intent menuIntent = new Intent(CartPage.this, MenuPage.class);
            startActivity(menuIntent);
            finish(); // Close the current activity
        });
    }

    // Custom Adapter class to handle list item and quantity change
    private class CartAdapter extends ArrayAdapter<CartItem> {
        public CartAdapter(ArrayList<CartItem> items) {
            super(CartPage.this, R.layout.item_cart, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_cart, parent, false);
            }

            CartItem currentItem = getItem(position);

            TextView itemText = convertView.findViewById(R.id.itemText);
            TextView itemPrice = convertView.findViewById(R.id.itemPrice);
            TextView quantityText = convertView.findViewById(R.id.quantityText);
            Button btnMinus = convertView.findViewById(R.id.btnMinus);
            Button btnPlus = convertView.findViewById(R.id.btnPlus);
            Button deleteButton = convertView.findViewById(R.id.btnDeleteItem);

            // Set item name and price properly
            itemText.setText(currentItem.getItemName());
            itemPrice.setText("$" + currentItem.getItemPrice());
            quantityText.setText(String.valueOf(currentItem.getQuantity()));

            // Handle buttons for updating quantity and deleting items
            btnMinus.setOnClickListener(v -> {
                if (currentItem.getQuantity() > 1) {
                    currentItem.setQuantity(currentItem.getQuantity() - 1);
                    quantityText.setText(String.valueOf(currentItem.getQuantity()));
                    notifyDataSetChanged();
                }
            });

            btnPlus.setOnClickListener(v -> {
                currentItem.setQuantity(currentItem.getQuantity() + 1);
                quantityText.setText(String.valueOf(currentItem.getQuantity()));
                notifyDataSetChanged();
            });

            deleteButton.setOnClickListener(v -> {
                cartItems.remove(position);
                notifyDataSetChanged();
                Toast.makeText(CartPage.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }
    }
}
