package ms.ute_project;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import java.util.ArrayList;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ms.ute_project.services.HttpRetriver;


public class Start_menu extends Activity {


    ArrayList<String> coords = null;
    private TextView zoneText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        Button cityButton = (Button) findViewById(R.id.city_button);
        Button bikeButton = (Button) findViewById(R.id.bike_button);
        Button parkingButton = (Button) findViewById(R.id.parking_button);
        SeekBar zoneButton = (SeekBar) findViewById(R.id.zone_button);
        zoneText = (TextView) findViewById(R.id.zone_text);


        zoneButton.setProgress(0);
        int prog = zoneButton.getProgress();
        zoneText.setText("Zasięg [km]:  " + String.valueOf(prog));

        coords = new ArrayList<String>();

        new Localization().execute();

        cityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opCity = new Intent(getApplicationContext(), City_page.class);

                opCity.putExtra("zasieg", zoneText.getText().toString().substring(14));
                opCity.putExtra("localization", coords);
                startActivity(opCity);

            }
        });

        bikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opBike = new Intent(getApplicationContext(), Bike_page.class);

                opBike.putExtra("zasieg", zoneText.getText().toString().substring(14));
                opBike.putExtra("localization", coords);
                startActivity(opBike);
            }
        });

        parkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opPark = new Intent(getApplicationContext(), Park_and_ride_page.class);

                opPark.putExtra("zasieg", zoneText.getText().toString().substring(14));
                opPark.putExtra("localization", coords);
                startActivity(opPark);
            }
        });

        zoneButton.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                zoneText.setText("Zasięg [km]:  " + String.valueOf(progress));

            }
        });

    }

    public class Localization extends AsyncTask<Void, Void, Void> {

        String url = "https://api.bihapi.pl/rest/terminal_location/location?query=%7B%22acceptableAccuracy%22%3A%22300%22%2C%22address%22%3A%22tel%3A48514168461%22%2C%22requestedAccuracy%22%3A%22300%22%2C%22tolerance%22%3A%22NoDelay%22%7D";

        String TAG_DATA = "result";
        String TAG_COORDINATES_LAT = "latitude";
        String TAG_COORDINATES_LON = "longitude";



        protected Void doInBackground(Void... arg0) {

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

                    coords.add(longitude);
                    coords.add(",");
                    coords.add(latitude);
                    coords.add(",");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        protected void onPostExecute(Void result) {

            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.localization_alert), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();

        }

    }


}















