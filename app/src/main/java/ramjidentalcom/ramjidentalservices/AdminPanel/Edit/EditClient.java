package ramjidentalcom.ramjidentalservices.AdminPanel.Edit;

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

import ramjidentalcom.ramjidentalservices.AdminPanel.Viewers.ClientViewer;
import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.OwnerPanel.OwnerDashboard;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class EditClient extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{

    TextView tv1;
    EditText et5,et1,et2,et3,et4,et6;
    String jsonp;
    boolean isConnected;
    Button bt1;
    String id="";
    String aid = "",oid = "";
    ArrayList<HashMap<String, String>> personList;
    String uid,name,salary,add,ph,cards,rk;
    GetLogin getLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_client);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv1 = findViewById(R.id.clntid);
        et1 = findViewById(R.id.clntname);
        et2 = findViewById(R.id.clntrefamount);
        et3 = findViewById(R.id.clntcards);
        et4 = findViewById(R.id.clntphone);
        et5 = findViewById(R.id.clntaddress);
        et6 = findViewById(R.id.clntremarks);
        bt1 = findViewById(R.id.clntsignup);
        personList = new ArrayList<HashMap<String, String>>();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        aid = intent.getStringExtra("allid");
        oid = intent.getStringExtra("oid");
        tv1.setText(id);
        FetchInBackground fb = new FetchInBackground();
        checkConnection();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uid = tv1.getText().toString();
                name = et1.getText().toString();
                salary = et2.getText().toString();
                cards = et3.getText().toString();
                ph = et4.getText().toString();
                add = et5.getText().toString();
                rk = et6.getText().toString();

                checkConnection();
                if (isConnected) {
                    String type = "login";
                    getLogin = new GetLogin(getApplicationContext());
                    getLogin.execute(type, uid, name,salary,cards,add,ph,rk);

                } else{
                    checkConnection();
                    if(isConnected)
                        Toast.makeText(getApplicationContext(), "Internet is Connected", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                }}});
        if (isConnected) {
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
        checkConnection();
        if(isConnected)
            Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
        else{
            Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EditClient.this,OwnerDashboard.class);
            startActivity(intent);
        }
    }
    class FetchInBackground extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String json_url = "http://ramjidental.com/RamjiApp/fetchclntuid.php";
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
    protected void showData(){
        try {
            JSONObject firstObject = new JSONObject(jsonp);
            JSONArray firstArray = firstObject.getJSONArray("clntuid");
            String name = "",sal = "",cards="", ph = "",add = "",admin="",rk="";
            for (int i = 0; i < firstArray.length(); i++) {
                JSONObject lastObject = firstArray.getJSONObject(i);
                String id = lastObject.getString("uid");
                name = lastObject.getString("name");
                sal = lastObject.getString("ref_amount");
                cards = lastObject.getString("card_issue");
                ph = lastObject.getString("mobile");
                add = lastObject.getString("address");
                admin = lastObject.getString("adminid");
                rk = lastObject.getString("remarks");


                HashMap<String, String> admins = new HashMap<String, String>();
                admins.put("name", name);
                admins.put("ref_amount", sal);
                admins.put("card_issue",cards);
                admins.put("mobile",ph);
                admins.put("address",add);
                personList.add(admins);
            }
            et1.setText(name);
            et2.setText(sal);
            et3.setText(cards);
            et4.setText(ph);
            et5.setText(add);
            et6.setText(rk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            String path = "http://ramjidental.com/RamjiApp/EditClnt.php";
            if(type.equals("login")){
                try {
                    String user_id = params[1];
                    String user_name = params[2];
                    String user_sal = params[3];
                    String user_card = params[4];
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
                            +URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                            +URLEncoder.encode("user_sal","UTF-8")+"="+URLEncoder.encode(user_sal,"UTF-8")+"&"
                            +URLEncoder.encode("user_card","UTF-8")+"="+URLEncoder.encode(user_card,"UTF-8")+"&"
                            +URLEncoder.encode("user_add","UTF-8")+"="+URLEncoder.encode(user_add,"UTF-8")+"&"
                            +URLEncoder.encode("user_phone","UTF-8")+"="+URLEncoder.encode(user_phone,"UTF-8")+"&"
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
                al.setMessage("Employee Updated");
                al.show();
                Intent intent = new Intent(EditClient.this,ClientViewer.class);
                intent.putExtra("allid",aid);
                intent.putExtra("oid",oid);
                startActivity(intent);
                finish();
            } else {
                al.setMessage("Updation Failed");
                al.show();
                tv1.setText(id);
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et6.setText("");
            }
        }


        @Override
        protected void onPreExecute() {
            al = new AlertDialog.Builder(EditClient.this).create();
            al.setTitle("Updation Status");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditClient.this,ClientViewer.class);
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
                intent = new Intent(EditClient.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
