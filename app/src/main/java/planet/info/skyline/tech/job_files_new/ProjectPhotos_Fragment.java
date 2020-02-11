package planet.info.skyline.tech.job_files_new;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.tech.fullscreenview.FullscreenImageView;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.network.Api;
import planet.info.skyline.util.FileDownloader;
import planet.info.skyline.util.Utility;

import static android.content.Context.MODE_PRIVATE;
//import static planet.info.skyline.network.Api.API_GetJobFileByTextJob11;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;
import static planet.info.skyline.util.Utility.LOADING_TEXT;


public class ProjectPhotos_Fragment extends Fragment {


    View rootView;
    String jobtxt_id, tab;
    TextView tv_msg;
    List<HashMap<String, String>> list_ProjectPhotos = new ArrayList<>();

    ArrayList<HashMap<String, String>> List_JobFiles = new ArrayList<>();
    HashMap<String, String> map;


    AlertDialog alertDialog;
    SwipeRefreshLayout pullToRefresh;


    SharedPreferences sp;
    Context context;
    ProgressHUD mProgressHUD;

    private RecyclerView jobFiles_recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_job_files, container, false);

context=getActivity();
        jobFiles_recyclerView = (RecyclerView) rootView.findViewById(R.id.jobFiles_recyclerView);
        jobFiles_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        jobFiles_recyclerView.setItemAnimator(new DefaultItemAnimator());

        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
        sp = getActivity().getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);

        pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                    new get_jobFiles_Acyntask().execute();
                } else {
                    Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }

            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            jobtxt_id = bundle.getString("JobId", "");
            tab = bundle.getString("tab", "");
        }


        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
            new get_jobFiles_Acyntask().execute();
        } else {
            Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }
    public void fettch_job_list_New() {
        List_JobFiles.clear();
        list_ProjectPhotos.clear();
        int index = 1;
        int count = 0;

        final String User_Role = Shared_Preference.getUSER_ROLE(getActivity());
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";

        final String METHOD_NAME = Api.API_GetJobFileByTextJob11withrole;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("job", jobtxt_id);
        request.addProperty("Role", User_Role);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            String recved = ks.toString();
            if (recved.contains("Job not found")) {
                count = 1;
            } else {
                recved = recved.substring(recved.indexOf("=") + 1, recved.lastIndexOf(";"));
                JSONObject jsonObject = new JSONObject(recved);

                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        map = new HashMap<>();
                        map.clear();
                        index = 1 + i;
                        String index1 = Integer.toString(index);
                        String clientnme = jsonObject1.getString("clientnme");
                        String txt_Job = jsonObject1.getString("txt_Job");
                        String int_job_file_id = jsonObject1.getString("int_job_file_id");
                        String int_job_id = jsonObject1.getString("int_job_id");
                        String var_file_name1 = jsonObject1.getString("var_file_name1");
                        String var_file_name = jsonObject1.getString("var_file_name");
                        String var_file_des = jsonObject1.getString("var_file_des");
                        String int_rndm_id_file = jsonObject1.getString("int_rndm_id_file");
                        String dt_file_Udate = jsonObject1.getString("dt_file_Udate");
                        String ftype = jsonObject1.getString("ftype");
                        map.put("index1", index1);
                        map.put("clientnme", clientnme);
                        map.put("txt_Job", txt_Job);
                        map.put("int_job_file_id", int_job_file_id);
                        map.put("int_job_id", int_job_id);
                        map.put("var_file_name1", var_file_name1);
                        map.put("var_file_name", var_file_name);
                        map.put("var_file_des", var_file_des);
                        map.put("int_rndm_id_file", int_rndm_id_file);
                        map.put("dt_file_Udate", dt_file_Udate);
                        map.put("isSelected", "false");
                        map.put("ftype", ftype);
                        List_JobFiles.add(map);
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    class get_jobFiles_Acyntask extends AsyncTask<Void, Void, Void> {
       // ProgressDialog progressDoalog;

        @Override
        protected Void doInBackground(Void... params) {

           // fettch_job_list();
            fettch_job_list_New();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*
            progressDoalog = new ProgressDialog(getActivity());
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();*/
showprogressdialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
/*

            try {
                progressDoalog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
*/
hideprogressdialog();


            ArrayList<HashMap<String, String>> ProjectPhotosList = new ArrayList<>();

            for (int i = 0; i < List_JobFiles.size(); i++) {
                HashMap<String, String> hashMap = List_JobFiles.get(i);
                String fileType = hashMap.get("ftype");
                if (fileType.equals("Project Photo(s)")) {
                    ProjectPhotosList.add(hashMap);
                }
            }

            // 0=Job Files  ,
            // 1= Client Art or Project Photos,
            // 3=Project photos


                if (ProjectPhotosList.size() < 1) {
                    tv_msg.setVisibility(View.VISIBLE);
                } else {
                    tv_msg.setVisibility(View.GONE);
                }




                JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(getActivity(), ProjectPhotosList);
                jobFiles_recyclerView.setAdapter(jobFilesAdapter);



            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }
        }
    }


    public class JobFilesAdapter extends RecyclerView.Adapter<JobFilesAdapter.MyViewHolder> {

        List<HashMap<String, String>> beanArrayList;
        Context context;


        public JobFilesAdapter(Context context, List<HashMap<String, String>> beanArrayList) {
            this.context = context;
            this.beanArrayList = beanArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_job_files_new, parent, false);
            /*View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_job_files_new_1, parent, false);*/


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int i) {


            final String index1 = beanArrayList.get(i).get("index1");
            final String clientnme = beanArrayList.get(i).get("clientnme");
            final String txt_Job = beanArrayList.get(i).get("txt_Job");
            final String job_file_id = beanArrayList.get(i).get("int_job_file_id");
            final String job_id = beanArrayList.get(i).get("int_job_id");
            final String file_name1 = beanArrayList.get(i).get("var_file_name1");
            final String file_name = beanArrayList.get(i).get("var_file_name");
            final String file_des = beanArrayList.get(i).get("var_file_des");
            final String rndm_id_file = beanArrayList.get(i).get("int_rndm_id_file");
            final String file_Udate = beanArrayList.get(i).get("dt_file_Udate");
            final String isSelected = beanArrayList.get(i).get("isSelected");
            final String ftype = beanArrayList.get(i).get("ftype");

            holder.index_no.setText(String.valueOf(i + 1));



            if (txt_Job.equals(null) || txt_Job.trim().equals("")) {
                holder.Source.setText("Not available");
            } else {
                holder.Source.setText(txt_Job);

                if (ftype.equalsIgnoreCase("Project Photo(s)")
                        ||
                        ftype.equalsIgnoreCase("Client Art")

                ) {
                    holder.checkBox.setVisibility(View.VISIBLE);
                } else {
                    holder.checkBox.setVisibility(View.INVISIBLE);
                }
            }

            if (clientnme.equals(null) || clientnme.trim().equals("")) {
                holder.user_name.setText("Not available");
            } else {
                holder.user_name.setText(clientnme);
            }


            holder.checkBox.setTag(i);
            if (isSelected.equalsIgnoreCase("true")) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag(); // Here

                    HashMap<String, String> hashMap = beanArrayList.get(getPosition);
                    if (isChecked) {
                        hashMap.put("isSelected", "true");
                    } else {
                        hashMap.put("isSelected", "false");
                    }

                    //   beanArrayList.set(getPosition, hashMap);
                    //    setProjectPhotoList(beanArrayList);

                    ((JobFilesTabActivity)getActivity()).addProjectPhotos(hashMap);
                    list_ProjectPhotos.add(hashMap);

                }
            });

//nks

            if (file_des.equals(null) || file_des.trim().equals("")) {
                holder.file_upload.setText("Not available");
            } else {
                holder.file_upload.setText(file_des.trim());
            }

            //holder.file_upload.setText(file_name);
            //nks

            if (file_Udate.equals(null) || file_Udate.trim().equals("")) {
                holder.date_upload.setText("Not available");
            } else {
                holder.date_upload.setText(file_Udate);
            }

            holder.view_file.setText("View");
            holder.download_file.setText("Download");

            holder.download_file.setOnClickListener(new View.OnClickListener() {    ///for download a pdf
                @Override
                public void onClick(View view) {


                    Utility. view_downloadFile(file_name1,getActivity());

                }
            });


            holder.view_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Utility. view_downloadFile(file_name1,getActivity());

                }
            });


            holder.row_jobFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

        }

        @Override
        public int getItemCount() {
            return beanArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView Source, user_name, file_upload, date_upload;
          //  ProgressBar spinner;
         //   ImageView thumbnail;
            //nks
            Button index_no;
            Button view_file, download_file;
            LinearLayout row_jobFile;
            CheckBox checkBox;

            public MyViewHolder(View convertview) {
                super(convertview);

                index_no = (Button) convertview.findViewById(R.id.serial_no);
                Source = (TextView) convertview.findViewById(R.id.source);
                user_name = (TextView) convertview.findViewById(R.id.user);
                file_upload = (TextView) convertview.findViewById(R.id.flie_upload);
                date_upload = (TextView) convertview.findViewById(R.id.date_upload);
                view_file = (Button) convertview.findViewById(R.id.view_file);
                download_file = (Button) convertview.findViewById(R.id.download_file);
                row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);
                checkBox = (CheckBox) convertview.findViewById(R.id.checkBox);
               // thumbnail = (ImageView) convertview.findViewById(R.id.thumbnail);
              //  spinner = (ProgressBar) convertview.findViewById(R.id.progressBar1);
            }
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
