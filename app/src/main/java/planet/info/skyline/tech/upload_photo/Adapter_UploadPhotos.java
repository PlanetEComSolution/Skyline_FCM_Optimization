package planet.info.skyline.tech.upload_photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.model.ImageItem;


public class Adapter_UploadPhotos extends RecyclerView.Adapter<Adapter_UploadPhotos.UserViewHolder> {

    private Context mContext;
    private ArrayList<ImageItem> ImageItemList;

    public Adapter_UploadPhotos(Context context, ArrayList<ImageItem> ImageItemList) {
        this.mContext = context;
        this.ImageItemList = ImageItemList;


    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_comment_box_new, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {

        holder.et_item_Desc.setText(ImageItemList.get(position).getDescription());
        holder.imageName.setText(ImageItemList.get(position).getImageName());
        holder.itemcount.setText(String.valueOf(position + 1));


        if (!ImageItemList.get(position).getImageURI().equals("")) {
            holder.thumbnail.setVisibility(View.VISIBLE);
            // holder.thumbnail.setImageURI(Uri.parse(ImageItemList.get(position).getImageURI()));

            Glide
                    .with(mContext)
                    .load(new File(ImageItemList.get(position).getImageURI()))     // Uri of the picture
                    .into(holder.thumbnail);

        } else {
            // holder.imgvwUploadedPic.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return ImageItemList.size();
    }

    public void addItemToList(String ImageURI) {
        String ImageName = ImageURI.substring(ImageURI.lastIndexOf("/") + 1);
        ImageItemList.add(new ImageItem(ImageName, "", ImageURI, ""));
        notifyDataSetChanged();
        if (mContext instanceof UploadPhotosActivity) {
            ((UploadPhotosActivity) mContext).ScrollRecyclerviewToBottom(ImageItemList.size() - 1);
        }
        if (mContext instanceof UploadPhotosActivity) {
            ((UploadPhotosActivity) mContext).onItemAdded();
        }
    }

    public void updateUploadedImageIds(ArrayList<String> listUploadedImageIds) {
        for (int i=0;i<ImageItemList.size();i++) {
            ImageItemList.get(i).setUploadedImageId(listUploadedImageIds.get(i));
        }
    }


    void removeItem(int AdapterPosition) {
        ImageItemList.remove(AdapterPosition);
        notifyDataSetChanged();
        if (mContext instanceof UploadPhotosActivity) {
            ((UploadPhotosActivity) mContext).onItemRemoved();
        }

    }


    public ArrayList<ImageItem> returnData() {
        if (validation() == 1) {
            return ImageItemList;
        } else return null;
    }

   /* public void setUploadedImageURL(String uploadedPhotoURL) {
        ImageItemList.get(uploadedImagePos).setImageURI(uploadedPhotoURL);
        notifyDataSetChanged();
    }*/

    public int validation() {
        int val = 0;

        for (int i = 0; i < ImageItemList.size(); i++) {

            int count = i + 1;
            if (ImageItemList.get(i).getDescription().equals("")) {
                Toast.makeText(mContext,
                        "Please enter description of photo " + count, Toast.LENGTH_LONG).show();
                val = 0;
                break;
            } else {
                val = 1;

            }

        }
        return val;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public EditText et_item_Desc;

        ImageView imgvw_remove, thumbnail;
        Button itemcount;
        TextView imageName;


        public UserViewHolder(View itemView) {
            super(itemView);
            et_item_Desc = itemView.findViewById(R.id.et_desc);
            imgvw_remove = itemView.findViewById(R.id.img_remove_item);
            itemcount = itemView.findViewById(R.id.serial_no);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            imageName = itemView.findViewById(R.id.imageName);


            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent i = new Intent(mContext, FullscreenWebViewNew.class);
                    //i.putExtra("url", SOAP_API_Client.URL_EP2 + "/upload/" + ImageItemList.get(getAdapterPosition()).getUploadedPhotoUrl());
                    // i.putExtra("FileName", ImageItemList.get(getAdapterPosition()).getUploadedPhotoUrl());
                    // mContext.startActivity(i);
                }
            });

            et_item_Desc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ImageItemList.get(getAdapterPosition()).setDescription(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });


            imgvw_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ImageItemList.remove(getAdapterPosition());
                    // notifyDataSetChanged();
                    if (mContext instanceof UploadPhotosActivity) {
                        //  ((UploadPhotosActivity) mContext).showConfirmationDialog(getAdapterPosition());
                        ((UploadPhotosActivity) mContext).ShowConfirmation(getAdapterPosition());


                    }
                }
            });

           /* uploadPic.setOnClickListener(new View.OnClickListener() {
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
            });*/


        }
    }


}