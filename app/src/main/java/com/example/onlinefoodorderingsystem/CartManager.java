package com.example.onlinefoodorderingsystem;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CartManager {
    private static final String PREF_NAME = "CartPreferences";
    private static final String CART_KEY = "CartItems";
    private static ArrayList<CartItem> cartItems = new ArrayList<>();

    public static void addItem(Context context, CartItem item) {
        loadCart(context);
        cartItems.add(item);
        saveCart(context);
    }

    public static void removeItem(Context context, int position) {
        loadCart(context);
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
            saveCart(context);
        }
    }

    public static void updateQuantity(Context context, int position, int quantity) {
        loadCart(context);
        if (position >= 0 && position < cartItems.size()) {
            cartItems.get(position).setQuantity(quantity);
            saveCart(context);
        }
    }

    public static ArrayList<CartItem> getCartItems(Context context) {
        loadCart(context);
        return cartItems;
    }

    public static void clearCart(Context context) {
        cartItems.clear();
        saveCart(context);
    }

    private static void saveCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        editor.putString(CART_KEY, json);
        editor.apply();
    }

    private static void loadCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(CART_KEY, null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        cartItems = gson.fromJson(json, type);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
    }
}
