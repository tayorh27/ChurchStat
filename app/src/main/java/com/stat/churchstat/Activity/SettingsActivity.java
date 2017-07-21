package com.stat.churchstat.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.stat.churchstat.Database.AppData;
import com.stat.churchstat.R;

import java.util.ArrayList;
import java.util.Collection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {

    AppData data;
    EditText editText1, editText2;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = new AppData(SettingsActivity.this);
        editText1 = (EditText) findViewById(R.id.password1);
        editText2 = (EditText) findViewById(R.id.password2);
    }

    public void AddCategoryClick(View view) {
        new MaterialDialog.Builder(this)
                .title("Input Category Name")
                .inputRangeRes(2, 40, R.color.material_red_500)
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        if (input.length() < 0) {
                            error("Please type in a category");
                            return;
                        }
                        String getAll = data.getCategory();
                        String insert = getAll + "" + String.valueOf(input) + ",";
                        data.addCategory(insert);
                        success("Category successfully added");
                    }
                }).show();
    }

    public void DelCategory(View view) {
        String cat = data.getCategory();
        final String[] ct = cat.split(",");
        final Collection<String> collection = new ArrayList<>();
        for (int i = 0; i < ct.length; i++) {
            collection.add(ct[i]);
        }
        new MaterialDialog.Builder(this)
                .title("Select a Category")
                .items(collection)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        for (int i = 0; i < text.length; i++) {
                            String check_text = String.valueOf(text[i]);
                            for (int j = 0; j < collection.size(); j++) {
                                if (collection.contains(check_text)) {
                                    collection.remove(check_text);
                                }
                            }
                        }
                        String insert = "";
                        ArrayList<String> arrayList = (ArrayList<String>) collection;
                        for (int k = 0; k < arrayList.size(); k++) {
                            insert += arrayList.get(k) + ",";
                        }
                        data.addCategory(insert);
                        if (text.length > 1) {
                            success("Categories successfully deleted");
                        } else {
                            success("Category successfully deleted");
                        }
                        return true;
                    }
                })
                .positiveText("Delete Selected")
                .show();
    }

    public void changePassword(View view) {
        String oldP = editText1.getText().toString();
        String newP = editText2.getText().toString();
        String[] logins = data.getAdminDetails();
        if (oldP.length() <= 0 || newP.length() <= 0) {
            error("Please all fields must be filled");
            return;
        }
        if (!oldP.contentEquals(logins[1])) {
            error("Incorrect Old Password.");
            return;
        }
        data.setAdminDetails(logins[0], newP);
        success("Password changed successfully.");
        editText1.setText("");
        editText2.setText("");
    }

    private void error(String text) {
        new SweetAlertDialog(SettingsActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Attendance")
                .setContentText(text)
                .setConfirmText("OK")
                .show();
    }

    private void success(String text) {
        new SweetAlertDialog(SettingsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Attendance")
                .setContentText(text)
                .setConfirmText("OK")
                .show();
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
