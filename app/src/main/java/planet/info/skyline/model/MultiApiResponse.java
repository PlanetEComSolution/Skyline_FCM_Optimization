package planet.info.skyline.model;

public class MultiApiResponse {

    String Method_Name;
    String Response;

    public MultiApiResponse(String method_Name, String response) {
        Method_Name = method_Name;
        Response = response;
    }

    public String getMethod_Name() {
        return Method_Name;
    }

    public void setMethod_Name(String method_Name) {
        Method_Name = method_Name;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
