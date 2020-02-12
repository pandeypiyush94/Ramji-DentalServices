package ramjidentalcom.ramjidentalservices.OwnerPanel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class AddAdmin extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    TextView tv1,tv2;
    EditText et1,et2,et3;
    boolean isConnected;
    Button bt1;
    String id="",dept="";
    String uid,deptt,name,add,ph;
    GetLogin getLogin = new GetLogin(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv1 = findViewById(R.id.adminid);
        tv2 = findViewById(R.id.admindept);
        et1 = findViewById(R.id.adminname);
        et2 = findViewById(R.id.adminphone);
        et3 = findViewById(R.id.adminaddress);
        bt1 = findViewById(R.id.adminsignup);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        dept = intent.getStringExtra("dept");
        tv1.setText(id);
        tv2.setText(dept);
        checkConnection();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {checkConnection();

                uid = tv1.getText().toString();
                deptt = tv2.getText().toString();
                name = et1.getText().toString();
                add = et3.getText().toString();
                ph = et2.getText().toString();
                if(isConnected){
                    String type="login";
                    getLogin.execute(type,uid,deptt,name,add,ph);
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
    private void checkConnection(){
        isConnected = ConnectivityReceiver.isConnected();
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected)
            Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ReceiverInitiator.getInstance().setConnectivityListener(this);
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
            String path = "http://ramjidental.com/RamjiApp/register.php";
            if(type.equals("login")){
                try {
                    String user_id = params[1];
                    String user_deptt = params[2];
                    String user_name = params[3];
                    String user_add = params[4];
                    String user_phone = params[5];
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("user_dept","UTF-8")+"="+URLEncoder.encode(user_deptt,"UTF-8")+"&"
                            +URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                            +URLEncoder.encode("user_add","UTF-8")+"="+URLEncoder.encode(user_add,"UTF-8")+"&"
                            +URLEncoder.encode("user_phone","UTF-8")+"="+URLEncoder.encode(user_phone,"UTF-8");
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
                al.setMessage("Admin Registered");
                al.show();
                Intent intent = new Intent(AddAdmin.this,OwnerDashboard.class);
                startActivity(intent);
                finish();
            } else {
                al.setMessage("Registration Failed");
                al.show();
                tv1.setText(id);
                tv2.setText(dept);
                et1.setText("");
                et2.setText("");
                et3.setText("");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent;
                intent = new Intent(AddAdmin.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
