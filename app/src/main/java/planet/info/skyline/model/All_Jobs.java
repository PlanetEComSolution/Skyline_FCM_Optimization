package planet.info.skyline.model;

import android.support.annotation.NonNull;

public class All_Jobs {

    private String job_id_pk;
    private String txt_job;
    private String JOB_DESC;
    private String compID;
    private String company;

    public All_Jobs(String job_id_pk, String txt_job, String JOB_DESC, String compID, String company) {
        this.job_id_pk = job_id_pk;
        this.txt_job = txt_job;
        this.JOB_DESC = JOB_DESC;
        this.compID = compID;
        this.company = company;
    }


    public String getJob_id_pk() {
        return job_id_pk;
    }

    public void setJob_id_pk(String job_id_pk) {
        this.job_id_pk = job_id_pk;
    }

    public String getTxt_job() {
        return txt_job;
    }

    public void setTxt_job(String txt_job) {
        this.txt_job = txt_job;
    }

    public String getJOB_DESC() {
        return JOB_DESC;
    }

    public void setJOB_DESC(String JOB_DESC) {
        this.JOB_DESC = JOB_DESC;
    }

    public String getCompID() {
        return compID;
    }

    public void setCompID(String compID) {
        this.compID = compID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    @Override
    public String toString() {
        char space = ' ';
        int index = 0;
        if (JOB_DESC.length() > 30) {
            for (int j = 30; j < JOB_DESC.length(); j++) {
                if (JOB_DESC.charAt(j) == space) {
                    index = j;
                    break;
                }
            }
        }
        String total_desc = "";
        if (index != 0) {
            JOB_DESC = JOB_DESC.substring(0, index) + System.getProperty("line.separator") + (JOB_DESC.substring(index)).trim();
        }
        total_desc = txt_job + System.getProperty("line.separator") + JOB_DESC.trim();
        return total_desc;


    }

    public String getJOB_Name_Desc() {
        char space = ' ';
        int index = 0;
        if (JOB_DESC.length() > 30) {
            for (int j = 30; j < JOB_DESC.length(); j++) {
                if (JOB_DESC.charAt(j) == space) {
                    index = j;
                    break;
                }
            }
        }
        String total_desc = "";
        if (index != 0) {
            JOB_DESC = JOB_DESC.substring(0, index) + System.getProperty("line.separator") + (JOB_DESC.substring(index)).trim();
        }
        total_desc = txt_job + System.getProperty("line.separator") + JOB_DESC.trim();
        return total_desc;

    }


}