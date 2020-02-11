package planet.info.skyline.model;

public class Job_2 {

    private String JOB_ID_PK;
    private String txt_Job;
    private String ShowName;
    private String JobName;
    private String JOB_TYPE;
    private String Status;



    public Job_2(String JOB_ID_PK, String txt_Job, String showName, String jobName, String JOB_TYPE, String status) {
        this.JOB_ID_PK = JOB_ID_PK;
        this.txt_Job = txt_Job;
        ShowName = showName;
        JobName = jobName;
        this.JOB_TYPE = JOB_TYPE;
        Status = status;
    }

    public String getJOB_ID_PK() {
        return JOB_ID_PK;
    }

    public void setJOB_ID_PK(String JOB_ID_PK) {
        this.JOB_ID_PK = JOB_ID_PK;
    }

    public String getTxt_Job() {
        return txt_Job;
    }

    public void setTxt_Job(String txt_Job) {
        this.txt_Job = txt_Job;
    }

    public String getShowName() {
        return ShowName;
    }

    public void setShowName(String showName) {
        ShowName = showName;
    }

    public String getJobName() {
        return JobName;
    }

    public void setJobName(String jobName) {
        JobName = jobName;
    }

    public String getJOB_TYPE() {
        return JOB_TYPE;
    }

    public void setJOB_TYPE(String JOB_TYPE) {
        this.JOB_TYPE = JOB_TYPE;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    public String getJOB_Name_Desc() {
        char space = ' ';
        int index = 0;
        if (txt_Job.length() > 30) {
            for (int j = 30; j < txt_Job.length(); j++) {
                if (txt_Job.charAt(j) == space) {
                    index = j;
                    break;
                }
            }
        }
        String total_desc = "";
        if (index != 0) {
            txt_Job = txt_Job.substring(0, index) + System.getProperty("line.separator") + (txt_Job.substring(index)).trim();
        }
        total_desc = JobName + System.getProperty("line.separator") + txt_Job.trim();
        return total_desc;

    }


}