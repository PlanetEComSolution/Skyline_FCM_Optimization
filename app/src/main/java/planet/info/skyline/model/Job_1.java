package planet.info.skyline.model;
public class Job_1 {

    private String JobId;
    private String JobDesc;
    private String ShowName;
    private String JobName;
    private String JobType;
    private String Status;
    private String JobNameDesc;

    public Job_1(String jobId, String jobDesc, String showName, String jobName, String jobType, String status, String JobNameDesc) {
       this. JobId = jobId;
        this.  JobDesc = jobDesc;
        this. ShowName = showName;
        this.  JobName = jobName;
        this. JobType = jobType;
        this.  Status = status;
        this.  JobNameDesc=JobNameDesc;

    }

    public String getJobNameDesc() {
        return JobNameDesc;
    }

    public void setJobNameDesc(String jobNameDesc) {
        JobNameDesc = jobNameDesc;
    }

    public String getJobId() {
        return JobId;
    }

    public void setJobId(String jobId) {
        JobId = jobId;
    }

    public String getJobDesc() {
        return JobDesc;
    }

    public void setJobDesc(String jobDesc) {
        JobDesc = jobDesc;
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

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String jobType) {
        JobType = jobType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {
        return  JobNameDesc ;
    }
}