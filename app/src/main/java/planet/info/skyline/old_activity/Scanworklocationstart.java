package planet.info.skyline.old_activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;
import planet.info.skyline.util.Utils;


public class Scanworklocationstart extends BaseActivity {

	 String arecode;
	int count;
	int punnu=0;
	List<String> arename=new ArrayList<String>();
	List<String> area_code=new ArrayList<String>();
	 ProgressDialog ringProgressDialog ;
	AlertDialog alertDialog;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_scanworklocationstart);
		ringProgressDialog	= new ProgressDialog(Scanworklocationstart.this);
		ringProgressDialog.setMessage("Kindly Wait..");
		ringProgressDialog.setCancelable(false);


		diloge_for_workArea();
	}
	public void showscannewbin() {
		final Dialog showd = new Dialog(Scanworklocationstart.this);
		showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		showd.setContentView(R.layout.alerfinishwithjob_new);
		showd.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		showd.setCancelable(false);
		TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
		TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
		ImageView close = (ImageView) showd.findViewById(R.id.close);
		TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
		texrtdesc.setText("Please Scan the Work Area");
		nofo.setText("  OK  ");
		yesfo.setText("Cancel");
		yesfo.setVisibility(View.GONE);

		TextView textviewheader = (TextView) showd.findViewById(R.id.textView1rr);
		textviewheader.setText("please select");
		nofo.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {

				showd.dismiss();
				Utility.scanqr(Scanworklocationstart.this,1);
			}
		});
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showd.dismiss();
				Scanworklocationstart.this.finish();
			}
		});
		yesfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showd.dismiss();
				Scanworklocationstart.this.finish();
			}
		});

		showd.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1)
			{
				String contents = intent.getStringExtra("SCAN_RESULT");
				String hom = contents;
				String result = hom.substring(hom.indexOf("_loc=") + 5, hom.length());

				Shared_Preference.setWORK_LOCATION(this,result);

				String name_client =  Shared_Preference.getCLIENT_NAME(this);
				Utils.alert_with_intent_withoutintent(Scanworklocationstart.this,"Begin work in  "+result, "",name_client);
			}
		}
		else
		{
			Scanworklocationstart.this.finish();
		}
	}


	public void diloge_for_workArea()
	{
		final Dialog choose_for_scan = new Dialog(Scanworklocationstart.this);
		choose_for_scan.requestWindowFeature(Window.FEATURE_NO_TITLE);
		choose_for_scan.setContentView(R.layout.area_diloge_new);

		choose_for_scan.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		choose_for_scan.setCancelable(false);
		ImageView closebtn = (ImageView) choose_for_scan.findViewById(R.id.close);
		final AutoCompleteTextView company_name = (AutoCompleteTextView)choose_for_scan.findViewById(R.id.company);
		Button btn_GO = (Button) choose_for_scan.findViewById(R.id.go_button);
		Button scann_swo = (Button) choose_for_scan.findViewById(R.id.Scann);

		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Scanworklocationstart.this, android.R.layout.simple_list_item_1,arename);
		company_name.setAdapter(dataAdapter1);

		company_name.setDropDownHeight(550);

		company_name.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {

				company_name.showDropDown();
				return false;
			}
		});

		company_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				punnu=12;
				int index = arename.indexOf(company_name.getText().toString());
				arecode = area_code.get(index);


			}
		});

		btn_GO.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (company_name.getText().toString().equals("--Select--") || (company_name.getText().length() == 0))
				{
					Toast.makeText(Scanworklocationstart.this, "Please enter Area", Toast.LENGTH_SHORT).show();
				}
				else
				{

					if(punnu==12)
					{

						Shared_Preference.setWORK_LOCATION(Scanworklocationstart.this,arecode);
						String name_client =  Shared_Preference.getCLIENT_NAME(Scanworklocationstart.this);
						Utils.alert_with_intent_withoutintent(Scanworklocationstart.this, "Begin work in  " + arecode, "", name_client);

					}
					else
					{

						Toast.makeText(Scanworklocationstart.this, "Please enter Correct Area", Toast.LENGTH_SHORT).show();

					}
				}

			}
		});


		closebtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				finish();
			}
		});
		scann_swo.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				showscannewbin();
			}
		});

		choose_for_scan.show();
	}





}
