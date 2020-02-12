package ramjidentalcom.ramjidentalservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

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
import ramjidentalcom.ramjidentalservices.ClientPanel.ClientDashboard;
import ramjidentalcom.ramjidentalservices.EmployeePanel.EmployeeDashboard;
import ramjidentalcom.ramjidentalservices.OwnerPanel.OwnerDashboard;
import ramjidentalcom.ramjidentalservices.PatientPanel.PatDashboard;

public class LoginScanner extends AppCompatActivity  implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    String id = "";
    AlertDialog al;
    FetchInBackground fb;
    AdminBackground ab;
    PatientBackground pb;
    ClientBackground cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(scannerView);
        al = new AlertDialog.Builder(LoginScanner.this).create();
        al.setTitle("Registration Status");
        fb = new FetchInBackground();
        ab = new AdminBackground();
        pb = new PatientBackground();
        cb = new ClientBackground();
        scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        scannerView.startCamera();         // Start camera
    }

    class FetchInBackground extends AsyncTask<String, Void, String> {
        Context context;
        @Override
        protected String doInBackground(String... params) {
            String json_url = "";
            json_url = "http://ramjidental.com/RamjiApp/fetchEmpProfile.php";

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
            if(result.equals("successemp")){
                alt();
            } else {
                gotemp();
            }
        }
    }
    private void alt(){
        al.setMessage("Id is not Registered");
        al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(LoginScanner.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
        al.show();
    }
    private void gotemp(){
        Intent intent = new Intent(LoginScanner.this, EmployeeDashboard.class);
        intent.putExtra("allid", id);
        startActivity(intent);
        finish();
    }

    class AdminBackground extends AsyncTask<String, Void, String> {
        Context context;
        @Override
        protected String doInBackground(String... params) {
            String json_url = "";
            json_url = "http://ramjidental.com/RamjiApp/fetchadmin.php";

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
            if(result.equals("successadmin")){
                altad();
            } else {
                gotad();
            }
        }
    }
    private void altad(){
        al.setMessage("Id is not Registered");
        al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(LoginScanner.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
        al.show();
    }
    private void gotad(){
        Intent intent = new Intent(LoginScanner.this, AdminDashboard.class);
        intent.putExtra("allid", id);
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
                Intent intent = new Intent(LoginScanner.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
        al.show();
    }
    private void gotcust(){
        Intent intent = new Intent(LoginScanner.this, PatDashboard.class);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }

    class ClientBackground extends AsyncTask<String, Void, String> {
        Context context;
        @Override
        protected String doInBackground(String... params) {
            String json_url = "";
            json_url = "http://ramjidental.com/RamjiApp/fetchClntProfile.php";

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
            if(result.equals("successclnt")){
                altclnt();
            } else {
                gotclnt();
            }
        }
    }
    private void altclnt(){
        al.setMessage("Id is not Registered");
        al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(LoginScanner.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
        al.show();
    }
    private void gotclnt(){
        Intent intent = new Intent(LoginScanner.this, ClientDashboard.class);
    intent.putExtra("allid", id);
    startActivity(intent);
    finish();
    }

    class ClientPatientBackground extends AsyncTask<String, Void, String> {
        Context context;
        @Override
        protected String doInBackground(String... params) {
            String json_url = "";
            json_url = "http://ramjidental.com/RamjiApp/fetchClntCustByUID.php";

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
                Intent intent = new Intent(LoginScanner.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
        al.show();
    }
    private void gotclntcust(){
        Intent intent = new Intent(LoginScanner.this, PatDashboard.class);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }


    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();   // Stop camera on pause
    }

    @Override
    public void handleResult(Result result)  {
        id = result.getText();
        if (id.matches("(.*)owner(.*)")) {
            fb.execute(id);
                Intent intent = new Intent(LoginScanner.this, OwnerDashboard.class);
                intent.putExtra("allid", id);
                startActivity(intent);
                finish();
        }
        else if (id.matches("(.*)admin(.*)")) {
            if(id.contains(":")) {
                String[] s = id.split(":");
                String ex = s[0];
                String clnt = s[1];
              ab.execute(ex);
            } else {
                ab.execute(id);
            }
//            Intent intent = new Intent(LoginScanner.this, AdminDashboard.class);
//            intent.putExtra("allid", id);
//            startActivity(intent);
//            finish();
        } else if (id.matches("(.*)emp(.*)")) {
            fb.execute(id);
//            Intent intent = new Intent(LoginScanner.this, EmployeeDashboard.class);
//            intent.putExtra("allid", id);
//            startActivity(intent);
//            finish();
        }
        else if (id.matches("(.*)cust(.*)")) {
            if(id.contains(":")) {
                String[] s = id.split(":");
                String ex = s[0];
                String clnt = s[1];
                pb.execute(ex);
            } else {
                pb.execute(id);

            }
        }else if ((id.matches("(.*)clnt(.*)")) &&(!id.matches("(.*)clntcust(.*)"))) {
            if(id.contains(":")) {
                String[] s = id.split(":");
                String ex = s[0];
                String clnt = s[1];
                cb.execute(ex);
//                Intent intent = new Intent(LoginScanner.this, ClientDashboard.class);
//                intent.putExtra("allid", ex);
//                intent.putExtra("hosp",clnt);
//                startActivity(intent);
//                finish();
            } else {
                cb.execute(id);

            }
        }
    }
}
