
package planet.info.skyline.model;



public class MySwo {

    private String JOB_ID;
    private String JOB_DESC;
    private String COMP_ID;
    private String txt_job;
    private String SWO_Status_new;
    private String swo_id;
    private String name;
    private String TECH_ID1;
    private String TECH_ID;
    private String CompanyName;

    public MySwo(String JOB_ID, String JOB_DESC, String COMP_ID, String txt_job, String SWO_Status_new, String swo_id, String name, String TECH_ID1, String TECH_ID,String CompanyName) {
        this.JOB_ID = JOB_ID;
        this.JOB_DESC = JOB_DESC;
        this.COMP_ID = COMP_ID;
        this.txt_job = txt_job;
        this.SWO_Status_new = SWO_Status_new;
        this.swo_id = swo_id;
        this.name = name;
        this.TECH_ID1 = TECH_ID1;
        this.TECH_ID = TECH_ID;
        this.CompanyName = CompanyName;

    }

    public String getJOB_ID() {
        return JOB_ID;
    }

    public void setJOB_ID(String JOB_ID) {
        this.JOB_ID = JOB_ID;
    }

    public String getJOB_DESC() {
        return JOB_DESC;
    }

    public void setJOB_DESC(String JOB_DESC) {
        this.JOB_DESC = JOB_DESC;
    }

    public String getCOMP_ID() {
        return COMP_ID;
    }

    public void setCOMP_ID(String COMP_ID) {
        this.COMP_ID = COMP_ID;
    }

    public String getTxt_job() {
        return txt_job;
    }

    public void setTxt_job(String txt_job) {
        this.txt_job = txt_job;
    }

    public String getSWO_Status_new() {
        return SWO_Status_new;
    }

    public void setSWO_Status_new(String SWO_Status_new) {
        this.SWO_Status_new = SWO_Status_new;
    }

    public String getSwo_id() {
        return swo_id;
    }

    public void setSwo_id(String swo_id) {
        this.swo_id = swo_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTECH_ID1() {
        return TECH_ID1;
    }

    public void setTECH_ID1(String TECH_ID1) {
        this.TECH_ID1 = TECH_ID1;
    }

    public String getTECH_ID() {
        return TECH_ID;
    }

    public void setTECH_ID(String TECH_ID) {
        this.TECH_ID = TECH_ID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    @Override
    public String toString() {
        return name;
    }
}
