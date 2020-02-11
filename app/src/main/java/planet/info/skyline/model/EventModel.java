package planet.info.skyline.model;

public class EventModel {
    private String id;
    private String fileId;
    private String creationDate;
    private String moduleId;
    private String status;
    private String doneBy;
    private String uploadedFileCount;
    private String clientId;
    private String jobId;
    private String isClient;
    private String clientUserId;
    private String compName;
    private String txtJob;
    private String statusName;
    private String event;

    public EventModel(String id, String fileId, String creationDate, String moduleId, String status, String doneBy, String uploadedFileCount, String clientId, String jobId, String isClient, String clientUserId, String compName, String txtJob, String statusName, String event) {
        this.id = id;
        this.fileId = fileId;
        this.creationDate = creationDate;
        this.moduleId = moduleId;
        this.status = status;
        this.doneBy = doneBy;
        this.uploadedFileCount = uploadedFileCount;
        this.clientId = clientId;
        this.jobId = jobId;
        this.isClient = isClient;
        this.clientUserId = clientUserId;
        this.compName = compName;
        this.txtJob = txtJob;
        this.statusName = statusName;
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
    }

    public String getUploadedFileCount() {
        return uploadedFileCount;
    }

    public void setUploadedFileCount(String uploadedFileCount) {
        this.uploadedFileCount = uploadedFileCount;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getIsClient() {
        return isClient;
    }

    public void setIsClient(String isClient) {
        this.isClient = isClient;
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getTxtJob() {
        return txtJob;
    }

    public void setTxtJob(String txtJob) {
        this.txtJob = txtJob;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
