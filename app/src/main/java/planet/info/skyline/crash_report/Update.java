package planet.info.skyline.crash_report;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

import planet.info.skyline.R;


public class Update extends Activity {
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Button download = (Button) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                //  new deleteData().execute();
                finish();
            }
        });
        Button btn_cancel = (Button) findViewById(R.id.cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    class deleteData extends AsyncTask<String, Void, ArrayList<String>> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            try {
                File destDir1 = new File("/data/data/"
                        + Update.this.getPackageName()
                        + "/databases/" + "DBName.sqlite");
                if (destDir1.exists()) {
                    destDir1.delete();
                    File destDir = new File("/data/data/"
                            + Update.this.getPackageName()
                            + "/databases");
                    if (destDir.exists()) {
                        destDir.delete();
                    }
                }        // current activity

            } catch (Exception er) {
                er.getMessage();
            }
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
           /* try{
                Intent intent=new Intent(Update.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }catch(Exception er){
                er.getMessage();
            }*/
        }

    }
}
