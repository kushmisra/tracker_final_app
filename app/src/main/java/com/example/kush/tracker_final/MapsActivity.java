package com.example.kush.tracker_final;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
//    SharedPreferences sharedPreferences;
    ArrayList<Double> latitudeA;
    ArrayList<Double> longitudeA;
    String secret = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        secret = getIntent().getStringExtra("secret");
        latitudeA = new ArrayList<>();
        longitudeA = new ArrayList<>();

//        sharedPreferences = this.getSharedPreferences("com.example.kush.tracker_final", Context.MODE_PRIVATE);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public class task extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            Map<String,String> m = new HashMap<>();
            m.put("secret",secret);
            JSONObject jsonObject = new JSONObject(m);
            String url;
            url = Network.BASE_URL_STRING + "getlocation";
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
                        latitudeA.clear();
                        longitudeA.clear();
                        for(int i=0;i<Jo.length();i++){
                            Double lat = Double.parseDouble(Jo.getJSONObject(String.valueOf(i)).getString("latitude"));
                            Double lon = Double.parseDouble(Jo.getJSONObject(String.valueOf(i)).getString("longitude"));
                            latitudeA.add(0,lat);
                            longitudeA.add(0,lon);
                        }
                    }else{

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mMap.clear();
            // Add a marker in Sydney and move the camera
            int i=0;
            for(i=0;i<latitudeA.size();i++) {
                LatLng sydney = new LatLng(latitudeA.get(i), longitudeA.get(i));
                mMap.addMarker(new MarkerOptions().position(sydney));

            }
            LatLng sydney = new LatLng(latitudeA.get(i-1), longitudeA.get(i-1));

            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_dialog_map)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudeA.get(latitudeA.size()-1),longitudeA.get(latitudeA.size()-1)),50));




            super.onPostExecute(aVoid);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        new task().execute();
    }
}
