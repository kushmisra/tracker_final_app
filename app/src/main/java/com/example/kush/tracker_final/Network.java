package com.example.kush.tracker_final;

/**
 * Created by kush on 15/4/17.
 */



import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by kush on 14/4/17.
 */

public class Network {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final String BASE_URL_STRING = "https://desolate-depths-12509.herokuapp.com/";


    public static Response getRequest(Context ctx, String url_string){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
//                    .addHeader()
                .url(url_string)
                .build();
        try{
            Response response = client.newCall(request).execute();
            if(response == null){
                Toast.makeText(ctx, "PLEASE CONNECT TO INTERNET", Toast.LENGTH_LONG).show();
            }
            return response;
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }


    public static Response postRequest(Context ctx, String json_string, String url_string){
        // TODO take care of ports in the constructor pf url
        OkHttpClient client = new OkHttpClient();
        try {
            RequestBody body = RequestBody.create(JSON, json_string);
            Request request = new Request.Builder()
                    .url(url_string)
                    .post(body)
                    .build();
            Log.i("res", "postRequest: going to request");
            Log.i("res", "postRequest: "+url_string);
            Response response = client.newCall(request).execute();
            return response;

//            if(response == null){
//                //Toast.makeText(ctx, "PLEASE CONNECT TO INTERNET", Toast.LENGTH_LONG).show();
////                Log.i("res", "postRequest: error");
//            }
//            return response;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }




}