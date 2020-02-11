
package planet.info.skyline.model;



public class OrderStatus
{

    private String showName;
    private String venueName;
    private String jobName;

    private String show_date;
    private String exhibit_name;
    private String job_no;


    /**
     * 
     * @param showName
     * @param jobName
     * @param venueName
     */
    public OrderStatus(String showName, String venueName, String jobName,
                       String show_date,String exhibit_name,String job_no) {
        super();
        this.showName = showName;
        this.venueName = venueName;
        this.jobName = jobName;
        this.show_date = show_date;
        this.exhibit_name = exhibit_name;
        this.job_no = job_no;


    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getShow_date() {
        return show_date;
    }
    public String getExhibit_name() {
        return exhibit_name;
    }
    public String getJob_no() {
        return job_no;
    }
}
