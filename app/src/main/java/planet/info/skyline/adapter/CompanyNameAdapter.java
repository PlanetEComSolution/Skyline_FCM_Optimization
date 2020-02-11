package planet.info.skyline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import planet.info.skyline.R;

/**
 * Created by Admin on 7/7/2017.
 */

public class CompanyNameAdapter extends ArrayAdapter<String> {
    Context context;
    int textViewResourceId;
    List<String> items, tempItems, suggestions;
    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((String) resultValue);
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (String people : tempItems) {
                   /* if (people.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }*/

                    if (people.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<String> filterList = (ArrayList<String>) results.values;
            try {
                if (results != null && results.count > 0) {
                    clear();
                    for (String people : filterList) {
                        add(people);
                        notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    };

    public CompanyNameAdapter(Context context, int textViewResourceId, List<String> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<String>(items); // this makes the difference.
        suggestions = new ArrayList<String>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_textview, parent, false);
        }
        try {
            String people = items.get(position);
            if (people != null) {
                TextView lblName = (TextView) view.findViewById(R.id.textView);
                if (lblName != null)
                    lblName.setText(people);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }
}
