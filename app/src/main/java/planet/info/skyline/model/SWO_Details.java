
package planet.info.skyline.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class SWO_Details implements Serializable {

    private String JOB_ID;
    private String JOB_DESC;
    private String COMP_ID;
    private String swo_name;
    private String txt_job;
    private String SWO_Status_new;
    private String swo_id;
    private String name;

    public SWO_Details(String JOB_ID, String JOB_DESC, String COMP_ID, String swo_name, String txt_job, String SWO_Status_new, String swo_id, String name) {
        this.JOB_ID = JOB_ID;
        this.JOB_DESC = JOB_DESC;
        this.COMP_ID = COMP_ID;
        this.swo_name = swo_name;
        this.txt_job = txt_job;
        this.SWO_Status_new = SWO_Status_new;
        this.swo_id = swo_id;
        this.name = name;
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

    public String getSwo_name() {
        return swo_name;
    }

    public void setSwo_name(String swo_name) {
        this.swo_name = swo_name;
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

    @NonNull
    @Override
    public String toString() {
        return swo_name;
    }
}
