package ramjidentalcom.ramjidentalservices.PatientPanel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import ramjidentalcom.ramjidentalservices.AdminPanel.Viewers.PatientViewer;
import ramjidentalcom.ramjidentalservices.ClientPanel.CheckPatients;
import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.EmployeePanel.CaseHistory;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.OwnerPanel.PatientList;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class PatDashboard extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
    String jsonp;
    ArrayList<HashMap<String, String>> personList;
    ListView list;
    String id="",aid = "",oid = "",eid = "",ctid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient0_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        personList = new ArrayList<HashMap<String,String>>();
        Intent intw = getIntent();
        id = intw.getStringExtra("id");
        oid = intw.getStringExtra("oid");
       // Toast.makeText(getApplicationContext(),oid, Toast.LENGTH_SHORT).show();
        checkConnection();
        if(isConnected){
            FetchInBackground fb = new FetchInBackground();
            fb.execute(id);
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
        try {
            JSONObject firstObject = new JSONObject(jsonp);
            final JSONArray firstArray = firstObject.getJSONArray("client5");

            for (int i = 0; i < firstArray.length(); i++) {
                JSONObject lastObject = firstArray.getJSONObject(i);
                String visit = lastObject.getString("visit");
                String id = lastObject.getString("uid");
                String name = lastObject.getString("name");
                String age = lastObject.getString("age");
                String ph = lastObject.getString("phone");
                String add = lastObject.getString("address");
                String cc = lastObject.getString("cc");
                String diag = lastObject.getString("diagnosis");
                String ta = lastObject.getString("ta");
                String td = lastObject.getString("td");
                String amt = lastObject.getString("amount");
                String rcll = lastObject.getString("recall");
                String dq1 = lastObject.getString("diag_q1");
                String dq2 = lastObject.getString("diag_q2");
                String dq3 = lastObject.getString("diag_q3");
                String dq4 = lastObject.getString("diag_q4");
                String taq1 = lastObject.getString("tadvise_q1");
                String taq2 = lastObject.getString("tadvise_q2");
                String taq3 = lastObject.getString("tadvise_q3");
                String taq4 = lastObject.getString("tadvise_q4");
                String tdq1 = lastObject.getString("tdone_q1");
                String tdq2 = lastObject.getString("tdone_q2");
                String tdq3 = lastObject.getString("tdone_q3");
                String tdq4 = lastObject.getString("tdone_q4");

                HashMap<String, String> admins = new HashMap<String, String>();
                admins.put("visit", visit);
                admins.put("uid", id);

                personList.add(admins);
            }
            list = findViewById(R.id.patientdashlistview);
            ListAdapter adapter = new SimpleAdapter(PatDashboard.this, personList, R.layout.listpatientvisit,
                    new String[]{"visit", "uid"},
                    new int[]{R.id.viewcustvisit, R.id.viewcustid});
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(PatDashboard.this,PatientDashboard.class);
                    String item = personList.get(position).get("uid");
                    String item1 = personList.get(position).get("visit");
                    intent.putExtra("id",item);
                    intent.putExtra("vst",item1);
                    intent.putExtra("oid",oid);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class FetchInBackground extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String json_url = "http://ramjidental.com/RamjiApp/fetchClntCust3.php";
            String result = null;
            try {
                String user_id=params[0];
                URL url = new URL(json_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStream outstream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                String post_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (oid != null) {
            if (oid.matches("(.*)owner(.*)")) {
                Intent intent = new Intent(PatDashboard.this,PatientList.class);
                intent.putExtra("allid",oid);
                startActivity(intent);
                finish();
            } else if (oid.matches("(.*)admin(.*)")){
                Intent intent = new Intent(PatDashboard.this,PatientViewer.class);
                intent.putExtra("allid",oid);
                startActivity(intent);
                finish();
            } else if (oid.matches("(.*)emp(.*)")){
                Intent intent = new Intent(PatDashboard.this,CaseHistory.class);
                intent.putExtra("allid",oid);
                startActivity(intent);
                finish();
            } else if (oid.matches("(.*)clnt(.*)")){
                Intent intent = new Intent(PatDashboard.this,CheckPatients.class);
                intent.putExtra("allid",oid);
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Please Logout Firstt...", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(),"Please Logout First...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent;
                intent = new Intent(PatDashboard.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
