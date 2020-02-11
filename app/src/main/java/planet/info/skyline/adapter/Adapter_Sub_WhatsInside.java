package planet.info.skyline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import planet.info.skyline.R;

public class Adapter_Sub_WhatsInside extends BaseAdapter {
    List<HashMap<String, String>> beanArrayList;
    Context context;
    int count = 1;

    public Adapter_Sub_WhatsInside(Context context, List<HashMap<String, String>> beanArrayList) {
        this.context = context;
        this.beanArrayList = beanArrayList;

    }

    @Override
    public int getCount() {
        return beanArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertview, ViewGroup viewGroup) {

        final Holder holder;
        final String index1 = String.valueOf(i+1);
        final String count = beanArrayList.get(i).get("qq");
         String desc = beanArrayList.get(i).get("descr");
         String crate_number = beanArrayList.get(i).get("crate_number");
         String cur_loacation = beanArrayList.get(i).get("cur_loacation");

         desc=desc.replaceAll(Pattern.quote("&nbsp;"), " ");
        crate_number=crate_number.replaceAll(Pattern.quote("&nbsp;"), " ");
        cur_loacation=cur_loacation.replaceAll(Pattern.quote("&nbsp;"), " ");


        if (convertview == null) {
            holder = new Holder();

            convertview = LayoutInflater.from(context).inflate(R.layout.row_sub_data, null);


            holder.txtvw_count = (TextView) convertview.findViewById(R.id.txtvw_count);
            holder.txtvw_desc = (TextView) convertview.findViewById(R.id.txtvw_desc);
            holder.txtvw_bin = (TextView) convertview.findViewById(R.id.txtvw_bin);


            convertview.setTag(holder);
        } else {
            holder = (Holder) convertview.getTag();
        }




        //nks

        if (!count.equals(null)) {
            holder.txtvw_count.setText(count);
        }
        if (!desc.equals(null)) {

            if(count.equalsIgnoreCase("1"))
            {
                if(crate_number==null || crate_number.equalsIgnoreCase("null")){
                    holder.txtvw_desc.setText(desc.trim()+" is unpacked.");
                }
                else{
                    holder.txtvw_desc.setText(desc.trim()+" is in "+crate_number+".".trim());

                }

            }
            else
            {
                String descr=desc.replaceAll(Pattern.quote("&nbsp;"), " ");


                if(crate_number==null || crate_number.equalsIgnoreCase("null")){
                    holder.txtvw_desc.setText(descr.trim()+" are unpacked.");
                }
                else{
                    holder.txtvw_desc.setText(descr.trim()+" are in "+crate_number+".".trim());

                }



            }


        }

        if (!cur_loacation.equals(null) && (!cur_loacation.equals("")) && (!cur_loacation.equals("null"))) {
            holder.txtvw_bin.setText(cur_loacation);
        }
        else
        {
            holder.txtvw_bin.setText("N/A");
        }



        //nks

        return convertview;
    }


    class Holder {
        TextView txtvw_count, txtvw_desc, txtvw_bin;

        Button index_no;


    }


}