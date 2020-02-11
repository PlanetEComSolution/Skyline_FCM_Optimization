package planet.info.skyline.tech.locate_crates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.adapter.LocateCrateAdapter;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.CreateDetails;
import planet.info.skyline.network.Api;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivityNew;
import planet.info.skyline.util.Utility;
import planet.info.skyline.util.Utils;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;
import static planet.info.skyline.util.Utility.LOADING_TEXT;


public class LocateCrates extends BaseActivity {
    ImageView merchantname;
    TextView clientname, text_noCrate, txtvw_selectCrate;
    ImageView imgvw_client_logo, homeacti;
    RecyclerView listView;
    ImageView img_search;
    EditText et_search;
    Context context;
    ProgressHUD mProgressHUD;
    private String urlskyline;
    //    private ProgressDialog pDialog;
    private List<CreateDetails> createlist = new ArrayList<CreateDetails>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeact_new);
        context = LocateCrates.this;
     /*   try {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.Loading_text));
            pDialog.setCancelable(false);
        } catch (Exception e) {
            e.getMessage();
        }*/
        listView = (RecyclerView) findViewById(R.id.listView1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        merchantname = (ImageView) findViewById(R.id.merchantname);
        merchantname.setImageResource(R.drawable.exhibitlogoa);
        clientname = (TextView) findViewById(R.id.txtvw_client_name);
        text_noCrate = (TextView) findViewById(R.id.text_noCrate);
        txtvw_selectCrate = (TextView) findViewById(R.id.homescreen);
        text_noCrate.setVisibility(View.GONE);
        imgvw_client_logo = findViewById(R.id.imgvw_client_logo);
        imgvw_client_logo.setVisibility(View.GONE);
        homeacti = findViewById(R.id.homeacti);
        search_Crate();
       /* try {
            String companyName = Shared_Preference.getCLIENT_NAME(this);
            String imageURL = Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);
            if (imageURL.equals("") || imageURL.equalsIgnoreCase("")) {
                imgvw_client_logo.setVisibility(View.GONE);
                clientname.setText(companyName);
                clientname.setVisibility(View.VISIBLE);
            } else {
                imgvw_client_logo.setVisibility(View.VISIBLE);
                clientname.setVisibility(View.GONE);
                Glide
                        .with(LocateCrates.this)
                        .load(imageURL)
                        .into(imgvw_client_logo);
            }
        } catch (Exception e) {
            e.getCause();
        }
*/

        Utils.DisplayClientLogo(this,clientname,imgvw_client_logo);
        homeacti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Check if this module is accessed directly from home screen or after clock started
        final boolean TIMER_STARTED_FROM_BILLABLE_MODULE = Shared_Preference.getTIMER_STARTED_FROM_BILLABLE_MODULE(this);
        if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
            txtvw_selectCrate.setVisibility(View.VISIBLE);
            if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                urlskyline = Shared_Preference.getLINK(this);
                getCrateList();
            } else {

                Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }
        } else {
            txtvw_selectCrate.setVisibility(View.GONE);
            Intent i = new Intent(LocateCrates.this, SelectCompanyActivityNew.class);
            i.putExtra(Utility.IS_JOB_MANDATORY, "0");
            i.putExtra(Utility.Show_DIALOG_SHOW_INFO, false);
            startActivityForResult(i, Utility.CODE_SELECT_COMPANY);
        }


    }

    public void getCrateList() {
        showprogressdialog();
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, urlskyline,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                try {
                    parseJsonFeed(obj);
                } catch (Exception e) {
                    e.getMessage();
                }
                hideprogressdialog();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                if (createlist.size() > 0) {
                    text_noCrate.setVisibility(View.GONE);
                } else {
                    text_noCrate.setVisibility(View.VISIBLE);
                }
                hideprogressdialog();
            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }

    /*    public void showprogressdialog() {
            try {
                pDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        public void hideprogressdialog() {
            try {
                pDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
        }*/
    public void showprogressdialog() {
       /* try {
            if (!(pDialog.isShowing())) {
                pDialog.show();
            }
        } catch (Exception e) {
            e.getMessage();
        }*/
        try {
            mProgressHUD = ProgressHUD.show(context, LOADING_TEXT, false);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void hideprogressdialog() {
        /*try {
            if ((pDialog.isShowing())) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.getMessage();
        }*/
        try {
            if (mProgressHUD.isShowing()) {
                mProgressHUD.dismiss();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utility.CODE_SELECT_COMPANY) {

            if (resultCode == Activity.RESULT_OK) {
                try {
                    String CompID = data.getStringExtra("CompID");
                    if (CompID.equals("") || CompID == null) {
                        Toast.makeText(getApplicationContext(), "Please select company!", Toast.LENGTH_SHORT).show();
                        text_noCrate.setVisibility(View.VISIBLE);
                    } else {
                        urlskyline = URL_EP1 + Api.API_CRATES_LIST
                                + CompID;
                        if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                            getCrateList();
                        } else {
                            Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "Exception caught!", Toast.LENGTH_SHORT).show();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

                finish();
            }
        }
    }

    private void parseJsonFeed(JSONObject response) {

        try {
            JSONArray Array = response.getJSONArray("data");
            JSONObject jsonObject = Array.getJSONObject(0);
            JSONArray client_info = jsonObject.getJSONArray("client_info");
            JSONObject jsonObject1 = client_info.getJSONObject(0);
            String companyLogo = jsonObject1.getString("logo");
            String companyName = jsonObject1.getString("name");


            String imagelogo = URL_EP1 + Api.API_COLLATERAL_PATH + companyLogo;
            Shared_Preference.setCLIENT_NAME(context, companyName);
            Shared_Preference.setCLIENT_IMAGE_LOGO_URL(context, imagelogo);

            Utils.DisplayClientLogo(this,clientname,imgvw_client_logo);


            JSONArray feedArray = jsonObject.getJSONArray("cds");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = feedArray.getJSONObject(i);
                CreateDetails item = new CreateDetails();
                item.setCretateid(feedObj.getString("id"));
                if (feedObj.getString("description") == null || feedObj.getString("description").equalsIgnoreCase("null") || feedObj.getString("description").trim().equalsIgnoreCase("")) {
                    item.setDescription("N/A");
                } else {
                    item.setDescription(feedObj.getString("description"));
                }
                item.setLocationcrate(feedObj.getString("cur_location"));
                item.setUnique_crate_id(feedObj.getString("crate_number"));
                item.setSelectim(0);
                item.setMissim(0);
                item.setPos(i);
                item.setSelected(false);
                createlist.add(item);
            }

            if (createlist.size() > 1) {
                Collections.reverse(createlist);
            }
            try {
                LocateCrateAdapter adapters = new LocateCrateAdapter(this, createlist);
                listView.setAdapter(adapters);

                if (createlist.size() > 0) {
                    text_noCrate.setVisibility(View.GONE);
                } else {
                    text_noCrate.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.getMessage();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (createlist.size() > 0) {
            text_noCrate.setVisibility(View.GONE);
        } else {
            text_noCrate.setVisibility(View.VISIBLE);
        }
    }

    public void search_Crate() {
        et_search = (EditText) findViewById(R.id.et_search);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                } else {
                    et_search.setFocusable(false);
                    et_search.setFocusableInTouchMode(true);
                }
            }
        });
        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean gotfocus) {
                if (gotfocus) {
                    et_search.setCompoundDrawables(null, null, null, null);
                } else if (!gotfocus) {
                    if (et_search.getText().length() == 0) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                        et_search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.searcher, 0, 0, 0);
                    }
                }
            }
        });

        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;

            }
        });
        img_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                search();

            }
        });

    }

    public void search() {
        showprogressdialog();
        List<CreateDetails> new_list = new ArrayList<CreateDetails>();
        String search_text = et_search.getText().toString().trim().toLowerCase();
        for (int i = 0; i < createlist.size(); i++) {
            CreateDetails createDetails = createlist.get(i);
            String uniqueCrateId = createDetails.getUnique_crate_id().toLowerCase();
            String crateDesc = createDetails.getDescription().toLowerCase();
            if (uniqueCrateId.contains(search_text) || crateDesc.contains(search_text)) {
                new_list.add(createDetails);
            }
        }
        try {
            LocateCrateAdapter adapters = new LocateCrateAdapter(this, new_list);
            listView.setAdapter(adapters);
            if (new_list.size() > 0) {
                text_noCrate.setVisibility(View.GONE);
            } else {
                text_noCrate.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            // Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_SHORT).show();
        }
        hideprogressdialog();
    }
}


