package com.stat.churchstat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.stat.churchstat.Activity.DashboardActivity;
import com.stat.churchstat.Activity.RatingsActivity;
import com.stat.churchstat.Database.AppData;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    EditText editText1, editText2;
    CheckBox checkBox;
    AppData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText1 = (EditText) findViewById(R.id.username);
        editText2 = (EditText) findViewById(R.id.password);
        checkBox = (CheckBox)findViewById(R.id.remember);
        data = new AppData(LoginActivity.this);
    }

    public void Login(View view) {
        String user = editText1.getText().toString();
        String pass = editText2.getText().toString();
        String[] logins = data.getAdminDetails();
        if (user.length() <= 0 || pass.length() <= 0) {
            error("Please all fields must be filled");
            return;
        }
        if (!pass.contentEquals(logins[1])) {
            error("Incorrect login details");
            return;
        }
        if(checkBox.isChecked()){
            data.setRemember(true);
        }
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        finish();
    }

    private void error(String text) {
        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Attendance")
                .setContentText(text)
                .setConfirmText("OK")
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(data.getRemember()){
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
    }
}
