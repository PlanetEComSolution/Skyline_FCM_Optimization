package planet.info.skyline.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MyTask {

    @SerializedName("ID_PK")
    @Expose
    private Integer iDPK;
    @SerializedName("task")
    @Expose
    private String task;
    @SerializedName("DeadlineDate")
    @Expose
    private String deadlineDate;
    @SerializedName("ComletedDate")
    @Expose
    private String comletedDate;
    @SerializedName("DeadlineDate1")
    @Expose
    private String deadlineDate1;
    @SerializedName("ComletedDate1")
    @Expose
    private String comletedDate1;
    @SerializedName("AssignTO")
    @Expose
    private Integer assignTO;
    @SerializedName("Job_Status")
    @Expose
    private Integer jobStatus;
    @SerializedName("Job_ID_PK")
    @Expose
    private Integer jobIDPK;
    @SerializedName("stID")
    @Expose
    private Integer stID;
    @SerializedName("Satate")
    @Expose
    private String satate;
    @SerializedName("Ename")
    @Expose
    private Object ename;
    @SerializedName("SortTask")
    @Expose
    private Integer sortTask;
    @SerializedName("dept_id")
    @Expose
    private Integer deptId;
    @SerializedName("totalchklst")
    @Expose
    private Integer totalchklst;
    @SerializedName("selectedchklst")
    @Expose
    private Integer selectedchklst;
    @SerializedName("DTaskID")
    @Expose
    private String dTaskID;
    @SerializedName("docCount")
    @Expose
    private Integer docCount;
    @SerializedName("TimeBudget")
    @Expose
    private String timeBudget;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("AssignUser")
    @Expose
    private String assignUser;
    @SerializedName("IsUsed")
    @Expose
    private Integer isUsed;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("AssignTO1")
    @Expose
    private String assignTO1;
    @SerializedName("UserID")
    @Expose
    private Integer userID;
    @SerializedName("CName")
    @Expose
    private String cName;
    @SerializedName("IsGreen")
    @Expose
    private String isGreen;
    @SerializedName("DP_SDP_Name")
    @Expose
    private String dPSDPName;
    @SerializedName("DeptIDS")
    @Expose
    private Integer deptIDS;
    @SerializedName("TaskA")
    @Expose
    private String taskA;
    @SerializedName("TechListC")
    @Expose
    private String techListC;
    @SerializedName("txt_job")
    @Expose
    private String txtJob;
    @SerializedName("txt_C_Name")
    @Expose
    private String txtCName;
    @SerializedName("swo_name")
    @Expose
    private String swoName;
    @SerializedName("SWOID")
    @Expose
    private Integer sWOID;
    @SerializedName("type_id")
    @Expose
    private Integer typeId;
    @SerializedName("ClientIDN")
    @Expose
    private Integer clientIDN;
    @SerializedName("txt_Jdes")
    @Expose
    private String txtJdes;
    @SerializedName("IsAWO")
    @Expose
    private Integer isAWO;

    @SerializedName("Lalor_ID_PK")
    @Expose
    private Integer Lalor_ID_PK;


    @SerializedName("Labor_name")
    @Expose
    private String Labor_name;


    /**
     * No args constructor for use in serialization
     *
     */
    public MyTask() {
    }

    public MyTask(Integer iDPK, String task, String deadlineDate, String comletedDate, String deadlineDate1, String comletedDate1, Integer assignTO, Integer jobStatus, Integer jobIDPK, Integer stID, String satate, Object ename, Integer sortTask, Integer deptId, Integer totalchklst, Integer selectedchklst, String dTaskID, Integer docCount, String timeBudget, String startDate, String assignUser, Integer isUsed, String description, String assignTO1, Integer userID, String cName, String isGreen, String dPSDPName, Integer deptIDS, String taskA, String techListC, String txtJob, String txtCName, String swoName, Integer sWOID, Integer typeId, Integer clientIDN, String txtJdes, Integer isAWO, Integer lalor_ID_PK, String labor_name) {
        this.iDPK = iDPK;
        this.task = task;
        this.deadlineDate = deadlineDate;
        this.comletedDate = comletedDate;
        this.deadlineDate1 = deadlineDate1;
        this.comletedDate1 = comletedDate1;
        this.assignTO = assignTO;
        this.jobStatus = jobStatus;
        this.jobIDPK = jobIDPK;
        this.stID = stID;
        this.satate = satate;
        this.ename = ename;
        this.sortTask = sortTask;
        this.deptId = deptId;
        this.totalchklst = totalchklst;
        this.selectedchklst = selectedchklst;
        this.dTaskID = dTaskID;
        this.docCount = docCount;
        this.timeBudget = timeBudget;
        this.startDate = startDate;
        this.assignUser = assignUser;
        this.isUsed = isUsed;
        this.description = description;
        this.assignTO1 = assignTO1;
        this.userID = userID;
        this.cName = cName;
        this.isGreen = isGreen;
        this.dPSDPName = dPSDPName;
        this.deptIDS = deptIDS;
        this.taskA = taskA;
        this.techListC = techListC;
        this.txtJob = txtJob;
        this.txtCName = txtCName;
        this.swoName = swoName;
        this.sWOID = sWOID;
        this.typeId = typeId;
        this.clientIDN = clientIDN;
        this.txtJdes = txtJdes;
        this.isAWO = isAWO;
        Lalor_ID_PK = lalor_ID_PK;
        Labor_name = labor_name;
    }

    public Integer getiDPK() {
        return iDPK;
    }

    public void setiDPK(Integer iDPK) {
        this.iDPK = iDPK;
    }

    public String getdTaskID() {
        return dTaskID;
    }

    public void setdTaskID(String dTaskID) {
        this.dTaskID = dTaskID;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getdPSDPName() {
        return dPSDPName;
    }

    public void setdPSDPName(String dPSDPName) {
        this.dPSDPName = dPSDPName;
    }

    public Integer getsWOID() {
        return sWOID;
    }

    public void setsWOID(Integer sWOID) {
        this.sWOID = sWOID;
    }

    public Integer getLalor_ID_PK() {
        return Lalor_ID_PK;
    }

    public void setLalor_ID_PK(Integer lalor_ID_PK) {
        Lalor_ID_PK = lalor_ID_PK;
    }

    public String getLabor_name() {
        return Labor_name;
    }

    public void setLabor_name(String labor_name) {
        Labor_name = labor_name;
    }

    public Integer getIDPK() {
        return iDPK;
    }

    public void setIDPK(Integer iDPK) {
        this.iDPK = iDPK;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getComletedDate() {
        return comletedDate;
    }

    public void setComletedDate(String comletedDate) {
        this.comletedDate = comletedDate;
    }

    public String getDeadlineDate1() {
        return deadlineDate1;
    }

    public void setDeadlineDate1(String deadlineDate1) {
        this.deadlineDate1 = deadlineDate1;
    }

    public String getComletedDate1() {
        return comletedDate1;
    }

    public void setComletedDate1(String comletedDate1) {
        this.comletedDate1 = comletedDate1;
    }

    public Integer getAssignTO() {
        return assignTO;
    }

    public void setAssignTO(Integer assignTO) {
        this.assignTO = assignTO;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getJobIDPK() {
        return jobIDPK;
    }

    public void setJobIDPK(Integer jobIDPK) {
        this.jobIDPK = jobIDPK;
    }

    public Integer getStID() {
        return stID;
    }

    public void setStID(Integer stID) {
        this.stID = stID;
    }

    public String getSatate() {
        return satate;
    }

    public void setSatate(String satate) {
        this.satate = satate;
    }

    public Object getEname() {
        return ename;
    }

    public void setEname(Object ename) {
        this.ename = ename;
    }

    public Integer getSortTask() {
        return sortTask;
    }

    public void setSortTask(Integer sortTask) {
        this.sortTask = sortTask;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getTotalchklst() {
        return totalchklst;
    }

    public void setTotalchklst(Integer totalchklst) {
        this.totalchklst = totalchklst;
    }

    public Integer getSelectedchklst() {
        return selectedchklst;
    }

    public void setSelectedchklst(Integer selectedchklst) {
        this.selectedchklst = selectedchklst;
    }

    public String getDTaskID() {
        return dTaskID;
    }

    public void setDTaskID(String dTaskID) {
        this.dTaskID = dTaskID;
    }

    public Integer getDocCount() {
        return docCount;
    }

    public void setDocCount(Integer docCount) {
        this.docCount = docCount;
    }

    public String getTimeBudget() {
        return timeBudget;
    }

    public void setTimeBudget(String timeBudget) {
        this.timeBudget = timeBudget;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(String assignUser) {
        this.assignUser = assignUser;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignTO1() {
        return assignTO1;
    }

    public void setAssignTO1(String assignTO1) {
        this.assignTO1 = assignTO1;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getIsGreen() {
        return isGreen;
    }

    public void setIsGreen(String isGreen) {
        this.isGreen = isGreen;
    }

    public String getDPSDPName() {
        return dPSDPName;
    }

    public void setDPSDPName(String dPSDPName) {
        this.dPSDPName = dPSDPName;
    }

    public Integer getDeptIDS() {
        return deptIDS;
    }

    public void setDeptIDS(Integer deptIDS) {
        this.deptIDS = deptIDS;
    }

    public String getTaskA() {
        return taskA;
    }

    public void setTaskA(String taskA) {
        this.taskA = taskA;
    }

    public String getTechListC() {
        return techListC;
    }

    public void setTechListC(String techListC) {
        this.techListC = techListC;
    }

    public String getTxtJob() {
        return txtJob;
    }

    public void setTxtJob(String txtJob) {
        this.txtJob = txtJob;
    }

    public String getTxtCName() {
        return txtCName;
    }

    public void setTxtCName(String txtCName) {
        this.txtCName = txtCName;
    }

    public String getSwoName() {
        return swoName;
    }

    public void setSwoName(String swoName) {
        this.swoName = swoName;
    }

    public Integer getSWOID() {
        return sWOID;
    }

    public void setSWOID(Integer sWOID) {
        this.sWOID = sWOID;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getClientIDN() {
        return clientIDN;
    }

    public void setClientIDN(Integer clientIDN) {
        this.clientIDN = clientIDN;
    }

    public String getTxtJdes() {
        return txtJdes;
    }

    public void setTxtJdes(String txtJdes) {
        this.txtJdes = txtJdes;
    }

    public Integer getIsAWO() {
        return isAWO;
    }

    public void setIsAWO(Integer isAWO) {
        this.isAWO = isAWO;
    }

}