package planet.info.skyline.model;

public class Role {

    private String id;
    private String EmpID;
    private String CateID;
    private String DealerID;
    private String CreatedDT;
    private String UpdatedDT;
    private String UpdatedBy;
    private String status;


    public Role(String id, String empID, String cateID, String dealerID, String createdDT, String updatedDT, String updatedBy, String status) {
        this.id = id;
        EmpID = empID;
        CateID = cateID;
        DealerID = dealerID;
        CreatedDT = createdDT;
        UpdatedDT = updatedDT;
        UpdatedBy = updatedBy;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getCateID() {
        return CateID;
    }

    public void setCateID(String cateID) {
        CateID = cateID;
    }

    public String getDealerID() {
        return DealerID;
    }

    public void setDealerID(String dealerID) {
        DealerID = dealerID;
    }

    public String getCreatedDT() {
        return CreatedDT;
    }

    public void setCreatedDT(String createdDT) {
        CreatedDT = createdDT;
    }

    public String getUpdatedDT() {
        return UpdatedDT;
    }

    public void setUpdatedDT(String updatedDT) {
        UpdatedDT = updatedDT;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}