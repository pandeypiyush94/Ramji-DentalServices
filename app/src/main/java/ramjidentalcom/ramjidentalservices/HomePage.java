package ramjidentalcom.ramjidentalservices;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    boolean exit=false;
    Button bt,bt1,bt2;
    TextView tv;
    boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bt = findViewById(R.id.button10);
        bt1 = findViewById(R.id.button4);
        bt2 = findViewById(R.id.button9);
        tv = findViewById(R.id.textView3);
        checkConnection();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if(isConnected){
                    Intent intent = new Intent(HomePage.this,LoginScanner.class);
                    startActivity(intent);
                }
                else{
                    checkConnection();
                    if(isConnected)
                        Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();

                }


            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             goToUrl("http://anshikatechnologies.com/");
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl ( "http://ramjidental.com/");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,Appointment.class));
                finish();
            }
        });    }
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void checkConnection(){
        isConnected = ConnectivityReceiver.isConnected();
    }

    @Override
    public void onBackPressed() {
        if(exit){
            finish();
        } else {
            Toast.makeText(getApplicationContext(),"Press Back again to Exit",Toast.LENGTH_SHORT).show();
            exit=true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit=false;
                }
            },3*1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReceiverInitiator.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        checkConnection();
        if(isConnected)
            Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();
    }
}
