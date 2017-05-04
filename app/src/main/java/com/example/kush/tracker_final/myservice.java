package com.example.kush.tracker_final;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONObject;

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by kush on 15/4/17.
 */

public class myservice extends Service {
    String user;
    SharedPreferences sharedPreferences;
    LocationManager locationManager;
    LocationListener locationListener;
    Double longitude = 0.0;
    Double latitude = 0.0;
    int batterylevel;
    boolean playing = false;




//    private static final int ADMIN_INTENT = 15;
//    private static final String description = "Some Description About Your Admin";
//    private DevicePolicyManager mDevicePolicyManager;
//    private ComponentName mComponentName;


    MediaPlayer mediaPlayer;
    public void VolumeUp() {
        playing = true;
        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.m);
        mediaPlayer.start();
    }

    public void stop(){
        playing = false;
        mediaPlayer.stop();
    }


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            batterylevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//        mDevicePolicyManager = (DevicePolicyManager)getSystemService(
//                Context.DEVICE_POLICY_SERVICE);
//        mComponentName = new ComponentName(this, MyAdmin.class);

        sharedPreferences = this.getSharedPreferences("com.example.kush.tracker_final", Context.MODE_PRIVATE);
        user = sharedPreferences.getString("USER_NAME", "");
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.m);
        mediaPlayer.setLooping(true);
        Log.i("res", "onStartCommand: 1");

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Log.i("res", "onStartCommand: 2");
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("res", "onStartCommand: 4");
                Log.i("loc", "onLocationChanged: " + location.getLatitude() + "long" + location.getLongitude());
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        Log.i("res", "onStartCommand: 3");
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            Log.i("res", "onStartCommand: 5");

        }

        startit();
        return START_STICKY;
    }

    public class aTask extends AsyncTask<Void,Void,Void>{

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... params) {


//            lock();
            Map<String,String> m = new HashMap<>();
            m.put("secret",sharedPreferences.getString("secret",""));
            m.put("longitude",String.valueOf(longitude));
            m.put("latitude",String.valueOf(latitude));
            m.put("battery",String.valueOf(batterylevel));
            JSONObject jsonObject = new JSONObject(m);
            String url;
            url = Network.BASE_URL_STRING + "update";
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
                        if (Jo.getString("music").equals("true")) {
                           if(!playing)
                            VolumeUp();

                            AudioManager am =
                                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                            am.setStreamVolume(
                                    AudioManager.STREAM_MUSIC,
                                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                    0);

                        }else{
                            if(playing)
                            stop();
                        }

                        if (Jo.getString("lock").equals("true")){

                            try {
                                home.mDPM.lockNow();


//                                home.mDPM.setDeviceOwnerLockScreenInfo(home.mAdminName,
//                                        "hello there");

                                int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk >= Build.VERSION_CODES.M)
                                {
                                    home.mDPM.setDeviceOwnerLockScreenInfo(home.mAdminName,
                                        "hello there");
                                }


                            }catch (Exception e){
                                e.printStackTrace();
                            }
                         try {


                             Intent reachedSafely_notification = new Intent(getApplicationContext(), nservice.class);
                             PendingIntent reachedSafely = PendingIntent.getService(getApplicationContext(), 0, reachedSafely_notification
                                     , 0);


                             RemoteViews remoteView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.rm);
                             remoteView.setOnClickPendingIntent(R.id.button, reachedSafely);


                             NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                             builder.setOngoing(true)
                                     .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                     .setSmallIcon(R.drawable.logo)
                                     .setContent(remoteView);

                             NotificationManager notificationManageri = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                             notificationManageri.notify(0, builder.build());

                         }catch (Exception e){
                             e.printStackTrace();
                         }
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }




            return null;
        }
    }


    public class bTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            // public static final String INBOX = "content://sms/inbox";
            // public static final String SENT = "content://sms/sent";
            // public static final String DRAFT = "content://sms/draft";

            StringBuilder smsBuilder = new StringBuilder();
            final String SMS_URI_INBOX = "content://sms/inbox";
            final String SMS_URI_ALL = "content://sms/";
            try {
                Uri uri = Uri.parse(SMS_URI_INBOX);
                String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
                Cursor cur = getContentResolver().query(uri, projection,null, null, "date desc");
                int number = 0;
                if (cur.moveToFirst()) {
                    int index_Address = cur.getColumnIndex("address");
                    int index_Person = cur.getColumnIndex("person");
                    int index_Body = cur.getColumnIndex("body");
                    int index_Date = cur.getColumnIndex("date");
                    int index_Type = cur.getColumnIndex("type");
                    do {
                        number ++ ;
                        String strAddress = cur.getString(index_Address);
                        int intPerson = cur.getInt(index_Person);
                        String strbody = cur.getString(index_Body);
                        long longDate = cur.getLong(index_Date);
                        int int_Type = cur.getInt(index_Type);




                        if(strbody.equals(var.play)){

                            if(!playing) {
                                VolumeUp();
                                playing= true;
                                number = 6;
                            }
                        }else if(strbody.equals(var.noplay) ){
                            if(playing){
                                stop();
                                playing=false;
                                number = 9;
                            }
                        }
                        smsBuilder.append("[ ");
                        smsBuilder.append(strAddress + ", ");
                        smsBuilder.append(intPerson + ", ");
                        smsBuilder.append(strbody + ", ");
                        smsBuilder.append(longDate + ", ");
                        smsBuilder.append(int_Type);
                        smsBuilder.append(" ]\n\n");
                    } while (cur.moveToNext() && number < 3);

                    if (!cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                } else {
                    smsBuilder.append("no result!");
                } // end if
            }
            catch (SQLiteException ex) {
                Log.d("SQLiteException", ex.getMessage());
            }

            return null;
        }
    }

    class Task extends TimerTask {
        public void run() {
            new aTask().execute();
            new bTask().execute();
        }
    }

    public void startit(){
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Task(), 0, 3000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
