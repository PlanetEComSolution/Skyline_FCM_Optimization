package planet.info.skyline.model;


public class OverlapTimesheet {

    private String uid;
    private String jobId;
    private String start_time;
    private String end_time;
    private String description;
    private String code;
    private String region;
    private String status;
    private String dayInfo;
    private String jobType;
    private String jobDesc;
    private String Swo_Status;
    private String jobIdBillable;
    private String pausedTimesheetId;


    public OverlapTimesheet(String uid, String jobId, String start_time, String end_time, String description, String code, String region, String status, String dayInfo, String jobType, String jobDesc, String Swo_Status, String jobIdBillable,String pausedTimesheetId) {
        this.uid = uid;
        this.jobId = jobId;
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.code = code;
        this.region = region;
        this.status = status;
        this.dayInfo = dayInfo;
        this.jobType = jobType;
        this.jobDesc = jobDesc;
        this.Swo_Status = Swo_Status;
        this.jobIdBillable = jobIdBillable;
        this.pausedTimesheetId = pausedTimesheetId;


    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDayInfo() {
        return dayInfo;
    }

    public void setDayInfo(String dayInfo) {
        this.dayInfo = dayInfo;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getSwo_Status() {
        return Swo_Status;
    }

    public void setSwo_Status(String swo_Status) {
        Swo_Status = swo_Status;
    }

    public String getJobIdBillable() {
        return jobIdBillable;
    }
    public void setJobIdBillable(String jobIdBillable) {
        this.jobIdBillable = jobIdBillable;
    }

    public String getPausedTimesheetId() {
        return pausedTimesheetId;
    }

    public void setPausedTimesheetId(String pausedTimesheetId) {
        this.pausedTimesheetId = pausedTimesheetId;
    }
}
