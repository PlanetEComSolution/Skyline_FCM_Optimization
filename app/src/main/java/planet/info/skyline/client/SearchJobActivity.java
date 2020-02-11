package planet.info.skyline.client;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.RequestControler.MyAsyncTask;
import planet.info.skyline.RequestControler.ResponseInterface;
import planet.info.skyline.adapter.JobsRecyclerAdapter;
import planet.info.skyline.adapter.MyDividerItemDecoration;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.Job_2;
import planet.info.skyline.network.Api;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

public class SearchJobActivity extends AppCompatActivity implements ResponseInterface {

    Context context;
    JobsRecyclerAdapter mAdapter;
    ArrayList<Job_2> list_Jobs = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Utility.getTitle("Jobs"));

        context = SearchJobActivity.this;
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new JobsRecyclerAdapter(context, list_Jobs);
        // white background notification bar
        // whiteNotificationBar(recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        FetchJobsByCompanyID();


    }

    private void FetchJobsByCompanyID() {

        String comp_ID =
                Shared_Preference.getCLIENT_LOGIN_CompID(SearchJobActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ClientID", comp_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this,true, this, Api.API_BindJob, jsonObject).execute();
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
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }


    public void onJobSelected(Job_2 job) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Job_Desc", job.getJOB_Name_Desc());
        returnIntent.putExtra("Job_id", job.getJOB_ID_PK());
        returnIntent.putExtra("JobName", job.getJobName());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();


    }

    @Override
    public void handleResponse(String responseString, String api) {

        if (api.equalsIgnoreCase(Api.API_BindJob)) {
            list_Jobs.clear();
            try {
                //    Gson gson = new Gson();
                //   GetCitiesResult citiesResult = gson.fromJson(responseString, GetCitiesResult.class);
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

                   /* Collections.sort(list_Jobs, new Comparator<Job_2>() {
                        @Override
                        public int compare( Job_2 o1, Job_2 o2) {
                            return o1.getJobName().compareTo(o2.getJobName());
                        }
                    });*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (list_Jobs != null && list_Jobs.size() > 0) {
                // refreshing recycler view
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(SearchJobActivity.this, "No vendor found!", Toast.LENGTH_LONG).show();
            }


        }

    }


}