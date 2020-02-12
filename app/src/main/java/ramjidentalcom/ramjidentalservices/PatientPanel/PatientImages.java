package ramjidentalcom.ramjidentalservices.PatientPanel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;

import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.GetImage;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;

public class PatientImages extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isConnected;
    String id = "",visit = "",oid = "";
    ArrayList<HashMap<String, String>> personList;
    ImageView i1,i2,i3,i4;
    ListView lv;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_images);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


         i1 = findViewById(R.id.patimg1);
         i2 = findViewById(R.id.patimg2);
         i3 = findViewById(R.id.patimg3);
         i4 = findViewById(R.id.patimg4);

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

        lv = findViewById(R.id.patimglistview);
        Intent intent = getIntent();
        id=intent.getStringExtra("id");
        visit =intent.getStringExtra("visit");
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

    class FetchInBackground extends AsyncTask<String, Void, List<GetImage>> {

        @Override
        protected List<GetImage> doInBackground(String... params) {
            String json_url = "";
            if (id.matches("(.*)0(.*)")) {
                json_url = "http://ramjidental.com/RamjiApp/fetchimage.php";
            } else if(id.matches("(.*)clnt(.*)")){
                json_url = "http://ramjidental.com/RamjiApp/fetchimages.php";
            }
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
                    return getImages;
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
            progressDialog=ProgressDialog.show(PatientImages.this,"Downloading Images",null,true,true);


        }

        @Override
        protected void onPostExecute(List<GetImage> result) {
                super.onPostExecute(result);



                ImageAdapter adapter = new ImageAdapter(getApplicationContext(),R.layout.listpatientimageview,result);
                lv.setAdapter(adapter);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            },3000);
        }
    }
    public class ImageAdapter extends ArrayAdapter{

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
                convertView = inflater.inflate(R.layout.listpatientimageview,null);
            }
            ImageView im1,im2,im3,im4;
            im1 = convertView.findViewById(R.id.patimg1);
            im2 = convertView.findViewById(R.id.patimg2);
            im3 = convertView.findViewById(R.id.patimg3);
            im4 = convertView.findViewById(R.id.patimg4);

            ImageLoader.getInstance().displayImage(img.get(position).getPreop(), im1);
            ImageLoader.getInstance().displayImage(img.get(position).getPostop(),im2);
            ImageLoader.getInstance().displayImage(img.get(position).getXray1(),im3);
            ImageLoader.getInstance().displayImage(img.get(position).getXray2(),im4);
            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientImages.this,PatientDashboard.class);
        intent.putExtra("id",id);
        intent.putExtra("vst",visit);
        intent.putExtra("oid",oid);
        startActivity(intent);
        finish();
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
                Intent intent = new Intent(PatientImages.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
