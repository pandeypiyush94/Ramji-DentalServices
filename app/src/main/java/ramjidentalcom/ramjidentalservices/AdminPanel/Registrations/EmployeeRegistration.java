package ramjidentalcom.ramjidentalservices.AdminPanel.Registrations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import ramjidentalcom.ramjidentalservices.AdminPanel.AdminDashboard;
import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class EmployeeRegistration extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
    TextView tv1;
    EditText et1,et2,et3,et4,et5;
    Button bt1;
    String aid="",aid1="",aid2="",oid="";
    String id="";
    String uid,name,sal,add,ph,rk;
    GetLogin getLogin = new GetLogin(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_registration);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv1 = findViewById(R.id.empid);
        et1 = findViewById(R.id.empname);
        et2 = findViewById(R.id.empsalary);
        et3 = findViewById(R.id.empphone);
        et4 = findViewById(R.id.empaddress);
        et5 = findViewById(R.id.empremarks);
        bt1 = findViewById(R.id.empsignup);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        aid = intent.getStringExtra("allid");
        oid = intent.getStringExtra("oid");
        tv1.setText(id);
        if (aid.contains(":")) {
            String[] s = aid.split(":");
            aid1 = s[0];
            aid2 = s[1];
        }
checkConnection();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkConnection();
                uid = tv1.getText().toString();
                name = et1.getText().toString();
                sal = et2.getText().toString();
                add = et4.getText().toString();
                ph = et3.getText().toString();
                rk = et5.getText().toString();
                if(name.equals("")){
                    et1.setError("Fields cannot be left empty");
                    return;
                } else if(sal.equals("")){
                    et2.setError("Fields cannot be left empty");
                    return;
                } else if(add.equals("")){
                    et4.setError("Fields cannot be left empty");
                    return;
                } else if(ph.equals("")){
                    et3.setError("Fields cannot be left empty");
                    return;
                }else if(rk.equals("")){
                    et5.setError("Fields cannot be left empty");
                    return;
                } else {
                    if (isConnected) {
                        String type = "login";
                        getLogin.execute(type, uid, aid1, name, sal, add, ph,rk);
                    } else {
                        checkConnection();
                        if (isConnected)
                            Toast.makeText(getApplicationContext(), "Connected To Internet", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Internet is Not Connected", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
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
            String path = "http://ramjidental.com/RamjiApp/registerEmp.php";
            if(type.equals("login")){
                try {
                    String user_id = params[1];
                    String admin_id = params[2];
                    String user_name = params[3];
                    String user_sal = params[4];
                    String user_add = params[5];
                    String user_phone = params[6];
                    String remarks = params[7];
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("admin_id","UTF-8")+"="+URLEncoder.encode(admin_id,"UTF-8")+"&"
                            +URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                            +URLEncoder.encode("user_sal","UTF-8")+"="+URLEncoder.encode(user_sal,"UTF-8")+"&"
                            +URLEncoder.encode("user_add","UTF-8")+"="+URLEncoder.encode(user_add,"UTF-8")+"&"
                            +URLEncoder.encode("user_phone","UTF-8")+"="+URLEncoder.encode(user_phone,"UTF-8") +"&"
                            +URLEncoder.encode("remarks","UTF-8")+"="+URLEncoder.encode(remarks,"UTF-8");
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
                al.setMessage("Employee Registered");
                al.show();
                Intent intent = new Intent(EmployeeRegistration.this, AdminDashboard.class);
                intent.putExtra("allid",aid);
                intent.putExtra("oid",oid);
                startActivity(intent);
                finish();
            } else {
                al.setMessage("Registration Failed");
                al.show();
                tv1.setText(id);
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et5.setText("");
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EmployeeRegistration.this, AdminDashboard.class);
        intent.putExtra("allid",aid);
        intent.putExtra("oid",oid);
        startActivity(intent);
        finish();
    }
}
