package planet.info.skyline.RequestControler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Iterator;

import planet.info.skyline.model.MultiApiResponse;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.progress.ProgressHUD;
import planet.info.skyline.shared_preference.Shared_Preference;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.util.Utility.LOADING_TEXT;

// The types specified here are the input data type, the progress type, and the result type
public class MyAsyncTask_MultiApiCall extends AsyncTask<Void, Void, JSONArray> {

    Context context;
    private ResponseInterface_MultiApiCall responseInterface;

    private JSONArray INPUT_JsonArray;
 //   private ProgressDialog pDialog;
 private Boolean showProgress;
    ProgressHUD mProgressHUD;
    public MyAsyncTask_MultiApiCall(Context context, Boolean showProgress, ResponseInterface_MultiApiCall responseInterface, JSONArray INPUT) {
        this.context = context;
        this.responseInterface = responseInterface;
        this.INPUT_JsonArray = INPUT;
        this.showProgress = showProgress;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (showProgress) {
            mProgressHUD = ProgressHUD.show(context, LOADING_TEXT, false);
        }
    }
    protected JSONArray doInBackground(Void... strings) {
        return Execute();
    }
    protected void onPostExecute(JSONArray response) {
        try {
            if (mProgressHUD.isShowing())
                mProgressHUD.dismiss();
        } catch (Exception e) {
        }
        responseInterface.handleMultiApiResponse(response);
    }

    public JSONArray Execute() {
        ArrayList<MultiApiResponse> responseArrayList=new ArrayList<>();
        JSONArray responseJsonArray = new JSONArray();
        for (int i = 0; i < INPUT_JsonArray.length(); i++) {
            try {
                JSONObject jsonObject = INPUT_JsonArray.getJSONObject(i);
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    final String NAMESPACE = KEY_NAMESPACE;
                    final String URL = SOAP_API_Client.BASE_URL;
                    final String METHOD_NAME = key;
                    final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;

                    // Create SOAP request
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                    JSONObject inputs = new JSONObject(jsonObject.getString(key));
                    Iterator iterator1 = inputs.keys();

                    while (iterator1.hasNext()) {
                        String key1 = (String) iterator1.next();
                        String val1 = inputs.getString(key1);
                        request.addProperty(key1, val1);
                    }

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    Log.e("Multi Api---", request.toString() );
                    HttpTransportSE httpTransport = new HttpTransportSE(URL);

                    httpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
                    String  receivedString = SoapPrimitiveresult.toString();
                    responseArrayList.add(new MultiApiResponse(METHOD_NAME,receivedString));

                    JSONObject jsonObject_Response = new JSONObject();
                    jsonObject_Response.put("Method_Name", METHOD_NAME);
                    jsonObject_Response.put("Response", receivedString);
                    responseJsonArray.put(jsonObject_Response);


                }

            } catch (Exception e) {
                e.getMessage();
            }
        }


        try {
         //   String string_object = new Gson().toJson(responseArrayList);
          //  responseJsonArray.put(string_object);



        }catch (Exception e){

        }
      //  Gson gson = new GsonBuilder().create();
       // JsonArray responseJsonArray = gson.toJsonTree(responseArrayList).getAsJsonArray();
        return responseJsonArray;
    }


}
