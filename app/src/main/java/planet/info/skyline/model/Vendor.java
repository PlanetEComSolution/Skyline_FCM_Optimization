package planet.info.skyline.model;
public class Vendor {

    private String VenderID;
    private String VenderName;
    private String IsCertified;

    public String getVenderID() {
        return VenderID;
    }

    public void setVenderID(String venderID) {
        VenderID = venderID;
    }

    public String getVenderName() {
        return VenderName;
    }

    public void setVenderName(String venderName) {
        VenderName = venderName;
    }

    public String getIsCertified() {
        return IsCertified;
    }

    public void setIsCertified(String isCertified) {
        IsCertified = isCertified;
    }

    public Vendor(String venderID, String venderName, String isCertified) {
        VenderID = venderID;
        VenderName = venderName;
        IsCertified = isCertified;
    }

    @Override
    public String toString() {
        return  VenderName;
    }
}