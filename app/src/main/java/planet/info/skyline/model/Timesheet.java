
package planet.info.skyline.model;



public class Timesheet {

    private String iDPK;
    private String eMPID;
    private String jOBID;
    private String aWOID;
    private String jobDesc;
    private String compid;
    private String wORKCODE;
    private String sTARTTIME;
    private String eNDTIME;
    private String dESC;
    private String tOTALHOUR;
    private String dATETIME;
    private String sUBMITDATE;
    private String basedon;
    private String workdays;
    private String workdays1;
    private String accessStatus;
    private String sWOID;
    private String dayInfo;
    private String isCurrentTimeSheet;
    private String isBillable;
    private String JOB_ID_BILLABLE;
    private String pausedTimesheetId;

    public String getIsBillable() {
        return isBillable;
    }

    public void setIsBillable(String isBillable) {
        this.isBillable = isBillable;
    }



    public String getiDPK() {
        return iDPK;
    }

    public void setiDPK(String iDPK) {
        this.iDPK = iDPK;
    }



    public String geteMPID() {
        return eMPID;
    }

    public void seteMPID(String eMPID) {
        this.eMPID = eMPID;
    }

    public String getjOBID() {
        return jOBID;
    }

    public void setjOBID(String jOBID) {
        this.jOBID = jOBID;
    }

    public String getaWOID() {
        return aWOID;
    }

    public void setaWOID(String aWOID) {
        this.aWOID = aWOID;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getCompid() {
        return compid;
    }

    public void setCompid(String compid) {
        this.compid = compid;
    }

    public String getwORKCODE() {
        return wORKCODE;
    }

    public void setwORKCODE(String wORKCODE) {
        this.wORKCODE = wORKCODE;
    }

    public String getsTARTTIME() {
        return sTARTTIME;
    }

    public void setsTARTTIME(String sTARTTIME) {
        this.sTARTTIME = sTARTTIME;
    }

    public String geteNDTIME() {
        return eNDTIME;
    }

    public void seteNDTIME(String eNDTIME) {
        this.eNDTIME = eNDTIME;
    }

    public String getdESC() {
        return dESC;
    }

    public void setdESC(String dESC) {
        this.dESC = dESC;
    }

    public String gettOTALHOUR() {
        return tOTALHOUR;
    }

    public void settOTALHOUR(String tOTALHOUR) {
        this.tOTALHOUR = tOTALHOUR;
    }

    public String getdATETIME() {
        return dATETIME;
    }

    public void setdATETIME(String dATETIME) {
        this.dATETIME = dATETIME;
    }

    public String getsUBMITDATE() {
        return sUBMITDATE;
    }

    public void setsUBMITDATE(String sUBMITDATE) {
        this.sUBMITDATE = sUBMITDATE;
    }

    public String getBasedon() {
        return basedon;
    }

    public void setBasedon(String basedon) {
        this.basedon = basedon;
    }

    public String getWorkdays() {
        return workdays;
    }

    public void setWorkdays(String workdays) {
        this.workdays = workdays;
    }

    public String getWorkdays1() {
        return workdays1;
    }

    public void setWorkdays1(String workdays1) {
        this.workdays1 = workdays1;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public String getsWOID() {
        return sWOID;
    }

    public void setsWOID(String sWOID) {
        this.sWOID = sWOID;
    }

    public String getDayInfo() {
        return dayInfo;
    }

    public void setDayInfo(String dayInfo) {
        this.dayInfo = dayInfo;
    }

    public String getIsCurrentTimeSheet() {
        return isCurrentTimeSheet;
    }

    public void setIsCurrentTimeSheet(String isCurrentTimeSheet) {
        this.isCurrentTimeSheet = isCurrentTimeSheet;
    }

    public String getJOB_ID_BILLABLE() {
        return JOB_ID_BILLABLE;
    }

    public void setJOB_ID_BILLABLE(String JOB_ID_BILLABLE) {
        this.JOB_ID_BILLABLE = JOB_ID_BILLABLE;
    }

    public String getPausedTimesheetId() {
        return pausedTimesheetId;
    }

    public void setPausedTimesheetId(String pausedTimesheetId) {
        this.pausedTimesheetId = pausedTimesheetId;
    }

    public Timesheet(String iDPK, String eMPID, String jOBID, String aWOID, String jobDesc, String compid, String wORKCODE, String sTARTTIME, String eNDTIME, String dESC, String tOTALHOUR, String dATETIME, String sUBMITDATE, String basedon, String workdays, String workdays1, String accessStatus, String sWOID, String dayInfo, String isCurrentTimeSheet, String isBillable, String JOB_ID_BILLABLE, String pausedTimesheetId) {
        this.iDPK = iDPK;
        this.eMPID = eMPID;
        this.jOBID = jOBID;
        this.aWOID = aWOID;
        this.jobDesc = jobDesc;
        this.compid = compid;
        this.wORKCODE = wORKCODE;
        this.sTARTTIME = sTARTTIME;
        this.eNDTIME = eNDTIME;
        this.dESC = dESC;
        this.tOTALHOUR = tOTALHOUR;
        this.dATETIME = dATETIME;
        this.sUBMITDATE = sUBMITDATE;
        this.basedon = basedon;
        this.workdays = workdays;
        this.workdays1 = workdays1;
        this.accessStatus = accessStatus;
        this.sWOID = sWOID;
        this.dayInfo = dayInfo;
        this. isCurrentTimeSheet=isCurrentTimeSheet;
        this. isBillable=isBillable;


        this.JOB_ID_BILLABLE=JOB_ID_BILLABLE;
        this.pausedTimesheetId=pausedTimesheetId;


    }
}
