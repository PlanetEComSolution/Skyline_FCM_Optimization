package planet.info.skyline.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.network.Api;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_ShowClientDashStatus;
import static planet.info.skyline.network.Api.API_ShowStatus;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;


public class DashboardActivity extends AppCompatActivity {

    TextView tv_Approved, tv_Snoozed, tv_Pending, tv_Rejected,
            tv_Client_Order_Paused, tv_Awaiting_Coordinator_Approval, tv_Approved_Orders;
    LinearLayout ll_Approved, ll_Snoozed, ll_Pending, ll_Rejected,
            ll_Client_Order_Paused, ll_Awaiting_Coordinator_Approval, ll_Approved_Orders;

    String Snoozed = "", Approved = "", YTBReviewed = "", Rejected = "";
    String ClientOrderPaused = "", AwaitingCoordinatorApproval = "", ApprovedOrder = "";

    String Client_id_Pk, comp_ID, jobID, job_Name, DealerId;

    ProgressDialog progressDoalog;


    private Menu menu;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_dashboard);
        tv_Approved = findViewById(R.id.tv_Approved);
        tv_Snoozed = findViewById(R.id.tv_Snoozed);
        tv_Pending = findViewById(R.id.tv_Pending);
        tv_Rejected = findViewById(R.id.tv_Rejected);
        tv_Client_Order_Paused = findViewById(R.id.tv_Client_Order_Paused);
        tv_Awaiting_Coordinator_Approval = findViewById(R.id.tv_Awaiting_Coordinator_Approval);
        tv_Approved_Orders = findViewById(R.id.tv_Approved_Orders);

        ll_Approved = findViewById(R.id.ll_Approved);
        ll_Snoozed = findViewById(R.id.ll_Snoozed);
        ll_Pending = findViewById(R.id.ll_Pending);
        ll_Rejected = findViewById(R.id.ll_Rejected);
        ll_Client_Order_Paused = findViewById(R.id.ll_Client_Order_Paused);
        ll_Awaiting_Coordinator_Approval = findViewById(R.id.ll_Awaiting_Coordinator_Approval);
        ll_Approved_Orders = findViewById(R.id.ll_Approved_Orders);

        progressDoalog = new ProgressDialog(DashboardActivity.this);
        progressDoalog.setMessage(getResources().getString(R.string.Loading_text));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCancelable(false);

        setTitle(Utility.getTitle("Power Dashboard"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Client_id_Pk = Shared_Preference.getCLIENT_LOGIN_userID(DashboardActivity.this);

        comp_ID =
                Shared_Preference.getCLIENT_LOGIN_CompID(DashboardActivity.this);

        DealerId =
                Shared_Preference.getCLIENT_LOGIN_DealerID(DashboardActivity.this);
        jobID = "-1"; //by default
        job_Name = getApplicationContext().getResources().getString(R.string.Select_Job);

        Log.e("comp_ID", comp_ID);
        Log.e("jobID", jobID);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiDashboardDataEp2();

            }
        });

        /**/
        callApiDashboardDataEp2();
        /**/
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void callApiDashboardDataEp2() {


        if (new ConnectionDetector(DashboardActivity.this).isConnectingToInternet()) {
            new async_getClientDashboardData().execute();

        } else {
            Toast.makeText(DashboardActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }


    }

    private void setTextData() {

        if (Approved != null && !Approved.equals("")) {
            tv_Approved.setText(Approved);
        } else {
            tv_Approved.setText("0");
        }


        if (Snoozed != null && !Snoozed.equals("")) {
            tv_Snoozed.setText(Snoozed);
        } else {
            tv_Snoozed.setText("0");
        }

        if (YTBReviewed != null && !YTBReviewed.equals("")) {
            tv_Pending.setText(YTBReviewed);
        } else {
            tv_Pending.setText("0");
        }

        if (Rejected != null && !Rejected.equals("")) {
            tv_Rejected.setText(Rejected);
        } else {
            tv_Rejected.setText("0");
        }


        if (ClientOrderPaused != null && !ClientOrderPaused.equals("")) {
            tv_Client_Order_Paused.setText(ClientOrderPaused);
        } else {
            tv_Client_Order_Paused.setText("0");
        }

        if (AwaitingCoordinatorApproval != null && !AwaitingCoordinatorApproval.equals("")) {
            tv_Awaiting_Coordinator_Approval.setText(AwaitingCoordinatorApproval);
        } else {
            tv_Awaiting_Coordinator_Approval.setText("0");
        }

        if (ApprovedOrder != null && !ApprovedOrder.equals("")) {
            tv_Approved_Orders.setText(ApprovedOrder);
        } else {
            tv_Approved_Orders.setText("0");
        }

        updateMenuTitles();

        ll_Approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP2.class);
                intent.putExtra("status", "Approved");
                intent.putExtra("job", jobID);
                startActivity(intent);

            }
        });

        ll_Snoozed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP2.class);
                intent.putExtra("status", "Snoozed");
                intent.putExtra("job", jobID);
                startActivity(intent);
            }
        });
        ll_Pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP2.class);
                intent.putExtra("status", "Yet To Be Reviewed");
                intent.putExtra("job", jobID);
                startActivity(intent);
            }
        });

        ll_Rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP2.class);
                intent.putExtra("status", "Rejected");
                intent.putExtra("job", jobID);
                startActivity(intent);
            }
        });

        ll_Client_Order_Paused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP1.class);
                intent.putExtra("status", "1");
                startActivity(intent);
            }
        });
        ll_Awaiting_Coordinator_Approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP1.class);
                intent.putExtra("status", "2");
                startActivity(intent);
            }
        });
        ll_Approved_Orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP1.class);
                intent.putExtra("status", "3");
                startActivity(intent);
            }
        });


    }

    public void fun_client_dashboard() {


        final String NAMESPACE = KEY_NAMESPACE;
        final String URL =SOAP_API_Client.BASE_URL;
        ;
        final String SOAP_ACTION = KEY_NAMESPACE + API_ShowClientDashStatus;
        final String METHOD_NAME = API_ShowClientDashStatus;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Client_id_Pk", Client_id_Pk);
        request.addProperty("comp_ID", comp_ID);
        request.addProperty("jobID", jobID);

        //request.addProperty("Connection", "Close");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        httpTransport.debug = true;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            //nks
            Log.d("HTTP REQUEST ", httpTransport.requestDump);
            Log.d("HTTP RESPONSE", httpTransport.responseDump);
            Object results = (Object) envelope.getResponse();

            String resultstring = results.toString();
            //{"cds":[{"Snoozed":"0","Approved":"0","YTBReviewed":"0","Rejected":"0"}]}
            JSONObject jsonObject = new JSONObject(resultstring);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Snoozed = jsonObject1.getString("Snoozed");
                    Approved = jsonObject1.getString("Approved");
                    YTBReviewed = jsonObject1.getString("YTBReviewed");
                    Rejected = jsonObject1.getString("Rejected");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void fun_client_dashboard_2() {


        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = SOAP_API_Client.BASE_URL;
        ;
        final String SOAP_ACTION = KEY_NAMESPACE + API_ShowStatus;
        final String METHOD_NAME = API_ShowStatus;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Str_jobId", jobID);
        request.addProperty("dealerID", DealerId);

        //request.addProperty("Connection", "Close");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        httpTransport.debug = true;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            //nks
            Log.d("HTTP REQUEST ", httpTransport.requestDump);
            Log.d("HTTP RESPONSE", httpTransport.responseDump);
            Object results = (Object) envelope.getResponse();

            String resultstring = results.toString();
            //{"cds":[{"Snoozed":"0","Approved":"0","YTBReviewed":"0","Rejected":"0"}]}
            JSONObject jsonObject = new JSONObject(resultstring);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Snoozed = jsonObject1.getString("Snoozed");
                    Approved = jsonObject1.getString("Approved");
                    YTBReviewed = jsonObject1.getString("YTBReviewed");
                    Rejected = jsonObject1.getString("Rejected");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getDahsboardDataEP1() {
        showProgressDialog();
        String client = comp_ID;
        String user = Client_id_Pk;
        String dealer =
                Shared_Preference.getCLIENT_LOGIN_DealerID(DashboardActivity.this);
        String URL = SOAP_API_Client.URL_EP1 + Api.API_GET_SERVICE_ORDER_COUNT + client + "&user=" + user + "&dealer=" + dealer;

        JsonObjectRequest bb = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                hideProgressDialog();

                try {
                    JSONArray jsonArray = obj.getJSONArray("data");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    try {
                        JSONArray jsonArray11 = jsonObject.getJSONArray("pending");
                        JSONObject jsonObject1 = jsonArray11.getJSONObject(0);
                        ClientOrderPaused = jsonObject1.getString("cpending");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    try {
                        JSONArray jsonArray12 = jsonObject.getJSONArray("waiting");
                        JSONObject jsonObject2 = jsonArray12.getJSONObject(0);
                        AwaitingCoordinatorApproval = jsonObject2.getString("waiting");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    try {
                        JSONArray jsonArray13 = jsonObject.getJSONArray("approved");
                        JSONObject jsonObject3 = jsonArray13.getJSONObject(0);
                        ApprovedOrder = jsonObject3.getString("approved");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                } catch (Exception e) {
                    e.getMessage();
                }
                setTextData();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                try {
                    hideProgressDialog();
                    Toast.makeText(DashboardActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.spinner:

             /*   if (new ConnectionDetector(DashboardActivity.this).isConnectingToInternet()) {
                    new get_company_job_id().execute();
                } else {
                    Toast.makeText(DashboardActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }*/
                Intent i = new Intent(this, SearchJobActivity.class);
                startActivityForResult(i, Utility.CODE_SELECT_JOB);


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dash_client, menu);
        // _menu = menu;
        this.menu = menu;
        return true;
    }


    private void updateMenuTitles() {
        MenuItem bedMenuItem = menu.findItem(R.id.spinner);

        bedMenuItem.setTitle(job_Name);

    }

    public void showProgressDialog() {

        if (!progressDoalog.isShowing()) {
            progressDoalog.show();
        }
    }

    public void hideProgressDialog() {
        try {
            if (progressDoalog != null) {
                progressDoalog.cancel();
            }
            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }
        } catch (Exception e) {
            e.getCause();
        }
    }

    public class async_getClientDashboardData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            showProgressDialog();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
          //  hideProgressDialog();
            setTextData();
            if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                getDahsboardDataEP1();
            } else {
                Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            if (jobID.equalsIgnoreCase("-1")) {
                fun_client_dashboard();
            } else {
                fun_client_dashboard_2();
            }
            return null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Utility.CODE_SELECT_JOB) {
                String Job_Desc = data.getStringExtra("Job_Desc");
                jobID  = data.getStringExtra("Job_id");
                job_Name  = data.getStringExtra("JobName");
                callApiDashboardDataEp2();


            }
        }
    }
}
