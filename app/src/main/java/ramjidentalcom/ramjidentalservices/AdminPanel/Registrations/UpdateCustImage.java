package ramjidentalcom.ramjidentalservices.AdminPanel.Registrations;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ramjidentalcom.ramjidentalservices.AdminPanel.AdminDashboard;
import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.GetImage;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;
import ramjidentalcom.ramjidentalservices.RequestHandler;

public class UpdateCustImage extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
    String id = "";
    private int RESULT_LOAD = 1;
    Bitmap bitmap;
    String visit = "",v2="";
    String ima = "";
    String imb = "";
    String imc = "";
    String imd = "";
    Button bt1;
    String aid = "";
    String oid = "";
    String ii1,ii2,ii3,ii4;
    ImageView currentImageView = null;
    ArrayList<HashMap<String, String>> personList;
    ImageView i1,i2,i3,i4;
    ListView lv;
    AlertDialog al;
    UploadImage1 u11,u12;
    UploadImage2 u21,u22;
    UploadImage3 u31,u32;
    UploadImage4 u41,u42;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_cust_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        u11 = new UploadImage1(this);
        u12 = new UploadImage1(this);
        u21 = new UploadImage2(this);
        u22 = new UploadImage2(this);
        u31 = new UploadImage3(this);
        u32 = new UploadImage3(this);
        u41 = new UploadImage4(this);
        u42 = new UploadImage4(this);

        i1 = findViewById(R.id.patimg1);
        i2 = findViewById(R.id.patimg2);
        i3 = findViewById(R.id.patimg3);
        i4 = findViewById(R.id.patimg4);

        al = new AlertDialog.Builder(UpdateCustImage.this).create();
        al.setTitle("Registration Status");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.add)
                .showImageOnLoading(R.drawable.load)
                .showImageOnFail(R.drawable.admin)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        lv = findViewById(R.id.patimgupdtlistview);
        Intent intent = getIntent();
        id=intent.getStringExtra("id");
        visit = intent.getStringExtra("visit");
        v2 = intent.getStringExtra("visitt");
        aid = intent.getStringExtra("allid");
        oid = intent.getStringExtra("oid");
        personList = new ArrayList<HashMap<String,String>>();

        checkConnection();
        if(isConnected){
            FetchInBackground fb = new FetchInBackground();
            fb.execute(id,visit);
        }
        else{
            checkConnection();
            if(isConnected)
                Toast.makeText(getApplicationContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();

        }
    }
        class UploadImage1 extends AsyncTask<String,Void,String> {
            Context context;
            AlertDialog al;
            UploadImage1(Context ctx){
                context=ctx;
            }
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null,true,true);
                al = new AlertDialog.Builder(context).create();
                al.setTitle("Registration Status");
            }

            @Override
            protected void onPostExecute(String s) {

                loading.dismiss();
                if(s.equals("success")|| s.equals("success2")){
                    al.setMessage("Image Has been Updated. Do you want to update more?");
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
                            Intent intent = new Intent(UpdateCustImage.this,AdminDashboard.class);
                            intent.putExtra("allid",aid);
                            intent.putExtra("oid",oid);
                            startActivity(intent);
                            finish();
                        }
                    });
                    al.show();
                } else
                    Toast.makeText(getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();
            }
            @Override
            protected String doInBackground(String... params) {

                String path = "http://ramjidental.com/RamjiApp/insertcustimage.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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
        class UploadImage2 extends AsyncTask<String,Void,String> {
     Context context;
     AlertDialog al;

     UploadImage2(Context ctx) {
         context = ctx;
     }

     ProgressDialog loading;
     RequestHandler rh = new RequestHandler();

     @Override
     protected void onPreExecute() {
         super.onPreExecute();
         loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
         al = new AlertDialog.Builder(context).create();
         al.setTitle("Registration Status");
     }

     @Override
     protected void onPostExecute(String s) {

         loading.dismiss();
         if (s.equals("success") || s.equals("success2")) {
             al.setMessage("Image Has been Updated. Do you want to update more?");
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
                     Intent intent = new Intent(UpdateCustImage.this, AdminDashboard.class);
                     intent.putExtra("allid",aid);
                     intent.putExtra("oid",oid);
                     startActivity(intent);
                     finish();
                 }
             });
             al.show();
         } else
             Toast.makeText(getApplicationContext(), "Not Working", Toast.LENGTH_SHORT).show();

         //ft.commit();
     }

     @Override
     protected String doInBackground(String... params) {

         String path = "http://ramjidental.com/RamjiApp/insertcustimage1.php";
         try {
             String bitmap = params[0];
             String user_id = params[1];
             String vist = params[2];

             String vstnew = params[3];

             URL url = new URL(path);
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             conn.setRequestMethod("POST");
             conn.setDoOutput(true);
             conn.setDoInput(true);

             OutputStream outstream = conn.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
             String post_data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(bitmap, "UTF-8") + "&"
                     + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                     + URLEncoder.encode("visit", "UTF-8") + "=" + URLEncoder.encode(vist, "UTF-8") + "&"
                     + URLEncoder.encode("new_visit", "UTF-8") + "=" + URLEncoder.encode(vstnew, "UTF-8");
             writer.write(post_data);
             writer.flush();
             writer.close();
             outstream.close();

             InputStream inputstream = conn.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.ISO_8859_1));
             String result = "";
             String line = "";
             while ((line = reader.readLine()) != null) {
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
        class UploadImage3 extends AsyncTask<String,Void,String> {
            Context context;
            AlertDialog al;
            UploadImage3(Context ctx){
                context=ctx;
            }
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null,true,true);
                al = new AlertDialog.Builder(context).create();
                al.setTitle("Registration Status");
            }

            @Override
            protected void onPostExecute(String s) {

                loading.dismiss();
                if(s.equals("success")|| s.equals("success2")){
                    al.setMessage("Image Has been Updated. Do you want to update more?");
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
                            Intent intent = new Intent(UpdateCustImage.this, AdminDashboard.class);
                            intent.putExtra("allid",aid);
                            intent.putExtra("oid",oid);
                            startActivity(intent);
                            finish();
                        }
                    });
                    al.show();
                } else
                    Toast.makeText(getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();

                //ft.commit();
            }

            @Override
            protected String doInBackground(String... params) {

                String path = "http://ramjidental.com/RamjiApp/insertcustimage3.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist =params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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
        class UploadImage4 extends AsyncTask<String,Void,String> {
            Context context;
            AlertDialog al;
            UploadImage4(Context ctx){
                context=ctx;
            }
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null,true,true);
                al = new AlertDialog.Builder(context).create();
                al.setTitle("Registration Status");
            }

            @Override
            protected void onPostExecute(String s) {

                loading.dismiss();
                if(s.equals("success")|| s.equals("success2")){
                    al.setMessage("Image Has been Updated. Do you want to update more?");
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
                            Intent intent = new Intent(UpdateCustImage.this, AdminDashboard.class);
                            intent.putExtra("allid",aid);
                            intent.putExtra("oid",oid);
                            startActivity(intent);
                            finish();
                        }
                    });
                    al.show();
                } else
                    Toast.makeText(getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();


                //ft.commit();
            }

            @Override
            protected String doInBackground(String... params) {

                String path = "http://ramjidental.com/RamjiApp/insertcustimage4.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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
    class FetchInBackground extends AsyncTask<String, Void, List<GetImage>> {

        @Override
        protected List<GetImage> doInBackground(String... params) {
            String json_url = "";
                json_url = "http://ramjidental.com/RamjiApp/fetchimage.php";
            String result = null;
            try {
                String user_id = params[0];
                String vist = params[1];
                URL url = new URL(json_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStream outstream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8")+"&"
                +URLEncoder.encode("visit", "UTF-8") + "=" + URLEncoder.encode(vist, "UTF-8");
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
                JSONObject firstObject = null;
                firstObject = new JSONObject(result);
                JSONArray firstArray = firstObject.getJSONArray("allimages");
                List<GetImage> getImages = new ArrayList<>();

                for (int i = 0; i < firstArray.length(); i++) {
                    JSONObject lastObject = firstArray.getJSONObject(i);
                    GetImage getImage = new GetImage();
                    getImage.setPreop(lastObject.getString("preop"));
                    getImage.setPostop(lastObject.getString("postop"));
                    getImage.setXray1(lastObject.getString("xray1"));
                    getImage.setXray2(lastObject.getString("xray2"));
                    getImages.add(getImage);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(UpdateCustImage.this,"Downloading Images",null,true,true);


        }

        @Override
        protected void onPostExecute(List<GetImage> result) {
            super.onPostExecute(result);



            ImageAdapter adapter = new ImageAdapter(getApplicationContext(),R.layout.listupdtpatimg,result);
            lv.setAdapter(adapter);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            },3000);
        }
    }
    public class ImageAdapter extends ArrayAdapter {

        private List<GetImage> img;
        private int resource;
        private LayoutInflater inflater;
        public ImageAdapter(Context context, int resource, List<GetImage> objects){
            super(context, resource, objects);
            img = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = inflater.inflate(R.layout.listupdtpatimg,null);
            }
            final ImageView im1,im2,im3,im4;
            TextView tv,t1,t2,t3;
            im1 = convertView.findViewById(R.id.patimg1);
            im2 = convertView.findViewById(R.id.patimg2);
            im3 = convertView.findViewById(R.id.patimg3);
            im4 = convertView.findViewById(R.id.patimg4);
            tv = convertView.findViewById(R.id.tv1);
            t1 = convertView.findViewById(R.id.tv2);
            t2 = convertView.findViewById(R.id.tv3);
            t3 = convertView.findViewById(R.id.tv4);

            ImageLoader.getInstance().displayImage(img.get(position).getPreop(), im1);
            ImageLoader.getInstance().displayImage(img.get(position).getPostop(),im2);
            ImageLoader.getInstance().displayImage(img.get(position).getXray1(),im3);
            ImageLoader.getInstance().displayImage(img.get(position).getXray2(),im4);
            convertView.setTag(im1);
            convertView.getTag();
            if(im1.getDrawable() != null) {
                Bitmap bmp;
                bmp = ((BitmapDrawable) im1.getDrawable()).getBitmap();
                ii1 = getStringImage(bmp);
            }
            if(im2.getDrawable() !=null) {
                Bitmap bmp1;
                bmp1 = ((BitmapDrawable)im2.getDrawable()).getBitmap();
                ii2 = getStringImage(bmp1);
            }

            if(im3.getDrawable() !=null) {
                Bitmap bmp2;
                bmp2 = ((BitmapDrawable)im3.getDrawable()).getBitmap();
                ii3 = getStringImage(bmp2);
            }

            if(im4.getDrawable() !=null) {
                Bitmap bmp3;
                bmp3 = ((BitmapDrawable)im4.getDrawable()).getBitmap();
                ii4 = getStringImage(bmp3);
            }

            im1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentImageView = (ImageView) v;
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD);
                    im1.setTag(currentImageView);

                }
            });
            im2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentImageView = (ImageView) v;
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD);
                    im2.setTag(currentImageView);

                }
            });
            im3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentImageView = (ImageView) v;
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD);
                    im3.setTag(currentImageView);

                }
            });
            im4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentImageView = (ImageView) v;
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD);
                    im4.setTag(currentImageView);

                }
            });
//            final View finalConvertView = convertView;

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bitmap == null) {
                        al.setMessage("First Select Image to Upload");
                        al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        al.show();
                    } else {
                        ima = getStringImage(bitmap);
                        if(imb.equals(ii1))
                        new UploadImage12(UpdateCustImage.this).execute(imb, id, visit, v2);
                        if(imc.equals(ii2))
                        new UploadImage13(UpdateCustImage.this).execute(imc, id, visit, v2);
                        if(imd.equals(ii4))
                        new UploadImage14(UpdateCustImage.this).execute(imd, id, visit, v2);
                        u11.execute(ima,id,visit,v2);
                    }
                }
            });
            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bitmap == null) {
                        al.setMessage("First Select Image to Upload");
                        al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        al.show();
                    } else {
                        imb = getStringImage(bitmap);
                        if(ima.equals(ii1))
                        new UploadImage21(UpdateCustImage.this).execute(ima,id,visit,v2);
                        if(imc.equals(ii2))
                        new UploadImage23(UpdateCustImage.this).execute(imc, id, visit, v2);
                        if(imd.equals(ii4))
                        new UploadImage24(UpdateCustImage.this).execute(imd,id,visit,v2);
                        u21.execute(imb,id,visit,v2);
                    }
                }
            });
            t2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bitmap == null) {
                        al.setMessage("First Select Image to Upload");
                        al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        al.show();
                    } else {
                        imc = getStringImage(bitmap);
                        if(ima.equals(ii1))
                        new UploadImage31(UpdateCustImage.this).execute(ima,id,visit,v2);
                        if(imb.equals(ii2))
                        new UploadImage32(UpdateCustImage.this).execute(imb, id, visit, v2);
                        if(imd.equals(ii4))
                        new UploadImage34(UpdateCustImage.this).execute(imd,id,visit,v2);
                        u31.execute(imc,id,visit,v2);
                    }

                }
            });
            t3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bitmap == null) {
                        al.setMessage("First Select Image to Upload");
                        al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        al.show();
                    } else {
                            imd = getStringImage(bitmap);
                            if(ima.equals(ii1))
                            new UploadImage41(UpdateCustImage.this).execute(ima, id, visit, v2);
                            if(imb.equals(ii2))
                            new UploadImage42(UpdateCustImage.this).execute(imb, id, visit, v2);
                            if(imc.equals(ii3))
                            new UploadImage43(UpdateCustImage.this).execute(imc, id, visit, v2);
                            u41.execute(imd,id,visit,v2);
                }

                }
            });
            return convertView;
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent = new Intent(UpdateCustImage.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    class UploadImage12 extends AsyncTask<String,Void,String>{
          Context context;
          AlertDialog al;
          UploadImage12(Context ctx) {
              context = ctx;
          }
          ProgressDialog loading;
          RequestHandler rh = new RequestHandler();
          @Override
          protected String doInBackground(String... params) {
              String path = "http://ramjidental.com/RamjiApp/insertcustimage1.php";
              try {
                  String bitmap = params[0];
                  String user_id = params[1];
                  String vist = params[2];
                  String vstnew = params[3];

                  URL url = new URL(path);
                  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                  conn.setRequestMethod("POST");
                  conn.setDoOutput(true);
                  conn.setDoInput(true);

                  OutputStream outstream = conn.getOutputStream();
                  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                  String post_data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(bitmap, "UTF-8") + "&"
                          + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                          + URLEncoder.encode("visit", "UTF-8") + "=" + URLEncoder.encode(vist, "UTF-8") + "&"
                          + URLEncoder.encode("new_visit", "UTF-8") + "=" + URLEncoder.encode(vstnew, "UTF-8");
                  writer.write(post_data);
                  writer.flush();
                  writer.close();
                  outstream.close();

                  InputStream inputstream = conn.getInputStream();
                  BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.ISO_8859_1));
                  String result = "";
                  String line = "";
                  while ((line = reader.readLine()) != null) {
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

          @Override
          protected void onPreExecute() {
              super.onPreExecute();
              loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
          }

          @Override
          protected void onPostExecute(String s) {
              super.onPostExecute(s);
          }
      }
    class UploadImage13 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage13(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {

                String path = "http://ramjidental.com/RamjiApp/insertcustimage3.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist =params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
    class UploadImage14 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage14(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {
                String path = "http://ramjidental.com/RamjiApp/insertcustimage4.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }

    class UploadImage21 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage21(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {
                String path = "http://ramjidental.com/RamjiApp/insertcustimage.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
    class UploadImage23 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage23(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {
                String path = "http://ramjidental.com/RamjiApp/insertcustimage3.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist =params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
    class UploadImage24 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage24(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {
                String path = "http://ramjidental.com/RamjiApp/insertcustimage4.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }

    class UploadImage31 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage31(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {
                String path = "http://ramjidental.com/RamjiApp/insertcustimage.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
    class UploadImage32 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage32(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {
                String path = "http://ramjidental.com/RamjiApp/insertcustimage1.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(bitmap, "UTF-8") + "&"
                            + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                            + URLEncoder.encode("visit", "UTF-8") + "=" + URLEncoder.encode(vist, "UTF-8") + "&"
                            + URLEncoder.encode("new_visit", "UTF-8") + "=" + URLEncoder.encode(vstnew, "UTF-8");
                    writer.write(post_data);
                    writer.flush();
                    writer.close();
                    outstream.close();

                    InputStream inputstream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.ISO_8859_1));
                    String result = "";
                    String line = "";
                    while ((line = reader.readLine()) != null) {
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
    class UploadImage34 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage34(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {
                String path = "http://ramjidental.com/RamjiApp/insertcustimage4.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }

    class UploadImage41 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage41(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {
                String path = "http://ramjidental.com/RamjiApp/insertcustimage.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
    class UploadImage42 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage42(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {
                String path = "http://ramjidental.com/RamjiApp/insertcustimage1.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist = params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(bitmap, "UTF-8") + "&"
                            + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                            + URLEncoder.encode("visit", "UTF-8") + "=" + URLEncoder.encode(vist, "UTF-8") + "&"
                            + URLEncoder.encode("new_visit", "UTF-8") + "=" + URLEncoder.encode(vstnew, "UTF-8");
                    writer.write(post_data);
                    writer.flush();
                    writer.close();
                    outstream.close();

                    InputStream inputstream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.ISO_8859_1));
                    String result = "";
                    String line = "";
                    while ((line = reader.readLine()) != null) {
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
    class UploadImage43 extends AsyncTask<String,Void,String>{
        Context context;
        AlertDialog al;
        UploadImage43(Context ctx) {
            context = ctx;
        }
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(String... params) {
                String path = "http://ramjidental.com/RamjiApp/insertcustimage3.php";
                try {
                    String bitmap = params[0];
                    String user_id = params[1];
                    String vist =params[2];
                    String vstnew = params[3];

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    OutputStream outstream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                    String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                            +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("visit","UTF-8")+"="+URLEncoder.encode(vist,"UTF-8")+"&"
                            +URLEncoder.encode("new_visit","UTF-8")+"="+URLEncoder.encode(vstnew,"UTF-8");
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCustImage.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
}
