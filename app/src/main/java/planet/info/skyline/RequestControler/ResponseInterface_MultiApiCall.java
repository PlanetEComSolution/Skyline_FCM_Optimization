package planet.info.skyline.RequestControler;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by admin on 04-Oct-17.
 */

public interface ResponseInterface_MultiApiCall {
        void handleMultiApiResponse(JSONArray responseJsonArray);// throws JSONException;
       // void handleErrorMessage(String response, String api);
}
