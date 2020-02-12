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

import ramjidentalcom.ramjidentalservices.AdminPanel.AdminDashboard;
import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.MultiSelectionSpinner;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class PatientUpdation extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
    String jsonp;
    String aid="",aid1="",aid2="",oid="";
    ArrayList<HashMap<String, String>> personList;
    EditText et1,et2,et3,et4,et5,et6,et7,et8,et9,et10,et11,et12;
    MultiSelectionSpinner s13,s14,s15,s16;
    String s="",t="",u="",vv="",w="",x="",y="",z="",a="",b="",cccc="",d="";
    Button bt1;
    String vsit = "";
    String id="";
    String uid,name,age,cc,ph,add,diag,ta,td,amt,rcll,vst,dte,rmk;
    GetLogin getLogin = new GetLogin(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_updation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv1 = findViewById(R.id.custid);
        tv2 = findViewById(R.id.custdate);
        et1 = findViewById(R.id.custname);
        et2 = findViewById(R.id.custage);
        et3 = findViewById(R.id.custcc);
        et10 = findViewById(R.id.custphone);
        et11 = findViewById(R.id.custadd);
        et4 = findViewById(R.id.custdiagnosis);
        et5 = findViewById(R.id.custta);
        et6 = findViewById(R.id.custtd);
        et7 = findViewById(R.id.custamount);
        et8 = findViewById(R.id.custrecall);
        et9 = findViewById(R.id.custvisit);
        et12 = findViewById(R.id.custremarks);
        bt1 = findViewById(R.id.custsignup);
        tv3 = findViewById(R.id.dq1);
        tv4 = findViewById(R.id.dq2);
        tv5 = findViewById(R.id.dq3);
        tv6 = findViewById(R.id.dq4);
        tv7 = findViewById(R.id.dq5);
        tv8 = findViewById(R.id.dq6);
        tv9 = findViewById(R.id.dq7);
        tv10 = findViewById(R.id.dq8);

        s13 = findViewById(R.id.mySpinner10);
        s14 = findViewById(R.id.mySpinner11);
        s15 = findViewById(R.id.mySpinner12);
        s16 = findViewById(R.id.mySpinner14);
        checkConnection();
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        String dateString = sdf.format(date);
        tv2.setText(dateString);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        aid = intent.getStringExtra("allid");
        oid = intent.getStringExtra("oid");
        if (aid.contains(":")) {
            String[] s = aid.split(":");
            aid1 = s[0];
            aid2 = s[1];
        }
        tv1.setText(id);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkConnection();
                uid = tv1.getText().toString();
                name = et1.getText().toString();
                age = et2.getText().toString();
                cc = et3.getText().toString();
                ph = et10.getText().toString();
                add = et11.getText().toString();
                diag = et4.getText().toString();
                ta = et5.getText().toString();
                td = et6.getText().toString();
                amt = et7.getText().toString();
                rcll = et8.getText().toString();
                vst = et9.getText().toString();
                rmk = et12.getText().toString();
                s = tv3.getText().toString();
                t = tv4.getText().toString();
                u = tv5.getText().toString();
                vv = tv6.getText().toString();
                w = tv7.getText().toString();
                x = tv8.getText().toString();
                y = tv9.getText().toString();
                z = tv10.getText().toString();
                a = s13.getSelectedItemsAsString();
                b = s14.getSelectedItemsAsString();
                cccc = s15.getSelectedItemsAsString();
                d = s16.getSelectedItemsAsString();
                dte = tv2.getText().toString();
                if(isConnected){
                    String type="login";
                    getLogin.execute(type,uid,name,age,cc,diag,ta,td,amt,rcll,s,t,u,vv,w,x,y,z,a,b,cccc,d,vst,dte,ph,add,aid1,rmk);
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
        if (isConnected) {
            FetchInBackground fb = new FetchInBackground();
            fb.execute(id,aid1);
        } else
            Toast.makeText(getApplicationContext(),"Not Connected to Internet",Toast.LENGTH_LONG).show();

        personList = new ArrayList<HashMap<String,String>>();
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
            JSONArray firstArray = firstObject.getJSONArray("p2");
            String name = "",aid="",ag = "",ph = "",add = "",cc = "",dg = "",tad = "",td = "",amt = "",rcll = "",dq1 = "",dq2 = "",dq3 = "",dq4 = "",
                    taq1 = "",taq2 = "",taq3 = "",taq4 = "",tdq1 = "",tdq2 = "",tdq3 = "",tdq4 = "",date = "";
            for (int i = 0; i < firstArray.length(); i++) {
                JSONObject lastObject = firstArray.getJSONObject(i);
                String id = lastObject.getString("uid");
                name = lastObject.getString("name");
                ag = lastObject.getString("age");
                ph = lastObject.getString("phone");
                add = lastObject.getString("address");
                cc = lastObject.getString("cc");
                dg = lastObject.getString("diagnosis");
                tad = lastObject.getString("ta");
                td = lastObject.getString("td");
                amt = lastObject.getString("amount");
                rcll = lastObject.getString("recall");
                vsit = lastObject.getString("visit");
                dq1 = lastObject.getString("diagnosis_q1");
                dq2 = lastObject.getString("diagnosis_q2");
                dq3 = lastObject.getString("diagnosis_q3");
                dq4 = lastObject.getString("diagnosis_q4");
                taq1 = lastObject.getString("tadvise_q1");
                taq2 = lastObject.getString("tadvise_q2");
                taq3 = lastObject.getString("tadvise_q3");
                taq4 = lastObject.getString("tadvise_q4");
                tdq1 = lastObject.getString("tdone_q1");
                tdq2 = lastObject.getString("tdone_q2");
                tdq3 = lastObject.getString("tdone_q3");
                tdq4 = lastObject.getString("tdone_q4");
                date = lastObject.getString("date");

                HashMap<String, String> admins = new HashMap<String, String>();
                admins.put("name", name);
                admins.put("adminid",aid);
                admins.put("age", ag);
                admins.put("phone",ph);
                admins.put("address",add);
                admins.put("cc", cc);
                admins.put("diagnosis", dg);
                admins.put("ta", tad);
                admins.put("td", td);
                admins.put("amount", amt);
                admins.put("recall", rcll);
                admins.put("visit", vsit);
                admins.put("diagnosis_q1", dq1);
                admins.put("diagnosis_q2", dq2);
                admins.put("diagnosis_q3", dq3);
                admins.put("diagnosis_q4", dq4);
                admins.put("tadvise_q1", taq1);
                admins.put("tadvise_q2", taq2);
                admins.put("tadvise_q3", taq3);
                admins.put("tadvise_q4", taq4);
                personList.add(admins);
            }
            et1.setText(name);
            et2.setText(ag);
            et3.setText(cc);
            et4.setText(dg);
            et5.setText(tad);
            et6.setText(td);
            et7.setText(amt);
            et8.setText(rcll);
            et9.setText(vsit);
            et10.setText(ph);
            et11.setText(add);
            tv3.setText(dq1);
            tv4.setText(dq2);
            tv5.setText(dq3);
            tv6.setText(dq4);
            tv7.setText(taq1);
            tv8.setText(taq2);
            tv9.setText(taq3);
            tv10.setText(taq4);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class FetchInBackground extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String json_url = "http://ramjidental.com/RamjiApp/fetchCust1.php";
                String result = null;
                try {
                    String user_id=params[0];
                    String admin_id=params[1];
                    URL url = new URL(json_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                    +URLEncoder.encode("admin_id","UTF-8")+"="+URLEncoder.encode(admin_id,"UTF-8");
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

    public class GetLogin extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog al;
        GetLogin(Context ctx){
            context=ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String path = "http://ramjidental.com/RamjiApp/updateCust.php";
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
                    String admin_id = params[26];
                    String remarks = params[27];
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
                        Intent intent = new Intent(PatientUpdation.this,UpdateCustImage.class);
                        intent.putExtra("id",id);
                        intent.putExtra("visit",vsit);
                        intent.putExtra("visitt",vst);
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
                        startActivity(new Intent(PatientUpdation.this,AdminDashboard.class));
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
                et6.setText("");
                et7.setText("");
                et8.setText("");
                et9.setText("");
                et10.setText("");
                et11.setText("");
                et12.setText("");
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
        Intent intent = new Intent(PatientUpdation.this,AdminDashboard.class);
        intent.putExtra("allid",aid);
        intent.putExtra("oid",oid);
        startActivity(intent);
        finish();
    }
}
