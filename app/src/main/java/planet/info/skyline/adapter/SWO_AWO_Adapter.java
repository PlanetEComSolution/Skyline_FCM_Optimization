package planet.info.skyline.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.model.MySwo;
import planet.info.skyline.model.Vendor;
import planet.info.skyline.tech.swo.SwoListActivity;

/**
 * Created by ravi on 16/11/17.
 */

public class SWO_AWO_Adapter extends RecyclerView.Adapter<SWO_AWO_Adapter.MyViewHolder>
        {
    private Context context;
    private List<MySwo> SWO_List;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView vendor;
        public LinearLayout ll_parent;

        public MyViewHolder(View view) {
            super(view);
            vendor = view.findViewById(R.id.txtvw_vendor);
            ll_parent= view.findViewById(R.id.ll_parent);
          /*  view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(context instanceof SwoListActivity){

                        Intent returnIntent = new Intent();

                        returnIntent.putExtra("Swo_id", SWO_List.get(i).getSwo_id());
                        setResult(Activity.RESULT_OK, returnIntent);
                        ((Activity) context).setResult(Activity.RESULT_OK, returnIntent);

                        ((Activity) context).finish();
                    }
                    // send selected vendor in callback
                   // listener.onVendorSelected(SWO_ListFiltered.get(getAdapterPosition()));
                }
            });*/
        }
    }


    public SWO_AWO_Adapter(Context context, List<MySwo> SWO_List) {
        this.context = context;
        this.SWO_List = SWO_List;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_vendor, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final MySwo swo = SWO_List.get(position);
        holder.vendor.setText(swo.getName());
        holder.ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof SwoListActivity){

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Swo_id", SWO_List.get(position).getSwo_id());
                    ((Activity) context).setResult(Activity.RESULT_OK, returnIntent);
                    ((Activity) context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return SWO_List.size();
    }



}
