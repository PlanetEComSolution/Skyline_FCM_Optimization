package planet.info.skyline.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Iterator;

import planet.info.skyline.R;
import planet.info.skyline.RequestControler.MyAsyncTask;
import planet.info.skyline.RequestControler.ResponseInterface;
import planet.info.skyline.adapter.PausedJob_ListAdapter;
import planet.info.skyline.adapter.SavedTimeSheet_ListAdapter;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.floating_view.ChatHeadService;
import planet.info.skyline.model.Awo;
import planet.info.skyline.model.PausedJob;
import planet.info.skyline.model.SWO_Details;
import planet.info.skyline.model.SavedTask;
import planet.info.skyline.network.Api;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivityNew;
import planet.info.skyline.tech.damage_report.DamageReportNew;
import planet.info.skyline.tech.job_files_new.JobFilesTabActivity;
import planet.info.skyline.tech.locate_crates.LocateCrates;
import planet.info.skyline.tech.material_move.SlotMoveactivity;
import planet.info.skyline.tech.task_plan.ChecklistActivity;
import planet.info.skyline.tech.update_timesheet.TimeSheetList1Activity;
import planet.info.skyline.tech.upload_photo.UploadPhotosActivity;
import planet.info.skyline.tech.usage_charges.UsageChargesListActivity;
import planet.info.skyline.tech.whats_inside.ShowWhatsInside_MainActivity;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_GetEmployee_Roles;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;
import static planet.info.skyline.util.Utility.LOADING_TEXT;


public class MainActivity extends BaseActivity implements ResponseInterface {

    private static final int WIFI_SETTING_REQUEST_CODE = 458;
    private static final int MOBILE_DATA_SETTING_REQUEST_CODE = 459;


    Dialog swo_dialog;
    String Comp_ID = "";

    boolean doubleBackToExitPressedOnce = false;
    LinearLayout ll_billable_row, ll_billable_row_new, ll_Checklist;

    ArrayList<PausedJob> pausedJobList = new ArrayList<>();
    WifiManager wifiManager;
    AlertDialog alertDialog;
    String userRole = "";
    ArrayList<Awo> list_Awo = new ArrayList<>();
    ArrayList<SWO_Details> list_SWO = new ArrayList<>();
    AlertDialog alertDialog_Paused;
    ArrayList<String> list_Employee_Role = new ArrayList<>();

    boolean CanEnter_MISByDailyTime = false;
    boolean Can_EditTime = false;
    boolean CanEnter_BillableTime = false;
    boolean CanEnter_DamageReport = false;
    boolean CanEnter_UsageCharge = false;
    boolean CanUpload_ClientArt = false;
    boolean CanUpload_ClientPhoto = false;

    boolean CanView_MISByDailyTime = false;
    boolean CanView_EditTime = false;
    boolean CanView_BillableTime = false;
    boolean CanView_ChangeOrder = false;
    boolean CanView_UsageCharge = false;
    boolean CanView_ClientArt = false;

    Context context;
    ProgressHUD mProgressHUD;
    private long lastClickTime = 0;
    private String CLOCK_IN_BILLABLE_CODE = "";
    private String CLOCK_OUT_BILLABLE_CODE = "";
    private String IN_PROGRESS_SWO_AWO_STATUS = "";
    private int BillableLaborCodesLength = 0;
    private boolean DamageReportModuleClicked = false;
    private boolean BillableJobModuleClicked = false;
    private boolean AdminTimesheetModuleClicked = false;
    private boolean UsageChargeModuleClicked = false;
    private boolean UploadPhotoModuleClicked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billablenonbillable_new);
        context = MainActivity.this;
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        userRole = Shared_Preference.getUSER_ROLE(this);

        INITIALIZE_VIEWS();

        if (Utility.isAppLaunchedFirstTime(context)) {
            Utility.setAppNotLaunchedFirstTime(context);
            checkWifi();
        }


        if (Utility.isAppUpdated(context)) {
            String currentVersion = Utility.getAppVersion(context);
            String oldVersion = Shared_Preference.getOldVersion(context);
            String text = "App updated successfully from V " + oldVersion + " to V " + currentVersion + "";
            dialog_App_Updated(text, true);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.showChatHead(getApplicationContext());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Utility.CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE) {
            {
            }
        } else if (requestCode == WIFI_SETTING_REQUEST_CODE) {
            if (new ConnectionDetector(context).isConnectingToInternet()) {
                // new checkVersionUpdate().execute();
                CheckVersion();
            } else {
                dialog_TurnOnMobileData();
            }
        } else if (requestCode == MOBILE_DATA_SETTING_REQUEST_CODE) {
            if (new ConnectionDetector(context).isConnectingToInternet()) {
                // new `().execute();
                CheckVersion();
            } else {
                dialog_MobileDataNotTrunedON();
            }
        } else if (requestCode == Utility.CODE_SELECT_COMPANY && resultCode == Activity.RESULT_OK) {

            Comp_ID = intent.getStringExtra("CompID");
            final String CompanyName = intent.getStringExtra("CompName");
            final String JobName = intent.getStringExtra("JobName");
            final String JOB_ID = intent.getStringExtra("JobID");
            String SWO_AWO_Id = "";
            boolean SWO_AWO_ID_AVAILABLE = intent.getBooleanExtra(Utility.SWO_AWO_ID_AVAILABLE, false);
            if (SWO_AWO_ID_AVAILABLE) {
                SWO_AWO_Id = intent.getStringExtra("Swo_Id");
            } else {
                Bundle bundle = intent.getBundleExtra("BUNDLE");
                list_Awo.clear();
                list_SWO.clear();
                try {
                    if (bundle.containsKey("Awo")) {
                        final Object myListObj = bundle.getSerializable("Awo");
                        if (myListObj instanceof ArrayList<?>) {
                            if (((ArrayList<?>) myListObj).get(0) instanceof Awo)
                                list_Awo = (ArrayList<Awo>) bundle.getSerializable("Awo");
                            SWO_AWO_Id = list_Awo.get(0).getiDPK();//awo_id
                        }
                    } else if (bundle.containsKey("Swo")) {
                        final Object myListObj = bundle.getSerializable("Swo");
                        if (myListObj instanceof ArrayList<?>) {
                            if (((ArrayList<?>) myListObj).get(0) instanceof SWO_Details)
                                list_SWO = (ArrayList<SWO_Details>) bundle.getSerializable("Swo");
                            SWO_AWO_Id = list_SWO.get(0).getSwo_id();//swo_id
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            // for clock in - clock out
            Shared_Preference.setJOB_NAME_BILLABLE(context, JobName);
            Shared_Preference.setCLIENT_NAME(this, CompanyName);
            Shared_Preference.setSWO_ID(this, SWO_AWO_Id);
            Shared_Preference.setJOB_ID_FOR_JOBFILES(context, JOB_ID);
            // for clock in - clock out

            if (JobName.equalsIgnoreCase(Utility.CLOCK_IN) || JobName.equalsIgnoreCase(Utility.CLOCK_OUT)) {
                CallAPI_FetchClockInClockOutCodes();
            } else if (SWO_AWO_ID_AVAILABLE) {
                prepareDataForClock(context, Comp_ID, SWO_AWO_Id, JOB_ID);
            } else {
                if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                    Dialog_show_AWO_List();
                } else {
                    Dialog_show_SWO_List();
                }
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    @SuppressLint("NewApi")
    private void showChatHead_Billable(Context context, boolean isShowOverlayPermission) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.startService(new Intent(context, ChatHeadService.class));
            return;
        }
        if (Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(context, ChatHeadService.class);
            context.startService(intent);
            Set_Billable_Row();
        } else {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, Utility.CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE);
        }


    }

    @SuppressLint("NewApi")
    private void showChatHead_Admin(Context context, boolean isShowOverlayPermission) {
        Shared_Preference.setTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(this, true);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.startService(new Intent(context, ChatHeadService.class));
            return;
        }
        if (Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(context, ChatHeadService.class);
            context.startService(intent);
            return;
        }
        if (isShowOverlayPermission) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, Utility.CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE);
        }


    }

    public void GoTo_Select_CompJob_Activity() {
        Intent i = new Intent(this, SelectCompanyActivityNew.class);
        i.putExtra(Utility.IS_JOB_MANDATORY, "1");
        i.putExtra(Utility.Show_DIALOG_SHOW_INFO, true);
        i.putExtra(Utility.Show_SWO_LIST_OPTION, true);
        i.putExtra(Utility.STARTING_BILLABLE_JOB, true);

        startActivityForResult(i, Utility.CODE_SELECT_COMPANY);


    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Utility.HideRunningClock(getApplicationContext());
            return;
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit!",
                Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);


    }

    public void INITIALIZE_VIEWS() {

        String nam = Shared_Preference.getLOGIN_USERNAME(this);
        TextView techname = (TextView) findViewById(R.id.textView1rr);
        ImageView merchantname = (ImageView) findViewById(R.id.merchantname);
        merchantname.setImageResource(R.drawable.exhibitlogoa);

        //nks.....
        LinearLayout ll_billableJob = (LinearLayout) findViewById(R.id.first);
        LinearLayout ll_timeClock = (LinearLayout) findViewById(R.id.first1);
        LinearLayout ll_MaterialMove = (LinearLayout) findViewById(R.id.first2);
        LinearLayout ll_Upload = (LinearLayout) findViewById(R.id.first3);
        ll_Checklist = (LinearLayout) findViewById(R.id.ll_Checklist);


        TextView tv_version = (TextView) findViewById(R.id.tv_ver);


        ll_billable_row = (LinearLayout) findViewById(R.id.ll_billable);
        ll_billable_row_new = (LinearLayout) findViewById(R.id.ll_billable_new);
        ll_billable_row.setVisibility(View.VISIBLE);
        ll_billable_row_new.setVisibility(View.GONE);

        LinearLayout ll_LocateCrate = (LinearLayout) findViewById(R.id.ll_LocateCrate);
        LinearLayout ll_DamageReport = (LinearLayout) findViewById(R.id.ll_DamageReport);
        LinearLayout ll_UsageCharges = (LinearLayout) findViewById(R.id.ll_UsageCharges);

        final boolean TIMER_STARTED_FROM_BILLABLE_MODULE = Shared_Preference.getTIMER_STARTED_FROM_BILLABLE_MODULE(this);

        if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
            Set_Billable_Row();
        }


        ll_billableJob.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //   preventing double click, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
//                if (Utility.isTimeAutomatic(context)) {
                BillableJobModuleClicked = true;
                boolean isTimerRunningFromAdminClockModule = Shared_Preference.getTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(context);
                if (isTimerRunningFromAdminClockModule) {
                    Toast.makeText(context, "Clock already running for admin functions!", Toast.LENGTH_LONG).show();
                } else {
                    Shared_Preference.setIS_STARTING_BILLABLE_JOB(context, true);
                    showprogressdialog();
                    CallAPI_CheckPermission();

                }

//                } else {
//                    dialog_AutoDateTimeSet();
//                }

            }
        });

        /*nks*/
        ll_timeClock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                AdminTimesheetModuleClicked = true;
                if (Utility.checkDrawOverlayPermission(MainActivity.this)) {
                    boolean isTimerRunningFromAdminClockModule = Shared_Preference.getTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(context);
                    if (!isTimerRunningFromAdminClockModule) {
                        Shared_Preference.setIS_STARTING_BILLABLE_JOB(context, false);
                        showprogressdialog();
                        CallAPI_CheckPermission();
                    } else {
                        Toast.makeText(context, "Clock already running for admin functions!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        ll_MaterialMove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionDetector(context).isConnectingToInternet()) {
                    startActivity(new Intent(context, SlotMoveactivity.class));
                    finish();
                } else {
                    Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });
        ll_Upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadPhotoModuleClicked = true;
                CallAPI_CheckPermission();
            }
        });
        //  ll_jobFiles.setVisibility(View.GONE);
        LinearLayout ll_jobFiles_new = (LinearLayout) findViewById(R.id.first6);
        ll_jobFiles_new.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                start_JobFilesActivity();
            }
        });


        LinearLayout ll_whatsInside = (LinearLayout) findViewById(R.id.ll_whatsInside);
        ll_whatsInside.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConnectionDetector(context).isConnectingToInternet()) {
                    Intent in = new Intent(context, ShowWhatsInside_MainActivity.class);
                    startActivity(in);
                } else {
                    Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //.....nks

        techname.setText("Welcome\n " + nam);

        ImageView ll_logout = findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(ChatHeadService.class)) {
                    Toast.makeText(context, "Clock is running, Please submit it first!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = LayoutInflater.from(context);
                        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
                        dialogView.setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialogBuilder.setView(dialogView);
                        final TextView title = dialogView.findViewById(R.id.textView1rr);
                        final TextView message = dialogView.findViewById(R.id.texrtdesc);

                        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
                        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
                        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
                        close.setVisibility(View.INVISIBLE);
                        // dialogBuilder.setTitle("Device Details");
                        title.setText("Logout?");
                        message.setText("Are you sure?");
                        positiveBtn.setText("Yes");
                        negativeBtn.setText("No");
                        negativeBtn.setVisibility(View.VISIBLE);
                        positiveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                boolean result = Shared_Preference.clearAllPreferences(context);
                                if (result) {

                                    Shared_Preference.setLoginFalse(context);
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(context, "Unable to clear app data!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        negativeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();

                            }
                        });
                        alertDialog = dialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        try {
                            alertDialog.show();
                        } catch (Exception e) {
                            e.getCause();
                        }


                    } catch (Exception e) {
                        e.getCause();
                    }
                }
            }
        });

        ll_LocateCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConnectionDetector(context).isConnectingToInternet()) {
                    Intent ii = new Intent(context, LocateCrates.class);
                    startActivity(ii);
                } else {
                    Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();

                }
            }
        });

        ll_DamageReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DamageReportModuleClicked = true;
                CallAPI_CheckPermission();

            }
        });
        ll_UsageCharges.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                UsageChargeModuleClicked = true;
                CallAPI_CheckPermission();


            }
        });
        ll_Checklist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent(context, ChecklistActivity.class);
                returnIntent.putExtra("job_id", Shared_Preference.getJOB_ID_FOR_JOBFILES(context));
                returnIntent.putExtra("task_id", Shared_Preference.get_Task_ID(context));
                returnIntent.putExtra("id", Shared_Preference.get_Task_PrimaryID(context));
                returnIntent.putExtra("Updatable", "1");
                (context).startActivity(returnIntent);


            }
        });

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            tv_version.setText("v" + versionName);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void Set_Billable_Row() {
        Shared_Preference.setTIMER_STARTED_FROM_BILLABLE_MODULE(this, true);
        ll_billable_row.setVisibility(View.GONE);
        ll_billable_row_new.setVisibility(View.VISIBLE);
        if (Shared_Preference.get_JobStartedFromMyTask(context)) {
            ll_Checklist.setVisibility(View.VISIBLE);
        } else {
            ll_Checklist.setVisibility(View.GONE);
        }

    }


    public void startClockForBillable(Context context) {

        String sDate = Utility.getCurrentTimeString();
        Shared_Preference.setTIMEGAP_JOB_START_TIME(context, sDate);//for next job time gap
        Shared_Preference.setPAUSED_TIMESHEET_ID(context, "0");

        if (Utility.IsTimeGapIssue(context)) {
            showDialog_for_TimeGapIssue_1(context);
        } else {
            Shared_Preference.setCLOCK_START_TIME(this, sDate);
            showClock(context);
        }
    }

    public void startClockForPaused(Context context) {
        String sDate = Utility.getCurrentTimeString();
        Shared_Preference.setCLOCK_START_TIME(this, sDate);
        showClock(context);
    }

    public void startClockForAdmin(Context context) {

        String sDate = Utility.getCurrentTimeString();
        Shared_Preference.setTIMEGAP_JOB_START_TIME(context, sDate);//for next job time gap
        if (Utility.IsTimeGapIssue(context)) {
            showDialog_for_TimeGapIssue_1(context);
        } else {
            Shared_Preference.setCLOCK_START_TIME(this, sDate);
            showClock(context);
        }

    }

    public void showDialog_for_TimeGapIssue_1(final Context context) {

        final Dialog showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.time_gap);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        ImageView close = (ImageView) showd.findViewById(R.id.close);

        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);
        final TextView Btn_Select_Another = (TextView) showd.findViewById(R.id.Btn_Select_Another);
        Btn_Done.setText("OK");
        Btn_Select_Another.setText("Select another option");

        long Total_Elapsed_Seconds = Utility.GetTimeGap(context);
        long totalmin = Total_Elapsed_Seconds / 60;
        long totalSec = Total_Elapsed_Seconds % 60;
        String str_totalmin;
        String str_totalSec;
        if (totalmin < 10) str_totalmin = "0" + String.valueOf(totalmin);
        else str_totalmin = String.valueOf(totalmin);

        if (totalSec < 10) str_totalSec = "0" + String.valueOf(totalSec);
        else str_totalSec = String.valueOf(totalSec);

        String diff = "00:" + str_totalmin + ":" + str_totalSec;

        tv_title.setText("");
        tv_title.setVisibility(View.GONE);


        tv_msg.setText("There is a difference of " + diff + "" +
                " in the time entry between your " +
                "last job and current job. " +
                "ExhibitPower will split the time " +
                "between your previous job and the current job.");

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                //split time between prev job and current job
                long Total_Elapsed_Seconds = Utility.GetTimeGap(context);
                String CurrentJobStartTime = Shared_Preference.getTIMEGAP_JOB_START_TIME(context);//dd-MM-yyyy HH:mm:ss
                int half_Elapsed_Seconds = (int) (Total_Elapsed_Seconds / 2);
                String CurrentJobStartTime_new = Utility.minus_seconds_fromCurrentTime(CurrentJobStartTime, half_Elapsed_Seconds);
                Shared_Preference.setCLOCK_START_TIME(context, CurrentJobStartTime_new);
                if (new ConnectionDetector(context).isConnectingToInternet()) {
                    //new Async_EditTimeSheetSplit().execute();
                    AsyncUpdateTimeSheetSplit();
                    showClock(context);

                } else {
                    Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });
        Btn_Select_Another.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                showDialog_for_TimeGapIssue_2();
            }
        });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void showDialog_for_TimeGapIssue_2() {

        final Dialog showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.time_gap_2);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        ImageView close = (ImageView) showd.findViewById(R.id.close);

        final RadioButton RBtn_AddToPrevJob = (RadioButton) showd.findViewById(R.id.RBtn_AddToPrevJob);
        final RadioButton RBtn_AddToCurrentJob = (RadioButton) showd.findViewById(R.id.RBtn_AddToCurrentJob);
        final RadioButton RBtn_split = (RadioButton) showd.findViewById(R.id.RBtn_split);
        final RadioButton RBtn_Cancel = (RadioButton) showd.findViewById(R.id.RBtn_Cancel);
        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);
        final TextView Btn_Cancel = (TextView) showd.findViewById(R.id.Btn_Cancel);


        tv_title.setText("");
        tv_title.setVisibility(View.GONE);
        tv_msg.setVisibility(View.GONE);

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if (RBtn_AddToPrevJob.isChecked()) {


                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    // call_api_to_update_timesheet_job1();
                    if (new ConnectionDetector(context).isConnectingToInternet()) {
                        String curent_BILLABLE_START_TIME = Shared_Preference.getTIMEGAP_JOB_START_TIME(context);//dd-MM-yyyy HH:mm:ss
                        Shared_Preference.setCLOCK_START_TIME(context, curent_BILLABLE_START_TIME);
                        // new Async_EditTimeSheet().execute();
                        AsyncUpdateTimesheet();
                        showClock(context);
                    } else {
                        Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }

                } else if (RBtn_AddToCurrentJob.isChecked()) {
                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    String Str_PrevJobEndTime = Shared_Preference.getTIMEGAP_JOB_END_TIME(context);
                    Shared_Preference.setCLOCK_START_TIME(context, Str_PrevJobEndTime);
                    showClock(context);

                } else if (RBtn_split.isChecked()) {

                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    // halftime=timeDifference/2;
                    // call_api_to_update_time_sheet_Prevjob();  //use halftime
                    // add halftime to the start time of current job

                    long Total_Elapsed_Seconds = Utility.GetTimeGap(context);
                    String CurrentJobStartTime = Shared_Preference.getTIMEGAP_JOB_START_TIME(context);//dd-MM-yyyy HH:mm:ss

                    int half_Elapsed_Seconds = (int) (Total_Elapsed_Seconds / 2);
                    String CurrentJobStartTime_new = Utility.minus_seconds_fromCurrentTime(CurrentJobStartTime, half_Elapsed_Seconds);
                    Shared_Preference.setCLOCK_START_TIME(context, CurrentJobStartTime_new);

                    if (new ConnectionDetector(context).isConnectingToInternet()) {
                        //  new Async_EditTimeSheetSplit().execute();
                        AsyncUpdateTimeSheetSplit();
                        showClock(context);
                    } else {
                        Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }

                } else if (RBtn_Cancel.isChecked()) {

                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    String CurrentJobStartTime = Shared_Preference.getTIMEGAP_JOB_START_TIME(context);//dd-MM-yyyy HH:mm:ss
                    Shared_Preference.setCLOCK_START_TIME(context, CurrentJobStartTime);
                    showClock(context);
                } else {
                    Toast.makeText(context, "Please choose an option!", Toast.LENGTH_LONG).show();

                }
            }
        });
        Btn_Cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });


        try {
            showd.show();

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void AsyncUpdateTimesheet() {

        String timesheetId = Shared_Preference.getTIME_SHEET_ID(this);
        String PREV_JOB_START_TIME = Shared_Preference.getTIMEGAP_PREV_JOB_START_TIME(context);

        String str_PREV_JOB_START_TIME = PREV_JOB_START_TIME;
        String arr1[] = PREV_JOB_START_TIME.split(" ");
        String str1 = arr1[1];
        PREV_JOB_START_TIME = str1.substring(0, str1.lastIndexOf(":"));

        String CURRENT_JOB_START_TIME = Shared_Preference.getTIMEGAP_JOB_START_TIME(context);//dd-MM-yyyy HH:mm:ss

        String str_CURRENT_JOB_START_TIME = CURRENT_JOB_START_TIME;
        String arr[] = CURRENT_JOB_START_TIME.split(" ");
        String str = arr[1];
        CURRENT_JOB_START_TIME = str.substring(0, str.lastIndexOf(":"));

        boolean bool_IsSameDay = Utility.IsSameDay(str_PREV_JOB_START_TIME, str_CURRENT_JOB_START_TIME);
        String dayInfo = "0";
        if (bool_IsSameDay) dayInfo = "0";
        else dayInfo = "1";


        if (CURRENT_JOB_START_TIME.equals("00:00")) {
            dayInfo = "1";
        }
        String Id = Shared_Preference.getLOGIN_USER_ID(context);
        String dt = Utility.parseDateToMMddyyyy(Utility.getCurrentTimeString());

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id", timesheetId);
            jsonObject.put("start_time", PREV_JOB_START_TIME);
            jsonObject.put("end_time", CURRENT_JOB_START_TIME);
            jsonObject.put("dayInfo", dayInfo);
            jsonObject.put("EMP_ID", Id);
            jsonObject.put("DATE_TIME", dt);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_EditTimesheet_AWO_SWO, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    private void AsyncUpdateTimeSheetSplit() {
        String Id = Shared_Preference.getLOGIN_USER_ID(this);
        String dt = Utility.parseDateToMMddyyyy(Utility.getCurrentTimeString());

        String timesheetId = Shared_Preference.getTIME_SHEET_ID(this);
        String PREV_JOB_START_TIME = Shared_Preference.getTIMEGAP_PREV_JOB_START_TIME(context);

        String str_PREV_JOB_START_TIME = PREV_JOB_START_TIME;
        String arr1[] = PREV_JOB_START_TIME.split(" ");
        String str1 = arr1[1];
        PREV_JOB_START_TIME = str1.substring(0, str1.lastIndexOf(":"));//HH:mm
        String PREV_JOB_END_TIME = Shared_Preference.getTIMEGAP_JOB_END_TIME(context);
        String str_PREV_JOB_END_TIME = PREV_JOB_END_TIME;

        long Total_Elapsed_Seconds = Utility.GetTimeGap(context);
        int half_Elapsed_Seconds = (int) (Total_Elapsed_Seconds / 2);
        String PREV_JOB_END_TIME_new = Utility.add_seconds_toCurrentTime(PREV_JOB_END_TIME, half_Elapsed_Seconds);

        String arr[] = PREV_JOB_END_TIME_new.split(" ");
        String str = arr[1];
        PREV_JOB_END_TIME_new = str.substring(0, str.lastIndexOf(":"));//HH:mm


        boolean bool_IsSameDay = Utility.IsSameDay(str_PREV_JOB_START_TIME, str_PREV_JOB_END_TIME);
        String dayInfo = "0";
        if (bool_IsSameDay) dayInfo = "0";
        else dayInfo = "1";

        if (PREV_JOB_END_TIME_new.equals("00:00")) {
            dayInfo = "1";
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", timesheetId);
            jsonObject.put("start_time", PREV_JOB_START_TIME);
            jsonObject.put("end_time", PREV_JOB_END_TIME_new);
            jsonObject.put("dayInfo", dayInfo);
            jsonObject.put("EMP_ID", Id);
            jsonObject.put("DATE_TIME", dt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_EditTimesheet_AWO_SWO, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    public void getCompanyInfo(final Context context, String urlskyline) {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, urlskyline,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                try {
                    parseJsonFeed(context, obj);
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    private void parseJsonFeed(Context context, JSONObject response) {

        try {
            JSONArray Array = response.getJSONArray("data");
            JSONObject jsonObject = Array.getJSONObject(0);
            JSONArray client_info = jsonObject.getJSONArray("client_info");
            JSONObject jsonObject1 = client_info.getJSONObject(0);
            String companyLogo = jsonObject1.getString("logo");
            String companyName = jsonObject1.getString("name");

            if (!companyLogo.equals("")) {
                String imagelogo = URL_EP1 + Api.API_COLLATERAL_PATH
                        + companyLogo;
                Shared_Preference.setCLIENT_NAME(context, companyName);
                Shared_Preference.setCLIENT_IMAGE_LOGO_URL(context, imagelogo);
            } else {
                Shared_Preference.setCLIENT_NAME(context, companyName);
                Shared_Preference.setCLIENT_IMAGE_LOGO_URL(context, "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int GetPausedJob() {
        pausedJobList.clear();
        String ID = Shared_Preference.getLOGIN_USER_ID(context);
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = SOAP_API_Client.BASE_URL;
        final String METHOD_NAME = Api.API_GetPauseLIst_AWO_SWO;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("techID", ID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String SWO_NAME = jsonObject1.getString("Awo_Swo_Name");//
                    String swo_id = jsonObject1.getString("Awo_Swo_Id");//
                    String cl_Id = jsonObject1.getString("Client_id");//
                    String jobid = jsonObject1.getString("Jobid");//
                    String jobName = jsonObject1.getString("Jobname");//
                    String compName = jsonObject1.getString("comp");//
                    String jobDesc = jsonObject1.getString("txt_Jdes");//
                    String SWO_Status_new = jsonObject1.getString("Swo_AWO_Status_new");//
                    String random_no = jsonObject1.getString("random_no");//
                    String Typeof_AWO_SWO = jsonObject1.getString("Typeof_AWO_SWO");//
                    pausedJobList.add(new PausedJob(jobName, jobid, cl_Id, swo_id, SWO_NAME, compName, jobDesc, SWO_Status_new, random_no, Typeof_AWO_SWO));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pausedJobList.size();
    }

    public void GetEmployeeRoles() {
        list_Employee_Role.clear();
        String ID = Shared_Preference.getLOGIN_USER_ID(context);
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = SOAP_API_Client.BASE_URL;
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetEmployee_Roles;
        final String METHOD_NAME = API_GetEmployee_Roles;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("ID", ID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String CateID = jsonObject1.getString("CateID");
                    list_Employee_Role.add(CateID);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dialog_App_Updated(String text, boolean icon_OK) {
        final Dialog showd = new Dialog(context, R.style.Theme_Dialog);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_app_updated);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(true);
        ImageView close = showd.findViewById(R.id.close);
        TextView tv_ver = showd.findViewById(R.id.tv_ver);
        ImageView imgvw_ok = showd.findViewById(R.id.imgvw_ok);
        if (icon_OK) {
            imgvw_ok.setImageResource(R.drawable.verified);
        } else {
            imgvw_ok.setImageResource(R.drawable.cancel);
        }

        tv_ver.setText(text);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();
            }
        });
        imgvw_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();
            }
        });

        showd.show();
    }

    private void dialog_show_Paused_Job_List() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pause_job_list_new, null);
        dialogBuilder.setView(dialogView);

        alertDialog_Paused = dialogBuilder.create();
        alertDialog_Paused.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog_Paused.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog_Paused.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        try {
            if (!alertDialog_Paused.isShowing()) {
                alertDialog_Paused.show();
            }
        } catch (Exception e) {
            e.getMessage();
        }

        ListView listView = (ListView) alertDialog_Paused.findViewById(R.id.listView);
        PausedJob_ListAdapter adapter = new PausedJob_ListAdapter(context, pausedJobList);
        listView.setAdapter(adapter);
        Button skip = (Button) alertDialog_Paused.findViewById(R.id.btn_skip);
        skip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    alertDialog_Paused.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                //  GoTo_Select_CompJob_Activity();
                dialog_show_TimesheetEnterOption();

            }
        });

    }

    private void dialog_show_TimesheetEnterOption() {
        if (list_Employee_Role.contains(Utility.USER_ROLE_ARTIST) || list_Employee_Role.contains(Utility.USER_ROLE_APC)) {
            Shared_Preference.set_EnterTimesheetByAWO(context, true); // awo
        }
        if (list_Employee_Role.contains(Utility.USER_ROLE_TECH) || list_Employee_Role.contains(Utility.USER_ROLE_SPC)) {
            Shared_Preference.set_EnterTimesheetByAWO(context, false); //swo
        }

        if (list_Employee_Role.contains(Utility.USER_ROLE_DC) || list_Employee_Role.contains(Utility.USER_ROLE_PM)
                || list_Employee_Role.contains(Utility.USER_ROLE_SUPER_ADMIN)) {
            Dialog_Choose_Work_From_SWO_AWO();//both
        } else if ((list_Employee_Role.contains(Utility.USER_ROLE_ARTIST) || list_Employee_Role.contains(Utility.USER_ROLE_APC))
                && (list_Employee_Role.contains(Utility.USER_ROLE_TECH) || list_Employee_Role.contains(Utility.USER_ROLE_SPC))) {
            Dialog_Choose_Work_From_SWO_AWO(); //both
        } else {
            GoTo_Select_CompJob_Activity();
        }

    }

    public void PrepareDataForPausedJob(Context context, String Comp_ID, String Comp_Name, String swo_id, String job_id, String jobName_forPaused, String strPausedTimesheetID) {
        try {
            alertDialog_Paused.dismiss();
        } catch (Exception e) {
            e.getMessage();
        }

        Shared_Preference.setIS_STARTING_BILLABLE_JOB(context, true);
        Shared_Preference.setPAUSED_TIMESHEET_ID(context, strPausedTimesheetID);
        Shared_Preference.setJOB_ID_FOR_JOBFILES(context, job_id);
        Shared_Preference.setJOB_NAME_BILLABLE(context, jobName_forPaused);
        Shared_Preference.setCLIENT_NAME(context, Comp_Name);
        String nextact = URL_EP1 + Api.API_CRATES_LIST + Comp_ID;
        Shared_Preference.setLINK(context, nextact);
        Shared_Preference.setSWO_ID(context, swo_id);
        Shared_Preference.setCOMPANY_ID_BILLABLE(context, Comp_ID);

        getCompanyInfo(context, nextact);
        startClockForPaused(context);
    }

    public int getBillableCodes_new() {
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = SOAP_API_Client.BASE_URL;
        final String METHOD_NAME = Api.API_BILLABLE_NONBILLABLE_CODE;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;

        String dealerId = Shared_Preference.getDEALER_ID(this);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("dealerID", dealerId);
        request.addProperty("cat", userRole);
        request.addProperty("type", "1");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        // int len = 0;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            JSONArray jArray = new JSONArray(receivedString);
            //  len = jArray.length();
            BillableLaborCodesLength = jArray.length();
        } catch (Exception e) {
            e.getMessage();
        }
        return BillableLaborCodesLength;
    }

    public void SetAuthenticationVariables(String ApiResponse) {
        try {
            JSONObject jsonObject = new JSONObject(ApiResponse);
            JSONArray jArray = jsonObject.getJSONArray("SubMenu");
            JSONArray MainMenu_Array = jsonObject.getJSONArray("MainMenu");
            if (jArray == null || jArray.length() == 0) return;
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject1 = jArray.getJSONObject(i);
                String SubMenuId = jsonObject1.getString("SubMenuId");
                String Authentication = jsonObject1.getString("Status");

                if (SubMenuId.equals(Utility.MISByDailyTime)) {
                    if (Authentication.equals("1")) {
                        CanEnter_MISByDailyTime = true;
                    } else {
                        CanEnter_MISByDailyTime = false;
                    }
                } else if (SubMenuId.equals(Utility.EditTime)) {
                    if (Authentication.equals("1")) {
                        Can_EditTime = true;
                    } else {
                        Can_EditTime = false;
                    }
                } else if (SubMenuId.equals(Utility.EnterTime)) {
                    String AuthenticationForTimeSheet = jsonObject1.getString("IsApp");
                    if (AuthenticationForTimeSheet.equals("1")) {
                        CanEnter_BillableTime = true;
                    } else {
                        CanEnter_BillableTime = false;
                    }
                } else if (SubMenuId.equals(Utility.ChangeOrder)) {
                    if (Authentication.equals("1")) {
                        CanEnter_DamageReport = true;
                    } else {
                        CanEnter_DamageReport = false;
                    }
                } else if (SubMenuId.equals(Utility.UsageCharge)) {
                    if (Authentication.equals("1")) {
                        CanEnter_UsageCharge = true;
                    } else {
                        CanEnter_UsageCharge = false;
                    }
                } else if (SubMenuId.equals(Utility.Client_Art)) {
                    if (Authentication.equals("1")) {
                        CanUpload_ClientArt = true;
                    } else {
                        CanUpload_ClientArt = false;
                    }
                } else if (SubMenuId.equals(Utility.Client_Photo)) {
                    if (Authentication.equals("1")) {
                        CanUpload_ClientPhoto = true;
                    } else {
                        CanUpload_ClientPhoto = false;
                    }
                }
            }


            for (int i = 0; i < MainMenu_Array.length(); i++) {

                JSONObject jsonObject1 = MainMenu_Array.getJSONObject(i);
                String MainId = jsonObject1.getString("MainId");
                String IsMainCheck = jsonObject1.getString("IsMainCheck");
                if (MainId.equals("7")) {//"Time Tracking",
                    if (IsMainCheck.equals("1")) {
                        CanView_BillableTime = true;
                        CanView_MISByDailyTime = true;
                        CanView_EditTime = true;
                    } else {
                        CanView_BillableTime = false;
                        CanView_MISByDailyTime = false;
                        CanView_EditTime = false;
                    }
                }
                if (MainId.equals("11")) { //other-damage report
                    if (IsMainCheck.equals("1")) {
                        CanView_ChangeOrder = true;
                    } else {
                        CanView_ChangeOrder = false;
                    }
                }

                if (MainId.equals("6")) { //Service-Usage Charge
                    if (IsMainCheck.equals("1")) {
                        CanView_UsageCharge = true;
                    } else {
                        CanView_UsageCharge = false;
                    }
                }

                if (MainId.equals("5")) { //Art-Upload Photo
                    if (IsMainCheck.equals("1")) {
                        CanView_ClientArt = true;
                    } else {
                        CanView_ClientArt = false;
                    }
                }


            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void dialog_NoJobCodes() {
        final Dialog showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        Button Btn_No = (Button) showd.findViewById(R.id.Btn_No);
        Btn_No.setVisibility(View.GONE);
        Button Btn_Yes = (Button) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView txt_message = (TextView) showd.findViewById(R.id.texrtdesc);
        txt_message.setText("There are NO Job Codes currently in the system, you cannot perform this action!\nPlease ask your admin to add Job Codes.");
        //  txt_message.setText(Utility.NO_INTERNET);
        Btn_Yes.setText("Ok");

        Btn_Yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();

                } catch (Exception e) {

                }

            }
        });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void checkWifi() {
        boolean isWifi = Utility.isWiFiConnected(context);
        if (!isWifi) {
            if (!wifiManager.isWifiEnabled()) { // If wifi disabled then enable it
                dialog_TurnOnWifi();
            } else {
                dialog_ConnectWifi();
            }
        } else {
            CheckVersion();
        }

    }

    private void EnableWifi() {
        wifiManager.setWifiEnabled(true);
        showprogressdialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                hideprogressdialog();
                boolean isWifi = Utility.isWiFiConnected(context);
                if (!isWifi) {
                    dialog_ConnectWifi();
                } else {
                    CheckVersion();
                }
            }
        }, 7000);   //3 seconds

    }

    public void dialog_ConnectWifi() {

        final Dialog showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.wifi_alert);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);

        tv_title.setText("No saved connection!");
        tv_msg.setText("Connect to Wi-Fi");

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), WIFI_SETTING_REQUEST_CODE);

            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void dialog_TurnOnWifi() {

        final Dialog showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.wifi_alert);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);

        tv_title.setText("Attention!");
        tv_msg.setText("Turn on Wi-Fi");

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                EnableWifi();
            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void dialog_TurnOnMobileData() {

        final Dialog showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.wifi_alert);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);


        tv_title.setText("Wi-Fi connection was unsuccessful!");
        tv_msg.setText("Please turn mobile data ON");

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                startActivityForResult(new Intent(Settings.ACTION_SETTINGS), MOBILE_DATA_SETTING_REQUEST_CODE);

            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void dialog_MobileDataNotTrunedON() {

        final Dialog showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.wifi_alert);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);


        tv_title.setText("Mobile data not turned ON!");
        tv_msg.setText("No Internet connection");

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                //  finish();

            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void showClock(Context context) {

        boolean isSTARTING_BILLABLE_JOB = Shared_Preference.getIS_STARTING_BILLABLE_JOB(context);
        if (isSTARTING_BILLABLE_JOB) {
            showChatHead_Billable(context, true);
        } else {
            showChatHead_Admin(context, true);
        }


    }

    private void RunIncompleteTask(String api_name, JSONObject api_input_jsonObject) {


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = SOAP_API_Client.BASE_URL;
        final String SOAP_ACTION = KEY_NAMESPACE + api_name;
        final String METHOD_NAME = api_name;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


        Iterator<?> keys = api_input_jsonObject.keys();
        while (keys.hasNext()) {
            try {
                String key = (String) keys.next();
                String val = api_input_jsonObject.getString(key);
                request.addProperty(key, val);

            } catch (Exception e) {
                e.getCause();
            }
        }
        Log.e("IncompleteAsync--->", "ApiRunning");
        Log.e("Api Data", request.toString());


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String receivedString = SoapPrimitiveresult.toString();
            JSONObject jsonObject = new JSONObject(receivedString);
            String TimeSheetID = jsonObject.getString("cds");
            if (TimeSheetID.contains("=")) {
                TimeSheetID = TimeSheetID.substring(TimeSheetID.indexOf("=") + 1);
            }
            Shared_Preference.setTIME_SHEET_ID(this, TimeSheetID);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void SavedTask() {
        JSONArray jsonArray = new JSONArray();

        if (Shared_Preference.containsKey(this, Shared_Preference.KEY_INCOMPLETE_ASYNC_ARRAY)) {
            String IncompleteAsyncArray = Shared_Preference.getINCOMPLETE_ASYNC_ARRAY(context);
            try {
                jsonArray = new JSONArray(IncompleteAsyncArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String str_apiId = jsonObject1.getString("apiId");
                    String api_name = jsonObject1.getString("api_name");
                    JSONObject jsonObject = jsonObject1.getJSONObject("apiInput");

                    Utility.removeIncompleteAsynctask(context, str_apiId);

                    RunIncompleteTask(api_name, jsonObject);

                }

            } catch (Exception e) {
                e.getCause();
            }

        }


    }

    void SavedTimeSheetDialog() {

        ArrayList<SavedTask> list = Utility.getOfflineTaskList(context);

        if (list.size() > 0) {
            final Dialog dialog1 = new Dialog(context);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.offline_timesheet_list);
            dialog1.setCancelable(false);
            ListView listView = (ListView) dialog1.findViewById(R.id.listView);

            SavedTimeSheet_ListAdapter adapter = new SavedTimeSheet_ListAdapter(context, list);
            listView.setAdapter(adapter);

            Button btn_Submit = (Button) dialog1.findViewById(R.id.btn_Submit);
            Button btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);

            btn_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        dialog1.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            });

            btn_Submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        dialog1.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    if (new ConnectionDetector(context).isConnectingToInternet()) {
                        new async_RunIncompleteTask().execute();
                    }
                }
            });

            try {
                dialog1.show();

            } catch (Exception e) {
                e.getMessage();
            }


        } else {
            if (Utility.isOverlapTimesheetExist(context)) {
                dialog_OverlapDataFound();

            }
        }


    }

    private void dialog_OverlapDataFound() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
        dialogView.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);

        final TextView desc = dialogView.findViewById(R.id.texrtdesc);
        final TextView title = dialogView.findViewById(R.id.textView1rr);


        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        close.setVisibility(View.INVISIBLE);
        // dialogBuilder.setTitle("Device Details");
        title.setText("Overlap time sheet data found!");
        desc.setText("Do you want to edit time sheet now?");
        // message.setText("Please set Automatic date and Time");

        positiveBtn.setText("Yes");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent i = new Intent(getApplicationContext(), TimeSheetList1Activity.class);
                startActivity(i);

            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();


            }
        });
        try {
            alertDialog = dialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void Dialog_show_SWO_List() {
        swo_dialog = new Dialog(context);
        swo_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        swo_dialog.setContentView(R.layout.swo_list_dialog_new);

        swo_dialog.setCancelable(false);


        final ListView listView = (ListView) swo_dialog.findViewById(R.id.listView);
        final Button backToJobSelect = (Button) swo_dialog.findViewById(R.id.btn_back);
        final TextView _text_noJObs = (TextView) swo_dialog.findViewById(R.id._text_noJObs);
        _text_noJObs.setVisibility(View.GONE);

        if (list_SWO.size() > 0) {
            _text_noJObs.setVisibility(View.GONE);
        } else {
            _text_noJObs.setVisibility(View.VISIBLE);
        }


        backToJobSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    swo_dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


            }
        });

        ArrayAdapter<SWO_Details> stringArrayAdapter = new ArrayAdapter<SWO_Details>(context, android.R.layout.simple_list_item_1, list_SWO);
        listView.setAdapter(stringArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String swo_id = list_SWO.get(i).getSwo_id();   ///////develop by aman kaushik
                final String job_id = list_SWO.get(i).getJOB_ID();
                prepareDataForClock(context, Comp_ID, swo_id, job_id);

                try {
                    swo_dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });
        try {
            swo_dialog.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void prepareDataForClock(Context context, String CompId, String swoID, String Job_id) {
        String nextact = URL_EP1 + Api.API_CRATES_LIST
                + CompId;
        Shared_Preference.setLINK(context, nextact);
        Shared_Preference.setSWO_ID(this, swoID);
        Shared_Preference.setJOB_ID_FOR_JOBFILES(context, Job_id);
        Shared_Preference.setCOMPANY_ID_BILLABLE(context, CompId);
        getCompanyInfo(context, nextact);
        startClockForBillable(context);
    }

    private void Dialog_show_AWO_List() {
        swo_dialog = new Dialog(context);
        swo_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        swo_dialog.setContentView(R.layout.swo_list_dialog_new);

        swo_dialog.setCancelable(false);

        final ListView listView = (ListView) swo_dialog.findViewById(R.id.listView);
        final Button backToJobSelect = (Button) swo_dialog.findViewById(R.id.btn_back);
        final TextView _text_noJObs = (TextView) swo_dialog.findViewById(R.id._text_noJObs);
        final TextView title = (TextView) swo_dialog.findViewById(R.id.title);
        title.setText("AWO(s) List");
        _text_noJObs.setVisibility(View.GONE);

        if (list_Awo.size() > 0) {
            _text_noJObs.setVisibility(View.GONE);
        } else {
            _text_noJObs.setVisibility(View.VISIBLE);
        }


        backToJobSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    swo_dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


            }
        });

        ArrayAdapter<Awo> stringArrayAdapter = new ArrayAdapter<Awo>(context, android.R.layout.simple_list_item_1, list_Awo);
        listView.setAdapter(stringArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    swo_dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                String SelectedAwo = list_Awo.get(i).getiDPK();
                String jobID = list_Awo.get(i).getjOBID();
                prepareDataForClock(context, Comp_ID, SelectedAwo, jobID);

            }
        });
        try {
            swo_dialog.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }


    private void dialog_UpdateAvailable() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
        dialogView.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);
        final TextView title = dialogView.findViewById(R.id.textView1rr);
        final TextView message = dialogView.findViewById(R.id.texrtdesc);
        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        close.setVisibility(View.INVISIBLE);
        // dialogBuilder.setTitle("Device Details");
        title.setText("New Update Available");
        message.setText("Please update to the latest version!");
        positiveBtn.setText("Update");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                Shared_Preference.setAppUpdateChecked(context);
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                finish();
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.getCause();
        }

    }

    private void setModuleClickedFalse() {
        DamageReportModuleClicked = false;
        AdminTimesheetModuleClicked = false;
        UsageChargeModuleClicked = false;
        UploadPhotoModuleClicked = false;
        AdminTimesheetModuleClicked = false;
        BillableJobModuleClicked = false;

    }

    private void CallAPI_CheckPermission() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", Shared_Preference.getLOGIN_USER_ID(this));
            jsonObject.put("Role", userRole);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean showProgress;
        if (BillableJobModuleClicked || AdminTimesheetModuleClicked) showProgress = false;
        else showProgress = true;

        if (new ConnectionDetector(this).isConnectingToInternet()) {
            new MyAsyncTask(this, showProgress, this, Api.API_getTimesheetAuth, jsonObject).execute();
        } else {
            Toast.makeText(this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    private void CallAPI_FetchBillableNonBillableCodes() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dealerID", Shared_Preference.getDEALER_ID(this));
            jsonObject.put("cat", userRole);
            jsonObject.put("type", "2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(this).isConnectingToInternet()) {
            new MyAsyncTask(this, false, this, Api.API_BILLABLE_NONBILLABLE_CODE, jsonObject).execute();
        } else {
            Toast.makeText(this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    private void start_JobFilesActivity() {
        Intent in = new Intent(context, JobFilesTabActivity.class);
        startActivity(in);
    }

    public void Dialog_Choose_Work_From_SWO_AWO() {
        final Dialog showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_swo_awo);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);

        Button btn_swo = (Button) showd.findViewById(R.id.btn_swo);
        Button btn_awo = (Button) showd.findViewById(R.id.btn_awo);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        showd.show();
        btn_swo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();

                    Shared_Preference.set_EnterTimesheetByAWO(context, false);
                    GoTo_Select_CompJob_Activity();
                } catch (Exception e) {

                }

            }
        });
        btn_awo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();
                    Shared_Preference.set_EnterTimesheetByAWO(context, true);
                    GoTo_Select_CompJob_Activity();
                } catch (Exception e) {

                }

            }
        });
        close.setOnClickListener(new OnClickListener() {
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

    private void CallAPI_FetchClockInClockOutCodes() {
        JSONObject jsonObject = new JSONObject();
        try {
            String dealerId = Shared_Preference.getDEALER_ID(this);
            jsonObject.put("dealerID", dealerId);//nks
            if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
                jsonObject.put("type", Utility.TYPE_AWO);//AWO
            } else {
                jsonObject.put("type", Utility.TYPE_SWO);//SWO
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(this).isConnectingToInternet()) {
            new MyAsyncTask(this, false, this, Api.API_bindclock_swo_awo_status, jsonObject).execute();
        } else {
            Toast.makeText(this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void handleResponse(String responseString, String api) {
        if (api.equalsIgnoreCase(Api.API_getTimesheetAuth)) {
            SetAuthenticationVariables(responseString);

            if (DamageReportModuleClicked) {
                if (CanEnter_DamageReport && CanView_ChangeOrder) {
                    Intent ii = new Intent(context, DamageReportNew.class);
                    startActivity(ii);
                } else {
                    Toast.makeText(context, "You do not have rights to access Damage Report!", Toast.LENGTH_SHORT).show();
                }
            } else if (UsageChargeModuleClicked) {

                if (CanEnter_UsageCharge && CanView_UsageCharge) {
                    startActivity(new Intent(getApplicationContext(), UsageChargesListActivity.class));
                } else {
                    Toast.makeText(context, "You do not have rights to access Usage Charges!", Toast.LENGTH_SHORT).show();
                }
            } else if (UploadPhotoModuleClicked) {
                if ((CanView_ClientArt && CanUpload_ClientArt) || (CanView_UsageCharge && CanUpload_ClientPhoto)) {
                    startActivity(new Intent(context, UploadPhotosActivity.class));
                    finish();
                } else {
                    Toast.makeText(context, "You do not have rights to Upload Photos!", Toast.LENGTH_SHORT).show();
                }

            } else if (BillableJobModuleClicked) {
                if (!CanEnter_BillableTime || !CanView_BillableTime) {
                    Toast.makeText(context, "You do not have rights to Enter Time Sheet via App!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new async_get_BillableCodes_PausedJobList().execute();
                }
            } else if (AdminTimesheetModuleClicked) {
                if (!CanEnter_MISByDailyTime || !CanView_MISByDailyTime) {
                    Toast.makeText(context, "You do not have rights to enter Admin Time Sheet!", Toast.LENGTH_SHORT).show();
                } else {
                    CallAPI_FetchBillableNonBillableCodes();
                }

            }

            setModuleClickedFalse();//should be in last
        } else if (api.equalsIgnoreCase(Api.API_EditTimesheet_AWO_SWO)) {
            try {
                JSONArray jsonArray = new JSONArray(responseString);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String output = jsonObject.getString("overlapp");
                String msg = jsonObject.getString("msg");
            } catch (Exception e) {
                e.getCause();
            }
        } else if (api.equalsIgnoreCase(Api.API_BILLABLE_NONBILLABLE_CODE)) {
            hideprogressdialog();
            int NonBillableLaborCodesLength = 0;
            try {
                Shared_Preference.setNON_BILLABLE_CODES(context, responseString);
                JSONArray jArray = new JSONArray(responseString);
                NonBillableLaborCodesLength = jArray.length();
            } catch (Exception e) {
                e.getMessage();
            }
            if (NonBillableLaborCodesLength == 0) {
                dialog_NoJobCodes();
            } else {
                startClockForAdmin(this);
            }

        } else if (api.equalsIgnoreCase(Api.API_bindclock_swo_awo_status)) {
            try {
                JSONObject jsonObject1 = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject1.getJSONArray("cds");
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                CLOCK_IN_BILLABLE_CODE = jsonObject.getString("ClockIn");
                CLOCK_OUT_BILLABLE_CODE = jsonObject.getString("ClockOut");

                if (Shared_Preference.get_EnterTimesheetByAWO(context)) { //AWO
                    IN_PROGRESS_SWO_AWO_STATUS = jsonObject.getString("AWOStatus");
                } else {  //SWO
                    IN_PROGRESS_SWO_AWO_STATUS = jsonObject.getString("SWOStatus");
                }
            } catch (Exception e) {
                e.getMessage();
            }

            CallAPI_SubmitClockInClockOut();
        } else if (api.equalsIgnoreCase(Api.API_BILLABLE_TIMESHEET)) {
            String output = "", msg = "";
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                output = jsonObject.getString("Output");
            } catch (Exception e) {
                e.getCause();
            }
            String jobName = Shared_Preference.getJOB_NAME_BILLABLE(context);
            if (output.equals("1")) {   //means sheet submitted successfully

                if (jobName.equalsIgnoreCase(Utility.CLOCK_IN)) {
                    dialog_App_Updated("You’ve clocked in!", true);
                } else if (jobName.equalsIgnoreCase(Utility.CLOCK_OUT)) {
                    dialog_App_Updated("You’ve clocked out!", true);
                }

            } else {
                if (jobName.equalsIgnoreCase(Utility.CLOCK_IN)) {
                    msg = "Clock in failed!";
                } else if (jobName.equalsIgnoreCase(Utility.CLOCK_OUT)) {
                    msg = "Clock out failed!";
                }
                dialog_App_Updated(msg, false);
            }
        } else if (api.equalsIgnoreCase(Api.API_GetVersion)) {
            Shared_Preference.setLatestVersion(context, responseString);
            Shared_Preference.setOldVersion(context);
            String nversionName = responseString;
            try {
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                String versionName = info.versionName;
                if (nversionName == null) {
                    nversionName = "0";
                }
                Double retuenvl = Double.parseDouble(nversionName);
                if (Double.parseDouble(versionName) < retuenvl) {
                    dialog_UpdateAvailable();
                }

            } catch (Exception err) {
                err.getMessage();
            }
            //  dialog_WorkLocationArrived();
            SavedTimeSheetDialog();

        }


    }

    private void CallAPI_SubmitClockInClockOut() {
        JSONObject jsonObject = new JSONObject();
        try {
            String JOB_STOP_DateTime = Utility.getCurrentTimeString();//dd-MM-yyyy HH:mm:ss
            String arr[] = JOB_STOP_DateTime.split(" ");
            String str = arr[1];
            String JOB_STOP_HrsMinuts = str.substring(0, str.lastIndexOf(":"));//HH:mm

            String jobName = Shared_Preference.getJOB_NAME_BILLABLE(context);
            String imei = "";
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                imei = telephonyManager.getDeviceId();
            } catch (SecurityException e) {
                e.getCause();
            }
            String Code = "";
            if (jobName.equalsIgnoreCase(Utility.CLOCK_IN)) {
                Code = CLOCK_IN_BILLABLE_CODE;
            } else if (jobName.equalsIgnoreCase(Utility.CLOCK_OUT)) {
                Code = CLOCK_OUT_BILLABLE_CODE;
            }
            jsonObject.put("tech_id", Shared_Preference.getLOGIN_USER_ID(this));
            jsonObject.put("swo_id", Shared_Preference.getSWO_ID(this));
            jsonObject.put("start_time", JOB_STOP_HrsMinuts);
            jsonObject.put("end_time", JOB_STOP_HrsMinuts);
            jsonObject.put("description", jobName);
            jsonObject.put("code", Code);
            jsonObject.put("dayInfo", "0");
            jsonObject.put("status", "2018");
            jsonObject.put("region", jobName);
            jsonObject.put("PhoneType", "Android");
            jsonObject.put("EMI", imei);
            jsonObject.put("SWOstatus", IN_PROGRESS_SWO_AWO_STATUS);
            jsonObject.put("jobID", Shared_Preference.getJOB_ID_FOR_JOBFILES(this));
            jsonObject.put("PauseTimeSheetID", "0");

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(this).isConnectingToInternet()) {
            new MyAsyncTask(this, false, this, Api.API_BILLABLE_TIMESHEET, jsonObject).execute();
        } else {
            Toast.makeText(this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    private void CheckVersion() {
        JSONObject jsonObject = new JSONObject();
        try {
            final String appPackageName = getPackageName();
            String address = "https://play.google.com/store/apps/details?id=" + appPackageName;
            jsonObject.put("address", address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new MyAsyncTask(this, true, this, Api.API_GetVersion, jsonObject).execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }


    private class async_RunIncompleteTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            showprogressdialog();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SavedTask();
            return null;
        }

        @Override
        protected void onPostExecute(Void integer) {
            hideprogressdialog();
            Toast.makeText(context, "Time sheet(s) submitted successfully!", Toast.LENGTH_SHORT).show();
            if (Utility.isOverlapTimesheetExist(context)) {
                dialog_OverlapDataFound();
            }
        }


    }

    private class async_get_BillableCodes_PausedJobList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            //showprogressdialog();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            getBillableCodes_new();
            GetPausedJob();
            GetEmployeeRoles();
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            hideprogressdialog();

            if (BillableLaborCodesLength == 0) {
                dialog_NoJobCodes();
                return;
            }
            if (pausedJobList != null && !pausedJobList.isEmpty()) {
                dialog_show_Paused_Job_List();
            } else {
                dialog_show_TimesheetEnterOption();


            }


        }


    }


}




