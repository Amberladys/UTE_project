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



public class Bike_page extends ListActivity  {


    private ProgressDialog pDialog;
    final Context context = this;
    JSONArray data = null;
    JSONArray properties = null;
    ArrayList<HashMap<String, String>> dataList;

    private String url = "https://api.bihapi.pl/wfs/warszawa/veturilo?circle=";
    private String localization;

    private static final int BIKE_FLAG = 1;

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


        Intent i = getIntent();
        // Receiving the Data
        final String zasieg = i.getStringExtra("zasieg") + "000";
        final ArrayList<String> coordinates = i.getStringArrayListExtra("localization");

        for(int index = 0;index < coordinates.size(); index++)
            url = url.concat(coordinates.get(index));
        url = url.concat(zasieg);
        localization = coordinates.get(0) + coordinates.get(2);


        new JsonParser().execute();

        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String lat = ((TextView) view.findViewById(R.id.latitude))
                        .getText().toString();
                String lon = ((TextView) view.findViewById(R.id.longitude))
                        .getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        MapsActivity.class);
                in.putExtra(TAG_GEOMETRY_COORDINATES_LAT, lat.substring(5));
                in.putExtra(TAG_GEOMETRY_COORDINATES_LON, lon.substring(5));
                in.putExtra("localization", localization);
                in.addFlags(BIKE_FLAG);
                startActivity(in);

            }
        });

    }



    public class JsonParser extends AsyncTask<Void, Void, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Bike_page.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }


        protected Integer doInBackground(Void... arg0) {


            // Creating service handler class instance
            HttpRetriver sh = new HttpRetriver();
            dataList = new ArrayList<HashMap<String, String>>();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, HttpRetriver.GET);
            url = "https://api.bihapi.pl/wfs/warszawa/veturilo?circle=";

            Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                if(jsonStr.contains("\"data\":[")) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        data = jsonObj.getJSONArray(TAG_DATA);

                        for (int i = 0; i < data.length(); i++) {
                            // geometry node is JSON Object
                            JSONObject tab = data.getJSONObject(i);
                            JSONObject geometry = tab.getJSONObject(TAG_GEOMETRY);
                            JSONObject coordinates = geometry.getJSONObject(TAG_GEOMETRY_COORDINATES);
                            String latitude = coordinates.getString(TAG_GEOMETRY_COORDINATES_LAT);
                            String longitude = coordinates.getString(TAG_GEOMETRY_COORDINATES_LON);

                            properties = tab.getJSONArray(TAG_PROPERTIES);
                            //  JSONObject properties = c.getJSONObject(TAG_PROPERTIES);
                            JSONObject nam = properties.getJSONObject(1);
                            JSONObject bie = properties.getJSONObject(3);
                            JSONObject fre = properties.getJSONObject(4);
                            String name = nam.getString(TAG_PROPERTIES_VALUE);
                            String bikes = bie.getString(TAG_PROPERTIES_VALUE);
                            String free = fre.getString(TAG_PROPERTIES_VALUE);


                            // tmp hashmap for single contact
                            HashMap<String, String> data_tmp = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            data_tmp.put(TAG_GEOMETRY_COORDINATES_LAT,"lat: " + latitude);
                            data_tmp.put(TAG_GEOMETRY_COORDINATES_LON,"lon: " + longitude);
                            data_tmp.put(TAG_PROPERTIES_VALUE, name);
                            data_tmp.put("bikes","wolne rowery: " + bikes);
                            data_tmp.put("free","wolne stojaki: " + free);

                            // adding contact to contact list
                            dataList.add(data_tmp);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        return 1;
                    }

                }
                else {
                    try {

                        url = "https://api.bihapi.pl/wfs/warszawa/veturilo?circle=";
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONObject data = jsonObj.getJSONObject(TAG_DATA);


                        // geometry node is JSON Object
                        JSONObject geometry = data.getJSONObject(TAG_GEOMETRY);
                        JSONObject coordinates = geometry.getJSONObject(TAG_GEOMETRY_COORDINATES);
                        String latitude = coordinates.getString(TAG_GEOMETRY_COORDINATES_LAT);
                        String longitude = coordinates.getString(TAG_GEOMETRY_COORDINATES_LON);

                        properties = data.getJSONArray(TAG_PROPERTIES);
                        //  JSONObject properties = c.getJSONObject(TAG_PROPERTIES);
                        JSONObject nam = properties.getJSONObject(1);
                        JSONObject bie = properties.getJSONObject(3);
                        JSONObject fre = properties.getJSONObject(4);

                        String name = nam.getString(TAG_PROPERTIES_VALUE);
                        String bikes = bie.getString(TAG_PROPERTIES_VALUE);
                        String free = fre.getString(TAG_PROPERTIES_VALUE);




                        // tmp hashmap for single contact
                        HashMap<String, String> data_tmp = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        data_tmp.put(TAG_GEOMETRY_COORDINATES_LAT,"lat: " + latitude);
                        data_tmp.put(TAG_GEOMETRY_COORDINATES_LON,"lon" + longitude);
                        data_tmp.put(TAG_PROPERTIES_VALUE, name);
                        data_tmp.put("bikes","wolne rowery: " + bikes);
                        data_tmp.put("free","wole stojaki: " + free);


                        // adding contact to contact list
                        dataList.add(data_tmp);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        return 1;
                    }

                }

            }

            else {
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
                    Bike_page.this, dataList,
                    R.layout.bike_list_item, new String[] { TAG_PROPERTIES_VALUE,  "bikes", "free", TAG_GEOMETRY_COORDINATES_LAT,
                    TAG_GEOMETRY_COORDINATES_LON }, new int[] { R.id.name, R.id.bikes, R.id.free_space,
                    R.id.latitude, R.id.longitude });

            setListAdapter(adapter);
        }


    }

}
