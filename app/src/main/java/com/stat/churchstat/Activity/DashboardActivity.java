package com.stat.churchstat.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.bijoysingh.starter.util.PermissionManager;
import com.stat.churchstat.Database.AppData;
import com.stat.churchstat.LoginActivity;
import com.stat.churchstat.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DashboardActivity extends AppCompatActivity {

    Button dash, forum, database, settings;
    AppData data;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dash = (Button) findViewById(R.id.btn_dash);
        forum = (Button) findViewById(R.id.btn_forum);
        database = (Button) findViewById(R.id.btn_data);
        settings = (Button) findViewById(R.id.btn_set);
        data = new AppData(DashboardActivity.this);

        askForPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            data.setRemember(false);
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void DashClick(View view) {
        startActivity(new Intent(DashboardActivity.this, HomeActivity.class));
    }

    public void DataClick(View view) {
        startActivity(new Intent(DashboardActivity.this, DatabaseActivity.class));
    }

    public void SetClick(View view) {
        startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
    }

    public void RateClick(View view) {
        startActivity(new Intent(DashboardActivity.this, RatingsActivity.class));
    }

    private void askForPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        PermissionManager manager = new PermissionManager(this, permissions);
        if (!manager.hasAllPermissions()) {
            manager.requestPermissions();
        }
    }
}
