package ramjidentalcom.ramjidentalservices.AdminPanel.Scanners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

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

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import ramjidentalcom.ramjidentalservices.AdminPanel.AdminDashboard;
import ramjidentalcom.ramjidentalservices.AdminPanel.Registrations.PatientRegistration;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.R;

public class PatientScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    AlertDialog al;
    String[] uid;
    String jsonp;
    ZXingScannerView scannerView;
    FetchInBackground fb;
    String ex = "";
    String aid="",oid="",id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        al = new AlertDialog.Builder(PatientScanner.this).create();
        al.setTitle("Registration Status");
        Intent intent = getIntent();
        aid = intent.getStringExtra("allid");
        oid = intent.getStringExtra("oid");
        //Toast.makeText(getApplicationContext(),aid, Toast.LENGTH_SHORT).show();
        fb = new FetchInBackground();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientScanner.this, AdminDashboard.class);
        intent.putExtra("allid",aid);
        intent.putExtra("oid",oid);
        startActivity(intent);
        finish();
    }
    protected void showData(){
        try {
            JSONObject firstObject = new JSONObject(jsonp);
            JSONArray firstArray = firstObject.getJSONArray("custid");
            uid = new String[firstArray.length()];
            for (int i = 0; i < firstArray.length(); i++) {
                JSONObject lastObject = firstArray.getJSONObject(i);
                uid[i] = lastObject.getString("uid");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class FetchInBackground extends AsyncTask<String, Void, String> {
        Context context;
        @Override
        protected String doInBackground(String... params) {
            String json_url = "http://ramjidental.com/RamjiApp/checkcustid.php";
            String result = null;
            try {
                String user_id = params[0];
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
            if(result.equals("success")){
                got();
            } else {
                alt();
            }
        }
    }
    private void alt(){
        al.setMessage("Id is already Registered");
        al.setButton(DialogInterface.BUTTON_NEUTRAL, "CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(PatientScanner.this, AdminDashboard.class);
                intent.putExtra("allid",aid);
                intent.putExtra("oid",oid);
                startActivity(intent);
                finish();
            }
        });
        al.show();
    }
    private void got(){
        Intent intent = new Intent(PatientScanner.this, PatientRegistration.class);
        intent.putExtra("id", ex);
        intent.putExtra("allid",aid);
        intent.putExtra("oid",oid);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
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
                intent = new Intent(PatientScanner.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleResult(Result result) {
        ex = result.getText();
        if((ex.matches("(.*)cust(.*)"))&&(ex.matches("(.*)0(.*)"))) {
            if (ex.contains(":")) {
                String[] s = ex.split(":");
                id = s[0];
                String clnt = s[1];
                fb.execute(id);
            } else {
                fb.execute(ex);
            }
        } else {
            al.setMessage("This is not a Patient Card");
            al.setButton(DialogInterface.BUTTON_NEUTRAL, "CLOSE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(PatientScanner.this, AdminDashboard.class);
                    intent.putExtra("allid",aid);
                    intent.putExtra("oid",oid);
                    startActivity(intent);
                    finish();
                }
            });
            al.show();
        }
    }
    }
