package planet.info.skyline.model;

public class ClientUserAll {
    String userID ;
    String txt_Mail;
    String CompID ;
    String CompName ;
    String UserName ;
    String UserCategory ;
    String CaType ;
    String MasterName;
    String txt_Mobile;


    public ClientUserAll(String userID, String txt_Mail, String compID,
                         String compName, String userName,
                         String userCategory, String caType,
                         String masterName,String txt_Mobile) {
        this.userID = userID;
        this.txt_Mail = txt_Mail;
        this.CompID = compID;
        this.CompName = compName;
        this.UserName = userName;
        this.UserCategory = userCategory;
        this.CaType = caType;
        this. MasterName = masterName;
        this.txt_Mobile=txt_Mobile;
    }
    public String getUserID() {
        return userID;
    }

    public String getTxt_Mail() {
        return txt_Mail;
    }

    public String getCompID() {
        return CompID;
    }

    public String getCompName() {
        return CompName;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserCategory() {
        return UserCategory;
    }

    public String getCaType() {
        return CaType;
    }

    public String getMasterName() {
        return MasterName;
    }
    public String gettxt_Mobile() {
        return txt_Mobile;
    }




}
