package com.stat.churchstat.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.stat.churchstat.Adapter.RatingsAdapter;
import com.stat.churchstat.Adapter.ViewAdapter;
import com.stat.churchstat.Database.Ratings;
import com.stat.churchstat.MyApplication;
import com.stat.churchstat.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewRatingsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RatingsAdapter adapter;
    ArrayList<Ratings> ratingses = new ArrayList<>();
    ArrayList<Ratings> detailses1 = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ratings);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ratingses = MyApplication.getWritableRatings().getAllMyRatings();
        for(int i = 0; i < ratingses.size(); i++){
            Ratings details = ratingses.get(i);
            if(!details.date.contentEquals("no_date")){
                detailses1.add(details);
            }
        }

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adapter = new RatingsAdapter(ViewRatingsActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewRatingsActivity.this));
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
