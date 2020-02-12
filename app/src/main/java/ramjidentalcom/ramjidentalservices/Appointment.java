package ramjidentalcom.ramjidentalservices;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.Locale;

public class Appointment extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;

    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener time;
    Calendar calendar;
    EditText et1,et2,et3;
    TextView datetext,timetext;
    Button bt;
    String name,cc,ph,dt,tm;
    GetLogin getLogin = new GetLogin(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        datetext = findViewById(R.id.date_text);
        timetext = findViewById(R.id.time_text);
        et1 = findViewById(R.id.aptname);
        et2 = findViewById(R.id.aptcc);
        et3 = findViewById(R.id.aptmob);
        bt = findViewById(R.id.appt);
        calendar = Calendar.getInstance();
        checkConnection();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateDate();
            }
        };
        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);

            }
        };
        timetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Appointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timetext.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Appointment.this, date,
                        calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  checkConnection();
                name = et1.getText().toString();
                cc = et2.getText().toString();
                ph = et3.getText().toString();
                dt = datetext.getText().toString();
                tm = timetext.getText().toString();

//                Log.e("getSelected", s);
                if(isConnected){
                    String type="login";
                    getLogin.execute(type,name,cc,dt,tm,ph);
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
            String path = "http://ramjidental.com/RamjiApp/appointments.php";
            if(type.equals("login")){
                try {
                    String user_name = params[1];
                    String user_cc = params[2];
                    String user_dt = params[3];
                    String user_tm = params[4];
                    String user_ph = params[5];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                            +URLEncoder.encode("user_cc","UTF-8")+"="+URLEncoder.encode(user_cc,"UTF-8")+"&"
                            +URLEncoder.encode("user_date","UTF-8")+"="+URLEncoder.encode(user_dt,"UTF-8")+"&"
                            +URLEncoder.encode("user_time","UTF-8")+"="+URLEncoder.encode(user_tm,"UTF-8")+"&"
                            +URLEncoder.encode("user_phone","UTF-8")+"="+URLEncoder.encode(user_ph,"UTF-8");
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
                al.setMessage("Appointment has been Created");
                al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Appointment.this,HomePage.class);
                        startActivity(intent);
                        finish();
                    }
                });
                al.show();
            } else {
                al.setMessage("Appointment Creation Failed");
                al.show();
                et1.setText("");
                et2.setText("");
                et3.setText("");
                datetext.setText("");
                timetext.setText("");
            }
        }

        @Override
        protected void onPreExecute() {
            al = new AlertDialog.Builder(context).create();
            al.setTitle("Appointment Status");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    private void updateDate(){
        String s = "dd/MM/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(s, Locale.ENGLISH);
        datetext.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Appointment.this,HomePage.class));
        finish();
    }
}
