package planet.info.skyline.model;


public class Company {

    private String id;
    private String Ename;
    private String Imagepath;

    public Company(String id, String ename, String imagepath) {
        this.id = id;
        this.Ename = ename;
        this.Imagepath = imagepath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEname() {
        return Ename;
    }

    public void setEname(String ename) {
        Ename = ename;
    }

    public String getImagepath() {
        return Imagepath;
    }

    public void setImagepath(String imagepath) {
        Imagepath = imagepath;
    }

    @Override
    public String toString() {
        return Ename;
    }
}
