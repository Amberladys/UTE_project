package ms.ute_project;

/**
 * Created by Marcus on 2015-02-10.
 */
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


import ms.ute_project.services.HttpRetriver;


public class City_page extends ListActivity {


    private ProgressDialog pDialog;
    final Context context = this;
    JSONArray data = null;
    ArrayList<HashMap<String, String>> dataList;
    String[] coordinates = null;
    JsonParser task = new JsonParser();

    private static String url = "https://api.bihapi.pl/wfs/warszawa/metroEntrances?circle=21.02,52.21,";

    private static final int CITY_FLAG = 0;

    private static final String TAG_DATA = "data";
    private static final String TAG_GEOMETRY = "geometry";
    private static final String TAG_GEOMETRY_COORDINATES = "coordinates";
    private static final String TAG_GEOMETRY_COORDINATES_LAT = "lat";
    private static final String TAG_GEOMETRY_COORDINATES_LON = "lon";
    private static final String TAG_PROPERTIES = "properties";
    private static final String TAG_PROPERTIES_VALUE = "value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);
        dataList = new ArrayList<HashMap<String, String>>();


        Intent i = getIntent();
        // Receiving the Data
        String zasieg = i.getStringExtra("zasieg")+"000";
        url = url.concat(zasieg);



        task.execute();
      /*  if(task.getStatus() == AsyncTask.Status.FINISHED){

            for (int s = 0; s < dataList.size(); s++)
                coordinates[s] = (dataList.get(s)).get(TAG_GEOMETRY_COORDINATES_LAT) + (dataList.get(s)).get(TAG_GEOMETRY_COORDINATES_LON);

           Intent google = new Intent(getApplicationContext(),
                   MapsActivity.class);
            google.putExtra("cords",dataList);
            google.addFlags(CITY_FLAG);
            startActivity(google);
        }*/



/*
        String lat = ((TextView) view.findViewById(R.id.latitude))
                .getText().toString();
        String lon = ((TextView) view.findViewById(R.id.longitude))
                .getText().toString();


        // Starting single contact activity
        Intent in = new Intent(getApplicationContext(),
                MapsActivity.class);
        in.putExtra(TAG_GEOMETRY_COORDINATES_LAT, lat);
        in.putExtra(TAG_GEOMETRY_COORDINATES_LON, lon);
        in.addFlags(CITY_FLAG);
        startActivity(in);
*/




    }

    public class JsonParser extends AsyncTask<Void, Void, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(City_page.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }


        protected Integer doInBackground(Void... arg0) {

            // Creating service handler class instance
            HttpRetriver sh = new HttpRetriver();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, HttpRetriver.GET);
            url = "https://api.bihapi.pl/wfs/warszawa/metroEntrances?circle=21.02,52.21,";

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    data = jsonObj.getJSONArray(TAG_DATA);

                    // looping through All Contacts
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        // geometry node is JSON Object
                        JSONObject geometry = c.getJSONObject(TAG_GEOMETRY);
                        JSONObject coordinates = geometry.getJSONObject(TAG_GEOMETRY_COORDINATES);
                        String latitude = coordinates.getString(TAG_GEOMETRY_COORDINATES_LAT);
                        String longitude = coordinates.getString(TAG_GEOMETRY_COORDINATES_LON);

                        //properties = jsonObj.getJSONArray(TAG_PROPERTIES);
                        JSONObject properties = c.getJSONObject(TAG_PROPERTIES);
                        //JSONObject p = properties.getJSONObject(1);
                        String name = properties.getString(TAG_PROPERTIES_VALUE);

                        // tmp hashmap for single contact
                        HashMap<String, String> data_tmp = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        data_tmp.put(TAG_GEOMETRY_COORDINATES_LAT, latitude);
                        data_tmp.put(TAG_GEOMETRY_COORDINATES_LON, longitude);
                        data_tmp.put(TAG_PROPERTIES_VALUE,name);

                        // adding contact to contact list
                        dataList.add(data_tmp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return 1;
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return 0;
        }


        protected void onPostExecute(Integer result) {
            if(result==1)
                super.onPostExecute(result-1);
            else
                super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (result==1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Error");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Brak obiektów w pobliżu")
                        .setCancelable(false)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    City_page.this, dataList,
                    R.layout.city_list_item, new String[] { TAG_PROPERTIES_VALUE, TAG_GEOMETRY_COORDINATES_LAT,
                    TAG_GEOMETRY_COORDINATES_LON }, new int[] { R.id.name,
                    R.id.latitude, R.id.longitude });

            setListAdapter(adapter);


        }


    }

}
