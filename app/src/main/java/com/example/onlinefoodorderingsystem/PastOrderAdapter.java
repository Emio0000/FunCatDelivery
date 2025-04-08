package com.example.onlinefoodorderingsystem;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class PastOrderAdapter extends ArrayAdapter<String> {

    public PastOrderAdapter(Context context, ArrayList<String> orders) {
        super(context, android.R.layout.simple_list_item_1, orders);
    }
}
