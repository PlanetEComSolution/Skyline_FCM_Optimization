package planet.info.skyline.tech.billable_timesheet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.model.SWO_Status;
import planet.info.skyline.network.Api;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.tech.update_timesheet.TimeSheetList1Activity;
import planet.info.skyline.util.AppConstants;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.util.Utility.LOADING_TEXT;


public class SubmitTimesheetNew extends BaseActivity {

    String descriptionm;
    String laborCode;
    SoapObject request_new;
    String JOB_START_DateTime;//dd-MM-yyyy HH:mm:ss
    String JOB_STOP_DateTime;//dd-MM-yyyy HH:mm:ss
    AlertDialog alertDialog;
    String Swo_Id = "";
    String dayInfo = "0";
    String reason = "";
    String JOB_START_HrsMinuts = "", JOB_STOP_HrsMinuts = "";
    String status = "";
    String jobType = "";

    String updatedSwoStatus = "", oldSwoStatus = "", Swo_Status = "";
    boolean resetClock = false;
    String userRole = "";
    String JobIdBillable = "";

    Context context;
    ProgressHUD mProgressHUD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scanforworkstation);
        context = SubmitTimesheetNew.this;
        userRole = Shared_Preference.getUSER_ROLE(this);
        Swo_Id = Shared_Preference.getSWO_ID(context);
        JobIdBillable = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(AppConstants.TYPE)) {
            status = bundle.getString(AppConstants.TYPE);
        }

        if(Shared_Preference.get_JobStartedFromMyTask(context)
                && !Shared_Preference.get_Task_LaborId(context).equals("")){
            laborCode=Shared_Preference.get_Task_LaborId(context);
            chooseSwoStatus();
        }else {
            chooseLaborCode();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void chooseLaborCode() {
        Intent i = new Intent(context, LaborCodesActivity.class);
        startActivityForResult(i, Utility.LABOR_CODE_UPDATE_REQUEST_CODE);
    }

    private void chooseSwoStatus() {
        Intent i = new Intent(context, SWO_Status_Activity.class);
        startActivityForResult(i, Utility.SWO_STATUS_UPDATE_REQUEST_CODE);
    }


    public void Dialog_EnterDesc() {
        final Dialog showd1 = new Dialog(context);
        showd1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd1.setContentView(R.layout.enterdescriptiondata_new);
        showd1.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        showd1.setCancelable(false);
        try {
            showd1.show();
        } catch (Exception e) {
            e.getMessage();
        }
        final EditText ed1 = (EditText) showd1.findViewById(R.id.texrtdesc);
        TextView no = (TextView) showd1.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) showd1.findViewById(R.id.close);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd1.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                Intent in = new Intent(context, MainActivity.class);
                startActivity(in);

                finish();


            }
        });

        no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // if (Utility.isTimeAutomatic(SubmitTimesheet.this)) {
                if (ed1.getText().length() < 1) {
                    Toast.makeText(context, "Please enter description   ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    descriptionm = ed1.getText().toString().trim();
                    Utility.hideKeyboard(SubmitTimesheetNew.this);
                    try {
                        showd1.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    String Total_time = Utility.get_TotalClockTime(context);

                    /**************************************************************/
                     /*  status = "1";//stop work
                        status = "0";//btn_PauseWork work
                       status = "3";//finish work
                        status = "2018"; // changeTimeCode
                        */
                    if (oldSwoStatus.equals(updatedSwoStatus)) {
                        Swo_Status = "0";
                    } else {
                        Swo_Status = updatedSwoStatus;
                    }
                    reason = descriptionm + "  " + "(" + Total_time + ")";
                    /*****************************************************************/
                    if (status.equals(AppConstants.CHANGE_TIME_CODE)) {      //Change Time Code
                        jobType = Utility.CHANGE_TIME_CODE;
                        resetClock = true;
                        status = "2018";
                    } else {                   // Pause , Stop  , Finish
                        jobType = Utility.BILLABLE;
                        resetClock = false;
                    }
                    PrepareApiData();
                }
            }

        });
    }


    public String Submit_Billable_TimeSheetNew() {  ///use this fo billable
        String receivedString = "";

        final String URL = SOAP_API_Client.BASE_URL;
        final String SOAP_ACTION = KEY_NAMESPACE + Utility.Method_BillableTimeSheet;


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request_new);
        Log.e("billable time url---", request_new.toString());

        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONObject jsonObject = new JSONObject(receivedString);
            //  {"ID":"0","Output":"0","Message":"Overlapping Time entries"}
            String timesheetID = jsonObject.getString("ID");
            Shared_Preference.setTIME_SHEET_ID(this, timesheetID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedString;
    }

    private void PrepareApiData() {
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            e.getCause();
        }

        final String NAMESPACE = KEY_NAMESPACE;
        String clientid = Shared_Preference.getLOGIN_USER_ID(context);
        String PAUSED_TIMESHEET_ID = Shared_Preference.getPAUSED_TIMESHEET_ID(context);
/************************************************************************************/
        JOB_START_DateTime = Shared_Preference.getCLOCK_START_TIME(this);//dd-MM-yyyy HH:mm:ss
        JOB_STOP_DateTime = Utility.getCurrentTimeString();//dd-MM-yyyy HH:mm:ss

        boolean bool_IsSameDay = Utility.IsSameDay(JOB_START_DateTime, JOB_STOP_DateTime);

        if (bool_IsSameDay) dayInfo = "0";
        else dayInfo = "1";

        String arr1[] = JOB_START_DateTime.split(" ");
        String str1 = arr1[1];
        JOB_START_HrsMinuts = str1.substring(0, str1.lastIndexOf(":"));//HH:mm

        String arr[] = JOB_STOP_DateTime.split(" ");
        String str = arr[1];
        JOB_STOP_HrsMinuts = str.substring(0, str.lastIndexOf(":"));//HH:mm

        if (JOB_STOP_HrsMinuts.equals("00:00")) {
            dayInfo = "1";
        }


/************************************************************************************/

        final String METHOD_NAME3 = Utility.Method_BillableTimeSheet;
        request_new = new SoapObject(NAMESPACE, METHOD_NAME3);
        request_new.addProperty("tech_id", clientid);
        request_new.addProperty("swo_id", Swo_Id);
        request_new.addProperty("start_time", JOB_START_HrsMinuts);
        request_new.addProperty("end_time", JOB_STOP_HrsMinuts);
        request_new.addProperty("description", descriptionm);
        request_new.addProperty("code", laborCode);
        request_new.addProperty("dayInfo", dayInfo);
        request_new.addProperty("status", status);
        request_new.addProperty("region", reason);
        request_new.addProperty("PhoneType", "Android");
        request_new.addProperty("EMI", imei);
        request_new.addProperty("SWOstatus", Swo_Status);
        request_new.addProperty("jobID", JobIdBillable);
        request_new.addProperty("PauseTimeSheetID", PAUSED_TIMESHEET_ID);
        if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
            request_new.addProperty("Type", Utility.TYPE_AWO);
        } else {
            request_new.addProperty("Type", Utility.TYPE_SWO);
        }

/**************************************************************************************/

        if (new ConnectionDetector(context).isConnectingToInternet()) {
            if (!resetClock) {
                Utility.StopRunningClock(context);
            }
            Shared_Preference.setCLIENT_IMAGE_LOGO_URL(this, "");
          //  Shared_Preference.setCLIENT(this, "");
            Shared_Preference.setCLIENT_NAME(this, "");
            new Async_Submit_Billable_Timesheet_New().execute();

        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            showDialog_for_OfflineApiDataSave(context, request_new.toString(), Utility.getUniqueId());
        }
    }

    public void showDialog_for_OfflineApiDataSave(final Context context, final String api_input, final String unique_apiId) {

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
        final TextView Btn_Cancel = (TextView) showd.findViewById(R.id.Btn_Select_Another);
        Btn_Done.setText("OK");
        Btn_Cancel.setText("Cancel");

        tv_title.setText("Lost Internet!");
        tv_title.setVisibility(View.GONE);


        tv_msg.setText(Utility.MSG_OFFLINE_DATA_SAVE);
        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Utility.StopRunningClock(context);
                Shared_Preference.setCLIENT_IMAGE_LOGO_URL(context, "");
               // Shared_Preference.setCLIENT(context, "");
                Shared_Preference.setCLIENT_NAME(context, "");
                Utility.saveOfflineIncompleteAsynctask(context, api_input, unique_apiId, "Billable");
                Toast.makeText(context, Utility.SAVED, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context, MainActivity.class));
                finish();

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


                onBackPressed();

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


                onBackPressed();
            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void dialog_AutoDateTimeSet() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
        dialogView.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);
        final TextView title = dialogView.findViewById(R.id.texrtdesc);
        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        close.setVisibility(View.INVISIBLE);
        title.setText("Your phone date is inaccurate! \n \n Please set automatic date and time.");
        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
            }
        });
        negativeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void dialog_OverlappingTimeEntry() {
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

        title.setText("Overlapping Time Entry.");
        desc.setText("Edit the Entry!");

        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                String PAUSED_TIMESHEET_ID = Shared_Preference.getPAUSED_TIMESHEET_ID(context);
                Utility.saveOverlapTimeSheetData(context,
                        Swo_Id,
                        JOB_START_HrsMinuts,
                        JOB_STOP_HrsMinuts,
                        descriptionm,
                        laborCode,
                        reason,
                        status,
                        dayInfo,
                        jobType,
                        Shared_Preference.getJOB_NAME_BILLABLE(context), Swo_Status, JobIdBillable, PAUSED_TIMESHEET_ID);

                Intent i = new Intent(getApplicationContext(), TimeSheetList1Activity.class);
                startActivity(i);
                finish();

            }
        });
        negativeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                String PAUSED_TIMESHEET_ID = Shared_Preference.getPAUSED_TIMESHEET_ID(context);
                Utility.saveOverlapTimeSheetData(context,
                        Swo_Id,
                        JOB_START_HrsMinuts,
                        JOB_STOP_HrsMinuts,
                        descriptionm,
                        laborCode,
                        reason,
                        status,
                        dayInfo,
                        jobType,
                        Shared_Preference.getJOB_NAME_BILLABLE(context), Swo_Status, JobIdBillable, PAUSED_TIMESHEET_ID);


                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();


            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    public void AssignTechToUnassignedSwo() {
        String clientid = Shared_Preference.getLOGIN_USER_ID(context);
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = SOAP_API_Client.BASE_URL;
        final String METHOD_NAME = Api.API_SaveTech;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("SWOid", Swo_Id);
        request.addProperty("tech", clientid);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();

        } catch (Exception e) {

            e.printStackTrace();
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == RESULT_CANCELED) {
            if (requestCode == Utility.LABOR_CODE_UPDATE_REQUEST_CODE)
                Toast.makeText(context, "Please select labor code.", Toast.LENGTH_SHORT).show();
            if (requestCode == Utility.SWO_STATUS_UPDATE_REQUEST_CODE)
                Toast.makeText(context, "Please select swo status.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(context, Clock_Submit_Type_Activity.class));
            finish();
        } else if (resultCode == RESULT_OK) {
            if (requestCode == Utility.LABOR_CODE_UPDATE_REQUEST_CODE) {
                laborCode = intent.getStringExtra("updatedLaborCode");
                chooseSwoStatus();
            } else if (requestCode == Utility.SWO_STATUS_UPDATE_REQUEST_CODE) {
                oldSwoStatus = intent.getStringExtra("oldSwoStatus");
                updatedSwoStatus = intent.getStringExtra("updatedSwoStatus");
                Dialog_EnterDesc();
            }
        }

    }

    public class Async_Submit_Billable_Timesheet_New extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showprogressdialog();
        }

        @Override
        protected void onPostExecute(String result) {

            hideprogressdialog();

            JSONObject jsonObject = null;
            String output = "";
            String msg = "";
            try {
                jsonObject = new JSONObject(result);
                //  {"ID":"0","Output":"0","Message":"Overlapping Time entries"}
                Log.e("AdminTimeSheetId", result);
                output = jsonObject.getString("Output");
                msg = jsonObject.getString("Message");
            } catch (Exception e) {
                e.getCause();
            }
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            if (output.equals("1")) {   //means sheet submitted successfully
                Shared_Preference.setTIMEGAP_JOB_END_TIME(context, JOB_STOP_DateTime);
                Shared_Preference.setTIMEGAP_PREV_JOB_START_TIME(context, JOB_START_DateTime);
                if (resetClock) {
                    Utility.ResetClock(context);
                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            } else if (output.equals("0")) { //overlap
                if (resetClock) {
                    Utility.ResetClock(context);
                }
                dialog_OverlappingTimeEntry();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String timesheet_id = Submit_Billable_TimeSheetNew();
            AssignTechToUnassignedSwo();
            return timesheet_id;
        }

    }

}
