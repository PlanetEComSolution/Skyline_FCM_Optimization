package planet.info.skyline.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.model.MyTask;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.tech.task_plan.ChecklistActivity;
import planet.info.skyline.tech.task_plan.MyTasksActivity;
import planet.info.skyline.util.Utility;

public class MyTasksRecyclerAdapter extends RecyclerView.Adapter<MyTasksRecyclerAdapter.MyViewHolder> {

    List<MyTask> List_myTask;
    Context context;


    public MyTasksRecyclerAdapter(Context context, List<MyTask> myTasks) {
        this.context = context;
        this.List_myTask = myTasks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_task, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int i) {


        final String checklist = List_myTask.get(i).getSelectedchklst() + "/" + List_myTask.get(i).getTotalchklst();
        final String completedDate = List_myTask.get(i).getComletedDate1();
        final String deadlineDate = List_myTask.get(i).getDeadlineDate();
        final String description = String.valueOf(List_myTask.get(i).getDescription());
        final String startBy = List_myTask.get(i).getStartDate();
        final String task = List_myTask.get(i).getTask();
        final String timeBudget = List_myTask.get(i).getTimeBudget();
        final String technician = List_myTask.get(i).getAssignUser();
        final String completedBy = List_myTask.get(i).getCName();
        final String taskID = List_myTask.get(i).getDTaskID();
        final String ID = String.valueOf(List_myTask.get(i).getIDPK());
        final String AssignUser = List_myTask.get(i).getAssignUser();
        final String JobId = String.valueOf(List_myTask.get(i).getJobIDPK());
        final String SWOId = String.valueOf(List_myTask.get(i).getSWOID());

        final String JobName = List_myTask.get(i).getTxtJob();
        final String JobDesc = List_myTask.get(i).getTxtJdes();
        final String Client = String.valueOf(List_myTask.get(i).getTxtCName());
        final String ClientID = String.valueOf(List_myTask.get(i).getClientIDN());
        final String isAWO = String.valueOf(List_myTask.get(i).getIsAWO());
        final String LaborId = String.valueOf(List_myTask.get(i).getLalor_ID_PK());
        final String LaborName = String.valueOf(List_myTask.get(i).getLabor_name());

        holder.index_no.setText(String.valueOf(i + 1));
        if (Client == null || Client.trim().equals("")) {
            holder.Client.setText("Not available");
        } else {
            holder.Client.setText(Client);

        }
        if (JobName == null || JobName.trim().equals("")) {
            holder.JobName.setText("Not available");
        } else {
            holder.JobName.setText(JobName);
        }

        if (JobDesc == null || JobDesc.trim().equals("")) {
            holder.Job_desc.setText("Not available");
        } else {
            holder.Job_desc.setText(JobDesc);
        }
        if (task == null || task.trim().equals("")) {
            holder.Task.setText("Not available");
        } else {
            holder.Task.setText(Utility.TrimString(task, 25));
        }

        if (timeBudget == null || timeBudget.trim().equals("")) {
            holder.Hours.setText("Not available");
        } else {
            holder.Hours.setText(timeBudget);
        }

        if (startBy == null || startBy.trim().equals("")) {
            holder.Start_date.setText("Not available");
        } else {
            holder.Start_date.setText(Utility.ChangeDateFormat("yyyy-MM-dd", "dd MMM yyyy", startBy.trim()));
        }
        if (completedDate == null || completedDate.trim().equals("")) {
            holder.Due_date.setText("Not available");
        } else {
            holder.Due_date.setText(Utility.ChangeDateFormat("yyyy-MM-dd", "dd MMM yyyy", completedDate.trim()));
        }

        if (technician == null || technician.trim().equals("")) {
            holder.Secondary_Tech.setText("Not available");
        } else {
            holder.Secondary_Tech.setText(technician);
        }


        holder.parent_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared_Preference.set_Task_ID(context, taskID);
                Shared_Preference.set_Task_PrimaryID(context, ID);
                Shared_Preference.set_Task_LaborId(context, LaborId);

                if (SWOId.equals("") || SWOId.equals("null")) {
                    Toast.makeText(context, "Task does not have SWO/AWO.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (LaborId.equals("") || LaborId.equals("null")) {
                    Toast.makeText(context, "Task does not have any Labor Code.", Toast.LENGTH_SHORT).show();
                }

                SendDataToCallingActivity(ClientID, JobId, Client, JobName, SWOId);


            }
        });

    }

    private void SendDataToCallingActivity(String CompanyId, String JObId, String CompanyName, String JobName, String swo_awo_id) {
        try {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("Comp_id", CompanyId);
            returnIntent.putExtra("JOB_ID", JObId);
            returnIntent.putExtra("CompanyName", CompanyName);
            returnIntent.putExtra("JobName", JobName);
            returnIntent.putExtra("Swo_id", swo_awo_id);
            if (context instanceof MyTasksActivity) {
                ((Activity) context).setResult(Activity.RESULT_OK, returnIntent);
                ((Activity) context).finish();
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }


    private void StartChecklistActivity(String JobId, String task_id, String id) {
        try {
            Intent returnIntent = new Intent(context, ChecklistActivity.class);
            returnIntent.putExtra("job_id", JobId);
            returnIntent.putExtra("task_id", task_id);
            returnIntent.putExtra("id", id);
            returnIntent.putExtra("Updatable", "1");
            (context).startActivity(returnIntent);

        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public int getItemCount() {
        return List_myTask.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Task, Client, JobName, Job_desc, Hours, Secondary_Tech;
        TextView Start_date, Due_date;
        Button index_no;
        LinearLayout parent_row;
        View view1;


        public MyViewHolder(View convertview) {
            super(convertview);

            index_no = (Button) convertview.findViewById(R.id.serial_no);
            Task = (TextView) convertview.findViewById(R.id.Task);
            Client = (TextView) convertview.findViewById(R.id.Client);
            JobName = (TextView) convertview.findViewById(R.id.JobName);
            Job_desc = (TextView) convertview.findViewById(R.id.Job_desc);
            Hours = (TextView) convertview.findViewById(R.id.Hours);
            Secondary_Tech = (TextView) convertview.findViewById(R.id.Secondary_Tech);
            view1 = convertview.findViewById(R.id.view1);
            Start_date = (TextView) convertview.findViewById(R.id.Start_date);
            Due_date = (TextView) convertview.findViewById(R.id.Due_date);
            parent_row = convertview.findViewById(R.id.parent_row);

        }
    }
}

