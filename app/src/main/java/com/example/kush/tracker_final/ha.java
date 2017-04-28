package com.example.kush.tracker_final;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ha);

        RelativeLayout r =(RelativeLayout)findViewById(R.id.rl);
        r.setVisibility(View.INVISIBLE);
    }


    public class task extends AsyncTask<Void,Void,Void>{
        @Override

        protected Void doInBackground(Void... params) {
            Map<String,String> m = new HashMap<>();
            m.put("secret",hs.secret);
            JSONObject jsonObject = new JSONObject(m);
            String url;
            url = Network.BASE_URL_STRING + "music";
            Response res = Network.postRequest(getApplicationContext(),jsonObject.toString(),url);
            if(res == null){
                Log.i("res", "doInBackground: recieved null ");
            }else{
                Log.i("res", "doInBackground: "+res.toString());
                ResponseBody jsonData = res.body();
                try {
                    JSONObject Jobject = new JSONObject(jsonData.string());
                    String status = Jobject.getString("status");
                    if(status.equals("200")) {
                        JSONObject Jo = Jobject.getJSONObject("data");
                        if (Jo.getString("music").equals("true")){

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    public class task2 extends AsyncTask<Void,Void,Void>{
        String battery ="";
        @Override
        protected Void doInBackground(Void... params) {
            Map<String,String> m = new HashMap<>();
            m.put("secret",hs.secret);
            JSONObject jsonObject = new JSONObject(m);
            String url;
            url = Network.BASE_URL_STRING + "battery";
            Response res = Network.postRequest(getApplicationContext(),jsonObject.toString(),url);
            if(res == null){
                Log.i("res", "doInBackground: recieved null ");
            }else{
                Log.i("res", "doInBackground: "+res.toString());
                ResponseBody jsonData = res.body();
                try {
                    JSONObject Jobject = new JSONObject(jsonData.string());
                    String status = Jobject.getString("status");
                    if(status.equals("200")) {
                        JSONObject Jo = Jobject.getJSONObject("data");
                        battery = Jo.getString("battery");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ha.this, battery + "%", Toast.LENGTH_LONG).show();
            super.onPostExecute(aVoid);
        }
    }


    public void  cb(View v){
        new task2().execute();
    }


    public void tom (View v){
        new task().execute();
    }

    public void tu (View v){
        Intent i = new Intent(ha.this,MapsActivity.class);
        i.putExtra("secret", hs.secret);
        startActivity(i);
    }

    public void sm (View v){

        RelativeLayout r =(RelativeLayout)findViewById(R.id.rl);
        r.setVisibility(View.VISIBLE);
    }

    public void send (View v){
        EditText e = (EditText)findViewById(R.id.mt);

        try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(String.valueOf(var.num), null,e.getText().toString(), null, null);
                Toast.makeText(getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();


            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }

        RelativeLayout r =(RelativeLayout)findViewById(R.id.rl);
        r.setVisibility(View.INVISIBLE);
    }

}
