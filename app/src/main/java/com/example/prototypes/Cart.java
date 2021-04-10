package com.example.prototypes;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cart {
    ArrayList<String> items;
    SharedPreferences pref;
    HashMap<String, Double> prices;

    public Cart(SharedPreferences pref) {
        items = new ArrayList<String>();
        prices = new HashMap<String, Double>();
        populatePrices();
        this.pref = pref;
    }

    public void populatePrices() {
        prices.put("ffp2", 10.0);
        prices.put("surgical", 1.0);
        prices.put("ffp3", 15.0);
        prices.put("visor", 15.0);
        prices.put("gloves", 0.5);
        prices.put("sanitiser", 5.0);
    }

    public void addToCart(String item) {
            SharedPreferences.Editor editor = pref.edit();
            this.items.add(item);
            if(!pref.contains(item)) {
                editor.putInt(item, 1);
            } else {
                int currentQuantity = pref.getInt(item, 0);
                editor.putInt(item, currentQuantity+1);
            }
            editor.commit();

    }

    public Map<String, Integer> retrieveItems() {
        Map<String, Integer> map = (Map<String, Integer>) pref.getAll();
        return map;
    }

    public void clearItems() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public String printItems() {
        StringBuilder stringBuilder = new StringBuilder();
        for(String s : items) {
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder.toString();
    }

    public void removeItem(String item) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(item);
        editor.commit();
    }

    public ArrayList<String> getItemList() {
        return items;
    }

    public Double getItemPrice(String item, int quantity) {
        double price = prices.get(item);
        double finalPrice = price*quantity;
        return finalPrice;
    }

    public Double getTotalPrice() {
        double sum = 0;
        Map<String, Integer> map = (Map<String, Integer>) pref.getAll();
        Set<String> items = map.keySet();
        for(String s : items) {
            int quantity = map.get(s);
            double price = prices.get(s);
            sum += price * quantity;
        }
        return sum;
    }

}
