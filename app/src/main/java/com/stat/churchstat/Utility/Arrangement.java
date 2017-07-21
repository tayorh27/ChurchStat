package com.stat.churchstat.Utility;

import android.annotation.TargetApi;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanniAdewale on 04/01/2017.
 */

public class Arrangement {//male=30.5female=21

    public static Map<String, Integer> arrangeMale = new HashMap<>();
    public static Map<String, Integer> arrangeFemale = new HashMap<>();
    public static Map<String, Integer> arrangeRatings = new HashMap<>();

    public static Map<String, Integer> counter = new HashMap<>();

    public static int count = 1;

    public static void doMaleFemale(String month, int male, int female) {
        doMale(month, male);
        doFemale(month, female);
        doCount(month);
    }

    public static void doRatings(String month, int rate) {
        doRate(month, rate);
        doCount(month);
    }

    private static void doRate(String month, int rate) {
        if (arrangeRatings.containsKey(month)) {
            int getLastValue = arrangeRatings.get(month);
            arrangeRatings.remove(month);
            arrangeRatings.put(month, (getLastValue + rate));
        } else {
            arrangeRatings.put(month, rate);
        }
    }

    private static void doMale(String month, int male) {
        if (arrangeMale.containsKey(month)) {
            int getLastValue = arrangeMale.get(month);
            arrangeMale.remove(month);
            arrangeMale.put(month, (getLastValue + male));
        } else {
            arrangeMale.put(month, male);
        }
    }

    private static void doFemale(String month, int female) {
        if (arrangeFemale.containsKey(month)) {
            int getLastValue = arrangeFemale.get(month);
            arrangeFemale.remove(month);
            arrangeFemale.put(month, (getLastValue + female));
        } else {
            arrangeFemale.put(month, female);
        }
    }

    private static void doCount(String month) {
        if (counter.containsKey(month)) {
            int getLastValue = counter.get(month);
            counter.remove(month);
            counter.put(month, (getLastValue + 1));
        } else {
            counter.put(month, count);
        }
    }


}
