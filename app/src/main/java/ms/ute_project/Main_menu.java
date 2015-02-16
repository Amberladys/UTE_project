package ms.ute_project;

/**
 * Created by Marcus on 2015-02-02.
 */

 import android.app.Activity;
 import android.content.Context;
 import android.content.Intent;
 import android.net.ConnectivityManager;
 import android.net.NetworkInfo;
 import android.os.Bundle;
 import android.view.LayoutInflater;
 import android.view.Gravity;
 import android.view.View;
 import android.view.View.OnClickListener;
 import android.view.ViewGroup;
 import android.widget.Button;
 import android.widget.PopupWindow;
 import android.widget.TextView;






public class Main_menu extends Activity{

  private PopupWindow pwindo;


  Button close_popupButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startButton = (Button) findViewById(R.id.start_button);
        Button infoButton = (Button) findViewById(R.id.info_button);
        Button exitButton = (Button) findViewById(R.id.end_button);
        //Button mapButton = (Button) findViewById(R.id.map_button);

        TextView tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        // check if you are connected or not
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are connected");
        }
        else{
            tvIsConnected.setText("You are NOT connected");
        }


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opStart = new Intent(getApplicationContext(), Start_menu.class);
                startActivity(opStart);

            }
        });

        infoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindow();
            }
        });

        exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

        private OnClickListener cancel_button_click_listener = new OnClickListener() {
            public void onClick(View v) {
            pwindo.dismiss();

            }
        };


        private void initiatePopupWindow(){

            LayoutInflater inflater = (LayoutInflater) Main_menu.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_info_popup,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 750, 870, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            close_popupButton = (Button) layout.findViewById(R.id.close_popup_button);
            close_popupButton.setOnClickListener(cancel_button_click_listener);

        }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }




}