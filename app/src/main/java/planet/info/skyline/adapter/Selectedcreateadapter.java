


package planet.info.skyline.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.model.Selectcreate;

public class Selectedcreateadapter extends BaseAdapter {
private Activity activity;
private LayoutInflater inflater;
private List<Selectcreate> creatmodel;
//private ImageLoader imageloader= AppController.getInstance().getImageloader();
ImageView gomissi;

//String[] gg=new String[10];
String selectcreate="",missingcreate="";
public Selectedcreateadapter(Activity act, List<Selectcreate> cr){
	this.activity=act;
	this.creatmodel=cr;
	gomissi=(ImageView)activity.findViewById(R.id.gomissi);
}
static class ViewHolder {
	protected TextView creid;
	protected TextView credesc;
	protected TextView creloca;
	protected ImageView selectt;
	protected ImageView Miss;
	protected Button index_no;
//	protected TextView text4;
//	protected ImageView useronlineof;
//	protected CheckBox checkbox;
//	protected ImageView img_url;
//	protected RelativeLayout r2;

	
	//protected Button button;
}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return creatmodel.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return creatmodel.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final String index1 = String.valueOf(position + 1);
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			//int sdszzz=planet.info.skyline.R.layout.customlistforhome;
			//
			convertView = inflater.inflate(R.layout.row_missing_crates_new,null);
			final ViewHolder viewHolder = new ViewHolder();
		
			viewHolder.creid=(TextView)convertView.findViewById(R.id.createid);
			viewHolder.credesc=(TextView)convertView.findViewById(R.id.description);
			viewHolder.creloca=(TextView)convertView.findViewById(R.id.locationss);
			
			viewHolder.Miss=(ImageView)convertView.findViewById(R.id.missss);
			viewHolder.selectt=(ImageView)convertView.findViewById(R.id.selects);
		viewHolder.index_no = (Button) convertView.findViewById(R.id.serial_no);
			viewHolder.creid.setTag(creatmodel.get(position));
			viewHolder.credesc.setTag(creatmodel.get(position));
			viewHolder.creloca.setTag(creatmodel.get(position));
			convertView.setTag(viewHolder);
			
		Selectcreate cc=creatmodel.get(position);
		//viewHolder.creid.setText(cc.getCretateid());
		String sl=cc.getUnique_crate_id();
		viewHolder.creid.setText(sl);
		viewHolder.credesc.setText(cc.getDescription());
		viewHolder.creloca.setText(cc.getLocationcrate());
		viewHolder.index_no.setText(index1);
		//convertView = inflater.inflate(R.layout.activity_list_item, null);
	//	return null;
		/*viewHolder.Miss.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Selectcreate hh= (Selectcreate)viewHolder.credesc.getTag();
				String ffff=hh.getCretateid();
				missingcreate=missingcreate+ffff+",";
				//viewHolder.selectt.setImageDrawable(R.drawable.)
			}
		});
		
		gomissi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(activity,"hello" , 2000).show();
				 Intent ii=new Intent(activity,Selectmissing.class);
			   	 // ii.putExtra("dataa", "http://exhibitpower.com/crate_web_service.php?id=1321");
				 ii.putExtra("selectcreate",selectcreate);
				 ii.putExtra("missingcreate",missingcreate);
			   //	  activity.startActivity(ii);
			}
		});
viewHolder.selectt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Selectcreate hh= (Selectcreate)viewHolder.credesc.getTag();
				viewHolder.selectt.setImageResource(R.drawable.selectgreen);
				//	viewHolder.selectt.setImageDrawable(R.drawable.selectgreen);
				String ffff=hh.getCretateid();
				
				
				Intent ii=new Intent();
				selectcreate=selectcreate+ffff+",";
				//ii.putExtra(name, value)
				//Toast.makeText(activity,ffff , 2000).show();
			}
		});
		
		viewHolder.credesc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		Selectcreate hh= (Selectcreate)viewHolder.credesc.getTag();
		
		String ffff=hh.getDescription();
		
		
		
		//Toast.makeText(activity,ffff , 2000).show();
		
				//int gg=creatmodel.get()
			//	Toast.makeText(activity, "hello", 2000).show();
			}
			
			
		}); 
		
		viewHolder.creid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		Selectcreate hh= (Selectcreate)viewHolder.creid.getTag();
		String ff=hh.getCretateid();
		
		//Toast.makeText(activity,ff , 2000).show();
		//
				//int gg=creatmodel.get()
			//	Toast.makeText(activity, "hello", 2000).show();
			}
			
			
		}); 
		
		viewHolder.creloca.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		Selectcreate hh= (Selectcreate)viewHolder.creloca.getTag();

		String fff=hh.getLocationcrate();
		
		//Toast.makeText(activity, fff, 2000).show();
				//int gg=creatmodel.get()
				//Toast.makeText(activity, "hello", 2000).show();
			}
			
			
		}); 
		*/
		viewHolder.Miss.setImageResource(R.drawable.missinglighta);
		viewHolder.selectt.setImageResource(R.drawable.selectgreena);
			
		return convertView;
	}

}

