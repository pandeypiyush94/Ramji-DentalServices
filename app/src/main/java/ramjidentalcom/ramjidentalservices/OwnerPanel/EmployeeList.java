package ramjidentalcom.ramjidentalservices.OwnerPanel;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.EmployeePanel.EmployeeDashboard;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class EmployeeList extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;

    String jsonp;
    AlertDialog al;
    ArrayList<HashMap<String, String>> personList;
    ListView list;
     String ii = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        al = new AlertDialog.Builder(EmployeeList.this).create();
        al.setTitle("Registration Status");
        list = findViewById(R.id.employeelistview);
        Intent intent = getIntent();
        ii = intent.getStringExtra("id");
        personList = new ArrayList<HashMap<String,String>>();
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
            JSONArray firstArray = firstObject.getJSONArray("employees");

            for (int i = 0; i < firstArray.length(); i++) {
                JSONObject lastObject = firstArray.getJSONObject(i);
                String id = lastObject.getString("uid");
                String name = lastObject.getString("name");
                String sal = lastObject.getString("salary");
                String add = lastObject.getString("address");
                String phone = lastObject.getString("phone");

                HashMap<String, String> admins = new HashMap<String, String>();
                admins.put("uid", id);
                admins.put("name", name);
                admins.put("salary", sal);
                admins.put("address", add);
                admins.put("phone", phone);

                personList.add(admins);
            }
            ListAdapter adapter = new SimpleAdapter(EmployeeList.this, personList, R.layout.listemployeeview,
                    new String[]{"uid", "name", "salary", "phone", "address"},
                    new int[]{R.id.viewempid, R.id.viewempname, R.id.viewempsalary, R.id.viewempmobile, R.id.viewempadd});
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) { checkConnection();
                    if(isConnected){
                        Intent intent = new Intent(EmployeeList.this,EmployeeDashboard.class);
                        String item = personList.get(position).get("uid");
                        intent.putExtra("allid",item);
                        intent.putExtra("iid",ii);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EmployeeList.this,ChooseView.class);
        intent.putExtra("allid",ii);
        startActivity(intent);
        finish();
    }
    public void getData() {
        class FetchInBackground extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String json_url = "http://ramjidental.com/RamjiApp/fetchemp.php";
                String result = null;
                try {
                    URL url = new URL(json_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

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
        fb.execute();
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
                intent = new Intent(EmployeeList.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
