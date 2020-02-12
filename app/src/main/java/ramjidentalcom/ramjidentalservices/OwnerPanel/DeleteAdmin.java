package ramjidentalcom.ramjidentalservices.OwnerPanel;

import android.app.AlertDialog;
import android.content.Context;
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
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class DeleteAdmin extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
        boolean isConnected;
        String jsonp;
    ArrayList<HashMap<String, String>> personList;
        ListView list;
    String id = "";
    Context context;
    AlertDialog al;
    GetLogin getLogin = new GetLogin(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        al = new AlertDialog.Builder(DeleteAdmin.this).create();
        al.setTitle("Registration Status");
        id = intent.getStringExtra("id");
                checkConnection();
        list = findViewById(R.id.deleteadminlistview);
        personList = new ArrayList<HashMap<String,String>>();
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
    protected void showData(){
        try {
            JSONObject firstObject = new JSONObject(jsonp);
            JSONArray firstArray = firstObject.getJSONArray("admin");

            for (int i = 0; i < firstArray.length(); i++) {
                JSONObject lastObject = firstArray.getJSONObject(i);
                String id = lastObject.getString("uid");
                String name = lastObject.getString("name");
                String dept = lastObject.getString("department");
                String add = lastObject.getString("address");
                String phone = lastObject.getString("phone");

                HashMap<String, String> admins = new HashMap<String, String>();
                admins.put("uid", id);
                admins.put("name", name);
                admins.put("department", dept);
                admins.put("phone", phone);

                personList.add(admins);
            }
            ListAdapter adapter = new SimpleAdapter(DeleteAdmin.this, personList, R.layout.listdeleteadmin,
                    new String[]{"uid", "name", "department", "phone"},
                    new int[]{R.id.viewadminid, R.id.viewadminname, R.id.viewadmindept, R.id.viewadminmobile});
            list.setAdapter(adapter);
          list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, final int position, long iid) {
                  al.setMessage("Are you sure want to delete?");
                  al.setButton(DialogInterface.BUTTON_NEUTRAL, "YES", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                          if(isConnected){
                              String item = personList.get(position).get("uid");
                              if(isConnected){
                                  String type="login";
                                  getLogin.execute(type,item);
                              }
                              else{
                                  checkConnection();
                                  if(isConnected)
                                      Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
                                  else
                                      Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();

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
                  });
                  al.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                          startActivity(new Intent(DeleteAdmin.this,AdminDashboard.class));
                          finish();
                      }
                  });
                  al.show();
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
                String json_url = "http://ramjidental.com/RamjiApp/fetchdata.php";
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
                    al.setMessage("No Admin has been Registered Yet to Delete...You have to Register first..");
                    al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(DeleteAdmin.this,OwnerDashboard.class);
                            intent.putExtra("allid",id);
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
        fb.execute();
    }

    public class GetLogin extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog al;
        GetLogin(Context ctx){
            context=ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String path = "http://ramjidental.com/RamjiApp/deleteAdmin.php";
            if(type.equals("login")){
                try {
                    String user_id = params[1];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
              Intent intent = new Intent(DeleteAdmin.this,DeleteAdmin.class);
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
                intent = new Intent(DeleteAdmin.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.delete:
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void onBackPressed() {
        Intent intent = new Intent(DeleteAdmin.this,OwnerDashboard.class);
        intent.putExtra("allid",id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected)
            Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();
    }
}
