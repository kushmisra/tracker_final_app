package com.example.kush.tracker_final;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class Welcome extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    int ACTION = 0;
    int NOT_DECIDED = 0;
    int USER_FOUND = 1;
    int NO_USER = 2;
    int NO_CONNECTION = 3;
    String secret = "";
    String USER_NAME = "";
    boolean pass = true;
    boolean once = false;

    public class task extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if ( activeNetworkInfo != null && activeNetworkInfo.isConnected()){
                if(!sharedPreferences.contains("secret")){
                    ACTION = NO_USER;
                }else{
                    secret = sharedPreferences.getString("secret","");
                    Map<String,String> m = new HashMap<>();
                    m.put("secret",secret);
                    JSONObject jsonObject = new JSONObject(m);
                    String url = Network.BASE_URL_STRING+"check";
                    Response res = Network.postRequest(getApplicationContext(),jsonObject.toString(),url);
                    if(res == null){
                        Log.i("res", "doInBackground: recieved null ");
                        ACTION = NOT_DECIDED;
                    }else{
                        Log.i("res", "doInBackground: "+res.toString());
                        ResponseBody jsonData = res.body();
                        try {
                            JSONObject Jobject = new JSONObject(jsonData.string());
                            String status = Jobject.getString("status");
                            if(status.equals("200")){
                                JSONObject Jo = Jobject.getJSONObject("data");
                                USER_NAME = Jo.getString("USER_NAME");
                                sharedPreferences.edit().putString("USER_NAME",USER_NAME).apply();
                                ACTION = USER_FOUND;
                            }else{
                                ACTION = NO_USER;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                ACTION = NO_CONNECTION;
            }
            return null;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Welcome.this, "welcome user", Toast.LENGTH_SHORT).show();
            pass = true;





        }else{
            this.finish();
        }
        pass = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        setContentView(R.layout.activity_welcome);
        sharedPreferences = this.getSharedPreferences("com.example.kush.tracker_final", Context.MODE_PRIVATE);
        final TextView tv = (TextView)findViewById(R.id.textView);
        final Animation a1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.welcome_animation);
        final Animation a2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.welcome2_animation);
        tv.setAnimation(a1);
//        tv.startAnimation(a1);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this,Manifest.permission.BIND_DEVICE_ADMIN)!= PackageManager.PERMISSION_GRANTED){
            pass = false;
            ActivityCompat.requestPermissions(Welcome.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET
            ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS,Manifest.permission.BIND_DEVICE_ADMIN},1);
        }
        a1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(!once) {
                    new task().execute();
                    once = true;
                }
                Log.i("res", "onAnimationStart: "+String.valueOf(ACTION));

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (ACTION == NOT_DECIDED){
                    tv.startAnimation(a2);
                }else if(ACTION == USER_FOUND){
                    Intent i = new Intent(getApplicationContext(),home.class);
                    if(pass) {
                        startActivity(i);
                    }else{
                        tv.startAnimation(a2);
                    }
                }else if(ACTION == NO_USER){
                    Intent i = new Intent(getApplicationContext(),beforelogin.class);
                    if(pass){
                        startActivity(i);
                    }else{
                        tv.startAnimation(a2);
                    }
                }else if(ACTION == NO_CONNECTION){
                    Toast.makeText(Welcome.this, "no connection", Toast.LENGTH_SHORT).show();
                }


            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        a2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(!once) {
                    new task().execute();
                    once = true;
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (ACTION == NOT_DECIDED){
                    tv.startAnimation(a1);
                }else if(ACTION == USER_FOUND){
                    Intent i = new Intent(getApplicationContext(),home.class);
                    if(pass) {
                        startActivity(i);
                    }else{
                        tv.startAnimation(a1);
                    }
                }else if(ACTION == NO_USER){
                    Intent i = new Intent(getApplicationContext(),beforelogin.class);
                     if(pass){
                        startActivity(i);
                     }else{
                         tv.startAnimation(a1);
                     }

                }else if(ACTION == NO_CONNECTION){
                    Toast.makeText(Welcome.this, "no connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }




}
