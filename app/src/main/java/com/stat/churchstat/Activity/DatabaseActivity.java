package com.stat.churchstat.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.stat.churchstat.Database.AppData;
import com.stat.churchstat.Database.Details;
import com.stat.churchstat.MainActivity;
import com.stat.churchstat.MyApplication;
import com.stat.churchstat.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DatabaseActivity extends AppCompatActivity {

    Button cate, month, year, filter;
    String category_selected = "none", month_selected = "none", year_selected = "none";
    AppData data;
    ArrayList<Details> current = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = new AppData(DatabaseActivity.this);
        current = MyApplication.getWritableDatabase().getAllMyDetails();

        cate = (Button) findViewById(R.id.dt_category);
        month = (Button) findViewById(R.id.dt_month);
        year = (Button) findViewById(R.id.dt_year);
        filter = (Button) findViewById(R.id.btn_filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ic_view) {
            startActivity(new Intent(DatabaseActivity.this, ViewAllActivity.class));
        }
        if (id == R.id.ic_view_rate) {
            startActivity(new Intent(DatabaseActivity.this, RateChartActivity.class));
        }
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
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
        bundle.putString("Dmonth", month_selected);
        bundle.putString("Dyear", year_selected);
        Intent intent = new Intent(DatabaseActivity.this, ChartActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void error(String text) {
        new SweetAlertDialog(DatabaseActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Attendance")
                .setContentText(text)
                .setConfirmText("OK")
                .show();
    }


    private Collection<String> getYears() {
        Collection<String> yrs = new ArrayList<>();
        for (int i = 0; i < current.size(); i++) {
            Details details = current.get(i);
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
}
