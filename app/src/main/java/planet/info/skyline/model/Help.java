
package planet.info.skyline.model;


public class Help {

    private Integer id;
    private String name;
    private String mail;
    private String phone;
    private String typo;

    /**
     * No args constructor for use in serialization
     * 
     */
//    public Help() {
//    }

    /**
     * 
     * @param id
     * @param mail
     * @param phone
     * @param typo
     * @param name
     */
    public Help(Integer id, String name, String mail, String phone, String typo) {
        super();
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.typo = typo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTypo() {
        return typo;
    }

    public void setTypo(String typo) {
        this.typo = typo;
    }



}
