package ms.ute_project.util;

/**
 * Created by Marcus on 2015-02-10.
 */

import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;




import ms.ute_project.services.HttpRetriver;



public class Localization extends AsyncTask<Void, Void, String> {

    public AsyncResponse delegate=null;

    protected void onPreExecute() {
        super.onPreExecute();
    }


    protected String doInBackground(Void... arg0) {

        String url = "https://api.bihapi.pl/rest/terminal_location/location?query=%7B%22acceptableAccuracy%22%3A%22100%22%2C%22address%22%3A%22tel%3A48514168461%22%2C%22requestedAccuracy%22%3A%22100%22%2C%22tolerance%22%3A%22NoDelay%22%7D";
        String coord = null;

        String TAG_DATA = "result";
        String TAG_COORDINATES_LAT = "latitude";
        String TAG_COORDINATES_LON = "longitude";


        HttpRetriver sh = new HttpRetriver();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url, HttpRetriver.GET);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONObject data = jsonObj.getJSONObject(TAG_DATA);
                // geometry node is JSON Object

                String latitude = data.getString(TAG_COORDINATES_LAT);
                String longitude = data.getString(TAG_COORDINATES_LON);

                coord.concat(longitude);
                coord.concat(",");
                coord.concat(latitude);
                coord.concat(",");

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return coord;

    }

    protected void onPostExecute(String result) {

       // delegate.processFinish(result);

    }


}






