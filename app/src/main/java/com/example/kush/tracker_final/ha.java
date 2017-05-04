package com.example.kush.tracker_final;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ha extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ha);

//        RelativeLayout r =(RelativeLayout)findViewById(R.id.rl);
//        r.setVisibility(View.INVISIBLE);
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

        ImageView iv =(ImageView)findViewById(R.id.tom);
        if(iv.getTag().toString().equals("bell")){
            iv.setTag("ring");
            iv.setImageResource(R.drawable.ring);
        }else{
            iv.setTag("bell");
            iv.setImageResource(R.drawable.bell);
        }
        new task().execute();
    }

    public void tu (View v){
        Intent i = new Intent(ha.this,MapsActivity.class);
        i.putExtra("secret", hs.secret);
        startActivity(i);
    }

    public void sm (View v){

//        RelativeLayout r =(RelativeLayout)findViewById(R.id.rl);
//        r.setVisibility(View.VISIBLE);


        final AlertDialog.Builder builder = new AlertDialog.Builder(ha.this);
        builder.setMessage("   send the coded messasge")
                .setTitle("       NEVER LOSE");
        final TextInputEditText et = (TextInputEditText) new TextInputEditText(ha.this);
        builder.setView(et);
        builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String s = et.getText().toString();
                send(s);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void send (String s){
//        EditText e = (EditText)findViewById(R.id.mt);

        try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(String.valueOf(var.num), null,s, null, null);
                Toast.makeText(getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();


            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
//
//        RelativeLayout r =(RelativeLayout)findViewById(R.id.rl);
//        r.setVisibility(View.INVISIBLE);
    }

    public class lockclass extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            Map<String,String> m = new HashMap<>();
            m.put("secret",hs.secret);
            JSONObject jsonObject = new JSONObject(m);
            String url;
            url = Network.BASE_URL_STRING + "lock";
            Response res = Network.postRequest(getApplicationContext(),jsonObject.toString(),url);
            if(res == null){
                Log.i("res", "doInBackground: recieved null ");
            }else{
                Log.i("res", "recieved ookay" );
            }

            return null;
        }
    }

    public void lock(View v){

        new lockclass().execute();

    }


    public void logout(View v){
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.kush.tracker_final", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("secret","1").apply();
        Intent i = new Intent(this , Welcome.class);
        startActivity(i);

    }



}
