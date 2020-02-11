package planet.info.skyline.tech.task_plan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.model.Attachment;
import planet.info.skyline.model.Technician;
import planet.info.skyline.tech.fullscreenview.FullscreenImageView;
import planet.info.skyline.tech.fullscreenview.FullscreenWebView;
import planet.info.skyline.util.Utility;


public class TechnicianListActivity extends AppCompatActivity {


//    View rootView;
    String jobtxt_id, tab;
    TextView tv_msg;
    List<Technician> list_techs = new ArrayList<>();


    AlertDialog alertDialog;
    //SwipeRefreshLayout pullToRefresh;
    SharedPreferences sp;

    private RecyclerView tech_recyclerView;
   Button btn_Add,btn_Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_list);
        setTitle(Utility.getTitle("Technician(s)"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setView();
        setTempData();

    }

    private void setView() {

        tech_recyclerView = (RecyclerView)findViewById(R.id.tech_recyclerView);
        tech_recyclerView.setLayoutManager(new LinearLayoutManager(  this, LinearLayoutManager.VERTICAL, false));
        tech_recyclerView.setItemAnimator(new DefaultItemAnimator());

        tv_msg = (TextView)findViewById(R.id.tv_msg);
        btn_Add = findViewById(R.id.btn_Add);
        btn_Cancel = findViewById(R.id.btn_Cancel);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           // jobtxt_id = bundle.getString("jobtxt_id", "");
           // tab = bundle.getString("tab", "");
        }
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
onBackPressed();
            }
        });
    }


    private void setTempData() {
        list_techs.add(new Technician("tech tech(Service Tech)", "28", false));
        list_techs.add(new Technician("test test(Service Tech)", "23", false));
        list_techs.add(new Technician("test1 test1(Service Tech)", "24", false));
        JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(  this, list_techs);
        tech_recyclerView.setAdapter(jobFilesAdapter);


    }



    class get_jobFiles_Acyntask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDoalog;

        @Override
        protected Void doInBackground(Void... params) {

            // fettch_job_list();
            // fettch_job_list_New();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDoalog = new ProgressDialog(  TechnicianListActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                progressDoalog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }


            ArrayList<Technician> JobfilesList = new ArrayList<>();


            if (JobfilesList.size() < 1) {
                tv_msg.setVisibility(View.VISIBLE);
            } else {
                tv_msg.setVisibility(View.GONE);
            }
            JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(  TechnicianListActivity.this, JobfilesList);
            tech_recyclerView.setAdapter(jobFilesAdapter);


          /*  if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }*/
        }
    }

    public class JobFilesAdapter extends RecyclerView.Adapter<JobFilesAdapter.MyViewHolder> {

        List<Technician> list_techss;
        Context context;


        public JobFilesAdapter(Context context, List<Technician> Attachments) {
            this.context = context;
            this.list_techss = Attachments;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_tech, parent, false);


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int i) {


            final String Name = list_techss.get(i).getName();
          //  final String fileURL = list_techss.get(i).getFileURL();
           // final String date = list_techss.get(i).getDate();


            holder.index_no.setText(String.valueOf(i + 1));


            if (Name == null || Name.trim().equals("")) {
                holder.Name.setText("Not available");
            } else {
                holder.Name.setText(Name);

            }

            holder.row_jobFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

        }

        @Override
        public int getItemCount() {
            return list_techss.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView Name;//, Date;

            Button index_no;
            LinearLayout row_jobFile;

            public MyViewHolder(View convertview) {
                super(convertview);

                index_no = (Button) convertview.findViewById(R.id.serial_no);
                Name = (TextView) convertview.findViewById(R.id.Name);
             //   Date = (TextView) convertview.findViewById(R.id.Date);

                row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);

            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

}
