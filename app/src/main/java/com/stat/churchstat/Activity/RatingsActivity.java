package com.stat.churchstat.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.stat.churchstat.Database.AppData;
import com.stat.churchstat.Database.Ratings;
import com.stat.churchstat.MyApplication;
import com.stat.churchstat.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RatingsActivity extends AppCompatActivity {

    RatingBar ratingBar;
    TextView textView, category;
    CardView cardView;
    AppData data;
    String category_selected = "none";
    int ratingValue = 0;
    EditText editText;
    ArrayList<Ratings> ratingsArrayList = new ArrayList<>();
    String improves = "";
    String date;
    CheckBox checkBox1, checkBox2;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = new AppData(RatingsActivity.this);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        textView = (TextView) findViewById(R.id.ratingTitle);
        cardView = (CardView) findViewById(R.id.cardView);
        category = (TextView) findViewById(R.id.rate_category);
        editText = (EditText) findViewById(R.id.ratingComment);
        checkBox1 = (CheckBox)findViewById(R.id.check1);
        checkBox2 = (CheckBox)findViewById(R.id.check2);

        if (!data.getRetain()) {
            checkBox1.setVisibility(View.GONE);
            checkBox2.setVisibility(View.GONE);
        }

        Calendar calendar = Calendar.getInstance();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int getMonth = calendar.get(Calendar.MONTH);
        date = months[getMonth] + " " + calendar.get(Calendar.DATE) + " " + calendar.get(Calendar.YEAR);

        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingValue = (int) v;
                if (v < 5) {
                    cardView.setVisibility(View.VISIBLE);
                } else {
                    cardView.setVisibility(View.GONE);
                }
                switch ((int) v) {
                    case 1:
                        textView.setText("Terrible");
                        break;
                    case 2:
                        textView.setText("Bad");
                        break;
                    case 3:
                        textView.setText("OK");
                        break;
                    case 4:
                        textView.setText("Good");
                        break;
                    case 5:
                        textView.setText("Excellent");
                        break;

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_view) {
            startActivity(new Intent(RatingsActivity.this, ViewRatingsActivity.class));
        }
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddRateCategoryClick(View view) {
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
                        category.setText("Category - " + category_selected);
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
    }


    public void WhatWentWrong(View view) {
        new MaterialDialog.Builder(this)
                .title("What can be improved?")
                .items(R.array.wrongs)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        for (int i = 0; i < text.length; i++) {
                            improves += text[i] + ",";
                            //Toast.makeText(RatingsActivity.this, "" + text[i], Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                })
                .positiveText("Done")
                .show();
    }

    public void submitRating(View view) {
//        if (ratingValue == 0) {
//            error("Please rating this category");
//            return;
//        }
        if (category_selected.contentEquals("none")) {
            error("Please select a category");
            return;
        }
        store_to_database();
    }

    private void store_to_database() {
        if (data.getRetain()) {
            int[] Counts = data.getMaleFemale();
            int getMaleCount = Counts[0];
            int getFemaleCount = Counts[1];
            if (checkBox1.isChecked() && getMaleCount > 0) {
                data.setCounterForMale(getMaleCount - 1);
            }else if (checkBox2.isChecked() && getFemaleCount > 0) {
                data.setCounterForFemale(getFemaleCount - 1);
            }
        }
        ratingsArrayList.clear();
        float star = ratingBar.getRating();
        if(star > 0) {
            String comment = editText.getText().toString();
            Ratings ratings = new Ratings(0, category_selected, String.valueOf(star), comment, improves, date);
            ratingsArrayList.add(ratings);
            MyApplication.getWritableRatings().insertRating(ratingsArrayList, false);
        }
        success();
    }

    private void error(String text) {
        new SweetAlertDialog(RatingsActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Attendance")
                .setContentText(text)
                .setConfirmText("OK")
                .show();
    }

    private void success() {
        new SweetAlertDialog(RatingsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Attendance")
                .setContentText("Thank you for your feedback :)")
                .setConfirmText("OK")
                .show();
        ratingValue = 0;
        ratingBar.setRating(0);
        improves = "";
        cardView.setVisibility(View.GONE);
        textView.setText("---");
        editText.setText("");
        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
    }
}
