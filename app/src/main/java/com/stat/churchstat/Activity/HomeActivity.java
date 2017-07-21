package com.stat.churchstat.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sdsmdg.tastytoast.TastyToast;
import com.stat.churchstat.Database.AppData;
import com.stat.churchstat.Database.Details;
import com.stat.churchstat.MyApplication;
import com.stat.churchstat.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity {

    Button male, female, category;
    AppData data;
    int getMaleCount, getFemaleCount;
    String date, category_selected = "none";
    CheckBox checkBox;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        male = (Button) findViewById(R.id.btn_male);
        female = (Button) findViewById(R.id.btn_female);
        category = (Button) findViewById(R.id.btn_category);

        data = new AppData(HomeActivity.this);
        int[] dt = data.getMaleFemale();
        getMaleCount = dt[0];
        getFemaleCount = dt[1];

        Calendar calendar = Calendar.getInstance();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int getMonth = calendar.get(Calendar.MONTH);
        date = months[getMonth] + " " + calendar.get(Calendar.DATE) + " " + calendar.get(Calendar.YEAR);

        checkRetain();
    }

    public void MaleClicked(View view) {
        getMaleCount = getMaleCount + 1;
        data.setCounterForMale(getMaleCount);
        TastyToast.makeText(HomeActivity.this, "Welcome to TREM", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        Log.e("Counter", "Male = " + getMaleCount);
    }

    public void FemaleClicked(View view) {
        getFemaleCount = getFemaleCount + 1;
        data.setCounterForFemale(getFemaleCount);
        TastyToast.makeText(HomeActivity.this, "Welcome to TREM", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        Log.e("Counter", "Female = " + getFemaleCount);
    }

    private void checkRetain(){
        if(data.getRetain()){
            new MaterialDialog.Builder(this)
                    .title("Attendance")
                    .content("Do you still want to retain the previous count?")
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .positiveText("Yes")
                    .negativeText("Clear")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            data.setRetain(false);
                            getMaleCount = 0;
                            getFemaleCount = 0;
                            data.setCounterForMale(getMaleCount);
                            data.setCounterForFemale(getFemaleCount);
                        }
                    }).show();
        }
    }

    public void CategoryClicked(View view) {
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

    public void MaleBackSlash(View view) {
        if(getMaleCount > 0) {
            getMaleCount = getMaleCount - 1;
            TastyToast.makeText(HomeActivity.this, "Subtracted from Male", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        }else {
            TastyToast.makeText(HomeActivity.this, "Count is zero", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
        }
    }

    public void FemaleBackSlash(View view) {
        if(getFemaleCount > 0) {
            getFemaleCount = getFemaleCount - 1;
            TastyToast.makeText(HomeActivity.this, "Subtracted from Female", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        }else {
            TastyToast.makeText(HomeActivity.this, "Count is zero", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        checkBox = (CheckBox) menu.findItem(R.id.action_check_home).getActionView();
        checkBox.setText("Retain Count");
        checkBox.setBackgroundColor(getResources().getColor(R.color.md_divider_white));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ic_done) {
            if (category_selected.contentEquals("none")) {
                error();
            } else {
                DoneCounting();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void DoneCounting() {
        new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Attendance")
                .setContentText("Are you sure you want to upload it?")
                .setConfirmText("Yes, upload it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ArrayList<Details> detailses = new ArrayList<>();
                        Details details = new Details(0, String.valueOf(getMaleCount), String.valueOf(getFemaleCount), "", "", date, category_selected);
                        detailses.add(details);
                        MyApplication.getWritableDatabase().insertMyPost(detailses, false);
                        if (!checkBox.isChecked()) {
                            data.setRetain(false);
                            getMaleCount = 0;
                            getFemaleCount = 0;
                            data.setCounterForMale(getMaleCount);
                            data.setCounterForFemale(getFemaleCount);
                        }else {
                            data.setRetain(true);
                        }
                        sweetAlertDialog
                                .setTitleText("Uploaded!")
                                .setContentText("Today's attendance has been uploaded!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();

    }

    private void error() {
        new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Attendance")
                .setContentText("Please select a category")
                .setConfirmText("OK")
                .show();
    }
}
