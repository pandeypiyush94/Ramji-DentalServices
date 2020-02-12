package ramjidentalcom.ramjidentalservices.AdminPanel.Registrations;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import ramjidentalcom.ramjidentalservices.AdminPanel.AdminDashboard;
import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;
import ramjidentalcom.ramjidentalservices.RequestHandler;

public class ClntCustImageRegistration extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
    private int RESULT_LOAD = 1;
    Bitmap bitmap;
    View view;
    String aid = "";
    String oid = "";
    String id = "";
    String ima = "";
    String visit = "";
    Button bt1,bt2,bt3,bt4;
    ImageView iv,iv1,iv2,iv3, currentImageView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clnt_cust_image_registration);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        visit = intent.getStringExtra("visit");
        aid = intent.getStringExtra("allid");
        oid = intent.getStringExtra("oid");
        iv = findViewById(R.id.patimg1);
        iv1 = findViewById(R.id.patimg2);
        iv2 = findViewById(R.id.patimg3);
        iv3 = findViewById(R.id.patimg4);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageView = (ImageView) v;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD);
            }
        });
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageView = (ImageView) v;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageView = (ImageView) v;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageView = (ImageView) v;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD);
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ima = getStringImage(bitmap);
                uploadImage();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ima = getStringImage(bitmap);
                uploadImage1();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ima = getStringImage(bitmap);
                uploadImage2();
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ima = getStringImage(bitmap);
                uploadImage3();
            }
        });
    }
    private void uploadImage(){
        class UploadImage extends AsyncTask<String,Void,String> {
            Context context;
            AlertDialog al;
            UploadImage(Context ctx){
                context=ctx;
            }
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ClntCustImageRegistration.this, "Uploading...", null,true,true);
                al = new AlertDialog.Builder(context).create();
                al.setTitle("Registration Status");
            }

            @Override
            protected void onPostExecute(String s) {

                loading.dismiss();
                if("success".equals(s)) {
                    al.setMessage("Image is Registered Successfully. Want to add more?");
                    al.setButton(DialogInterface.BUTTON_NEUTRAL, "YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    al.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(ClntCustImageRegistration.this, AdminDashboard.class);
                            intent.putExtra("allid",aid);
                            intent.putExtra("oid",oid);
                            startActivity(intent);
                            finish();
                        }
                    });
                    al.show();
                } else
                    Toast.makeText(getApplicationContext(),"Something Went Wrong. Try Again..",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... params) {

                String path = "http://ramjidental.com/RamjiApp/insertclntcustimage.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8");
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
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        UploadImage ui = new UploadImage(this);
        ui.execute(ima,id,visit);
    }
    private void uploadImage1(){
        class UploadImage extends AsyncTask<String,Void,String> {
            Context context;
            AlertDialog al;
            UploadImage(Context ctx){
                context=ctx;
            }
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ClntCustImageRegistration.this, "Uploading...", null,true,true);
                al = new AlertDialog.Builder(context).create();
                al.setTitle("Registration Status");
            }

            @Override
            protected void onPostExecute(String s) {

                loading.dismiss();
                if("success".equals(s)) {
                    al.setMessage("Image is Registered Successfully. Want to add more?");
                    al.setButton(DialogInterface.BUTTON_NEUTRAL, "YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    al.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(ClntCustImageRegistration.this, AdminDashboard.class);
                            intent.putExtra("allid",aid);
                            intent.putExtra("oid",oid);
                            startActivity(intent);
                            finish();
                        }
                    });
                    al.show();
                } else
                    Toast.makeText(getApplicationContext(),"Something Went Wrong. Try Again..",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... params) {

                String path = "http://ramjidental.com/RamjiApp/insertclntcustimage1.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8");
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
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        UploadImage ui = new UploadImage(this);
        ui.execute(ima,id,visit);
    }
    private void uploadImage2(){
        class UploadImage extends AsyncTask<String,Void,String> {
            Context context;
            AlertDialog al;
            UploadImage(Context ctx){
                context=ctx;
            }
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ClntCustImageRegistration.this, "Uploading...", null,true,true);
                al = new AlertDialog.Builder(context).create();
                al.setTitle("Registration Status");
            }

            @Override
            protected void onPostExecute(String s) {

                loading.dismiss();
                if("success".equals(s)) {
                    al.setMessage("Image is Registered Successfully. Want to add more?");
                    al.setButton(DialogInterface.BUTTON_NEUTRAL, "YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    al.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(ClntCustImageRegistration.this, AdminDashboard.class);
                            intent.putExtra("allid",aid);
                            intent.putExtra("oid",oid);
                            startActivity(intent);
                            finish();
                        }
                    });
                    al.show();
                } else
                    Toast.makeText(getApplicationContext(),"Something Went Wrong. Try Again..",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... params) {

                String path = "http://ramjidental.com/RamjiApp/insertclntcustimage3.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8");
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
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        UploadImage ui = new UploadImage(this);
        ui.execute(ima,id,visit);
    }
    private void uploadImage3(){
        class UploadImage extends AsyncTask<String,Void,String> {
            Context context;
            AlertDialog al;
            UploadImage(Context ctx){
                context=ctx;
            }
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ClntCustImageRegistration.this, "Uploading...", null,true,true);
                al = new AlertDialog.Builder(context).create();
                al.setTitle("Registration Status");
            }

            @Override
            protected void onPostExecute(String s) {

                loading.dismiss();
                if("success".equals(s)) {
                    al.setMessage("Image is Registered Successfully. Want to add more?");
                    al.setButton(DialogInterface.BUTTON_NEUTRAL, "YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    al.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(ClntCustImageRegistration.this, AdminDashboard.class);
                            intent.putExtra("allid",aid);
                            intent.putExtra("oid",oid);
                            startActivity(intent);
                            finish();
                        }
                    });
                    al.show();
                } else
                    Toast.makeText(getApplicationContext(),"Something Went Wrong. Try Again..",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... params) {

                String path = "http://ramjidental.com/RamjiApp/insertclntcustimage4.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8");
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
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        UploadImage ui = new UploadImage(this);
        ui.execute(ima,id,visit);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 960, 960, true);
                    currentImageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void checkConnection(){
        isConnected = ConnectivityReceiver.isConnected();
    }
    @Override
    public void onResume() {
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
}
