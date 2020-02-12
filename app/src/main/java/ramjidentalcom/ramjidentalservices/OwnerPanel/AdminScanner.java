package ramjidentalcom.ramjidentalservices.OwnerPanel;

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

public class AdminScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    String jsonp;
    ZXingScannerView scannerView;
    AlertDialog al;
    String[] uid = null;
    String[] s;
    String id,dept;
    String ex="";
    FetchInBackground fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
       // Programmatically initialize the scanner view
        setContentView(scannerView);
        al = new AlertDialog.Builder(AdminScanner.this).create();
        al.setTitle("Registration Status");
        fb = new FetchInBackground();
        scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        scannerView.startCamera();
    }
    class FetchInBackground extends AsyncTask<String, Void, String> {
        Context context;
        @Override
        protected String doInBackground(String... params) {
            String json_url = "http://ramjidental.com/RamjiApp/checkadminid.php";
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



    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();   // Stop camera on pause
    }
public void alt(){
    al.setMessage("Id is already Registered");
    al.setButton(DialogInterface.BUTTON_NEUTRAL, "CLOSE", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            startActivity(new Intent(AdminScanner.this, OwnerDashboard.class));
            finish();
        }
    });
    al.show();
}
    public void got(){
        Intent intent = new Intent(AdminScanner.this, AddAdmin.class);
        intent.putExtra("id", id);
        intent.putExtra("dept", dept);
        startActivity(intent);
        finish();
    }
    @Override
    public void handleResult(Result result) {
        String ex = result.getText();
        if(ex.matches("(.*)admin(.*)")) {
            if (ex.contains(":")) {
                String[] s = ex.split(":");
                id = s[0];
               dept = s[1];
                fb.execute(id);
            } else {
                fb.execute(ex);
            }
        } else {
            al.setMessage("This is not an Admin Card");
            al.setButton(DialogInterface.BUTTON_NEUTRAL, "CLOSE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(AdminScanner.this, OwnerDashboard.class);
                    startActivity(intent);
                    finish();
                }
            });
            al.show();
        }
            }
}
