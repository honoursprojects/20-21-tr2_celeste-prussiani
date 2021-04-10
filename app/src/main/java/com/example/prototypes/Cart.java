package com.example.prototypes;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class Cart {
    ArrayList<String> items;
    SharedPreferences pref;
    public Cart(SharedPreferences pref) {
        items = new ArrayList<String>();
        this.pref = pref;
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
}
