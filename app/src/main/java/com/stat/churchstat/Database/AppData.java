package com.stat.churchstat.Database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sanniAdewale on 04/01/2017.
 */

public class AppData {

    Context context;
    SharedPreferences prefs;

    public AppData(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("app_data", 0);
    }

    public void setAdminDetails(String username, String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    public String[] getAdminDetails() {
        String[] details = {
                prefs.getString("username", ""),
                prefs.getString("password", "")
        };
        return details;
    }

    public void setRemember(boolean remember) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("remember", remember);
        editor.apply();
    }

    public boolean getRemember() {
        return prefs.getBoolean("remember", false);
    }

    public void setTourShown(boolean tour) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("tour", tour);
        editor.apply();
    }

    public boolean getTourShown() {
        return prefs.getBoolean("tour", false);
    }

    public void setRetain(boolean retain) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("retain", retain);
        editor.apply();
    }

    public boolean getRetain() {
        return prefs.getBoolean("retain", false);
    }

    public void setCounterForMale(int count) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("male_count", count);
        editor.apply();
    }

    public void setCounterForFemale(int count) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("female_count", count);
        editor.apply();
    }

    public int[] getMaleFemale() {
        int[] male_female = {
                prefs.getInt("male_count", 0),
                prefs.getInt("female_count", 0)
        };
        return male_female;
    }

    public void addCategory(String category) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("category", category);
        editor.apply();
    }

    public String getCategory() {
        return prefs.getString("category", "");
    }
}
