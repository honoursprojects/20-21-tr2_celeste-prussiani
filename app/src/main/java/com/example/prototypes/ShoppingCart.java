package com.example.prototypes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Map;
import java.util.Set;

public class ShoppingCart extends AppCompatActivity {

    Cart cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("covidTracker_store", MODE_PRIVATE);
        cart = new Cart(pref);
        display();
    }

    public void display() {
        Map<String, Integer> items = cart.retrieveItems();
        Set<String> itemNames = items.keySet();
        for(String s : itemNames) {
            int resID = getResources().getIdentifier(s, "id", getPackageName());
            LinearLayout layout = findViewById(resID);
            layout.setVisibility(View.VISIBLE);
        }
    }

    public void remove(View view) {
        String itemName = getResources().getResourceEntryName(view.getId());
        cart.removeItem(itemName);
        int resID = getResources().getIdentifier(itemName, "id", getPackageName());
        LinearLayout layout = findViewById(resID);
        layout.setVisibility(View.GONE);
    }

    public void clearAll(View view) {
        LinearLayout ffp2 = findViewById(R.id.ffp2);
        LinearLayout ffp3 = findViewById(R.id.ffp3);
        LinearLayout surgical = findViewById(R.id.surgical);
        LinearLayout visor = findViewById(R.id.visor);
        LinearLayout gloves = findViewById(R.id.gloves);
        LinearLayout sanitiser = findViewById(R.id.sanitiser);
        ffp2.setVisibility(View.GONE);
        ffp3.setVisibility(View.GONE);
        surgical.setVisibility(View.GONE);
        visor.setVisibility(View.GONE);
        gloves.setVisibility(View.GONE);
        sanitiser.setVisibility(View.GONE);

        cart.clearItems();
    }


}