package planet.info.skyline.tech.damage_report;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.DamageDetail;
import planet.info.skyline.model.ItemType;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.tech.fullscreenview.FullscreenWebView;
import planet.info.skyline.tech.fullscreenview.FullscreenWebViewNew;
import planet.info.skyline.util.Utility;


public class Adapter_DamageReport extends RecyclerView.Adapter<Adapter_DamageReport.UserViewHolder> {

    private static int uploadedImagePos;
    private Context mContext;
    private ArrayList<DamageDetail> damageDetailList;
    private ArrayList<ItemType> itemTypeList;
    private ArrayAdapter<ItemType> dataAdapter;


    public Adapter_DamageReport(Context context, ArrayList<DamageDetail> damageDetailList) {
        this.mContext = context;
        this.damageDetailList = damageDetailList;
        this.itemTypeList = ItemType.fillItemTypes();
        dataAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, itemTypeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_row_1, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {

        holder.et_item_Desc.setText(damageDetailList.get(position).getItemDesc());
        holder.et_damage_desc.setText(damageDetailList.get(position).getDamageDesc());
        holder.spinner.setAdapter(dataAdapter);
        holder.spinner.setSelection(damageDetailList.get(position).getSpinnerSelectPos());
        holder.itemcount.setText(String.valueOf(position + 1));

        if (damageDetailList.get(position).isRemoveIconVisible()) {
            holder.imgvw_remove.setVisibility(View.VISIBLE);
        } else {
            holder.imgvw_remove.setVisibility(View.GONE);
        }

        //  int p= holder.getAdapterPosition();
        if (!damageDetailList.get(position).getUploadedPhotoUrl().equals("")) {
            holder.imgvwUploadedPic.setVisibility(View.VISIBLE);
            final String Image_Link = SOAP_API_Client.URL_EP2 + "/upload/" + damageDetailList.get(position).getUploadedPhotoUrl();
            Glide
                    .with(mContext)
                    .load(Image_Link)     // Url of the picture
                    .into(holder.imgvwUploadedPic);

        } else {
            holder.imgvwUploadedPic.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return damageDetailList.size();
    }

    public void addItemToList() {

        damageDetailList.add(DamageDetail.addDamageDetail());
        notifyDataSetChanged();
       if (mContext instanceof DamageReportNew) {
            ((DamageReportNew) mContext).ScrollRecyclerviewToBottom(damageDetailList.size() - 1);
        }
    }

    public ArrayList<DamageDetail> returnData() {
        if (validation() == 1) {
            return damageDetailList;
        } else return null;
    }

    public void setUploadedImageURL(String uploadedPhotoURL) {
        damageDetailList.get(uploadedImagePos).setUploadedPhotoUrl(uploadedPhotoURL);
        notifyDataSetChanged();
    }

    public int validation() {
        int val = 0;

        for (int i = 0; i < damageDetailList.size(); i++) {

            int count = i + 1;
            if (damageDetailList.get(i).getItemDesc().equals("")) {
                Toast.makeText(mContext,
                        "Please enter item description of item " + count, Toast.LENGTH_LONG).show();
                val = 0;
                break;
            } else if (damageDetailList.get(i).getDamageDesc().equals("")) {
                Toast.makeText(mContext,
                        "Please enter damage description of item " + count, Toast.LENGTH_LONG)
                        .show();
                val = 0;
                break;
            } else if (damageDetailList.get(i).getSpinnerSelectPos() == 0) {
                Toast.makeText(mContext,
                        "Please choose Item Type of item " + count, Toast.LENGTH_LONG)
                        .show();
                val = 0;
                break;
            } else {
                val = 1;

            }

        }
        return val;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public EditText et_item_Desc, et_damage_desc;
        public Spinner spinner;
        ImageView imgvw_remove, uploadPic, imgvwUploadedPic;
        Button itemcount;

        public UserViewHolder(View itemView) {
            super(itemView);
            et_item_Desc = itemView.findViewById(R.id.itemdetails);
            et_damage_desc = itemView.findViewById(R.id.itemdetails1);
            imgvw_remove = itemView.findViewById(R.id.remove_item);
            uploadPic = itemView.findViewById(R.id.uploadpict);
            spinner = itemView.findViewById(R.id.spnr_Item_Type);
            itemcount = itemView.findViewById(R.id.itencount);
            imgvwUploadedPic = itemView.findViewById(R.id.imgvwUploadedPic);
           // imgvwUploadedPic.setVisibility(View.GONE);


            imgvwUploadedPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, FullscreenWebViewNew.class);
                    i.putExtra("url", SOAP_API_Client.URL_EP2 + "/upload/" + damageDetailList.get(getAdapterPosition()).getUploadedPhotoUrl());
                    i.putExtra("FileName", damageDetailList.get(getAdapterPosition()).getUploadedPhotoUrl());
                    mContext.startActivity(i);
                }
            });

            et_item_Desc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    damageDetailList.get(getAdapterPosition()).setItemDesc(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            et_damage_desc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    damageDetailList.get(getAdapterPosition()).setDamageDesc(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int SpinnerPosition, long id) {
                    damageDetailList.get(getAdapterPosition()).setSpinnerSelectPos(SpinnerPosition);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            imgvw_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    damageDetailList.remove(getAdapterPosition());
                    // notifyItemRemoved(getAdapterPosition());
                    // notifyItemRangeChanged(getAdapterPosition(), damageDetailList.size());
                    notifyDataSetChanged();
                }
            });

            uploadPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new ConnectionDetector(mContext).isConnectingToInternet()) {
                        uploadedImagePos = getAdapterPosition();
                        if (mContext instanceof DamageReportNew) {
                            ((DamageReportNew) mContext).opendilogforattachfileandimage();
                        }

                    } else {
                        Toast.makeText(mContext, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }

}