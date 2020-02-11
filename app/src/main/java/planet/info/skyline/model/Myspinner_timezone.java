package planet.info.skyline.model;

public class Myspinner_timezone {
    String spinnerText;
    String timezone_id;
    String timezone_name;

    public Myspinner_timezone(String spinnerText, String timezone_id, String timezone_name) {
        this.spinnerText = spinnerText;
        this.timezone_id = timezone_id;
        this.timezone_name = timezone_name;
    }


    public String getSpinnerText() {
        return spinnerText;
    }

    public String gettimezone_id() {
        return timezone_id;
    }

    public String gettimezone_name() {
        return timezone_name;
    }

    public String toString() {
        return spinnerText;
    }


}
