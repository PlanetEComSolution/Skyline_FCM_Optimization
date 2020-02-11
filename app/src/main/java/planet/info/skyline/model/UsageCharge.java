package planet.info.skyline.model;

public class UsageCharge {


 /*   private String UsageChargeID;
    private String Job;
    private String Company;
    private String User;
    private String Vendor;
    private String Description;
    private String Quantity;
    private String Cost;
    private String Total;*/


    private  String Usage_id;
    private  String Quantity;
    private  String username;
    private  String Username1;
    private  String cost;
    private  String Vendor;
    private   String Vendorid;
    private    String Descriptions;
    private  String Total;
    private String job;
    private String jobid;
    private String Company;
    private  String Companyid;


    public UsageCharge(String usage_id, String quantity, String username, String username1, String cost, String vendor, String vendorid, String descriptions, String total, String job, String jobid, String company, String companyid) {
        this. Usage_id = usage_id;
        this. Quantity = quantity;
        this.username = username;
        this. Username1 = username1;
        this.cost = cost;
        this. Vendor = vendor;
        this.Vendorid = vendorid;
        this.Descriptions = descriptions;
        this. Total = total;
        this.job = job;
        this.jobid = jobid;
        this. Company = company;
        this. Companyid = companyid;
    }

    public String getUsage_id() {
        return Usage_id;
    }

    public void setUsage_id(String usage_id) {
        Usage_id = usage_id;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername1() {
        return Username1;
    }

    public void setUsername1(String username1) {
        Username1 = username1;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public String getVendorid() {
        return Vendorid;
    }

    public void setVendorid(String vendorid) {
        Vendorid = vendorid;
    }

    public String getDescriptions() {
        return Descriptions;
    }

    public void setDescriptions(String descriptions) {
        Descriptions = descriptions;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getCompanyid() {
        return Companyid;
    }

    public void setCompanyid(String companyid) {
        Companyid = companyid;
    }
}