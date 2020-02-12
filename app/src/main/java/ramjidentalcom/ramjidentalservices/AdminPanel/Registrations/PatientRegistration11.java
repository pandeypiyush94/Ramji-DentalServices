package ramjidentalcom.ramjidentalservices.AdminPanel.Registrations;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

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
import java.util.List;

import ramjidentalcom.ramjidentalservices.AdminPanel.AdminDashboard;
import ramjidentalcom.ramjidentalservices.ConnectivityReceiver;
import ramjidentalcom.ramjidentalservices.R;
import ramjidentalcom.ramjidentalservices.ReceiverInitiator;
import ramjidentalcom.ramjidentalservices.RequestHandler;

public class PatientRegistration11 extends AppCompatActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_registration11);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_registration11, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment implements  ConnectivityReceiver.ConnectivityReceiverListener{
        boolean isConnected;
        private int RESULT_LOAD = 1;
        Bitmap bitmap;
        String id = "";
        String ima = "";        Button bt1;
        ImageView iv, currentImageView = null;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_patient_registration11, container, false);
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            iv = rootView.findViewById(R.id.img1);
            bt1 = rootView.findViewById(R.id.bt1);
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
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ima = getStringImage(bitmap);

                    uploadImage();
                }
            });
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
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
                    loading = ProgressDialog.show(PatientRegistration11.this, "Uploading...", null,true,true);
                    al = new AlertDialog.Builder(context).create();
                    al.setTitle("Registration Status");
                }

                @Override
                protected void onPostExecute(String s) {

                    loading.dismiss();
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

                    //ft.commit();
                }

                @Override
                protected String doInBackground(String... params) {

                    String path = "http://ramjidental.com/RamjiApp/insertcustimage.php";
                    try {
                        String bitmap = params[0];
                        String user_id = params[1];

                        URL url = new URL(path);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(false);
                        conn.setDoInput(true);

                        OutputStream outstream = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                        String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                                +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8");
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

            UploadImage ui = new UploadImage(getContext());
            ui.execute(ima,id);
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
                Toast.makeText(getContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();
        }
    }

    public class PlaceholderFragment2 extends Fragment implements  ConnectivityReceiver.ConnectivityReceiverListener{
        boolean isConnected;
        private int RESULT_LOAD = 1;
        Bitmap bitmap;
        String id = "";
        String ima = "";
        Button bt1,bt2,bt3,bt4;
        ImageView iv,img, currentImageView = null,iv1,image31;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment2() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_patient_registration12, container, false);
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            iv = rootView.findViewById(R.id.img2);
            bt1 = rootView.findViewById(R.id.bt2);
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
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ima = getStringImage(bitmap);
                    uploadImage();
                }
            });
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
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
                    loading = ProgressDialog.show(PatientRegistration11.this, "Uploading...", null,true,true);
                    al = new AlertDialog.Builder(context).create();
                    al.setTitle("Registration Status");
                }

                @Override
                protected void onPostExecute(String s) {
                    //super.onPostExecute(s);
                    loading.dismiss();
//                al.setMessage("Image Uploaded Successfully.");
//                al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        bt1.setVisibility(Button.GONE);
//                    }
//                });
//                al.show();
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

                }

                @Override
                protected String doInBackground(String... params) {

                    String path = "http://ramjidental.com/RamjiApp/insertcustimage1.php";
                    try {
                        String bitmap = params[0];
                        String user_id = params[1];

                        URL url = new URL(path);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        OutputStream outstream = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                        String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                                +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8");
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

            UploadImage ui = new UploadImage(getContext());
            ui.execute(ima,id);
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
                Toast.makeText(getContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();
        }
    }

    public class PlaceholderFragment3 extends Fragment implements  ConnectivityReceiver.ConnectivityReceiverListener{
        boolean isConnected;
        private int RESULT_LOAD = 1;
        Bitmap bitmap;
        String id = "";
        String ima = "";
        Button bt1,bt2,bt3,bt4;
        ImageView iv,img, currentImageView = null,iv1,image31;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment3() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_patient_registration13, container, false); Intent intent = getIntent();
            id = intent.getStringExtra("id");
            iv = rootView.findViewById(R.id.img3);
            bt1 = rootView.findViewById(R.id.bt3);
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
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ima = getStringImage(bitmap);
                    uploadImage();
                }
            });
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
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
                    loading = ProgressDialog.show(PatientRegistration11.this, "Uploading...", null,true,true);
                    al = new AlertDialog.Builder(context).create();
                    al.setTitle("Registration Status");
                }

                @Override
                protected void onPostExecute(String s) {
                    //super.onPostExecute(s);
                    loading.dismiss();
//                al.setMessage("Image Uploaded Successfully.");
//                al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        bt1.setVisibility(Button.GONE);
//                    }
//                });
//                al.show();
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                }

                @Override
                protected String doInBackground(String... params) {

                    String path = "http://ramjidental.com/RamjiApp/insertcustimage3.php";
                    try {
                        String bitmap = params[0];
                        String user_id = params[1];

                        URL url = new URL(path);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        OutputStream outstream = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                        String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                                +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8");
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

            UploadImage ui = new UploadImage(getContext());
            ui.execute(ima,id);
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
                Toast.makeText(getContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();
        }
    }

    public class PlaceholderFragment4 extends Fragment implements  ConnectivityReceiver.ConnectivityReceiverListener{
        boolean isConnected;
        private int RESULT_LOAD = 1;
        Bitmap bitmap;
        String id = "";
        String ima = "";
        Button bt1,bt2,bt3,bt4;
        ImageView iv,img, currentImageView = null,iv1,image31;       /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment4() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_patient_registration14, container, false); Intent intent = getIntent();
            id = intent.getStringExtra("id");
            iv = rootView.findViewById(R.id.img4);
            bt1 = rootView.findViewById(R.id.bt4);
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
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ima = getStringImage(bitmap);
                    uploadImage();
                }
            });
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
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
                    loading = ProgressDialog.show(PatientRegistration11.this, "Uploading...", null,true,true);
                    al = new AlertDialog.Builder(context).create();
                    al.setTitle("Registration Status");
                }

                @Override
                protected void onPostExecute(String s) {
                    //super.onPostExecute(s);
                    loading.dismiss();
//                al.setMessage("Image Uploaded Successfully.");
//                al.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        bt1.setVisibility(Button.GONE);
//                    }
//                });
//                al.show();
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PatientRegistration11.this, AdminDashboard.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                protected String doInBackground(String... params) {

                    String path = "http://ramjidental.com/RamjiApp/insertcustimage4.php";
                    try {
                        String bitmap = params[0];
                        String user_id = params[1];

                        URL url = new URL(path);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        OutputStream outstream = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));
                        String post_data = URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(bitmap,"UTF-8")+"&"
                                +URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8");
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

            UploadImage ui = new UploadImage(getContext());
            ui.execute(ima,id);
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
                Toast.makeText(getContext(),"Connected To Internet",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(),"Internet is Not Connected",Toast.LENGTH_LONG).show();
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    private void setViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PlaceholderFragment(),"PREOP");
        adapter.addFragment(new PlaceholderFragment2(),"POSTOP");
        adapter.addFragment(new PlaceholderFragment3(),"X-RAY1");
        adapter.addFragment(new PlaceholderFragment4(),"X-RAY2");
        viewPager.setAdapter(adapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitle = new ArrayList<>();


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            //return 4;
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return fragmentTitle.get(position);
        }
    }
}
