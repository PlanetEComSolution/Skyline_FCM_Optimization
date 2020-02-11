package planet.info.skyline.model;
public class PathProjectFile {

    private String Name;
    private String MasterId;

    public PathProjectFile(String name, String masterId) {
        Name = name;
        MasterId = masterId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMasterId() {
        return MasterId;
    }

    public void setMasterId(String masterId) {
        MasterId = masterId;
    }

    @Override
    public String toString() {
        return  Name;
    }
}