package com.stat.churchstat.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.stat.churchstat.Adapter.ViewAdapter;
import com.stat.churchstat.Database.Details;
import com.stat.churchstat.MyApplication;
import com.stat.churchstat.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewAllActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ViewAdapter adapter;
    ArrayList<Details> detailses = new ArrayList<>();
    ArrayList<Details> detailses1 = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailses = MyApplication.getWritableDatabase().getAllMyDetails();
        for (int i = 0; i < detailses.size(); i++) {
            Details details = detailses.get(i);
            if (!details.date.contentEquals("no_date")) {
                detailses1.add(details);
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new ViewAdapter(ViewAllActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewAllActivity.this));
        recyclerView.setAdapter(adapter);
        adapter.setData(detailses1);
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
