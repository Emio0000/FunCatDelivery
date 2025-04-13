package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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

        cartItems = CartManager.getCartItems(this);

        for (CartItem item : cartItems) {
            item.setSelected(true); // Select all items by default
        }

        adapter = new CartAdapter(cartItems);
        cartListView.setAdapter(adapter);

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
                Toast.makeText(this, "Please select at least one item.", Toast.LENGTH_SHORT).show();
                return;
            }

            CartManager.replaceCart(this, unselectedItems);

            Intent intent = new Intent(CartPage.this, PaymentPage.class);
            intent.putExtra("cartItems", selectedItems);
            startActivity(intent);
        });

        btnBackToMenu.setOnClickListener(v -> {
            startActivity(new Intent(CartPage.this, MenuPage.class));
            finish();
        });
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                total += item.getItemPrice() * item.getQuantity();
            }
        }
        totalPriceTextView.setText(String.format("RM%.2f", total));
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

            ImageView itemImage = convertView.findViewById(R.id.cart_item_image);
            TextView itemName = convertView.findViewById(R.id.cart_item_name);
            TextView itemPrice = convertView.findViewById(R.id.cart_item_price);
            TextView quantityText = convertView.findViewById(R.id.cart_item_quantity);
            CheckBox checkBox = convertView.findViewById(R.id.cart_item_checkbox);
            Button btnMinus = convertView.findViewById(R.id.btnMinus);
            Button btnPlus = convertView.findViewById(R.id.btnPlus);
            Button deleteButton = convertView.findViewById(R.id.btnDelete);

            itemImage.setImageResource(currentItem.getImageResId());
            itemName.setText(currentItem.getItemName());
            itemPrice.setText("RM" + currentItem.getItemPrice());
            quantityText.setText(String.valueOf(currentItem.getQuantity()));
            checkBox.setChecked(currentItem.isSelected());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                currentItem.setSelected(isChecked);
                notifyDataSetChanged();
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
