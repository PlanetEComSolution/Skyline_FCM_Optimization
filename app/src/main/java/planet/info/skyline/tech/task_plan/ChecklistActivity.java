package planet.info.skyline.tech.task_plan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.adapter.MyTasksRecyclerAdapter;
import planet.info.skyline.model.Checklist;
import planet.info.skyline.model.MyTask;
import planet.info.skyline.model.SavChecklist;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.REST_API_Client_TaskPlan;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static planet.info.skyline.util.Utility.LOADING_TEXT;


public class ChecklistActivity extends AppCompatActivity {

Context context;
    TextView tv_msg;
    List<Checklist> list_Checklists = new ArrayList<>();
    Button btn_Add, btn_Cancel;
    String updatable = "";
    Integer request_code = 0;
    String job_id;
    String task_id;
    String id;
    private RecyclerView Checklist_recyclerView;
    ProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        context=ChecklistActivity.this;
        setTitle(Utility.getTitle("Checklist"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIntentData();
        setView();
        APIGetCheckList();


    }

    private void setView() {

        Checklist_recyclerView = (RecyclerView) findViewById(R.id.Checklist_recyclerView);
        Checklist_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Checklist_recyclerView.setItemAnimator(new DefaultItemAnimator());

        tv_msg = (TextView) findViewById(R.id.tv_msg);

        btn_Add = findViewById(R.id.btn_Add);
        btn_Cancel = findViewById(R.id.btn_Cancel);

        if (updatable.equals("1")) {
            btn_Add.setVisibility(View.VISIBLE);
        } else {
            btn_Add.setVisibility(View.GONE);
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

    private void getIntentData() {

        Bundle bundle = getIntent().getExtras();


        try {
            if (bundle != null) {
                if (bundle.containsKey("Updatable")) {
                    updatable = bundle.getString("Updatable");
                }
                if (bundle.containsKey("RequestCode")) {
                    request_code = bundle.getInt("RequestCode");
                }

                job_id = bundle.getString("job_id");
                task_id = bundle.getString("task_id");
                id = bundle.getString("id");


            }
        } catch (Exception e) {
            e.getMessage();
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

    private void APIGetCheckList() {
        showprogressdialog();
        String url = SOAP_API_Client.URL_EP2 + "/ep6/api/TaskPlanning/GetMasterCheckList?tid=" + task_id + "&Complexity=0&jobID=" + job_id + "&ttID=" + id;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              hideprogressdialog();
                list_Checklists.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String Col1 = jsonObject.getString("Col1");
                            String Col2 = jsonObject.getString("Col2");
                            String Col3 = jsonObject.getString("Col3");
                            String Col4 = jsonObject.getString("Col4");
                            String Col5 = jsonObject.getString("Col5");

                            list_Checklists.add(new Checklist(Col1, Col2, Col3, Col4, Col5));


                        }

                    }

                } catch (Exception e) {
                    e.getMessage();
                }

                CheckListAdapter jobFilesAdapter = new CheckListAdapter(ChecklistActivity.this);
                Checklist_recyclerView.setAdapter(jobFilesAdapter);


                if (list_Checklists.isEmpty()) {
                    tv_msg.setVisibility(View.VISIBLE);



                    Intent intent = new Intent();
                    if(getCheckedCount()== list_Checklists.size()){
                        intent.putExtra(Utility.IS_CHECKLIST_DONE, true);
                        setResult(request_code, intent);
                        finish();//finishing activity
                    }


                } else {
                    tv_msg.setVisibility(View.GONE);
                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideprogressdialog();
                Log.i("err", "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
    private void SaveChecklist(String icChecked, String id2,String title) {
        showprogressdialog();


        String swo_awo_id=Shared_Preference.getSWO_ID(context);
        if(swo_awo_id.equals("")){ swo_awo_id="0"; }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("job_id", job_id);
        hashMap.put("taskid", task_id);
        hashMap.put("Ischecked", icChecked);
        hashMap.put("id_pk", id2);
        hashMap.put("title", title);
        hashMap.put("EmpID", Shared_Preference.getLOGIN_USER_ID(context));
        hashMap.put("AWO_SWO_ID", swo_awo_id);


        API_Interface apiService = REST_API_Client_TaskPlan.getClient().create(API_Interface.class);
        apiService.SaveChecklist(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SavChecklist>>() {
                    @Override
                    public void onCompleted() {
                        hideprogressdialog();

                        APIGetCheckList();

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideprogressdialog();
                        Toast.makeText(context,
                                "Network Error....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<SavChecklist> checklists) {


                    }
                });


    }

    public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {

      //  List<Checklist> list_Checklists;
        Context context;


        public CheckListAdapter(Context context){//, List<Checklist> Checklists) {
            this.context = context;
          //  this.list_Checklists = Checklists;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_checklist_2, parent, false);


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int i) {


            final String Name = list_Checklists.get(i).getName();
            final String _ischecked = list_Checklists.get(i).getIsChecked();
            final String _id2 = list_Checklists.get(i).getId2();
            final String _title = list_Checklists.get(i).getName();

            boolean ischecked = false;
            if (_ischecked.equals("1")) {
                ischecked = true;
            } else {
                ischecked = false;
            }

            holder.index_no.setText(String.valueOf(i + 1));


            if (Name == null || Name.trim().equals("")) {
                holder.Name.setText("Not available");
            } else {
                holder.Name.setText(Name);

            }

            if (!updatable.equals("1")) {
                holder.chkbox.setEnabled(false);
            } else {
                holder.chkbox.setEnabled(true);
            }


            holder.chkbox.setChecked(ischecked);
            if (ischecked) {
               // holder.Name.setForeground(context.getResources().getDrawable(R.drawable.strike_through));

               // holder.Name.setEnabled(false);
                holder.Name.setBackgroundColor(Color.parseColor("#10000000"));

            } else {

              //  holder.Name.setForeground(null);
               // holder.Name.setEnabled(true);
                holder.Name.setBackgroundColor(Color.TRANSPARENT);
            }


            holder.chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                       // holder.Name.setForeground(context.getResources().getDrawable(R.drawable.strike_through));
                     //   list_Checklists.get(i).setIsChecked("1");

                        SaveChecklist("1",_id2,_title);
                    } else {
                     //   holder.Name.setForeground(null);
                       // list_Checklists.get(i).setIsChecked("0");

                        SaveChecklist("0",_id2,_title);
                    }

                   /*
                    Intent intent = new Intent();
                    if(getCheckedCount()== list_Checklists.size()){
                        intent.putExtra(Utility.IS_CHECKLIST_DONE, true);
                        setResult(request_code, intent);
                        finish();//finishing activity
                    }*/


                }
            });
            holder.row_jobFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

        }

        @Override
        public int getItemCount() {
            return list_Checklists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView Name;
            CheckBox chkbox;
            Button index_no;
            LinearLayout row_jobFile;

            public MyViewHolder(View convertview) {
                super(convertview);

                index_no = (Button) convertview.findViewById(R.id.serial_no);
                Name = (TextView) convertview.findViewById(R.id.Name);
                chkbox = convertview.findViewById(R.id.chkbox);

                row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);

            }
        }
    }


    private int getCheckedCount(){
        int total = 0;
        for (int i = 0; i < list_Checklists.size(); i++) {

            if (list_Checklists.get(i).getIsChecked().equals("1")) {
                total = total + 1;
            }
        }
       /* if (total == list_Checklists.size()) {
            Intent intent = new Intent();
            intent.putExtra(Utility.IS_CHECKLIST_DONE, true);
            setResult(request_code, intent);
            finish();//finishing activity
        }*/
        return total;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if(getCheckedCount()== list_Checklists.size()){
            intent.putExtra(Utility.IS_CHECKLIST_DONE, true);

        }else {
            intent.putExtra(Utility.IS_CHECKLIST_DONE, false);
        }
        setResult(request_code, intent);
        finish();//finishing activity
    }

    public void showprogressdialog() {
        try {
            mProgressHUD = ProgressHUD.show(context, LOADING_TEXT, false);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void hideprogressdialog() {
        try {
            if (mProgressHUD.isShowing()) {
                mProgressHUD.dismiss();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
