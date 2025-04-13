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
    private TextView totalPriceTextView;
    private ArrayList<CartItem> cartItems;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        btnProceedToOrder = findViewById(R.id.btnProceedToOrder);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);
        totalPriceTextView = findViewById(R.id.totalPrice);

        // Load cart items from CartManager
        cartItems = CartManager.getCartItems(this);

        // ✅ Preselect all items by default
        for (CartItem item : cartItems) {
            item.setSelected(true);
        }

        adapter = new CartAdapter(cartItems);
        cartListView.setAdapter(adapter);

        // Calculate and display total price
        updateTotalPrice();

        btnProceedToOrder.setOnClickListener(v -> {
            ArrayList<CartItem> selectedItems = new ArrayList<>();
            ArrayList<CartItem> unselectedItems = new ArrayList<>();

            for (CartItem item : cartItems) {
                if (item.isSelected()) {
                    selectedItems.add(item);
                } else {
                    unselectedItems.add(item);
                }
            }

            if (selectedItems.isEmpty()) {
                Toast.makeText(CartPage.this, "Please select at least one item to proceed.", Toast.LENGTH_SHORT).show();
                return;
            }

            // DEBUG TOAST
            Toast.makeText(CartPage.this, "Selected items: " + selectedItems.size(), Toast.LENGTH_SHORT).show();

            CartManager.replaceCart(CartPage.this, unselectedItems);

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
                totalPrice += item.getItemPrice() * item.getQuantity();
            }
        }
        totalPriceTextView.setText(String.format("RM%.2f", totalPrice));
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
            CheckBox checkBox = convertView.findViewById(R.id.itemCheckBox);
            Button btnMinus = convertView.findViewById(R.id.btnMinus);
            Button btnPlus = convertView.findViewById(R.id.btnPlus);
            Button deleteButton = convertView.findViewById(R.id.btnDeleteItem);

            itemText.setText(currentItem.getItemName());
            itemPrice.setText("RM" + currentItem.getItemPrice());
            quantityText.setText(String.valueOf(currentItem.getQuantity()));

            // ✅ Fix for checkbox state change triggering during view recycling
            checkBox.setOnCheckedChangeListener(null); // Remove previous listener
            checkBox.setChecked(currentItem.isSelected());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                currentItem.setSelected(isChecked);
                updateTotalPrice();
            });

            btnMinus.setOnClickListener(v -> {
                if (currentItem.getQuantity() > 1) {
                    currentItem.setQuantity(currentItem.getQuantity() - 1);
                    CartManager.updateQuantity(CartPage.this, position, currentItem.getQuantity());
                    notifyDataSetChanged();
                    updateTotalPrice();
                }
            });

            btnPlus.setOnClickListener(v -> {
                currentItem.setQuantity(currentItem.getQuantity() + 1);
                CartManager.updateQuantity(CartPage.this, position, currentItem.getQuantity());
                notifyDataSetChanged();
                updateTotalPrice();
            });

            deleteButton.setOnClickListener(v -> {
                CartManager.removeItem(CartPage.this, position);
                cartItems.remove(position);
                notifyDataSetChanged();
                Toast.makeText(CartPage.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
                updateTotalPrice();
            });

            return convertView;
        }
    }
}
