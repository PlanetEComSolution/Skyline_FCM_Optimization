package planet.info.skyline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.model.Vendor;

/**
 * Created by ravi on 16/11/17.
 */

public class VendorsAdapter extends RecyclerView.Adapter<VendorsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Vendor> vendorList;
    private List<Vendor> vendorListFiltered;
    private VendorsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView vendor;


        public MyViewHolder(View view) {
            super(view);
            vendor = view.findViewById(R.id.txtvw_vendor);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected vendor in callback
                    listener.onVendorSelected(vendorListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public VendorsAdapter(Context context, List<Vendor> vendorList, VendorsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.vendorList = vendorList;
        this.vendorListFiltered = vendorList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_vendor, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Vendor vendor = vendorListFiltered.get(position);
        holder.vendor.setText(vendor.getVenderName());

    }

    @Override
    public int getItemCount() {
        return vendorListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    vendorListFiltered = vendorList;
                } else {
                    List<Vendor> filteredList = new ArrayList<>();
                    for (Vendor row : vendorList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getVenderName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    vendorListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = vendorListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                vendorListFiltered = (ArrayList<Vendor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
