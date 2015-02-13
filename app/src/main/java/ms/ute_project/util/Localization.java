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




    protected void onPostExecute(String result) {

       // delegate.processFinish(result);

    }


}






