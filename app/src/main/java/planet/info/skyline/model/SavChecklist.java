
package planet.info.skyline.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavChecklist {

    @SerializedName("totalchklst")
    @Expose
    private Integer totalchklst;
    @SerializedName("selectedchklst")
    @Expose
    private Integer selectedchklst;

    /**
     * No args constructor for use in serialization
     * 
     */
    public SavChecklist() {
    }

    /**
     * 
     * @param totalchklst
     * @param selectedchklst
     */
    public SavChecklist(Integer totalchklst, Integer selectedchklst) {
        super();
        this.totalchklst = totalchklst;
        this.selectedchklst = selectedchklst;
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

}
