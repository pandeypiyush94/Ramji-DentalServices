package ramjidentalcom.ramjidentalservices.EmployeePanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import ramjidentalcom.ramjidentalservices.HomePage;
import ramjidentalcom.ramjidentalservices.R;

public class Scanner_Patient extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    String id1 = "",oid="",eid="";
    ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        scannerView.setResultHandler(this);
        scannerView.startCamera();

        Intent intent = getIntent();
        id1 = intent.getStringExtra("id");
        oid = intent.getStringExtra("iid");
        eid = intent.getStringExtra("allid");
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        String ex = result.getText();
        if(ex.contains(":")) {
            String[] s = ex.split(":");
            String id = s[0];
            String clnt = s[1];
            Intent intent = new Intent(Scanner_Patient.this, AddPatient.class);
            intent.putExtra("custid", id);
            intent.putExtra("id",id1);
            intent.putExtra("allid",eid);
            intent.putExtra("iid",oid);
            //intent.putExtra("clnt", clnt);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Scanner_Patient.this, AddPatient.class);
            intent.putExtra("custid", ex);
            intent.putExtra("id",id1);
            intent.putExtra("allid",eid);
            intent.putExtra("iid",oid);
            startActivity(intent);
            finish();
        }
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
                intent = new Intent(Scanner_Patient.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Scanner_Patient.this,EmployeeDashboard.class);
        intent.putExtra("allid",eid);
        intent.putExtra("iid",oid);
        startActivity(intent);
        finish();
    }
}
