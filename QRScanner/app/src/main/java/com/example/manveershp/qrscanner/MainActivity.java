package com.example.manveershp.qrscanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button scan;
    private TextView name1, name2;
    private IntentIntegrator qrScan;
    ProgressDialog pdialog;
    HashMap<String,Object> params = new HashMap<>();
    JSONObject jobj = null;
    Connection jParser = new Connection();
    TextView status,students,adults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan = (Button) findViewById(R.id.qrscan);
        name1 = (TextView) findViewById(R.id.name1);
        //name2 = (TextView) findViewById(R.id.name2);
        students = (TextView)findViewById(R.id.students);
        adults = (TextView)findViewById(R.id.adults);
        status = (TextView)findViewById(R.id.status);

        qrScan = new IntentIntegrator(this);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the da`ta to json
                    //JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    //name1.setText(obj.getString("name"));
                    //name2.setText(obj.getString("address"));
                    name1.setText(result.getContents());
                    String qrText = result.getContents();
                    params.put("id",qrText);
                    params.put("monument","Amer Fort");
                    new verifyDetails().execute();


                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }










    class verifyDetails extends AsyncTask<String,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(getApplicationContext());
            pdialog.setMessage("Fetch Details");
            pdialog.setIndeterminate(false);
            pdialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pdialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Log.d("Final Response",jobj.toString());
                        status.setText(jobj.get("result").toString());
                        students.setText(jobj.get("student_count").toString());
                        adults.setText(jobj.get("adult_count").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }

        @Override
        protected Void doInBackground(String... args) {

            try {
                Log.d("alert","inside background");
                jobj = jParser.makeHttpRequest("http://10.42.0.1:3000/verify","POST",params);
                Log.d("json", String.valueOf(jobj));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }




}
