package ramjidentalcom.ramjidentalservices.AdminPanel.Registrations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.text.SimpleDateFormat;

import ramjidentalcom.ramjidentalservices.AdminPanel.AdminDashboard;
import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.MultiSelectionSpinner;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class ClntCustRegistration extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
    MultiSelectionSpinner s1,s2,s3,s4,s5,s6,s7,s8;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;
    EditText et1,et2,et3,et4,et5,et7,et8,et9,et10,et11;
    Button bt1;
    String aid="",aid1="",aid2="",oid="";
    String s="",t="",u="",vv="",w="",x="",y="",z="",a="",b="",cccc="",d="";
    String uid,client,name,age,ph,add,cc,diag,ta,td,amt,rcll,vst,date,rmk;
    GetLogin getLogin = new GetLogin(this);
    String id="",clientid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clnt_cust_registration);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        s1 = findViewById(R.id.mySpinner1);
        s2 = findViewById(R.id.mySpinner2);
        s3 = findViewById(R.id.mySpinner3);
        s4 = findViewById(R.id.mySpinner4);
        s5 = findViewById(R.id.mySpinner5);
        s6 = findViewById(R.id.mySpinner6);
        s7 = findViewById(R.id.mySpinner7);
        s8 = findViewById(R.id.mySpinner8);

        tv1 = findViewById(R.id.clntcustid);
        tv2 = findViewById(R.id.clntcustdate);
        tv3 = findViewById(R.id.clntcusttd);
        tv4 = findViewById(R.id.dq5);
        tv5 = findViewById(R.id.dq6);
        tv6 = findViewById(R.id.dq7);
        tv7 = findViewById(R.id.dq8);
        tv8 = findViewById(R.id.clntcustrcll);
        tv9 = findViewById(R.id.clnt);
        et1 = findViewById(R.id.clntcustname);
        et2 = findViewById(R.id.clntcustage);
        et3 = findViewById(R.id.clntcustcc);
        et4 = findViewById(R.id.clntcustdiagnosis);
        et5 = findViewById(R.id.clntcustta);
        et7 = findViewById(R.id.clntcustamount);
        et8 = findViewById(R.id.clntcustphone);
        et9 = findViewById(R.id.clntcustvisit);
        et10 = findViewById(R.id.clntcustadd);
        et11 = findViewById(R.id.clntcustremarks);
        bt1 = findViewById(R.id.clntcustsignup);

        long dae = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        String dateString = sdf.format(dae);
        tv2.setText(dateString);
        Intent intent = getIntent();
        aid = intent.getStringExtra("allid");
        oid = intent.getStringExtra("oid");
        id = intent.getStringExtra("id");
        clientid = intent.getStringExtra("hosp");
        tv1.setText(id);
        tv9.setText(clientid);
        if (aid.contains(":")) {
            String[] s = aid.split(":");
            aid1 = s[0];
            aid2 = s[1];
        }
        Toast.makeText(getApplicationContext(), aid1, Toast.LENGTH_SHORT).show();
        checkConnection();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkConnection();
                uid = tv1.getText().toString();
                name = et1.getText().toString();
                age = et2.getText().toString();
                ph = et8.getText().toString();
                add = et10.getText().toString();
                cc = et3.getText().toString();
                diag = et4.getText().toString();
                ta = et5.getText().toString();
                td = tv3.getText().toString();
                amt = et7.getText().toString();
                rcll = tv8.getText().toString();
                vst = et9.getText().toString();
                rmk = et11.getText().toString();
                s = s1.getSelectedItemsAsString();
                t = s2.getSelectedItemsAsString();
                u = s3.getSelectedItemsAsString();
                vv = s4.getSelectedItemsAsString();
                w = s5.getSelectedItemsAsString();
                x = s6.getSelectedItemsAsString();
                y = s7.getSelectedItemsAsString();
                z = s8.getSelectedItemsAsString();
                a = tv4.getText().toString();
                b = tv5.getText().toString();
                cccc = tv6.getText().toString();
                d =  tv7.getText().toString();
                date = tv2.getText().toString();
                client = tv9.getText().toString();
                if(name.equals("")){
                    et1.setError("Fields cannot be left empty");
                    return;
                } else if(age.equals("")){
                    et2.setError("Fields cannot be left empty");
                    return;
                } else if(ph.equals("")){
                    et8.setError("Fields cannot be left empty");
                    return;
                } else if(add.equals("")){
                    et10.setError("Fields cannot be left empty");
                    return;
                } else if(cc.equals("")){
                    et3.setError("Fields cannot be left empty");
                    return;
                } else if(diag.equals("")){
                    et4.setError("Fields cannot be left empty");
                    return;
                } else if(ta.equals("")){
                    et5.setError("Fields cannot be left empty");
                    return;
                } else if(amt.equals("")){
                    et7.setError("Fields cannot be left empty");
                    return;
                }  else if(vst.equals("")){
                    et9.setError("Fields cannot be left empty");
                    return;
                }  else if(rmk.equals("")){
                    et11.setError("Fields cannot be left empty");
                    return;
                } else {
                    if (isConnected) {
                        String type = "login";
                        getLogin.execute(type, uid, name, age, cc, diag, ta, td, amt, rcll, s, t, u, vv, w, x, y, z, a, b, cccc, d, vst, date, ph, add, client,aid1,rmk);
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
            String path = "http://ramjidental.com/RamjiApp/registerClntCust.php";
            if(type.equals("login")){
                try {
                    String user_id = params[1];
                    String user_name = params[2];
                    String user_age = params[3];
                    String user_cc = params[4];
                    String user_diag = params[5];
                    String user_ta = params[6];
                    String user_td = params[7];
                    String user_amt = params[8];
                    String user_rcll = params[9];
                    String user_s = params[10];
                    String user_t = params[11];
                    String user_u = params[12];
                    String user_vv = params[13];
                    String user_w = params[14];
                    String user_x = params[15];
                    String user_y = params[16];
                    String user_z = params[17];
                    String user_a = params[18];
                    String user_b = params[19];
                    String user_c = params[20];
                    String user_d = params[21];
                    String user_vst = params[22];
                    String user_date = params[23];
                    String user_ph = params[24];
                    String user_add = params[25];
                    String user_clnt = params[26];
                    String admin_id = params[27];
                    String remarks = params[28];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                            +URLEncoder.encode("user_age","UTF-8")+"="+URLEncoder.encode(user_age,"UTF-8")+"&"
                            +URLEncoder.encode("user_cc","UTF-8")+"="+URLEncoder.encode(user_cc,"UTF-8")+"&"
                            +URLEncoder.encode("user_diag","UTF-8")+"="+URLEncoder.encode(user_diag,"UTF-8")+"&"
                            +URLEncoder.encode("user_ta","UTF-8")+"="+URLEncoder.encode(user_ta,"UTF-8")+"&"
                            +URLEncoder.encode("user_td","UTF-8")+"="+URLEncoder.encode(user_td,"UTF-8")+"&"
                            +URLEncoder.encode("user_amt","UTF-8")+"="+URLEncoder.encode(user_amt,"UTF-8")+"&"
                            +URLEncoder.encode("user_rcll","UTF-8")+"="+URLEncoder.encode(user_rcll,"UTF-8")+"&"
                            +URLEncoder.encode("user_s","UTF-8")+"="+URLEncoder.encode(user_s,"UTF-8")+"&"
                            +URLEncoder.encode("user_t","UTF-8")+"="+URLEncoder.encode(user_t,"UTF-8")+"&"
                            +URLEncoder.encode("user_u","UTF-8")+"="+URLEncoder.encode(user_u,"UTF-8")+"&"
                            +URLEncoder.encode("user_v","UTF-8")+"="+URLEncoder.encode(user_vv,"UTF-8")+"&"
                            +URLEncoder.encode("user_w","UTF-8")+"="+URLEncoder.encode(user_w,"UTF-8")+"&"
                            +URLEncoder.encode("user_x","UTF-8")+"="+URLEncoder.encode(user_x,"UTF-8")+"&"
                            +URLEncoder.encode("user_y","UTF-8")+"="+URLEncoder.encode(user_y,"UTF-8")+"&"
                            +URLEncoder.encode("user_z","UTF-8")+"="+URLEncoder.encode(user_z,"UTF-8")+"&"
                            +URLEncoder.encode("user_a","UTF-8")+"="+URLEncoder.encode(user_a,"UTF-8")+"&"
                            +URLEncoder.encode("user_b","UTF-8")+"="+URLEncoder.encode(user_b,"UTF-8")+"&"
                            +URLEncoder.encode("user_c","UTF-8")+"="+URLEncoder.encode(user_c,"UTF-8")+"&"
                            +URLEncoder.encode("user_d","UTF-8")+"="+URLEncoder.encode(user_d,"UTF-8")+"&"
                            +URLEncoder.encode("user_visit","UTF-8")+"="+URLEncoder.encode(user_vst,"UTF-8")+"&"
                            +URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(user_date,"UTF-8")+"&"
                            +URLEncoder.encode("user_ph","UTF-8")+"="+URLEncoder.encode(user_ph,"UTF-8")+"&"
                            +URLEncoder.encode("user_add","UTF-8")+"="+URLEncoder.encode(user_add,"UTF-8")+"&"
                            +URLEncoder.encode("user_clnt","UTF-8")+"="+URLEncoder.encode(user_clnt,"UTF-8")+"&"
                            +URLEncoder.encode("admin_id","UTF-8")+"="+URLEncoder.encode(admin_id,"UTF-8")+"&"
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
                al.setMessage("Patient Added Successfully. Insert Images of Patient");
                al.setButton(DialogInterface.BUTTON_NEUTRAL, "INSERT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(ClntCustRegistration.this,ClntCustImageRegistration.class);
                        intent.putExtra("id",id);
                        intent.putExtra("visit",vst);
                        intent.putExtra("allid",aid);
                        intent.putExtra("oid",oid);
                        startActivity(intent);
                        finish();
                    }
                });
                al.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent in = new Intent(ClntCustRegistration.this,AdminDashboard.class);
                        in.putExtra("allid",aid);
                        in.putExtra("oid",oid);
                        startActivity(in);
                        finish();
                    }
                });
                al.show();
            } else {
                al.setMessage("Registration Failed");
                al.show();
                tv1.setText(id);
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et5.setText("");
                tv3.setText("N/A");
                tv8.setText("N/A");
                et7.setText("");
                et8.setText("");
                et9.setText("");
                et10.setText("");
                et11.setText("");
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
            Intent intent = new Intent(ClntCustRegistration.this,AdminDashboard.class);
            intent.putExtra("allid",aid);
            intent.putExtra("oid",oid);
            startActivity(intent);
            finish();
        }
}
