package com.stat.churchstat.Activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sdsmdg.tastytoast.TastyToast;
import com.stat.churchstat.Database.Details;
import com.stat.churchstat.MyApplication;
import com.stat.churchstat.R;
import com.stat.churchstat.Utility.Arrangement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChartActivity extends AppCompatActivity {

    String getCategory, getMonth, getYear;
    ArrayList<Details> InitialCurrent;
    ArrayList<Details> current;
    int MaleCount, FemaleCount;
    BarChart chart;
    String name = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        getCategory = bundle.getString("Dcategory");
        getMonth = bundle.getString("Dmonth");
        getYear = bundle.getString("Dyear");
        InitialCurrent = MyApplication.getWritableDatabase().getAllMyDetails();
        current = CategoryFilter();
        Log.e("Database", "InitialCurrent = " + InitialCurrent.size() + " Current = " + current.size());

        chart = (BarChart) findViewById(R.id.chart1);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        if (getMonth.contentEquals("All months") || getYear.contentEquals("All years")) {
            name = "Avg. " + getCategory + " Chart for " + getMonth + "," + getYear;
            chart.setDescription("Avg. " + getCategory + " Chart for " + getMonth + "," + getYear);
        } else {
            name = getCategory + " Chart for " + getMonth + "," + getYear;
            chart.setDescription(getCategory + " Chart for " + getMonth + "," + getYear);
        }
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            chart.saveToGallery(name, BarChart.DRAWING_CACHE_QUALITY_HIGH);
            TastyToast.makeText(ChartActivity.this, "Chart saved to gallery", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        }
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Details> CategoryFilter() {
        ArrayList<Details> detailses = new ArrayList<>();
        for (int i = 0; i < InitialCurrent.size(); i++) {
            Details details = InitialCurrent.get(i);
            if (!details.date.contentEquals("no_date")) {
                String cate = details.category;
                Log.e("Categories", "Cate = " + cate + " Category = " + getCategory);
                if (cate.contentEquals(getCategory)) {
                    detailses.add(details);
                }
            }
        }
        return detailses;
    }


    private ArrayList<BarDataSet> _getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Male");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Female");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> _getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();

        Arrangement.arrangeMale.clear();
        Arrangement.arrangeFemale.clear();
        Arrangement.counter.clear();


        if (getMonth.contentEquals("All months")) {
            for (int i = 0; i < current.size(); i++) {
                Details details = current.get(i);
                String[] date = details.date.split(" ");
                String mth = date[0];
                if (date[2].contentEquals(getYear)) {//!mths.contains(mth) &&
                    Arrangement.doMaleFemale(mth, Integer.parseInt(details.no_of_males), Integer.parseInt(details.no_of_females));
                }
            }
            Collection<Integer> _valueMale = Arrangement.arrangeMale.values();
            Collection<Integer> _valueFemale = Arrangement.arrangeFemale.values();
            Collection<Integer> _counter = Arrangement.counter.values();


            Iterator<Integer> valueMale = _valueMale.iterator();
            Iterator<Integer> valueFemale = _valueFemale.iterator();
            Iterator<Integer> count = _counter.iterator();

            for (int j = 0; j < Arrangement.arrangeMale.size(); j++) {

                int getCount = count.next();
                BarEntry v1e1 = new BarEntry(Float.parseFloat(String.valueOf(valueMale.next() / getCount)), j);
                valueSet1.add(v1e1);

                BarEntry v2e1 = new BarEntry(Float.parseFloat(String.valueOf(valueFemale.next() / getCount)), j); // Jan
                valueSet2.add(v2e1);
            }
        } else {
            for (int k = 0; k < current.size(); k++) {
                Details details = current.get(k);
                String[] date = details.date.split(" ");
                if (date[0].contentEquals(getMonth) && date[2].contentEquals(getYear)) {

                    BarEntry _v1e1 = new BarEntry(Float.parseFloat(details.no_of_males), k);
                    valueSet1.add(_v1e1);

                    BarEntry _v2e1 = new BarEntry(Float.parseFloat(details.no_of_females), k); // Jan
                    valueSet2.add(_v2e1);

                    //Log.e("yAxix Added", "Males = " + details.no_of_males + " Female = " + details.no_of_females);
                }
            }
        }
//        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
//        valueSet1.add(v1e1);
//        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
//        valueSet1.add(v1e2);
//        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
//        valueSet1.add(v1e3);
//        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
//        valueSet1.add(v1e4);
//        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
//        valueSet1.add(v1e5);
//        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
//        valueSet1.add(v1e6);
//
//
//        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
//        valueSet2.add(v2e1);
//        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
//        valueSet2.add(v2e2);
//        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
//        valueSet2.add(v2e3);
//        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
//        valueSet2.add(v2e4);
//        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
//        valueSet2.add(v2e5);
//        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
//        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Male");
        barDataSet1.setColor(getResources().getColor(R.color.material_red_500));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Female");
        barDataSet2.setColor(getResources().getColor(R.color.md_material_blue_600));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }


    private ArrayList<String> getXAxisValues() {
        ArrayList<String> mths = new ArrayList<>();
        ArrayList<String> mths2 = new ArrayList<>();
        ArrayList<String> check_yr = new ArrayList<>();

        ArrayList<String> xAxis = new ArrayList<>();

        if (getYear.contentEquals("All years")) {
            for (int k = 0; k < current.size(); k++) {
                Details details = current.get(k);
                String[] date = details.date.split(" ");
                String _yr = date[2];
                if (!check_yr.contains(_yr)) {//!mths2.contains(mth) &&
                    check_yr.add(_yr);
                }
            }
            for (int l = 0; l < mths2.size(); l++) {
//                Log.e("xAxis labels", "xLAbel = " + mths.get(l));
                xAxis.add(check_yr.get(l));
            }
        } else if (getMonth.contentEquals("All months")) {
            for (int i = 0; i < current.size(); i++) {
                Details details = current.get(i);
                String[] date = details.date.split(" ");
                String mth = date[0];
                if (!mths.contains(mth) && date[2].contentEquals(getYear)) {
                    mths.add(mth);
                }
            }
            for (int j = 0; j < mths.size(); j++) {
                Log.e("xAxis labels", "xLAbel = " + mths.get(j));
                xAxis.add(mths.get(j));
            }
        } else {
            for (int k = 0; k < current.size(); k++) {
                Details details = current.get(k);
                String[] date = details.date.split(" ");
                String mth = date[0] + " " + date[1];
                if (date[0].contentEquals(getMonth) && date[2].contentEquals(getYear)) {//!mths2.contains(mth) &&
                    mths2.add(mth);
                }
            }
            for (int l = 0; l < mths2.size(); l++) {
//                Log.e("xAxis labels", "xLAbel = " + mths.get(l));
                xAxis.add(mths2.get(l));
            }
        }

        return xAxis;
    }
}
