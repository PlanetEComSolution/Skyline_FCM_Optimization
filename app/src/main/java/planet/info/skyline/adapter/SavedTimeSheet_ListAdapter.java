package planet.info.skyline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.model.SavedTask;


/**
 * Created by agent on 12/22/16.
 */

public class SavedTimeSheet_ListAdapter extends BaseAdapter {
    Context context;



    ArrayList<SavedTask> list_savedTimesheet;

    public SavedTimeSheet_ListAdapter(Context activity, ArrayList<SavedTask> list_savedTimesheet) {
        // TODO Auto-generated constructor stub
        this.context = activity;



        this.list_savedTimesheet = list_savedTimesheet;


        //      this.icon=icon;
    }


    final public class ViewHolder {

        private TextView tv_jobtype;
        private TextView tv_comments;
        private TextView tv_total_duration;



    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_savedTimesheet.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            final ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.offline_timesheet_item, null);



                holder = new ViewHolder();
                holder.tv_jobtype = (TextView) convertView.findViewById(R.id.tv_jobtype);
                holder.tv_comments = (TextView) convertView.findViewById(R.id.tv_comments);
                holder.tv_total_duration = (TextView) convertView.findViewById(R.id.tv_total_duration);


                convertView.setTag(holder);


            } else {
                holder = (ViewHolder) convertView.getTag();

            }



            holder.tv_jobtype.setText(list_savedTimesheet.get(position).getJobType());
            holder.tv_comments.setText(list_savedTimesheet.get(position).getComments());
            holder.tv_total_duration.setText(list_savedTimesheet.get(position).getJob_Duration());


        } catch (Exception e) {
            e.getMessage();
        }
        return convertView;
    }


}
