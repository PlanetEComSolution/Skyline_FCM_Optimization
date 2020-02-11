package planet.info.skyline.tech.damage_report;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import planet.info.skyline.R;
import planet.info.skyline.RequestControler.MyAsyncTask_MultiApiCall;
import planet.info.skyline.RequestControler.ResponseInterface_MultiApiCall;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.DamageDetail;
import planet.info.skyline.model.ItemType;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.Api;
import planet.info.skyline.network.ProgressRequestBody;
import planet.info.skyline.network.REST_API_Client;
import planet.info.skyline.util.AppConstants;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.CameraUtils;
import planet.info.skyline.util.Utility;
import planet.info.skyline.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class DamageReportNew extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks, ResponseInterface_MultiApiCall {


    public static final int MEDIA_TYPE_IMAGE = 1;
    private static String imageStoragePath;
    Adapter_DamageReport userAdapter;
    ArrayList<String> list_path = new ArrayList<>();

  //  ProgressDialog uploadProgressDialog;
    int Count_Image_Uploaded = 0;
    long totalSize = 0;
    AlertDialog alertDialog;
    private Context mContext;
    private RecyclerView recycler;
       ProgressHUD mProgressHUD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.damage_report_new);
        mContext = DamageReportNew.this;
        init();
        setToolbar();
    }

    private void init() {
        ArrayList<DamageDetail> damageDetailList = new ArrayList<>();
        damageDetailList.add(DamageDetail.getInitDamageDetail());
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));

        userAdapter = new Adapter_DamageReport(mContext, damageDetailList);
        recycler.setAdapter(userAdapter);
        Button btn_add = findViewById(R.id.btn_AddItem);
        Button btn_getData = findViewById(R.id.btn_FinishedWithReport);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAdapter.addItemToList();
            }
        });
        btn_getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<DamageDetail> list = userAdapter.returnData();
                if (list != null) {
                    PostDamageReport(list);
                }

            }
        });

    }

    private void PostDamageReport(ArrayList<DamageDetail> listdamageDetail) {
        JSONArray jsonArray = new JSONArray();
        for (DamageDetail damageDetail : listdamageDetail) {
            try {
                JSONObject jsonObject_Input = new JSONObject();
                jsonObject_Input.put("swo_id", Shared_Preference.getSWO_ID(this));
                jsonObject_Input.put("emp_id", Shared_Preference.getLOGIN_USER_ID(this));
                jsonObject_Input.put("desc", "Damage Report to " + ItemType.getItemTypeByPosition(damageDetail.getSpinnerSelectPos()) + ": " + damageDetail.getItemDesc()
                        + ": " + damageDetail.getDamageDesc());
                jsonObject_Input.put("fname", damageDetail.getUploadedPhotoUrl());
                if (Shared_Preference.get_EnterTimesheetByAWO(DamageReportNew.this)) {
                    jsonObject_Input.put("Type",  Utility.TYPE_AWO);
                } else {
                    jsonObject_Input.put("Type",  Utility.TYPE_SWO);
                }
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put(Api.API_add_item_descwithFileByType, jsonObject_Input);
                jsonArray.put(jsonObject1);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (new ConnectionDetector(DamageReportNew.this).isConnectingToInternet()) {
            new MyAsyncTask_MultiApiCall(this,true, this, jsonArray).execute();
        } else {
            Toast.makeText(DamageReportNew.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }


    private void setToolbar() {
        TextView txtvw_CompanyName = (TextView) findViewById(R.id.textView1);
        ImageView imgvw_CompanyImg = (ImageView) findViewById(R.id.missing);
        Utils.DisplayClientLogo(this,txtvw_CompanyName,imgvw_CompanyImg);
        ImageView img_Home = (ImageView) findViewById(R.id.homeacti);
        img_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            list_path.clear();
            if (requestCode == AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
                    for (int i = 0; i < images.size(); i++) {
                        list_path.add(images.get(i).path);
                    }
                    multipartImageUpload();
                } else {
                    File file1 = null;
                    Uri mImageCaptureUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file1);
                    try {
                        list_path.add(Utils.getPath(mImageCaptureUri,
                                DamageReportNew.this));
                        multipartImageUpload();

                    } catch (Exception e) {
                        list_path.add(mImageCaptureUri.getPath());
                        multipartImageUpload();
                    }
                }


            } else if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                int orientation = Utility.getExifOrientation(imageStoragePath);
                try {
                    if (orientation == 90) {
                        //   list_TempImagePath.add(path);
                        Bitmap bmp = Utility.getRotatedBitmap(imageStoragePath, 90);
                        imageStoragePath = Utility.saveImage(bmp);
                    }
                } catch (Exception e) {
                    e.getCause();
                }
                list_path.add(imageStoragePath);
                multipartImageUpload();
            }
        }

    }

    private void multipartImageUpload() {
        final ArrayList<String> list_UploadImageName = new ArrayList<>();

        if (Count_Image_Uploaded < list_path.size())
            Count_Image_Uploaded++;
      /*  uploadProgressDialog = new ProgressDialog(mContext);
        uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_path.size() + ", Please wait..");
        uploadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        uploadProgressDialog.setIndeterminate(false);
        uploadProgressDialog.setProgress(0);
        uploadProgressDialog.setMax(100);
        uploadProgressDialog.setCancelable(false);
        uploadProgressDialog.show();*/
        mProgressHUD = ProgressHUD.show(mContext, "Uploading " + Count_Image_Uploaded + "/" + list_path.size()+", wait..." , false);


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

            }

            String jid = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);
            String url = URL_EP2 + "/UploadFileHandler.ashx?jid=" + jid;
            /**/

            API_Interface APIInterface = REST_API_Client.getClient().create(API_Interface.class);

            Call<ResponseBody> req = APIInterface.uploadMedia(surveyImagesParts, url);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Count_Image_Uploaded = 0;
                    if (String.valueOf(response.code()).equals("200")) {
                        try {
                          //  uploadProgressDialog.setProgress(100);
                            String responseStr = response.body().string();
                            if (!responseStr.contains("api_error")) {
                                if (responseStr.contains(",")) {
                                    String s[] = responseStr.split(",");
                                    List<String> stringList = new ArrayList<String>(Arrays.asList(s));
                                    list_UploadImageName.clear();
                                    list_UploadImageName.addAll(stringList);
                                } else {
                                    list_UploadImageName.add(responseStr);
                                }
                                //only one image will be use in api
                                if (list_UploadImageName.size() > 0) {
                                    userAdapter.setUploadedImageURL(list_UploadImageName.get(0));
                                } else {
                                    Toast.makeText(mContext, "No url found!", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(mContext, "Uploading failed!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.getMessage();

                        }

                    }


                    for (int i = 0; i < list_path.size(); i++) {
                        String TempImagePath = list_path.get(i);
                        if (TempImagePath.contains("TempImage-")) {
                            Utility.delete(TempImagePath);
                        }
                    }

                    list_path.clear();

                    /*try {
                        uploadProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }*/
                    if(mProgressHUD.isShowing()){
                        mProgressHUD.dismiss();
                    }


                    if (String.valueOf(response.code()).equals("200")) {
                        Toast.makeText(getApplicationContext(), "Photo uploaded successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), " Upload Failed!", Toast.LENGTH_SHORT).show();
                        dialog_photo_upload_failed();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                  /*  try {
                        if (uploadProgressDialog.isShowing())
                            uploadProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }*/
                    if(mProgressHUD.isShowing()){
                        mProgressHUD.dismiss();
                    }

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

    public void opendilogforattachfileandimage() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.openattachmentdilog_new);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout cameralayout = (LinearLayout) dialog
                .findViewById(R.id.cameralayout);
        LinearLayout gallarylayout = (LinearLayout) dialog
                .findViewById(R.id.gallarylayout);
        LinearLayout filelayout = (LinearLayout) dialog
                .findViewById(R.id.filelayout);
        ImageView crosse = (ImageView) dialog
                .findViewById(R.id.close);
        crosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });
        cameralayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (CameraUtils.checkPermissions(mContext)) {

                    captureImage();

                } else {
                    Toast.makeText(mContext, "Camera & Write_External_Storage permission are disabled!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        gallarylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumSelectActivity.class);
                intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 1);
                startActivityForResult(intent, AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                // finish();
            }
        });
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(mContext, file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (mContext instanceof DamageReportNew) {
            ((DamageReportNew) mContext).startActivityForResult(intent, AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }

    }

    private void dialog_photo_upload_failed() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
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

        title.setText("Failed!");
        message.setText("Image(s) not uploaded!");
        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
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

    public void ScrollRecyclerviewToBottom(int height) {
        recycler.smoothScrollToPosition(height);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        // textView.setText(percentage + "%");
       // uploadProgressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        if (Count_Image_Uploaded < list_path.size())
            Count_Image_Uploaded++;
      //  uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_path.size() + ", Please wait..");
        mProgressHUD.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_path.size()+", wait..." );

    }

    @Override
    public void uploadStart() {
        //  textView.setText("0%");
    }

    @Override
    public void handleMultiApiResponse(JSONArray responseJsonArray) {

        String result = "";
        for (int i = 0; i < responseJsonArray.length(); i++) {
            try {
                JSONObject jsonObject=    responseJsonArray.getJSONObject(i);
                String _ApiName= jsonObject.getString("Method_Name");
                if (_ApiName.equals(Api.API_add_item_descwithFileByType)) {
                    String  str_res = jsonObject.getString("Response");
                    JSONObject responseObj = new JSONObject(str_res);
                    result= responseObj.getString("cds");
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

        if (result.equalsIgnoreCase("st=1")) {
            Toast.makeText(DamageReportNew.this, "Report submitted successfully !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DamageReportNew.this, "Report submitting failed!", Toast.LENGTH_SHORT).show();
        }
        finish();

    }
}