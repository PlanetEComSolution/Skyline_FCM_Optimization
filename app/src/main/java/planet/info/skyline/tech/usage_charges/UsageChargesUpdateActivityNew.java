package planet.info.skyline.tech.usage_charges;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import planet.info.skyline.R;
import planet.info.skyline.RequestControler.MyAsyncTask;
import planet.info.skyline.RequestControler.ResponseInterface;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.network.Api;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivityNew;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

public class UsageChargesUpdateActivityNew extends AppCompatActivity implements ResponseInterface {

    EditText et_job, et_company, et_Vendor;
    EditText Description, Cost, Total, Quantity;

    Button Btn_Save, Btn_Cancel;
    String compID = "";
    String jobID = "";
    String vendor_id = "";


    String description = "";
    String quantity = "";
    String cost = "";
    String total = "";

    String IsAdd = "1", UsageChargeID = "0";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_charges_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = UsageChargesUpdateActivityNew.this;
        setTitle(Utility.getTitle("Add Usage Charges"));

        setView();
        getDataFromIntent();

    }

    private void SaveUsageReportNew() {
        String user_id = Shared_Preference.getLOGIN_USER_ID(this);
        String Name = Shared_Preference.getLOGIN_USERNAME(UsageChargesUpdateActivityNew.this);
        String LoginUsername = Shared_Preference.getLOGIN_ID(this);
        if (IsAdd == null) IsAdd = "1";
        if (UsageChargeID == null) UsageChargeID = "0";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("jobid", jobID);
            jsonObject.put("compid", compID);
            jsonObject.put("vendorid", vendor_id);
            jsonObject.put("desc", description);
            jsonObject.put("cost", cost);
            jsonObject.put("quantity", quantity);
            jsonObject.put("total", total);
            jsonObject.put("LoginUserID", LoginUsername);
            jsonObject.put("empID", user_id);
            jsonObject.put("UserName", Name);
            jsonObject.put("IsAdd", IsAdd);
            jsonObject.put("UsageChargeID", UsageChargeID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_SAVE_USAGE_REPORT, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }


    }

    private void setView() {
        et_job = findViewById(R.id.job);
        et_company = findViewById(R.id.company);
        et_Vendor = findViewById(R.id.Vendor);
        Description = findViewById(R.id.Description);

        Quantity = findViewById(R.id.Quantity);

        Cost = findViewById(R.id.Cost);
        Total = findViewById(R.id.Total);
        Btn_Save = findViewById(R.id.Btn_Save);
        Btn_Cancel = findViewById(R.id.Btn_Cancel);

        Quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getTotal();
            }
        });

        Cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getTotal();
            }
        });

        et_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToSelectCompJobActivity();
            }
        });
        et_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToSelectCompJobActivity();
            }
        });
        et_Vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, VendorsListActivity.class);
                startActivityForResult(i, Utility.CODE_SELECT_VENDOR);
            }
        });
        Btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                description = Description.getText().toString().trim();
                quantity = Quantity.getText().toString().trim();
                cost = Cost.getText().toString().trim();
                total = Total.getText().toString().trim();


                if (compID.equals("")) {
                    // et_company.setError("Please enter valid company name!");
                    Toast.makeText(getApplicationContext(), "Please enter valid company name!", Toast.LENGTH_SHORT).show();
                } else if (jobID.equals("")) {
                    //  et_job.setError("Please enter valid job name!");
                    Toast.makeText(getApplicationContext(), "Please enter valid job name!", Toast.LENGTH_SHORT).show();
                } else if (vendor_id.equals("")) {
                    //  et_Vendor.setError("Please enter valid vendor name!");
                    Toast.makeText(getApplicationContext(), "Please enter valid vendor name!", Toast.LENGTH_SHORT).show();
                } else if (description.equals("")) {
                    //  Description.setError("Please enter description!");
                    Toast.makeText(getApplicationContext(), "Please enter description!", Toast.LENGTH_SHORT).show();
                } else if (quantity.equals("")) {
                    // Quantity.setError("Please enter quantity!");
                    Toast.makeText(getApplicationContext(), "Please enter quantity!", Toast.LENGTH_SHORT).show();
                } else if (cost.equals("")) {
                    // Cost.setError("Please enter cost!");
                    Toast.makeText(getApplicationContext(), "Please enter cost!", Toast.LENGTH_SHORT).show();
                } else if (total.equals("")) {
                    // Total.setError("Please enter total cost!");
                    Toast.makeText(getApplicationContext(), "Please enter total cost!", Toast.LENGTH_SHORT).show();
                } else {
                    //  new async_save_usage_report().execute();
                    SaveUsageReportNew();
                }

            }
        });
        Btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getDataFromIntent() {
        try {
            IsAdd = getIntent().getStringExtra(Utility.IS_ADD);
            UsageChargeID = getIntent().getStringExtra(Utility.USAGE_CHARGE_ID);

            if (!IsAdd.equals("1")) {
                setTitle(Utility.getTitle("Update Usage Charges"));
            }

            jobID = getIntent().getStringExtra("jobID");
            String Job = getIntent().getStringExtra("Job");

            compID = getIntent().getStringExtra("compid");
            String Company = getIntent().getStringExtra("Company");

            vendor_id = getIntent().getStringExtra("vendorid");
            String Vendor = getIntent().getStringExtra("Vendor");

            description = getIntent().getStringExtra("desc");
            cost = getIntent().getStringExtra("cost");
            quantity = getIntent().getStringExtra("quantity");
            total = getIntent().getStringExtra("total");

            et_company.setText(Company);
            et_job.setText(Job);
            et_Vendor.setText(Vendor);
            Description.setText(description);
            Quantity.setText(quantity);
            Cost.setText(cost);
            Total.setText(total);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void getTotal() {
        try {
            String quantity_str = Quantity.getText().toString().trim();
            double quantity = Double.parseDouble(quantity_str);
            String Cost_str = Cost.getText().toString().trim();
            double cost = Double.parseDouble(Cost_str);
            double total = quantity * cost;
            Total.setText(String.valueOf(total));
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

    private void GoToSelectCompJobActivity() {
        Intent i = new Intent(context, SelectCompanyActivityNew.class);
        i.putExtra(Utility.IS_JOB_MANDATORY, "1");
        i.putExtra(Utility.Show_DIALOG_SHOW_INFO, false);
        startActivityForResult(i, Utility.CODE_SELECT_COMPANY);
    }

    @Override
    public void handleResponse(String responseString, String api) {
        if (api.equalsIgnoreCase(Api.API_SAVE_USAGE_REPORT)) {
            try {

                String status = "0";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    status = jsonObject.getString("status");
                    String msg = jsonObject.getString("Message");
                } catch (Exception e) {
                    e.getMessage();
                }
                if (status.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Saved successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_CANCELED && requestCode == Utility.CODE_SELECT_COMPANY) {
            compID = intent.getStringExtra("CompID");
            final String CompanyName = intent.getStringExtra("CompName");
            final String JobName = intent.getStringExtra("JobName");
            jobID = intent.getStringExtra("JobID");
            et_company.setText(CompanyName);
            et_job.setText(JobName);

        } else if (resultCode != RESULT_CANCELED && requestCode == Utility.CODE_SELECT_VENDOR) {

            vendor_id = intent.getStringExtra("VendorID");
            final String VendorName = intent.getStringExtra("VendorName");
            et_Vendor.setText(VendorName);

        }

    }
}


