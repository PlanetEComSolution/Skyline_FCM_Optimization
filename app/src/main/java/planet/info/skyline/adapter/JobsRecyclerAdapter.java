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
import planet.info.skyline.model.Job_2;
import planet.info.skyline.client.SearchJobActivity;

/**
 * Created by ravi on 16/11/17.
 */

public class JobsRecyclerAdapter extends RecyclerView.Adapter<JobsRecyclerAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Job_2> JobsList;
    private List<Job_2> JobsListFiltered;


    public JobsRecyclerAdapter(Context context, List<Job_2> JobsList) {
        this.context = context;
      //  this.listener = listener;
        this.JobsList = JobsList;
        this.JobsListFiltered = JobsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_textview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Job_2 Jobs = JobsListFiltered.get(position);
        holder.Jobs.setText(Jobs.getJOB_Name_Desc());

    }

    @Override
    public int getItemCount() {
        return JobsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    JobsListFiltered = JobsList;
                } else {
                    List<Job_2> filteredList = new ArrayList<>();
                    for (Job_2 row : JobsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getJobName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    JobsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = JobsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                JobsListFiltered = (ArrayList<Job_2>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Jobs;

        public MyViewHolder(View view) {
            super(view);
            Jobs = view.findViewById(R.id.textView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected vendor in callback
                   // listener.onVendorSelected(vendorListFiltered.get(getAdapterPosition()));
                    if (context instanceof SearchJobActivity) {
                        ((SearchJobActivity) context).onJobSelected(JobsListFiltered.get(getAdapterPosition()));
                    }
                }
            });
        }
    }


}
