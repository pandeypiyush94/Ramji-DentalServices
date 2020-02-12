package ramjidentalcom.ramjidentalservices.AdminPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import ramjidentalcom.ramjidentalservices.AdminPanel.Scanners.CheckCustScanner;
import ramjidentalcom.ramjidentalservices.AdminPanel.Scanners.ClientScanner;
import ramjidentalcom.ramjidentalservices.AdminPanel.Scanners.ClntCustScanner;
import ramjidentalcom.ramjidentalservices.AdminPanel.Scanners.EmployeeScanner;
import ramjidentalcom.ramjidentalservices.AdminPanel.Scanners.PatientScanner;
import ramjidentalcom.ramjidentalservices.AdminPanel.Viewers.ClientViewer;
import ramjidentalcom.ramjidentalservices.AdminPanel.Viewers.ClntCustViewer;
import ramjidentalcom.ramjidentalservices.AdminPanel.Viewers.EmployeeViewer;
import ramjidentalcom.ramjidentalservices.AdminPanel.Viewers.PatientViewer;
import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.OwnerPanel.AdminsList;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class AdminDashboard extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
    Spinner sp1,sp2;
    TextView tv,tv1,tv3;
    String aid = "",id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkConnection();
        sp1 = findViewById(R.id.spinneraddadmin);
        sp2 = findViewById(R.id.spinnerviewadmin);
        tv = findViewById(R.id.spinnerclntcust);
        tv1 = findViewById(R.id.addclntcustomers);
        tv3 = findViewById(R.id.checkcusttext);
        Intent intent = getIntent();
        aid = intent.getStringExtra("allid");
        id = intent.getStringExtra("oid");
        //Toast.makeText(getApplicationContext(),aid, Toast.LENGTH_SHORT).show();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  checkConnection();
                if(isConnected){
                    Intent intent = new Intent(AdminDashboard.this, ClntCustViewer.class);
                    intent.putExtra("allid",aid);
                    intent.putExtra("oid",id);
                    startActivity(intent);
                    finish();
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

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkConnection();
                if(isConnected){
                    Intent intent1 = new Intent(AdminDashboard.this, ClntCustScanner.class);
                    intent1.putExtra("allid",aid);
                    intent1.putExtra("oid",id);
                    startActivity(intent1);
                    finish();
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
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  checkConnection();
                if(isConnected){
                    Intent intent2 = new Intent(AdminDashboard.this, CheckCustScanner.class);
                    intent2.putExtra("allid",aid);
                    intent2.putExtra("oid",id);
                    startActivity(intent2);
                    finish();
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
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long iid) { checkConnection();
                if(isConnected){
                    String item = parent.getItemAtPosition(position).toString();
                    if(item.equals("Employee")){
                        Intent intent = new Intent(AdminDashboard.this,EmployeeScanner.class);
                        intent.putExtra("allid",aid);
                        intent.putExtra("oid",id);
                        startActivity(intent);
                        finish();
                    } else if(item.equals("Patient")){
                        Intent intent = new Intent(AdminDashboard.this,PatientScanner.class);
                        intent.putExtra("allid",aid);
                        intent.putExtra("oid",id);
                        startActivity(intent);
                        finish();
                    } else if(item.equals("Clients")){
                        Intent intent = new Intent(AdminDashboard.this,ClientScanner.class);
                        intent.putExtra("allid",aid);
                        intent.putExtra("oid",id);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    checkConnection();
                    if(isConnected)
                        Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long iid) {  checkConnection();
                if(isConnected){
                    String item = parent.getItemAtPosition(position).toString();
                    if(item.equals("Patient")){
                        Intent intent = new Intent(AdminDashboard.this,PatientViewer.class);
                        intent.putExtra("allid",aid);
                        intent.putExtra("oid",id);
                        startActivity(intent);
                        finish();
                    } else if(item.equals("Employee")){
                        Intent intent = new Intent(AdminDashboard.this,EmployeeViewer.class);
                        intent.putExtra("allid",aid);
                        intent.putExtra("oid",id);
                        startActivity(intent);
                        finish();
                    } else if(item.equals("Clients")){
                        Intent intent = new Intent(AdminDashboard.this,ClientViewer.class);
                        intent.putExtra("allid",aid);
                        intent.putExtra("oid",id);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    checkConnection();
                    if(isConnected)
                        Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//
//Items for Adding Spinner
        List<String> addpeople = new ArrayList<String>();
        addpeople.add("Add");
        addpeople.add("Employee");
        addpeople.add("Patient");
        addpeople.add("Clients");
        addpeople.add("3rd Party");

        ArrayAdapter<String> addadapter =  new ArrayAdapter<String>(this, R.layout.customespinner,addpeople);
        addadapter.setDropDownViewResource(R.layout.customespinner);
        sp1.setAdapter(addadapter);

        List<String> viewpeople = new ArrayList<String>();
        viewpeople.add("View");
        viewpeople.add("Employee");
        viewpeople.add("Patient");
        viewpeople.add("Clients");
        viewpeople.add("3rd Party");

        ArrayAdapter<String> viewadapter =  new ArrayAdapter<String>(this, R.layout.customespinner,viewpeople);
        addadapter.setDropDownViewResource(R.layout.customespinner);
        sp2.setAdapter(viewadapter);

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent;
                intent = new Intent(AdminDashboard.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (id != null) {
            if (id.matches("(.*)owner(.*)")) {
             Intent intent = new Intent(AdminDashboard.this,AdminsList.class);
                intent.putExtra("id",id);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Please Logout First...", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(),"Please Logout Firstt...",Toast.LENGTH_SHORT).show();
    }
}
