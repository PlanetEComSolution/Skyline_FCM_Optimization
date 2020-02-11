package planet.info.skyline.tech.billable_timesheet;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
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
import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;
import static planet.info.skyline.util.Utility.LOADING_TEXT;


public class SubmitTimesheet extends BaseActivity {
    String urlofwebservice11_new = SOAP_API_Client.BASE_URL;
    int[] arrid;
    String[] arrname;
    String descriptionm;
    String laborCode;

    Dialog showd;
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
    ArrayList<SWO_Status> list_Swo_Status = new ArrayList<>();

    String Selected_Swo_Status = "", oldSwoStatus = "", Swo_Status = "";
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
        context=SubmitTimesheet.this;
        userRole = Shared_Preference.getUSER_ROLE(this);
        Swo_Id = Shared_Preference.getSWO_ID(context);
        JobIdBillable = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null && bundle.containsKey(AppConstants.TYPE)) {
            status = bundle.getString(AppConstants.TYPE);
        }

        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new async_Get_Billable_Code().execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }


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
                    Utility.hideKeyboard(SubmitTimesheet.this);
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
                    if (oldSwoStatus.equals(Selected_Swo_Status)) {
                        Swo_Status = "0";
                    } else {
                        Swo_Status = Selected_Swo_Status;
                    }
                    reason = descriptionm + "  " + "(" + Total_time + ")";
                    /*****************************************************************/

                    if (status .equals( AppConstants.CHANGE_TIME_CODE)) {      //Change Time Code
                        jobType = Utility.CHANGE_TIME_CODE;
                        resetClock = true;
                        status = "2018";
                        PrepareApiData();

                    } else {                   // Pause , Stop  , Finish
                        jobType = Utility.BILLABLE;
                        resetClock = false;
                        PrepareApiData();
                    }

                }

                /*} else {
                    showd1.dismiss();
                    dialog_AutoDateTimeSet();
                }*/
            }

        });

    }

    public void Dialog_Choose_Labor_Code() {

        showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.radiogroupfortimesheat);
        showd.setCancelable(false);
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

        RadioGroup rg = (RadioGroup) showd.findViewById(R.id.radioGroup1);


        if (arrid != null && arrid.length > 0) {
            for (int i = 0; i < arrid.length; i++) {
                RadioButton button = new RadioButton(this);
                button.setId(arrid[i]);
                String names = arrname[i];
                button.setText(names);
                button.setChecked(false); // Only select button with same index as
                rg.addView(button);
            }
        } else {
            Toast.makeText(context, "Some api_error occured!", Toast.LENGTH_SHORT).show();
        }

        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                laborCode = String.valueOf(checkedId);
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                Dialog_Update_Swo_Awo_Status();
            }
        });
    }

    public void Dialog_Update_Swo_Awo_Status() {
        showd = new Dialog(context);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_swo_status);

        showd.setCancelable(false);
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

        RadioGroup radioGroup = showd.findViewById(R.id.radioGroup);
        TextView title = showd.findViewById(R.id.textView1rr);
        if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
            title.setText("Update AWO Status");
        } else {
            title.setText("Update SWO Status");
        }
        for (int i = 0; i < list_Swo_Status.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            int id = 0;
            try {
                id = Integer.parseInt(list_Swo_Status.get(i).getIDPK());
            } catch (Exception e) {
                e.getMessage();
            }
            radioButton.setId(id);
            radioButton.setText(list_Swo_Status.get(i).getTxtStatus());
            if (String.valueOf(id).equals(oldSwoStatus)) {
                radioButton.setChecked(true);
            }
            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Selected_Swo_Status = String.valueOf(checkedId);
            }
        });

        Button btn_Done = showd.findViewById(R.id.btn_Done);
        btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                Dialog_EnterDesc();
            }
        });

    }

    public void getBillableCodes() {
        String receivedString = "";

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Api.API_BILLABLE_NONBILLABLE_CODE;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;

        String dealerId = Shared_Preference.getDEALER_ID(this);
        String role = Shared_Preference.getUSER_ROLE(this);
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("dealerID", dealerId);
        request.addProperty("cat", role);
        request.addProperty("type", "1");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            receivedString = ks.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String Jsonstring = receivedString;

            String news = Jsonstring.substring(Jsonstring.indexOf("["), Jsonstring.indexOf("]") + 1);
            String n1 = news;
            JSONArray jArray = new JSONArray(n1);
            int len = jArray.length();
            arrid = new int[len];
            arrname = new String[len];
            for (int k = 0; k < (jArray.length()); k++) {

                JSONObject json_obj = jArray.getJSONObject(k);
                arrid[k] = json_obj.getInt("Lalor_ID_PK");
                arrname[k] = json_obj.getString("Labor_name");

            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void get_SWO_AWO_Details_By_ID() {
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Api.API_Get_Swo_AWO_DetailByID_Type;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String swoID = Shared_Preference.getSWO_ID(context);

        request.addProperty("swoID_Awoid", swoID);
        if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
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
            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();
            JSONArray jsonArray = new JSONArray(resultstring);

            if (jsonArray.length() > 0) {
                JSONObject json_obj = jsonArray.getJSONObject(0);
                oldSwoStatus = json_obj.getString("SWO_Status_new");
                Selected_Swo_Status = oldSwoStatus;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void get_SWO_AWO_Status_List() {
        list_Swo_Status.clear();
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Api.API_bind_SWO_AWO_Status;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = Shared_Preference.getDEALER_ID(this);

        request.addProperty("dealerID", dealerId);//nks
        String Role = Shared_Preference.getUSER_ROLE(this);
        if (Shared_Preference.get_EnterTimesheetByAWO(context)) {
            Role = Utility.USER_ROLE_ARTIST;
        }
        request.addProperty("Role", Role);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();
            JSONArray jsonArray = new JSONArray(resultstring);

            for (int k = 0; k < (jsonArray.length()); k++) {
                JSONObject json_obj = jsonArray.getJSONObject(k);
                String ID_PK = json_obj.getString("ID_PK");
                String txt_status = json_obj.getString("txt_status");
                list_Swo_Status.add(new SWO_Status(ID_PK, txt_status));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public String Submit_Billable_TimeSheetNew() {  ///use this fo billable
        String receivedString = "";

        final String URL = urlofwebservice11_new;
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
         //   Shared_Preference.setCLIENT(this, "");
            Shared_Preference.setCLIENT_NAME(this, "");
            new Async_Submit_Billable_Timesheet_New().execute();

        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            showDialog_for_OfflineApiDataSave(context, request_new.toString(), Utility.getUniqueId());
        }
    }

    public void showDialog_for_OfflineApiDataSave(final Context context, final String api_input, final String unique_apiId) {
        //ToDo
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
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
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
        positiveBtn.setOnClickListener(new View.OnClickListener() {
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
        negativeBtn.setOnClickListener(new View.OnClickListener() {
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
        final String URL = urlofwebservice11_new;
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

    public class async_Get_Billable_Code extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

         showprogressdialog();
        }

        @Override
        protected void onPostExecute(Void result) {

           hideprogressdialog();
            if (arrid != null && arrid.length > 0) {
                Dialog_Choose_Labor_Code();
            } else {
                Toast.makeText(getApplicationContext(), "Error! There are NO Job Codes currently in the system!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            get_SWO_AWO_Details_By_ID();
            getBillableCodes();
            get_SWO_AWO_Status_List();
            return null;
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
