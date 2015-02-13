package ms.ute_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import ms.ute_project.util.AsyncResponse;
import ms.ute_project.util.Localization;


public class Start_menu extends Activity implements AsyncResponse {


    Localization localization = new Localization();
    String url = null;
    TextView zoneText;

    public void processFinish(String output){
        url.concat(output);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        Button cityButton = (Button) findViewById(R.id.city_button);
        Button bikeButton = (Button) findViewById(R.id.bike_button);
        Button parkingButton = (Button) findViewById(R.id.parking_button);
        SeekBar zoneButton = (SeekBar) findViewById(R.id.zone_button);
        zoneText = (TextView) findViewById(R.id.zone_text);

        localization.delegate = this;
        localization.execute();

        zoneButton.setProgress(0);
        int prog = zoneButton.getProgress();
        zoneText.setText("Zasięg [km]:  " + String.valueOf(prog));

        cityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opCity = new Intent(getApplicationContext(), City_page.class);

                opCity.putExtra("zasieg", zoneText.getText().toString().substring(14));
                startActivity(opCity);

            }
        });

        bikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opBike = new Intent(getApplicationContext(), Bike_page.class);

                opBike.putExtra("zasieg", zoneText.getText().toString().substring(14));
                startActivity(opBike);
            }
        });

        parkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opPark = new Intent(getApplicationContext(), Park_and_ride_page.class);

                opPark.putExtra("zasieg", zoneText.getText().toString().substring(14));
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


                    zoneText.setText("Zasięg [km]:  "+String.valueOf(progress));

            }
        });






    }











}
