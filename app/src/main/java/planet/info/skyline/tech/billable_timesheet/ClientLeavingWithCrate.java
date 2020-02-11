package planet.info.skyline.tech.billable_timesheet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.Api;
import planet.info.skyline.network.ProgressRequestBody;
import planet.info.skyline.network.REST_API_Client;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.util.AppConstants;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.runtime_permission.PermissionActivity;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.CameraUtils;
import planet.info.skyline.util.Utility;
import retrofit2.Call;
import retrofit2.Callback;

import static planet.info.skyline.network.Api.API_UploadPhotoTech;
import static planet.info.skyline.network.Api.API_UploadPhotoTech_new;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;
import static planet.info.skyline.util.Utility.LOADING_TEXT;


public class ClientLeavingWithCrate extends BaseActivity implements ProgressRequestBody.UploadCallbacks {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static String imageStoragePath;
   // public ProgressDialog pDialog;
    List<String> id_list = new ArrayList<>();
    String createid;
    ImageView merchantname, missing, homeacti;
    String path;
    TextView clientnam;
    Button nofr, yesfor;
    DisplayImageOptions options;
    String webhit = "", webhitforupdate_photo = "";
    String saveonnewloc = "", scancreateid = "", scancreateloc = "";
    RelativeLayout mains;
    int sellects_opttion = 0;
    String remove_crete = "";
    int check_gotocatch;
    String linkforconect = "";
    String urlofwebservice2 = URL_EP2 + "/WebService/techlogin_service.asmx?op=UploadPhotoTech";
    String location_name = "", locationorbin = "", location = "";
    List<HashMap<String, String>> noteList = new ArrayList<HashMap<String, String>>();
    Map<String, HashMap<String, String>> noteList2 = new HashMap<String, HashMap<String, String>>();
    List<HashMap<String, String>> SelectedCrateList = new ArrayList<>();
    String crate_name = "";
    long totalSize = 0;
    ArrayList<String> list_path = new ArrayList<>();
    ArrayList<String> list_imageSize = new ArrayList<>();
    ArrayList<String> list_UploadImageName = new ArrayList<>();
    int crateNumber;
    String mainCrate_Name;
    String mainCrate_Id;
    String insideCrate;
    AlertDialog alertDialog;
   // ProgressDialog pDailog;
    ArrayList<HashMap<String, String>> list_CratesPackInto = new ArrayList<>();
    String BinId;
    boolean isBin = false;
    boolean isFreight = false;
    boolean isArea = false;
   // ProgressDialog uploadProgressDialog;
    int Count_Image_Uploaded = 0;

    Context context;
    ProgressHUD mProgressHUD;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alercratebackonrack_new);
        context=ClientLeavingWithCrate.this;

        nofr = findViewById(R.id.Btn_No);
        yesfor = findViewById(R.id.Btn_Yes);
        mains = (RelativeLayout) findViewById(R.id.mains);
        mains.setVisibility(View.INVISIBLE);
        merchantname = (ImageView) findViewById(R.id.merchantname);
        merchantname.setVisibility(View.VISIBLE);
        merchantname.setImageResource(R.drawable.exhibitlogoa);

       // pDialog = new ProgressDialog(ClientLeavingWithCrate.this);
       // pDialog.setMessage(getString(R.string.Loading_text));

       // pDialog.setCancelable(false);
        homeacti = (ImageView) findViewById(R.id.homeacti);
        homeacti.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii = new Intent(ClientLeavingWithCrate.this,
                                Clock_Submit_Type_Activity.class);
                        startActivity(ii);
                    }
                });

        clientnam = (TextView) findViewById(R.id.textView1);

        missing = (ImageView) findViewById(R.id.missing);
        missing.setVisibility(View.VISIBLE);

        String imageloc = Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);
        clientnam.setVisibility(View.VISIBLE);
        if (imageloc.equals("") || imageloc.equalsIgnoreCase("")) {
            missing.setVisibility(View.GONE);
            clientnam.setVisibility(View.VISIBLE);
        } else {
            imageLoadery.displayImage(imageloc, missing, options);
            clientnam.setVisibility(View.INVISIBLE);
        }


        String nams = Shared_Preference.getCLIENT_NAME(this);
     //   String names = Shared_Preference.getCLIENT(this);
            clientnam.setText(nams);



        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.skylinelogopng)

                .showImageForEmptyUri(R.drawable.skylinelogopng)
                .showImageOnFail(R.drawable.skylinelogopng).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();






   Utility.scanqr(ClientLeavingWithCrate.this,1);

        nofr.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Utility.scanqr(ClientLeavingWithCrate.this,1);

            }
        });
        yesfor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Utility.scanqr(ClientLeavingWithCrate.this,2);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String result = "";
                String Client_id = "";
                try {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    if (contents.contains("^")) {
                        contents = contents.replaceAll(Pattern.quote("^"), "#");//nks
                    }
                    if (contents.contains("_crate_name=")) {
                    } else {
                        Toast.makeText(getApplicationContext(), "Please scan valid QR Code of Crate!", Toast.LENGTH_LONG).show();
                        qcanqrcode(1);
                        return;
                    }

                    if (contents.contains("loc=")) {
                        String hom = contents;
                        result = hom.substring(hom.indexOf("_id=") + 4, hom.indexOf("_client"));
                        crate_name = hom.substring(hom.indexOf("_crate_name=") + 12, hom.length());
                        Client_id = hom.substring(hom.indexOf("client="), hom.indexOf("_loc"));
                        Client_id = Client_id.substring(Client_id.indexOf("=") + 1);
                    }

                    createid = result;
                    if (crate_name == null || crate_name.equals("")) {
                        getCrateName(createid, Client_id);
                    } else {
                        addThisCrateToList(Client_id);
                    }

                } catch (Exception e) {
                }


            } else if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {


                path = imageStoragePath;

                int orientation = Utility.getExifOrientation(path);
                try {
                    if (orientation == 90) {

                        Bitmap bmp = Utility.getRotatedBitmap(path, 90);
                        path = Utility.saveImage(bmp);
                    }
                } catch (Exception e) {
                    e.getCause();
                }

                list_path.add(path);
                if (new ConnectionDetector(ClientLeavingWithCrate.this).isConnectingToInternet()) {
                    multipartImageUpload();


                } else {
                    Toast.makeText(ClientLeavingWithCrate.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ClientLeavingWithCrate.this, Clock_Submit_Type_Activity.class));
                    finish();
                }

            } else if (requestCode == 2) {

                try {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    //nks
                    if (contents.contains("^")) {
                        contents = contents.replaceAll(Pattern.quote("^"), "#");//nks
                    }
                    //nks
                    Log.d("BHANU_Client_name", contents);
                    String hom = contents;

                    if ((hom.contains(",") || (hom.contains("crate_name")))) {
                        validation_diloge();

                    } else {

/*
 bin
https://beta.ep2.businesstowork.com/ep1//superadmin/inventory/warehouse-qr-code-new.php?type=bin_id=992_aid=260_w=473_wn=TR1221

area
https://beta.ep2.businesstowork.com/ep1//superadmin/inventory/show-qr-warehouse-one.php?type=area_id=39_loc=Client

freight
id :586Vname :Pankaj Saini:Cat :Service Production CoordinatorScanValue : 1

*/

                        if (hom.contains("type=bin_id")) {  //bin

                            isBin = true;
                            isFreight = false;
                            isArea = false;
                            BinId = hom.substring(hom.indexOf("bin_id=") + 7, hom.indexOf("_aid"));
                            GetBinName(BinId);

                        } else if (hom.contains("Vname")) {  //freight vendor
                            isBin = false;
                            isFreight = true;
                            isArea = false;
                            String[] tokens = hom.split(":");
                            scancreateloc = tokens[2].trim();
                            showdialogforupdateconfirm();
                        } else if (hom.contains("type=area_id")) {  //area
                            isBin = false;
                            isFreight = false;
                            isArea = true;
                            BinId = hom.substring(hom.indexOf("area_id=") + 8, hom.indexOf("_loc"));
                            GetAreaName(BinId);

                        } else if (hom.contains("id") && hom.contains("aid")) { //Bin, old qr code
                            isBin = true;
                            isFreight = false;
                            isArea = false;
                            BinId = hom.substring(hom.indexOf("id=") + 3, hom.indexOf("_aid"));
                            GetBinName(BinId);

                        } else {
                            Toast.makeText(ClientLeavingWithCrate.this,
                                    "Please scan valid QR Code of Bin/Freight Vendor/Area!", Toast.LENGTH_LONG)
                                    .show();
                        }


                    }


                } catch (Exception ee) {
                    Toast.makeText(ClientLeavingWithCrate.this,
                            "Please scan valid QR Code of Bin/Freight Vendor/Area!", Toast.LENGTH_LONG)
                            .show();
                }

            } else if (requestCode == 3) {

            } else if (requestCode == 5) {
                StopClock();
            } else if (requestCode == 6) {
                try {
                    String result = "";
                    String[] gg = null;
                    String contents = data.getStringExtra("SCAN_RESULT");
                    String hom = contents;
                    if (contents.contains("_wn"))//bin
                    {
                        location_name = hom.substring(hom.indexOf("_wn=") + 4, hom.length());
                    } else {

                        try {
                            //LocationType="Workstation";
                            gg = contents.split("loc=");
                            String loc = gg[1];
                            location_name = loc;
                        } catch (Exception e) {
                            location_name = "";

                        }


                    }
                } catch (Exception e) {

                }

                try {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    String hom = contents;
                    if (contents.contains("type=area"))//bin
                    {
                        locationorbin = "area";
                        location = hom.substring(hom.indexOf("area_id=") + 8, hom.indexOf("_loc"));
                    } else if (contents.contains("type=bin_id")) {

                        try {
                            locationorbin = "bin";
                            location = hom.substring(hom.indexOf("bin_id=") + 7, hom.indexOf("_aid"));
                        } catch (Exception e) {
                            location = "";

                        }
                    }
                } catch (Exception e) {

                }

                //
                String result = "";
                try {

                    String[] gg = null;
                    String contents = data.getStringExtra("SCAN_RESULT");
                    String hom = contents;
                    if (hom.contains("_wn=")) {
                        result = hom.substring(hom.indexOf("_wn=") + 4,
                                hom.length());
                        String ssdcf = result;
                        scancreateloc = result;
                    }

                    if (contents.contains("loc=")) {
                        gg = contents.split("loc=");
                        scancreateloc = gg[1];
                    }
                    confirmcratemove(scancreateloc);

                } catch (Exception e) {

                }
            } else if (requestCode == 10) {
                String result = "";
                String cratename = "";

                try {

                    String contents = data.getStringExtra("SCAN_RESULT");

                    if (contents.contains("^")) {
                        contents = contents.replaceAll(Pattern.quote("^"), "#");//nks
                    }

                    if (contents.contains("_crate_name=")) {

                    } else {
                        Toast.makeText(getApplicationContext(), "Please scan valid QR Code of Crate!", Toast.LENGTH_LONG).show();
                        qcanqrcode(10);
                        return;
                    }
                    if (contents.contains("loc=")) {
                        String hom = contents;
                        result = hom.substring(hom.indexOf("_id=") + 4, hom.indexOf("_client"));

                        cratename = hom.substring(hom.indexOf("_crate_name=") + 12);
                    }

                    createid = result;
                    if (createid.equals(mainCrate_Id)) {
                        //add the main crate to the list
                        HashMap<String, String> hasmap = new HashMap<String, String>();
                        hasmap.put("id", result);
                        hasmap.put("name", cratename);
                        noteList.add(hasmap);

                        //remove the inside crate from the list
                        for (int i = 0; i < noteList.size(); i++) {
                            String crateId = noteList.get(i).get("id");
                            if (crateId.equals(insideCrate)) {
                                noteList.remove(i);
                            }
                        }

                        String msg = "Main crate " + mainCrate_Name + " is added to the list!\nPlease select it to move!";
                        Show_confirmation_Dialog(msg);

                    } else {

                        ShowValidationDialog("Please scan " + mainCrate_Name + " QR code!", 10);
                    }


                } catch (Exception e) {
                    e.getMessage();
                }
            } else if (requestCode == Utility.REQUEST_CODE_PERMISSIONS) {
                takeaphoto();
            }
        } else {
            finish();
        }
    }


    void StopClock(){
      //  Intent i = new Intent(getApplicationContext(), SubmitTimesheet.class);
        Intent i = new Intent(getApplicationContext(), SubmitTimesheetNew.class);
        i.putExtra(AppConstants.TYPE,AppConstants.FINISH);
        startActivity(i);
        finish();
    }
    public void getCrateName(final String Crate_id, final String clientId) {
showprogressdialog();
        //nks
        String url = SOAP_API_Client.URL_EP1 + Api.API_FETCH_CRATE_NAME + Crate_id;
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                hideprogressdialog();

                try {

                    JSONArray jsonArray = obj.getJSONArray("cds");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    crate_name = jsonObject.getString("crate_number");
                    addThisCrateToList(clientId);

                } catch (Exception e) {
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

                hideprogressdialog();
                Toast.makeText(ClientLeavingWithCrate.this, "Some api error occurred!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ClientLeavingWithCrate.this, MainActivity.class));
                finish();
            }
        });
        AppController.getInstance().addToRequestQueue(bb);

    }

    private void addThisCrateToList(String _clientID) {
        HashMap<String, String> hasmap = new HashMap<>();
        if (id_list.size() > 0) {
            if (!(id_list.contains(createid))) {
                id_list.add(createid);
                hasmap.put("id", createid);
                hasmap.put("name", crate_name);
                hasmap.put("Client_id", _clientID);
                noteList.add(hasmap);
            } else {
                Toast.makeText(getApplicationContext(), "This crate is already added!", Toast.LENGTH_SHORT).show();
            }
        } else {
            id_list.add(createid);
            hasmap.put("id", createid);
            hasmap.put("name", crate_name);
            hasmap.put("Client_id", _clientID);
            noteList.add(hasmap);
        }

        workornot();

    }

    public void showdialogforupdateconfirm() {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.alerdialforupdatelocat_new);
        showd.setCancelable(false);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);

        crate_name = "";
        scancreateid = "";

        for (int i = 0; i < SelectedCrateList.size(); i++) {
            crate_name = crate_name + SelectedCrateList.get(i).get("name") + ",";
            scancreateid = scancreateid + noteList.get(i).get("id") + ",";
        }
        crate_name = crate_name.substring(0, crate_name.lastIndexOf(","));
        scancreateid = scancreateid.substring(0, scancreateid.lastIndexOf(","));


        //


//
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        String message = "Please confirm that you have moved " + crate_name + " crate(s) to " + scancreateloc;
        texrtdesc.setText(message);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
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

        nofo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Utility.scanqr(ClientLeavingWithCrate.this,2);


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


            }
        });

        yesfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                if (new ConnectionDetector(ClientLeavingWithCrate.this).isConnectingToInternet()) {


                    try {
                        showd.dismiss();

                    } catch (Exception e) {
                        e.getMessage();
                    }


                    showprogressdialog();//
                    String tnam = Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                    if (isBin) {
                        saveonnewloc = URL_EP1 + Api.API_CRATE_INFO_UPDATE + "cratesid=" + scancreateid + "&type=bin&aid=" + BinId + "&tech_id=" + tnam + "&print=yes";
                    } else if (isArea) {
                        saveonnewloc = URL_EP1 + Api.API_CRATE_INFO_UPDATE + "cratesid=" + scancreateid + "&type=area&aid=" + BinId + "&tech_id=" + tnam + "&print=yes";
                    } else if (isFreight) {
                        saveonnewloc = URL_EP1 + Api.API_CRATE_INFO_UPDATE + "cratesid=" + scancreateid + "&type=freight&fname=" + scancreateloc + "&tech_id=" + tnam;
                    }
                    saveonnewloc = saveonnewloc.replace(" ", "%20");
                    Log.d("CrateMoveApi", saveonnewloc);
                    updatelocation(saveonnewloc);
                } else {
                    Toast.makeText(ClientLeavingWithCrate.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });

        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void showdialogforcomplete() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientLeavingWithCrate.this);
        LayoutInflater inflater = LayoutInflater.from(ClientLeavingWithCrate.this);
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
        title.setText("Confirm job completion");
        message.setText("Is the job completely finished?");
        positiveBtn.setText("Yes,Stop Clock");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (new ConnectionDetector(ClientLeavingWithCrate.this).isConnectingToInternet()) {


                    String dd = Shared_Preference.getSWO_ID(ClientLeavingWithCrate.this);
                    String clientidme = Shared_Preference.getLOGIN_USER_ID(ClientLeavingWithCrate.this);
                    String tech = Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                    Long jj = System.currentTimeMillis();
                    String sd = jj.toString();
                    Random ss = new Random();
                    int i1 = ss.nextInt(99999) + 2;
                    sd = String.valueOf(i1);
                    webhit = URL_EP2 + "/Register/auto_generate_event2.aspx?done_by="
                            + clientidme
                            + "&swo_id="
                            + dd
                            + "&msg="
                            + tech
                            + " Has completely finished the job "
                            + "&status=3";
                    webhit = webhit.replace(" ", "%20");
                    String dh = webhit;
                    String de = dh;
                    getjsonobject11();
                   StopClock();

                } else {
                    Toast.makeText(ClientLeavingWithCrate.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();


    }

    public void getjsonobject11() {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, webhit, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject obj) {

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                Log.e("api_error", arg0.toString());

            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }

    public void slotmovcess() {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, linkforconect, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject obj) {
                        StopClock();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                StopClock();

            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }



    public void updatelocation(String links) {
        JsonObjectRequest updatess = new JsonObjectRequest(Method.GET, links,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsono) {
                hideprogressdialog();
                showtoast();
                showdialogforcomplete();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                //
                hideprogressdialog();
                showdialogforcomplete();

            }
        });
        AppController.getInstance().addToRequestQueue(updatess);
    }

    public void showtoast() {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a dummy image
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.apply);

        Toast toast = new Toast(getApplicationContext());
        // toast.setGravity(Gravity.CENTER_VERTICAL, 0, 120);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void Dialog_Show_All_Crates() {
        if (noteList != null && noteList.size() > 0) {
            final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
            showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            showd.setContentView(R.layout.putmoveclient_new_2);
            showd.setCancelable(false);
            showd.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));

            // crate_count = 0;
            // remove_crete = "";
            SelectedCrateList = new ArrayList<>();

            LinearLayout checkboxlayout = (LinearLayout) showd
                    .findViewById(R.id.first4);
            Button Submit = (Button) showd.findViewById(R.id.submits);
            Submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (sellects_opttion == 0 || SelectedCrateList.size() == 0) {
                        Toast.makeText(getApplicationContext(),
                                "Please select one!", Toast.LENGTH_LONG).show();
                    } else if (sellects_opttion == 1) {
                        showd.dismiss();

                        //   mains.setVisibility(View.VISIBLE);
                        // for crate inside another crate

                        crateNumber = 0;
                        if (SelectedCrateList.size() > crateNumber) {
                            Check_This_Crate_Packed_In_Other_Crate();
                        }


                    } else if (sellects_opttion == 2) {
                        showd.dismiss();
                        takeaphoto();
                    }
                }
            });
            CheckBox checkBox;
            class chBxOnclick implements OnClickListener {
                public void onClick(View v) {
                    CheckBox cc = (CheckBox) v;
                    if (cc.isChecked()) {
                        cc.getTag();
                        //    crate_count = crate_count + 1;
                        // remove_crete = remove_crete + "," + cc.getTag();

                        //
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", String.valueOf(cc.getTag()));
                        hashMap.put("name", String.valueOf(cc.getText()));
                        SelectedCrateList.add(hashMap);
                        Log.d("---Selected Crate--", SelectedCrateList.toString());
                        //

                    } else {
                        //  crate_count = crate_count - 1;
                        //  remove_crete = remove_crete.replace("" + cc.getTag(), "");

                        //
                        for (int i = 0; i < SelectedCrateList.size(); i++) {
                            HashMap<String, String> hashMap = SelectedCrateList.get(i);
                            if (hashMap.get("id").equals(cc.getTag())) {
                                SelectedCrateList.remove(i);
                                break;
                            }
                        }
                        //
                        Log.d("---Selected Crate--", SelectedCrateList.toString());
                        //
                    }


                }
            }

            for (int count = 0; count < noteList.size(); count++) {
                checkBox = new CheckBox(this);
                checkBox.setId(count);
                String name = noteList.get(count).get("name");
                checkBox.setText(name);
                checkBox.setTag(noteList.get(count).get("id"));
                checkBox.setOnClickListener(new chBxOnclick());
                checkboxlayout.addView(checkBox);

            }


            Spinner dropdown = (Spinner) showd.findViewById(R.id.spinner1);  /// here is my breaking point
            String[] items = new String[]{"--Select One--",
                    "Put the Crate(s) back in a\nBin/Freight Vendor",
                    "Client is leaving with the Crate(s)"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                           long arg3) {
                    if (pos == 0) {
                        sellects_opttion = 0;
                    } else
                        sellects_opttion = pos;
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    Toast.makeText(getApplicationContext(), "Please select one!",
                            Toast.LENGTH_LONG).show();
                }
            });


            showd.show();
        } else {

            showdialogforcomplete();
        }


    }

    public void workornot() {

        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        TextView yes_text = (TextView) showd.findViewById(R.id.Btn_No);
        TextView no_text = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        //texrtdesc.setText(cratid + "  Scan another crate?");
        texrtdesc.setText(" Scan another crate?");
        yes_text.setText("  Yes  ");
        no_text.setText("  No  ");
        close.setVisibility(View.GONE);
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Crate No. : " + crate_name);
        yes_text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sacancrtaeandopenanother(1);

                // finish();

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


                finish();

            }
        });
        no_text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("--Scanned crate list--", noteList.toString());

                SelectedCrateList = new ArrayList<>();//make a copy of scanned crates


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Show_All_Crates();

                // finish();
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void takeaphoto() {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        showd.setCancelable(false);
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        texrtdesc.setText("Take a photo of the Clients with the crates");
        nofo.setText("  Yes  ");
        yesfo.setText("  No  ");
        yesfo.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Please select");
        close.setVisibility(View.INVISIBLE);
        nofo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                if (PermissionActivity.CheckingPermissionIsEnabledOrNot(ClientLeavingWithCrate.this)) {
                    captureImage();
                } else {
                    Intent i = new Intent(getApplicationContext(), PermissionActivity.class);
                    startActivityForResult(i, Utility.REQUEST_CODE_PERMISSIONS);
                }


            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void confirmcratemove(String loc) {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        if (remove_crete.startsWith(","))
            remove_crete = remove_crete.substring(1, remove_crete.length());

        String[] vv = remove_crete.split(",");
        int lens = vv.length;
        String name = "";
        for (int j = 0; j < lens; j++) {
            try {
                name = name + noteList2.get(vv[j]).get("name") + ",";
            } catch (Exception e) {

            }
        }
        if (name.endsWith(",")) {
            name = name.substring(0, name.length() - 1);
        }
        texrtdesc.setText("Crate " + name + " has been moved to " + loc);
        //texrtdesc.setText("Crate " + crte + " has been moved to " + loc);
        nofo.setText("  Yes  ");
        // yesfo.setText("  No  ");
        yesfo.setVisibility(View.GONE);
        close.setVisibility(View.INVISIBLE);
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Please select");
        close.setVisibility(View.INVISIBLE);
        nofo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (remove_crete.startsWith(","))
                    remove_crete = remove_crete.substring(1, remove_crete.length());

                String clientida = Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                linkforconect = URL_EP1 + Api.API_CRATE_INFO_UPDATE + "cratesid=" + remove_crete + "&type=" + locationorbin + "&aid=" + location + "&tech_id=" + clientida + "&print=yes";
                linkforconect = linkforconect.replace(" ", "%20");
                slotmovcess();


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                //
                // finish();

            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void sacancrtaeandopenanother(int bits) {
        Utility.scanqr(ClientLeavingWithCrate.this,bits);
    }




    public void enterdata(String a, String b, String c, String d, String e,
                          String f, String g, String h) {

        check_gotocatch = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice2;
        final String SOAP_ACTION = KEY_NAMESPACE + API_UploadPhotoTech;
        final String METHOD_NAME = API_UploadPhotoTech;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        // request.addProperty("job_id",enterjobid );
        request.addProperty("job_swo_id", a);
        request.addProperty("description", b);
        request.addProperty("filename", c);
        request.addProperty("comment", d);
        request.addProperty("filesize", e);
        request.addProperty("tech_name", f);
        request.addProperty("tech_id", g);
        request.addProperty("type", h);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j); // if complex type is present then you can
                // cast this to SoapObject and if primitive
                // type is returned you can use toString()
                // to get actuall value.
            }
        } catch (Exception ed) {
        }
        if (check_gotocatch == 0) {
            try {

            } catch (Exception edd) {
            }

        }
    }

    public String generate_event(String job_swo_id, String tech_id, String type, String count) {
        String result = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice2;
        final String SOAP_ACTION = KEY_NAMESPACE + API_UploadPhotoTech_new;
        final String METHOD_NAME = API_UploadPhotoTech_new;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("job_swo_id", job_swo_id);
        request.addProperty("tech_id", tech_id);
        request.addProperty("type", type);
        request.addProperty("count", count);

        Log.d("billable photo event", request.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j); // if complex type is present then you can

            }
            String response = ks.toString();
            if (response.contains("st=1")) {
                result = "1";
            } else {
                result = "0";
            }
        } catch (Exception ed) {
            ed.printStackTrace();
        }
        return result;
    }

    public void qcanqrcode(int c) {
        Utility.scanqr(ClientLeavingWithCrate.this,c);
    }

    public void validation_diloge() {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.validation_new);
        showd.setCancelable(false);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView yesfo = (TextView) showd.findViewById(R.id.scann_again);
        ImageView close = (ImageView) showd.findViewById(R.id.close);

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
        yesfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Utility.scanqr(ClientLeavingWithCrate.this,2);
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Check_This_Crate_Packed_In_Other_Crate() {

        showprogressdialog();
        final String CrateId = SelectedCrateList.get(crateNumber).get("id");
        final String CrateName = SelectedCrateList.get(crateNumber).get("name");

        //nks
        String url = URL_EP1 + Api.API_CHECK_CRATE + CrateId;
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //  pDailog.dismiss();
                //nks
                try {
                   hideprogressdialog();
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1"))//means crate is packed within another crate
                    {


                        insideCrate = CrateId;
                        mainCrate_Name = jsonObject.getString("main_crate");
                        mainCrate_Id = jsonObject.getString("crt_id");
                        Show_Dialog_for_Main_Crate_Move(CrateName, CrateId, mainCrate_Name, mainCrate_Id);

                    } else {
                        Check_Crates_Packed_in_this_Crate(CrateId, CrateName);
                    }


                } catch (Exception e) {
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub

        hideprogressdialog();

            }
        });
        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Show_confirmation_Dialog(String message) {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);

        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        // texrtdesc.setText("Please scan the crates you want to move");
        texrtdesc.setText(message);
        nofo.setText("  No  ");
        nofo.setVisibility(View.GONE);
        yesfo.setText("Ok");
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("");


        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Show_All_Crates();
            }
        });
        yesfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Show_All_Crates();

            }
        });


        try {
            showd.show();

        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Show_Dialog_for_Main_Crate_Move(final String Crate_Name, final String Crate_Id,
                                                final String mainCrate_Name, final String mainCrate_Id) {

        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_main_crate_move);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);

        TextView text_Main_crateName = (TextView) showd.findViewById(R.id.text_Main_crateName);
        TextView text_crateMsg = (TextView) showd.findViewById(R.id.textView1rr);


        Button btn_MoveMainCrate = (Button) showd.findViewById(R.id.btn_MoveMainCrate);
        Button btn_RemoveFromMainCrate = (Button) showd.findViewById(R.id.btn_RemoveFromMainCrate);
        Button btn_PackInAnotherCrate = (Button) showd.findViewById(R.id.btn_PackInAnotherCrate);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        text_Main_crateName.setText(mainCrate_Name);

        String sourceString = "The Crate " + Crate_Name + " is packed inside: " + "<b>" + mainCrate_Name + "</b> ";

        text_crateMsg.setText(Html.fromHtml(sourceString));
        btn_MoveMainCrate.setText("Please scan the " + mainCrate_Name + " QR Code to move " + mainCrate_Name + " and its contents.");
        btn_RemoveFromMainCrate.setText("Remove this crate from " + mainCrate_Name + " to Store/Ship alone.");
        btn_PackInAnotherCrate.setText("Pack this crate in a different crate.");

        btn_MoveMainCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                //check mainCrate is already added or not
                boolean isMainCrateAlreadySelected = false;
                for (int i = 0; i < noteList.size(); i++) {
                    String crateId = noteList.get(i).get("id");
                    if (crateId.equals(mainCrate_Id)) {
                        isMainCrateAlreadySelected = true;
                        break;
                    } else {
                        if (i == (noteList.size() - 1)) {
                            isMainCrateAlreadySelected = false;
                            break;
                        }
                    }

                }
                //
                if (!isMainCrateAlreadySelected) {

                    Utility.scanqr(ClientLeavingWithCrate.this,10);

                } else {

                    //if MAIN CRATE is already added then remove INSIDE CRATE from list
                    for (int i = 0; i < noteList.size(); i++) {
                        String crateId = noteList.get(i).get("id");
                        if (crateId.equals(Crate_Id)) {
                            noteList.remove(i);
                        }
                    }
                    //
                    Show_confirmation_Dialog("The main crate " + mainCrate_Name + " is already added! Please select it to move!");
                }

            }
        });

        btn_RemoveFromMainCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Remove_Crate_from_Main_Crate_to_Ship_Alone(Crate_Name, Crate_Id);
            }
        });


        btn_PackInAnotherCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Get_Crates_to_Pack_in(Crate_Name, Crate_Id);
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


                Dialog_Show_All_Crates();
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void ShowValidationDialog(String message, final int extra) {
        final Dialog diloge = new Dialog(ClientLeavingWithCrate.this); // open a diloge if usre Scann wrong qr code
        diloge.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diloge.setContentView(R.layout.validation_new);
        diloge.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        diloge.setCancelable(false);
        TextView scann = (TextView) diloge.findViewById(R.id.scann_again);
        TextView texrtdesc = (TextView) diloge.findViewById(R.id.texrtdesc);
        ImageView close = (ImageView) diloge.findViewById(R.id.close);

        texrtdesc.setText(message);

        scann.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    diloge.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Utility.scanqr(ClientLeavingWithCrate.this,extra);
            }
        });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    diloge.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Show_All_Crates();
            }
        });


        try {
            diloge.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Remove_Crate_from_Main_Crate_to_Ship_Alone(final String Crate_name, String Crate_id) {
      showprogressdialog();
        String clientid = Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
        //nks
        String url = URL_EP1 + Api.API_STORE_ALONE_CRATE + Crate_id + "&tech_id=" + clientid;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success"))//means crate is shipped alone
                    {
                        String Message = " Crate " + Crate_name + " has been shipped alone. \n Please select it to move!";
                        Show_confirmation_Dialog(Message);

                    } else {

                        Toast.makeText(ClientLeavingWithCrate.this, "Failed! Crate " + Crate_name + " has not been shipped alone!", Toast.LENGTH_LONG).show();
                        Dialog_Show_All_Crates();
                    }

                } catch (Exception e) {
                    e.getMessage();
                }


           hideprogressdialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
           hideprogressdialog();
            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Get_Crates_to_Pack_in(final String Crate_Name, final String Crate_Id) {
       showprogressdialog();
        list_CratesPackInto = new ArrayList<>();
        String Client_id = "";
        for (int i = 0; i < noteList.size(); i++) {
            if (Crate_Id.equals(noteList.get(i).get("id"))) {
                Client_id = noteList.get(i).get("Client_id");
                break;
            }
        }

        //nks
        String url = URL_EP1 + Api.API_CHECK_CRATE_PACKED_IN_OTHER_CRATE + Client_id + "&crate_id=" + Crate_Id;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONArray jsonArray = obj.getJSONArray("cds");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Crate_Id = jsonObject.getString("id");
                        String Crate_Number = jsonObject.getString("crate_number");
                        String Crate_Desc = jsonObject.getString("description");

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Crate_Id", Crate_Id);
                        hashMap.put("Crate_Number", Crate_Number);
                        hashMap.put("Crate_Desc", Crate_Desc);
                        list_CratesPackInto.add(hashMap);

                    }
                    Show_Dialog_for_Crates(Crate_Name, Crate_Id);

                } catch (Exception e) {
                    e.getMessage();
                }

             hideprogressdialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
               hideprogressdialog();
                Toast.makeText(ClientLeavingWithCrate.this, "Failed! Error getting crate list!", Toast.LENGTH_SHORT).show();
                Dialog_Show_All_Crates();
            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Show_Dialog_for_Crates(final String Crate_Name, final String Crate_Id) {

        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.radiogroupfortimesheat);
        showd.setCancelable(true);//nks
        showd.setCanceledOnTouchOutside(false);
        TextView title = (TextView) showd.findViewById(R.id.textView1rr);

        title.setText("Select Crate");
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

        RadioGroup rg = (RadioGroup) showd.findViewById(R.id.radioGroup1);
        try {
            for (int i = 0; i < list_CratesPackInto.size(); i++) {
                RadioButton button = new RadioButton(this);
                button.setId(Integer.parseInt(list_CratesPackInto.get(i).get("Crate_Id")));
                String CrateNumber = list_CratesPackInto.get(i).get("Crate_Number");
                String CrateDesc = list_CratesPackInto.get(i).get("Crate_Desc");
                String text = CrateNumber + "\n" + CrateDesc;
                button.setText(text);
                button.setChecked(false);
                rg.addView(button);
            }
        } catch (Exception e) {
            e.getMessage();
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String checked_CrateNum = (String) radioButton.getText();
                checked_CrateNum = checked_CrateNum.substring(0, checked_CrateNum.indexOf("\n"));


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Confirmation_CrateMoveIntoCrate(checkedId + "", checked_CrateNum, Crate_Name, Crate_Id);


            }
        });

        //nks
        showd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onBackPressed();
            }
        });

    }

    public void Dialog_Confirmation_CrateMoveIntoCrate(final String Checked_CrateId, final String Checked_CrateName, final String Crate_Name, final String Crate_id) {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        Button Btn_No = (Button) showd.findViewById(R.id.Btn_No);
        Button Btn_Yes = (Button) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView txt_message = (TextView) showd.findViewById(R.id.texrtdesc);
        String message = "Please confirm that you have moved " + Crate_Name + " crate to crate " + Checked_CrateName + "?";
        txt_message.setText(message);
        Btn_Yes.setText("Yes");
        Btn_No.setText("No");


        Btn_Yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();
                } catch (Exception e) {

                }

                Move_CrateInto_Crate(Crate_id, Checked_CrateId);


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


                Dialog_Show_All_Crates();

            }
        });

        Btn_No.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Show_All_Crates();

            }
        });


        try {
            showd.show();

        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Move_CrateInto_Crate(final String CrateToMove, String CrateIntoMove) {
        showprogressdialog();


        String techName = Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
        String url = URL_EP1 + Api.API_MOVE_CRATE_TO_OTHER_LOCATION + techName + "&crate_id=" + CrateToMove + "&new_crate_id=" + CrateIntoMove;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {

                try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success"))//means crate is moved successfully
                    {

                        //now remove this crate from list
                        for (int i = 0; i < noteList.size(); i++) {
                            if (noteList.get(i).get("id").equals(CrateToMove)) {
                                noteList.remove(i);
                                break;
                            }
                        }

                        String msg = "Crate moved successfully!";
                        Show_confirmation_Dialog(msg);//

                    } else {
                        Toast.makeText(ClientLeavingWithCrate.this, "Failed! Error moving crate into crate!", Toast.LENGTH_LONG).show();
                        Dialog_Show_All_Crates();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

             showprogressdialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
              hideprogressdialog();
                Toast.makeText(ClientLeavingWithCrate.this, "Failed! Error moving crate into crate!", Toast.LENGTH_SHORT).show();
                Dialog_Show_All_Crates();
            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void GetBinName(String BinId) {
       showprogressdialog();
        String url = URL_EP1 + Api.API_GET_BIN_NAME + BinId;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    scancreateloc = jsonObject.getString("win_loc");
                    if (!scancreateloc.equals("")) {
                        showdialogforupdateconfirm();
                    } else {
                        Toast.makeText(ClientLeavingWithCrate.this, "Error getting Bin Name!", Toast.LENGTH_LONG).show();
                        Dialog_Show_All_Crates();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
             hideprogressdialog();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

              hideprogressdialog();
                Toast.makeText(ClientLeavingWithCrate.this, "Error getting Bin Name!", Toast.LENGTH_LONG).show();
                Dialog_Show_All_Crates();

            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void GetAreaName(String AreaId) {

      showprogressdialog();


        //nks
        String url = URL_EP1 + Api.API_GET_AREA_LOCATION + AreaId;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {

               hideprogressdialog();
                try {

                    JSONArray jsonArray = obj.getJSONArray("cds");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    scancreateloc = jsonObject.getString("area_name");
                    String areaCode = jsonObject.getString("area_code");
                    String areaId = jsonObject.getString("a_id");

                    Log.e("Location_name", location_name);
                } catch (Exception e) {
                    e.getMessage();
                }
                showdialogforupdateconfirm();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
         hideprogressdialog();
                Toast.makeText(ClientLeavingWithCrate.this, "Error getting Bin Name!", Toast.LENGTH_LONG).show();
                Dialog_Show_All_Crates();

            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Check_Crates_Packed_in_this_Crate(final String CrateId, final String CrateName) {
        showprogressdialog();
        /*http://www.exhibitpower2.com/ep1/crateslist_insidecrate_webservice.php?id=3016*/
        String url = URL_EP1 + Api.API_GET_INSIDE_CRATE_LIST + CrateId;
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {



                try {

                  hideprogressdialog();
                    JSONArray jsonArray = obj.getJSONArray("data");

                    JSONArray jsonArray_Element1 = new JSONArray();
                    JSONArray jsonArray_Element2 = new JSONArray();
                    JSONObject jsonObject_main = jsonArray.getJSONObject(0);
                    try {

                        jsonArray_Element1 = jsonObject_main.getJSONArray("element1");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    try {

                        jsonArray_Element2 = jsonObject_main.getJSONArray("element2");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    int totalCrates = jsonArray_Element1.length() + jsonArray_Element2.length();
                    if (totalCrates > 0) { //means crate contains other crate inside it.
                        ArrayList<HashMap<String, String>> List_InsideCrates = new ArrayList<>();

                        for (int i = 0; i < jsonArray_Element1.length(); i++) {

                            JSONObject jsonObject = jsonArray_Element1.getJSONObject(i);
                            String CrateId = jsonObject.getString("id");
                            String CrateName = jsonObject.getString("crate_number");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("CrateId", CrateId);
                            hashMap.put("CrateName", CrateName);
                            List_InsideCrates.add(hashMap);
                        }

                        for (int i = 0; i < jsonArray_Element2.length(); i++) {
                            JSONObject jsonObject = jsonArray_Element2.getJSONObject(i);
                            String CrateId = jsonObject.getString("id");
                            String CrateName = jsonObject.getString("crate_number");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("CrateId", CrateId);
                            hashMap.put("CrateName", CrateName);
                            List_InsideCrates.add(hashMap);
                        }

                        Show_Dialog_for_Inside_Crates(List_InsideCrates, CrateName, CrateId);
                    } else {  //means crate not contains other crate inside it.

                        if ((SelectedCrateList.size() - 1) == crateNumber) {

                         hideprogressdialog();
                            mains.setVisibility(View.VISIBLE);
                        } else {
                            crateNumber = crateNumber + 1;
                            if (SelectedCrateList.size() > crateNumber) {
                                Check_This_Crate_Packed_In_Other_Crate();
                            }
                        }

                    }

                } catch (Exception e) {

                   hideprogressdialog();
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
           hideprogressdialog();

            }
        });
        AppController.getInstance().addToRequestQueue(bb);
    }

    public void Show_Dialog_for_Inside_Crates(ArrayList<HashMap<String, String>> List_InsideCrates, final String MainCrateName, final String MainCrateId) {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        Button Btn_No = (Button) showd.findViewById(R.id.Btn_No);
        Button Btn_Yes = (Button) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView txt_message = (TextView) showd.findViewById(R.id.texrtdesc);


        String InsideCrates = "";
        for (int i = 0; i < List_InsideCrates.size(); i++) {
            HashMap<String, String> hashMap = List_InsideCrates.get(i);
            InsideCrates = InsideCrates + hashMap.get("CrateName") + ",";
        }

        InsideCrates = InsideCrates.substring(0, InsideCrates.lastIndexOf(","));


        String message = "The Crate " + MainCrateName + " contains Crate(s):" + InsideCrates + ".\n" + "Do you want to move this Crate?";
        txt_message.setText(message);
        Btn_Yes.setText("Yes");
        Btn_No.setText("No");


        Btn_Yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    if ((SelectedCrateList.size() - 1) == crateNumber) {

                     hideprogressdialog();
                        mains.setVisibility(View.VISIBLE);
                    } else {
                        crateNumber = crateNumber + 1;
                        if (SelectedCrateList.size() > crateNumber) {
                            Check_This_Crate_Packed_In_Other_Crate();
                        }
                    }

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


                if ((SelectedCrateList.size() - 1) == crateNumber) {
                  hideprogressdialog();


                    mains.setVisibility(View.VISIBLE);
                } else {
                    crateNumber = crateNumber + 1;
                    if (SelectedCrateList.size() > crateNumber) {
                        Check_This_Crate_Packed_In_Other_Crate();
                    }
                }
            }
        });

        Btn_No.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                //remove the inside crate from the list
                for (int i = 0; i < noteList.size(); i++) {
                    String crateId = noteList.get(i).get("id");
                    if (crateId.equals(MainCrateId)) {
                        noteList.remove(i);
                    }
                }

                Dialog_Show_All_Crates();


            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    private void multipartImageUpload() {
        if (Count_Image_Uploaded < list_path.size())
            Count_Image_Uploaded++;
        mProgressHUD = ProgressHUD.show(context, "Uploading " + Count_Image_Uploaded + "/" + list_path.size()+", wait..." , false);
        list_UploadImageName.clear();
        totalSize = 0;


        try {
            /**/
            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[list_path.size()];
            for (int index = 0; index < list_path.size(); index++) {

                String path = list_path.get(index);
                File file = new File(path);
                ProgressRequestBody surveyBody = new ProgressRequestBody(file, this);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("images[]", file.getName(), surveyBody);
                long Size = file.length();
                totalSize = totalSize + Size;
                list_imageSize.add(String.valueOf(Size / 1000));
            }
            String jid = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);
            String url = URL_EP2 + "/UploadFileHandler.ashx?jid=" + jid;


            API_Interface APIInterface = REST_API_Client.getClient().create(API_Interface.class);

            Call<ResponseBody> req = APIInterface.uploadMedia(surveyImagesParts, url);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    String result = "";
                    Count_Image_Uploaded = 0;
                    if (String.valueOf(response.code()).equals("200")) {
                        try {
                           // uploadProgressDialog.setProgress(100);
                            String responseStr = response.body().string();
                            if (!responseStr.contains("api_error")) {
                                String s[] = responseStr.split(",");
                                List<String> stringList = new ArrayList<String>(Arrays.asList(s)); //new Ar
                                list_UploadImageName = new ArrayList<String>(stringList);

                                if (list_UploadImageName != null & list_UploadImageName.size() > 0 & (!list_UploadImageName.get(0).equalsIgnoreCase(""))) {
                                    result = "1";
                                } else {
                                    result = "0";
                                }

                            } else {
                                result = "0";
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            result = "0";
                        }


                    } else {

                        result = "0";
                    }

                    for (int i = 0; i < list_path.size(); i++) {
                        String TempImagePath = list_path.get(i);
                        if (TempImagePath.contains("TempImage-")) {
                            Utility.delete(TempImagePath);
                        }
                    }




                    if (result.equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), "Photo uploaded successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        hideprogressdialog();
                        Toast.makeText(getApplicationContext(), " Upload Failed!", Toast.LENGTH_SHORT).show();
                        dialog_photo_upload_failed();
                    }



                    /**/
                    if (result.equalsIgnoreCase("1")) {

                        String techName = Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                        String selectedCratesId = "";
                        for (int i = 0; i < SelectedCrateList.size(); i++) {
                            selectedCratesId = selectedCratesId + SelectedCrateList.get(i).get("id") + ",";
                        }
                        selectedCratesId = selectedCratesId.substring(0, selectedCratesId.length() - 1);
                        webhitforupdate_photo = URL_EP1 + "/crate_with_client.php?crate=" + selectedCratesId + "&image=" + URL_EP2 + "/upload/" + list_UploadImageName.get(0) + "&tech_id=" + techName;//google.com";
                        webhitforupdate_photo = webhitforupdate_photo.replaceAll(Pattern.quote(" "), "%20");
                        Log.d("--Client is leaving---", webhitforupdate_photo);
                        String name = Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                        String clid = Shared_Preference.getLOGIN_USER_ID(ClientLeavingWithCrate.this);
                        //String Jobid_or_swoid = Shared_Preference.getSWO_ID(ClientLeavingWithCrate.this);
                        String Jobid_or_swoid = Shared_Preference.getJOB_ID_FOR_JOBFILES(ClientLeavingWithCrate.this);

                        if (new ConnectionDetector(ClientLeavingWithCrate.this).isConnectingToInternet()) {
                            new updateonep1().execute(Jobid_or_swoid, "Client POD", list_UploadImageName.get(0), "Client POD", String.valueOf(list_imageSize.get(0)), name, clid, "" + "3");
                        } else {
                            Toast.makeText(ClientLeavingWithCrate.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                        }

                    }


                    list_path.clear();
                    //	list_ImageDescData.clear();
                    list_imageSize.clear();
                    list_UploadImageName.clear();
                    /**/


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                   hideprogressdialog();
                    Count_Image_Uploaded = 0;
                    list_path.clear();
                    Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    dialog_photo_upload_failed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dialog_photo_upload_failed() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientLeavingWithCrate.this);
        LayoutInflater inflater = LayoutInflater.from(ClientLeavingWithCrate.this);
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
        title.setText("Failed!");
        message.setText("Image(s) not uploaded!");
        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
                startActivity(new Intent(ClientLeavingWithCrate.this, Clock_Submit_Type_Activity.class));

            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    @Override
    public void onProgressUpdate(int percentage) {
        // textView.setText(percentage + "%");
      //  uploadProgressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {
        //  textView.setText("Uploaded Failed!");
    }

    @Override
    public void onFinish() {
        if (Count_Image_Uploaded < list_path.size())
            Count_Image_Uploaded++;
     //   uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_path.size() + ", Please wait..");
        mProgressHUD.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_path.size()+", wait..." );
    }

    @Override
    public void uploadStart() {

    }

    public class updateonep1 extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
           // hideprogressdialog();
           // super.onPostExecute(result);
            eventgenerate_for_photo();

        }

        @Override
        protected Void doInBackground(String... params) {

            enterdata(params[0], params[1], params[2], params[3], params[4],
                    params[5], params[6], params[7]);

            generate_event(params[0], params[6], params[7], "1");

            return null;
        }

    }

    public void eventgenerate_for_photo() {

        //showprogressdialog();
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET,
                webhitforupdate_photo, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                hideprogressdialog();
                StopClock();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

                hideprogressdialog();
                StopClock();
            }
        });

        AppController.getInstance().addToRequestQueue(bb);
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



