package planet.info.skyline.tech.billable_timesheet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.AppConstants;
import planet.info.skyline.util.Utility;
import planet.info.skyline.util.Utils;

public class Clock_Submit_Type_Activity extends BaseActivity {
    ImageView imgvw_Company_Logo, homeimg;
    TextView clientname;
    Button btn_StopWork, btn_FinishWork, btnChangeTimeCode, btn_PauseWork;
  //  ProgressDialog pDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_danage_refurbish_new);

        btn_PauseWork = findViewById(R.id.btn_PauseWork);
        btn_StopWork = findViewById(R.id.btn_StopWork);
        btn_FinishWork = findViewById(R.id.btn_FinishWork);
        btnChangeTimeCode = findViewById(R.id.btnChangeTimeCode);

        homeimg = (ImageView) findViewById(R.id.homeimg);

        homeimg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Clock_Submit_Type_Activity.this, MainActivity.class));
                finish();

            }
        });

        imgvw_Company_Logo =  findViewById(R.id.imgvw_Company_Logo);
        imgvw_Company_Logo.setVisibility(View.VISIBLE);
        clientname =  findViewById(R.id.txtvw_Company_Name);
        Utils.DisplayClientLogo(this,clientname,imgvw_Company_Logo);


              btn_PauseWork.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {
                    StopClock(AppConstants.PAUSE);
                } else {
                    Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });
        btn_StopWork.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//stop

                if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {

                    StopClock(AppConstants.STOP);
                } else {
                    Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });


        btnChangeTimeCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {

                    StopClock(AppConstants.CHANGE_TIME_CODE);

                } else {
                    Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });


        btn_FinishWork.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {
                    dialog_FinishWithJob();
                } else {
                    Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Clock_Submit_Type_Activity.this, MainActivity.class));
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Utility.showChatHead(Clock_Submit_Type_Activity.this);
    }




    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.danage_refurbish, menu);
        return true;
    }

    public void dialog_FinishWithJob() {


        final Dialog showd = new Dialog(Clock_Submit_Type_Activity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.alerfinishwithjob_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(true);
        showd.setCanceledOnTouchOutside(false);
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        TextView NoCrate = (TextView) showd.findViewById(R.id.noCrate);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        nofo.setOnClickListener(new OnClickListener() {
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
        NoCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {
                    StopClock(AppConstants.FINISH);

                } else {
                    Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }

            }
        });
        yesfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Clock_Submit_Type_Activity.this, ClientLeavingWithCrate.class));
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

 void StopClock(String type){
   //  Intent i = new Intent(getApplicationContext(), SubmitTimesheet.class);
     Intent i = new Intent(getApplicationContext(), SubmitTimesheetNew.class);
     i.putExtra(AppConstants.TYPE,type);
     startActivity(i);
     finish();
 }


}
