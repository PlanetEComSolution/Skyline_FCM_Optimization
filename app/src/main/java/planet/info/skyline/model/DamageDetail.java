package planet.info.skyline.model;

/**
 * Created by Abhishek on 16/02/17.
 */

public class DamageDetail {
    public String  ItemDesc;
    public String  DamageDesc;
    public String  UploadedPhotoUrl;
    public Integer SpinnerSelectPos;
    public boolean RemoveIconVisible;

    public DamageDetail(String itemDesc, String damageDesc, String uploadedPhotoUrl, Integer spinnerSelectPos,boolean RemoveIconVisible) {
        this.ItemDesc = itemDesc;
        this.DamageDesc = damageDesc;
        this.UploadedPhotoUrl = uploadedPhotoUrl;
        this.SpinnerSelectPos = spinnerSelectPos;
        this. RemoveIconVisible = RemoveIconVisible;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getDamageDesc() {
        return DamageDesc;
    }

    public void setDamageDesc(String damageDesc) {
        DamageDesc = damageDesc;
    }

    public String getUploadedPhotoUrl() {
        return UploadedPhotoUrl;
    }

    public void setUploadedPhotoUrl(String uploadedPhotoUrl) {
        UploadedPhotoUrl = uploadedPhotoUrl;
    }

    public Integer getSpinnerSelectPos() {
        return SpinnerSelectPos;
    }

    public void setSpinnerSelectPos(Integer spinnerSelectPos) {
        SpinnerSelectPos = spinnerSelectPos;
    }

    public boolean isRemoveIconVisible() {
        return RemoveIconVisible;
    }

    public void setRemoveIconVisible(boolean removeIconVisible) {
        RemoveIconVisible = removeIconVisible;
    }

    public static DamageDetail addDamageDetail(){
      return  new DamageDetail("", "", "",0,true);
    }
    public static DamageDetail getInitDamageDetail(){
        return  new DamageDetail("", "", "",0,false);
    }


}