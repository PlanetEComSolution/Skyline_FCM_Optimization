package planet.info.skyline.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.model.PausedJob;
import planet.info.skyline.shared_preference.Shared_Preference;

/**
 * Created by Admin on 7/7/2017.
 */

public class PausedJob_ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<PausedJob> list_PausedJobs;
    String userRole;

    public PausedJob_ListAdapter(Context activity, ArrayList<PausedJob> list_PausedJobs) {
        this.context = activity;
        this.list_PausedJobs = list_PausedJobs;
        userRole = Shared_Preference.getUSER_ROLE(context);

    }

    @Override
    public int getCount() {
        return list_PausedJobs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            final ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_row_recent_transac_2, null);

                holder = new ViewHolder();
                holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                holder.tv_SWO_AWO_Name = (TextView) convertView.findViewById(R.id.tv_SWO_AWO_Name);
                holder.tv_job_name = (TextView) convertView.findViewById(R.id.tv_job_name);
                holder.serial_no = (Button) convertView.findViewById(R.id.serial_no);
                holder.tv_job_desc = (TextView) convertView.findViewById(R.id.tv_job_desc);
                holder.card_view = convertView.findViewById(R.id.card_view);
                holder.ll_swo_row = convertView.findViewById(R.id.ll_swo_row);
                holder.tv_SWO_AWO_Title = convertView.findViewById(R.id.tv_SWO_AWO_Title);
               /* if (userRole.equals(Utility.USER_ROLE_APC) ||
                        userRole.equals(Utility.USER_ROLE_ARTIST)) {
                    holder.ll_swo_row.setVisibility(View.GONE);// Hide SWO ROW
                } else {
                    holder.ll_swo_row.setVisibility(View.VISIBLE);// SHOW SWO ROW
                }*/
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.serial_no.setText(String.valueOf(position + 1));
            holder.tv_company.setText(list_PausedJobs.get(position).getComp());
            holder.tv_SWO_AWO_Name.setText(list_PausedJobs.get(position).getSwo_name());
            holder.tv_job_name.setText(list_PausedJobs.get(position).getJobName());
            holder.tv_job_desc.setText(list_PausedJobs.get(position).getTxt_Jdes());
            if(list_PausedJobs.get(position).getTypeof_AWO_SWO().equals("AWO")){
                holder.tv_SWO_AWO_Title.setText("AWO");
            }else {
                holder.tv_SWO_AWO_Title.setText("SWO");
            }


            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof MainActivity) {
                        if(list_PausedJobs.get(position).getTypeof_AWO_SWO().equals("AWO")){
                            Shared_Preference.set_EnterTimesheetByAWO(context, true);
                        }else {
                            Shared_Preference.set_EnterTimesheetByAWO(context, false);
                        }

                        String Comp_ID = list_PausedJobs.get(position).getClient_ID();
                        final String swo_id = list_PausedJobs.get(position).getSwo_id();
                        String job_id = list_PausedJobs.get(position).getJobID();
                        String jobName_forPaused = list_PausedJobs.get(position).getJobName();
                        String strPausedTimesheetID = list_PausedJobs.get(position).getRandom_no();
                        String Comp_Name=list_PausedJobs.get(position).getComp();

                        ((MainActivity)context).PrepareDataForPausedJob(context,Comp_ID,Comp_Name,swo_id,job_id,jobName_forPaused,strPausedTimesheetID);

                    }
                }
            });


        } catch (Exception e) {
            e.getMessage();
        }
        return convertView;
    }

    final public class ViewHolder {

        private TextView tv_company;
        private TextView tv_SWO_AWO_Name;
        private TextView tv_job_name;
        private TextView tv_job_desc;
        private Button serial_no;
        private CardView card_view;
        private LinearLayout ll_swo_row;
        private TextView tv_SWO_AWO_Title;
    }


}
