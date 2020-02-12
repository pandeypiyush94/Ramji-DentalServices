package ramjidentalcom.ramjidentalservices.AdminPanel.Viewers;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
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

import ramjidentalcom.ramjidentalservices.AdminPanel.AdminDashboard;
import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.PatientPanel.PatDashboard;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class PatientViewer extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
    String jsonp;
    ArrayList<HashMap<String, String>> personList;
    ListView list;
    AlertDialog al;
    String aid="",aid1="",aid2="",oid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        al = new AlertDialog.Builder(PatientViewer.this).create();
        al.setTitle("Registration Status");
        personList = new ArrayList<HashMap<String,String>>();
        Intent intent = getIntent();
        aid = intent.getStringExtra("allid");
        oid = intent.getStringExtra("oid");
        //Toast.makeText(getApplicationContext(),aid, Toast.LENGTH_SHORT).show();
        if (aid.contains(":")) {
            String[] s = aid.split(":");
            aid1 = s[0];
            aid2 = s[1];
        }
        checkConnection();
        if(isConnected){
            getData();
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
            JSONArray firstArray = firstObject.getJSONArray("customers");

            for (int i = 0; i < firstArray.length(); i++) {
                JSONObject lastObject = firstArray.getJSONObject(i);
                String id = lastObject.getString("uid");
                String name = lastObject.getString("name");
                String age = lastObject.getString("age");
                String cc = lastObject.getString("cc");
                String diag = lastObject.getString("diagnosis");
                String ta = lastObject.getString("ta");
                String td = lastObject.getString("td");
                String amt = lastObject.getString("amount");
                String rcll = lastObject.getString("recall");

                HashMap<String, String> admins = new HashMap<String, String>();
                admins.put("uid", id);
                admins.put("name", name);

                personList.add(admins);
            }
            list = findViewById(R.id.patientlistview);
            ListAdapter adapter = new SimpleAdapter(PatientViewer.this, personList, R.layout.listpatientview,
                    new String[]{"uid", "name"},
                    new int[]{R.id.viewcustid, R.id.viewcustname});
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(PatientViewer.this,PatDashboard.class);
                    String item = personList.get(position).get("uid");
                    intent.putExtra("id",item);
                    intent.putExtra("oid",aid);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData() {
        class FetchInBackground extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String json_url = "http://ramjidental.com/RamjiApp/fetchcust.php";
                String result = null;
                try {
                    String admin_id = params[0];
                    URL url = new URL(json_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("admin_id","UTF-8")+"="+URLEncoder.encode(admin_id,"UTF-8");
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
                if ("success".equals(result)) {
                    al.setMessage("No Patient Registered Yet...You have to Register first..");
                    al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(PatientViewer.this,AdminDashboard.class);
                            intent.putExtra("allid",aid);
                            intent.putExtra("oid",oid);
                            startActivity(intent);
                            finish();
                        }
                    });
                    al.show();
                } else {
                    jsonp = result;
                    showData();
                }
            }
        }
        FetchInBackground fb = new FetchInBackground();
        fb.execute(aid1);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientViewer.this, AdminDashboard.class);
        intent.putExtra("allid",aid);
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
                Intent intent;
                intent = new Intent(PatientViewer.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
