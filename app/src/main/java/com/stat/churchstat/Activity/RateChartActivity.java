package com.stat.churchstat.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.stat.churchstat.Database.AppData;
import com.stat.churchstat.Database.Ratings;
import com.stat.churchstat.MyApplication;
import com.stat.churchstat.R;
import com.stat.churchstat.Utility.Arrangement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RateChartActivity extends AppCompatActivity {

    Button cate, month, year, filter;
    String category_selected = "none", month_selected = "none", year_selected = "none";
    AppData data;
    ArrayList<Ratings> current = new ArrayList<>();
    RelativeLayout relativeLayout;
    RatingBar ratingBar;
    TextView textView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_chart);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = new AppData(RateChartActivity.this);
        current = MyApplication.getWritableRatings().getAllMyRatings();

        cate = (Button) findViewById(R.id.dt_category);
        month = (Button) findViewById(R.id.dt_month);
        year = (Button) findViewById(R.id.dt_year);
        filter = (Button) findViewById(R.id.btn_filter);

        relativeLayout = (RelativeLayout) findViewById(R.id.rating_layout);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        textView = (TextView) findViewById(R.id.tv_rate);
    }

    public void CatClicked(View view) {
        String cat = data.getCategory();
        final String[] ct = cat.split(",");
        Collection<String> collection = new ArrayList<>();
        for (int i = 0; i < ct.length; i++) {
            collection.add(ct[i]);
        }
        new MaterialDialog.Builder(this)
                .title("Select a Category")
                .items(collection)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        category_selected = ct[which];
                        cate.setText("Category - " + category_selected);
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
    }

    public void MonthClicked(View view) {
        new MaterialDialog.Builder(this)
                .title("Select a Month")
                .items(R.array.months)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        String[] months = getResources().getStringArray(R.array.months);
                        month_selected = months[which];
                        month.setText("Month - " + month_selected);
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
    }

    public void YearClicked(View view) {
        final Collection<String> yr = getYears();
        final ArrayList<String> arrayList = (ArrayList<String>) yr;
        new MaterialDialog.Builder(this)
                .title("Select a Year")
                .items(yr)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        year_selected = arrayList.get(which);
                        year.setText("Year - " + year_selected);
                        getAverageRating();
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
    }

    public void getAverageRating() {
        if (!month_selected.contentEquals("All months")) {
            float getValue = getRatingValue();
            relativeLayout.setVisibility(View.VISIBLE);
            ratingBar.setRating(getValue);
            textView.setText("Total Average Rating for " + month_selected + " " + year_selected + " is " + getValue);
        }
    }

    public void FilterClicked(View view) {
        if (category_selected.contentEquals("none") || month_selected.contentEquals("none") || year_selected.contentEquals("none")) {
            error("Please select from all options above");
            return;
        }
        if (month_selected.contentEquals("All months") && year_selected.contentEquals("All years")) {
            error("Sorry you can't select All months and All years at once.");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("Dcategory", category_selected);
        bundle.putString("Dmonth", month_selected);//
        bundle.putString("Dyear", year_selected);
        Intent intent = new Intent(RateChartActivity.this, ChartRateActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void error(String text) {
        new SweetAlertDialog(RateChartActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Attendance")
                .setContentText(text)
                .setConfirmText("OK")
                .show();
    }


    private Collection<String> getYears() {
        Collection<String> yrs = new ArrayList<>();
        for (int i = 0; i < current.size(); i++) {
            Ratings details = current.get(i);
            if (!details.date.contentEquals("no_date")) {
                String[] date = details.date.split(" ");
                String yr = date[2];
                if (!yrs.contains(yr)) {
                    yrs.add(yr);
                }
            }
        }
        //yrs.add("All years");
        return yrs;
    }

    private float getRatingValue() {

        float value = 0;
        Arrangement.arrangeRatings.clear();
        Arrangement.counter.clear();


        if (month_selected.contentEquals("All months")) {
            for (int i = 0; i < current.size(); i++) {
                Ratings details = current.get(i);
                String[] date = details.date.split(" ");
                String mth = date[0];
                if (date[2].contentEquals(year_selected)) {//!mths.contains(mth) &&
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

                value = getRate / getCount;
            }
        } else {
            for (int k = 0; k < current.size(); k++) {
                Ratings details = current.get(k);
                String[] date = details.date.split(" ");
                if (date[0].contentEquals(month_selected) && date[2].contentEquals(year_selected)) {

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
                value = getRate / getCount;
            }


        }
        return value;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
