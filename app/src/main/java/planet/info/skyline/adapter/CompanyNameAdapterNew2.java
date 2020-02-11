package planet.info.skyline.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.model.Company;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivityNew;

/**
 * Created by Admin on 7/7/2017.
 */

public class CompanyNameAdapterNew2  extends ArrayAdapter {

    private List<Company> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<Company> dataListAllItems;
   // private CompanyAdapterListener listener;


    public CompanyNameAdapterNew2(Context context, int resource, List<Company> storeDataLst){//,CompanyAdapterListener listener) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    //    this.listener = listener;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Company getItem(int position) {
        Log.d("CustomListAdapter",
                dataList.get(position).getEname());
        return dataList.get(position);
    }

    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_textview, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.textView);
        strName.setText(getItem(position).getEname());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send selected vendor in callback
                //listener.onCompanySelected(dataList.get(position));
                if (mContext instanceof SelectCompanyActivityNew) {
                    ((SelectCompanyActivityNew)mContext).onCompanySelected1(dataList.get(position));
                }


            }
        });


        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<Company>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<Company> matchValues = new ArrayList<Company>();

                for (Company dataItem : dataListAllItems) {
                    if (dataItem.getEname().toLowerCase().contains(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<Company>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}