package planet.info.skyline.tech.swo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.adapter.MyDividerItemDecoration;
import planet.info.skyline.adapter.SWO_AWO_Adapter;
import planet.info.skyline.adapter.VendorsAdapter;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.MySwo;
import planet.info.skyline.network.Api;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivityNew;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.util.Utility.LOADING_TEXT;

public class SwoListActivity extends AppCompatActivity {
    TextView tv_msg;//, txtvw_count;
    String compID = "";
    String jobID = "";
    String company_Name = "";
    String job_Name = "";
    String swo_id = "";
    boolean IsMySwo = false;
    ArrayList<MySwo> mySwoArrayList = new ArrayList<>();
    //ListView listView;
    private Menu menu;
    Context context;
    ProgressHUD mProgressHUD;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=SwoListActivity.this;
        setContentView(R.layout.swo_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setView();
        getIntentData();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            IsMySwo = bundle.getBoolean("MySwo", false);
            if (IsMySwo) {  // My SWO/AWO
                if (Shared_Preference.get_EnterTimesheetByAWO(this)) {
                    setTitle(Utility.getTitle("My AWO(s)"));
                } else {
                    setTitle(Utility.getTitle("My SWO(s)"));
                }

            } else {  // Unassigned SWO/AWO
                if (Shared_Preference.get_EnterTimesheetByAWO(this)) {
                    setTitle(Utility.getTitle("Unassigned AWO(s"));
                } else {
                    setTitle(Utility.getTitle("Unassigned SWO(s)"));
                }

            }
            if (new ConnectionDetector(SwoListActivity.this).isConnectingToInternet()) {
                new async_getSWO_AWO_List().execute();
            } else {
                Toast.makeText(SwoListActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }

        }


    }

    private void setView() {
        tv_msg = findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL,   getResources().getInteger(R.integer.list_divider_margin_left)));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.choose:
                Intent i = new Intent(SwoListActivity.this, SelectCompanyActivityNew.class);
                i.putExtra(Utility.IS_JOB_MANDATORY, "0");
                startActivityForResult(i, Utility.CODE_SELECT_COMPANY);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usage_report, menu);
        // _menu = menu;
        this.menu = menu;
        return true;
    }

    private void updateMenuTitles() {

        try {
            MenuItem bedMenuItem = menu.findItem(R.id.choose);

            if (!company_Name.equals("")) {
                if (!job_Name.equals("")) {
                    bedMenuItem.setTitle(company_Name + "\n" + job_Name);
                } else {
                    bedMenuItem.setTitle(company_Name);
                }
            } else {
                bedMenuItem.setTitle(getApplicationContext().getResources().getString(R.string.Select_Job));
            }

        } catch (Exception e) {
            e.getCause();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utility.CODE_SELECT_COMPANY) {

            if (resultCode == Activity.RESULT_OK) {
                try {
                    compID = data.getStringExtra("CompID");
                    jobID = data.getStringExtra("JobID");
                    company_Name = data.getStringExtra("CompName");
                    job_Name = data.getStringExtra("JobName");
                    if (data.hasExtra("Swo_Id")) { // Means result is from scanning a awo qr / swo qr code
                        swo_id = data.getStringExtra("Swo_Id");
                    }

                    updateMenuTitles();
                    filterSWOList();

                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "Exception caught!", Toast.LENGTH_SHORT).show();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                finish();
            }
        }
    }

    public int getMySwoAwoList() {
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = SOAP_API_Client.BASE_URL;
        final String METHOD_NAME = Api.API_MY_SWO_AWO_by_Type;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;


        // Create SOAP request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = Shared_Preference.getDEALER_ID(this);
        String userID = Shared_Preference.getLOGIN_USER_ID(this);
        /* String Role = Shared_Preference.getUSER_ROLE(this);
        if(Shared_Preference.get_EnterTimesheetByAWO(SwoListActivity.this)){
            Role=Utility.USER_ROLE_ARTIST;
        }*/

        request.addProperty("user", userID);
        request.addProperty("DealerID", dealerId);
        if (Shared_Preference.get_EnterTimesheetByAWO(SwoListActivity.this)) {
            request.addProperty("Type", Utility.TYPE_AWO);
        } else {
            request.addProperty("Type", Utility.TYPE_SWO);
        }


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            JSONObject jsonObject = new JSONObject(receivedString);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            mySwoArrayList.clear();
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String JOB_ID = jsonObject1.getString("JOB_ID");
                    String JOB_DESC = jsonObject1.getString("JOB_DESC");
                    String CompanyName = jsonObject1.getString("CompanyName");

                    String COMP_ID = jsonObject1.getString("COMP_ID");
                    String txt_job = jsonObject1.getString("txt_job");
                    String SWO_Status_new = jsonObject1.getString("SWO_Status_new");
                    String swo_id = jsonObject1.getString("swo_id");
                    String name = jsonObject1.getString("name");
                    String TECH_ID1 = jsonObject1.getString("TECH_ID1");
                    String TECH_ID = jsonObject1.getString("TECH_ID");

                    mySwoArrayList.add(new MySwo(JOB_ID, JOB_DESC, COMP_ID, txt_job,
                            SWO_Status_new, swo_id, name, TECH_ID1, TECH_ID, CompanyName));

                }


            }

            // sort list alphabetically
           /* Collections.sort(mySwoArrayList, new Comparator<MySwo>() {
                @Override
                public int compare(MySwo u1, MySwo u2) {
                    return u1.getName().compareToIgnoreCase(u2.getName());
                }
            });*/


        } catch (Exception e) {
            e.getMessage();
        }
        return mySwoArrayList.size();

    }

    public int getUnassignedSwoAwo() {
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = SOAP_API_Client.BASE_URL;
        final String METHOD_NAME = Api.API_UNASSIGNED_SWO_AWO_by_Type;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = Shared_Preference.getDEALER_ID(this);

        request.addProperty("DealerID", dealerId);
        if (Shared_Preference.get_EnterTimesheetByAWO(SwoListActivity.this)) {
            request.addProperty("Type", Utility.TYPE_AWO);
        } else {
            request.addProperty("Type", Utility.TYPE_SWO);
        }


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            JSONObject jsonObject = new JSONObject(receivedString);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            mySwoArrayList.clear();
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String JOB_ID = jsonObject1.getString("JOB_ID");
                    String JOB_DESC = jsonObject1.getString("JOB_DESC");
                    String CompanyName = jsonObject1.getString("CompanyName");

                    String COMP_ID = jsonObject1.getString("COMP_ID");
                    String txt_job = jsonObject1.getString("txt_job");
                    String SWO_Status_new = jsonObject1.getString("SWO_Status_new");
                    String swo_id = jsonObject1.getString("swo_id");
                    String name = jsonObject1.getString("name");
                    String TECH_ID1 = jsonObject1.getString("TECH_ID1");
                    String TECH_ID = jsonObject1.getString("TECH_ID");

                    mySwoArrayList.add(new MySwo(JOB_ID, JOB_DESC, COMP_ID, txt_job,
                            SWO_Status_new, swo_id, name, TECH_ID1, TECH_ID, CompanyName));

                }
                // sort list alphabetically
               /* Collections.sort(mySwoArrayList, new Comparator<MySwo>() {
                    @Override
                    public int compare(MySwo u1, MySwo u2) {
                        return u1.getName().compareToIgnoreCase(u2.getName());
                    }
                });*/

            }


        } catch (Exception e) {
            e.getMessage();
        }
        return mySwoArrayList.size();

    }

    private void setListAdapter(final ArrayList<MySwo> arrayList) {
       SWO_AWO_Adapter mAdapter = new SWO_AWO_Adapter(this, arrayList);
        recyclerView.setAdapter(mAdapter);

    }

    private void filterSWOList() {
        if (mySwoArrayList != null && mySwoArrayList.size() > 0) {
            ArrayList<MySwo> filteredSWOList = new ArrayList<>();
            if (!swo_id.equals("")) { // only for swo/awo id
                for (MySwo mySwo : mySwoArrayList) {
                    if (mySwo.getSwo_id().equals(swo_id)) {
                        filteredSWOList.add(mySwo);
                    }
                }

            } else if (!compID.equals("") && jobID.equals("")) {  // only compId
                for (MySwo mySwo : mySwoArrayList) {
                    if (mySwo.getCOMP_ID().equals(compID)) {
                        filteredSWOList.add(mySwo);
                    }
                }
            } else if (compID.equals("") && !jobID.equals("")) {  // only jobId
                for (MySwo mySwo : mySwoArrayList) {
                    if (mySwo.getJOB_ID().equals(jobID)) {
                        filteredSWOList.add(mySwo);
                    }
                }
            } else if (!compID.equals("") && !jobID.equals("")) {  //  compId && jobID
                for (MySwo mySwo : mySwoArrayList) {
                    if (mySwo.getCOMP_ID().equals(compID) && mySwo.getJOB_ID().equals(jobID)) {
                        filteredSWOList.add(mySwo);
                    }
                }
            }

            if (filteredSWOList.size() == 0) {
                tv_msg.setText("No swo found for this Job/Company!");
                tv_msg.setVisibility(View.VISIBLE);
            } else {
                tv_msg.setVisibility(View.GONE);
            }

            setListAdapter(filteredSWOList);


        }
    }

    private class async_getSWO_AWO_List extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          showprogressdialog();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            hideprogressdialog();
            try {
                getSupportActionBar().setSubtitle(Utility.getTitle("Total : "+mySwoArrayList.size()));
            } catch (Exception e) {
            }
            if (mySwoArrayList.size() > 0) {
                tv_msg.setVisibility(View.GONE);
            } else {
                tv_msg.setVisibility(View.VISIBLE);
            }

            tv_msg.setText("No data Available!");

            setListAdapter(mySwoArrayList);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            int size = 0;
            if (IsMySwo) {

                size = getMySwoAwoList();
            } else {

                size = getUnassignedSwoAwo();


            }
            return size;
        }
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
