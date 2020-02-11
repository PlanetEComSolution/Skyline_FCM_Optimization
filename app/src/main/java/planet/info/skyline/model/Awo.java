
package planet.info.skyline.model;


import java.io.Serializable;

public class Awo implements  Serializable {

    private String iDPK;
    private String jOBID;
    private String swoName;
    private String techId;
    private String sWOStatusNew;

    public Awo(String iDPK, String jOBID, String swoName, String techId, String sWOStatusNew) {
        this.iDPK = iDPK;
        this.jOBID = jOBID;
        this.swoName = swoName;
        this.techId = techId;
        this.sWOStatusNew = sWOStatusNew;
    }


    public String getiDPK() {
        return iDPK;
    }

    public void setiDPK(String iDPK) {
        this.iDPK = iDPK;
    }

    public String getjOBID() {
        return jOBID;
    }

    public void setjOBID(String jOBID) {
        this.jOBID = jOBID;
    }

    public String getSwoName() {
        return swoName;
    }

    public void setSwoName(String swoName) {
        this.swoName = swoName;
    }

    public String getTechId() {
        return techId;
    }

    public void setTechId(String techId) {
        this.techId = techId;
    }

    public String getsWOStatusNew() {
        return sWOStatusNew;
    }

    public void setsWOStatusNew(String sWOStatusNew) {
        this.sWOStatusNew = sWOStatusNew;
    }

    @Override
    public String toString() {
        return  swoName;
    }
}
