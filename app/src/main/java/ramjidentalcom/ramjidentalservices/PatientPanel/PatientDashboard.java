package ramjidentalcom.ramjidentalservices.PatientPanel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class PatientDashboard extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
    String jsonp;
    ArrayList<HashMap<String, String>> personList;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12,tv13,tv14,tv15,tv16,t17,tv18,tv19,tv20,tv21,tv22,tv23;
    String id="", visit = "",oid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv1 = findViewById(R.id.viewcustname);
        tv2 = findViewById(R.id.viewcustage);
        tv3 = findViewById(R.id.viewcustmobile);
        tv4 = findViewById(R.id.viewcustadd);
        tv5 = findViewById(R.id.viewcustcc);
        tv6 = findViewById(R.id.viewcustdiag);
        tv7 = findViewById(R.id.viewcustta);
        tv8 = findViewById(R.id.viewcusttd);
        tv9 = findViewById(R.id.viewcustamt);
        tv10 = findViewById(R.id.viewcustrcll);
        tv11 = findViewById(R.id.viewcustdq1);
        tv12 = findViewById(R.id.viewcustdq2);
        tv13 = findViewById(R.id.viewcustdq3);
        tv14 = findViewById(R.id.viewcustdq4);
        tv15 = findViewById(R.id.viewcusttaq1);
        tv16 = findViewById(R.id.viewcusttaq2);
        t17 = findViewById(R.id.viewcusttaq3);
        tv18= findViewById(R.id.viewcusttaq4);
        tv19 = findViewById(R.id.viewcusttdq1);
        tv20 = findViewById(R.id.viewcusttdq2);
        tv21 = findViewById(R.id.viewcusttdq3);
        tv22 = findViewById(R.id.viewcusttdq4);
        tv23 = findViewById(R.id.viewcustrmk);
        final Intent intent = getIntent();
        id=intent.getStringExtra("id");
        visit = intent.getStringExtra("vst");
        oid = intent.getStringExtra("oid");
        personList = new ArrayList<HashMap<String,String>>();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PatientDashboard.this,PatientImages.class);
                intent1.putExtra("id",id);
                intent1.putExtra("oid",oid);
                intent1.putExtra("visit",visit);
                startActivity(intent1);
                finish();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
            } });

        checkConnection();
        if(isConnected){
            FetchInBackground fb = new FetchInBackground();
            fb.execute(id,visit);
        }
        else{
            checkConnection();
            if(isConnected)
                Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();

        }
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
    protected void showData(){
        JSONObject firstObject = null;
        try {
            firstObject = new JSONObject(jsonp);
            JSONArray firstArray = firstObject.getJSONArray("custcase");
            String  id= "";
            String name = "";
            String age = "";
            String ph = "";
            String add = "";
            String cc = "";
            String di = "";
            String ta = "";
            String td = "";
            String amt = "";
            String rcll = "";
            String rmk = "";
            String dq1 = "";
            String dq2 = "";
            String dq3 = "";
            String dq4 = "";
            String taq1 = "";
            String taq2 = "";
            String taq3 = "";
            String taq4 = "";
            String tdq1 = "";
            String tdq2 = "";
            String tdq3 = "";
            String tdq4 = "";

            for (int i = 0; i < firstArray.length(); i++) {
                JSONObject lastObject = firstArray.getJSONObject(i);
                id = lastObject.getString("uid");
                name = lastObject.getString("name");
                age = lastObject.getString("age");
                ph = lastObject.getString("phone");
                add = lastObject.getString("address");
                cc = lastObject.getString("cc");
                di = lastObject.getString("diagnosis");
                ta = lastObject.getString("ta");
                td = lastObject.getString("td");
                amt = lastObject.getString("amount");
                rcll = lastObject.getString("recall");
                rmk = lastObject.getString("remarks");
                dq1 = lastObject.getString("diag_q1");
                dq2 = lastObject.getString("diag_q2");
                dq3 = lastObject.getString("diag_q3");
                dq4 = lastObject.getString("diag_q4");
                taq1 = lastObject.getString("tadvise_q1");
                taq2 = lastObject.getString("tadvise_q2");
                taq3 = lastObject.getString("tadvise_q3");
                taq4 = lastObject.getString("tadvise_q4");
                tdq1 = lastObject.getString("tdone_q1");
                tdq2 = lastObject.getString("tdone_q2");
                tdq3 = lastObject.getString("tdone_q3");
                tdq4 = lastObject.getString("tdone_q4");


                HashMap<String, String> admins = new HashMap<String, String>();

                admins.put("name", name);
                admins.put("age", age);
                admins.put("phone", ph);
                admins.put("address", add);
                admins.put("cc", cc);
                admins.put("diagnosis", di);
                admins.put("ta", ta);
                admins.put("td", td);
                admins.put("amt", amt);
                admins.put("rcll", rcll);
                admins.put("rmk",rmk);
                admins.put("dq1", dq1);
                admins.put("dq2", dq2);
                admins.put("dq3", dq3);
                admins.put("dq4", dq4);
                admins.put("taq1", taq1);
                admins.put("taq2", taq2);
                admins.put("taq3", taq3);
                admins.put("taq4", taq4);
                admins.put("tdq1", tdq1);
                admins.put("tdq2", tdq2);
                admins.put("tdq3", tdq3);
                admins.put("tdq4", tdq4);


                personList.add(admins);
            }

            tv1.setText(name);
            tv2.setText(age);
            tv3.setText(ph);
            tv4.setText(add);
            tv5.setText(cc);
            tv6.setText(di);
            tv7.setText(ta);
            tv8.setText(td);
            tv9.setText(amt);
            tv10.setText(rcll);
            tv11.setText(dq1);
            tv12.setText(dq2);
            tv13.setText(dq3);
            tv14.setText(dq4);
            tv15.setText(taq1);
            tv16.setText(taq2);
            t17.setText(taq3);
            tv18.setText(taq4);
            tv19.setText(tdq1);
            tv20.setText(tdq2);
            tv21.setText(tdq3);
            tv22.setText(tdq4);
            tv23.setText(rmk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class FetchInBackground extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String json_url = "http://ramjidental.com/RamjiApp/fetchCustCase.php";
            String result = null;
            try {
                String user_id=params[0];
                String user_vst=params[1];
                URL url = new URL(json_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStream outstream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                String post_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                +URLEncoder.encode("user_vst","UTF-8")+"="+URLEncoder.encode(user_vst,"UTF-8");
                writer.write(post_data);
                writer.flush();
                writer.close();
                outstream.close();


                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));
                result = "";
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result += line;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            jsonp=result;
            showData();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientDashboard.this,PatDashboard.class);
        intent.putExtra("id",id);
        intent.putExtra("oid",oid);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent = new Intent(PatientDashboard.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}