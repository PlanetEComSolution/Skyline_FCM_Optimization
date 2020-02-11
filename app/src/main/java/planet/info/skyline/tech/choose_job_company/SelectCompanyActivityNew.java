package planet.info.skyline.tech.choose_job_company;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.RequestControler.MyAsyncTask;
import planet.info.skyline.RequestControler.ResponseInterface;
import planet.info.skyline.adapter.AllJobsAdapter;
import planet.info.skyline.adapter.CompanyNameAdapterNew2;
import planet.info.skyline.adapter.JobNameAdapterNew2;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.All_Jobs;
import planet.info.skyline.model.Awo;
import planet.info.skyline.model.Company;
import planet.info.skyline.model.Job_2;
import planet.info.skyline.model.SWO_Details;
import planet.info.skyline.network.Api;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.tech.swo.SwoListActivity;
import planet.info.skyline.tech.task_plan.MyTasksActivity;
import planet.info.skyline.util.Utility;

public class SelectCompanyActivityNew extends AppCompatActivity implements ResponseInterface, View.OnClickListener {
    String userRole = "";
    String Swo_Id = "";


    //search by All job
    ArrayList<All_Jobs> list_AllJobs = new ArrayList<>();
    //  fetch company // search by company -job
    ArrayList<Company> list_company = new ArrayList<>();

    CompanyNameAdapterNew2 companyNameAdapter;
    // fetch jobs //search by company-job
    ArrayList<Job_2> list_Jobs = new ArrayList<>();

    ArrayList<SWO_Details> list_SWO = new ArrayList<>();
    ArrayList<Awo> list_Awo = new ArrayList<>();

    Dialog dialog_companyName, dialog_ScanSWO;

    AutoCompleteTextView txtvw_JOB_NAME_SearchByCompany, txtvw_COMP_NAME_SearchByCompany;
    AutoCompleteTextView txtvw_JOB_NAME_SearchByJob;
    AlertDialog alertDialog;

    String Is_Job_Mandatory = "";
    boolean showDialog_SHOW_INFO = false;
    boolean STARTING_BILLABLE_JOB = false;

    Context context;
    boolean IsMySwo = false;
    //boolean Scan_QR_CODE = false;
    boolean Search_by_Job = false;
    boolean SWO_AWO_ID_AVAILABLE = false;

    Button Btn_SearchByJob, Btn_SearchByCompany, scan_swo, scan_awo, Btn_MySwo, Btn_UnassignedSwo, Btn_MyTask;
    LinearLayout ll_MySWO;
    String CompId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        setTitle(Utility.getTitle("Choose Company/Job"));
        context = SelectCompanyActivityNew.this;


        userRole = Shared_Preference.getUSER_ROLE(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Btn_SearchByJob = findViewById(R.id.Btn_SearchByJob);
        Btn_SearchByCompany = findViewById(R.id.Btn_SearchByCompany);
        scan_swo = findViewById(R.id.scan_swo);
        scan_awo = findViewById(R.id.scan_awo);
        Btn_MySwo = findViewById(R.id.Btn_MySwo);
        Btn_UnassignedSwo = findViewById(R.id.Btn_UnassignedSwo);
        Btn_MyTask = findViewById(R.id.Btn_MyTask);
        ll_MySWO = findViewById(R.id.ll_MySWO);


        ll_MySWO.setVisibility(View.GONE);
        Btn_MyTask.setVisibility(View.VISIBLE);

        getIntentData();
        setVisibility();
        setListener();


    }

    public void onCompanySelected1(Company company) {
        txtvw_COMP_NAME_SearchByCompany.setText(company.getEname());
        txtvw_COMP_NAME_SearchByCompany.clearFocus();
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            FetchJobsByCompanyID(company.getId());
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    public void onJobSelected1(Job_2 job) {

        txtvw_JOB_NAME_SearchByCompany.setText(job.getJobName());
        txtvw_JOB_NAME_SearchByCompany.clearFocus();
        if (STARTING_BILLABLE_JOB) {
            if (new ConnectionDetector(context).isConnectingToInternet()) {
                if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                    FetchAWOsByJobID(job.getJOB_ID_PK());
                } else {
                    FetchSWOsByJobName(job.getJobName());
                }
            } else {
                Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onJobSelectedFromAllJobs(All_Jobs job) {
        txtvw_JOB_NAME_SearchByJob.setText(job.getTxt_job());
        txtvw_JOB_NAME_SearchByJob.clearFocus();

        if (STARTING_BILLABLE_JOB) {
            if (new ConnectionDetector(context).isConnectingToInternet()) {
                if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                    FetchAWOsByJobID(job.getJob_id_pk());
                } else {
                    FetchSWOsByJobName(job.getTxt_job());
                }
            } else {
                Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void getIntentData() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.containsKey(Utility.IS_JOB_MANDATORY)) {
                    Is_Job_Mandatory = bundle.getString(Utility.IS_JOB_MANDATORY, "0");
                }
                if (bundle.containsKey(Utility.Show_DIALOG_SHOW_INFO)) {
                    showDialog_SHOW_INFO = bundle.getBoolean(Utility.Show_DIALOG_SHOW_INFO, false);
                }
                if (bundle.containsKey(Utility.Show_SWO_LIST_OPTION)) {
                    boolean Show_SWO_LIST = bundle.getBoolean(Utility.Show_SWO_LIST_OPTION, false);
                    if (Show_SWO_LIST)
                        ll_MySWO.setVisibility(View.VISIBLE);
                    else
                        ll_MySWO.setVisibility(View.GONE);
                }
                if (bundle.containsKey(Utility.STARTING_BILLABLE_JOB)) {
                    STARTING_BILLABLE_JOB = bundle.getBoolean(Utility.STARTING_BILLABLE_JOB, false);

                }

            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setVisibility() {

        if (STARTING_BILLABLE_JOB) {
            if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                scan_swo.setVisibility(View.GONE);
                scan_awo.setVisibility(View.VISIBLE);
            } else {
                scan_swo.setVisibility(View.VISIBLE);
                scan_awo.setVisibility(View.GONE);
            }
        } else {
            scan_swo.setVisibility(View.VISIBLE);
            scan_awo.setVisibility(View.VISIBLE);
        }


        if (Shared_Preference.get_EnterTimesheetByAWO(context)) {

            Btn_MySwo.setText("My AWOs");
            Btn_UnassignedSwo.setText("Unassigned AWOs");

        } else {

            Btn_MySwo.setText("My SWOs");
            Btn_UnassignedSwo.setText("Unassigned SWOs");
        }


    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.Btn_SearchByJob:

                Shared_Preference.set_JobStartedFromMyTask(context,false);

                Search_by_Job = true;
                SWO_AWO_ID_AVAILABLE = false;
                if (new ConnectionDetector(context).isConnectingToInternet()) {
                    FetchAllJobsByDealerID();
                } else {
                    Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.Btn_SearchByCompany:

                Shared_Preference.set_JobStartedFromMyTask(context,false);

                Search_by_Job = false;
                SWO_AWO_ID_AVAILABLE = false;
                FetchCompanyNames();

                break;
            case R.id.scan_swo:

                Shared_Preference.set_JobStartedFromMyTask(context,false);

                SWO_AWO_ID_AVAILABLE = true;
                Shared_Preference.set_EnterTimesheetByAWO(context, false);
                scanqr();

                break;
            case R.id.scan_awo:

                Shared_Preference.set_JobStartedFromMyTask(context,false);

                SWO_AWO_ID_AVAILABLE = true;
                Shared_Preference.set_EnterTimesheetByAWO(context, true);
                scanqr();

                break;
            case R.id.Btn_MySwo:

                Shared_Preference.set_JobStartedFromMyTask(context,false);

                IsMySwo = true;
                SWO_AWO_ID_AVAILABLE = true;
                goToSwoListActivity();

                break;
            case R.id.Btn_UnassignedSwo:

                Shared_Preference.set_JobStartedFromMyTask(context,false);

                IsMySwo = false;
                SWO_AWO_ID_AVAILABLE = true;
                goToSwoListActivity();

                break;
            case R.id.Btn_MyTask:

                Shared_Preference.set_JobStartedFromMyTask(context,true);

                SWO_AWO_ID_AVAILABLE = true;
                Intent i = new Intent(context, MyTasksActivity.class);
                startActivityForResult(i, Utility.MY_TASKS_LIST_REQUEST_CODE);

                break;



        }
    }

    private void setListener() {
        Btn_SearchByJob.setOnClickListener(this);
        Btn_SearchByCompany.setOnClickListener(this);
        scan_swo.setOnClickListener(this);
        scan_awo.setOnClickListener(this);
        Btn_MySwo.setOnClickListener(this);
        Btn_UnassignedSwo.setOnClickListener(this);
        Btn_MyTask.setOnClickListener(this);



   /*     Btn_SearchByCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_by_Job = false;
                SWO_AWO_ID_AVAILABLE = false;
                FetchCompanyNames();

            }
        });*/

      /*  Btn_SearchByJob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Search_by_Job = true;
                SWO_AWO_ID_AVAILABLE = false;
                if (new ConnectionDetector(context).isConnectingToInternet()) {
                    FetchAllJobsByDealerID();
                } else {
                    Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }

            }
        });*/

      /*  scan_swo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SWO_AWO_ID_AVAILABLE = true;
                Shared_Preference.set_EnterTimesheetByAWO(context, false);
                scanqr();
            }
        });*/

        /*scan_awo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SWO_AWO_ID_AVAILABLE = true;
                Shared_Preference.set_EnterTimesheetByAWO(context, true);
                scanqr();
            }
        });*/

      /*  Btn_MySwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsMySwo = true;
                SWO_AWO_ID_AVAILABLE = true;
                goToSwoListActivity();
            }
        });*/

       /* Btn_UnassignedSwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsMySwo = false;
                SWO_AWO_ID_AVAILABLE = true;
                goToSwoListActivity();
            }
        });*/
       /* Btn_MyTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SWO_AWO_ID_AVAILABLE = true;
                Intent i = new Intent(context, MyTasksActivity.class);
                startActivityForResult(i, Utility.MY_TASKS_LIST_REQUEST_CODE);

            }
        });*/
    }

    private void goToSwoListActivity() {
        Intent i = new Intent(context, SwoListActivity.class);
        i.putExtra("MySwo", IsMySwo);
        startActivityForResult(i, Utility.SWO_LIST_REQUEST_CODE);

    }

    private void FetchCompanyNames() {
        JSONObject jsonObject = new JSONObject();
        try {
            String dealerId = Shared_Preference.getDEALER_ID(this);
            jsonObject.put("dealerID", dealerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_bindClientByDealer, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }

    }

    private void FetchJobsByCompanyID(String Client_ID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ClientID", Client_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_BindJob, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }

    }

    private void FetchAllJobsByDealerID() {
        String dealerId = Shared_Preference.getDEALER_ID(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DealerId", dealerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_GetallJobByDealerID, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }

    }

    private void FetchAWOsByJobID(String JobId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Job", JobId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_GetAwoDetailByJob, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }

    }

    private void FetchSWOsByJobName(String Job) {
        String dealerId = Shared_Preference.getDEALER_ID(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("job", Job);
            jsonObject.put("DealerID", dealerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_GetSwoByJObDealer, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }

    }

    private void FetchJobDetailBy_SWO_AWO_Id(String awo_swo_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AWO_SWO", awo_swo_id);
            if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                jsonObject.put("type", Utility.TYPE_AWO);
            } else {
                jsonObject.put("type", Utility.TYPE_SWO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_GetJobDetailsBy_SWO_AWO, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }

    }

    private void FetchJobDetailBy_JobName(String Job) {
        String dealerId = Shared_Preference.getDEALER_ID(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("job", Job);
            jsonObject.put("dealerid", dealerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_GetAllDetailbyJobtext_New, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }

    }

    public void dialog_SearchByCompany() {
        dialog_companyName = new Dialog(context);
        dialog_companyName.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_companyName.setContentView(R.layout.test_new);
        dialog_companyName.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_companyName.setCancelable(true);
        dialog_companyName.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        try {
            dialog_companyName.show();
        } catch (Exception e) {
            e.getMessage();
        }
        ImageView closebtn = (ImageView) dialog_companyName.findViewById(R.id.close);
        txtvw_COMP_NAME_SearchByCompany = (AutoCompleteTextView) dialog_companyName.findViewById(R.id.company);
        txtvw_JOB_NAME_SearchByCompany = (AutoCompleteTextView) dialog_companyName.findViewById(R.id.job);
        TextView txt_optional = (TextView) dialog_companyName.findViewById(R.id.txt_optional);

        if (Is_Job_Mandatory.equals("1")) {
            txt_optional.setVisibility(View.INVISIBLE);
        } else {
            txt_optional.setVisibility(View.VISIBLE);
        }

        Button btn_GO = (Button) dialog_companyName.findViewById(R.id.go_button);

        companyNameAdapter = new CompanyNameAdapterNew2(context,
                android.R.layout.simple_list_item_1, list_company);
        txtvw_COMP_NAME_SearchByCompany.setAdapter(companyNameAdapter);

        txtvw_COMP_NAME_SearchByCompany.setDropDownHeight(550);

        txtvw_COMP_NAME_SearchByCompany.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                txtvw_COMP_NAME_SearchByCompany.showDropDown();

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                try {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (txtvw_COMP_NAME_SearchByCompany.getRight() - txtvw_COMP_NAME_SearchByCompany.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            txtvw_COMP_NAME_SearchByCompany.setText("");
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
                /**/

                return false;
            }
        });

        //ontouch for job name---->
        txtvw_JOB_NAME_SearchByCompany.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                txtvw_JOB_NAME_SearchByCompany.showDropDown();
                /*to clear autocomplete*/
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                try {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (txtvw_JOB_NAME_SearchByCompany.getRight() - txtvw_JOB_NAME_SearchByCompany.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            txtvw_JOB_NAME_SearchByCompany.setText("");
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
                /**/

                return false;
            }
        });


        txtvw_COMP_NAME_SearchByCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtvw_COMP_NAME_SearchByCompany.getText().length() == 0) {
                    txtvw_JOB_NAME_SearchByCompany.setText("");
                    txtvw_COMP_NAME_SearchByCompany.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


                } else {
                    txtvw_COMP_NAME_SearchByCompany.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);


                }
            }
        });

        txtvw_JOB_NAME_SearchByCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (txtvw_JOB_NAME_SearchByCompany.getText().length() == 0) {
                        txtvw_JOB_NAME_SearchByCompany.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    } else {
                        txtvw_JOB_NAME_SearchByCompany.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }


        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog_companyName.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });
        btn_GO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utility.hideKeyboard((Activity) context);
                String str_CompanyName = txtvw_COMP_NAME_SearchByCompany.getText().toString().trim();
                String str_JobName = txtvw_JOB_NAME_SearchByCompany.getText().toString().trim();

                String CompanyId = "";
                String JObId = "";

                if (str_CompanyName.equals("")) {
                    Toast.makeText(context, "Please select Company!", Toast.LENGTH_SHORT).show();
                    return;
                }

                CompanyId = getCompanyID(str_CompanyName);
                JObId = getJobID(str_JobName);
                if (CompanyId.equals("")) {
                    Toast.makeText(context, "Please enter correct Company Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Is_Job_Mandatory.equals("1") && str_JobName.equals("")) {
                    Toast.makeText(context, "Please select Job!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (Is_Job_Mandatory.equals("1") && JObId.equals("")) {
                    Toast.makeText(context, "Please enter correct Job Name!", Toast.LENGTH_SHORT).show();
                    return;
                }


                dialog_companyName.dismiss();
                if (showDialog_SHOW_INFO) {
                    CompId = CompanyId;

                    FetchJobDetailBy_JobName(str_JobName);
                } else {
                    SendDataToCallingActivity(CompanyId, JObId, str_CompanyName, str_JobName);
                }


            }
        });


    }

    public void scanqr() {
        Utility.scanqr(SelectCompanyActivityNew.this, 1);

    }

    public void dialog_SearchByJob() {
        dialog_ScanSWO = new Dialog(context);
        dialog_ScanSWO.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_ScanSWO.setContentView(R.layout.diloge_for_billable_only_new);
        dialog_ScanSWO.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_ScanSWO.setCancelable(true);
        try {
            dialog_ScanSWO.show();
        } catch (Exception e) {
            e.getMessage();
        }
        ImageView closebtn = (ImageView) dialog_ScanSWO.findViewById(R.id.close);


        txtvw_JOB_NAME_SearchByJob = (AutoCompleteTextView) dialog_ScanSWO.findViewById(R.id.jobtext);
        Button btn_GO = (Button) dialog_ScanSWO.findViewById(R.id.Btn_Yes);


        txtvw_JOB_NAME_SearchByJob.setText("");
        AllJobsAdapter jobDescAdapter = new AllJobsAdapter(context, android.R.layout.simple_list_item_1, list_AllJobs);
        txtvw_JOB_NAME_SearchByJob.setAdapter(jobDescAdapter);
        txtvw_JOB_NAME_SearchByJob.setDropDownHeight(550);

        txtvw_JOB_NAME_SearchByJob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                txtvw_JOB_NAME_SearchByJob.showDropDown();
                /*to clear autocomplete*/
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                try {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (txtvw_JOB_NAME_SearchByJob.getRight() - txtvw_JOB_NAME_SearchByJob.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            txtvw_JOB_NAME_SearchByJob.setText("");
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
                /**/

                return false;
            }
        });


        txtvw_JOB_NAME_SearchByJob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtvw_JOB_NAME_SearchByJob.getText().length() == 0) {

                    txtvw_JOB_NAME_SearchByJob.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    txtvw_JOB_NAME_SearchByJob.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                }
            }
        });

        closebtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog_ScanSWO.dismiss();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                });

        btn_GO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard((Activity) context);
                String str_CompanyName = "";
                String str_JobName = txtvw_JOB_NAME_SearchByJob.getText().toString();
                String CompanyId = "";
                String JObId = "";

                if (str_JobName.length() == 0) {
                    Toast.makeText(context, "Please select job!", Toast.LENGTH_SHORT).show();
                    return;
                }

                All_Jobs job = getJobIDFromAllJobs(str_JobName);
                if (job == null) {
                    Toast.makeText(context, "Please enter correct job!", Toast.LENGTH_SHORT).show();
                } else {
                    dialog_ScanSWO.dismiss();
                    CompanyId = job.getCompID();
                    str_JobName = job.getTxt_job();
                    JObId = job.getJob_id_pk();
                    str_CompanyName = job.getCompany();

                    if (showDialog_SHOW_INFO) {
                        CompId = CompanyId;
                        FetchJobDetailBy_JobName(str_JobName);
                    } else {
                        SendDataToCallingActivity(CompanyId, JObId, str_CompanyName, str_JobName);
                    }
                }

            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = intent.getStringExtra("SCAN_RESULT");
                Swo_Id = "";
                String clientid = "";
                if (result.contains(",")) {
                    try {
                        String[] jj = result.split(",");
                        Swo_Id = jj[0];
                        clientid = jj[1];
                        // dealerid = jj[2];
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "SWO error!", Toast.LENGTH_SHORT).show();
                    }
                    if (clientid.equalsIgnoreCase("")) {
                        if (Shared_Preference.get_EnterTimesheetByAWO(this)) {
                            Toast.makeText(getApplicationContext(), "Please scan valid QR Code of AWO!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please scan valid QR Code of SWO!", Toast.LENGTH_SHORT).show();
                        }

                        return;
                    }
                    FetchJobDetailBy_SWO_AWO_Id(Swo_Id);
                } else {
                    if (Shared_Preference.get_EnterTimesheetByAWO(this)) {
                        Toast.makeText(getApplicationContext(), "Please scan valid QR Code of AWO!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please scan valid QR Code of SWO!", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            } else if (resultCode == RESULT_CANCELED) {
            }
        } else if (requestCode == Utility.SWO_LIST_REQUEST_CODE && resultCode != RESULT_CANCELED) {
            Swo_Id = intent.getStringExtra("Swo_id");
            FetchJobDetailBy_SWO_AWO_Id(Swo_Id);

        } else if (requestCode == Utility.MY_TASKS_LIST_REQUEST_CODE && resultCode != RESULT_CANCELED) {
            // setResult(requestCode, intent);
            //  finish();
            Swo_Id = intent.getStringExtra("Swo_id");
            FetchJobDetailBy_SWO_AWO_Id(Swo_Id);
        }
        //nks


    }

    public void dialog_NO_JObs_Available() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
        dialogView.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.getCause();
        }

        final TextView title = dialogView.findViewById(R.id.textView1rr);
        final TextView message = dialogView.findViewById(R.id.texrtdesc);

        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        close.setVisibility(View.INVISIBLE);
        // dialogBuilder.setTitle("Device Details");
        title.setText("No jobs available!");
        message.setText("Kindly choose another Company!");
        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });


    }

    private void SendDataToCallingActivity(String CompanyId, String JObId, String CompanyName, String JobName) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("CompID", CompanyId);
        returnIntent.putExtra("JobID", JObId);
        returnIntent.putExtra("CompName", CompanyName);
        returnIntent.putExtra("JobName", JobName);

        if (SWO_AWO_ID_AVAILABLE) {
            returnIntent.putExtra(Utility.SWO_AWO_ID_AVAILABLE, true);
            returnIntent.putExtra("Swo_Id", Swo_Id);

        } else {
            returnIntent.putExtra(Utility.SWO_AWO_ID_AVAILABLE, false);

            Bundle bundle = new Bundle();
            if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                bundle.putSerializable("Awo", list_Awo);
            } else {
                bundle.putSerializable("Swo", list_SWO);
            }
            returnIntent.putExtra("BUNDLE", bundle);

        }

        setResult(Activity.RESULT_OK, returnIntent);

        finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void dialog_SHOW_Info(final String CompanyID, final String JobID, final String CompanyName, final String JobName, final String JobType, final String JobDesc, final String Status, final String ShowName, final String AWO_SWO_Name) {

        final Dialog dialog_JobShowDetails = new Dialog(context);
        dialog_JobShowDetails.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_JobShowDetails.setContentView(R.layout.test2_new);
        dialog_JobShowDetails.show();
        Utility.dialog_setAttribute(this, dialog_JobShowDetails);

        final TextView tv_Company = (TextView) dialog_JobShowDetails.findViewById(R.id.tv_company);
        final TextView t1 = (TextView) dialog_JobShowDetails.findViewById(R.id.t1);
        final TextView t2 = (TextView) dialog_JobShowDetails.findViewById(R.id.t2);
        final TextView t3 = (TextView) dialog_JobShowDetails.findViewById(R.id.t3);
        final TextView t4 = (TextView) dialog_JobShowDetails.findViewById(R.id.t4);
        final TextView t5 = (TextView) dialog_JobShowDetails.findViewById(R.id.t5);

        final LinearLayout ll_swo_awo_info = dialog_JobShowDetails.findViewById(R.id.ll_swo_awo_info);
        final TextView tv_title_awo_swo = (TextView) dialog_JobShowDetails.findViewById(R.id.tv_title_awo_swo);
        final TextView tv_awo_swo = (TextView) dialog_JobShowDetails.findViewById(R.id.tv_awo_swo);

        final Button cancle = (Button) dialog_JobShowDetails.findViewById(R.id.cancle);
        final Button btn_GO = (Button) dialog_JobShowDetails.findViewById(R.id.proced);
        tv_Company.setText(CompanyName);
        t1.setText(JobName);
        t2.setText(JobType);
        t3.setText(Status);
        t4.setText(ShowName);
        t5.setText(JobDesc);
        if (SWO_AWO_ID_AVAILABLE) {
            ll_swo_awo_info.setVisibility(View.VISIBLE);
        } else {
            ll_swo_awo_info.setVisibility(View.GONE);
        }
        if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
            tv_title_awo_swo.setText("AWO #");
        } else {
            tv_title_awo_swo.setText("SWO #");
        }
        tv_awo_swo.setText(AWO_SWO_Name);

        btn_GO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_JobShowDetails.dismiss();
                SendDataToCallingActivity(CompanyID, JobID, CompanyName, JobName);
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_JobShowDetails.dismiss();
            }
        });

    }

    public void validation_dialog(String msg) {
        final Dialog showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.labourcode_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        showd.setCancelable(false);
        TextView textView1rr = (TextView) showd.findViewById(R.id.textView1rr);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView yesforloc = (TextView) showd.findViewById(R.id.Btn_Yes);
        texrtdesc.setText(msg);
        textView1rr.setVisibility(View.GONE);

        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        yesforloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


            }
        });


    }

    private String getCompanyID(String CompanyName) {
        String CompanyID = "";
        for (Company company : list_company) {
            if (CompanyName.equalsIgnoreCase(company.getEname())) {
                CompanyID = company.getId();
                break;
            }
        }
        return CompanyID;
    }

    private String getJobID(String JobName) {
        String JobID = "";
        for (Job_2 job : list_Jobs) {
            if (JobName.equalsIgnoreCase(job.getJobName())) {
                JobID = job.getJOB_ID_PK();
                break;
            }
        }
        return JobID;
    }

    private All_Jobs getJobIDFromAllJobs(String JobName) {
        All_Jobs job_ = null;
        for (All_Jobs job : list_AllJobs) {
            if (JobName.equalsIgnoreCase(job.getTxt_job())) {
                job_ = job;
                break;
            }
        }
        return job_;
    }

    @Override
    public void handleResponse(String responseString, String api) {


        if (api.equalsIgnoreCase(Api.API_bindClientByDealer)) {
            list_company.clear();
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String comapny_id = jsonObject1.getString("id");
                    String company_name = jsonObject1.getString("Ename");
                    String company_logo = jsonObject1.getString("Imagepath");
                    list_company.add(new Company(comapny_id, company_name, company_logo));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list_company != null && list_company.size() > 0) {
                dialog_SearchByCompany();
            } else {
                Toast.makeText(context, "No data found!", Toast.LENGTH_LONG).show();
            }
        } else if (api.equalsIgnoreCase(Api.API_BindJob)) {
            list_Jobs.clear();
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String job_id = jsonObject1.getString("JOB_ID_PK");
                    String jobName = jsonObject1.getString("JobName");
                    String job_descripition = jsonObject1.getString("txt_Job");
                    String status = jsonObject1.getString("Status");
                    String show = jsonObject1.getString("ShowName");
                    String jobtype = jsonObject1.getString("JOB_TYPE");

                    list_Jobs.add(new Job_2(job_id, job_descripition, show, jobName, jobtype, status));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list_Jobs.size() < 1) {
                txtvw_JOB_NAME_SearchByCompany.setText("");
                if (Is_Job_Mandatory.equals("1")) {
                    txtvw_COMP_NAME_SearchByCompany.setText("");
                    dialog_NO_JObs_Available();
                }
            } else {
                txtvw_JOB_NAME_SearchByCompany.setText("");
                JobNameAdapterNew2 jobDescAdapter = new JobNameAdapterNew2(context, android.R.layout.simple_list_item_1, list_Jobs);
                txtvw_JOB_NAME_SearchByCompany.setAdapter(jobDescAdapter);
                txtvw_JOB_NAME_SearchByCompany.setDropDownHeight(550);
            }
        } else if (api.equalsIgnoreCase(Api.API_GetallJobByDealerID)) {
            list_AllJobs.clear();
            try {
                JSONObject jsonObject1 = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject1.getJSONArray("cds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String jobName = jsonObject.getString("txt_job");
                    String JobId = jsonObject.getString("job_id_pk");
                    String job_descripition = jsonObject.getString("JOB_DESC").trim();
                    String compID = jsonObject.getString("compID");
                    String companyName = jsonObject.getString("company");
                    list_AllJobs.add(new All_Jobs(JobId, jobName, job_descripition, compID, companyName));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog_SearchByJob();
        } else if (api.equalsIgnoreCase(Api.API_GetAwoDetailByJob)) {
            list_Awo.clear();
            try {
                JSONArray jsonArray = new JSONArray(responseString);
                for (int k = 0; k < (jsonArray.length()); k++) {
                    JSONObject json_obj = jsonArray.getJSONObject(k);
                    String ID_PK = json_obj.getString("ID_PK");
                    String JOB_ID = json_obj.getString("JOB_ID");
                    String swo_name = json_obj.getString("swo_name");
                    String tech_id = "";
                    String SWO_Status_new = json_obj.getString("SWO_Status_new");
                    list_Awo.add(new Awo(ID_PK, JOB_ID, swo_name, tech_id, SWO_Status_new));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list_Awo.size() < 1) {
                if (Search_by_Job) txtvw_JOB_NAME_SearchByJob.setText("");
                else txtvw_JOB_NAME_SearchByCompany.setText("");
                String msg = "You cannot book time to a Job without a Art Work Order.";
                validation_dialog(msg);
            }
        } else if (api.equalsIgnoreCase(Api.API_GetSwoByJObDealer)) {
            list_SWO.clear();
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String swo_id = jsonObject1.getString("swo_id");
                    String JOB_ID = jsonObject1.getString("JOB_ID");
                    String JOB_DESC = jsonObject1.getString("JOB_DESC");
                    String COMP_ID = jsonObject1.getString("COMP_ID");
                    String SWO_NAME = jsonObject1.getString("swo_name");
                    String SWO_Status_new = jsonObject1.getString("SWO_Status_new");
                    String TXT_JOB = jsonObject1.getString("txt_job");
                    list_SWO.add(new SWO_Details(JOB_ID, JOB_DESC, COMP_ID, SWO_NAME, TXT_JOB, SWO_Status_new, swo_id, SWO_NAME));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list_SWO.size() < 1) {
                if (Search_by_Job) txtvw_JOB_NAME_SearchByJob.setText("");
                else txtvw_JOB_NAME_SearchByCompany.setText("");
                String msg = "You cannot book time to a Job without a Service Work Order.";
                validation_dialog(msg);
            }
        } else if (api.equalsIgnoreCase(Api.API_GetJobDetailsBy_SWO_AWO)) {
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                JSONObject js_obj = jsonArray.getJSONObject(0);
                if (js_obj == null || js_obj.length() == 0) {
                    if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                        Toast.makeText(getApplicationContext(), "Please scan a valid qr code of AWO!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please scan a valid qr code of SWO!", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } else {
                    String login_dealerId = Shared_Preference.getDEALER_ID(context);
                    String job_id = js_obj.getString("job_id");
                    String jobName = js_obj.getString("jobName");
                    String JOB_TYPE = js_obj.getString("JOB_TYPE");
                    String jobstatus = js_obj.getString("jobstatus");
                    String companyName = js_obj.getString("company");
                    String Show_Name = js_obj.getString("Show_Name");
                    String desciption = js_obj.getString("desciption");
                    String SWO_Status_new = js_obj.getString("SWO_Status_new");
                    String dealerID = js_obj.getString("dealerID");
                    String comp_id = js_obj.getString("compID");
                    String Archivedstatus = js_obj.getString("Archivedstatus");
                    String Swo_Awo_Name = "";
                    if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                        Swo_Awo_Name = js_obj.getString("AwoName");
                    } else {
                        Swo_Awo_Name = js_obj.getString("SwoName");
                    }


                    if (showDialog_SHOW_INFO) {
                        if (!dealerID.equals(login_dealerId)) {
                            Toast.makeText(getApplicationContext(), "Please scan valid QR Code!", Toast.LENGTH_SHORT).show();
                        } else if (Archivedstatus.equals("1")) {
                            Toast.makeText(getApplicationContext(), "Job has been archived. Please scan valid QR Code.", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog_SHOW_Info(comp_id, job_id, companyName, jobName, JOB_TYPE, desciption, jobstatus, Show_Name, Swo_Awo_Name);
                        }
                    } else {
                        SendDataToCallingActivity(comp_id, job_id, companyName, jobName);
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please scan valid QR Code!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (api.equalsIgnoreCase(Api.API_GetAllDetailbyJobtext_New)) {
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String txt_Job = jsonObject1.getString("txt_Job");
                    String ShowName = jsonObject1.getString("ShowName");
                    String JOB_TYPE = jsonObject1.getString("JOB_TYPE");
                    String JobName = jsonObject1.getString("JobName");
                    String Status = jsonObject1.getString("Status");
                    String compname = jsonObject1.getString("compname");
                    String JOB_ID_PK = jsonObject1.getString("JOB_ID_PK");
                    dialog_SHOW_Info(CompId, JOB_ID_PK, compname, JobName, JOB_TYPE, txt_Job, Status, ShowName, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
