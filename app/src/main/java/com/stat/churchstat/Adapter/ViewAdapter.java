package com.stat.churchstat.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stat.churchstat.Database.Details;
import com.stat.churchstat.R;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 05/01/2017.
 */

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.VHolder>{

    Context context;
    LayoutInflater inflater;
    ArrayList<Details> detailses = new ArrayList<>();

    public ViewAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Details> detailses){
        this.detailses = detailses;
        notifyDataSetChanged();
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_attendance_list,parent,false);
        VHolder vHolder = new VHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(VHolder holder, int position) {
        Details current = detailses.get(position);
        if(!current.date.contentEquals("no_date")){
            holder.cat.setText(current.category);
            holder.dt.setText(current.date);
            holder.ml.setText("Number of Males: "+current.no_of_males);
            holder.fl.setText("Number of Females: "+current.no_of_females);
        }
    }

    @Override
    public int getItemCount() {
        return detailses.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{

        TextView cat,dt,ml,fl;

        public VHolder(View itemView) {
            super(itemView);

            cat = (TextView)itemView.findViewById(R.id.sapp_name);
            dt = (TextView)itemView.findViewById(R.id.sapp_nstatus);
            ml = (TextView)itemView.findViewById(R.id.sapp_mode);
            fl = (TextView)itemView.findViewById(R.id.sapp_dt);
        }
    }
}
