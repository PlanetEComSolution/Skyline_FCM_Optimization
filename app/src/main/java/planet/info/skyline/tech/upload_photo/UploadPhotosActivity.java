package planet.info.skyline.tech.upload_photo;

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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import planet.info.skyline.R;
import planet.info.skyline.RequestControler.MyAsyncTask_MultiApiCall;
import planet.info.skyline.RequestControler.ResponseInterface_MultiApiCall;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.model.ImageItem;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.Api;
import planet.info.skyline.network.ProgressRequestBody;
import planet.info.skyline.network.REST_API_Client;
import planet.info.skyline.util.AppConstants;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivityNew;
import planet.info.skyline.tech.share_photos.SharePhotosToClientActivity;
import planet.info.skyline.util.CameraUtils;
import planet.info.skyline.util.Utility;
import planet.info.skyline.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class UploadPhotosActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks, ResponseInterface_MultiApiCall {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static String imageStoragePath;
    Adapter_UploadPhotos userAdapter;
    int Count_Image_Uploaded = 0;
    AlertDialog alertDialog;
    String JobId;
    ArrayList<ImageItem> listPhotos = new ArrayList<>();
    ProgressHUD mProgressHUD;
    ArrayList<HashMap<String, String>> list_UploadedImageID = new ArrayList<>();
    LinearLayout ll_btn;
    private Context mContext;
    private RecyclerView recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.upload_photo_activity);
        mContext = UploadPhotosActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Utility.getTitle("Upload Photo(s)"));
        final boolean TIMER_STARTED_FROM_BILLABLE_MODULE = Shared_Preference.getTIMER_STARTED_FROM_BILLABLE_MODULE(this);
        init();
        if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
            JobId = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);
           // String compID = Shared_Preference.getCOMPANY_ID_BILLABLE(this);
           // String company_Name = Shared_Preference.getCLIENT_NAME(this);
           // String job_Name = Shared_Preference.getJOB_NAME_BILLABLE(this);

            opendilogforattachfileandimage();

        } else {
            Intent i = new Intent(this, SelectCompanyActivityNew.class);
            i.putExtra(Utility.IS_JOB_MANDATORY, "1");
            i.putExtra(Utility.Show_DIALOG_SHOW_INFO, true);
            startActivityForResult(i, Utility.CODE_SELECT_COMPANY);
        }


        // setToolbar();
    }

    private void init() {


        ArrayList<ImageItem> ImageItemList = new ArrayList<>();
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        userAdapter = new Adapter_UploadPhotos(mContext, ImageItemList);
        recycler.setAdapter(userAdapter);

        ll_btn = findViewById(R.id.ll_btn);
        ll_btn.setVisibility(View.GONE);
        Button btn_add = findViewById(R.id.btn_AddItem);
        Button btn_getData = findViewById(R.id.btn_Submit);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendilogforattachfileandimage();
            }
        });
        btn_getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPhotos = userAdapter.returnData();
                if (listPhotos != null) {
                    multipartImageUpload();
                }

            }
        });

    }

    private void CallApi_LinkUploadedPhotos() {
        JSONArray jsonArray = new JSONArray();

        for (ImageItem ImageItem : listPhotos) {
            try {
                JSONObject jsonObject1 = new JSONObject();
                JSONObject jsonObject_Input = new JSONObject();
                jsonObject_Input.put("job_swo_id", Shared_Preference.getJOB_ID_FOR_JOBFILES(this));
                jsonObject_Input.put("description", ImageItem.getDescription());
                jsonObject_Input.put("filename", ImageItem.getUploadedImageId());
                jsonObject_Input.put("comment", ImageItem.getDescription());
                File file = new File(ImageItem.getImageURI());
                jsonObject_Input.put("filesize", file.length());
                jsonObject_Input.put("tech_name", Shared_Preference.getLOGIN_USERNAME(this));
                jsonObject_Input.put("tech_id", Shared_Preference.getLOGIN_USER_ID(this));
                jsonObject_Input.put("type", "3");
                jsonObject1.put(Api.API_UploadPhotoTech, jsonObject_Input);
                jsonArray.put(jsonObject1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject_Input = new JSONObject();
            jsonObject_Input.put("job_swo_id", Shared_Preference.getJOB_ID_FOR_JOBFILES(this));
            jsonObject_Input.put("tech_id", Shared_Preference.getLOGIN_USER_ID(this));
            jsonObject_Input.put("type", "3");
            jsonObject_Input.put("count", String.valueOf(listPhotos.size()));
            jsonObject1.put(Api.API_UploadPhotoTech_new, jsonObject_Input);
            jsonArray.put(jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (new ConnectionDetector(UploadPhotosActivity.this).isConnectingToInternet()) {
            new MyAsyncTask_MultiApiCall(this, false, this, jsonArray).execute();
        } else {
            Toast.makeText(UploadPhotosActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
                    for (int i = 0; i < images.size(); i++) {
                        userAdapter.addItemToList(images.get(i).path);
                    }
                } else {
                    File file1 = null;
                    Uri mImageCaptureUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file1);
                    try {
                        userAdapter.addItemToList(Utils.getPath(mImageCaptureUri, UploadPhotosActivity.this));


                    } catch (Exception e) {
                        userAdapter.addItemToList(mImageCaptureUri.getPath());
                    }
                }


            } else if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                int orientation = Utility.getExifOrientation(imageStoragePath);
                try {
                    if (orientation == 90) {
                        Bitmap bmp = Utility.getRotatedBitmap(imageStoragePath, 90);
                        imageStoragePath = Utility.saveImage(bmp);
                    }
                } catch (Exception e) {
                    e.getCause();
                }
                userAdapter.addItemToList(imageStoragePath);
            } else if (requestCode == Utility.CODE_SELECT_COMPANY) {
                try {
                    String compID = data.getStringExtra("CompID");
                    String jobID = data.getStringExtra("JobID");
                    String company_Name = data.getStringExtra("CompName");
                    String job_Name = data.getStringExtra("JobName");
                    Shared_Preference.setCOMPANY_ID_BILLABLE(this, compID);
                    Shared_Preference.setJOB_ID_FOR_JOBFILES(this, jobID);

                    JobId = jobID;
                    opendilogforattachfileandimage();
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "Exception caught!", Toast.LENGTH_SHORT).show();
                }

            }

        }else  if(resultCode==RESULT_CANCELED){
            if (userAdapter.getItemCount() == 0) {
                finishs();
            }
        }

    }

    private void multipartImageUpload() {
        final ArrayList<String> list_UploadImageName = new ArrayList<>();
        if (Count_Image_Uploaded < listPhotos.size()) Count_Image_Uploaded++;
        // totalSize = 0;
        mProgressHUD = ProgressHUD.show(mContext, "Uploading " + Count_Image_Uploaded + "/" + listPhotos.size() + ", Please wait...", false);

        try {
            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[listPhotos.size()];
            for (int index = 0; index < listPhotos.size(); index++) {
                String path = listPhotos.get(index).getImageURI();
                File file = new File(path);
                ProgressRequestBody surveyBody = new ProgressRequestBody(file, this);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("images[]", file.getName(), surveyBody);
              //  long Size = file.length();
                // totalSize = totalSize + Size;
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
                                userAdapter.updateUploadedImageIds(list_UploadImageName);
                                CallApi_LinkUploadedPhotos();

                            } else {
                                Toast.makeText(mContext, "Uploading failed!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.getMessage();

                        }

                    }

                    if (String.valueOf(response.code()).equals("200")) {
                       // Toast.makeText(getApplicationContext(), "Photo uploaded successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), " Upload Failed!", Toast.LENGTH_SHORT).show();
                        dialog_photo_upload_failed();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        if (mProgressHUD.isShowing()) {
                            mProgressHUD.dismiss();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    Count_Image_Uploaded = 0;
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
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        LinearLayout cameralayout = dialog
                .findViewById(R.id.cameralayout);
        LinearLayout gallarylayout = dialog
                .findViewById(R.id.gallarylayout);
        LinearLayout filelayout = dialog
                .findViewById(R.id.filelayout);
        ImageView crosse = dialog
                .findViewById(R.id.close);
        crosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(userAdapter.getItemCount()==0){
                    finishs();
                }
            }
        });
        cameralayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (CameraUtils.checkPermissions(mContext)) {
                    captureImage();
                } else {
                    Toast.makeText(mContext, "Camera & Write External Storage permission are disabled!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gallarylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumSelectActivity.class);
                intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 50);
                startActivityForResult(intent, AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                // finishs();
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
        if (mContext instanceof UploadPhotosActivity) {
            ((UploadPhotosActivity) mContext).startActivityForResult(intent, AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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

    public void onItemRemoved() {
        if (userAdapter.getItemCount() == 0) {
            finishs();
        }
    }

    public void onItemAdded() {
        if (userAdapter.getItemCount() > 0) {
            if (ll_btn.getVisibility() == View.GONE) {
                ll_btn.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        // textView.setText(percentage + "%");
        //   uploadProgressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {
    }

    @Override
    public void onFinish() {
        if (Count_Image_Uploaded < listPhotos.size())
            Count_Image_Uploaded++;
        mProgressHUD.setMessage("Uploading " + Count_Image_Uploaded + "/" + listPhotos.size() + ", Please wait...");
    }

    @Override
    public void uploadStart() {
        //  textView.setText("0%");
        //  }
    }


    @Override
    public void handleMultiApiResponse(JSONArray responseJsonArray) {
        if (mProgressHUD.isShowing()) {
            mProgressHUD.dismiss();
        }
        String result = "";
        for (int i = 0; i < responseJsonArray.length(); i++) {
            try {
                JSONObject jsonObject = responseJsonArray.getJSONObject(i);
                String _ApiName = jsonObject.getString("Method_Name");
                if (_ApiName.equals(Api.API_UploadPhotoTech)) {
                    String Response = jsonObject.getString("Response");
                    JSONObject responseObj = new JSONObject(Response);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("int_job_file_id", responseObj.getString("cds"));
                    hashMap.put("ftype", "Project Photo(s)");
                    list_UploadedImageID.add(hashMap);
                }
                if (_ApiName.equals(Api.API_UploadPhotoTech_new)) {
                    String str_res = jsonObject.getString("Response");
                    JSONObject responseObj = new JSONObject(str_res);
                    result = responseObj.getString("cds");
                }

            } catch (Exception e) {
                e.getMessage();
            }
        }
        if (result.equalsIgnoreCase("st=1")) {
            dialog_sharePhotos();
        } else {
            dialog_photo_upload_failed();
        }


    }

    public void dialog_sharePhotos() {
        final Dialog showd = new Dialog(mContext);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_yes_no);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        showd.setCancelable(false);
        TextView Btn_No = showd.findViewById(R.id.Btn_No);
        TextView Btn_Yes = showd.findViewById(R.id.Btn_Yes);
        ImageView close = showd.findViewById(R.id.close);
        close.setVisibility(View.GONE);
        TextView texrtdesc = showd.findViewById(R.id.texrtdesc);
        texrtdesc.setText("Do you want to share these photos?");
        TextView textviewheader = showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Uploaded Successfully!");

        Btn_No.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showd.dismiss();
                finishs();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();
                finishs();

            }
        });
        Btn_Yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();
                Intent intent = new Intent(mContext, SharePhotosToClientActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) list_UploadedImageID);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
                finish();

            }
        });

        showd.show();


    }

    public void finishs() {
        Intent iiu = new Intent(mContext, MainActivity.class);
        iiu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iiu);
        finish();
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

    @Override
    protected void onResume() {
        super.onResume();
       /* if(userAdapter.getItemCount()==0){
            finishs();
        }*/
    }

    @Override
    public void onBackPressed() {
        finishs();
    }

    public void ShowConfirmation(final int adapterPosition) {
        final Dialog dialogView = new Dialog(mContext);
        dialogView.setContentView(R.layout.dialog_yes_no);
        dialogView.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final TextView title = dialogView.findViewById(R.id.textView1rr);
        final TextView message = dialogView.findViewById(R.id.texrtdesc);

        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        close.setVisibility(View.INVISIBLE);
        title.setText("Remove this item?");
        message.setText("Are you sure?");
        positiveBtn.setText("Yes");
        negativeBtn.setText("No");

        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.dismiss();
                userAdapter.removeItem(adapterPosition);
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.dismiss();

            }
        });
        dialogView.show();

    }

}