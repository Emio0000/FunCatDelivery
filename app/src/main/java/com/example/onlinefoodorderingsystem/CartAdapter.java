package com.example.onlinefoodorderingsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvName.setText(item.getItemName());
        holder.tvPrice.setText(String.format("RM %.2f", item.getItemPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.checkBox.setChecked(item.isSelected());

        // Set image based on item name
        String itemName = item.getItemName().toLowerCase();
        int imageResId = R.drawable.logo; // fallback

        switch (itemName) {
            case "chicken chop":
                imageResId = R.drawable.chicken;
                break;
            case "lamb grilled":
                imageResId = R.drawable.lamb;
                break;
            case "double burger":
                imageResId = R.drawable.burger;
                break;
            case "deer satay":
                imageResId = R.drawable.satay;
                break;
            case "salad":
                imageResId = R.drawable.salad;
                break;
            case "salmon salad":
                imageResId = R.drawable.salmon;
                break;
            case "orange juice":
                imageResId = R.drawable.drink1;
                break;
            case "dragon juice":
                imageResId = R.drawable.drink2;
                break;
            case "ice matcha":
                imageResId = R.drawable.drink3;
                break;
            case "vanilla coffe":
                imageResId = R.drawable.drink4;
                break;
        }

        holder.imageView.setImageResource(imageResId);

        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                cartItems.remove(pos);
                notifyItemRemoved(pos);
            }
        });

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        Button btnPlus, btnMinus, btnDelete;
        CheckBox checkBox;
        ImageView imageView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.cart_item_name);
            tvPrice = itemView.findViewById(R.id.cart_item_price);
            tvQuantity = itemView.findViewById(R.id.cart_item_quantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            checkBox = itemView.findViewById(R.id.cart_item_checkbox);
            imageView = itemView.findViewById(R.id.cart_item_image);
        }
    }
}
