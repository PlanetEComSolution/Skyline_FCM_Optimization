package planet.info.skyline.tech.job_files_new;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
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

import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.tech.fullscreenview.FullscreenImageView;
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.util.FileDownloader;
import planet.info.skyline.util.Utility;

import static android.content.Context.MODE_PRIVATE;
import static planet.info.skyline.network.Api.API_Bind_Job_client11;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;
import static planet.info.skyline.util.Utility.LOADING_TEXT;


public class ShowClientFiles_Fragment extends Fragment {


    View rootView;
    String jobtxt_id, tab;
    TextView tv_msg;


    ArrayList<HashMap<String, String>> List_ClientFiles = new ArrayList<>();
    HashMap<String, String> map1;
    AlertDialog alertDialog;
    SwipeRefreshLayout pullToRefresh;

  //  SharedPreferences sp;


    Context context;
    ProgressHUD mProgressHUD;
    private RecyclerView jobFiles_recyclerView;
    // ListView jobs_list_View;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_client_files, container, false);
        context=getActivity();
        jobFiles_recyclerView = (RecyclerView) rootView.findViewById(R.id.jobFiles_recyclerView);
        jobFiles_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        jobFiles_recyclerView.setItemAnimator(new DefaultItemAnimator());

        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
      //  sp = getActivity().getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);


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


    public void fettch_job_by_Client_list()//methord for job by client
    {
        List_ClientFiles = new ArrayList<>();

        int index = 1;
        //  count = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = SOAP_API_Client.BASE_URL;
        final String SOAP_ACTION = KEY_NAMESPACE + API_Bind_Job_client11;
        final String METHOD_NAME = API_Bind_Job_client11;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("job", jobtxt_id);
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
            if (recved.contains("No file Available.")) {
                //   count = 1;

            } else {
                recved = recved.substring(recved.indexOf("=") + 1, recved.lastIndexOf(";"));
                JSONObject jsonObject = new JSONObject(recved);


                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    map1 = new HashMap<>();
                    map1.clear();
                    index = 1 + i;
                    String index1 = Integer.toString(index);
                    String clientnme = jsonObject1.getString("Source");
                    String txt_Job = jsonObject1.getString("txt_C_Name");
                    String int_job_file_id = jsonObject1.getString("INT_FID_client");
                    String int_job_id = jsonObject1.getString("job_id_client");
                    String var_file_name1 = jsonObject1.getString("TXT_Filename_client");
                    String var_file_name = jsonObject1.getString("DTR_SubmitDate_client");
                    String var_file_des = jsonObject1.getString("VCHAR_Heading_client");

                    map1.put("index1", index1);
                    map1.put("clientnme", clientnme);
                    map1.put("txt_Job", txt_Job);
                    map1.put("int_job_file_id", int_job_file_id);
                    map1.put("int_job_id", int_job_id);
                    map1.put("var_file_name1", var_file_name1);
                    map1.put("var_file_name", var_file_name);
                    map1.put("var_file_des", var_file_des);
                    List_ClientFiles.add(map1);

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

            //Client Files
            fettch_job_by_Client_list();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

         showprogressdialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


hideprogressdialog();


                if (List_ClientFiles.size() < 1) {
                    tv_msg.setVisibility(View.VISIBLE);
                } else {
                    tv_msg.setVisibility(View.GONE);
                }
                ClientFilesAdapter clientFilesAdapter = new ClientFilesAdapter(getActivity(), List_ClientFiles);
                jobFiles_recyclerView.setAdapter(clientFilesAdapter);
                if (pullToRefresh.isRefreshing()) {
                    pullToRefresh.setRefreshing(false);
                }

        }


        public class ClientFilesAdapter extends RecyclerView.Adapter<ClientFilesAdapter.MyViewHolder> {
            List<HashMap<String, String>> beanArrayList;
            Context context;

            public ClientFilesAdapter(Context context, List<HashMap<String, String>> beanArrayList) {
                this.context = context;
                this.beanArrayList = beanArrayList;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_job_files_new, parent, false);

                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, final int i) {


                final String index1 = beanArrayList.get(i).get("index1");
                final String clientnme = beanArrayList.get(i).get("clientnme");
                final String txt_Job = beanArrayList.get(i).get("txt_Job");
                final String job_file_id = beanArrayList.get(i).get("int_job_file_id");
                final String job_id = beanArrayList.get(i).get("int_job_id");
                final String file_name1 = beanArrayList.get(i).get("var_file_name1");
                final String file_name = beanArrayList.get(i).get("var_file_name");
                final String file_des = beanArrayList.get(i).get("var_file_des");


                holder.index_no.setText(String.valueOf(i + 1));
                holder.checkBox.setVisibility(View.GONE);
                if (clientnme.equals(null) || clientnme.trim().equals("")) {
                    holder.Source.setText("Not available");
                } else {
                    holder.Source.setText(clientnme);
                }
                if (txt_Job.equals(null) || txt_Job.trim().equals("")) {
                    holder.user_name.setText("Not available");
                } else {
                    holder.user_name.setText(txt_Job);
                }
                //nks
                // holder.file_upload.setText(file_name1);
                if (file_des.equals(null) || file_des.trim().equals("")) {
                    holder.file_upload.setText("Not available");
                } else {
                    holder.file_upload.setText(file_des.trim());
                }

                if (file_name.equals(null) || file_name.trim().equals("")) {
                    holder.date_upload.setText("Not available");
                } else {
                    holder.date_upload.setText(file_name);
                }
                //nks

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

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
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
                        title.setText("Please select the action!");
                        message.setText("Press Back to cancel!");
                        positiveBtn.setText("View");
                        negativeBtn.setText("Download");
                        negativeBtn.setVisibility(View.VISIBLE);
                        positiveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();

                                Utility. view_downloadFile(file_name1,getActivity());
                            }
                        });
                        negativeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();

                                Utility. view_downloadFile(file_name1,getActivity());
                            }
                        });
                        alertDialog = dialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();




                    }
                });

            }

            @Override
            public int getItemCount() {
                return beanArrayList.size();
            }

            public class MyViewHolder extends RecyclerView.ViewHolder {

                TextView Source, user_name, file_upload, date_upload;
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


                }
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
