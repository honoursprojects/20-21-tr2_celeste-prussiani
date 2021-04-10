package com.example.prototypes.CovidTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.prototypes.Cart;
import com.example.prototypes.R;
import com.example.prototypes.ShoppingCart;

import java.util.ArrayList;

public class CovidTrackerStore extends AppCompatActivity {
    Intent intent;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker_store);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("covidTracker_store", MODE_PRIVATE);
        cart = new Cart(pref);

        ImageButton cartBtn = findViewById(R.id.cartBtn);
        if(cart.getItemList().isEmpty()) {
            cartBtn.setImageResource(R.drawable.ic_shopping_cart);
        } else {
            cartBtn.setImageResource(R.drawable.ic_notif_cart);
        }
    }

    public void openChat(View view) {
        intent = new Intent(this, CovidTrackerChat.class);
        startActivity(intent);
    }

    public void addToCart(View view) {
        String itemName = getResources().getResourceEntryName(view.getId());
        cart.addToCart(itemName);
        ImageButton cartBtn = findViewById(R.id.cartBtn);
        cartBtn.setImageResource(R.drawable.ic_notif_cart);
        Toast.makeText(this, itemName, Toast.LENGTH_SHORT);
    }

    public void openShoppingCart(View view) {
        intent = new Intent(this, ShoppingCart.class);
        startActivity(intent);
        finish();
    }

}