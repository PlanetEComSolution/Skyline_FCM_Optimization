
package planet.info.skyline.model;
public class SWO_Status {

    private String iDPK;
    private String txtStatus;

    public SWO_Status(String iDPK, String txtStatus) {
        super();
        this.iDPK = iDPK;
        this.txtStatus = txtStatus;
    }

    public String getIDPK() {
        return iDPK;
    }

    public void setIDPK(String iDPK) {
        this.iDPK = iDPK;
    }

    public String getTxtStatus() {
        return txtStatus;
    }

    public void setTxtStatus(String txtStatus) {
        this.txtStatus = txtStatus;
    }



}
