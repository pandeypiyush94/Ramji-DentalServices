package ramjidentalcom.ramjidentalservices.OwnerPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class ChooseView extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    boolean isConnected;
    ImageButton bt1,bt2,bt3,bt4;
    String oid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkConnection();
        Intent intent = getIntent();
        oid = intent.getStringExtra("allid");
        bt1 = findViewById(R.id.viewadminimage);
        bt2 = findViewById(R.id.viewpatientimage);
        bt3 = findViewById(R.id.viewemployeeimage);
        bt4 = findViewById(R.id.viewclientimage);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {checkConnection();
                if(isConnected){
                    Intent intent = new Intent(ChooseView.this,AdminsList.class);
                    intent.putExtra("id",oid);
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
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {checkConnection();
                if(isConnected){
                    Intent intent = new Intent(ChooseView.this,PatientList.class);
                    intent.putExtra("allid",oid);
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
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {checkConnection();
                if(isConnected){
                    Intent intent = new Intent(ChooseView.this,EmployeeList.class);
                    intent.putExtra("id",oid);
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
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {checkConnection();
                if(isConnected){
                    Intent intent = new Intent(ChooseView.this,ClientList.class);
                    intent.putExtra("id",oid);
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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChooseView.this,OwnerDashboard.class);
        intent.putExtra("allid",oid);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    private void checkConnection(){
        isConnected = ConnectivityReceiver.isConnected();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent;
                intent = new Intent(ChooseView.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ReceiverInitiator.getInstance().setConnectivityListener(this);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected)
            Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();
    }
}
