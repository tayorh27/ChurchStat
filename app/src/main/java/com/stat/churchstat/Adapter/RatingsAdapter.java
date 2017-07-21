package com.stat.churchstat.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.stat.churchstat.Database.Ratings;
import com.stat.churchstat.R;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 10/02/2017.
 */

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.RatingHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<Ratings> Ratingses = new ArrayList<>();

    public RatingsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Ratings> Ratingses) {
        this.Ratingses = Ratingses;
        notifyDataSetChanged();
    }

    @Override
    public RatingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_rating, parent, false);
        RatingHolder vHolder = new RatingHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(RatingHolder holder, int position) {
        Ratings current = Ratingses.get(position);
        if (!current.date.contentEquals("no_date")) {
            holder.cat.setText(current.cat);
            holder.dt.setText(current.date);
            holder.cm.setText("Comment: "+current.comment);
            if(current.improves.length() > 0) {
                holder.im.setText("Improves: "+current.improves.substring(0, current.improves.lastIndexOf(",")));
            }else {
                holder.im.setText("Improves: none"+current.improves);
            }
            holder.ratingBar.setRating(Float.parseFloat(current.star));
        }
    }

    @Override
    public int getItemCount() {
        return Ratingses.size();
    }

    class RatingHolder extends RecyclerView.ViewHolder {

        TextView cat, dt, cm, im;
        RatingBar ratingBar;

        RatingHolder(View itemView) {
            super(itemView);
            cat = (TextView) itemView.findViewById(R.id.sapp_name);
            dt = (TextView) itemView.findViewById(R.id.sapp_nstatus);
            cm = (TextView) itemView.findViewById(R.id.sapp_dt);
            im = (TextView) itemView.findViewById(R.id.tv_improves);
            ratingBar = (RatingBar) itemView.findViewById(R.id.sapp_mode);
        }
    }
}
