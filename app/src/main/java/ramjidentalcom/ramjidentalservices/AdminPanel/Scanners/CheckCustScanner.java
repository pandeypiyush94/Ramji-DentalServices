package ramjidentalcom.ramjidentalservices.AdminPanel.Scanners;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

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
import ramjidentalcom.ramjidentalservices.AdminPanel.Registrations.ClntCustUpdation;
import ramjidentalcom.ramjidentalservices.AdminPanel.Registrations.PatientUpdation;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.R;

public class CheckCustScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

        ZXingScannerView scannerView;
    AlertDialog al;
    String aid="",oid="",id="",clnt="",ex="";
    PatientBackground pb;
    ClntCustBackground ccb;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            scannerView = new ZXingScannerView(this);
            setContentView(scannerView);
            al = new AlertDialog.Builder(CheckCustScanner.this).create();
            al.setTitle("Registration Status");
            Intent intent = getIntent();
            aid = intent.getStringExtra("allid");
            oid = intent.getStringExtra("oid");
            pb = new PatientBackground();
            ccb = new ClntCustBackground();
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }

        @Override
        protected void onPause() {
            super.onPause();
            scannerView.stopCamera();
        }
    class ClntCustBackground extends AsyncTask<String, Void, String> {
        Context context;
        @Override
        protected String doInBackground(String... params) {
            String json_url = "";
            json_url = "http://ramjidental.com/RamjiApp/fetchClntCustProfile.php";

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
            if(result.equals("successclntcust")){
                altclntcust();
            } else {
                gotclntcust();
            }
        }
    }
    private void altclntcust(){
        al.setMessage("Id is not Registered");
        al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(CheckCustScanner.this, AdminDashboard.class);
                intent.putExtra("allid",aid);
                intent.putExtra("oid",oid);
                startActivity(intent);
                finish();
                startActivity(intent);
                finish();
            }
        });
        al.show();
    }
    private void gotclntcust(){
        Intent intent = new Intent(CheckCustScanner.this, ClntCustUpdation.class);
        intent.putExtra("id", id);
        intent.putExtra("clnt", clnt);
        intent.putExtra("allid", aid);
        intent.putExtra("oid", oid);
        startActivity(intent);
        finish();
    }

    class PatientBackground extends AsyncTask<String, Void, String> {
        Context context;
        @Override
        protected String doInBackground(String... params) {
            String json_url = "";
            json_url = "http://ramjidental.com/RamjiApp/fetchCustProfile.php";

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
            if(result.equals("successcust")){
                altcust();
            } else {
                gotcust();
            }
        }
    }
    private void altcust(){
        al.setMessage("Id is not Registered");
        al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(CheckCustScanner.this, AdminDashboard.class);
                intent.putExtra("allid",aid);
                intent.putExtra("oid",oid);
                startActivity(intent);
                finish();
                startActivity(intent);
                finish();
            }
        });
        al.show();
    }
    private void gotcust(){
        Intent intent = new Intent(CheckCustScanner.this, PatientUpdation.class);
        intent.putExtra("id", ex);
        intent.putExtra("allid", aid);
        intent.putExtra("oid", oid);
        startActivity(intent);
        finish();
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main,menu);
            return true;
        }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CheckCustScanner.this, AdminDashboard.class);
        intent.putExtra("allid",aid);
        intent.putExtra("oid",oid);
        startActivity(intent);
        finish();
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.logout:
                    Intent intent;
                    intent = new Intent(CheckCustScanner.this, HomePage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void handleResult(Result result) {
             ex = result.getText();
            if (ex.matches("(.*)cust(.*)")) {
                if (ex.contains(":")) {
                    String[] s = ex.split(":");
                    id = s[0];
                    clnt = s[1];
                    ccb.execute(id);
                } else {
                    pb.execute(ex);
                }
            } else {
                al.setMessage("This is not a Patient Card");
                al.setButton(DialogInterface.BUTTON_NEUTRAL, "CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(CheckCustScanner.this, AdminDashboard.class);
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
