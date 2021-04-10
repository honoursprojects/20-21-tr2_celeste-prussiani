package com.example.prototypes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prototypes.CovidTracker.CovidTrackerStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoppingCart extends AppCompatActivity {

    Cart cart;
    LinearLayout msgBox;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("covidTracker_store", MODE_PRIVATE);
        cart = new Cart(pref);
        msgBox = findViewById(R.id.msgContainer);

        display();
    }

    public void display() {
        Map<String, Integer> items = cart.retrieveItems();
        Set<String> itemNames = items.keySet();
        if(!itemNames.isEmpty()) {
            msgBox.setVisibility(View.GONE);
            for(String s : itemNames) {
                int quantity = items.get(s);
                String price = "\n\nQuantity: " + quantity + " Price: £" + cart.getItemPrice(s, quantity);
                int resID = getResources().getIdentifier(s, "id", getPackageName());
                LinearLayout layout = findViewById(resID);
                String msgId = s + "Msg";
                int messageBoxId = getResources().getIdentifier(msgId, "id", getPackageName());
                TextView messageBox = findViewById(messageBoxId);
                layout.setVisibility(View.VISIBLE);
                messageBox.append(price);
            }
            displayTotalPrice();
        }
    }

    public void displayTotalPrice() {
        Button checkoutBtn = findViewById(R.id.checkoutBtn);
        double total = cart.getTotalPrice();
        checkoutBtn.setText("Checkout: £" + String.valueOf(total));
    }


    public void remove(View view) {
        String btnName = getResources().getResourceEntryName(view.getId());
        String itemName[] = btnName.split("(?=\\p{Upper})");
        cart.removeItem(itemName[0]);
        int resID = getResources().getIdentifier(itemName[0], "id", getPackageName());
        LinearLayout layout = findViewById(resID);
        layout.setVisibility(View.GONE);
        displayTotalPrice();
    }

    public void clearItems() {
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
        msgBox.setVisibility(View.VISIBLE);
        cart.clearItems();
    }

    public void clearAll(View view) {
        clearItems();
    }

    public void buyItems(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Thanks for your purchase")
                .setMessage("The items you ordered will be shipped out to you today. Thanks for helping everyone keep safe and protected!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       clearItems();
                       dialog.dismiss();
                    }
                })
                .show();
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(this, CovidTrackerStore.class);
        startActivity(intent);
        finish();
    }
}