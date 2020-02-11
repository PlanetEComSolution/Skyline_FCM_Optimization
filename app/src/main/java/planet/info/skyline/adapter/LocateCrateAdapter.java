package planet.info.skyline.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.model.CreateDetails;
import planet.info.skyline.tech.locate_crates.LocateCrates;
import planet.info.skyline.tech.whats_inside.ShowWhatsInside_Activity;
import planet.info.skyline.util.Utility;


public class LocateCrateAdapter extends RecyclerView.Adapter<LocateCrateAdapter.MyViewHolder> {
    private List<CreateDetails> moviesList;

    public LocateCrateAdapter(Activity context, List<CreateDetails> moviesList) {
        this.moviesList = moviesList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_missing_crates_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CreateDetails cc = moviesList.get(position);
        holder.creid.setText(cc.getUnique_crate_id());
        holder.credesc.setText(cc.getDescription());
        holder.index_no.setText(String.valueOf(position + 1));
        String location = cc.getLocationcrate();
        if (location == null || location.equalsIgnoreCase("null") || location.equalsIgnoreCase("")) {
            holder.creloca.setText("N/A");
        } else {
            holder.creloca.setText(location);
        }

        holder.selectt.setChecked(cc.isSelected());


        holder.selectt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    moviesList.get(position).setSelected(true);
                } else {
                    moviesList.get(position).setSelected(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView creid;
        TextView credesc;
        TextView creloca;
        CheckBox selectt;
        Button index_no;


        public MyViewHolder(View convertView) {
            super(convertView);
            creid = (TextView) convertView.findViewById(R.id.createid);
            credesc = (TextView) convertView.findViewById(R.id.description);
            creloca = (TextView) convertView.findViewById(R.id.locationss);
            selectt = (CheckBox) convertView.findViewById(R.id.selects);
            index_no = (Button) convertView.findViewById(R.id.serial_no);

        }
    }
}

