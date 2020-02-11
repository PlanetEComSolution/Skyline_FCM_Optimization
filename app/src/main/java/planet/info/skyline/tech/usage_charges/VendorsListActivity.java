package planet.info.skyline.tech.usage_charges;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.RequestControler.MyAsyncTask;
import planet.info.skyline.RequestControler.ResponseInterface;
import planet.info.skyline.adapter.MyDividerItemDecoration;
import planet.info.skyline.adapter.VendorsAdapter;
import planet.info.skyline.adapter.VendorsAdapterListener;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.Vendor;
import planet.info.skyline.network.Api;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

public class VendorsListActivity extends AppCompatActivity implements VendorsAdapterListener, ResponseInterface {

    ArrayList<Vendor> List_Vendor = new ArrayList<>();
    Context context;
    private RecyclerView recyclerView;
    private VendorsAdapter mAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Utility.getTitle("Vendors"));


        context = VendorsListActivity.this;
        recyclerView = findViewById(R.id.recycler_view);
        List_Vendor = new ArrayList<>();
        mAdapter = new VendorsAdapter(this, List_Vendor, this);
        // white background notification bar
        // whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL,   getResources().getInteger(R.integer.list_divider_margin_left)));
        recyclerView.setAdapter(mAdapter);

        FetchVendor();
    }


    private void FetchVendor() {
        JSONObject jsonObject = new JSONObject();
        try {
            String dealerId = Shared_Preference.getDEALER_ID(this);
            jsonObject.put("DealerID", dealerId);
            jsonObject.put("Dealer_Type", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_VENDOR, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vendor, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        if (id == android.R.id.home) {
            // API 5+ solution
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onVendorSelected(Vendor vendor) {
        //  Toast.makeText(getApplicationContext(), "Selected: " + vendor.getVenderName() , Toast.LENGTH_LONG).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("VendorName", vendor.getVenderName());
        returnIntent.putExtra("VendorID", vendor.getVenderID());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();


    }

    @Override
    public void handleResponse(String responseString, String api) {

        if (api.equalsIgnoreCase(Api.API_VENDOR)) {
            List_Vendor.clear();
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                try {
                    getSupportActionBar().setSubtitle(Utility.getTitle("Total : "+jsonArray.length()));
                } catch (Exception e) {
                }
                ArrayList<Vendor> items = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String VenderID = jsonObject1.getString("VenderID");
                    String VenderName = jsonObject1.getString("VenderName");
                    String IsCertified = jsonObject1.getString("IsCertified");
                    items.add(new Vendor(VenderID, VenderName, IsCertified));
                }

                if (items != null && items.size() > 0) {
                    List_Vendor.clear();
                    List_Vendor.addAll(items);
                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(VendorsListActivity.this, "No vendor found!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}