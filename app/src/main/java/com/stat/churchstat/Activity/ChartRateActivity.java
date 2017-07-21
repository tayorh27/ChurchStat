package com.stat.churchstat.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.sdsmdg.tastytoast.TastyToast;
import com.stat.churchstat.Database.Ratings;
import com.stat.churchstat.MyApplication;
import com.stat.churchstat.R;
import com.stat.churchstat.Utility.Arrangement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChartRateActivity extends AppCompatActivity {

    String getCategory, getMonth, getYear;
    ArrayList<Ratings> InitialCurrent;
    ArrayList<Ratings> current;
    BarChart chart;
    String name = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_rate);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        getCategory = bundle.getString("Dcategory");
        getMonth = bundle.getString("Dmonth");
        getYear = bundle.getString("Dyear");
        InitialCurrent = MyApplication.getWritableRatings().getAllMyRatings();
        current = CategoryFilter();
        Log.e("Database", "InitialCurrent = " + InitialCurrent.size() + " Current = " + current.size());

        chart = (BarChart) findViewById(R.id.chart1);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        if (getMonth.contentEquals("All months") || getYear.contentEquals("All years")) {
            name = "Avg. " + getCategory + " Rating Chart for " + getMonth + "," + getYear;
            chart.setDescription("Avg. " + getCategory + " Rating Chart for " + getMonth + "," + getYear);
        } else {
            name = getCategory + " Rating Chart for " + getMonth + "," + getYear;
            chart.setDescription(getCategory + " Rating Chart for " + getMonth + "," + getYear);
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
            TastyToast.makeText(ChartRateActivity.this, "Chart saved to gallery", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        }
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Ratings> CategoryFilter() {
        ArrayList<Ratings> detailses = new ArrayList<>();
        for (int i = 0; i < InitialCurrent.size(); i++) {
            Ratings details = InitialCurrent.get(i);
            if (!details.date.contentEquals("no_date")) {
                String cate = details.cat;
                Log.e("Categories", "Cate = " + cate + " Category = " + getCategory);
                if (cate.contentEquals(getCategory)) {
                    detailses.add(details);
                }
            }
        }
        return detailses;
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        Arrangement.arrangeRatings.clear();
        Arrangement.counter.clear();


        if (getMonth.contentEquals("All months")) {
            for (int i = 0; i < current.size(); i++) {
                Ratings details = current.get(i);
                String[] date = details.date.split(" ");
                String mth = date[0];
                if (date[2].contentEquals(getYear)) {//!mths.contains(mth) &&
                    Arrangement.doRatings(mth, Integer.parseInt(details.star.substring(0, 1)));
                }
            }
            Collection<Integer> _valueRate = Arrangement.arrangeRatings.values();
            Collection<Integer> _counter = Arrangement.counter.values();


            Iterator<Integer> valueRate = _valueRate.iterator();
            Iterator<Integer> count = _counter.iterator();

            for (int j = 0; j < Arrangement.arrangeRatings.size(); j++) {

                float getCount = count.next();
                float getRate = valueRate.next();
                BarEntry v1e1 = new BarEntry(getRate / getCount, j);
                valueSet1.add(v1e1);
            }
        } else {
            for (int k = 0; k < current.size(); k++) {
                Ratings details = current.get(k);
                String[] date = details.date.split(" ");
                if (date[0].contentEquals(getMonth) && date[2].contentEquals(getYear)) {

                    String month = date[0];
                    Arrangement.doRatings(month, Integer.parseInt(details.star.substring(0, 1)));

                }
            }
            Collection<Integer> _valueRate = Arrangement.arrangeRatings.values();
            Collection<Integer> _counter = Arrangement.counter.values();


            Iterator<Integer> valueRate = _valueRate.iterator();
            Iterator<Integer> count = _counter.iterator();

            for (int j = 0; j < Arrangement.arrangeRatings.size(); j++) {

                float getCount = count.next();
                float getRate = valueRate.next();
                BarEntry v1e1 = new BarEntry(getRate / getCount, j);
                valueSet1.add(v1e1);
            }


        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Rate");
        barDataSet1.setColor(getResources().getColor(R.color.material_red_500));
//        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Female");
//        barDataSet2.setColor(getResources().getColor(R.color.md_material_blue_600));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
//        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> mths = new ArrayList<>();
        ArrayList<String> mths2 = new ArrayList<>();
        ArrayList<String> check_yr = new ArrayList<>();

        ArrayList<String> xAxis = new ArrayList<>();
        if (getMonth.contentEquals("All months")) {
            for (int i = 0; i < current.size(); i++) {
                Ratings details = current.get(i);
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
//            for (int k = 0; k < current.size(); k++) {
//                Ratings details = current.get(k);
//                String[] date = details.date.split(" ");
//                String mth = date[0] + " " + date[1];
//                if (date[0].contentEquals(getMonth) && date[2].contentEquals(getYear)) {//!mths2.contains(mth) &&
//                    mths2.add(mth);
//                }
//            }
            //for (int l = 0; l < mths2.size(); l++) {
//                Log.e("xAxis labels", "xLAbel = " + mths.get(l));
            // xAxis.add(mths2.get(l));
            xAxis.add(getMonth);
            // }
        }

        return xAxis;
    }
}
