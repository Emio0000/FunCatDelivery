package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartPage extends Activity {

    private ListView cartListView;
    private Button btnProceedToOrder;
    private Button btnBackToMenu;
    private TextView totalPriceTextView; // Reference to the total price TextView
    private ArrayList<CartItem> cartItems;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        btnProceedToOrder = findViewById(R.id.btnProceedToOrder);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);
        totalPriceTextView = findViewById(R.id.totalPrice); // Initialize the TextView

        // Load cart items from CartManager
        cartItems = CartManager.getCartItems(this);

        adapter = new CartAdapter(cartItems);
        cartListView.setAdapter(adapter);

        // Calculate and display total price
        updateTotalPrice();

        btnProceedToOrder.setOnClickListener(v -> {
            ArrayList<CartItem> selectedItems = new ArrayList<>();
            ArrayList<CartItem> unselectedItems = new ArrayList<>();

            // Separate selected and unselected items
            for (CartItem item : cartItems) {
                if (item.isSelected()) {
                    selectedItems.add(item);
                } else {
                    unselectedItems.add(item);
                }
            }

            // Check if at least one item is selected
            if (selectedItems.isEmpty()) {
                Toast.makeText(CartPage.this, "Please select at least one item to proceed.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save only the unselected items back to CartManager
            CartManager.replaceCart(CartPage.this, unselectedItems);

            // Proceed with selected items to the PaymentPage
            Intent paymentIntent = new Intent(CartPage.this, PaymentPage.class);
            paymentIntent.putExtra("cartItems", selectedItems);
            startActivity(paymentIntent);
        });

        btnBackToMenu.setOnClickListener(v -> {
            Intent menuIntent = new Intent(CartPage.this, MenuPage.class);
            startActivity(menuIntent);
            finish();
        });
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                totalPrice += item.getItemPrice() * item.getQuantity(); // Calculate total for selected items
            }
        }

        // Update the total price in the TextView
        totalPriceTextView.setText(String.format("$%.2f", totalPrice));
    }

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
            CheckBox checkBox = convertView.findViewById(R.id.itemCheckBox);  // Reference to the CheckBox
            Button btnMinus = convertView.findViewById(R.id.btnMinus);
            Button btnPlus = convertView.findViewById(R.id.btnPlus);
            Button deleteButton = convertView.findViewById(R.id.btnDeleteItem);

            itemText.setText(currentItem.getItemName());
            itemPrice.setText("$" + currentItem.getItemPrice());
            quantityText.setText(String.valueOf(currentItem.getQuantity()));
            checkBox.setChecked(currentItem.isSelected());  // Set the checkbox state based on selection

            // Handle checkbox click event
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                currentItem.setSelected(isChecked);  // Update the CartItem's selected state
                notifyDataSetChanged();  // Refresh the ListView to reflect the change
                updateTotalPrice(); // Update total price when selection changes
            });

            // Minus button to decrease quantity
            btnMinus.setOnClickListener(v -> {
                if (currentItem.getQuantity() > 1) {
                    currentItem.setQuantity(currentItem.getQuantity() - 1);
                    CartManager.updateQuantity(CartPage.this, position, currentItem.getQuantity());
                    notifyDataSetChanged();
                    updateTotalPrice(); // Update total price when quantity changes
                }
            });

            // Plus button to increase quantity
            btnPlus.setOnClickListener(v -> {
                currentItem.setQuantity(currentItem.getQuantity() + 1);
                CartManager.updateQuantity(CartPage.this, position, currentItem.getQuantity());
                notifyDataSetChanged();
                updateTotalPrice(); // Update total price when quantity changes
            });

            // Delete button to remove item from the cart
            deleteButton.setOnClickListener(v -> {
                CartManager.removeItem(CartPage.this, position);
                cartItems.remove(position);
                notifyDataSetChanged();
                Toast.makeText(CartPage.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
                updateTotalPrice(); // Update total price after item is removed
            });

            return convertView;
        }
    }
}
