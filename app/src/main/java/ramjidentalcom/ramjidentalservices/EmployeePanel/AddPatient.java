package ramjidentalcom.ramjidentalservices.EmployeePanel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;


public class AddPatient extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
   private String jsonp;
   private String id = "",id2="",oid="",eid="";
    TextView tv2;
   private Button CustomerSignUp_Button;

   private ArrayList<HashMap<String, String>> personList;
    private TextView CustomerId_Text,CustomerName_Text,CustomerAge_Text,CustomerCC_Text,CustomerDiag_Text,CustomerTA_Text,CustomerTD_Text,
            CustomerAmt_Text,CustomerRcll_Text,dgq1,dgq2,dgq3,dgq4,tadq1,tadq2,tadq3,tadq4,tddq1,tddq2,tddq3,tddq4;
    private GetLogin getLogin = new GetLogin(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empadd_patient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CustomerId_Text = findViewById(R.id.viewcustid);
        CustomerName_Text = findViewById(R.id.viewcustname);
        CustomerAge_Text = findViewById(R.id.viewcustage);
        CustomerCC_Text = findViewById(R.id.viewcustcc);
        CustomerDiag_Text = findViewById(R.id.viewcustdiag);
        CustomerTA_Text = findViewById(R.id.viewcustta);
        CustomerTD_Text = findViewById(R.id.viewcusttd);
        CustomerAmt_Text = findViewById(R.id.viewcustamt);
       CustomerSignUp_Button = findViewById(R.id.custsignup);
        CustomerRcll_Text = findViewById(R.id.viewcustrcll);
        dgq1 = findViewById(R.id.viewdq1);
        dgq2 = findViewById(R.id.viewdq2);
        dgq3 = findViewById(R.id.viewdq3);
        dgq4 = findViewById(R.id.viewdq4);
        tadq1 = findViewById(R.id.viewtaq1);
        tadq2 = findViewById(R.id.viewtaq2);
        tadq3 = findViewById(R.id.viewtaq3);
        tadq4 = findViewById(R.id.viewtaq4);
        tddq1 = findViewById(R.id.viewtdq1);
        tddq2 = findViewById(R.id.viewtdq2);
        tddq3 = findViewById(R.id.viewtdq3);
        tddq4 = findViewById(R.id.viewtdq4);

        Intent intent = getIntent();
        id = intent.getStringExtra("custid");
        oid = intent.getStringExtra("iid");
        eid = intent.getStringExtra("allid");
        id2 = intent.getStringExtra("id");

        long date = System.currentTimeMillis();
        String s = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(s, Locale.ENGLISH);
        final String dateString = sdf.format(date);
        Toast.makeText(getApplicationContext(),eid,Toast.LENGTH_SHORT).show();
        //tv2.setText(dateString);
        checkConnection();
        CustomerSignUp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {checkConnection();
                if(isConnected){
                    String type="login";
                    getLogin.execute(type,eid,CustomerId_Text.getText().toString(),
                            CustomerName_Text.getText().toString(),CustomerTD_Text.getText().toString(),
                            CustomerAmt_Text.getText().toString(),dateString);
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

        personList = new ArrayList<HashMap<String,String>>();
        if (isConnected) {
            FetchInBackground fb = new FetchInBackground();
            fb.execute(id);
        } else
            Toast.makeText(getApplicationContext(),"Not Connected to Internet",Toast.LENGTH_LONG).show();
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
    //Async Method for Background Tasks
    public class GetLogin extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog al;
        GetLogin(Context ctx){
            context=ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String path = "http://ramjidental.com/RamjiApp/registerEmpCust.php";
            if(type.equals("login")){
                try {
                    String emp_id = params[1];
                    String user_id = params[2];
                    String user_name = params[3];
                    String user_td = params[4];
                    String user_amount = params[5];
                    String user_date = params[6];
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("empid","UTF-8")+"="+URLEncoder.encode(emp_id,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                            +URLEncoder.encode("td","UTF-8")+"="+URLEncoder.encode(user_td,"UTF-8")+"&"
                            +URLEncoder.encode("amount","UTF-8")+"="+URLEncoder.encode(user_amount,"UTF-8")+"&"
                            +URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(user_date,"UTF-8");
                    writer.write(post_data);
                    writer.flush();
                    writer.close();
                    outstream.close();

                    InputStream inputstream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.ISO_8859_1));
                    String result = "";
                    String line="";
                    while((line = reader.readLine())!=null) {
                        result += line;
                    }
                    reader.close();
                    inputstream.close();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("success")) {
                al.setMessage("Patient Added");
                al.show();
                Intent intent = new Intent(AddPatient.this,EmployeeDashboard.class);
                intent.putExtra("id",id2);
                intent.putExtra("allid",eid);
                intent.putExtra("iid",oid);
                startActivity(intent);
                finish();
            } else {
                al.setMessage("Registration Failed");
                al.show();
            }
        }

        @Override
        protected void onPreExecute() {
            al = new AlertDialog.Builder(context).create();
            al.setTitle("Registration Status");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    protected void showData(){
        try {
            JSONObject firstObject = new JSONObject(jsonp);
            JSONArray firstArray = firstObject.getJSONArray("allcust");

            String id = "";
            String hosp = "";
            String name = "";
            String age = "";
            String cc = "";
            String diag = "";
            String ta = "";
            String td = "";
            String amt = "";
            String rcll = "";
            String dq1 = "";
            String dq2 = "";
            String dq3 = "";
            String dq4 = "";
            String taq1 = "";
            String taq2 = "";
            String taq3 = "";
            String taq4 = "";
            String tdq1 = "";
            String tfq2 = "";
            String tdq3 = "";
            String tdq4 = "";
            String disc = "";

            for (int i = 0; i < firstArray.length(); i++) {
                JSONObject lastObject = firstArray.getJSONObject(i);
                id = lastObject.getString("uid");
                hosp = lastObject.getString("client");
                name = lastObject.getString("name");
                age = lastObject.getString("age");
                cc = lastObject.getString("cc");
                diag = lastObject.getString("diagnosis");
                ta = lastObject.getString("ta");
                td = lastObject.getString("td");
                amt = lastObject.getString("amount");
                rcll = lastObject.getString("recall");
                dq1 =  lastObject.getString("diagnosis_q1");
                dq2 =  lastObject.getString("diagnosis_q2");
                dq3 =  lastObject.getString("diagnosis_q3");
                dq4 =  lastObject.getString("diagnosis_q4");
                taq1 = lastObject.getString("tadvise_q1");
                taq2 = lastObject.getString("tadvise_q2");
                taq3 = lastObject.getString("tadvise_q3");
                taq4 = lastObject.getString("tadvise_q4");
                tdq1 = lastObject.getString("tdone_q1");
                tfq2 = lastObject.getString("tdone_q2");
                tdq3 = lastObject.getString("tdone_q3");
                tdq4 = lastObject.getString("tdone_q4");
                disc = lastObject.getString("discount");

                HashMap<String, String> admins = new HashMap<String, String>();
                admins.put("uid", id);
                admins.put("name", name);
                admins.put("age", age);
                admins.put("cc", cc);
                admins.put("diagnosis", diag);
                admins.put("ta", ta);
                admins.put("td", td);
                admins.put("amount", amt);
                admins.put("recall", rcll);
                admins.put("diagnosis_q1", dq1);
                admins.put("diagnosis_q2", dq2);
                admins.put("diagnosis_q3", dq3);
                admins.put("diagnosis_q4", dq4);
                admins.put("tadvise_q1", taq1);
                admins.put("tadvise_q2", taq2);
                admins.put("tadvise_q3", taq3);
                admins.put("tadvise_q4", taq4);
                admins.put("tdone_q1", tdq1);
                admins.put("tdone_q2", tfq2);
                admins.put("tdone_q3", tdq3);
                admins.put("tdone_q4", tdq4);

                personList.add(admins);
            }
            CustomerId_Text.setText(id);
            CustomerName_Text.setText(name);
            CustomerAge_Text.setText(age);
            CustomerCC_Text.setText(cc);
            CustomerDiag_Text.setText(diag);
            CustomerTA_Text.setText(ta);
            CustomerTD_Text.setText(td);
            CustomerAmt_Text.setText(amt);
            CustomerRcll_Text.setText(rcll);
            dgq1.setText(dq1);
            dgq2.setText(dq2);
            dgq3.setText(dq3);
            dgq4.setText(dq4);
            tadq1.setText(taq1);
            tadq2.setText(taq2);
            tadq3.setText(taq3);
            tadq4.setText(taq4);
            tddq1.setText(tdq1);
            tddq2.setText(tfq2);
            tddq3.setText(tdq3);
            tddq4.setText(tdq4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class FetchInBackground extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String json_url = "http://ramjidental.com/RamjiApp/fetchallcust.php";
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent;
                intent = new Intent(AddPatient.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddPatient.this,EmployeeDashboard.class);
        intent.putExtra("allid",eid);
        intent.putExtra("iid",oid);
        startActivity(intent);
        finish();
    }

}
