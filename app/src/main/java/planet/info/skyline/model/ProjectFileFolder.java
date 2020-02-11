package planet.info.skyline.model;

public class ProjectFileFolder {
    private String FILE_idPk;
    private String FILE_jobId;
    private String FILE_clientID;
    private String FILE_googleID;
    private String FILE_originalFilename;
    private String FILE_createdDate;
    private String FILE_modifyDate;
    private String FILE_status;
    private String FILE_description;
    private String FILE_masterfolderID;
    private String FILE_parentfolderID;
    private String FILE_path;
    private String FILE_uploadedBy;
    private String FILE_txtJob;
    private String FILE_txtCName;
    private String FILE_internalStatus;
    private String FILE_lastActionDate;
    private String FILE_clientStatus;
    private String FILE_uploadbyName;
    private String FILE_iconLink;
    private String FILE_fileType;
    private String FILE_approvalRequired;
    private String FILE_pGoogleID;
    private String FILE_degree;
    private String FILE_d1;


    private String FOLDER_dealerID;
    private String FOLDER_fileIDPK;
    private String FOLDER_fileCat;
    private String FOLDER_fileStatus;
    private String FOLDER_fileSubmitDate;
    private String FOLDER_masterID;
    private String FOLDER_parentID;
    private String FOLDER_path;
    private String FOLDER_jobName;
    private String FOLDER_whoDelete;
    private String FOLDER_jobId;
    private String FOLDER_googleID;
    private String FOLDER_pGoogleID;

    private String isFOLDER;

    public ProjectFileFolder(String FILE_idPk, String FILE_jobId, String FILE_clientID, String FILE_googleID, String FILE_originalFilename, String FILE_createdDate, String FILE_modifyDate, String FILE_status, String FILE_description, String FILE_masterfolderID, String FILE_parentfolderID, String FILE_path, String FILE_uploadedBy, String FILE_txtJob, String FILE_txtCName, String FILE_internalStatus, String FILE_lastActionDate, String FILE_clientStatus, String FILE_uploadbyName, String FILE_iconLink, String FILE_fileType, String FILE_approvalRequired, String FILE_pGoogleID, String FILE_degree, String FILE_d1, String FOLDER_dealerID, String FOLDER_fileIDPK, String FOLDER_fileCat, String FOLDER_fileStatus, String FOLDER_fileSubmitDate, String FOLDER_masterID, String FOLDER_parentID, String FOLDER_path, String FOLDER_jobName, String FOLDER_whoDelete, String FOLDER_jobId, String FOLDER_googleID, String FOLDER_pGoogleID,String isFOLDER) {
        this.FILE_idPk = FILE_idPk;
        this.FILE_jobId = FILE_jobId;
        this.FILE_clientID = FILE_clientID;
        this.FILE_googleID = FILE_googleID;
        this.FILE_originalFilename = FILE_originalFilename;
        this.FILE_createdDate = FILE_createdDate;
        this.FILE_modifyDate = FILE_modifyDate;
        this.FILE_status = FILE_status;
        this.FILE_description = FILE_description;
        this.FILE_masterfolderID = FILE_masterfolderID;
        this.FILE_parentfolderID = FILE_parentfolderID;
        this.FILE_path = FILE_path;
        this.FILE_uploadedBy = FILE_uploadedBy;
        this.FILE_txtJob = FILE_txtJob;
        this.FILE_txtCName = FILE_txtCName;
        this.FILE_internalStatus = FILE_internalStatus;
        this.FILE_lastActionDate = FILE_lastActionDate;
        this.FILE_clientStatus = FILE_clientStatus;
        this.FILE_uploadbyName = FILE_uploadbyName;
        this.FILE_iconLink = FILE_iconLink;
        this.FILE_fileType = FILE_fileType;
        this.FILE_approvalRequired = FILE_approvalRequired;
        this.FILE_pGoogleID = FILE_pGoogleID;
        this.FILE_degree = FILE_degree;
        this.FILE_d1 = FILE_d1;
        this.FOLDER_dealerID = FOLDER_dealerID;
        this.FOLDER_fileIDPK = FOLDER_fileIDPK;
        this.FOLDER_fileCat = FOLDER_fileCat;
        this.FOLDER_fileStatus = FOLDER_fileStatus;
        this.FOLDER_fileSubmitDate = FOLDER_fileSubmitDate;
        this.FOLDER_masterID = FOLDER_masterID;
        this.FOLDER_parentID = FOLDER_parentID;
        this.FOLDER_path = FOLDER_path;
        this.FOLDER_jobName = FOLDER_jobName;
        this.FOLDER_whoDelete = FOLDER_whoDelete;
        this.FOLDER_jobId = FOLDER_jobId;
        this.FOLDER_googleID = FOLDER_googleID;
        this.FOLDER_pGoogleID = FOLDER_pGoogleID;

        this.isFOLDER = isFOLDER;

    }


    public String getFILE_idPk() {
        return FILE_idPk;
    }

    public void setFILE_idPk(String FILE_idPk) {
        this.FILE_idPk = FILE_idPk;
    }

    public String getFILE_jobId() {
        return FILE_jobId;
    }

    public void setFILE_jobId(String FILE_jobId) {
        this.FILE_jobId = FILE_jobId;
    }

    public String getFILE_clientID() {
        return FILE_clientID;
    }

    public void setFILE_clientID(String FILE_clientID) {
        this.FILE_clientID = FILE_clientID;
    }

    public String getFILE_googleID() {
        return FILE_googleID;
    }

    public void setFILE_googleID(String FILE_googleID) {
        this.FILE_googleID = FILE_googleID;
    }

    public String getFILE_originalFilename() {
        return FILE_originalFilename;
    }

    public void setFILE_originalFilename(String FILE_originalFilename) {
        this.FILE_originalFilename = FILE_originalFilename;
    }

    public String getFILE_createdDate() {
        return FILE_createdDate;
    }

    public void setFILE_createdDate(String FILE_createdDate) {
        this.FILE_createdDate = FILE_createdDate;
    }

    public String getFILE_modifyDate() {
        return FILE_modifyDate;
    }

    public void setFILE_modifyDate(String FILE_modifyDate) {
        this.FILE_modifyDate = FILE_modifyDate;
    }

    public String getFILE_status() {
        return FILE_status;
    }

    public void setFILE_status(String FILE_status) {
        this.FILE_status = FILE_status;
    }

    public String getFILE_description() {
        return FILE_description;
    }

    public void setFILE_description(String FILE_description) {
        this.FILE_description = FILE_description;
    }

    public String getFILE_masterfolderID() {
        return FILE_masterfolderID;
    }

    public void setFILE_masterfolderID(String FILE_masterfolderID) {
        this.FILE_masterfolderID = FILE_masterfolderID;
    }

    public String getFILE_parentfolderID() {
        return FILE_parentfolderID;
    }

    public void setFILE_parentfolderID(String FILE_parentfolderID) {
        this.FILE_parentfolderID = FILE_parentfolderID;
    }

    public String getFILE_path() {
        return FILE_path;
    }

    public void setFILE_path(String FILE_path) {
        this.FILE_path = FILE_path;
    }

    public String getFILE_uploadedBy() {
        return FILE_uploadedBy;
    }

    public void setFILE_uploadedBy(String FILE_uploadedBy) {
        this.FILE_uploadedBy = FILE_uploadedBy;
    }

    public String getFILE_txtJob() {
        return FILE_txtJob;
    }

    public void setFILE_txtJob(String FILE_txtJob) {
        this.FILE_txtJob = FILE_txtJob;
    }

    public String getFILE_txtCName() {
        return FILE_txtCName;
    }

    public void setFILE_txtCName(String FILE_txtCName) {
        this.FILE_txtCName = FILE_txtCName;
    }

    public String getFILE_internalStatus() {
        return FILE_internalStatus;
    }

    public void setFILE_internalStatus(String FILE_internalStatus) {
        this.FILE_internalStatus = FILE_internalStatus;
    }

    public String getFILE_lastActionDate() {
        return FILE_lastActionDate;
    }

    public void setFILE_lastActionDate(String FILE_lastActionDate) {
        this.FILE_lastActionDate = FILE_lastActionDate;
    }

    public String getFILE_clientStatus() {
        return FILE_clientStatus;
    }

    public void setFILE_clientStatus(String FILE_clientStatus) {
        this.FILE_clientStatus = FILE_clientStatus;
    }

    public String getFILE_uploadbyName() {
        return FILE_uploadbyName;
    }

    public void setFILE_uploadbyName(String FILE_uploadbyName) {
        this.FILE_uploadbyName = FILE_uploadbyName;
    }

    public String getFILE_iconLink() {
        return FILE_iconLink;
    }

    public void setFILE_iconLink(String FILE_iconLink) {
        this.FILE_iconLink = FILE_iconLink;
    }

    public String getFILE_fileType() {
        return FILE_fileType;
    }

    public void setFILE_fileType(String FILE_fileType) {
        this.FILE_fileType = FILE_fileType;
    }

    public String getFILE_approvalRequired() {
        return FILE_approvalRequired;
    }

    public void setFILE_approvalRequired(String FILE_approvalRequired) {
        this.FILE_approvalRequired = FILE_approvalRequired;
    }

    public String getFILE_pGoogleID() {
        return FILE_pGoogleID;
    }

    public void setFILE_pGoogleID(String FILE_pGoogleID) {
        this.FILE_pGoogleID = FILE_pGoogleID;
    }

    public String getFILE_degree() {
        return FILE_degree;
    }

    public void setFILE_degree(String FILE_degree) {
        this.FILE_degree = FILE_degree;
    }

    public String getFILE_d1() {
        return FILE_d1;
    }

    public void setFILE_d1(String FILE_d1) {
        this.FILE_d1 = FILE_d1;
    }

    public String getFOLDER_dealerID() {
        return FOLDER_dealerID;
    }

    public void setFOLDER_dealerID(String FOLDER_dealerID) {
        this.FOLDER_dealerID = FOLDER_dealerID;
    }

    public String getFOLDER_fileIDPK() {
        return FOLDER_fileIDPK;
    }

    public void setFOLDER_fileIDPK(String FOLDER_fileIDPK) {
        this.FOLDER_fileIDPK = FOLDER_fileIDPK;
    }

    public String getFOLDER_fileCat() {
        return FOLDER_fileCat;
    }

    public void setFOLDER_fileCat(String FOLDER_fileCat) {
        this.FOLDER_fileCat = FOLDER_fileCat;
    }

    public String getFOLDER_fileStatus() {
        return FOLDER_fileStatus;
    }

    public void setFOLDER_fileStatus(String FOLDER_fileStatus) {
        this.FOLDER_fileStatus = FOLDER_fileStatus;
    }

    public String getFOLDER_fileSubmitDate() {
        return FOLDER_fileSubmitDate;
    }

    public void setFOLDER_fileSubmitDate(String FOLDER_fileSubmitDate) {
        this.FOLDER_fileSubmitDate = FOLDER_fileSubmitDate;
    }

    public String getFOLDER_masterID() {
        return FOLDER_masterID;
    }

    public void setFOLDER_masterID(String FOLDER_masterID) {
        this.FOLDER_masterID = FOLDER_masterID;
    }

    public String getFOLDER_parentID() {
        return FOLDER_parentID;
    }

    public void setFOLDER_parentID(String FOLDER_parentID) {
        this.FOLDER_parentID = FOLDER_parentID;
    }

    public String getFOLDER_path() {
        return FOLDER_path;
    }

    public void setFOLDER_path(String FOLDER_path) {
        this.FOLDER_path = FOLDER_path;
    }

    public String getFOLDER_jobName() {
        return FOLDER_jobName;
    }

    public void setFOLDER_jobName(String FOLDER_jobName) {
        this.FOLDER_jobName = FOLDER_jobName;
    }

    public String getFOLDER_whoDelete() {
        return FOLDER_whoDelete;
    }

    public void setFOLDER_whoDelete(String FOLDER_whoDelete) {
        this.FOLDER_whoDelete = FOLDER_whoDelete;
    }

    public String getFOLDER_jobId() {
        return FOLDER_jobId;
    }

    public void setFOLDER_jobId(String FOLDER_jobId) {
        this.FOLDER_jobId = FOLDER_jobId;
    }

    public String getFOLDER_googleID() {
        return FOLDER_googleID;
    }

    public void setFOLDER_googleID(String FOLDER_googleID) {
        this.FOLDER_googleID = FOLDER_googleID;
    }

    public String getFOLDER_pGoogleID() {
        return FOLDER_pGoogleID;
    }

    public void setFOLDER_pGoogleID(String FOLDER_pGoogleID) {
        this.FOLDER_pGoogleID = FOLDER_pGoogleID;
    }

    public String getIsFOLDER() {
        return isFOLDER;
    }

    public void setIsFOLDER(String isFOLDER) {
        this.isFOLDER = isFOLDER;
    }
}
