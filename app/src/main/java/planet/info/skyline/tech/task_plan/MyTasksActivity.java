package planet.info.skyline.tech.task_plan;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.adapter.MyTasksRecyclerAdapter;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.MyTask;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.REST_API_Client_TaskPlan;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static planet.info.skyline.util.Utility.LOADING_TEXT;

public class MyTasksActivity extends AppCompatActivity {

    TextView tv_msg;
    List<MyTask> list_myTask = new ArrayList<>();
    Context context;
    ProgressHUD mProgressHUD;
    private RecyclerView recycler_myTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        context = MyTasksActivity.this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Utility.getTitle("My Task(s)"));

        recycler_myTasks = (RecyclerView) findViewById(R.id.recycler_myTasks);
        recycler_myTasks.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler_myTasks.setItemAnimator(new DefaultItemAnimator());

        tv_msg = (TextView) findViewById(R.id.tv_msg);

        if (new ConnectionDetector(context).isConnectingToInternet()) {
            getmyTaskList();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }

    private void getmyTaskList() {
        showprogressdialog();
        final ArrayList<MyTask> myTaskTempList=new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("CateID", "");
        hashMap.put("DealerID", "");
        hashMap.put("UserId", Shared_Preference.getLOGIN_USER_ID(this));
        hashMap.put("uniqueid", "");

        API_Interface apiService = REST_API_Client_TaskPlan.getClient().create(API_Interface.class);
        apiService.GetTaskListByEmployeeID(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<MyTask>>() {
                    @Override
                    public void onCompleted() {
                        hideprogressdialog();
                        list_myTask.clear();

                        if (Shared_Preference.get_EnterTimesheetByAWO(context)) { // AWO
                            for (MyTask myTask : myTaskTempList) {
                                String isAWO = String.valueOf(myTask.getIsAWO());
                                if (isAWO.equals("1")) {
                                    list_myTask.add(myTask);
                                }
                            }
                        } else {     // SWO
                            for (MyTask myTask : myTaskTempList) {
                                String isAWO = String.valueOf(myTask.getIsAWO());
                                if (isAWO.equals("2")) {
                                    list_myTask.add(myTask);
                                }
                            }
                        }


                        if (list_myTask.isEmpty()) {
                            tv_msg.setVisibility(View.VISIBLE);
                        } else {
                            tv_msg.setVisibility(View.GONE);
                        }
                        MyTasksRecyclerAdapter myTasksRecyclerAdapter = new MyTasksRecyclerAdapter(context, list_myTask);
                        recycler_myTasks.setAdapter(myTasksRecyclerAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideprogressdialog();
                        String error = e.getMessage();
                        Toast.makeText(context,
                                "Network Error....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<MyTask> myTask) {
                        myTaskTempList.clear();
                        myTaskTempList.addAll(myTask);

                    }
                });


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }

            default: {
                return false;
            }
        }
    }


}
